package org.osivia.sample.transaction.repository.command;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class FetchPublicationInfoCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public FetchPublicationInfoCommand(Configuration configuration, String suffix) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {

        Document createdDoc = null;


        OperationRequest operationRequest = session.newRequest("Document.Create");

        createdDoc = (Document) operationRequest.setInput(new PathRef(configuration.getPath())).set("type", "Note").execute();
        System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");

        OperationRequest request = session.newRequest("Document.FetchPublicationInfos");

        request.set("path", createdDoc.getPath());

        Blob binariesInfos = (Blob) request.execute();
        System.out.println("Creation DONE: " + binariesInfos.toString());

        return createdDoc;
    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
