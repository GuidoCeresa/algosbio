import grails.util.Holder
import grails.util.Holders
import it.algos.algosbio.LibBio
import it.algos.algosbio.LibPass
import it.algos.algospref.Pref
import it.algos.algoswiki.ErrLogin
import it.algos.algoswiki.Login

class WikiBootStrap {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def grailsApplication

    //--metodo invocato direttamente da Grails
    def init = { servletContext ->


        Login login
        String nickname = Pref.getStr(LibBio.BOT_NAME)
        String password = LibPass.codifica(Pref.getStr(LibBio.BOT_PASSWORD))

        if (nickname && password) {
            login = new Login(nickname, password)
            assert login.isValido()
            assert login.getFirstResult() == ErrLogin.needToken
            assert login.getRisultato() == ErrLogin.success
            assert login.getToken().size() > 20
            assert login.getCookiePrefix() == 'itwiki'
            assert login.getSessionId().size() > 20
        }// fine del blocco if

        //--inietta una property a livello globale
        //--login del collegamento come bot
        if (login && login.isValido()) {
            //--registra il login nella property globale
            grailsApplication.config.loginBot = login
        } else {
            grailsApplication.config.loginBot = null
        }// fine del blocco if-else

        //--inietta una property a livello globale
        //--login normale del collegamento
        if (login && login.isValido()) {
            //--registra il login nella property globale
            grailsApplication.config.login = login
        } else {
            grailsApplication.config.login = null
        }// fine del blocco if-else

        //--inietta una property a livello globale
        //--login del collegamento come admin
        grailsApplication.config.loginAdmin = null
    }// fine della closure

    //--metodo invocato direttamente da Grails
    def destroy = {
    }// fine della closure

}// fine della classe di tipo BootStrap
