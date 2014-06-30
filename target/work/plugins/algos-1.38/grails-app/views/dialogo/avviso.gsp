<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <title>Avviso</title>
</head>
<body>

<g:if test="${titolo}">
    <div class="algosTitolo">${titolo}</div>
</g:if>

<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>
<g:if test="${flash.messages}">
    <g:each in="${flash.messages}" status="i" var="singoloMessaggio">
        <div class="message" role="status">${singoloMessaggio}</div>
    </g:each>
</g:if>
<g:if test="${flash.error}">
    <div class="errors" role="status">${flash.error}</div>
</g:if>

<g:if test="${flash.errors}">
    <g:each in="${flash.errors}" status="i" var="singoloErrore">
        <div class="errors" role="status">${singoloErrore}</div>
    </g:each>
</g:if>

<g:form action="boxReturn">
    <input type="hidden" name="returnController" value="${returnController}" />
    <input type="hidden" name="returnAction" value="${returnAction}" />
    <table>
        <tbody>
        <tr>
            <td><input type="submit" value="Continua" /></td>
        </tr>
        </tbody>
    </table>
</g:form>
</body>
</html>
