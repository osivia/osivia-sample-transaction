<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:renderURL var="url" />


<p class="text-danger">
    <i class="glyphicons glyphicons-circle-remove"></i>
    <op:translate key="ERROR" />
</p>
    
<p>
    <a href="${url}" class="btn btn-default"><op:translate key="BACK" /></a>
</p>
