package com.wenin819.easyweb.system.controller;

import com.wenin819.easyweb.core.persistence.Page;
import com.wenin819.easyweb.core.service.mybatis.MybatisBaseService;
import com.wenin819.easyweb.core.utils.SecurityUtils;
import com.wenin819.easyweb.core.utils.WebUtils;
import com.wenin819.easyweb.core.web.BaseCrudController;
import com.wenin819.easyweb.system.model.SysRole;
import com.wenin819.easyweb.system.service.SysMenuService;
import com.wenin819.easyweb.system.service.SysRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;

/**
 * @author wenin819@gmail.com
 */
@Controller
@RequestMapping(SysRoleController.BASE_URL)
public class SysRoleController extends BaseCrudController<SysRole> {

    public static final String BASE_URL = "/system/SysRole/";
    private static final String BASE_PATH = "system/SysRole";

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMenuService sysMenuService;

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    protected String getBasePagePath() {
        return BASE_PATH;
    }

    @Override
    protected String getBasePermission() {
        return "system:SysRole";
    }

    @Override
    protected MybatisBaseService<SysRole> getService() {
        return sysRoleService;
    }

    @Override
    public String toList(Page<SysRole> page, SysRole entity, Model model, HttpServletRequest request) {
        checkPermission("view");
        model.addAttribute(WebUtils.ENTRY, entity);
        page.addAll(SecurityUtils.getAllRole());
        model.addAttribute(WebUtils.PAGE, page);
        return pagePathList;
    }

    @Override
    public String toForm(boolean autoQuery, SysRole entry, Model model, HttpServletRequest request) {
        String toForm = super.toForm(autoQuery, entry, model, request);
        entry = (SysRole) model.asMap().get(WebUtils.ENTRY);

        entry.setMenuIds(sysRoleService.queryMenuIdsByRole(entry));
        model.addAttribute("menus", SecurityUtils.getAllMenu());
        return toForm;
    }

    @Override
    @Transient
    public String save(SysRole entity, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String save = super.save(entity, model, redirectAttributes, request);
        if(redirect2ListPage.equals(save)) {
            sysRoleService.saveRoleMenuRelations(entity, entity.getMenuIds());
        }
        return save;
    }
}
