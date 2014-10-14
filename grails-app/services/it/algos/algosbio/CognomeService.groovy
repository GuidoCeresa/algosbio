package it.algos.algosbio

import grails.transaction.Transactional
import it.algos.algoslib.LibMat
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algospref.Pref

@Transactional
class CognomeService {

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    AntroponimoService antroponimoService = new AntroponimoService()

    public static String PATH = 'Progetto:Biografie/Cognomi/Persone di cognome '
    private static String aCapo = '\n'

    // Elabora tutte le pagine
    def elabora() {
        ArrayList<String> listaCognomi
        String query = 'select distinct cognome from BioGrails order by cognome asc'
        String cognome
        int numCicliMax = Pref.getInt(LibBio.MAX_CICLI_ELABORA_COGNOMI, 10000)
        int numCicli
        int numListaCognomi
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempoTxt

        resetAll()
        listaCognomi = (ArrayList<String>) BioGrails.executeQuery(query)
        numListaCognomi = listaCognomi.size()
        fine = System.currentTimeMillis()
        durata = fine - inizio
        durata = durata / 1000
        tempoTxt = LibTesto.formatNum(durata)
        log.info "Cancellati tutti i cognomi e caricata la lista iniziale in ${tempoTxt} secondi"

        numCicli = LibMat.minimoPositivo(numListaCognomi, numCicliMax)
        for (int k = 0; k < numCicli; k++) {
            cognome = listaCognomi.get(k)
            if (cognome && LibBio.checkNome(cognome)) {
                elaboraCognome(cognome)
            }// fine del blocco if
        } // fine del ciclo for
        fine = System.currentTimeMillis()
        durata = fine - inizio
        durata = durata / 1000
        tempoTxt = LibTesto.formatNum(durata)
        log.info "Ciclo completo di creazione di ${numCicli} records di cognomi in ${tempoTxt} secondi"
    }// fine del metodo

    //--upload di tutti i cognomi
    public upload() {
        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI, 100)
        ArrayList<Cognome> listaCognomi = Cognome.findAllByVociGreaterThanEquals(taglio, [order: 'cognome'])
        Cognome cognome
        String testo

        listaCognomi?.each {
            cognome = (Cognome) it
            testo = cognome.testo
            if (LibBio.checkNome(testo)) {
                upload(cognome)
            }// fine del blocco if
        }// fine del ciclo each

        // creaPaginaControllo()
        def stop
    }// fine del metodo

    //--creazione ed upload sul server della singola pagina
    //--comprese eventuali sottopagine
    public  upload(Cognome cognome) {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        ArrayList<BioGrails> listaBiografie = getListaBiografie(cognome)
        String summary = LibBio.getSummary()
        String testo = ''
        String titolo
        String cognomeTxt = ''

        if (cognome) {
            cognomeTxt = cognome.testo
        }// fine del blocco if

        titolo = PATH + cognomeTxt

        //header
        testo += this.getHead(cognome, listaBiografie.size())

        //body
        testo += this.getBody(listaBiografie, true)

        //footer
        testo += this.getFooter(cognome)

        testo = testo.trim()

        //registra la pagina
        if (!debug) {
            new EditBio(titolo, testo, summary)
        }// fine del blocco if

        def stop
    }// fine del metodo

    public  String getHead(Cognome cognome, int num) {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI)
        String numero = ''
        String incipit
        String tagIndice = '__FORCETOC__'
        String tagNoIndice = '__NOTOC__'
        String cognomeTxt = cognome.testo

        if (num) {
            numero = LibTesto.formatNum(num)
        }// fine del blocco if

        if (usaTavolaContenuti) {
            testo += tagIndice
        } else {
            testo += tagNoIndice
        }// fine del blocco if-else
        testo += aCapo

        testo += '<noinclude>'
        testo += aCapo
        testo += "{{StatBio"
        if (numero) {
            testo += "|bio=$numero"
        }// fine del blocco if
        testo += "|data=$dataCorrente}}"
        testo += aCapo

        incipit = "Questa è una lista di persone presenti nell'enciclopedia che hanno il cognome '''${cognomeTxt}''', suddivise per attività principale."
        testo += incipit
        testo += aCapo
        testo += aCapo

        return testo
    }// fine del metodo

    public String getBody(ArrayList<BioGrails> listaBiografie, boolean usaSottopagine) {
        String testo = ''
        Map mappa
        String chiave
        ArrayList<String> listaDidascalie
        int num = 0
        int maxVoci = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_LOCALITA, 100)

        mappa = antroponimoService.getMappaAttività(listaBiografie)
        mappa = antroponimoService.ordinaMappa(mappa)
        if (mappa) {
            mappa?.each {
                chiave = it.key
                listaDidascalie = (ArrayList<String>) mappa.get(chiave)
                num = listaDidascalie.size()
                if (usaSottopagine && num >= maxVoci) {
                    testo += getBodyPagina(chiave, listaDidascalie)
                } else {
                    testo += getBodyPagina(chiave, listaDidascalie)
                }// fine del blocco if-else
            }// fine del ciclo each
        }// fine del blocco if

        return testo
    }// fine del metodo

    public String getBodyPagina(String chiave, ArrayList listaDidascalie) {
        String testo = ''
        String tag = '=='

        testo += tag
        testo += chiave
        testo += tag
        testo += aCapo
        testo += antroponimoService.getParagrafoDidascalia(listaDidascalie)
        testo += aCapo
        testo += aCapo

        return testo
    }// fine del metodo

    public String getFooter(Cognome cognome) {
        String testo = ''
        String cognomeTxt = cognome.testo

        testo += '<noinclude>'
        testo += "[[Categoria:Bio cognomi|${cognomeTxt}]]"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo

    //--cancella tutti i records
    private static resetAll() {
        Cognome.executeUpdate('delete Cognome')
    }// fine del metodo


    private static elaboraCognome(String testo) {
        Cognome cognome = new Cognome()
        int numVoci = BioGrails.countByCognome(testo)

        cognome.testo = testo
        cognome.voci = numVoci
        cognome.lunghezza = testo.length()
        cognome.save(flush: true)
    }// fine del metodo

    //--costruisce una lista di biografie che 'usano' il cognome
    private static ArrayList<BioGrails> getListaBiografie(Cognome cognome) {
        ArrayList<BioGrails> listaBiografie = null
        String cognomeTxt

        if (cognome) {
            cognomeTxt = cognome.testo
        }// fine del blocco if

        if (cognomeTxt) {
            listaBiografie = BioGrails.findAllByCognome(cognomeTxt, [sort: 'forzaOrdinamento'])
        }// fine del blocco if

        return listaBiografie
    }// fine del metodo

} // fine della service classe
