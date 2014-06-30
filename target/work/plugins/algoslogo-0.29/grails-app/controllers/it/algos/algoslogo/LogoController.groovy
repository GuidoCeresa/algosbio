package it.algos.algoslogo


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class LogoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Logo.list(params), model: [logoInstanceCount: Logo.count()]
    }

    def show(Logo logoInstance) {
        respond logoInstance
    }

    def create() {
        respond new Logo(params)
    }

    @Transactional
    def save(Logo logoInstance) {
        if (logoInstance == null) {
            notFound()
            return
        }

        if (logoInstance.hasErrors()) {
            respond logoInstance.errors, view: 'create'
            return
        }

        logoInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'logo.label', default: 'Logo'), logoInstance.id])
                redirect logoInstance
            }
            '*' { respond logoInstance, [status: CREATED] }
        }
    }

    def edit(Logo logoInstance) {
        respond logoInstance
    }

    @Transactional
    def update(Logo logoInstance) {
        if (logoInstance == null) {
            notFound()
            return
        }

        if (logoInstance.hasErrors()) {
            respond logoInstance.errors, view: 'edit'
            return
        }

        logoInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Logo.label', default: 'Logo'), logoInstance.id])
                redirect logoInstance
            }
            '*' { respond logoInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Logo logoInstance) {

        if (logoInstance == null) {
            notFound()
            return
        }

        logoInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Logo.label', default: 'Logo'), logoInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'logo.label', default: 'Logo'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
