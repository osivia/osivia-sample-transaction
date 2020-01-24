package org.osivia.sample.transaction.repository.command;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.sample.transaction.model.Configuration;


import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class InitCommand implements INuxeoCommand {

    private final Configuration configuration;
    private final PersonUpdateService personUpdateService;

    
    private Log logger = LogFactory.getLog(InitCommand.class);

    public InitCommand(Configuration configuration, PersonUpdateService personUpdateService) {
        super();
        this.configuration = configuration;
        this.personUpdateService = personUpdateService;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {

        // // Fist step: creation
        // OperationRequest operationCreateRequest = session.newRequest("Document.Create");
        //
        // Document createdDoc = (Document) operationCreateRequest.setInput(new PathRef(configuration.getPath())).set("type", "Note").execute();
        // System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


        DocumentService documentService = session.getAdapter(DocumentService.class);
        try {
            Document docRoot = documentService.getDocument(configuration.getPath());

            OperationRequest operationUpdateRequest = session.newRequest("Document.Delete");
            operationUpdateRequest.setInput(docRoot).execute();

        } catch (Exception e) {
            // main directory doesn't exists
            
        }

        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", "transactions");
        String parentPath = configuration.getPath().substring(0, configuration.getPath().lastIndexOf('/'));
        documentService.createDocument(new PathRef(parentPath), "Folder", "transactions", properties);
    

        
        // Update procedure
        
        String modelPath = "/default-domain/procedures/procedures-models";
        Document modelsContainer = documentService.getDocument(modelPath);
          

        URL proceduresUrl = this.getClass().getResource("/docs/models/");
        File dir = new File(proceduresUrl.getFile());
        File[] procedures = dir.listFiles();
        for (int i = 0; i < procedures.length; i++) {
            try {
                
                Blob blob = new FileBlob(procedures[i]);
                
                OperationRequest operationRequest = session.newRequest("FileManager.Import").setInput(blob);
                operationRequest.setContextProperty("currentDocument", modelsContainer.getId());
                operationRequest.set("overwite", "true");

                operationRequest.execute();
            }
            catch(Exception e) {
                logger.error("Error when importing procedure : " + procedures[i].getName());

            }
        }
        
        
        // Remove users

        
        Person criteria = personUpdateService.getEmptyPerson();
        criteria.setUid("test*");
        List<Person> persons = personUpdateService.findByCriteria(criteria);
        for(Person person : persons) {
            personUpdateService.delete(person);
        }

        
        
        return null;
    }

    @Override
    public String getId() {
        return "InitCommand" ;
    }
}
