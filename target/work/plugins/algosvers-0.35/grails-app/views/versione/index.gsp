
<%@ page import="it.algos.algosvers.Versione" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'versione.label', default: 'Versione')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-versione" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-versione" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="numero" title="${message(code: 'versione.numero.label', default: 'Numero')}" />
					
						<g:sortableColumn property="giorno" title="${message(code: 'versione.giorno.label', default: 'Giorno')}" />
					
						<g:sortableColumn property="titolo" title="${message(code: 'versione.titolo.label', default: 'Titolo')}" />
					
						<g:sortableColumn property="descrizione" title="${message(code: 'versione.descrizione.label', default: 'Descrizione')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${versioneInstanceList}" status="i" var="versioneInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${versioneInstance.id}">${fieldValue(bean: versioneInstance, field: "numero")}</g:link></td>
					
						<td><g:formatDate date="${versioneInstance.giorno}" /></td>
					
						<td>${fieldValue(bean: versioneInstance, field: "titolo")}</td>
					
						<td>${fieldValue(bean: versioneInstance, field: "descrizione")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${versioneInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
