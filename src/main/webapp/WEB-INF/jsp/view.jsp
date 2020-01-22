<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="createone" var="createOneUrl" />
<portlet:actionURL name="createseveral" var="createSeveralUrl" />
<portlet:actionURL name="createandrollback" var="createAndRollbackUrl" />
<portlet:actionURL name="deleteandrollback" var="deleteAndRollbackUrl" />
<portlet:actionURL name="createblob" var="createBlobUrl" />
<portlet:actionURL name="createfile" var="createFileUrl" />
<portlet:actionURL name="createblobs" var="createBlobsUrl" />
<portlet:actionURL name="fetchPublicationInfo" var="fetchPublicationInfoUrl" />
<portlet:actionURL name="updateAndCommit" var="updateAndCommitUrl" />
<portlet:actionURL name="updateWithoutCommit" var="updateWithoutCommitUrl" />
<portlet:actionURL name="updateAndRollback" var="updateAndRollbackUrl" />
<portlet:actionURL name="reminder" var="reminderUrl" />
<portlet:actionURL name="init" var="initUrl" />

<p>

    <p>A QAULIFIER</p>

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
    

    
    <a href="${deleteAndRollbackUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="DELETE_AND_ROLLBACK" /></span>
    </a>
    <br/><br/>
    
    <a href="${createBlobUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_BLOB" /></span>
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
    
    
        <p>INIT</p>
    
    
    <a href="${initUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="INIT" /></span>
    </a>
    

    
    <p>TESTS CMS</p>
    
        <a href="${createFileUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="CREATE_FILE" /></span>
    </a>
    
    <a href="${updateAndCommitUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="UPDATE_AND_COMMIT" /></span>
    </a>
    
    <a href="${updateWithoutCommitUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="UPDATE_WITHOUT_COMMIT" /></span>
    </a>
    
   <a href="${updateAndRollbackUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="UPDATE_AND_ROLLBACK" /></span>
    </a>
    
        <p>TELEPROCEDURES</p>
        
    <a href="${reminderUrl}" class="btn btn-primary no-ajax-link">
        <span><op:translate key="REMINDER" /></span>
    </a>

</p>
