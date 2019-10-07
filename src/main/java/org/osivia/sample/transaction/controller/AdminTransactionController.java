package org.osivia.sample.transaction.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.sample.transaction.model.Configuration;
import org.osivia.sample.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Admin transaction controller.
 *
 * @author Jean-Sébastien Steux
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "ADMIN")
public class AdminTransactionController implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;

    /** transaction service. */
    @Autowired
    private TransactionService service;


    /**
     * Constructor.
     */
    public AdminTransactionController() {
        super();
    }


    /**
     * Admin render mapping.
     * @param request render request
     * @param response render response
     * @return admin path
     */
    @RenderMapping
    public String admin(RenderRequest request, RenderResponse response) {
        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param configuration transaction configuration
     * @throws PortletException
     */
    @ActionMapping(value = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute(value = "configuration") Configuration configuration)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.saveConfiguration(portalControllerContext, configuration);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get transaction configuration model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return configuration
     * @throws PortletException
     */
    @ModelAttribute(value = "configuration")
    public Configuration getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getConfiguration(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
