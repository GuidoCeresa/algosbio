package it.algos.algosbio

import it.algos.algos.DialogoController
import it.algos.algos.TipoDialogo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

class GenereController {

    static boolean transactional = false
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    private static int MAX = 100

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def genereService

    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def download() {
        params.tipo = TipoDialogo.conferma
        params.avviso = 'Download dalla pagina Modulo:Bio/Plurale attività genere. Vengono aggiunti nuovi generi ed aggiornati quelli esistenti.'
        params.returnController = 'genere'
        params.returnAction = 'downloadDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--aggiorna i records leggendoli dalla pagina wiki
    //--modifica i valori esistenti
    //--aggiunge nuovi valori
    def downloadDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Download non eseguito.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    genereService.download()
                    flash.message = 'Operazione effettuata. Sono stati aggiornati i valori dei generi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'index')
    } // fine del metodo

    def index(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra
        ArrayList campiLista
        def campoSort
        int recordsTotali
        String titoloLista = ''
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'genere', action: 'download', icon: 'frecciagiu', title: 'Download'],
        ]
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
        recordsTotali = Genere.count()

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond Genere.list(params), model: [titoloLista       : titoloLista,
                                             menuExtra         : menuExtra,
                                             campiLista        : campiLista,
                                             genereInstanceCount: recordsTotali,
                                             noMenuCreate      : noMenuCreate,
                                             params            : params]
    } // fine del metodo

    def show(Genere genereInstance) {
        respond genereInstance
    } // fine del metodo

    def create() {
        respond new Genere(params)
    } // fine del metodo

    @Transactional
    def save(Genere genereInstance) {
        if (genereInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (genereInstance.hasErrors()) {
            respond genereInstance.errors, view: 'create'
            return
        }// fine del blocco if

        genereInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'genere.label', default: 'Genere'), genereInstance.id])
                redirect genereInstance
            }// fine di form
            '*' { respond genereInstance, [status: CREATED] }
        }// fine di request
    } // fine del metodo

    def edit(Genere genereInstance) {
        respond genereInstance
    } // fine del metodo

    @Transactional
    def update(Genere genereInstance) {
        if (genereInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (genereInstance.hasErrors()) {
            respond genereInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        genereInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Genere.label', default: 'Genere'), genereInstance.id])
                redirect genereInstance
            }// fine di form
            '*' { respond genereInstance, [status: OK] }
        }// fine di request
    } // fine del metodo

    @Transactional
    def delete(Genere genereInstance) {

        if (genereInstance == null) {
            notFound()
            return
        }// fine del blocco if

        genereInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Genere.label', default: 'Genere'), genereInstance.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NO_CONTENT }
        }// fine di request
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'genere.label', default: 'Genere'), params.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo
} // fine della controller classe
