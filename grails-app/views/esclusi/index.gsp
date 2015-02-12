
<%@ page import="it.algos.algosbio.Esclusi" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'esclusi.label', default: 'Esclusi')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-esclusi" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
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

<div id="list-esclusi" class="content scaffold-list" role="main">

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
    <g:if test="${titoloLista}">
        <h1>${titoloLista}</h1>
    </g:if>
    <g:else>
        <h1><g:message code="esclusi.list.label" args="[entityName]" default="Elenco"/></h1>
    </g:else>

    <table>
        <thead>
        <g:if test="${campiLista}">
            <algos:titoliLista campiLista="${campiLista}"></algos:titoliLista>
        </g:if>
        <g:else>
            <tr>
                
                <g:sortableColumn property="pageid"
                                  title="${message(code: 'esclusi.pageid.label', default: 'Pageid')}"/>
                
                <g:sortableColumn property="title"
                                  title="${message(code: 'esclusi.title.label', default: 'Title')}"/>
                
            </tr>
        </g:else>
        </thead>
        <tbody>
        <g:if test="${campiLista}">
            <g:each in="${esclusiInstanceList}" status="i" var="esclusiInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <algos:rigaLista campiLista="${campiLista}" rec="${esclusiInstance}"></algos:rigaLista>
                </tr>
            </g:each>
        </g:if>
        <g:else>
            <g:each in="${esclusiInstanceList}" status="i" var="esclusiInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    
                    <td><g:link action="show"
                                id="${esclusiInstance.id}">${fieldValue(bean: esclusiInstance, field: "pageid")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${esclusiInstance.id}">${fieldValue(bean: esclusiInstance, field: "title")}</g:link></td>
                    
                </tr>
            </g:each>
        </g:else>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${esclusiInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
