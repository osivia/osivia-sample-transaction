package org.osivia.sample.transaction.repository.command;

import java.io.File;
import java.net.URL;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class CreateFolderCommand implements INuxeoCommand {

    private final Configuration configuration;


    public CreateFolderCommand(Configuration configuration ) {
        super();
        this.configuration = configuration;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {



        Calendar now = Calendar.getInstance();

        // Creation
        // Document service
        DocumentService documentService = session.getAdapter(DocumentService.class);

        // Parent document reference
        DocRef parent = new PathRef(this.configuration.getPath());

        PropertyMap properties = new PropertyMap();

        properties.set("dc:title", "folder " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) );

        
        // Document creation
        DocRef document = documentService.createDocument(parent, "Folder", null, properties);

 
        return document;
    }

    @Override
    public String getId() {
        return null;
    }
}
