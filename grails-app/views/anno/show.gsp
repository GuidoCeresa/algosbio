
<%@ page import="it.algos.algosbio.Anno" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'anno.label', default: 'Anno')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-anno" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-anno" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list anno">
			
				<g:if test="${annoInstance?.progressivoCategoria}">
				<li class="fieldcontain">
					<span id="progressivoCategoria-label" class="property-label"><g:message code="anno.progressivoCategoria.label" default="Progressivo Categoria" /></span>
					
						<span class="property-value" aria-labelledby="progressivoCategoria-label"><g:fieldValue bean="${annoInstance}" field="progressivoCategoria"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${annoInstance?.titolo}">
				<li class="fieldcontain">
					<span id="titolo-label" class="property-label"><g:message code="anno.titolo.label" default="Titolo" /></span>
					
						<span class="property-value" aria-labelledby="titolo-label"><g:fieldValue bean="${annoInstance}" field="titolo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${annoInstance?.secolo}">
				<li class="fieldcontain">
					<span id="secolo-label" class="property-label"><g:message code="anno.secolo.label" default="Secolo" /></span>
					
						<span class="property-value" aria-labelledby="secolo-label"><g:fieldValue bean="${annoInstance}" field="secolo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${annoInstance?.sporcoMorto}">
				<li class="fieldcontain">
					<span id="sporcoMorto-label" class="property-label"><g:message code="anno.sporcoMorto.label" default="Sporco Morto" /></span>
					
						<span class="property-value" aria-labelledby="sporcoMorto-label"><g:formatBoolean boolean="${annoInstance?.sporcoMorto}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${annoInstance?.sporcoNato}">
				<li class="fieldcontain">
					<span id="sporcoNato-label" class="property-label"><g:message code="anno.sporcoNato.label" default="Sporco Nato" /></span>
					
						<span class="property-value" aria-labelledby="sporcoNato-label"><g:formatBoolean boolean="${annoInstance?.sporcoNato}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:annoInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${annoInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
