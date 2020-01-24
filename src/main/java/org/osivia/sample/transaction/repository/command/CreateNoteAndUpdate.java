package org.osivia.sample.transaction.repository.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.springframework.transaction.annotation.Transactional;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class CreateNoteAndUpdate implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;


    public CreateNoteAndUpdate(Configuration configuration, String suffix) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {


        PropertyMap propertyMap = new PropertyMap();
        // Fist step: creation of a document with ttc:webid=oFyFyS
        OperationRequest operationCreateRequest = session.newRequest("Document.TTCCreate");


        Document createdDoc = (Document) operationCreateRequest.setInput(new PathRef(configuration.getPath())).set("type", "Note").execute();
        System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");

        propertyMap = new PropertyMap();
        propertyMap.set("dc:title", "Document CreateAndUpdate");

        OperationRequest operationUpdateRequest = session.newRequest("Document.TTCUpdate");
        createdDoc = (Document) operationUpdateRequest.setInput(createdDoc).set("properties", propertyMap).execute();
        System.out.println("Update DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");

        return createdDoc;
    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
