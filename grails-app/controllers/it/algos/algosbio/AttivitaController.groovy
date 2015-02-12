/* Created by Algos s.r.l. */
/* Date: mag 2013 */
/* Il plugin Algos ha creato o sovrascritto il templates che ha creato questo file. */
/* L'header del templates serve per controllare le successive release */
/* (tramite il flag di controllo aggiunto) */
/* Tipicamente VERRA sovrascritto (il template, non il file) ad ogni nuova release */
/* del plugin per rimanere aggiornato. */
/* Se vuoi che le prossime release del plugin NON sovrascrivano il template che */
/* genera questo file, perdendo tutte le modifiche precedentemente effettuate, */
/* regola a false il flag di controllo flagOverwrite© del template stesso. */
/* (non quello del singolo file) */
/* flagOverwrite = true */

package it.algos.algosbio

import it.algos.algos.DialogoController
import it.algos.algos.TipoDialogo
import it.algos.algoslib.Lib
import it.algos.algoslib.LibTesto
import it.algos.algoswiki.Login
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.springframework.dao.DataIntegrityViolationException

class AttivitaController {

    static boolean transactional = false
    static allowedMethods = [save: 'POST', update: 'POST', delete: 'POST']

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def exportService
    def logoService
    def eventoService
    def attivitaService
    def listaService
    def statisticheService
    def mailService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def download() {
        params.tipo = TipoDialogo.conferma
        params.avviso = 'Download dalla pagina Modulo:Bio/Plurale attività. Vengono aggiunte nuove attività e aggiornate quelle esistenti.'
        params.returnController = 'attivita'
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
                    attivitaService.download()
                    flash.message = 'Operazione effettuata. Sono stati aggiornati i valori delle attività'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea le pagine della prima meta delle attività
    //--il taglio è la preferenza TAGLIO_META_ATTIVITA
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadAttivitaPrimaMeta() {
        if (grailsApplication && grailsApplication.config.login) {
            listaService.uploadAttivitaPrimaMeta()
            statisticheService.attivitaUsate()
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea le pagine della seconda meta delle attività
    //--il taglio è la preferenza TAGLIO_META_ATTIVITA
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadAttivitaSecondaMeta() {
        listaService.uploadAttivitaSecondaMeta()

        if (grailsApplication && grailsApplication.config.login) {
            listaService.uploadAttivitaSecondaMeta()
            statisticheService.attivitaUsate()
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti le pagine di attività
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadAttivita() {
        if (grailsApplication && grailsApplication.config.login) {
            listaService.uploadAttivita()
            statisticheService.attivitaUsate()
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    //--elabora e crea le liste dell'attivita indicato e lo uploada sul server wiki
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadSingolaAttivita(Long id) {
        def attivita = Attivita.get(id)
        String plurale = attivita.plurale

        if (grailsApplication && grailsApplication.config.login) {
            new BioAttivita(plurale).registraPagina()
            flash.info = "Eseguito upload delle liste dell'attivita sul server wiki"
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    //--aggiornamento della pagina di statistica
    def statistiche() {
        params.tipo = TipoDialogo.conferma
        params.avviso = []
        params.avviso.add('Creazione della pagina di sintesi con le statistiche.')
        params.avviso.add("Normalmente viene creata alla fine dell'upload delle attività")
        params.returnController = 'attivita'
        params.returnAction = 'statisticheDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--aggiornamento della pagina di statistica
    def statisticheDopoConferma() {
        String valore
        boolean continua = false

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (grailsApplication && grailsApplication.config.login) {
                        continua = true
                    } else {
                        flash.message = 'Devi essere loggato per poter modificare le pagine sul server wiki'
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (statisticheService) {
                statisticheService?.attivitaUsate()
            }// fine del blocco if
        } else {
            flash.error = "Annullata l'operazione di creazione delle statistiche"
        }// fine del blocco if-else

        redirect(action: 'list')
    } // fine del metodo

    def list(Integer max) {
        params.max = Math.min(max ?: 100, 100)
        ArrayList menuExtra
        ArrayList campiLista
        def lista
        def campoSort
        String titoloLista
        int recordsTotali
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'attivita', action: 'download', icon: 'frecciagiu', title: 'Download'],
                [cont: 'attivita', action: 'uploadAttivita', icon: 'frecciasu', title: 'Upload All'],
                [cont: 'attivita', action: 'statistiche', icon: 'frecciasu', title: 'Statistiche']
        ]
        // fine della definizione

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', titolo:'titoloVisibile', sort:'ordinamento']
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

        //--metodo di esportazione dei dati (eventuale)
        export(params)

        //--selezione dei records da mostrare
        //--per una lista filtrata (parziale), modificare i parametri
        //--oppure modificare il findAllByInteroGreaterThan()...
        lista = Attivita.list(params)
        recordsTotali = Attivita.count()

        //--calcola il numero di record
        titoloLista = 'Elenco di ' + Lib.Txt.formatNum(recordsTotali) + ' attività'

        //--presentazione della view (list), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 8)
        render(view: 'list', model: [
                attivitaInstanceList : lista,
                attivitaInstanceTotal: recordsTotali,
                menuExtra            : menuExtra,
                titoloLista          : titoloLista,
                campiLista           : campiLista,
                noMenuCreate         : noMenuCreate],
                params: params)
    } // fine del metodo

    //--metodo di esportazione dei dati
    //--funziona SOLO se il flag -usaExport- è true (iniettato e regolato in ExportBootStrap)
    //--se non si regola la variabile -titleReport- non mette nessun titolo al report
    //--se non si regola la variabile -records- esporta tutti i records
    //--se non si regola la variabile -fields- esporta tutti i campi
    def export = {
        String titleReport = new Date()
        def records = null
        List fields = null
        Map parameters

        if (exportService && servletContext.usaExport) {
            if (params?.format && params.format != 'html') {
                if (!records) {
                    records = Attivita.list(params)
                }// fine del blocco if
                if (!fields) {
                    fields = new DefaultGrailsDomainClass(Attivita.class).persistentProperties*.name
                }// fine del blocco if
                parameters = [title: titleReport]
                response.contentType = grailsApplication.config.grails.mime.types[params.format]
                response.setHeader("Content-disposition", "attachment; filename=Attivita.${params.extension}")
                exportService.export((String) params.format, response.outputStream, records, fields, [:], [:], parameters)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo


    def save() {
        def attivitaInstance = new Attivita(params)

        if (!attivitaInstance.save(flush: true)) {
            render(view: 'create', model: [attivitaInstance: attivitaInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        //--log della registrazione
        if (logoService && eventoService) {
            logoService.setInfo(request, eventoService.getNuovo(), 'Gac')
        }// fine del blocco if

        flash.message = message(code: 'default.created.message', args: [message(code: 'attivita.label', default: 'Attivita'), attivitaInstance.id])
        redirect(action: 'show', id: attivitaInstance.id)
    } // fine del metodo

    def show(Long id) {
        def attivitaInstance = Attivita.get(id)
        ArrayList menuExtra
        def noMenuCreate = true

        if (!attivitaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attivita.label', default: 'Attivita'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'attivita', action: "uploadSingolaAttivita/${id}", icon: 'database', title: 'UploadSingolaAttivita'],
        ]
        // fine della definizione

        //--presentazione della view (show), secondo il modello
        //--menuExtra può essere nullo o vuoto
        render(view: 'show', model: [
                attivitaInstance: attivitaInstance,
                menuExtra       : menuExtra,
                noMenuCreate    : noMenuCreate],
                params: params)
    } // fine del metodo

    def edit(Long id) {
        def attivitaInstance = Attivita.get(id)

        if (!attivitaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attivita.label', default: 'Attivita'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        [attivitaInstance: attivitaInstance]
    } // fine del metodo

    def update(Long id, Long version) {
        def attivitaInstance = Attivita.get(id)

        if (!attivitaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attivita.label', default: 'Attivita'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        if (version != null) {
            if (attivitaInstance.version > version) {
                attivitaInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'attivita.label', default: 'Attivita')] as Object[],
                        "Another user has updated this Attivita while you were editing")
                render(view: 'edit', model: [attivitaInstance: attivitaInstance])
                return
            }// fine del blocco if e fine anticipata del metodo
        }// fine del blocco if

        attivitaInstance.properties = params

        if (!attivitaInstance.save(flush: true)) {
            render(view: 'edit', model: [attivitaInstance: attivitaInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        flash.message = message(code: 'default.updated.message', args: [message(code: 'attivita.label', default: 'Attivita'), attivitaInstance.id])
        redirect(action: 'show', id: attivitaInstance.id)
    } // fine del metodo

    def delete(Long id) {
        def attivitaInstance = Attivita.get(id)
        if (!attivitaInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attivita.label', default: 'Attivita'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        try {
            attivitaInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'attivita.label', default: 'Attivita'), id])
            redirect(action: 'list')
        }// fine del blocco try
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'attivita.label', default: 'Attivita'), id])
            redirect(action: 'show', id: id)
        }// fine del blocco catch
    } // fine del metodo

} // fine della controller classe
