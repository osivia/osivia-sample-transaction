package org.osivia.sample.transaction.repository.command;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.osivia.sample.transaction.model.Configuration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Import files Nuxeo command.
 * 
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportFilesCommand implements INuxeoCommand {

    Configuration configuration;

    /**
     * Constructor.
     * 
     * @param path current path
     * @param upload upload multipart files
     */
    public ImportFilesCommand(Configuration configuration) {
        super();
        this.configuration = configuration;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        URL dirUrl = this.getClass().getResource("/WEB-INF/classes/upload");
        File dir = new File(dirUrl.getFile());
  
        
        File[] fileList = dir.listFiles();
        

        
        // Blobs
        Blobs blobs = new Blobs(fileList.length);
        for (File file: fileList) {
            String mimeType = Files.probeContentType(file.toPath());
            
            Calendar now = Calendar.getInstance();
            String fileName = "import " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND) + " " + file.getName();

            Blob blob = new StreamBlob(new FileInputStream(file), fileName, mimeType);
            blobs.add(blob);
        }

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import");
        operationRequest.setInput(blobs);
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationRequest.setContextProperty("currentDocument", configuration.getPath());
        operationRequest.set("overwite", true);

        return operationRequest.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
