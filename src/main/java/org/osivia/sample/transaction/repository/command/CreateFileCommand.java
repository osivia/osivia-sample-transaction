package org.osivia.sample.transaction.repository.command;

import java.io.File;
import java.net.URL;
import java.util.Calendar;

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

public class CreateFileCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;
    boolean exception;

    public CreateFileCommand(Configuration configuration, String suffix, boolean exception) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;
        this.exception = exception;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {



        Calendar now = Calendar.getInstance();

        // Creation
        OperationRequest operationRequest = session.newRequest("Document.Create");
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));

        Document createdDoc = (Document) operationRequest.setInput(new PathRef(configuration.getPath())).set("type", "File").execute();


        System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


        PropertyMap properties = new PropertyMap();
        String end = "-commitx1";
        if (suffix != null)
            end += "-" + suffix;
        properties.set("dc:title", "file " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + end);

        OperationRequest operationUpdateRequest = session.newRequest("Document.Update");
        operationUpdateRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationUpdateRequest.setInput(createdDoc).set("properties", properties).execute();


        URL exampleFile = this.getClass().getResource("/WEB-INF/classes/osivia.docx");
        File file = new File(exampleFile.getFile());
        Blob blob = new FileBlob(file, "osivia", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");


        OperationRequest req = session.newRequest("Blob.Attach").setInput(blob).set("document", createdDoc.getPath());
        req.set("xpath", "file:content");

        req.setHeader("nx_es_sync", String.valueOf(true));
        req.execute();
        
        
        if( exception)  {

            req = session.newRequest("Transaction.GenerateExceptionTest");
            req.execute();
                        
        }

        return createdDoc;
    }

    @Override
    public String getId() {
        return null;
    }
}
