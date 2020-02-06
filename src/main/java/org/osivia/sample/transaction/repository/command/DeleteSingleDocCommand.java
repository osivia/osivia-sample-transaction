package org.osivia.sample.transaction.repository.command;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.sample.transaction.model.CommandNotification;
import org.osivia.sample.transaction.model.Configuration;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class DeleteSingleDocCommand implements INuxeoCommand {


    String path;

    public DeleteSingleDocCommand(String path) {
        super();
        this.path = path;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {

        DocumentService documentService = session.getAdapter(DocumentService.class);
        documentService.remove(new PathRef(this.path));

        return null;

    }

    @Override
    public String getId() {
        return "DeleteSingleDocCommand" + path;
    }
}
