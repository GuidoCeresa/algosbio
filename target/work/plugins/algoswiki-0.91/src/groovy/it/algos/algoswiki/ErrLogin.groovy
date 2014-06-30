package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:23
 */

public enum ErrLogin {

    success('Success', ""),
    noName('NoName', "You didn't set the lgname parameter"),
    illegal('Illegal', "You provided an illegal username"),
    notExists('NotExists', "The username you provided doesn't exist"),
    emptyPass('EmptyPass', "You didn't set the lgpassword parameter or you left it empty"),
    wrongPass('WrongPass', "The password you provided is incorrect"),
    wrongPluginPass('WrongPluginPass', "Same as WrongPass, returned when an authentication plugin rather than MediaWiki itself rejected the password"),
    createBlocked('CreateBlocked', "The wiki tried to automatically create a new account for you, but your IP address has been blocked from account creation"),
    throttled('Throttled', "You've logged in too many times in a short time. See also throttling"),
    blocked('Blocked', "User is blocked"),
    mustbeposted('mustbeposted', "The login module requires a POST request"),
    needToken('NeedToken', "Either you did not provide the login token or the sessionid cookie. Request again with the token and cookie given in this response"),
    lettura('Lettura', "Solo lettura pagina singola"),
    generico('generico', "errore generico"),
    noHost('noHost', "java.net.UnknownHostException")

    String tag
    String messaggio


    ErrLogin(String tag, String messaggio) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag)
        this.setMessaggio(messaggio)
    }

    /**
     * Restituisce l'errore dal tag
     *
     * @param tag
     * @return errore
     */
    public static get = {String tag ->
        // variabili e costanti locali di lavoro
        ErrLogin errore = null
        String tagCorrente

        // controllo di congruità
        if (tag) {
            ErrLogin.each {
                tagCorrente = it.getTag()
                if (tagCorrente.equals(tag)) {
                    errore = it
                }// fine del blocco if
            }// fine di each
        }// fine del blocco if

        // valore di ritorno
        return errore
    } // fine della closure


    private void setTag(String tag) {
        this.tag = tag
    }


    public String getTag() {
        return tag
    }


    private void setMessaggio(String messaggio) {
        this.messaggio = messaggio
    }


    public String getMessaggio() {
        return messaggio
    }

} // fine della Enumeration
