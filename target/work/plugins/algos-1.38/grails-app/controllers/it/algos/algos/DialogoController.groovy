/* Created by Algos s.r.l. */
/* Date: mag 2013 */
/* Il plugin Algos ha creato o sovrascritto il templates che ha creato questo file. */
/* L'header del templates serve per controllare le successive release */
/* (tramite il flag di controllo aggiunto) */
/* Tipicamente VERRA sovrascritto (il template, non il file) ad ogni nuova release */
/* del plugin per rimanere aggiornato. */
/* Se vuoi che le prossime release del plugin NON sovrascrivano il template che */
/* genera questo file, perdendo tutte le modifiche precedentemente effettuate, */
/* regola a false il flag di controllo flagOverwrite© del template stesso. */
/* (non quello del singolo file) */
/* flagOverwrite = true */

package it.algos.algos

class DialogoController {
    public static final String DIALOGO_ANNULLA = 'Annulla'
    public static final String DIALOGO_CONFERMA = 'Conferma'

    def index() {
        flash.error = 'Dialogo è un controller solamente per uso interno'
        redirect(uri: '/')
    } // fine del metodo

    //--se chiamato con i necessari parametri, alla fine ritorna al chiamante
    //--se mancano i parametri, ritorna alla videata Home
    def box = {
        TipoDialogo tipo = TipoDialogo.inputTesto
        String view = 'testo'
        String titolo = 'Dialogo'
        def avviso = ''
        def alert=''
        String label = 'Valore richiesto:'
        String valore = 'Inserisci qui il valore'
        String labelDirectory = ''
        String valoreDirectory = ''
        String labelNomeFile = ''
        String valoreNomeFile = ''

        if (!params.returnController) {
            params.returnController = ''
        }// fine del blocco if
        if (!params.returnAction) {
            params.returnAction = ''
        }// fine del blocco if

        if (params.tipo) {
            tipo = params.tipo
        }// fine del blocco if

        if (params.titolo) {
            titolo = params.titolo
        }// fine del blocco if

        if (params.avviso) {
            avviso = params.avviso
        }// fine del blocco if

        if (params.alert) {
            alert = params.alert
        }// fine del blocco if

        switch (tipo) {
        //--avviso e bottone di conferma
            case TipoDialogo.avviso:
                view = 'avviso'
                if (!avviso) {
                    avviso = 'Messaggio di avviso'
                }// fine del blocco if
                break
        //--avviso e due bottoni: di conferma e di annulla
            case TipoDialogo.conferma:
                view = 'conferma'
                if (!avviso && !alert) {
                    avviso = 'Messaggio di conferma'
                }// fine del blocco if
                if (!alert) {
                    alert = 'Sei sicuro di volerlo fare?'
                }// fine del blocco if
                break
        //--campo testo di inserimento
            case TipoDialogo.inputTesto:
                view = 'testo'
                break
        //--inserimento di un file
            case TipoDialogo.inputFile:
                view = 'file'
                avviso = 'Selezione di un file dal disco locale. Devi scrivere la directory e selezionare un file. Premi poi il bottone Conferma'
                labelDirectory = 'Directory del file'
                valoreDirectory = '/Users/Xxx/Desktop/'
                labelNomeFile = 'Nome del file:'
                valoreNomeFile = 'Inserisci qui il nome (completo) del file'
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        if (avviso) {
            if (avviso instanceof String) {
                flash.message = avviso
            }// fine del blocco if
            if (avviso instanceof String[]) {
                flash.messages = avviso
            }// fine del blocco if
        }// fine del blocco if
        if (alert) {
            if (alert instanceof String) {
                flash.error = alert
            }// fine del blocco if
            if (alert instanceof String[]) {
                flash.errors = alert
            }// fine del blocco if
        }// fine del blocco if

        render(view: view, model: [
                returnController: params.returnController,
                returnAction: params.returnAction,
                titolo: titolo,
                label: label,
                valore: valore,
                labelDirectory: labelDirectory,
                valoreDirectory: valoreDirectory,
                labelNomeFile: labelNomeFile,
                valoreNomeFile: valoreNomeFile],
                params: params)
    } // fine della closure

    //--dal form passa SEMPRE di qui
    //--controlla esistenza e valore dei parametri e ritorna al chiamante
    //--se i parametri non sono validi, ritorna alla videata Home
    def boxReturn = {
        String returnController
        String returnAction

        if (params.returnController) {
            returnController = params.returnController
        }// fine del blocco if
        if (params.returnAction) {
            returnAction = params.returnAction
        }// fine del blocco if

        if (returnController && returnAction) {
            redirect(controller: returnController, action: returnAction, params: params)
        } else {
            redirect(uri: '/')
        }// fine del blocco if-else
    }// fine del metodo

    //--dal form passa SEMPRE di qui
    //--controlla esistenza e valore dei parametri e ritorna al chiamante
    //--se i parametri non sono validi, ritorna alla videata Home
    def annulla() {
        if (params._action_Annulla) {
            params.remove('_action_Annulla')
            params.valore = DIALOGO_ANNULLA
        }// fine del blocco if

        redirect(action: 'boxReturn', params: params)
    }// fine del metodo

    //--dal form passa SEMPRE di qui
    //--controlla esistenza e valore dei parametri e ritorna al chiamante
    //--se i parametri non sono validi, ritorna alla videata Home
    def conferma() {
        if (params._action_Conferma) {
            params.remove('_action_Conferma')
            params.valore = DIALOGO_CONFERMA
        }// fine del blocco if

        redirect(action: 'boxReturn', params: params)
    }// fine del metodo

    def pippo() {
        flash.error = 'Pippo superstar'
        redirect(uri: '/')
    } // fine del metodo

} // fine della classe controller
