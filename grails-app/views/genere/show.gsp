
<%@ page import="it.algos.algosbio.Genere" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genere.label', default: 'Genere')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-genere" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-genere" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list genere">
			
				<g:if test="${genereInstance?.singolare}">
				<li class="fieldcontain">
					<span id="singolare-label" class="property-label"><g:message code="genere.singolare.label" default="Singolare" /></span>
					
						<span class="property-value" aria-labelledby="singolare-label"><g:fieldValue bean="${genereInstance}" field="singolare"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${genereInstance?.plurale}">
				<li class="fieldcontain">
					<span id="plurale-label" class="property-label"><g:message code="genere.plurale.label" default="Plurale" /></span>
					
						<span class="property-value" aria-labelledby="plurale-label"><g:fieldValue bean="${genereInstance}" field="plurale"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:genereInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${genereInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
