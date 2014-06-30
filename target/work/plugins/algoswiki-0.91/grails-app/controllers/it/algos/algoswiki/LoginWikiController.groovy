package it.algos.algoswiki

class LoginWikiController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(controller: 'LoginWiki', action: 'login', params: params)
    }// fine del metodo

    //--se chiamato con i necessari parametri, alla fine ritorna al chiamante
    //--se mancano i parametri, ritorna alla videata Home
    def login() {
        render(view: 'login', model: [
                returnController: params.returnController,
                returnAction: params.returnAction,
                targetUri: 'NonServe',
                nickname: 'Biobot'],
                params: params)
    }// fine del metodo

    //--dal form passa SEMPRE di qui
    //--controlla esistenza e valore dei parametri e ritorna al chiamante
    //--se i parametri non sono validi, ritorna alla videata Home
    def wikiLoginReturn() {
        String returnController
        String returnAction
        String nickname = ''
        String password = ''
        Login login

        if (params.nickname) {
            nickname = params.nickname
        }// fine del blocco if
        if (params.password) {
            password = params.password
        }// fine del blocco if
        if (params.returnController) {
            returnController = params.returnController
        }// fine del blocco if
        if (params.returnAction) {
            returnAction = params.returnAction
        }// fine del blocco if

        if (nickname && password) {
            login = new Login(nickname, password)
            if (login && login.isValido()) {
                assert login.isValido()
                assert login.getFirstResult() == ErrLogin.needToken
                assert login.getRisultato() == ErrLogin.success
                assert login.getToken().size() > 20
                assert login.getCookiePrefix() == 'itwiki'
                assert login.getSessionId().size() > 20

                //--registra il login nella property globale
                grailsApplication.config.login = login

                flash.message = 'Collegato come ' + login.getUserName()
            } else {
                grailsApplication.config.login = null
                flash.message = 'Non collegato'
            }// fine del blocco if-else
        }// fine del blocco if

        if (returnController && returnAction) {
            redirect(controller: returnController, action: returnAction, params: params)
        } else {
            redirect(uri: '/')
        }// fine del blocco if-else
    }// fine del metodo

}// fine del controller
