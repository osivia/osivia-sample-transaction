package org.osivia.sample.transaction.repository.command;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class OneCreationCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public OneCreationCommand(Configuration configuration, String suffix) {
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
        return createdDoc;
     }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
