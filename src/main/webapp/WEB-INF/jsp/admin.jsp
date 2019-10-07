<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<form:form modelAttribute="configuration" action="${url}" method="post" cssClass="form-horizontal no-ajax-link" role="form">
        
        <!-- Path -->
        <div class="form-group">
            <form:label path="path" cssClass="col-sm-3 control-label"><op:translate key="PATH" /></form:label>
            <div class="col-sm-9">
                <form:input path="path" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Webid -->
        <div class="form-group">
            <form:label path="webid" cssClass="col-sm-3 control-label"><op:translate key="WEBID" /></form:label>
            <div class="col-sm-9">
                <form:input path="webid" cssClass="form-control" />
            </div>
        </div>


    <!-- Buttons -->
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
        </div>
    </div>
</form:form>
