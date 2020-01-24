package org.osivia.sample.transaction.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

/**
 * Transaction repository interface.
 *
 * @author Jean-Sébastien Steux
 */
public interface TransactionRepository {

    /**
     * Get transaction configuration.
     *
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set transaction configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration transaction configuration
     * @throws PortletException
     */
    void setConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException;


    Document createOne(PortalControllerContext portalControllerContext) throws PortletException;

    
    Document delete(PortalControllerContext portalControllerContext) throws PortletException;
  


    Document createBlob(PortalControllerContext portalControllerContext) throws PortletException;
    
    Document createBlobs(PortalControllerContext portalControllerContext) throws PortletException;
    
    Document fetchPublicationInfo(PortalControllerContext portalControllerContext) throws PortletException;
    
 
    Document getTask(PortalControllerContext portalControllerContext, String uuid) throws PortletException;


    
    // Implicits transaction
    Document createAndUpdate(PortalControllerContext portalControllerContext, PersonUpdateService personUpdateService) throws PortletException;


    void init(PortalControllerContext portalControllerContext, PersonUpdateService personUpdateService) throws PortletException;


    Document createFile(PortalControllerContext portalControllerContext, String suffix, boolean exception) throws PortletException;


    List<Document> getTasks(PortalControllerContext portalControllerContext) throws PortletException;



    void initReminderUserTasks(PortalControllerContext portalControllerContext) throws PortletException;


    void startReminderTask(PortalControllerContext portalControllerContext) throws PortletException;

}
