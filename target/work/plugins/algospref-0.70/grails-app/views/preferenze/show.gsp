
<%@ page import="it.algos.algospref.Preferenze" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'preferenze.label', default: 'Preferenze')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-preferenze" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-preferenze" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list preferenze">
			
				<g:if test="${preferenzeInstance?.ordine}">
				<li class="fieldcontain">
					<span id="ordine-label" class="property-label"><g:message code="preferenze.ordine.label" default="Ordine" /></span>
					
						<span class="property-value" aria-labelledby="ordine-label"><g:fieldValue bean="${preferenzeInstance}" field="ordine"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${preferenzeInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="preferenze.code.label" default="Code" /></span>
					
						<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${preferenzeInstance}" field="code"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${preferenzeInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="preferenze.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${preferenzeInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${preferenzeInstance?.value}">
				<li class="fieldcontain">
					<span id="value-label" class="property-label"><g:message code="preferenze.value.label" default="Value" /></span>
					
						<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${preferenzeInstance}" field="value"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${preferenzeInstance?.descrizione}">
				<li class="fieldcontain">
					<span id="descrizione-label" class="property-label"><g:message code="preferenze.descrizione.label" default="Descrizione" /></span>
					
						<span class="property-value" aria-labelledby="descrizione-label"><g:fieldValue bean="${preferenzeInstance}" field="descrizione"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${preferenzeInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="preferenze.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${preferenzeInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${preferenzeInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="preferenze.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${preferenzeInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${preferenzeInstance?.id}" />
					<g:link class="edit" action="edit" id="${preferenzeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
