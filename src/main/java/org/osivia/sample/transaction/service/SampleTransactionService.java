package org.osivia.sample.transaction.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

/**
 * Transaction service interface.
 *
 * @author Jean-Sébastien Steux
 */
public interface SampleTransactionService {

    /**
     * Get transaction configuration.
     *
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save transaction configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration transaction configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException;

    /**
     * Create 1 document 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification createOne(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Create several documents
     * 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification createSeveral(PortalControllerContext portalControllerContext) throws PortletException;
    
    

    
    /**
     * Create and rollback
     * 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification createAndRollback(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Delete and rollback
     * 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification deleteAndRollback(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Create blob
     * 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification createBlob(PortalControllerContext portalControllerContext) throws PortletException;

    
    
    /**
     * Create blobs
     * 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification createBlobs(PortalControllerContext portalControllerContext) throws PortletException;
    
    
    /**
     * FetchPublicationInfo
     * 
     * @param portalControllerContext
     * @throws PortletException
     */
    CommandNotification fetchPublicationInfo(PortalControllerContext portalControllerContext) throws PortletException;
    
     



    CommandNotification updateAndRollback(PortalControllerContext portalControllerContext) throws PortletException;
    CommandNotification updateAndCommit(PortalControllerContext portalControllerContext) throws PortletException;


    CommandNotification updateWithoutCommit(PortalControllerContext portalControllerContext) throws PortletException;


    CommandNotification init(PortalControllerContext portalControllerContext) throws PortletException;

    CommandNotification createFile(PortalControllerContext portalControllerContext, String suffix) throws PortletException;
    CommandNotification createFileOutTrans(PortalControllerContext portalControllerContext, String suffix) throws PortletException;


    CommandNotification createFileException(PortalControllerContext portalControllerContext, String suffix) throws PortletException;


    CommandNotification reminderStartCommit(PortalControllerContext portalControllerContext) throws PortletException;
    CommandNotification reminderStartRollback(PortalControllerContext portalControllerContext) throws PortletException;


    CommandNotification reminderStartNoTrans(PortalControllerContext portalControllerContext) throws PortletException;


    CommandNotification importFiles(PortalControllerContext portalControllerContext) throws PortletException;


    CommandNotification createFolder(PortalControllerContext portalControllerContext) throws PortletException;
    
}
