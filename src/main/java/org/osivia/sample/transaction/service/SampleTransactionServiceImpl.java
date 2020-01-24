package org.osivia.sample.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.portlet.PortletException;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.transaction.ITransactionService;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.osivia.sample.transaction.repository.TransactionRepository;
import org.osivia.sample.transaction.repository.TransactionRepositoryImpl;
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
    
    private static Logger logger = Logger.getLogger(SampleTransactionServiceImpl.class);


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

    
    private CommandNotification manageException(String operation, Exception e)    {
        logger.error("error during "+ operation+ " :" + e.getMessage(), e);
        
        CommandNotification notif = new CommandNotification(false, e.getMessage());
        
        if (transactionService.isStarted())
            transactionService.rollback();
        
        return notif;
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
            notif = manageException("createOne", e);
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
            notif = manageException("deleteAndRollback", e);

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
            notif = manageException("createBlob", e);

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
            this.repository.createFile(portalControllerContext, suffix, false);
            transactionService.commit();
            notif = new CommandNotification(true, "createFile");
        } catch (Exception e) {
            notif = manageException("createFile", e);
        }
        return notif;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createFileOutTrans(PortalControllerContext portalControllerContext, String suffix) throws PortletException {

        CommandNotification notif;

        try {
            this.repository.createFile(portalControllerContext, suffix, false);

            notif = new CommandNotification(true, "createFile");
        } catch (Exception e) {
            notif = manageException("createFileOutTrans", e);
        }
        return notif;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandNotification createFileException(PortalControllerContext portalControllerContext, String suffix) throws PortletException {

        CommandNotification notif;
        transactionService.begin();
        try {
            this.repository.createFile(portalControllerContext, suffix, true);
            transactionService.commit();
            notif = new CommandNotification(true, "createFile");
        } catch (Exception e) {
            notif = manageException("createFileException", e);
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
            notif = manageException("createBlobs", e);
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
            notif = manageException("fetchPublicationInfo", e);
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
        } catch (Exception e) {
            commandNotification = manageException("updateAndRollback", e);

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
        } catch (Exception e) {
            commandNotification = manageException("updateAndCommit", e);

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
        } catch (Exception e) {
            commandNotification = manageException("updateWithoutCommit", e);

        }
        return commandNotification;
    }



    @Override
    public CommandNotification reminderStartCommit(PortalControllerContext portalControllerContext) throws PortletException {

        // Internationalization bundle

        CommandNotification commandNotification;

        try {
            transactionService.begin();
            this.repository.startReminderTask(portalControllerContext);
            transactionService.commit();

            commandNotification = new CommandNotification(true, "procedure started");
        } catch (Exception e) {
            commandNotification = manageException("reminderStartCommit", e);
        }

        return commandNotification;
    }
    
    

    @Override
    public CommandNotification reminderStartNoTrans(PortalControllerContext portalControllerContext) throws PortletException {

        // Internationalization bundle

        CommandNotification commandNotification;

        try {

            this.repository.startReminderTask(portalControllerContext);

            commandNotification = new CommandNotification(true, "procedure started");
        } catch (Exception e) {
            commandNotification = manageException("reminderStartCommit", e);
        }

        return commandNotification;
    }
    
    @Override
    public CommandNotification reminderStartRollback(PortalControllerContext portalControllerContext) throws PortletException {

        CommandNotification commandNotification;

        try {
 
            transactionService.begin();
            this.repository.startReminderTask(portalControllerContext);
            transactionService.rollback();

            commandNotification = new CommandNotification(true, "procedure rollback");


        } catch (Exception e) {
            commandNotification = manageException("reminderStartRollback", e);
        }

        return commandNotification;
    }


    @Override
    public CommandNotification init(PortalControllerContext portalControllerContext) throws PortletException {
        CommandNotification commandNotification;


        try {
            this.repository.init(portalControllerContext, personUpdateService);
            this.repository.initReminderUserTasks(portalControllerContext);

            commandNotification = new CommandNotification(true, "init");


        } catch (Exception e) {
            commandNotification = manageException("init", e);
        }


        return commandNotification;


    }

}
