package it.algos.algosbio


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import it.algos.algoslib.Lib

@Transactional(readOnly = false)
class EsclusiController {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def exportService

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
    private static int MAX = 20

    def index(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra
        ArrayList campiLista
        def campoSort
        int recordsTotali
        String titoloLista = ''
        def noMenuCreate = false

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
        //--per una lista filtrata (parziale), modificare i parametri
        //--oppure modificare il findAllByInteroGreaterThan()...
        recordsTotali = Esclusi.count()

        //--calcola il numero di record
        //--titolo visibile sopra la table dei dati
        titoloLista = 'Elenco di '
        titoloLista += Lib.Txt.formatNum(Esclusi.list(params).size())
        titoloLista += ' records su un totale di ' + Lib.Txt.formatNum(recordsTotali)

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond Esclusi.list(params), model: [titoloLista       : titoloLista,
                                              menuExtra         : menuExtra,
                                              campiLista        : campiLista,
                                              provaInstanceCount: recordsTotali,
                                              noMenuCreate      : noMenuCreate,
                                              params            : params]
    } // fine del metodo

    def show(Esclusi esclusiInstance) {
        respond esclusiInstance
    } // fine del metodo

    def create() {
        respond new Esclusi(params)
    } // fine del metodo

    @Transactional
    def save(Esclusi esclusiInstance) {
        if (esclusiInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (esclusiInstance.hasErrors()) {
            respond esclusiInstance.errors, view: 'create'
            return
        }// fine del blocco if

        esclusiInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'esclusi.label', default: 'Esclusi'), esclusiInstance.id])
                redirect esclusiInstance
            }// fine di form
            '*' { respond esclusiInstance, [status: CREATED] }
        }// fine di request
    } // fine del metodo

    def edit(Esclusi esclusiInstance) {
        respond esclusiInstance
    } // fine del metodo

    @Transactional
    def update(Esclusi esclusiInstance) {
        if (esclusiInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (esclusiInstance.hasErrors()) {
            respond esclusiInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        esclusiInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Esclusi.label', default: 'Esclusi'), esclusiInstance.id])
                redirect esclusiInstance
            }// fine di form
            '*' { respond esclusiInstance, [status: OK] }
        }// fine di request
    } // fine del metodo

    @Transactional
    def delete(Esclusi esclusiInstance) {

        if (esclusiInstance == null) {
            notFound()
            return
        }// fine del blocco if

        esclusiInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Esclusi.label', default: 'Esclusi'), esclusiInstance.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NO_CONTENT }
        }// fine di request
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'esclusi.label', default: 'Esclusi'), params.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo
} // fine della controller classe
