package org.osivia.sample.transaction.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.naming.NamingException;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import org.osivia.sample.transaction.repository.command.CreateBlobCommand;
import org.osivia.sample.transaction.repository.command.CreateBlobsCommand;
import org.osivia.sample.transaction.repository.command.CreateFileCommand;
import org.osivia.sample.transaction.repository.command.DeleteCommand;
import org.osivia.sample.transaction.repository.command.FetchPublicationInfoCommand;
import org.osivia.sample.transaction.repository.command.GetTasksCommand;
import org.osivia.sample.transaction.repository.command.InitCommand;
import org.osivia.sample.transaction.repository.command.OneCreationCommand;
import org.osivia.sample.transaction.repository.command.UpdateCommand;
import org.osivia.sample.transaction.service.GetUserProfileCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;


/**
 * Transaction repository implementation.
 *
 * @author Jean-Sébastien Steux
 * @see TransactionRepository
 */
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

	private static Logger LOGGER = Logger.getLogger(TransactionRepositoryImpl.class);

    private static final String PORTLET_TRANSACTION_CONFIGURATION = "portlet.transaction.configuration";
    /** Generator properties file name. */
    private static final String PROPERTIES_FILE_NAME = "transaction.properties";
	/** Path. */
	private static final String PATH_PROPERTY = "transaction.path";
	/** Number of spaces. */
    private static final String WEBID = "transaction.webid";
    
 
    

    /** Generator properties. */
    private final Properties properties;
    
    
    
    /** Forms service. */
    @Autowired
    private IFormsService formsService;
    
    /**
     * Constructor.
     *
     * @throws IOException
     * @throws NamingException
     */
    public TransactionRepositoryImpl() throws IOException, NamingException {
        super();

        // Generator properties
        this.properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (inputStream != null) {
            this.properties.load(inputStream);
        } else {
            throw new FileNotFoundException(PROPERTIES_FILE_NAME);
        }


    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        //Configuration
        Configuration configuration = (Configuration) portalControllerContext.getRequest().getPortletSession().getAttribute(PORTLET_TRANSACTION_CONFIGURATION);
        if (configuration == null)
        {
            configuration = new Configuration();

//            //int nbOfworkspaces = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_WORKSPACES_PROPERTY), "10");
//            String path = StringUtils.defaultIfEmpty(window.getProperty(PATH_PROPERTY),
//                    this.properties.getProperty(PATH_PROPERTY));

            String webid = StringUtils.defaultIfEmpty(window.getProperty(WEBID),
                    this.properties.getProperty(WEBID));


//            configuration.setPath(path);
            configuration.setWebid(webid);
            portalControllerContext.getRequest().getPortletSession().setAttribute(PORTLET_TRANSACTION_CONFIGURATION, configuration);
        }

        
        
        Document userWorkspace = (Document) getNuxeoController(portalControllerContext).executeNuxeoCommand(new GetUserProfileCommand(portalControllerContext.getRequest().getRemoteUser()));
        String rootPath = userWorkspace.getPath().substring(0, userWorkspace.getPath().lastIndexOf('/')) + "/documents/transactions";
        configuration.setPath(rootPath);
 
        
        return configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException {
    	
        
        // Window
      PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

      if (configuration.getPath() == null) {
    	  configuration.setPath(this.properties.getProperty(PATH_PROPERTY));
      }
      if (configuration.getWebid() == null) {
    	  configuration.setWebid(this.properties.getProperty(WEBID));
      }
      
      // Nuxeo request
      window.setProperty(PATH_PROPERTY, configuration.getPath());

      // BeanShell
      window.setProperty(WEBID,configuration.getWebid());

    }

    
    
    
    
    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document createOne(PortalControllerContext portalControllerContext) throws PortletException {
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        NuxeoController nuxeoController = getNuxeoController(portalControllerContext);
        
        return (Document) nuxeoController.executeNuxeoCommand(new OneCreationCommand(configuration,UUID.randomUUID().toString()));

        
    }



    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document delete(PortalControllerContext portalControllerContext) throws PortletException {
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        return (Document) nuxeoController.executeNuxeoCommand(new DeleteCommand(configuration,UUID.randomUUID().toString()));

    }
    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document createBlob(PortalControllerContext portalControllerContext) throws PortletException {
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        return (Document) nuxeoController.executeNuxeoCommand(new CreateBlobCommand(configuration,UUID.randomUUID().toString()));

    }
    
    
    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document createFile(PortalControllerContext portalControllerContext, String suffix) throws PortletException {
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        return (Document) nuxeoController.executeNuxeoCommand(new CreateFileCommand(configuration,suffix));

    }
    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document createBlobs(PortalControllerContext portalControllerContext) throws PortletException {
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        return (Document) nuxeoController.executeNuxeoCommand(new CreateBlobsCommand(configuration,UUID.randomUUID().toString()));

    }

    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document fetchPublicationInfo(PortalControllerContext portalControllerContext) throws PortletException {
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        return (Document) nuxeoController.executeNuxeoCommand(new FetchPublicationInfoCommand(configuration,UUID.randomUUID().toString()));

    }
    
    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public void init(PortalControllerContext portalControllerContext, PersonUpdateService personUpdateService) throws PortletException {
    
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        nuxeoController.executeNuxeoCommand(new InitCommand(configuration, personUpdateService));
  
    
    }
    
    
    /** 
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public Document createAndUpdate(PortalControllerContext portalControllerContext, PersonUpdateService personUpdateService) throws PortletException {
         Person person;
        String uid;
        person = personUpdateService.getEmptyPerson();
        uid = "test-"+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        person.setUid(uid);


        person.setGivenName(uid);
        person.setSn(uid);
        person.setMail(uid);
        person.setDisplayName(uid);
        person.setCn(person.getGivenName() + " " + person.getSn());

        personUpdateService.create(person);
        
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        Document doc = (Document) nuxeoController.executeNuxeoCommand(new UpdateCommand(configuration,UUID.randomUUID().toString()));
        return doc;

    }
    
    /**
     * Get Nuxeo controller
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        PortletRequest request = portalControllerContext.getRequest();
        PortletResponse response = portalControllerContext.getResponse();
        PortletContext portletContext = portalControllerContext.getPortletCtx();
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);


        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_USER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(false);


        return nuxeoController;

    }


    @Override
    public Document getTask(PortalControllerContext portalControllerContext, String uuid) throws PortletException {
        
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

        
        Documents tasks =  (Documents) nuxeoController.executeNuxeoCommand(new GetTasksCommand(uuid));
        if( tasks.size() != 1)
            throw new PortletException(" tasks size = "+ tasks.size());
        
        return tasks.get(0);
   }



}
