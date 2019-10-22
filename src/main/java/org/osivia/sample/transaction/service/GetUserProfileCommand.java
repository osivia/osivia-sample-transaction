
package org.osivia.sample.transaction.service;


import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;



public class GetUserProfileCommand  implements INuxeoCommand{
	
    private String username;

    
    public GetUserProfileCommand(String username) {
        this.username = username;
    }

	/**
	 * execution d'une requete nuxéo permettant de récuperer le userProfile
	 * @return 
	 */
	public Object execute(Session automationSession) throws Exception {
	
        OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
        if (username != null) {
            newRequest.set("username", username);
        }
        Document refDoc = (Document) newRequest.execute();
        
        Document doc = (Document) automationSession
        .newRequest("Document.FetchLiveDocument").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", refDoc).execute();

         return doc;        
		
	}

	public String getId() {
		
        return "GetUserProfileCommand/".concat(username);
	}

}
