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

import grails.transaction.Transactional
import it.algos.algos.DialogoController
import it.algos.algos.TipoDialogo
import it.algos.algoslib.Lib
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.orm.hibernate.HibernateSession
import org.hibernate.FlushMode
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.orm.hibernate3.SessionFactoryUtils

import javax.persistence.FlushModeType

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = false)
class AntroponimoController {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
    private static int MAX = 100

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def exportService
    def logoService
    def eventoService
    def antroponimoService
    def professioneService
    def genereService

    def list() {
        redirect(action: 'index', params: params)
    } // fine del metodo

    //--costruisce
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def costruisce() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Costruisce tutti i nuovi records'
        params.avviso = []
        params.avviso.add('Azzera (null) tutti i link tra BioGrails e Antroponimi')
        params.avviso.add('Cancella tutti i records di Antroponimi')
        params.avviso.add('Vengono creati nuovi records per i nomi presenti nelle voci (bioGrails) che superano la soglia minima')
        params.avviso.add('Tempo indicativo: quattro ore')
        params.returnController = 'antroponimo'
        params.returnAction = 'costruisceDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--crea i records estraendoli dalle voci esistenti (bioGrails)
    def costruisceDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. BioGrails e Antroponimi non modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (antroponimoService) {
                        antroponimoService.cancellaTutto()
                        antroponimoService.aggiunge()
                        antroponimoService.ricalcola()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati creati i nuovi antroponimi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--aggiunge
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def aggiunge() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Aggiunta nuovi records'
        params.avviso = []
        params.avviso.add('Vengono creati nuovi records per i nomi presenti nelle voci (bioGrails) che superano la soglia minima')
        params.avviso.add('Tempo indicativo: quattro ore')
        params.returnController = 'antroponimo'
        params.returnAction = 'aggiungeDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--crea i records estraendoli dalle voci esistenti (bioGrails)
    def aggiungeDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Antroponimi non modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (antroponimoService) {
                        antroponimoService.aggiunge()
                        antroponimoService.ricalcola()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati aggiunti alcuni nuovi antroponimi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--ricalcola
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def ricalcola() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Ricalcolo records esistenti'
        params.avviso = []
        params.avviso.add('Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (antroponimo)')
        params.avviso.add('Tempo indicativo: una ora')
        params.returnController = 'antroponimo'
        params.returnAction = 'ricalcolaDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (antroponimo)
    def ricalcolaDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Antroponimi non modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (antroponimoService) {
                        antroponimoService.ricalcola()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati ricalcolati gli antroponimi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--mostra un dialogo di conferma per l'operazione da compiere
    //--passa al metodo effettivo
    def upload() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Ciclo'
        params.avviso = []
        params.avviso.add('Upload dalle pagina antroponimi. Vengono create/aggiornate tutte le voci.')
        params.returnController = 'antroponimo'
        params.returnAction = 'uploadDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--crea/aggiorna le pagine antroponimi
    def uploadDopoConferma() {
        String valore
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        flash.message = 'Operazione annullata. Pagine antroponimi non modificate.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (grailsApplication && grailsApplication.config.login) {
                        uploadBase()
                        flash.message = 'Operazione effettuata. Sono stati creati/aggiornate le pagine antroponimi'
                    } else {
                        if (debug) {
                            uploadBase()
                            flash.message = 'Operazione effettuata. Sono stati creati/aggiornate le pagine antroponimi'
                        } else {
                            flash.error = 'Devi essere loggato per poter caricare gli antroponimi'
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    private uploadBase() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)

        //--ricontrolla la lista delle professioni
        if (!debug && professioneService) {
            professioneService.download()
        }// fine del blocco if

        //--ricontrolla la lista dei plurali per genere
        if (!debug && genereService) {
            genereService.download()
        }// fine del blocco if

        if (!debug && antroponimoService) {
            //--aggiorna il numero di voci per ogni antroponimo della lista (semi-statica)
            antroponimoService.ricalcola()

            //--costruisce una lista di nomi (circa 900)
            antroponimoService.upload()

            //--pagine di controllo
            antroponimoService.creaPagineControllo()
        }// fine del blocco if
    } // fine del metodo

    //--elabora e crea le liste del nome indicato e le uploada sul server wiki
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadSingoloNome(Long id) {
        def nonServe
        def antroponimo = Antroponimo.get(id)
        String nome = antroponimo.nome

        if (grailsApplication && grailsApplication.config.login) {
            nonServe = new ListaNome(nome)
            flash.info = "Eseguito upload delle liste del nome sul server wiki"
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    //--elabora e crea sul server wiki le pagine di servizio
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def controllo() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Creazione pagine di servizio'
        params.avviso = []
        params.avviso.add('Crea Progetto:Antroponimi/Nomi, Progetto:Antroponimi/Liste, Progetto:Antroponimi/Didascalie')
        params.returnController = 'antroponimo'
        params.returnAction = 'controlloDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--elabora e crea sul server wiki le pagine di servizio
    //--passa al metodo effettivo
    def controlloDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. Pagine non create.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (antroponimoService) {
                        antroponimoService.creaPagineControllo()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono state ricreate le pagine di servizio'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--apre le pagine di servizio sul server wiki
    def linkPagineServizio() {
        redirect(url: "https://it.wikipedia.org/wiki/Categoria:Progetto Antroponimi")
    } // fine del metodo

    def index(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra
        ArrayList campiLista
        def campoSort
        int recordsTotali
        String titoloLista
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'antroponimo', action: 'costruisce', icon: 'frecciasu', title: 'Ricrea tutti i records'],
                [cont: 'antroponimo', action: 'aggiunge', icon: 'frecciasu', title: 'Aggiungi records'],
                [cont: 'antroponimo', action: 'ricalcola', icon: 'frecciasu', title: 'Ricalcolo voci'],
                [cont: 'antroponimo', action: 'upload', icon: 'frecciasu', title: 'Upload antroponimi'],
                [cont: 'antroponimo', action: 'controllo', icon: 'frecciasu', title: 'Ricrea pagine di servizio'],
                [cont: 'antroponimo', action: 'linkPagineServizio', icon: 'frecciasu', title: 'Pagine di servizio wiki'],
        ]
        params.menuExtra = menuExtra
        // fine della definizione

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        //--se vuoto, mostra i primi n (stabilito nel templates:scaffoldinf:list)
        //--    nell'ordine stabilito nella constraints della DomainClass
        campiLista = ['nome', 'voci', 'isVocePrincipale', 'voceRiferimento', 'wikiUrl']
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
        recordsTotali = Antroponimo.count()

        //--calcola il numero di record
        //--titolo visibile sopra la table dei dati
        titoloLista = 'Elenco di '
        titoloLista += Lib.Txt.formatNum(Antroponimo.list(params).size())
        titoloLista += ' records di antroponimi su un totale di ' + Lib.Txt.formatNum(recordsTotali)

        //--presentazione della view (index), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 12)
        respond Antroponimo.list(params), model: [titoloLista             : titoloLista,
                                                  menuExtra               : menuExtra,
                                                  campiLista              : campiLista,
                                                  antroponimoInstanceCount: recordsTotali,
                                                  noMenuCreate            : noMenuCreate,
                                                  params                  : params]

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
                    records = Antroponimo.list(params)
                }// fine del blocco if
                if (!fields) {
                    fields = new DefaultGrailsDomainClass(Antroponimo.class).persistentProperties*.name
                }// fine del blocco if
                parameters = [title: titleReport]
                response.contentType = grailsApplication.config.grails.mime.types[params.format]
                response.setHeader("Content-disposition", "attachment; filename=Antroponimo.${params.extension}")
                exportService.export((String) params.format, response.outputStream, records, fields, [:], [:], parameters)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo


    def create() {
        respond new Antroponimo(params)
    } // fine del metodo


    def save() {
        def antroponimoInstance = new Antroponimo(params)

        if (!antroponimoInstance.save(flush: true)) {
            render(view: 'create', model: [antroponimoInstance: antroponimoInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        //--log della registrazione
        if (logoService && eventoService) {
            logoService.setInfo(request, eventoService.getNuovo(), 'Gac')
        }// fine del blocco if

        flash.message = message(code: 'default.created.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), antroponimoInstance.id])
        redirect(action: 'show', id: antroponimoInstance.id)
    } // fine del metodo

    def show(Antroponimo antroponimoInstance) {
        ArrayList menuExtra
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        if (antroponimoInstance.isVocePrincipale && antroponimoInstance.wikiUrl) {
            menuExtra = [
                    [cont: 'antroponimo', action: "uploadSingoloNome/${antroponimoInstance.id}", icon: 'database', title: 'UploadSingoloNome'],
            ]
        } else {
            menuExtra = []
        }// fine del blocco if-else

        // fine della definizione

        //--presentazione della view (show), secondo il modello
        //--menuExtra può essere nullo o vuoto
        render(view: 'show', model: [
                antroponimoInstance: antroponimoInstance,
                menuExtra          : menuExtra,
                noMenuCreate       : noMenuCreate],
                params: params)
    } // fine del metodo


    def edit(Antroponimo antroponimoInstance) {
        respond antroponimoInstance
    } // fine del metodo

//    def pippoz(Antroponimo antroponimoInstance) {
//        if (antroponimoInstance == null) {
//            notFound()
//            return
//        }// fine del blocco if
//
//        if (antroponimoInstance.hasErrors()) {
//            respond antroponimoInstance.errors, view: 'edit'
//            return
//        }// fine del blocco if
//
//        antroponimoInstance.save flush: true
//
//        request.withFormat {
//            form multipartForm {
//                flash.message = message(code: 'default.updated.message', args: [message(code: 'Antroponimo.label', default: 'Cognome'), antroponimoInstance.id])
//                redirect antroponimoInstance
//            }// fine di form
//            '*' { respond antroponimoInstance, [status: OK] }
//        }// fine di request
//    } // fine del metodo


    @Transactional
    def update(Antroponimo antroponimoInstance) {
        if (antroponimoInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (antroponimoInstance.hasErrors()) {
            respond antroponimoInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        antroponimoInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Antroponimo.label', default: 'Cognome'), antroponimoInstance.id])
                redirect antroponimoInstance
            }// fine di form
            '*' { respond antroponimoInstance, [status: OK] }
        }// fine di request
    } // fine del metodo

//    def update(Long id, Long version) {
//        def antroponimoInstance = Antroponimo.get(id)
//
//        if (!antroponimoInstance) {
//            flash.message = message(code: 'default.not.found.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
//            redirect(action: 'list')
//            return
//        }// fine del blocco if e fine anticipata del metodo
//
//        if (version != null) {
//            if (antroponimoInstance.version > version) {
//                antroponimoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
//                        [message(code: 'antroponimo.label', default: 'Antroponimo')] as Object[],
//                        "Another user has updated this Antroponimo while you were editing")
//                render(view: 'edit', model: [antroponimoInstance: antroponimoInstance])
//                return
//            }// fine del blocco if e fine anticipata del metodo
//        }// fine del blocco if
//
//        antroponimoInstance.properties = params
//
//        if (!antroponimoInstance.save(flush: true)) {
//            render(view: 'edit', model: [antroponimoInstance: antroponimoInstance])
//            return
//        }// fine del blocco if e fine anticipata del metodo
//
//        flash.message = message(code: 'default.updated.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), antroponimoInstance.id])
//        redirect(action: 'show', id: antroponimoInstance.id)
//    } // fine del metodo

    def delete(Long id) {
        def antroponimoInstance = Antroponimo.get(id)
        if (!antroponimoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        try {
            antroponimoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
            redirect(action: 'list')
        }// fine del blocco try
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
            redirect(action: 'show', id: id)
        }// fine del blocco catch
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), params.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo

} // fine della controller classe
