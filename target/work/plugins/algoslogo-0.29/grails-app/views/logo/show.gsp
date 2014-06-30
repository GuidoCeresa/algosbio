
<%@ page import="it.algos.algoslogo.Logo" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'logo.label', default: 'Logo')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-logo" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-logo" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list logo">
			
				<g:if test="${logoInstance?.time}">
				<li class="fieldcontain">
					<span id="time-label" class="property-label"><g:message code="logo.time.label" default="Time" /></span>
					
						<span class="property-value" aria-labelledby="time-label"><g:fieldValue bean="${logoInstance}" field="time"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${logoInstance?.utente}">
				<li class="fieldcontain">
					<span id="utente-label" class="property-label"><g:message code="logo.utente.label" default="Utente" /></span>
					
						<span class="property-value" aria-labelledby="utente-label"><g:fieldValue bean="${logoInstance}" field="utente"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${logoInstance?.ruolo}">
				<li class="fieldcontain">
					<span id="ruolo-label" class="property-label"><g:message code="logo.ruolo.label" default="Ruolo" /></span>
					
						<span class="property-value" aria-labelledby="ruolo-label"><g:fieldValue bean="${logoInstance}" field="ruolo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${logoInstance?.evento}">
				<li class="fieldcontain">
					<span id="evento-label" class="property-label"><g:message code="logo.evento.label" default="Evento" /></span>
					
						<span class="property-value" aria-labelledby="evento-label"><g:link controller="evento" action="show" id="${logoInstance?.evento?.id}">${logoInstance?.evento?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${logoInstance?.livello}">
				<li class="fieldcontain">
					<span id="livello-label" class="property-label"><g:message code="logo.livello.label" default="Livello" /></span>
					
						<span class="property-value" aria-labelledby="livello-label"><g:fieldValue bean="${logoInstance}" field="livello"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${logoInstance?.dettaglio}">
				<li class="fieldcontain">
					<span id="dettaglio-label" class="property-label"><g:message code="logo.dettaglio.label" default="Dettaglio" /></span>
					
						<span class="property-value" aria-labelledby="dettaglio-label"><g:fieldValue bean="${logoInstance}" field="dettaglio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${logoInstance?.ip}">
				<li class="fieldcontain">
					<span id="ip-label" class="property-label"><g:message code="logo.ip.label" default="Ip" /></span>
					
						<span class="property-value" aria-labelledby="ip-label"><g:fieldValue bean="${logoInstance}" field="ip"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:logoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${logoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
