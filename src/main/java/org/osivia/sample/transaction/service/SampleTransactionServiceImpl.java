package org.osivia.sample.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.transaction.ITransactionService;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.osivia.sample.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Generator service implementation.
 *
 * @author Cédric Krommenhoek
 * @see GeneratorService
 */
@Service
public class SampleTransactionServiceImpl implements SampleTransactionService {

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

    /** Transaction service. */
    @Autowired
    public ITransactionService transactionService;


    @Autowired
    @Qualifier("personUpdateService")
    private PersonUpdateService personUpdateService;

    private static final String REMINDER_MODEL_ID = "reminder";

    /**
     * Constructor.
     *
     * @throws NamingException
     */
    public SampleTransactionServiceImpl() throws NamingException {
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
        CommandNotification notif;

        transactionService.begin();
        try {
            this.repository.createOne(portalControllerContext);
            transactionService.commit();
            notif = new CommandNotification(true, "createOne");
        } catch (Exception e) {
            transactionService.rollback();
            notif = new CommandNotification(false, e.getMessage());
        }
        return notif;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createSeveral(PortalControllerContext portalControllerContext) throws PortletException {
        return new CommandNotification(false, "Not Yet");

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createAndRollback(PortalControllerContext portalControllerContext) throws PortletException {
        return new CommandNotification(false, "Not Yet");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification deleteAndRollback(PortalControllerContext portalControllerContext) throws PortletException {


        CommandNotification notif;

        transactionService.begin();
        try {
            this.repository.delete(portalControllerContext);
            transactionService.rollback();
            notif = new CommandNotification(true, "deleteAndRoolback");
        } catch (Exception e) {

            notif = new CommandNotification(false, e.getMessage());
        }
        return notif;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createBlob(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification notif;

        transactionService.begin();
        try {
            this.repository.createBlob(portalControllerContext);
            transactionService.commit();
            notif = new CommandNotification(true, "createBlob");
        } catch (Exception e) {
            transactionService.rollback();
            notif = new CommandNotification(false, e.getMessage());
        }
        return notif;


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createFile(PortalControllerContext portalControllerContext, String suffix) throws PortletException {

        CommandNotification notif;
        transactionService.begin();
        try {
            this.repository.createFile(portalControllerContext, suffix);
            transactionService.commit();
            notif = new CommandNotification(true, "createBlob");
        } catch (Exception e) {
            transactionService.rollback();
            notif = new CommandNotification(false, e.getMessage());
        }
        return notif;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createBlobs(PortalControllerContext portalControllerContext) throws PortletException {

        CommandNotification notif;
        transactionService.begin();
        try {
            this.repository.createBlobs(portalControllerContext);
            transactionService.commit();
            notif = new CommandNotification(true, "createBlobs");
        } catch (Exception e) {
            transactionService.rollback();
            notif = new CommandNotification(false, e.getMessage());
        }
        return notif;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification fetchPublicationInfo(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification notif;
        transactionService.begin();
        try {
            this.repository.fetchPublicationInfo(portalControllerContext);
            transactionService.commit();
            notif = new CommandNotification(true, "createBlobs");
        } catch (Exception e) {
            transactionService.rollback();
            notif = new CommandNotification(false, e.getMessage());
        }
        return notif;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification updateAndRollback(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification commandNotification;
        try {

            transactionService.begin();
            this.repository.createAndUpdate(portalControllerContext, personUpdateService);
            transactionService.rollback();
            commandNotification = new CommandNotification(true, "update + user created");
        } catch (PortletException pe) {
            commandNotification = new CommandNotification(false, "error, cause:" + pe.toString());
            throw new PortletException(pe);
        }
        return commandNotification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification updateAndCommit(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification commandNotification;
        try {

            transactionService.begin();
            this.repository.createAndUpdate(portalControllerContext, personUpdateService);
            transactionService.commit();
            commandNotification = new CommandNotification(true, "update + user created");
        } catch (PortletException pe) {
            commandNotification = new CommandNotification(false, "error, cause:" + pe.toString());
            throw new PortletException(pe);
        }
        return commandNotification;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification updateWithoutCommit(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification commandNotification;
        try {

            transactionService.begin();
            this.repository.createAndUpdate(portalControllerContext, personUpdateService);
            commandNotification = new CommandNotification(true, "update + user created - no commit");
        } catch (PortletException pe) {
            commandNotification = new CommandNotification(false, "error, cause:" + pe.toString());
            throw new PortletException(pe);
        }
        return commandNotification;
    }


    @Override
    public CommandNotification reminder(PortalControllerContext portalControllerContext) throws PortletException {


        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        CommandNotification commandNotification;

        NuxeoController nuxeoCtrl = new NuxeoController(portalControllerContext);

        try {
            //
            // Start procedure
            //
            
            transactionService.begin();

            Map<String, String> variables = new HashMap<>();

            variables.put("recipient", portalControllerContext.getRequest().getRemoteUser());

            // Start
            String modelWebId = IFormsService.FORMS_WEB_ID_PREFIX + REMINDER_MODEL_ID;
            Map<String, String> returnMap = this.formsService.start(portalControllerContext, modelWebId, variables);

            returnMap.get("uuid");



            transactionService.commit();
            
            
            commandNotification = new CommandNotification(true, "procedure started");
            
            
            //
            // Get task from ES
            //

            Document task = null;
            // Task documents
            List<EcmDocument> documents;
            try {


                task = this.repository.getTask(portalControllerContext, returnMap.get("uuid"));
            } catch (PortletException e) {
                throw new PortletException(e);
            }
                        
            /*
            transactionService.begin();
            


            //
            // Create file
            //

            String user = portalControllerContext.getRequest().getRemoteUser();

            Document userWorkspace = (Document) nuxeoCtrl.executeNuxeoCommand(new GetUserProfileCommand(user));
            String rootPath = userWorkspace.getPath().substring(0, userWorkspace.getPath().lastIndexOf('/')) + "/documents";
            nuxeoCtrl.executeNuxeoCommand(new SampleCreationCommand(rootPath));


            commandNotification = new CommandNotification(true, "procedure created");


            //
            // Update procedure
            //


            variables = new HashMap<>();

            if (task != null) {
                this.formsService.proceed(portalControllerContext, task, variables);


                // Notification

                commandNotification = new CommandNotification(true, "procedure created and deleted");
            } else
                commandNotification = new CommandNotification(false, "no task found");


            // transactionService.commit();

            transactionService.rollback();
            
            */

        } catch (Exception e) {

            if( transactionService.isStarted())
                transactionService.rollback();
            commandNotification = new CommandNotification(false, "error, cause:" + e.toString());
        }

        return commandNotification;
    }


    @Override
    public CommandNotification init(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification commandNotification;
           this.repository.init(portalControllerContext, personUpdateService);
            commandNotification = new CommandNotification(true, "init");
         return commandNotification;
        
        

    }

}
