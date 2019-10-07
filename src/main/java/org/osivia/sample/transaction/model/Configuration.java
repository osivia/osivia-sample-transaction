package org.osivia.sample.transaction.model;


/**
 * Transaction configuration java-bean.
 *
 * @author Jean-Sébastien Steux
 */
public class Configuration {

    private String path;
    
    private String webid;
    
    /**
     * Getter for path.
     * @return the path
     */
    public String getPath() {
        return path;
    }

    
    /**
     * Setter for path.
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }


    
    /**
     * Getter for webid.
     * @return the webid
     */
    public String getWebid() {
        return webid;
    }


    
    /**
     * Setter for webid.
     * @param webid the webid to set
     */
    public void setWebid(String webid) {
        this.webid = webid;
    }

}
