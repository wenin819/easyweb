package com.wenin819.easyweb.modules.contacts.controller;

import com.wenin819.easyweb.core.db.BaseService;
import com.wenin819.easyweb.core.db.CriteriaQuery;
import com.wenin819.easyweb.core.web.BaseEntityController;
import com.wenin819.easyweb.modules.contacts.model.TxContacts;
import com.wenin819.easyweb.modules.contacts.service.TxContactsService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wenin819@gmail.com on 2014-09-02.
 */
@Controller
@RequestMapping("/contacts/")
public class TxContactsController extends BaseEntityController<TxContacts> {
    @Override
    protected CriteriaQuery genCriteriaes(TxContacts entity, HttpServletRequest request) {
        final CriteriaQuery criteriaQuery = super.genCriteriaes(entity, request);
        criteriaQuery.createAndCriteria().isNotNull(TxContacts.TE.cellphone);
        return criteriaQuery;
    }

    @Resource
    private TxContactsService txContactsService;

    @Override
    protected String getBasePagePath() {
        return "contacts/contact";
    }

    @Override
    protected BaseService<TxContacts> getService() {
        return txContactsService;
    }
}