package it.algos.algospref

import org.springframework.dao.DataIntegrityViolationException

class PreferenzeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    //--la view è specifica perché ha un menu aggiuntivo non-standard
    //--uso un nome diverso, per evitare che la rigenerazione automatica delle view la sovrascriva
    def list(Integer max) {
        params.max = Math.min(max ?: 20, 100)

        if (!params.sort) {
            params.sort = 'ordine'
        }// fine del blocco if

        render(view: 'listnormale', model: [preferenzeInstanceList: Preferenze.list(params), preferenzeInstanceTotal: Preferenze.count()])
    } // fine del metodo


    def listcontrollo(Integer max) {
        params.max = Math.min(max ?: 20, 100)

        if (!params.sort) {
            params.sort = 'ordine'
        }// fine del blocco if

        [preferenzeInstanceList: Preferenze.list(params), preferenzeInstanceTotal: Preferenze.count()]
    } // fine del metodo

    def create() {
        def risposta = Preferenze.executeQuery('select ordine from Preferenze order by ordine desc limit 1')

        if (risposta && risposta instanceof ArrayList && risposta.size() > 0) {
            params.ordine = risposta[0] + 1
        } else {
            params.ordine = 1
        }// fine del blocco if-else

        [preferenzeInstance: new Preferenze(params)]
    } // fine del metodo

    def save() {
        def preferenzeInstance = new Preferenze(params)

        if (!preferenzeInstance.save(flush: true)) {
            render(view: 'create', model: [preferenzeInstance: preferenzeInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        flash.message = message(code: 'default.created.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), preferenzeInstance.id])
        redirect(action: 'show', id: preferenzeInstance.id)
    } // fine del metodo

    def show(Long id) {
        def preferenzeInstance = Preferenze.get(id)

        if (!preferenzeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), id])
            redirect(action: "list")
            return
        }// fine del blocco if e fine anticipata del metodo

        [preferenzeInstance: preferenzeInstance]
    } // fine del metodo

    def edit(Long id) {
        def preferenzeInstance = Preferenze.get(id)

        if (!preferenzeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        [preferenzeInstance: preferenzeInstance]
    } // fine del metodo

    def update(Long id, Long version) {
        def preferenzeInstance = Preferenze.get(id)

        if (!preferenzeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        if (version != null) {
            if (preferenzeInstance.version > version) {
                preferenzeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'preferenze.label', default: 'Preferenze')] as Object[],
                        "Another user has updated this Preferenze while you were editing")
                render(view: 'edit', model: [preferenzeInstance: preferenzeInstance])
                return
            }// fine del blocco if e fine anticipata del metodo
        }// fine del blocco if

        preferenzeInstance.properties = params

        if (!preferenzeInstance.save(flush: true)) {
            render(view: 'edit', model: [preferenzeInstance: preferenzeInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        flash.message = message(code: 'default.updated.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), preferenzeInstance.id])
        redirect(action: 'show', id: preferenzeInstance.id)
    } // fine del metodo

    def delete(Long id) {
        def preferenzeInstance = Preferenze.get(id)
        if (!preferenzeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        try {
            preferenzeInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), id])
            redirect(action: 'list')
        }// fine del blocco try
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'preferenze.label', default: 'Preferenze'), id])
            redirect(action: 'show', id: id)
        }// fine del blocco catch
    } // fine del metodo

} // fine della controller classe
