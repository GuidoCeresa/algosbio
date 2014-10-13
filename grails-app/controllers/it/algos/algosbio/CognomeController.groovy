package it.algos.algosbio

import it.algos.algos.DialogoController
import it.algos.algos.TipoDialogo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = false)
class CognomeController {

    def cognomeService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    private static int MAX = 20

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
                        cognomeService.upload()
                        flash.message = 'Operazione effettuata. Sono state aggiornate le pagine dei cognomi'
                    } else {
                        flash.error = 'Devi essere loggato per poter modificare le pagine sul server wiki'
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'index', params: params)
    } // fine del metodo

    def list() {
        redirect(action: 'index', params: params)
    } // fine del metodo

    def index(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra = null
        ArrayList campiLista = null
        def campoSort
        int recordsTotali
        String titoloLista = ''
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'cognome', action: 'elabora', icon: 'frecciasu', title: 'Elabora'],
                [cont: 'cognome', action: 'upload', icon: 'frecciasu', title: 'Upload cognomi']
        ]
        params.menuExtra = menuExtra
        // fine della definizione

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        //--se vuoto, mostra i primi n (stabilito nel templates:scaffoldinf:list)
        //--    nell'ordine stabilito nella constraints della DomainClass
        campiLista = []
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
            params.order = 'asc'
        }// fine del blocco if-else

        //--selezione dei records da mostrare
        recordsTotali = Cognome.count()

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
        respond cognomeInstance
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
