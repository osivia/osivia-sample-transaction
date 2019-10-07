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

        CommandNotification commandNotification;
        String txId = null;
        try {

            // Creation
            OperationRequest operationRequest = session.newRequest("Document.Create");

            Document createdDoc = (Document) operationRequest.setHeader("Tx-conversation-id", txId).setInput(new PathRef(configuration.getPath()))
                    .set("type", "Note").execute();
            System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


            PropertyMap properties = new PropertyMap();
            properties.set("dc:title", "Image + vignette");

            OperationRequest operationUpdateRequest = session.newRequest("Document.Update");
            operationUpdateRequest.setHeader("Tx-conversation-id", txId).setInput(createdDoc).set("properties", properties).execute();


            URL exampleFile = this.getClass().getResource("/WEB-INF/classes/spring.png");
            File file = new File(exampleFile.getFile());
            Blob blob = new FileBlob(file, "printemps", "image/png");


            OperationRequest req = session.newRequest("Blob.Attach").setInput(blob).set("document", createdDoc.getPath());
            // req.setHeader(Constants.HEADER_NX_VOIDOP, "true");
            req.setHeader("Tx-conversation-id", txId);
            req.set("xpath", "ttc:vignette");
            FileBlob blobAdded = (FileBlob) req.execute();


            // First commit
            session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();

            // 2nd transaction
            txId = TransactionUtils.createTransaction(session);
            
            
             exampleFile = this.getClass().getResource("/WEB-INF/classes/automn.png");
             file = new File(exampleFile.getFile());
             blob = new FileBlob(file, "automn", "image/png");


            req = session.newRequest("Blob.Attach").setInput(blob).set("document", createdDoc.getPath());
            // req.setHeader(Constants.HEADER_NX_VOIDOP, "true");
            req.setHeader("Tx-conversation-id", txId);
            req.set("xpath", "ttc:vignette");
            blobAdded = (FileBlob) req.execute();

            
            // And rollback
            session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();


            System.out.println("Creation Blob DONE : " + blobAdded.getInputRef());
            commandNotification = new CommandNotification(true, "Blob créé avec succès : " + blobAdded.getInputRef());

        } catch (Exception e) {
            if (txId != null) {
                session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();
                System.out.println(e);
            }
            commandNotification = new CommandNotification(false, "Erreur, Rollback nécessaire, cause:" + e.toString());
        } finally {
            if( txId != null)
                session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();
        }

        return commandNotification;
    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
