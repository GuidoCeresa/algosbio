<%@ page import="it.algos.algospref.Preferenze" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'pref.label', default: 'Preferenze')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-preferenze" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="pref.new.label" args="[entityName]" /></g:link></li>
        <li><g:link class="list" action="list"><g:message code="pref.list" args="[entityName]"/></g:link></li>
    </ul>
</div>
<div id="list-preferenze" class="content scaffold-list" role="main">
    <h1><g:message code="pref.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="id" title="Id" />
            <g:sortableColumn property="ordine" title="#"/>
            <g:sortableColumn property="code" title="Codice" />
            <g:sortableColumn property="type" title="Tipo" />
            <g:sortableColumn property="value" title="Valore" />
            <g:sortableColumn property="descrizione" title="Descrizione"/>
            <g:sortableColumn property="dateCreated" title="Creazione iniziale" />
            <g:sortableColumn property="lastUpdated" title="Ultima modifica" />
        </tr>
        </thead>
        <tbody>
        <g:each in="${preferenzeInstanceList}" status="i" var="preferenzeInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${preferenzeInstance.id}">${fieldValue(bean: preferenzeInstance, field: "id")}</g:link></td>

                <td><g:link action="show"
                            id="${preferenzeInstance.id}">${fieldValue(bean: preferenzeInstance, field: "ordine")}</g:link></td>

                <td><g:link action="show"
                            id="${preferenzeInstance.id}">${fieldValue(bean: preferenzeInstance, field: "code")}</g:link></td>

                <td><g:link action="show"
                            id="${preferenzeInstance.id}">${fieldValue(bean: preferenzeInstance, field: "type")}</g:link></td>

                <td><g:link action="show"
                            id="${preferenzeInstance.id}">${fieldValue(bean: preferenzeInstance, field: "value")}</g:link></td>

                <td><g:link action="show"
                            id="${preferenzeInstance.id}">${fieldValue(bean: preferenzeInstance, field: "descrizione")}</g:link></td>

                <td><g:formatDate date="${preferenzeInstance.dateCreated}" /></td>

                <td><g:formatDate date="${preferenzeInstance.lastUpdated}" /></td>

            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${preferenzeInstanceTotal}" />
    </div>
</div>
</body>
</html>
