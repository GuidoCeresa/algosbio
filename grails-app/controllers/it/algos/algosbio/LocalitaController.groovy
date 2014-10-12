package it.algos.algosbio


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class LocalitaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def localitaService

    private static int MAX = 100

    def elabora() {
        localitaService.elabora()
        redirect(action: 'index')
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
                [cont: 'localita', action: 'elabora', icon: 'frecciasu', title: 'Upload località']
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
        recordsTotali = Localita.count()

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond Localita.list(params), model: [titoloLista          : titoloLista,
                                               menuExtra            : menuExtra,
                                               campiLista           : campiLista,
                                               localitaInstanceCount: recordsTotali,
                                               noMenuCreate         : noMenuCreate,
                                               params               : params]
    } // fine del metodo

    def show(Localita localitaInstance) {
        respond localitaInstance
    } // fine del metodo

    def create() {
        respond new Localita(params)
    } // fine del metodo

    @Transactional
    def save(Localita localitaInstance) {
        if (localitaInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (localitaInstance.hasErrors()) {
            respond localitaInstance.errors, view: 'create'
            return
        }// fine del blocco if

        localitaInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'localita.label', default: 'Localita'), localitaInstance.id])
                redirect localitaInstance
            }// fine di form
            '*' { respond localitaInstance, [status: CREATED] }
        }// fine di request
    } // fine del metodo

    def edit(Localita localitaInstance) {
        respond localitaInstance
    } // fine del metodo

    @Transactional
    def update(Localita localitaInstance) {
        if (localitaInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (localitaInstance.hasErrors()) {
            respond localitaInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        localitaInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Localita.label', default: 'Localita'), localitaInstance.id])
                redirect localitaInstance
            }// fine di form
            '*' { respond localitaInstance, [status: OK] }
        }// fine di request
    } // fine del metodo

    @Transactional
    def delete(Localita localitaInstance) {

        if (localitaInstance == null) {
            notFound()
            return
        }// fine del blocco if

        localitaInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Localita.label', default: 'Localita'), localitaInstance.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NO_CONTENT }
        }// fine di request
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'localita.label', default: 'Localita'), params.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo
} // fine della controller classe
