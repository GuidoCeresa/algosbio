<%=packageName ? "package ${packageName}\n\n" : ''%>

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ${className}Controller {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
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
        recordsTotali = ${className}.count()

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond ${className}.list(params), model: [titoloLista       : titoloLista,
                                            menuExtra         : menuExtra,
                                            campiLista        : campiLista,
                                            provaInstanceCount: recordsTotali,
                                            params            : params]
    } // fine del metodo

    def show(${className} ${propertyName}) {
        respond ${propertyName}
    } // fine del metodo

    def create() {
        respond new ${className}(params)
    } // fine del metodo

    @Transactional
    def save(${className} ${propertyName}) {
        if (${propertyName} == null) {
            notFound()
            return
        }// fine del blocco if

        if (${propertyName}.hasErrors()) {
            respond ${propertyName}.errors, view:'create'
            return
        }// fine del blocco if

        ${propertyName}.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
                redirect action:'index', method:'GET'
            }// fine di form
            '*' { respond ${propertyName}, [status: CREATED] }
        }// fine di request
    } // fine del metodo

    def edit(${className} ${propertyName}) {
        respond ${propertyName}
    } // fine del metodo

    @Transactional
    def update(${className} ${propertyName}) {
        if (${propertyName} == null) {
            notFound()
            return
        }// fine del blocco if

        if (${propertyName}.hasErrors()) {
            respond ${propertyName}.errors, view:'edit'
            return
        }// fine del blocco if

        ${propertyName}.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: '${className}.label', default: '${className}'), ${propertyName}.id])
                redirect action:'index', method:'GET'
            }// fine di form
            '*'{ respond ${propertyName}, [status: OK] }
        }// fine di request
    } // fine del metodo

    @Transactional
    def delete(${className} ${propertyName}) {

        if (${propertyName} == null) {
            notFound()
            return
        }// fine del blocco if

        ${propertyName}.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: '${className}.label', default: '${className}'), ${propertyName}.id])
                redirect action:'index', method:'GET'
            }// fine di form
            '*'{ render status: NO_CONTENT }
        }// fine di request
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
                redirect action:'index', method:'GET'
            }// fine di form
            '*'{ render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo
} // fine della controller classe
