package it.algos.algosbio

import it.algos.algos.DialogoController
import it.algos.algos.TipoDialogo
import it.algos.algoslib.Lib
import it.algos.algospref.Pref

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = false)
class CognomeController {

    def cognomeService
    def bioService

    static boolean transactional = false
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    private static int MAX = 1000

    //--costruisce
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def costruisce() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Costruisce tutti i nuovi records'
        params.avviso = []
        params.avviso.add('Azzera (null) tutti i link tra BioGrails e Cognomi')
        params.avviso.add('Cancella tutti i records di Cognomi')
        params.avviso.add('Vengono creati nuovi records per tutti i cognomi presenti nelle voci (bioGrails)')
        params.avviso.add('Tempo indicativo: quattro ore')
        params.returnController = 'cognome'
        params.returnAction = 'costruisceDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--crea i records estraendoli dalle voci esistenti (bioGrails)
    def costruisceDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. BioGrails e Cognomi non modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (cognomeService) {
                        cognomeService.cancellaTutto()
                        cognomeService.aggiunge()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati creati i nuovi cognomi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--aggiunge
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def aggiunge() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Aggiunta nuovi records'
        params.avviso = []
        params.avviso.add('Vengono creati nuovi records per tutti i cognomi presenti nelle voci (bioGrails)')
        params.avviso.add('Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (cognome)')
        params.avviso.add('Cancella i records che non superano la soglia minima')
        params.avviso.add('Tempo indicativo: quattro ore')
        params.returnController = 'cognome'
        params.returnAction = 'aggiungeDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--crea i records estraendoli dalle voci esistenti (bioGrails)
    def aggiungeDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Cognomi non modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (cognomeService) {
                        cognomeService.aggiunge()
                        cognomeService.ricalcola()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati aggiunti tutti i nuovi cognomi e ricalcolati tutti i records'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--ricalcola
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def ricalcola() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Ricalcolo records esistenti'
        params.avviso = []
        params.avviso.add('Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (cognome)')
        params.avviso.add('Nelle preferenze si regola il numero di records da ricalcolare (MAX_RICALCOLA_COGNOMI)')
        params.avviso.add('Tempo indicativo: una ora')
        params.returnController = 'cognome'
        params.returnAction = 'ricalcolaDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (cognome)
    def ricalcolaDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Cognomi non modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (cognomeService) {
                        cognomeService.ricalcola()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati ricalcolati i cognomi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def elabora() {
        params.tipo = TipoDialogo.conferma
        params.avviso = []
        params.avviso.add('Cancellazione di tutti i record di cognomi.')
        params.avviso.add('Ricostruzione di tutti i valori unici del parametro cognome di BioGrails')
        params.avviso.add('Calcolo del numero di voci attuale usato da ogni cognome .')
        params.avviso.add('Occorrono diverse ore (circa 5).')
        params.avviso.add('Nelle preferenze può essere impostato un limite di record da ricreare.')
        params.avviso.add('Non è necessario essere loggato')
        params.alert = []
        params.alert.add('Se prosegui verranno cancellati e riscritti tutti i records')
        params.alert.add('Sei sicuro di voler continuare?')
        params.returnController = 'cognome'
        params.returnAction = 'elaboraDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    def elaboraDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Ricostruzione dei record di cognomi non eseguita.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    cognomeService.elabora()
                    flash.message = 'Operazione effettuata. Sono stati aggiornati i valori delle attività'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'index', params: params)
    } // fine del metodo

    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def upload() {
        params.tipo = TipoDialogo.conferma
        params.avviso = []
        params.avviso.add('Upload dei cognomi sul server wiki')
        params.avviso.add('Creazione delle pagine dedicate con le liste di voci per ogni cognome')
        params.alert = 'È necessario essere loggato'
        params.returnController = 'cognome'
        params.returnAction = 'uploadDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo


    def uploadDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Le liste non sono state create.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (grailsApplication && grailsApplication.config.login) {
                        uploadBase()
                        flash.message = 'Operazione effettuata. Sono state aggiornate le pagine dei cognomi'
                    } else {
                        flash.error = 'Devi essere loggato per poter modificare le pagine sul server wiki'
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'index', params: params)
    } // fine del metodo

    private uploadBase() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)

        if (cognomeService) {
            //--aggiorna il numero di voci per ogni cognome della lista (semi-statica)
            if (!debug) {
                cognomeService.ricalcola()
            }// fine del blocco if

            //--costruisce una lista di cognomi
            if (Pref.getBool(LibBio.USA_LISTE_BIO_COGNOMI)) {
                if (bioService) {
                    cognomeService.uploadAllCognomi(bioService)
                }// fine del blocco if
            } else {
                flash.error = 'Parte di programma non funzionante'
            }// fine del blocco if-else

            //--pagine di controllo
            cognomeService.creaPagineControllo()
        }// fine del blocco if
    } // fine del metodo

    //--elabora e crea sul server wiki le pagine di servizio
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def controllo() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Creazione pagine di servizio'
        params.avviso = []
        params.avviso.add('Crea Progetto:Antroponimi/Cognomi, Progetto:Antroponimi/Liste cognomi, Progetto:Antroponimi/Didascalie')
        params.returnController = 'cognome'
        params.returnAction = 'controlloDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--elabora e crea sul server wiki le pagine di servizio
    //--passa al metodo effettivo
    def controlloDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Pagine non create.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (cognomeService) {
                        cognomeService.creaPagineControllo()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono state ricreate le pagine di servizio'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--elabora e crea le liste del cognome indicato e le uploada sul server wiki
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadSingoloCognome(Long id) {
        Cognome cognome = Cognome.get(id)

        if (grailsApplication && grailsApplication.config.login) {
            if (cognome && bioService) {
                ListaCognome.uploadCognome(cognome, bioService)
                flash.message = "Eseguito upload sul server wiki delle pagine con le liste delle voci di nome ${cognome.testo}"
            } else {
                flash.error = "Non ho trovato le classi necessarie"
            }// fine del blocco if-else
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    def list() {
        redirect(action: 'index', params: params)
    } // fine del metodo

    def index(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra
        ArrayList campiLista
        def campoSort = 'voci'
        int recordsTotali
        String titoloLista
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'cognome', action: 'costruisce', icon: 'frecciasu', title: 'Ricrea tutti i records'],
                [cont: 'cognome', action: 'aggiunge', icon: 'frecciasu', title: 'Aggiungi records'],
                [cont: 'cognome', action: 'ricalcola', icon: 'frecciasu', title: 'Ricalcolo voci'],
                [cont: 'cognome', action: 'upload', icon: 'frecciasu', title: 'Upload cognomi'],
                [cont: 'cognome', action: 'controllo', icon: 'frecciasu', title: 'Ricrea pagine di servizio'],
                [cont: 'cognome', action: 'linkPagineServizio', icon: 'frecciasu', title: 'Pagine di servizio wiki'],
        ]
        params.menuExtra = menuExtra
        // fine della definizione

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        //--se vuoto, mostra i primi n (stabilito nel templates:scaffoldinf:list)
        //--    nell'ordine stabilito nella constraints della DomainClass
        campiLista = ['testo', 'voci', 'isVocePrincipale', 'voceRiferimento', 'wikiUrl']
        // fine della definizione

        //--regolazione dei campo di ordinamento
        //--regolazione dei parametri di ordinamento
        if (!params.sort) {
            if (campoSort) {
                params.sort = campoSort
            }// fine del blocco if
        }// fine del blocco if-else
        if (params.order) {
            if (params.order == 'asc') {
                params.order = 'desc'
            } else {
                params.order = 'asc'
            }// fine del blocco if-else
        } else {
            if (campoSort && campoSort.equals('voci')) {
                params.order = 'desc'
            } else {
                params.order = 'asc'
            }// fine del blocco if-else
        }// fine del blocco if-else

        //--selezione dei records da mostrare
        //--per una lista filtrata (parziale), modificare i parametri
        //--oppure modificare il findAllByInteroGreaterThan()...
        recordsTotali = Cognome.count()

        //--calcola il numero di record
        //--titolo visibile sopra la table dei dati
        titoloLista = 'Elenco di '
        titoloLista += Lib.Txt.formatNum(Cognome.list(params).size())
        titoloLista += ' records di cognomi su un totale di ' + Lib.Txt.formatNum(recordsTotali)

        //--aggiunta specifica di questo controller
        int numRecNonControllate = Cognome.countByVoci(CognomeService.TAG_DA_CONTROLLARE)
        int numRecControllate = Cognome.countByVociGreaterThan(Pref.getInt(LibBio.SOGLIA_COGNOMI, 20))
        titoloLista += ' di cui ' + Lib.Txt.formatNum(numRecNonControllate)
        titoloLista += ' non ancora controllati e '
        titoloLista += Lib.Txt.formatNum(numRecControllate)
        titoloLista += ' controllati.'

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond Cognome.list(params), model: [titoloLista         : titoloLista,
                                              menuExtra           : menuExtra,
                                              campiLista          : campiLista,
                                              cognomeInstanceCount: recordsTotali,
                                              noMenuCreate        : noMenuCreate,
                                              params              : params]
    } // fine del metodo

    def show(Cognome cognomeInstance) {
        ArrayList menuExtra
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        if (cognomeInstance.isVocePrincipale) {
            menuExtra = [
                    [cont: 'cognome', action: "uploadSingoloCognome/${cognomeInstance.id}", icon: 'database', title: 'UploadSingoloCognome'],
            ]
        } else {
            menuExtra = []
        }// fine del blocco if-else

        // fine della definizione

        //--presentazione della view (show), secondo il modello
        //--menuExtra può essere nullo o vuoto
        render(view: 'show', model: [
                cognomeInstance: cognomeInstance,
                menuExtra      : menuExtra,
                noMenuCreate   : noMenuCreate],
                params: params)
    } // fine del metodo

    def create() {
        respond new Cognome(params)
    } // fine del metodo

    @Transactional
    def save(Cognome cognomeInstance) {
        if (cognomeInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (cognomeInstance.hasErrors()) {
            respond cognomeInstance.errors, view: 'create'
            return
        }// fine del blocco if

        cognomeInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'cognome.label', default: 'Cognome'), cognomeInstance.id])
                redirect cognomeInstance
            }// fine di form
            '*' { respond cognomeInstance, [status: CREATED] }
        }// fine di request
    } // fine del metodo

    def edit(Cognome cognomeInstance) {
        respond cognomeInstance
    } // fine del metodo

    @Transactional
    def update(Cognome cognomeInstance) {
        if (cognomeInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (cognomeInstance.hasErrors()) {
            respond cognomeInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        cognomeInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Cognome.label', default: 'Cognome'), cognomeInstance.id])
                redirect cognomeInstance
            }// fine di form
            '*' { respond cognomeInstance, [status: OK] }
        }// fine di request
    } // fine del metodo

    @Transactional
    def delete(Cognome cognomeInstance) {

        if (cognomeInstance == null) {
            notFound()
            return
        }// fine del blocco if

        cognomeInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Cognome.label', default: 'Cognome'), cognomeInstance.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NO_CONTENT }
        }// fine di request
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'cognome.label', default: 'Cognome'), params.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo
} // fine della controller classe
