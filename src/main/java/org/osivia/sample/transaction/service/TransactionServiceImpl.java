package org.osivia.sample.transaction.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.tasks.ITasksService;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.osivia.sample.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Generator service implementation.
 *
 * @author Cédric Krommenhoek
 * @see GeneratorService
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    /** Transaction repository. */
    @Autowired
    private TransactionRepository repository;


    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Forms service. */
    @Autowired
    private IFormsService formsService;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;



    private static final String REMINDER_MODEL_ID = "reminder";

    /**
     * Constructor.
     *
     * @throws NamingException
     */
    public TransactionServiceImpl() throws NamingException {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getConfiguration(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException {
        this.repository.setConfiguration(portalControllerContext, configuration);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createOne(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createOne(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createSeveral(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createSeveral(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createAndUpdate(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createAndUpdate(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createAndUpdateTx2(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createAndUpdateTx2(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createAndRollback(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createAndRollback(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification deleteAndRollback(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.deleteAndRollback(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createBlob(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createBlob(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createFile(PortalControllerContext portalControllerContext, String suffix) throws PortletException {
        return this.repository.createFile(portalControllerContext, suffix);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createBlobs(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.createBlobs(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification fetchPublicationInfo(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.fetchPublicationInfo(portalControllerContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification updateAndRollback(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.updateAndRollback(portalControllerContext);

    }


    @Override
    public CommandNotification reminder(PortalControllerContext portalControllerContext) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        CommandNotification commandNotification;

        try {

            // Variables
            Map<String, String> variables = new HashMap<>();

            variables.put("recipient", portalControllerContext.getRequest().getRemoteUser());

            // Start
            String modelWebId = IFormsService.FORMS_WEB_ID_PREFIX + REMINDER_MODEL_ID;
            Map<String, String> returnMap = this.formsService.start(portalControllerContext, modelWebId, variables);

            returnMap.get("uuid");


            /* Get task from ES */

            Document task = null;
            // Task documents
            List<EcmDocument> documents;
            try {
                task = this.repository.getTask(portalControllerContext, returnMap.get("uuid"));
            } catch (PortletException e) {
                throw new PortletException(e);
            }


            // Variables
            variables = new HashMap<>();

            if (task != null) {
                this.formsService.proceed(portalControllerContext, task, variables);


                // Notification

                commandNotification = new CommandNotification(true, "procedure created and deleted");
            }
            else commandNotification = new CommandNotification(false, "no task found");

        } catch (Exception e) {
            commandNotification = new CommandNotification(false, "error, cause:" + e.toString());

        }

        return commandNotification;
    }
}
