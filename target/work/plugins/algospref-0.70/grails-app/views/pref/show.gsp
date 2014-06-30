
<%@ page import="it.algos.algospref.Pref" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pref.label', default: 'Pref')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-pref" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-pref" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list pref">
			
				<g:if test="${prefInstance?.ordine}">
				<li class="fieldcontain">
					<span id="ordine-label" class="property-label"><g:message code="pref.ordine.label" default="Ordine" /></span>
					
						<span class="property-value" aria-labelledby="ordine-label"><g:fieldValue bean="${prefInstance}" field="ordine"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="pref.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${prefInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="pref.code.label" default="Code" /></span>
					
						<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${prefInstance}" field="code"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.descrizione}">
				<li class="fieldcontain">
					<span id="descrizione-label" class="property-label"><g:message code="pref.descrizione.label" default="Descrizione" /></span>
					
						<span class="property-value" aria-labelledby="descrizione-label"><g:fieldValue bean="${prefInstance}" field="descrizione"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.stringa}">
				<li class="fieldcontain">
					<span id="stringa-label" class="property-label"><g:message code="pref.stringa.label" default="Stringa" /></span>
					
						<span class="property-value" aria-labelledby="stringa-label"><g:fieldValue bean="${prefInstance}" field="stringa"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.bool}">
				<li class="fieldcontain">
					<span id="bool-label" class="property-label"><g:message code="pref.bool.label" default="Bool" /></span>
					
						<span class="property-value" aria-labelledby="bool-label"><g:formatBoolean boolean="${prefInstance?.bool}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.intero}">
				<li class="fieldcontain">
					<span id="intero-label" class="property-label"><g:message code="pref.intero.label" default="Intero" /></span>
					
						<span class="property-value" aria-labelledby="intero-label"><g:fieldValue bean="${prefInstance}" field="intero"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.lungo}">
				<li class="fieldcontain">
					<span id="lungo-label" class="property-label"><g:message code="pref.lungo.label" default="Lungo" /></span>
					
						<span class="property-value" aria-labelledby="lungo-label"><g:fieldValue bean="${prefInstance}" field="lungo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.reale}">
				<li class="fieldcontain">
					<span id="reale-label" class="property-label"><g:message code="pref.reale.label" default="Reale" /></span>
					
						<span class="property-value" aria-labelledby="reale-label"><g:fieldValue bean="${prefInstance}" field="reale"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.doppio}">
				<li class="fieldcontain">
					<span id="doppio-label" class="property-label"><g:message code="pref.doppio.label" default="Doppio" /></span>
					
						<span class="property-value" aria-labelledby="doppio-label"><g:fieldValue bean="${prefInstance}" field="doppio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.decimale}">
				<li class="fieldcontain">
					<span id="decimale-label" class="property-label"><g:message code="pref.decimale.label" default="Decimale" /></span>
					
						<span class="property-value" aria-labelledby="decimale-label"><g:fieldValue bean="${prefInstance}" field="decimale"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.data}">
				<li class="fieldcontain">
					<span id="data-label" class="property-label"><g:message code="pref.data.label" default="Data" /></span>
					
						<span class="property-value" aria-labelledby="data-label"><g:formatDate date="${prefInstance?.data}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.testo}">
				<li class="fieldcontain">
					<span id="testo-label" class="property-label"><g:message code="pref.testo.label" default="Testo" /></span>
					
						<span class="property-value" aria-labelledby="testo-label"><g:fieldValue bean="${prefInstance}" field="testo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="pref.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${prefInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${prefInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="pref.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${prefInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:prefInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${prefInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
