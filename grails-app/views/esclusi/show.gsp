
<%@ page import="it.algos.algosbio.Esclusi" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'esclusi.label', default: 'Esclusi')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-esclusi" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="esclusi.list.label"  default="Elenco"/></g:link></li>
                <g:if test="${!noMenuCreate}">
                    <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                          args="[entityName]"/></g:link></li>
                </g:if>
                <g:if test="${menuExtra}">
                    <algos:menuExtra menuExtra="${menuExtra}"></algos:menuExtra>
                </g:if>
			</ul>
		</div>
		<div id="show-esclusi" class="content scaffold-show" role="main">
			<h1><g:message code="esclusi.show.label" args="[entityName]" default="Mostra"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list esclusi">
			
				<g:if test="${esclusiInstance?.pageid}">
				<li class="fieldcontain">
					<span id="pageid-label" class="property-label"><g:message code="esclusi.pageid.label" default="Pageid" /></span>
					
						<span class="property-value" aria-labelledby="pageid-label"><g:fieldValue bean="${esclusiInstance}" field="pageid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${esclusiInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="esclusi.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${esclusiInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:esclusiInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${esclusiInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
