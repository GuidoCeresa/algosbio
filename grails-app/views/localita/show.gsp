
<%@ page import="it.algos.algosbio.Localita" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'localita.label', default: 'Localita')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-localita" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-localita" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list localita">
			
				<g:if test="${localitaInstance?.nome}">
				<li class="fieldcontain">
					<span id="nome-label" class="property-label"><g:message code="localita.nome.label" default="Nome" /></span>
					
						<span class="property-value" aria-labelledby="nome-label"><g:fieldValue bean="${localitaInstance}" field="nome"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${localitaInstance?.link}">
				<li class="fieldcontain">
					<span id="link-label" class="property-label"><g:message code="localita.link.label" default="Link" /></span>
					
						<span class="property-value" aria-labelledby="link-label"><g:fieldValue bean="${localitaInstance}" field="link"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${localitaInstance?.voci}">
				<li class="fieldcontain">
					<span id="voci-label" class="property-label"><g:message code="localita.voci.label" default="Voci" /></span>
					
						<span class="property-value" aria-labelledby="voci-label"><g:fieldValue bean="${localitaInstance}" field="voci"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:localitaInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${localitaInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
