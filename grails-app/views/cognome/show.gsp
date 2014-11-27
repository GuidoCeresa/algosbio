
<%@ page import="it.algos.algosbio.Cognome" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cognome.label', default: 'Cognome')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-cognome" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="cognome.list.label"  default="Elenco"/></g:link></li>
                <g:if test="${!noMenuCreate}">
                    <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                          args="[entityName]"/></g:link></li>
                </g:if>
                <g:if test="${menuExtra}">
                    <algos:menuExtra menuExtra="${menuExtra}"></algos:menuExtra>
                </g:if>
			</ul>
		</div>
		<div id="show-cognome" class="content scaffold-show" role="main">
			<h1><g:message code="cognome.show.label" args="[entityName]" default="Mostra"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list cognome">
			
				<g:if test="${cognomeInstance?.testo}">
				<li class="fieldcontain">
					<span id="testo-label" class="property-label"><g:message code="cognome.testo.label" default="Testo" /></span>
					
						<span class="property-value" aria-labelledby="testo-label"><g:fieldValue bean="${cognomeInstance}" field="testo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cognomeInstance?.voci}">
				<li class="fieldcontain">
					<span id="voci-label" class="property-label"><g:message code="cognome.voci.label" default="Voci" /></span>
					
						<span class="property-value" aria-labelledby="voci-label"><g:fieldValue bean="${cognomeInstance}" field="voci"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cognomeInstance?.lunghezza}">
				<li class="fieldcontain">
					<span id="lunghezza-label" class="property-label"><g:message code="cognome.lunghezza.label" default="Lunghezza" /></span>
					
						<span class="property-value" aria-labelledby="lunghezza-label"><g:fieldValue bean="${cognomeInstance}" field="lunghezza"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cognomeInstance?.isVocePrincipale}">
				<li class="fieldcontain">
					<span id="isVocePrincipale-label" class="property-label"><g:message code="cognome.isVocePrincipale.label" default="Is Voce Principale" /></span>
					
						<span class="property-value" aria-labelledby="isVocePrincipale-label"><g:formatBoolean boolean="${cognomeInstance?.isVocePrincipale}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cognomeInstance?.voceRiferimento}">
				<li class="fieldcontain">
					<span id="voceRiferimento-label" class="property-label"><g:message code="cognome.voceRiferimento.label" default="Voce Riferimento" /></span>
					
						<span class="property-value" aria-labelledby="voceRiferimento-label"><g:link controller="cognome" action="show" id="${cognomeInstance?.voceRiferimento?.id}">${cognomeInstance?.voceRiferimento?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${cognomeInstance?.wikiUrl}">
				<li class="fieldcontain">
					<span id="wikiUrl-label" class="property-label"><g:message code="cognome.wikiUrl.label" default="Wiki Url" /></span>
					
						<span class="property-value" aria-labelledby="wikiUrl-label"><g:fieldValue bean="${cognomeInstance}" field="wikiUrl"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:cognomeInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${cognomeInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
