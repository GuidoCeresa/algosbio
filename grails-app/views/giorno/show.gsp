
<%@ page import="it.algos.algosbio.Giorno" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'giorno.label', default: 'Giorno')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-giorno" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="giorno.list.label"  default="Elenco"/></g:link></li>
                <g:if test="${!noMenuCreate}">
                    <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                          args="[entityName]"/></g:link></li>
                </g:if>
                <g:if test="${menuExtra}">
                    <algos:menuExtra menuExtra="${menuExtra}"> </algos:menuExtra>
                </g:if>
			</ul>
		</div>
		<div id="show-giorno" class="content scaffold-show" role="main">
			<h1><g:message code="giorno.show.label" args="[entityName]" default="Mostra"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list giorno">
			
				<g:if test="${giornoInstance?.normale}">
				<li class="fieldcontain">
					<span id="normale-label" class="property-label"><g:message code="giorno.normale.label" default="Normale" /></span>
					
						<span class="property-value" aria-labelledby="normale-label"><g:fieldValue bean="${giornoInstance}" field="normale"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${giornoInstance?.bisestile}">
				<li class="fieldcontain">
					<span id="bisestile-label" class="property-label"><g:message code="giorno.bisestile.label" default="Bisestile" /></span>
					
						<span class="property-value" aria-labelledby="bisestile-label"><g:fieldValue bean="${giornoInstance}" field="bisestile"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${giornoInstance?.nome}">
				<li class="fieldcontain">
					<span id="nome-label" class="property-label"><g:message code="giorno.nome.label" default="Nome" /></span>
					
						<span class="property-value" aria-labelledby="nome-label"><g:fieldValue bean="${giornoInstance}" field="nome"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${giornoInstance?.titolo}">
				<li class="fieldcontain">
					<span id="titolo-label" class="property-label"><g:message code="giorno.titolo.label" default="Titolo" /></span>
					
						<span class="property-value" aria-labelledby="titolo-label"><g:fieldValue bean="${giornoInstance}" field="titolo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${giornoInstance?.mese}">
				<li class="fieldcontain">
					<span id="mese-label" class="property-label"><g:message code="giorno.mese.label" default="Mese" /></span>
					
						<span class="property-value" aria-labelledby="mese-label"><g:fieldValue bean="${giornoInstance}" field="mese"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${giornoInstance?.sporcoNato}">
				<li class="fieldcontain">
					<span id="sporcoNato-label" class="property-label"><g:message code="giorno.sporcoNato.label" default="Sporco Nato" /></span>
					
						<span class="property-value" aria-labelledby="sporcoNato-label"><g:formatBoolean boolean="${giornoInstance?.sporcoNato}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${giornoInstance?.sporcoMorto}">
				<li class="fieldcontain">
					<span id="sporcoMorto-label" class="property-label"><g:message code="giorno.sporcoMorto.label" default="Sporco Morto" /></span>
					
						<span class="property-value" aria-labelledby="sporcoMorto-label"><g:formatBoolean boolean="${giornoInstance?.sporcoMorto}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:giornoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${giornoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
