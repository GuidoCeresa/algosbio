













<%@ page import="it.algos.algosbio.BioGrails" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'bioGrails.label', default: 'BioGrails')}"/>
    <title><g:message code="bioGrails.list.label" args="[entityName]" default="Elenco"/></title>
</head>

<body>
<a href="#list-bioGrails" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                                  default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <g:if test="${!noMenuCreate}">
            <li><g:link class="create" action="create"><g:message code="bioGrails.new.label"
                                                                  args="[entityName]" default="Nuovo"/></g:link></li>
        </g:if>
        <g:if test="${menuExtra}">
            <algos:menuExtra menuExtra="${menuExtra}"></algos:menuExtra>
        </g:if>
    </ul>
</div>

<div id="list-bioGrails" class="content scaffold-list" role="main">

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
        <h1><g:message code="bioGrails.list.label" args="[entityName]" default="Elenco"/></h1>
    </g:else>

    <div class="pagination">
        <g:paginate total="${bioGrailsInstanceTotal}"/>
    </div>
    <table>
        <thead>
        <g:if test="${campiLista}">
            <algos:titoliLista campiLista="${campiLista}"></algos:titoliLista>
        </g:if>
        <g:else>
            <tr>
                
                <g:sortableColumn property="pageid"
                                  title="${message(code: 'bioGrails.pageid.label', default: 'Pageid')}"/>
                
                <g:sortableColumn property="title"
                                  title="${message(code: 'bioGrails.title.label', default: 'Title')}"/>
                
                <g:sortableColumn property="nome"
                                  title="${message(code: 'bioGrails.nome.label', default: 'Nome')}"/>
                
                <g:sortableColumn property="cognome"
                                  title="${message(code: 'bioGrails.cognome.label', default: 'Cognome')}"/>
                
                <g:sortableColumn property="forzaOrdinamento"
                                  title="${message(code: 'bioGrails.forzaOrdinamento.label', default: 'Forza Ordinamento')}"/>
                
                <g:sortableColumn property="sesso"
                                  title="${message(code: 'bioGrails.sesso.label', default: 'Sesso')}"/>
                
                <g:sortableColumn property="attivita"
                                  title="${message(code: 'bioGrails.attivita.label', default: 'Attivita')}"/>
                
                <g:sortableColumn property="attivita2"
                                  title="${message(code: 'bioGrails.attivita2.label', default: 'Attivita2')}"/>
                
            </tr>
        </g:else>
        </thead>
        <tbody>
        <g:if test="${campiLista}">
            <g:each in="${bioGrailsInstanceList}" status="i" var="bioGrailsInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <algos:rigaLista campiLista="${campiLista}" rec="${bioGrailsInstance}"></algos:rigaLista>
                </tr>
            </g:each>
        </g:if>
        <g:else>
            <g:each in="${bioGrailsInstanceList}" status="i" var="bioGrailsInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "pageid")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "title")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "nome")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "cognome")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "forzaOrdinamento")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "sesso")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "attivita")}</g:link></td>
                    
                    <td><g:link action="show"
                                id="${bioGrailsInstance.id}">${fieldValue(bean: bioGrailsInstance, field: "attivita2")}</g:link></td>
                    
                </tr>
            </g:each>
        </g:else>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${bioGrailsInstanceTotal}"/>
    </div>
    <g:if test="${application.usaExport}">
        <div class="buttons">
            <export:formats/>
        </div>
    </g:if>
</div>
</body>
</html>
