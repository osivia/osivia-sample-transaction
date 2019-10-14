package org.osivia.sample.transaction.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
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


    CommandNotification createOne(PortalControllerContext portalControllerContext) throws PortletException;
    
    CommandNotification createSeveral(PortalControllerContext portalControllerContext) throws PortletException;
    
    CommandNotification createAndUpdate(PortalControllerContext portalControllerContext) throws PortletException;
  
    CommandNotification createAndUpdateTx2(PortalControllerContext portalControllerContext) throws PortletException;

    CommandNotification createAndRollback(PortalControllerContext portalControllerContext) throws PortletException;
    
    CommandNotification deleteAndRollback(PortalControllerContext portalControllerContext) throws PortletException;
  
    CommandNotification createFile(PortalControllerContext portalControllerContext, String suffix) throws PortletException;

    CommandNotification createBlob(PortalControllerContext portalControllerContext) throws PortletException;
    
    CommandNotification createBlobs(PortalControllerContext portalControllerContext) throws PortletException;
    
    CommandNotification fetchPublicationInfo(PortalControllerContext portalControllerContext) throws PortletException;
    
    CommandNotification updateAndRollback(PortalControllerContext portalControllerContext) throws PortletException;

    Document getTask(PortalControllerContext portalControllerContext, String uuid) throws PortletException;


}
