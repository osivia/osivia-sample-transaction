package org.osivia.sample.transaction.repository.command;

import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class CreateAndTxUpdateCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public CreateAndTxUpdateCommand(Configuration configuration, String suffix) {
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
            
            Calendar now = Calendar.getInstance();


            txId = TransactionUtils.createTransaction(session);

            // Fist step: creation
            OperationRequest operationCreateRequest = session.newRequest("Document.Create");

            Document createdDoc = (Document) operationCreateRequest.setHeader("Tx-conversation-id", txId).setInput(new PathRef(configuration.getPath()))
                    .set("type", "Note").execute();
            System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");
            
            // Then, update document
            PropertyMap propertyMap = new PropertyMap();
            propertyMap.set("ttc:keywords", "KEYWORD1");
            propertyMap.set("dc:title", "note AAAA "+now.get(Calendar.MINUTE) +":"+now.get(Calendar.SECOND));            
            
            OperationRequest operationUpdateRequest = session.newRequest("Document.Update");
            createdDoc = (Document) operationUpdateRequest.setHeader("Tx-conversation-id", txId).setInput(createdDoc).set("properties", propertyMap)
                    .execute();            

            // First commit
            session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();

            // 2nd transaction
            txId = TransactionUtils.createTransaction(session);

            // Then, update in the new transaction
            propertyMap = new PropertyMap();
            propertyMap.set("ttc:keywords", "KEYWORD2");
            propertyMap.set("dc:title", "note BBBB "+now.get(Calendar.MINUTE) +":"+now.get(Calendar.SECOND));      

            operationUpdateRequest = session.newRequest("Document.Update");
            Document createdDoc2 = (Document) operationUpdateRequest.setHeader("Tx-conversation-id", txId).setInput(createdDoc).set("properties", propertyMap)
                    .execute();
            
            System.out.println("Update DONE: " + createdDoc2.getPath() + " | " + createdDoc2.getInputRef() + "\n");
            
            // And rollback
            session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();
            
            
            commandNotification = new CommandNotification(true, "Création et mise à jour en rollback réussie sur : " + createdDoc.getInputRef());
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
