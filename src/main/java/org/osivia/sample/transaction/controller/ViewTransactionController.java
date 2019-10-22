package org.osivia.sample.transaction.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.osivia.sample.transaction.service.SampleTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View transaction controller.
 *
 * @author Jean-Sébastien Steux
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewTransactionController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Generator service. */
    @Autowired
    private SampleTransactionService service;
    
    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public ViewTransactionController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @return error path
     * @throws PortletException
     */
    @ExceptionHandler
    public String exceptionHandler(PortletRequest request, PortletResponse response) throws PortletException {
        return "error";
    }

    /**
     * Get generator configuration model attribute.
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
     * Create one document action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createone")
    public void createOne(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createOne(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * Create several documents action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createseveral")
    public void createSeveral(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createSeveral(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * Create and update document action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createandupdate")
    public void createAndUpdate(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createAndUpdate(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    
    /**
     * Create and update document action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createandupdatetx2")
    public void createAndUpdateTx2(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createAndUpdateTx2(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * Create and rollback action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createandrollback")
    public void createAndRollback(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        CommandNotification commandNotification = this.service.createAndRollback(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * Delete and rollback action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "deleteandrollback")
    public void deleteAndRollback(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.deleteAndRollback(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * Create blob action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createblob")
    public void createBlob(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createBlob(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    
    /**
     * Create file action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createfile")
    public void createFile(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createFile(portalControllerContext, request.getParameter("suffix"));
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
 
        
        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);
        
        // Update public render parameter for associated portlets refresh
        response.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));

    }
        
    /**
     * Create blobs action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "createblobs")
    public void createBlobs(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.createBlobs(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * FetchPublicationInfo action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "fetchPublicationInfo")
    public void fetchPublicationInfo(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.fetchPublicationInfo(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    /**
     * Update and rollback action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "updateAndRollback")
    public void updateAndRollback(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.updateAndRollback(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    
    /**
     * Update and rollback action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "reminder")
    public void reminder(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CommandNotification commandNotification = this.service.reminder(portalControllerContext);
        this.notificationsService.addSimpleNotification(portalControllerContext, commandNotification.getMsgReturn(), commandNotification.isSuccess()? NotificationsType.SUCCESS : NotificationsType.WARNING);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
