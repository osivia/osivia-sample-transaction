package org.osivia.sample.transaction.model;

import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Admin transaction notification.
 *
 * @author Jean-Sébastien Steux
 * @see PortletContextAware
 */
public class CommandNotification {

    public boolean success;
    
    public String msgReturn;

    
    public CommandNotification() {
        super();
    }


    public CommandNotification(boolean success, String msgReturn) {
        super();
        this.success = success;
        this.msgReturn = msgReturn;
    }


    /**
     * Getter for success.
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    
    /**
     * Setter for success.
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    
    /**
     * Getter for msgReturn.
     * @return the msgReturn
     */
    public String getMsgReturn() {
        return msgReturn;
    }

    
    /**
     * Setter for msgReturn.
     * @param msgReturn the msgReturn to set
     */
    public void setMsgReturn(String msgReturn) {
        this.msgReturn = msgReturn;
    }

}
