<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="createone" var="createOneUrl" />
<portlet:actionURL name="createseveral" var="createSeveralUrl" />
<portlet:actionURL name="createandrollback" var="createAndRollbackUrl" />
<portlet:actionURL name="createandupdate" var="createAndUpdateUrl" />
<portlet:actionURL name="createandupdatetx2" var="createAndUpdateTx2Url" />


<portlet:actionURL name="deleteandrollback" var="deleteAndRollbackUrl" />
<portlet:actionURL name="createblob" var="createBlobUrl" />
<portlet:actionURL name="createfile" var="createFileUrl" />
<portlet:actionURL name="createblobs" var="createBlobsUrl" />
<portlet:actionURL name="fetchPublicationInfo" var="fetchPublicationInfoUrl" />
<portlet:actionURL name="updateAndRollback" var="updateAndRollbackUrl" />
<portlet:actionURL name="reminder" var="reminderUrl" />

<p>
    <a href="${createOneUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_ONE" /></span>
    </a>
    <br/><br/>
    
    <a href="${createSeveralUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_SEVERAL" /></span>
    </a>
    <br/><br/>
    
    <a href="${createAndRollbackUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_AND_ROLLBACK" /></span>
    </a>
    <br/><br/>
    
    <a href="${createAndUpdateUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_AND_UPDATE" /></span>
    </a>
    <br/><br/>
    
        <a href="${createAndUpdateTx2Url}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_AND_UPDATE_TX2" /></span>
    </a>
    <br/><br/>
    
    <a href="${deleteAndRollbackUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="DELETE_AND_ROLLBACK" /></span>
    </a>
    <br/><br/>
    
    <a href="${createBlobUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_BLOB" /></span>
    </a>
    <br/><br/>
    
        
    <a href="${createFileUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_FILE" /></span>
    </a>
    <br/><br/>
    
    <a href="${createBlobsUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_BLOBS" /></span>
    </a>
    <br/><br/>
    
    <a href="${fetchPublicationInfoUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="FETCH_PUBLICATION_INFO" /></span>
    </a>
    <br/><br/>
    
    <a href="${updateAndRollbackUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="UPDATE_AND_ROLLBACK" /></span>
    </a>
    
    <a href="${reminderUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="REMINDER" /></span>
    </a>

</p>
