package org.osivia.sample.transaction.repository.command;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class CreateBlobsCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public CreateBlobsCommand(Configuration configuration, String suffix) {
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

        URL exampleFile1 = this.getClass().getResource("/WEB-INF/classes/example1.doc");
        URL exampleFile2 = this.getClass().getResource("/WEB-INF/classes/example2.pdf");

        Blob blob1 = new FileBlob(new File(exampleFile1.getFile()));
        Blob blob2 = new FileBlob(new File(exampleFile2.getFile()));
        List<Blob> blobs = new ArrayList<>(2);
        blobs.add(blob1);
        blobs.add(blob2);

        OperationRequest req = session.newRequest("Blob.AttachList").setInput(new Blobs(blobs)).set("document", createdDoc.getPath());
        req.setHeader(Constants.HEADER_NX_VOIDOP, "true");
        req.set("xpath", "files:files");
        Document blobAdded = (Document) req.execute();

        return blobAdded;
    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
