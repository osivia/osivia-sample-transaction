package org.osivia.sample.transaction.repository.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;
import org.springframework.transaction.annotation.Transactional;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class UpdateAndRollbackCommand implements INuxeoCommand {

    private final Configuration configuration;
    String suffix;


    public UpdateAndRollbackCommand(Configuration configuration, String suffix) {
        super();
        this.configuration = configuration;
        this.suffix = suffix;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Object execute(Session session) throws Exception {

        CommandNotification commandNotification;
        String txId = null;
        try {
            // Start Tx
            Object object = session.newRequest("Repository.StartTransaction").execute();
            if (object instanceof FileBlob) {
                FileBlob txIdAsBlob = (FileBlob) object;
                txId = IOUtils.toString(txIdAsBlob.getStream(), "UTF-8");
                System.out.println("[TXID]: " + txId + "\n");
                try {
                    PropertyMap propertyMap = new PropertyMap();                    
                    // Fist step: creation of a document with ttc:webid=oFyFyS
                    OperationRequest operationCreateRequest = session.newRequest("Document.TTCCreate");

                    // Si on crée un document en spécifiant le ttc:webid, le document créé n'a pas le ttc:webid voulu
                    Document createdDoc = (Document) operationCreateRequest.setHeader("Tx-conversation-id", txId).setInput(new PathRef(configuration.getPath()))
                            .set("type", "Note").execute();
                    System.out.println("Creation DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");

                    propertyMap = new PropertyMap();   
                    propertyMap.set("dc:title", "Document créé pour mise a jour et rollback");

                    OperationRequest operationUpdateRequest = session.newRequest("Document.TTCUpdate");
                    createdDoc = (Document) operationUpdateRequest.setHeader("Tx-conversation-id", txId).setInput(createdDoc).set("properties", propertyMap)
                            .execute();
                    System.out.println("Update DONE: " + createdDoc.getPath() + " | " + createdDoc.getInputRef() + "\n");

  

                    // Force rollback
                    session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();
                    commandNotification = new CommandNotification(true, "Création, mise à jour et rollback réalisées avec succès");
                } catch (Exception e) {
                    session.newRequest("Repository.MarkTransactionAsRollback").setHeader("Tx-conversation-id", txId).execute();
                    System.out.println(e);
                    commandNotification = new CommandNotification(false, "Erreur, Rollback nécessaire, cause:" + e.toString());
                } finally {
                    session.newRequest("Repository.CommitOrRollbackTransaction").setHeader("Tx-conversation-id", txId).execute();
                }
            } else {
                Document document = (Document) object;
                System.out.println("Pas réussi à faire l'appel à Start Transaction, document" + document.toString());
                commandNotification = new CommandNotification(false, "Pas réussi à faire l'appel à Start Transaction");
            }

        } catch (Exception e) {
            System.out.println(e);
            commandNotification = new CommandNotification(false, "Erreur, cause : " + e.toString());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return commandNotification;
    }

    @Override
    public String getId() {
        return "transaction" + suffix;
    }
}
