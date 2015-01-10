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
import it.algos.algospref.Pref
import it.algos.algoswiki.Login
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = false)
class GiornoController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'POST']

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def exportService
    def logoService
    def eventoService
    def giornoService
    def bioGrailsService
    def bioService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    //--mostra un avviso di spiegazione per l'operazione da compiere
    //--passa al metodo effettivo
    def create() {
        params.tipo = TipoDialogo.avviso
        params.avviso = 'Vengono creati tutti i giorni necessari per il funzionamento del bot. Sono previsti 366 giorni per comprendere gli anni bisestili.'
        params.returnController = 'giorno'
        params.returnAction = 'createDopoAvviso'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--creazione iniziale
    //--controlla se esistono già
    //--mostra un dialogo di conferma per l'operazione da compiere
    //--resetta i valori della tavola
    //--cosa che provoca una perdita delle chiavi ids
    def createDopoAvviso() {
        if (Giorno.count() > 0) {
            params.tipo = TipoDialogo.conferma
            params.avviso = 'I giorni esistono già. Se prosegui verranno cancellati e riscritti. Si perdono i valori-chiave dei records. Sei sicuro di volerlo fare?'
            params.returnController = 'giorno'
            params.returnAction = 'createDopoConferma'
            redirect(controller: 'dialogo', action: 'box', params: params)
        } else {
            giornoService.creazioneIniziale()
            flash.message = 'Operazione effettuata. Sono stati creati tutti i giorni necessari per il bot.'
            redirect(action: 'list')
        }// fine del blocco if-else
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--resetta i valori della tavola
    //--cosa che provoca una perdita delle chiavi ids
    def createDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. I giorni non sono stati modificati.'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    giornoService.creazioneIniziale()
                    flash.message = 'Operazione effettuata. Sono stati creati tutti i giorni necessari per il bot.'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--mostra un avviso di conferma per l'operazione da compiere
    def sporca() {
        params.tipo = TipoDialogo.conferma
        params.avviso = 'Normalmente il bot sporca SOLO i giorni relativi alle voci bio modificate. Sei sicuro di voler sporcare tutto?'
        params.returnController = 'giorno'
        params.returnAction = 'sporcaDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--forza a true tutti i records
    def sporcaDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. I giorni non sono stati sporcati'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    giornoService.regolaSporco(true)
                    flash.message = 'Operazione effettuata. Sono stati sporcati tutti i giorni.'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--mostra un avviso di conferma per l'operazione da compiere
    def pulisce() {
        params.tipo = TipoDialogo.conferma
        params.avviso = 'Normalmente il bot pulisce tutti i giorni DOPO il ciclo di registrazione giorni/anni. Sei sicuro di volerli pulire adesso?'
        params.returnController = 'giorno'
        params.returnAction = 'pulisceDopoConferma'
        redirect(controller: 'dialogo', action: 'box', params: params)
    } // fine del metodo

    //--ritorno dal dialogo di conferma
    //--a seconda del valore ritornato come parametro, esegue o meno l'operazione
    //--forza a false tutti i records
    def pulisceDopoConferma() {
        String valore
        flash.message = 'Operazione annullata. I giorni non sono stati puliti'

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    giornoService.regolaSporco(false)
                    flash.message = 'Operazione effettuata. Sono stati puliti tutti i giorni.'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati (solo nascita)
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadGiorniNascita() {
        if (grailsApplication && grailsApplication.config.login && grailsApplication.config.login.isValido()) {
            if (bioService && Pref.getBool(LibBio.USA_LISTE_BIO_GIORNI)) {
                giornoService.uploadGiorniNascita(bioService)
            } else {
                bioGrailsService.uploadGiorniNascita()
            }// fine del blocco if-else
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else

        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati (solo morte)
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadGiorniMorte() {
        if (grailsApplication && grailsApplication.config.login && grailsApplication.config.login.isValido()) {
            if (bioService && Pref.getBool(LibBio.USA_LISTE_BIO_GIORNI)) {
                giornoService.uploadGiorniMorte(bioService)
            } else {
                bioGrailsService.uploadGiorniMorte()
            }// fine del blocco if-else
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else

        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadAllGiorni() {
        if (grailsApplication && grailsApplication.config.login && grailsApplication.config.login.isValido()) {
            if (bioService && Pref.getBool(LibBio.USA_LISTE_BIO_GIORNI)) {
                giornoService.uploadGiorniNascita(bioService)
                giornoService.uploadGiorniMorte(bioService)
            } else {
                bioGrailsService.uploadGiorniNascita()
                bioGrailsService.uploadGiorniMorte()
            }// fine del blocco if-else
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else

        redirect(action: 'list')
    } // fine del metodo

    //--elabora e crea le liste del giorno indicato (nascita e morte) e lo uploada sul server wiki
    //--passa al metodo effettivo senza nessun dialogo di conferma
    def uploadSingoloGiorno(Long id) {
        Giorno giorno = Giorno.get(id)

        if (grailsApplication && grailsApplication.config.login && grailsApplication.config.login.isValido()) {
            if (giorno && bioService) {
                ListaGiorno.uploadGiorno(giorno, bioService)
                flash.message = "Eseguito upload sul server wiki delle pagine con le liste delle voci nati e morti per il giorno ${giorno.titolo}"
            } else {
                flash.error = 'Non ho trovato il giorno indicato'
            }// fine del blocco if-else
        } else {
            flash.error = 'Devi essere loggato per effettuare un upload di pagine sul server wiki'
        }// fine del blocco if-else

        redirect(action: 'list')
    } // fine del metodo

    def list(Integer max) {
        params.max = 30
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
                [action: 'sporca', icon: 'list', title: 'Sporca tutti'],
                [action: 'pulisce', icon: 'list', title: 'Pulisce tutti'],
                [cont: 'giorno', action: 'uploadGiorniNascita', icon: 'frecciasu', title: 'Upload nascita'],
                [cont: 'giorno', action: 'uploadGiorniMorte', icon: 'frecciasu', title: 'Upload morte'],
                [cont: 'giorno', action: 'uploadAllGiorni', icon: 'frecciasu', title: 'Upload all giorni']
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
        lista = Giorno.list(params)
        recordsTotali = Giorno.count()

        //--calcola il numero di record
        titoloLista = 'Elenco di ' + Lib.Txt.formatNum(recordsTotali) + ' giorni'

        //--presentazione della view (list), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 8)
        render(view: 'list', model: [
                giornoInstanceList : lista,
                giornoInstanceTotal: recordsTotali,
                menuExtra          : menuExtra,
                titoloLista        : titoloLista,
                campiLista         : campiLista,
                noMenuCreate       : noMenuCreate],
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
                    records = Giorno.list(params)
                }// fine del blocco if
                if (!fields) {
                    fields = new DefaultGrailsDomainClass(Giorno.class).persistentProperties*.name
                }// fine del blocco if
                parameters = [title: titleReport]
                response.contentType = grailsApplication.config.grails.mime.types[params.format]
                response.setHeader("Content-disposition", "attachment; filename=Giorno.${params.extension}")
                exportService.export((String) params.format, response.outputStream, records, fields, [:], [:], parameters)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo


    def save() {
        def giornoInstance = new Giorno(params)

        if (!giornoInstance.save(flush: true)) {
            render(view: 'create', model: [giornoInstance: giornoInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        //--log della registrazione
        if (logoService && eventoService) {
            logoService.setInfo(request, eventoService.getNuovo(), 'Gac')
        }// fine del blocco if

        flash.message = message(code: 'default.created.message', args: [message(code: 'giorno.label', default: 'Giorno'), giornoInstance.id])
        redirect(action: 'show', id: giornoInstance.id)
    } // fine del metodo

    def show(Long id) {
        def giornoInstance = Giorno.get(id)
        ArrayList menuExtra
        def noMenuCreate = true

        if (!giornoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'giorno.label', default: 'Giorno'), id])
            redirect(action: 'index')
            return
        }// fine del blocco if e fine anticipata del metodo

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'giorno', action: "uploadSingoloGiorno/${id}", icon: 'database', title: 'Upload singolo giorno'],
        ]
        // fine della definizione

        //--presentazione della view (show), secondo il modello
        //--noMenuCreate può esserci: true o false
        //--menuExtra può essere nullo o vuoto
        render(view: 'show', model: [
                giornoInstance: giornoInstance,
                menuExtra     : menuExtra,
                noMenuCreate  : noMenuCreate],
                params: params)
    } // fine del metodo

    def edit(Long id) {
        def giornoInstance = Giorno.get(id)
        def noMenuCreate = true

        if (!giornoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'giorno.label', default: 'Giorno'), id])
            redirect(action: 'index')
            return
        }// fine del blocco if e fine anticipata del metodo

        //--presentazione della view (edit), secondo il modello
        //--noMenuCreate può esserci: true o false
        //--menuExtra può essere nullo o vuoto
        render(view: 'edit', model: [
                giornoInstance: giornoInstance,
                noMenuCreate  : noMenuCreate],
                params: params)
    } // fine del metodo


    @Transactional
    def updateNew(Giorno giornoInstance) {
        if (giornoInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (giornoInstance.hasErrors()) {
            respond giornoInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        giornoInstance.properties = params
        giornoInstance.save(flush: true)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'giorno.label', default: 'Anno'), giornoInstance.titolo])
                redirect giornoInstance
            }// fine di form
            '*' { respond giornoInstance, [status: OK] }
        }// fine di request
    } // fine del metodo

    def delete(Long id) {
        def giornoInstance = Giorno.get(id)
        if (!giornoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'giorno.label', default: 'Giorno'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        try {
            giornoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'giorno.label', default: 'Giorno'), id])
            redirect(action: 'list')
        }// fine del blocco try
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'giorno.label', default: 'Giorno'), id])
            redirect(action: 'show', id: id)
        }// fine del blocco catch
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'giorno.label', default: 'Giorno'), params.id])
                redirect action: "index", method: "GET"
            }// fine di form
            '*' { render status: NOT_FOUND }
        }// fine di request
    } // fine del metodo

} // fine della controller classe
