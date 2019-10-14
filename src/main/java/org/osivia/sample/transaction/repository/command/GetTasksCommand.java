package org.osivia.sample.transaction.repository.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get tasks Nuxeo command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class GetTasksCommand implements INuxeoCommand {


    private final String uuid;


    /**
     * Constructor.
     *
     * @param actors task actors
     * @param notifiable notifiable task indicator
     * @param directives task directives
     */
    public GetTasksCommand(String uuid) {
        this.uuid = uuid;
    }

 
   
    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document ");
        query.append("WHERE ecm:primaryType = 'TaskDoc' ");
        query.append("AND ecm:currentLifeCycleState = 'opened' ");
         if (this.uuid != null) {
            query.append("AND nt:pi.pi:globalVariablesValues.uuid = '").append(this.uuid).append("' ");
        }

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, task");
        request.set("query", query.toString());

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
          builder.append(this.uuid);
        return builder.toString();
    }

}
