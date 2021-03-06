package com.wenin819.easyweb.core.web;

import com.wenin819.easyweb.core.persistence.BaseEntity;
import com.wenin819.easyweb.core.persistence.Page;
import com.wenin819.easyweb.core.persistence.mybatis.CriteriaQuery;
import com.wenin819.easyweb.core.service.mybatis.MybatisBaseService;
import com.wenin819.easyweb.core.utils.SecurityUtils;
import com.wenin819.easyweb.core.utils.WebUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wenin819@gmail.com on 2014-09-02.
 */
public abstract class BaseCrudController<E extends BaseEntity> extends BaseController {


    protected abstract String getBaseUrl();

    /**
     * 得到页面路径
     * @return
     */
    protected abstract String getBasePagePath();

    /**
     * 得到基础权限标识
     * @return
     */
    protected String getBasePermission() {
        return null;
    }

    /**
     * 查询页面路径
     */
    protected final String pagePathList = getBasePagePath() + "List";
    /**
     * 查看修改页面路径
     */
    protected final String pagePathForm = getBasePagePath() + "Form";
    /**
     * 跳转到列表页面的路径
     */
    protected final String redirect2ListPage = "redirect:list.html";
    /**
     * 得到对应service实现
     * @return
     */
    protected abstract MybatisBaseService<E> getService();

    /**
     * 检查权限
     * @param subPerm
     */
    protected void checkPermission(String subPerm) {
        if(null == getBasePermission()) {
            return;
        }
        SecurityUtils.getSubject().checkPermission(getBasePermission() + (null != subPerm ? ":" + subPerm : ""));
    }

    /**
     * 生成分布查询条件
     * @param entity
     * @param request
     * @param model
     * @return
     */
    protected CriteriaQuery genCriteriaQuery(E entity, HttpServletRequest request, Model model) {
        return getService().genCriteriaQuery(entity);
    }

    /**
     * 更新操作实体
     * @param entity    实体
     * @param type  操作类型
     * @param request
     * @param model
     * @return
     */
    protected E updateEntity(E entity, ActionType type, HttpServletRequest request, Model model) {
        return entity;
    }

    /**
     * 跳转分页查询页面
     * @param page  分页信息
     * @param entity
     * @param model
     * @param request
     * @return
     */
    @RequestMapping({"list", ""})
    public String toList(Page<E> page, E entity, Model model, HttpServletRequest request) {
        checkPermission("view");
        model.addAttribute(WebUtils.ENTRY, entity);
        entity = updateEntity(entity, ActionType.SELECT, request, model);
        CriteriaQuery example = genCriteriaQuery(entity, request, model);
        page = getService().queryPage(example, page);
        model.addAttribute(WebUtils.PAGE, page);
        return pagePathList;
    }

    /**
     * 跳转查看修改页面
     * @param entry
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("form")
    public String toForm(E entry, Model model, HttpServletRequest request) {
        return toForm(true, entry, model, request);
    }

    /**
     * 跳转查看修改页面
     * @param autoQuery 是否自动查询实体
     * @param entry
     * @param model
     * @param request
     * @return
     */
    protected String toForm(boolean autoQuery, E entry, Model model, HttpServletRequest request) {
        checkPermission("view");
        E entity;
        if(!autoQuery || null == entry.getId() || null == (entity = getService().queryById(entry.getId()))) {
            model.addAttribute(WebUtils.ENTRY, entry);
        } else {
            model.addAttribute(WebUtils.ENTRY, entity);
        }
        return pagePathForm;
    }

    /**
     * 增加或修改处理
     * @param entity 实体
     * @param model
     * @param request
     * @return 正常处理返回 redirect2ListPage, 否则返回 pagePathForm
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(E entity, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        checkPermission("edit");
        String errMsg = getService().validate(entity);
        if(null != errMsg) {
            addMessages(model, "保存失败，" + errMsg);
            return toForm(false, entity, model, request);
        }
        entity = updateEntity(entity, ActionType.SAVE, request, model);
        final int success = getService().save(entity);
        if(success > 0) {
            addMessages(redirectAttributes, "保存成功");
            return redirect2ListPage;
        } else {
            addMessages(model, "保存失败，请重试");
            return toForm(false, entity, model, request);
        }
    }

    @RequestMapping("delete")
    public String delete(E entity, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        checkPermission("edit");
        final int success = getService().delete(entity);
        if(success > 0) {
            addMessages(redirectAttributes, "删除成功");
        } else {
            addMessages(redirectAttributes, "删除失败，请重试");
        }
        return redirect2ListPage;
    }

    @RequestMapping("validate")
    @ResponseBody
    public boolean validate(E entity) {
        return null == getService().validate(entity);
    }
}
