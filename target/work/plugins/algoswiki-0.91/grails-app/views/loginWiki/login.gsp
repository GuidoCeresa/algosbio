<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <title>Login</title>
</head>
<body>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<g:form action="wikiLoginReturn">
    <input type="hidden" name="targetUri" value="${targetUri}" />
    <input type="hidden" name="returnController" value="${returnController}" />
    <input type="hidden" name="returnAction" value="${returnAction}" />
    <table>
        <tbody>
        <tr>
            <td>Nome del bot:</td>
            <td><input type="text" name="nickname" value="${nickname}" /></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" value="" /></td>
        </tr>
        <tr>
            <td>Remember me?:</td>
            <td><g:checkBox name="rememberMe" value="${rememberMe}" /></td>
        </tr>
        <tr>
            <td />
            <td><input type="submit" value="Login" /></td>
        </tr>
        </tbody>
    </table>
</g:form>
</body>
</html>
