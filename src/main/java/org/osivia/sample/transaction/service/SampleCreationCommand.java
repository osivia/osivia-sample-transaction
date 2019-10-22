package org.osivia.sample.transaction.service;

import java.util.Calendar;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class SampleCreationCommand implements INuxeoCommand {


    String path;

    public SampleCreationCommand(String path) {
        super();

        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {

        
        Calendar now = Calendar.getInstance();
        
        // Start Tx
        // Creation
        OperationRequest operationRequest = session.newRequest("Document.Create");
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
    
        Document createdDoc = (Document) operationRequest.setInput(new PathRef(path))
                .set("type", "File").execute();
        
        



        PropertyMap properties = new PropertyMap();

        properties.set("dc:title", "file " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));

        OperationRequest operationUpdateRequest = session.newRequest("Document.Update");
        operationUpdateRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationUpdateRequest.setInput(createdDoc).set("properties", properties).execute();

        


        return createdDoc;
    }

    @Override
    public String getId() {
        return "SampleCreationCommand" + path;
    }
}
