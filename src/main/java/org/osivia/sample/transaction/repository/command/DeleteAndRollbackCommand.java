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

public class DeleteAndRollbackCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;

    public DeleteAndRollbackCommand(Configuration configuration, String suffix) {
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
        Document createdDoc = null;
        try {
            // Start Tx
            Object object = session.newRequest("Repository.StartTransaction").execute();
            if (object instanceof FileBlob)
            {
                FileBlob txIdAsBlob = (FileBlob) object;
                try {
                    txId = IOUtils.toString(txIdAsBlob.getStream(), "UTF-8");
                    System.out.println("[TXID]: " + txId + "\n");

                    // Fist step: creation
                    OperationRequest operationCreateRequest = session.newRequest("Document.Create");

                    createdDoc = (Document) operationCreateRequest.setHeader("Tx-conversation-id", txId)
                            .setInput(new PathRef(configuration.getPath()))
                            .set("type", "Note")
                            .execute();
                    System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");


                    OperationRequest operationUpdateRequest = session.newRequest("Document.Delete");
                    operationUpdateRequest.setHeader("Tx-conversation-id", txId)
                    .setInput(createdDoc)
                    .execute();
                    System.out.println("Delete DONE");

                    session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();

                    System.out.println("Delete CANCEL");
                    commandNotification = new CommandNotification(true, "Création, suppression et rollback réalisé avec succès");
                } catch (Exception e) {
                    session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();
                    System.out.println(e);
                    commandNotification = new CommandNotification(false, "Erreur, Rollback nécessaire, cause:"+e.toString());
                } finally {
                    session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();
                }
            } else
            {
                Document document = (Document) object;
                System.out.println("Pas réussi à faire l'appel à Start Transaction, document"+document.toString());
                commandNotification = new CommandNotification(false, "Pas réussi à faire l'appel à Start Transaction");
            }

        } catch (Exception e) {
            System.out.println(e);
            commandNotification = new CommandNotification(false,"Erreur, cause : "+e.toString());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return commandNotification;
    }

    @Override
    public String getId() {
        return "transaction"+suffix;
    }
}
