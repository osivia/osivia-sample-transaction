package org.osivia.sample.transaction.repository.command;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.Session;

import org.nuxeo.ecm.automation.client.model.FileBlob;

public class TransactionUtils {

    public static String createTransaction(Session session) throws Exception {
        String txId = null;

        // Start Tx
        Object object = session.newRequest("Repository.StartTransaction").execute();
        if (object instanceof FileBlob) {
            FileBlob txIdAsBlob = (FileBlob) object;
            txId = IOUtils.toString(txIdAsBlob.getStream(), "UTF-8");
            System.out.println("[TXID]: " + txId + "\n");
        }
        return txId;
    }

}
