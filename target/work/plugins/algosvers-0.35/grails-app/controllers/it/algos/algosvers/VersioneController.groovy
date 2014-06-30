package it.algos.algosvers



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class VersioneController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Versione.list(params), model:[versioneInstanceCount: Versione.count()]
    }

    def show(Versione versioneInstance) {
        respond versioneInstance
    }

    def create() {
        respond new Versione(params)
    }

    @Transactional
    def save(Versione versioneInstance) {
        if (versioneInstance == null) {
            notFound()
            return
        }

        if (versioneInstance.hasErrors()) {
            respond versioneInstance.errors, view:'create'
            return
        }

        versioneInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'versione.label', default: 'Versione'), versioneInstance.id])
                redirect versioneInstance
            }
            '*' { respond versioneInstance, [status: CREATED] }
        }
    }

    def edit(Versione versioneInstance) {
        respond versioneInstance
    }

    @Transactional
    def update(Versione versioneInstance) {
        if (versioneInstance == null) {
            notFound()
            return
        }

        if (versioneInstance.hasErrors()) {
            respond versioneInstance.errors, view:'edit'
            return
        }

        versioneInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Versione.label', default: 'Versione'), versioneInstance.id])
                redirect versioneInstance
            }
            '*'{ respond versioneInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Versione versioneInstance) {

        if (versioneInstance == null) {
            notFound()
            return
        }

        versioneInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Versione.label', default: 'Versione'), versioneInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'versione.label', default: 'Versione'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
