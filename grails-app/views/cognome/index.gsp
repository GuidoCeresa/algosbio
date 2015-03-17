
<%@ page import="it.algos.algosbio.Cognome" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'cognome.label', default: 'Cognome')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-cognome" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
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

<div id="list-cognome" class="content scaffold-list" role="main">

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
        <h1><g:message code="cognome.list.label" args="[entityName]" default="Elenco"/></h1>
    </g:else>

    <div class="pagination">
        <g:paginate total="${cognomeInstanceCount ?: 0}"/>
    </div>
    <table>
        <thead>
        <g:if test="${campiLista}">
            <algos:titoliLista campiLista="${campiLista}"></algos:titoliLista>
        </g:if>
        <g:else>
            <tr>
                
                <g:sortableColumn property="testo"
                                  title="${message(code: 'cognome.testo.label', default: 'Testo')}"/>
                
                <g:sortableColumn property="voci"
                                  title="${message(code: 'cognome.voci.label', default: 'Voci')}"/>
                
                <g:sortableColumn property="lunghezza"
                                  title="${message(code: 'cognome.lunghezza.label', default: 'Lunghezza')}"/>
                
                <g:sortableColumn property="isVocePrincipale"
                                  title="${message(code: 'cognome.isVocePrincipale.label', default: 'Is Voce Principale')}"/>
                
                <th><g:message code="cognome.voceRiferimento.label" default="Voce Riferimento"/></th>
                
                <g:sortableColumn property="wikiUrl"
                                  title="${message(code: 'cognome.wikiUrl.label', default: 'Wiki Url')}"/>
                
            </tr>
        </g:else>
        </thead>
        <tbody>
        <g:if test="${campiLista}">
            <g:each in="${cognomeInstanceList}" status="i" var="cognomeInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <algos:rigaLista campiLista="${campiLista}" rec="${cognomeInstance}"></algos:rigaLista>
                </tr>
            </g:each>
        </g:if>
        <g:else>
            <g:each in="${cognomeInstanceList}" status="i" var="cognomeInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    
                    <td><g:link action="show"
                                id="${cognomeInstance.id}">${fieldValue(bean: cognomeInstance, field: "testo")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${cognomeInstance.id}">${fieldValue(bean: cognomeInstance, field: "voci")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${cognomeInstance.id}">${fieldValue(bean: cognomeInstance, field: "lunghezza")}</g:link></td>
                    
                    <g:if test="${cognomeInstance.isVocePrincipale!=null}">
                        <td><g:checkBox name="isVocePrincipale" value="${cognomeInstance.isVocePrincipale}"/></td>
                    </g:if>
                    
                    <td><g:link action="show"
                                id="${cognomeInstance.id}">${fieldValue(bean: cognomeInstance, field: "voceRiferimento")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${cognomeInstance.id}">${fieldValue(bean: cognomeInstance, field: "wikiUrl")}</g:link></td>
                    
                </tr>
            </g:each>
        </g:else>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${cognomeInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
