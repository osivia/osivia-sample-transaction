package org.osivia.sample.transaction.repository.command;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class CreateBlobCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public CreateBlobCommand(Configuration configuration, String suffix) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {



        // Creation
        OperationRequest operationRequest = session.newRequest("Document.Create");

        Document createdDoc = (Document) operationRequest.setInput(new PathRef(configuration.getPath())).set("type", "Note").execute();
        System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", "Image + vignette");

        OperationRequest operationUpdateRequest = session.newRequest("Document.Update");
        operationUpdateRequest.setInput(createdDoc).set("properties", properties).execute();


        URL exampleFile = this.getClass().getResource("/WEB-INF/classes/spring.png");
        File file = new File(exampleFile.getFile());
        Blob blob = new FileBlob(file, "printemps", "image/png");


        OperationRequest req = session.newRequest("Blob.Attach").setInput(blob).set("document", createdDoc.getPath());

        req.set("xpath", "ttc:vignette");
         req.execute();
        
        return createdDoc;


    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
