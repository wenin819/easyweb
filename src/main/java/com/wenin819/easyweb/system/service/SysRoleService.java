package com.wenin819.easyweb.system.service;

import com.wenin819.easyweb.core.persistence.mybatis.CriteriaQuery;
import com.wenin819.easyweb.core.persistence.mybatis.MybatisBaseDao;
import com.wenin819.easyweb.core.service.mybatis.MybatisBaseService;
import com.wenin819.easyweb.core.utils.ConfigEnum;
import com.wenin819.easyweb.core.utils.ConfigUtils;
import com.wenin819.easyweb.core.utils.SecurityUtils;
import com.wenin819.easyweb.system.dao.SysRoleDao;
import com.wenin819.easyweb.system.model.SysMenu;
import com.wenin819.easyweb.system.model.SysRole;
import com.wenin819.easyweb.system.model.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author wenin819@gmail.com
 */
@Service
public class SysRoleService extends MybatisBaseService<SysRole> {

    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public String getTableName() {
        return ConfigUtils.get().getValue("schema.configPlat") + ".sys_role";
    }

    @Override
    public String getIdKey() {
        return "id";
    }

    @Override
    public MybatisBaseDao<SysRole> getDao() {
        return sysRoleDao;
    }

    @Override
    public CriteriaQuery genCriteriaQuery(SysRole entity) {
        CriteriaQuery query = super.genCriteriaQuery(entity);
        query.createAndCriteria().equalTo(SysRole.TE.delFlag, ConfigEnum.DEL_FLAG_NORMAL);
        return query;
    }
    /**
     * 查询角色菜单ID列表
     * @param role 角色
     * @return
     */
    public List<String> queryMenuIdsByRole(SysRole role) {
        if(null == role) {
            return Collections.EMPTY_LIST;
        }
        return sysRoleDao.queryMenuIdsByRole(role);
    }

    /**
     * 查询用户角色ID列表
     * @param user
     * @return
     */
    public List<String> queryRoleIdsByUser(SysUser user) {
        if(null == user) {
            return Collections.EMPTY_LIST;
        }
        return sysRoleDao.queryRoleIdsByUser(user);
    }

    /**
     * 保存角色与菜单关系
     * @param role 角色
     * @param menuIds 菜单标识列表
     */
    public void saveRoleMenuRelations(SysRole role, List<String> menuIds) {
        Assert.notNull(role, "role不能为空");
        sysRoleDao.deleteMenuRelationByRole(role);
        if(null != menuIds) {
            for (String menuId : menuIds) {
                SysRoleDao.RoleMenuRelation relation = new SysRoleDao.RoleMenuRelation(role.getId(), menuId);
                updateCreateEntity(relation);
                sysRoleDao.insertRoleMenuRelation(relation);
            }
        }
        SecurityUtils.updateMenuCache();
    }

    /**
     * 保存角色与用户关系
     * @param role 角色
     * @param userIds 用户标识列表
     */
    public void saveRoleUserRelations(SysRole role, List<String> userIds) {
        Assert.notNull(role, "role不能为空");
        sysRoleDao.deleteUserRelationByRole(role);
        if(null != userIds) {
            for (String userId : userIds) {
                SysRoleDao.RoleUserRelation relation = new SysRoleDao.RoleUserRelation(role.getId(), userId);
                updateCreateEntity(relation);
                sysRoleDao.insertRoleUserRelation(relation);
            }
        }
    }

    /**
     * 保存角色用户关系
     * @param user
     * @param roleIds
     */
    public void saveRoleUserRelations(SysUser user, List<String> roleIds) {
        Assert.notNull(user, "user不能为空");
        sysRoleDao.deleteUserRelationByUser(user);
        if(null != roleIds) {
            for (String roleId : roleIds) {
                SysRoleDao.RoleUserRelation relation = new SysRoleDao.RoleUserRelation(roleId, user.getId());
                updateCreateEntity(relation);
                sysRoleDao.insertRoleUserRelation(relation);
            }
        }
    }

    /**
     * 查询所有角色
     * @return
     */
    public List<SysRole> queryAllRoles() {
        CriteriaQuery query = new CriteriaQuery();
        query.createAndCriteria().equalTo(SysRole.TE.delFlag, ConfigEnum.DEL_FLAG_NORMAL);
        return sysRoleDao.queryByCriteria(query);
    }

    /**
     * 查询用户对应的所有角色
     * @param user
     * @return
     */
    public List<SysRole> queryMyRoles(SysUser user) {
        if(null == user) {
            return Collections.EMPTY_LIST;
        } else {
            return sysRoleDao.queryMyRoles(user);
        }
    }

    /**
     * 检查角色有效性
     * @param entity 角色
     * @return 有效是返回null，其它返回原因
     */
    @Override
    public String validate(SysRole entity) {
        CriteriaQuery query = new CriteriaQuery();
        if(null != entity.getId()) {
            query.createAndCriteria().notEqualTo(SysRole.TE.id, entity.getId());
        }
        String msg;
        if(null != entity.getName()) {
            query.createAndCriteria().equalTo(SysRole.TE.name, entity.getName());
            msg = "名称";
        } else if(null != entity.getCode()) {
            query.createAndCriteria().equalTo(SysRole.TE.code, entity.getCode());
            msg = "编码";
        } else {
            return "名称和编码不能为空";
        }
        if(0 < getDao().countByCriteria(query)) {
            return msg + "重复";
        }
        List<String> menuIds = entity.getMenuIds();
        if(null != menuIds) {
            List<SysMenu> allMenu = SecurityUtils.getAllMenu();
            Set<String> myMenuIds = new HashSet<String>();
            for (SysMenu sysMenu : allMenu) {
                myMenuIds.add(sysMenu.getId());
            }
            for (String menuId : menuIds) {
                if(!myMenuIds.contains(menuId)) {
                    return "保存的菜单列表，超过您的权限范围，请重新选择";
                }
            }
        }
        return super.validate(entity);
    }
}
