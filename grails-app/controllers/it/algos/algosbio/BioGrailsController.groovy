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
import it.algos.algospref.Pref
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.springframework.dao.DataIntegrityViolationException

//--gestisce l'upload delle informazioni
class BioGrailsController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'POST']

    private static int MAX = 1000

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def exportService
    def logoService
    def eventoService
    def bioService
    def bioGrailsService
    def statisticheService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    //--elaborazione dei dati da BioWiki a BioGrails
    //--elabora la lista dei records BioWiki col flag 'elaborata'=false
    //--mostra un dialogo di conferma per l'operazione da compiere
    //--passa al metodo effettivo
    def resetElabora() {
        params.titolo = 'Elaborazione'
        def lista = BioWiki.executeQuery('select id from BioWiki where elaborata=true')

        if (lista && lista.size() > 0) {
            params.tipo = TipoDialogo.conferma
            params.avviso = []
            params.avviso.add('Elaborazione delle biografie (BioWiki) non ancora elaborate.')
            params.avviso.add("Azzera il flag 'elaborata' dei records (BioWiki) trattati")
            params.returnController = 'bioGrails'
            params.returnAction = 'elaboraDopoConferma'
            redirect(controller: 'dialogo', action: 'box', params: params)
        } else {
            params.tipo = TipoDialogo.avviso
            params.avviso = 'Sorry, non ci sono voci biografiche (BioWiki) da elaborare !'
            params.returnController = 'bioGrails'
            redirect(controller: 'dialogo', action: 'box', params: params)
        }// fine del blocco if-else
    } // fine del metodo

    //--elaborazione dei dati da BioWiki a BioGrails
    //--elabora la lista dei records BioWiki col flag 'elaborata'=false
    def elaboraDopoConferma() {
        String valore
        boolean continua = false
        flash.message = 'Operazione annullata. Le voci biografiche non sono state elaborate.'
        String oldDataTxt

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    continua = true
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            bioService.elaboraAll()
            oldDataTxt = LibBio.voceElaborataVecchia()
            flash.message = oldDataTxt
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--elaborazione dei dati da BioWiki a BioGrails
    //--elabora tutti i records esistenti di BioWiki
    //--mostra un dialogo di conferma per l'operazione da compiere
    //--passa al metodo effettivo
    def elaboraAll() {
        params.titolo = 'Elaborazione'
        if (BioWiki.count() > 0) {
            params.tipo = TipoDialogo.conferma
            params.avviso = []
            params.avviso.add('Elaborazione delle biografie (BioWiki) esistenti per avere la corrispondente voce (BioGrails) allineata.')
            params.avviso.add("Se il flag globale usaLimiteElabora è falso, azzera il flag 'elaborata' di tutte le voci (BioWiki) e le elabora tutte")
            params.avviso.add("Se il flag globale usaLimiteElabora è true, elabora solo le prime maxElabora in ordine dalla più vecchia")
            params.avviso.add('Ci vuole diverso tempo. Parecchie ore se usaLimiteElabora è falso')
            params.returnController = 'bioGrails'
            params.returnAction = 'elaboraAllDopoConferma'
            redirect(controller: 'dialogo', action: 'box', params: params)
        } else {
            params.tipo = TipoDialogo.avviso
            params.avviso = 'Sorry, non ci sono voci biografiche da elaborare !'
            params.returnController = 'bioGrails'
            redirect(controller: 'dialogo', action: 'box', params: params)
        }// fine del blocco if-else
    } // fine del metodo

    //--elaborazione dei dati da BioWiki a BioGrails
    //--elabora tutti i records esistenti di BioWiki
    def elaboraAllDopoConferma() {
        String valore
        boolean continua = false
        flash.message = 'Operazione annullata. Le voci biografiche non sono state elaborate.'
        String oldDataTxt

        if (params.valore) {
            if (params.valore instanceof String) {
                valore = (String) params.valore
                if (valore.equals(DialogoController.DIALOGO_CONFERMA)) {
                    continua = true
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (Pref.getBool(LibBio.USA_LIMITE_ELABORA)) {
                bioService.elabora()
            } else {
                bioService.elaboraAll()
            }// fine del blocco if-else
            oldDataTxt = LibBio.voceElaborataVecchia()
            flash.message = oldDataTxt
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati
    //--elabora e crea tutti gli anni modificati
    //--elabora e crea tutte le attività
    //--elabora e crea tutte le nazionalità
    //--mostra un dialogo di conferma per l'operazione da compiere
    //--passa al metodo effettivo
    def uploadAll() {
        params.titolo = 'Liste'
        if (BioGrails.count() > 0) {
            params.tipo = TipoDialogo.conferma
            params.avviso = []
            params.avviso.add('Creazione delle liste di tutte le voci modificate (BioGrails).')
            params.avviso.add('Elabora e crea tutti i giorni modificati')
            params.avviso.add('Elabora e crea tutti gli anni modificati')
//            params.avviso.add('Elabora e crea tutte le attività modificate')
//            params.avviso.add('Elabora e crea tutte le nazionalità modificate')
            params.avviso.add('Crea le pagine base di statistiche')
            params.avviso.add('Ci vogliono diverse ore')
            params.returnController = 'bioGrails'
            params.returnAction = 'uploadAllDopoConferma'
            redirect(controller: 'dialogo', action: 'box', params: params)
        } else {
            params.tipo = TipoDialogo.avviso
            params.avviso = 'Sorry, non ci sono voci biografiche da utilizzare !'
            params.returnController = 'bioGrails'
            redirect(controller: 'dialogo', action: 'box', params: params)
        }// fine del blocco if-else
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati
    //--elabora e crea tutti gli anni modificati
    //--elabora e crea tutte le attività
    //--elabora e crea tutte le nazionalità
    def uploadAllDopoConferma() {
        String valore
        boolean continua = false
        flash.message = 'Operazione annullata. Le liste non sono state create.'

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

        if (continua && statisticheService) {
//            bioGrailsService.uploadAll()
            statisticheService.uploadAll()
            statisticheService.paginaSintesi()
        }// fine del blocco if

        redirect(action: 'list')
    } // fine del metodo

    //--aggiornamento della pagina di statistica
    def statistiche() {
        params.tipo = TipoDialogo.conferma
        params.avviso = []
        params.avviso.add('Creazione della pagina di sintesi con le statistiche.')
        params.avviso.add("Normalmente viene creata alla fine dell'upload delle pagine di giorni ed anni")
        params.avviso.add('Modifica i valori di alcune preferenze nel DB per calcolare le differenze.')
        params.returnController = 'bioGrails'
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
                statisticheService.paginaSintesi()
            }// fine del blocco if
        } else {
            flash.error = "Annullata l'operazione di creazione delle statistiche"
        }// fine del blocco if-else

        redirect(action: 'list')
    } // fine del metodo

    def list(Integer max) {
        if (!params.max) params.max = MAX
        ArrayList menuExtra
        ArrayList campiLista
        def lista
        def campoSort = 'forzaOrdinamento'
        String titoloLista
        int recordsTotali
        def noMenuCreate = true

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        menuExtra = [
                [cont: 'bioGrails', action: 'resetElabora', icon: 'database', title: 'Reset elabora'],
                [cont: 'bioGrails', action: 'elaboraAll', icon: 'database', title: 'Elabora'],
                [cont: 'bioGrails', action: 'uploadAll', icon: 'frecciasu', title: 'Upload all'],
                [cont: 'bioGrails', action: 'statistiche', icon: 'frecciasu', title: 'Statistiche'],
                [cont: 'bioWiki', action: 'list', icon: 'scambia', title: 'BioWiki']
        ]
        // fine della definizione

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        campiLista = [
                'pageid',
                'title',
                'didascaliaBase',
                [campo: 'giornoMeseNascitaLink', title: 'gg n.'],
                [campo: 'annoNascitaLink', title: 'anno n.'],
                [campo: 'luogoNatoLink', title: 'nato'],
                [campo: 'giornoMeseMorteLink', title: 'gg m.'],
                [campo: 'annoMorteLink', title: 'anno m.'],
                [campo: 'luogoMortoLink', title: 'morto'],
                [campo: 'attivitaLink', title: 'att'],
                [campo: 'nazionalitaLink', title: 'naz']]
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
        lista = BioGrails.list(params)

        //--selezione dei records da mostrare
        recordsTotali = BioGrails.count()

        //--titolo visibile sopra la table dei dati
        titoloLista = 'Elenco di ' + Lib.Txt.formatNum(recordsTotali) + ' biografie (records del database)'

        //--presentazione della view (list), secondo il modello
        //--menuExtra e campiLista possono essere nulli o vuoti
        //--se campiLista è vuoto, mostra tutti i campi (primi 8)
        render(view: 'list', model: [
                bioGrailsInstanceList : lista,
                bioGrailsInstanceTotal: recordsTotali,
                menuExtra             : menuExtra,
                titoloLista           : titoloLista,
                campiLista            : campiLista,
                noMenuCreate          : noMenuCreate],
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
                    records = BioGrails.list(params)
                }// fine del blocco if
                if (!fields) {
                    fields = new DefaultGrailsDomainClass(BioGrails.class).persistentProperties*.name
                }// fine del blocco if
                parameters = [title: titleReport]
                response.contentType = grailsApplication.config.grails.mime.types[params.format]
                response.setHeader("Content-disposition", "attachment; filename=BioGrails.${params.extension}")
                exportService.export((String) params.format, response.outputStream, records, fields, [:], [:], parameters)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    def save() {
        def bioGrailsInstance = new BioGrails(params)

        if (!bioGrailsInstance.save(flush: true)) {
            render(view: 'create', model: [bioGrailsInstance: bioGrailsInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        //--log della registrazione
        if (logoService && eventoService) {
            logoService.setInfo(request, eventoService.getNuovo(), 'Gac')
        }// fine del blocco if

        flash.message = message(code: 'default.created.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), bioGrailsInstance.id])
        redirect(action: 'show', id: bioGrailsInstance.id)
    } // fine del metodo

    def elaboraSingola() {
        int pageid = 0
        def bioGrailsInstance = null

        if (params.id) {
            pageid = Integer.decode(params.id)
        }// fine del blocco if

        if (pageid) {
            bioGrailsInstance = BioGrails.findByPageid(pageid)
        }// fine del blocco if

        if (pageid && bioService) {
            bioService.elabora(pageid)
        }// fine del blocco if

        if (bioGrailsInstance) {
            flash.message = "Il record di ${bioGrailsInstance.title} è stato elaborato ed è aggiornato"
            redirect(action: 'show', id: bioGrailsInstance.id)
        } else {
            flash.error = 'Non sono riuscito ad elaborare il record'
            redirect(action: 'list')
        }// fine del blocco if-else
    } // fine del metodo


    def show(Long id) {
        def bioGrailsInstance = BioGrails.get(id)
        ArrayList menuExtra
        def noMenuCreate = true

        if (!bioGrailsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        //--selezione dei menu extra
        //--solo azione e di default controller=questo; classe e titolo vengono uguali
        //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
        int pageid = bioGrailsInstance.pageid
        String query = "select id from BioWiki where pageid=" + pageid
        ArrayList ref = BioWiki.executeQuery(query)
        long idWiki = (long) ref.get(0)
        menuExtra = [
                [cont: 'bioGrails', action: "elaboraSingola/${pageid}", icon: 'database', title: 'Elabora'],
                [cont: 'bioWiki', action: "show/${idWiki}", icon: 'scambia', title: 'BioWiki']
        ]
        // fine della definizione

        //--presentazione della view (show), secondo il modello
        //--menuExtra può essere nullo o vuoto
        render(view: 'show', model: [
                bioGrailsInstance: bioGrailsInstance,
                menuExtra        : menuExtra,
                noMenuCreate     : noMenuCreate],
                params: params)
    } // fine del metodo


    def edit(Long id) {
        def bioGrailsInstance = BioGrails.get(id)

        if (!bioGrailsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        [bioGrailsInstance: bioGrailsInstance]
    } // fine del metodo

    def update(Long id, Long version) {
        def bioGrailsInstance = BioGrails.get(id)

        if (!bioGrailsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        if (version != null) {
            if (bioGrailsInstance.version > version) {
                bioGrailsInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'bioGrails.label', default: 'BioGrails')] as Object[],
                        "Another user has updated this BioGrails while you were editing")
                render(view: 'edit', model: [bioGrailsInstance: bioGrailsInstance])
                return
            }// fine del blocco if e fine anticipata del metodo
        }// fine del blocco if

        bioGrailsInstance.properties = params

        if (!bioGrailsInstance.save(flush: true)) {
            render(view: 'edit', model: [bioGrailsInstance: bioGrailsInstance])
            return
        }// fine del blocco if e fine anticipata del metodo

        flash.message = message(code: 'default.updated.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), bioGrailsInstance.id])
        redirect(action: 'show', id: bioGrailsInstance.id)
    } // fine del metodo

    def delete(Long id) {
        def bioGrailsInstance = BioGrails.get(id)
        if (!bioGrailsInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if e fine anticipata del metodo

        try {
            bioGrailsInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), id])
            redirect(action: 'list')
        }// fine del blocco try
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'bioGrails.label', default: 'BioGrails'), id])
            redirect(action: 'show', id: id)
        }// fine del blocco catch
    } // fine del metodo

} // fine della controller classe
