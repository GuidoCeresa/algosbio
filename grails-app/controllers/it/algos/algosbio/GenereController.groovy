package it.algos.algosbio


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GenereController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    private static int MAX = 20

    def index(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra = null
        ArrayList campiLista = null
        def campoSort
        int recordsTotali
        String titoloLista = ''

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = []
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
        recordsTotali = Genere.count()

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond Genere.list(params), model: [titoloLista       : titoloLista,
                                             menuExtra         : menuExtra,
                                             campiLista        : campiLista,
                                             provaInstanceCount: recordsTotali,
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
