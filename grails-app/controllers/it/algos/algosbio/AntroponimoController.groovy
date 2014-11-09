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
import it.algos.algospref.Pref
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.orm.hibernate.HibernateSession
import org.hibernate.FlushMode
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.orm.hibernate3.SessionFactoryUtils

import javax.persistence.FlushModeType

class AntroponimoController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'POST']

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def exportService
    def logoService
    def eventoService
    def antroponimoService
    def professioneService
    def genereService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    //--aggiunge
    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def aggiunge() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Aggiunta nuovi records'
        params.avviso = []
        params.avviso.add('Vengono creati nuovi records per i nomi presenti nelle voci (bioGrails) che superano la soglia minima')
        params.avviso.add('Tempo indicativo: cinque ore')
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
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati creati i nuovi antroponimi'
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
        params.avviso.add('Tempo indicativo: quattro ore')
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
                        antroponimoService.creaPagineControllo()
                    }// fine del blocco if
                    flash.message = 'Operazione effettuata. Sono stati ricalcolati gli antroponimi'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--mostra un dialogo di conferma per l'operazione da compiere
    //--passa al metodo effettivo
    def elabora() {
        params.tipo = TipoDialogo.conferma
        params.titolo = 'Ciclo'
        params.avviso = []
        params.avviso.add('Upload dalle pagina antroponimi. Vengono create/aggiornate tutte le voci.')
        params.returnController = 'antroponimo'
        params.returnAction = 'elaboraDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--crea/aggiorna le pagine antroponimi
    def elaboraDopoConferma() {
        String valore
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        flash.message = 'Operazione annullata. Pagine antroponimi non modificate.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    if (grailsApplication && grailsApplication.config.login) {
                        elaboraBase()
                        flash.message = 'Operazione effettuata. Sono stati creati/aggiornate le pagine antroponimi'
                    } else {
                        if (debug) {
                            elaboraBase()
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

    private elaboraBase() {
        def nonServe
        ArrayList<String> listaNomi = null
//        int dimBlocco = Pref.getInt(LibBio.CICLO_ANTROPONIMI, 10)
//        ArrayList<String> listaBlocchiNomi
//        int numVoci = 0
//        String numVociTxt
//        long inizioInizio = System.currentTimeMillis()
//        long inizio = 0
//        long fine = 0
//        long durata = 0
//        long durataTotale = 0
//        String tempoTxt
//        String tempoTotaleTxt
//        int cont = 0
//        String vociCreateTxt = ''
//        int vociCreate = 0

        //--ricontrolla la lista delle professioni
        if (professioneService) {
            professioneService.download()
        }// fine del blocco if

        //--ricontrolla la lista dei plurali per genere
        if (genereService) {
            genereService.download()
        }// fine del blocco if

        //--costruisce una lista di nomi (circa 600)
        if (antroponimoService) {
            listaNomi = antroponimoService.getListaNomi()
        }// fine del blocco if

        //--crea le pagine dei singoli nomi a blocchi
        listaNomi?.each {
            nonServe = new ListaNome(it)
        }// fine del blocco if
//        if (listaNomi) {
//            inizio = inizioInizio
//            numVoci = listaNomi.size()
//            numVociTxt = LibTesto.formatNum(numVoci)
//            log.info "Inizio del metodo di creazione di ${numVociTxt} voci di Antroponimi"
//                cont++
//                antroponimoService.elabora((ArrayList) it)
//                fine = System.currentTimeMillis()
//                durataTotale = fine - inizioInizio
//                durata = fine - inizio
//                durataTotale = durataTotale / 1000
//                durata = durata / 1000
//                tempoTotaleTxt = LibTesto.formatNum(durataTotale)
//                tempoTxt = LibTesto.formatNum(durata)
//                vociCreate = cont * dimBlocco
//                vociCreateTxt = LibTesto.formatNum(vociCreate)
//                log.info "Aggiornate ${vociCreateTxt}/${numVociTxt} voci di Antroponimi in ${tempoTxt}/${tempoTotaleTxt} secondi"
//                inizio = fine
//            }// fine del ciclo each

        if (listaNomi) {
            antroponimoService.creaPagineControllo()
        }// fine del blocco if
    } // fine del metodo

    //--elabora e crea le liste del nome indicato e le uploada sul server wiki
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadSingoloNome(Long id) {
        def antroponimo = Antroponimo.get(id)
        String nome = antroponimo.nome

        if (grailsApplication && grailsApplication.config.login) {
            antroponimoService.elaboraSingoloNome(nome)
            flash.info = "Eseguito upload delle liste del nome sul server wiki"
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else
        redirect(action: 'list')
    } // fine del metodo

    def list(Integer max) {
        params.max = Math.min(max ?: 1000, 1000)
        ArrayList menuExtra
        ArrayList campiLista
        def lista
        def campoSort
        int recordsTotali
        String titoloLista

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'antroponimo', action: 'aggiunge', icon: 'frecciasu', title: 'Aggiunta nuovi records'],
                [cont: 'antroponimo', action: 'ricalcola', icon: 'frecciasu', title: 'Ricalcolo voci'],
                [cont: 'antroponimo', action: 'elabora', icon: 'frecciasu', title: 'Upload antroponimi'],
        ]
        // fine della definizione

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        //--se vuoto, mostra i primi n (stabilito nel templates:scaffoldinf:list)
        //--    nell'ordine stabilito nella constraints della DomainClass
        campiLista = ['nome', 'voci', 'lunghezza']
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
        lista = Antroponimo.list(params)
        recordsTotali = Antroponimo.count()

        //--calcola il numero di record
        titoloLista = 'Elenco di ' + lista.size() + ' records di antroponimi'

        //--presentazione della view (list), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 8)
        render(view: 'list', model: [
                antroponimoInstanceList : lista,
                antroponimoInstanceTotal: recordsTotali,
                menuExtra               : menuExtra,
                titoloLista             : titoloLista,
                campiLista              : campiLista],
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

    def show(Long id) {
        def antroponimoInstance = Antroponimo.get(id)
        ArrayList menuExtra
        def noMenuCreate = true

        if (!antroponimoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'antroponimo', action: "uploadSingoloNome/${id}", icon: 'database', title: 'UploadSingoloNome'],
        ]
        // fine della definizione

        //--presentazione della view (show), secondo il modello
        //--menuExtra può essere nullo o vuoto
        render(view: 'show', model: [
                antroponimoInstance: antroponimoInstance,
                menuExtra          : menuExtra,
                noMenuCreate       : noMenuCreate],
                params: params)
    } // fine del metodo

    def edit(Long id) {
        def antroponimoInstance = Antroponimo.get(id)

        if (!antroponimoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        [antroponimoInstance: antroponimoInstance]
    } // fine del metodo

    def update(Long id, Long version) {
        def antroponimoInstance = Antroponimo.get(id)

        if (!antroponimoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        if (version != null) {
            if (antroponimoInstance.version > version) {
                antroponimoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'antroponimo.label', default: 'Antroponimo')] as Object[],
                        "Another user has updated this Antroponimo while you were editing")
                render(view: 'edit', model: [antroponimoInstance: antroponimoInstance])
                return
            }// fine del blocco if e fine anticipata del metodo
        }// fine del blocco if

        antroponimoInstance.properties = params

        if (!antroponimoInstance.save(flush: true)) {
            render(view: 'edit', model: [antroponimoInstance: antroponimoInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        flash.message = message(code: 'default.updated.message', args: [message(code: 'antroponimo.label', default: 'Antroponimo'), antroponimoInstance.id])
        redirect(action: 'show', id: antroponimoInstance.id)
    } // fine del metodo

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

} // fine della controller classe
