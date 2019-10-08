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

    public CreateFileCommand(Configuration configuration, String suffix) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {

        boolean transaction = true;

        CommandNotification commandNotification;
        String txId = null;
        try {

            Calendar now = Calendar.getInstance();

            // Creation
            OperationRequest operationRequest = session.newRequest("Document.Create");
            operationRequest.setHeader("nx_es_sync", String.valueOf(true));
            if( transaction)
                operationRequest.setHeader("Tx-conversation-id", txId);
            Document createdDoc = (Document) operationRequest.setInput(new PathRef(configuration.getPath()))
                    .set("type", "File").execute();
            
            
            System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


            PropertyMap properties = new PropertyMap();
            String end = "-commitx1";
            if (suffix != null)
                end += "-" + suffix;
            properties.set("dc:title", "file " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + end);

            OperationRequest operationUpdateRequest = session.newRequest("Document.Update");
            operationUpdateRequest.setHeader("nx_es_sync", String.valueOf(true));
            if (transaction)
                operationUpdateRequest.setHeader("Tx-conversation-id", txId);
            operationUpdateRequest.setInput(createdDoc).set("properties", properties).execute();


            URL exampleFile = this.getClass().getResource("/WEB-INF/classes/osivia.docx");
            File file = new File(exampleFile.getFile());
            Blob blob = new FileBlob(file, "osivia", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");


            OperationRequest req = session.newRequest("Blob.Attach").setInput(blob).set("document", createdDoc.getPath());
            if (transaction)
                req.setHeader("Tx-conversation-id", txId);
            req.set("xpath", "file:content");

            req.setHeader("nx_es_sync", String.valueOf(true));
            FileBlob blobAdded = (FileBlob) req.execute();


            // First commit
            if( transaction)             
                session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();

            // 2nd transaction
            if (transaction)
                txId = TransactionUtils.createTransaction(session);

            properties = new PropertyMap();
            end = "-commitx2";
            if (suffix != null)
                end += "-" + suffix;
            properties.set("dc:title", "file " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + end);

            operationUpdateRequest = session.newRequest("Document.Update");
            if (transaction)
                operationUpdateRequest.setHeader("Tx-conversation-id", txId);
            operationUpdateRequest.setInput(createdDoc).set("properties", properties).execute();
            operationUpdateRequest.setHeader("nx_es_sync", String.valueOf(true));


            exampleFile = this.getClass().getResource("/WEB-INF/classes/empty.docx");
            file = new File(exampleFile.getFile());
            blob = new FileBlob(file, "almost_empty", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");


            req = session.newRequest("Blob.Attach").setInput(blob).set("document", createdDoc.getPath());
            // req.setHeader(Constants.HEADER_NX_VOIDOP, "true");
            if (transaction)
                req.setHeader("Tx-conversation-id", txId);
            req.set("xpath", "file:content");
            req.setHeader("nx_es_sync", String.valueOf(true));
            blobAdded = (FileBlob) req.execute();


            // And rollback
            if (transaction)
                session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();


            System.out.println("Creation File DONE : " + blobAdded.getInputRef());
            commandNotification = new CommandNotification(true, "File créé avec succès : " + blobAdded.getInputRef());

        } catch (Exception e) {
            if (txId != null) {
                if (transaction)
                    session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();
                System.out.println(e);
            }
            commandNotification = new CommandNotification(false, "Erreur, Rollback nécessaire, cause:" + e.toString());
        } finally {
            if (txId != null)
                if (transaction)
                    session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();
        }

        return commandNotification;
    }

    @Override
    public String getId() {
        return null;
    }
}
