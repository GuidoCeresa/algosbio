
<%@ page import="it.algos.algospref.Pref" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pref.label', default: 'Pref')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pref" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-pref" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="ordine" title="${message(code: 'pref.ordine.label', default: 'Ordine')}" />
					
						<g:sortableColumn property="type" title="${message(code: 'pref.type.label', default: 'Type')}" />
					
						<g:sortableColumn property="code" title="${message(code: 'pref.code.label', default: 'Code')}" />
					
						<g:sortableColumn property="descrizione" title="${message(code: 'pref.descrizione.label', default: 'Descrizione')}" />
					
						<g:sortableColumn property="stringa" title="${message(code: 'pref.stringa.label', default: 'Stringa')}" />
					
						<g:sortableColumn property="bool" title="${message(code: 'pref.bool.label', default: 'Bool')}" />
					
						<g:sortableColumn property="intero" title="${message(code: 'pref.intero.label', default: 'Intero')}" />
					
						<g:sortableColumn property="lungo" title="${message(code: 'pref.lungo.label', default: 'Lungo')}" />
					
						<g:sortableColumn property="reale" title="${message(code: 'pref.reale.label', default: 'Reale')}" />
					
						<g:sortableColumn property="doppio" title="${message(code: 'pref.doppio.label', default: 'Doppio')}" />
					
						<g:sortableColumn property="decimale" title="${message(code: 'pref.decimale.label', default: 'Decimale')}" />
					
						<g:sortableColumn property="data" title="${message(code: 'pref.data.label', default: 'Data')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${prefInstanceList}" status="i" var="prefInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${prefInstance.id}">${fieldValue(bean: prefInstance, field: "ordine")}</g:link></td>
					
						<td>${fieldValue(bean: prefInstance, field: "type")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "code")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "descrizione")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "stringa")}</td>
					
						<td><g:formatBoolean boolean="${prefInstance.bool}" /></td>
					
						<td>${fieldValue(bean: prefInstance, field: "intero")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "lungo")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "reale")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "doppio")}</td>
					
						<td>${fieldValue(bean: prefInstance, field: "decimale")}</td>
					
						<td><g:formatDate date="${prefInstance.data}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${prefInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
