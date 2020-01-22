package org.osivia.sample.transaction.repository.command;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class DeleteCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public DeleteCommand(Configuration configuration, String suffix) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {

       // Fist step: creation
        OperationRequest operationCreateRequest = session.newRequest("Document.Create");

        Document createdDoc = (Document) operationCreateRequest.setInput(new PathRef(configuration.getPath())).set("type", "Note").execute();
        System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


        OperationRequest operationUpdateRequest = session.newRequest("Document.Delete");
        operationUpdateRequest.setInput(createdDoc).execute();
        System.out.println("Delete DONE");

        return createdDoc;
    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
