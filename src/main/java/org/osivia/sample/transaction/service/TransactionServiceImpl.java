package org.osivia.sample.transaction.service;

import javax.naming.NamingException;
import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.osivia.sample.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
