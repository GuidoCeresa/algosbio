
<%@ page import="it.algos.algosbio.Localita" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'localita.label', default: 'Localita')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-localita" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                                  default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <g:if test="${!noMenuCreate}">
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </g:if>
        <g:if test="${menuExtra}">
            <algos:menuExtra menuExtra="${menuExtra}"></algos:menuExtra>
        </g:if>
    </ul>
</div>

<div id="list-localita" class="content scaffold-list" role="main">

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="errors" role="status">${flash.error}</div>
    </g:if>
    <g:if test="${flash.messages}">
        <g:each in="${flash.messages}" status="i" var="singoloMessaggio">
            <div class="message" role="status">${singoloMessaggio}</div>
        </g:each>
    </g:if>
    <g:if test="${flash.errors}">
        <g:each in="${flash.errors}" status="i" var="singoloErrore">
            <div class="errors" role="status">${singoloErrore}</div>
        </g:each>
    </g:if>
    <h1><g:message code="localita.list.label" args="[entityName]" default="Elenco"/></h1>

    <table>
        <thead>
        <g:if test="${campiLista}">
            <algos:titoliLista campiLista="${campiLista}"></algos:titoliLista>
        </g:if>
        <g:else>
            <tr>
                
                <g:sortableColumn property="nome"
                                  title="${message(code: 'localita.nome.label', default: 'Nome')}"/>
                
                <g:sortableColumn property="nati"
                                  title="${message(code: 'localita.nati.label', default: 'Nati')}"/>
                
                <g:sortableColumn property="morti"
                                  title="${message(code: 'localita.morti.label', default: 'Morti')}"/>
                
            </tr>
        </g:else>
        </thead>
        <tbody>
        <g:if test="${campiLista}">
            <g:each in="${localitaInstanceList}" status="i" var="localitaInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <algos:rigaLista campiLista="${campiLista}" rec="${localitaInstance}"></algos:rigaLista>
                </tr>
            </g:each>
        </g:if>
        <g:else>
            <g:each in="${localitaInstanceList}" status="i" var="localitaInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    
                    <td><g:link action="show"
                                id="${localitaInstance.id}">${fieldValue(bean: localitaInstance, field: "nome")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${localitaInstance.id}">${fieldValue(bean: localitaInstance, field: "nati")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${localitaInstance.id}">${fieldValue(bean: localitaInstance, field: "morti")}</g:link></td>
                    
                </tr>
            </g:each>
        </g:else>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${localitaInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
