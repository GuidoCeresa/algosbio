package it.algos.algosbio

import grails.transaction.Transactional
import groovy.util.logging.Log4j
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.Mese
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiLib

/**
 * Created by gac on 17/10/14.
 */
@Log4j
@Transactional(readOnly = false)
abstract class ListaBio {
    static transactional = false

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    BioService bioService

    protected Object oggetto
    protected ArrayList<BioGrails> listaBiografie
    protected boolean loggato
    protected static String TAG_INDICE = '__FORCETOC__'
    protected static String TAG_NO_INDICE = '__NOTOC__'

    protected static String A_CAPO = '\n'
    protected static String SPAZIO = ' '

    protected boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI, true)
    protected boolean usaSuddivisioneParagrafi = false
    protected boolean usaDoppiaColonna = false
    protected boolean usaSottopagine = false
    protected String tagLivelloParagrafo = '=='
    protected String tagParagrafoNullo = 'Altre...'

    public ListaBio(String soggetto) {
        this(soggetto, false)
    }// fine del costruttore

    public ListaBio(String soggetto, boolean loggato) {
        this.loggato = loggato
        inizia(soggetto)
    }// fine del costruttore


    protected inizia(String soggetto) {
        elaboraOggetto(soggetto)
        elaboraParametri()
        elaboraListaBiografie()
        elaboraPagina()
    }// fine del metodo

    /**
     * Costruisce un oggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    protected elaboraOggetto(String soggetto) {
    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    protected elaboraParametri() {
    }// fine del metodo

    /**
     * Elaborazione principale della pagina
     */
    protected elaboraPagina() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String summary = LibBio.getSummary()
        String titolo = getTitolo()
        String testo = ''

        //header
        testo += this.elaboraHead()

        //body
        testo += this.elaboraBody()

        //footer
        testo += this.elaboraFooter()

        //registra la pagina
        testo = testo.trim()
        if (!debug) {
            new EditBio(titolo, testo, summary)
        }// fine del blocco if
        if (debug && loggato) {
            new EditBio('Utente:Biobot/2', testo, summary)
        }// fine del blocco if
        def stop
    }// fine del metodo

    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected String getTitolo() {
        return ''
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina
     */
    protected String elaboraHead() {
        // variabili e costanti locali di lavoro
        String testo = ''
        String torna = elaboraRitornoPrincipale()
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        int numPersone = listaBiografie.size()
        String personeTxt = LibTesto.formatNum(numPersone)

        if (usaTavolaContenuti) {
            testo += TAG_INDICE
        } else {
            testo += TAG_NO_INDICE
        }// fine del blocco if-else
        testo += A_CAPO

        testo += "<noinclude>"
        testo += A_CAPO
        testo += "{{ListaBio"
        testo += "|bio="
        testo += personeTxt
        testo += "|data="
        testo += dataCorrente.trim()
        testo += "}}"
        testo += A_CAPO
        if (torna) {
            testo += "{{torna a|$torna}}"
            testo += A_CAPO
        }// fine del blocco if
        testo += "</noinclude>"

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Pagina principale a cui tornare
     * Sovrascritto
     */
    protected String elaboraRitornoPrincipale() {
        return ''
    }// fine del metodo

    /**
     * Corpo della pagina
     * Decide se c'è la suddivisione in paragrafi
     * Costruisce una mappa in funzione della suddivisione in paragrafi
     *  chiave=una chiave per ogni parametro/paragrafo
     *  valore=una lista di BioGrails
     * Se non c'è suddivisione, la mappa ha un unico valore con chiave vuota
     * Decide se ci sono sottopagine
     * Decide se c'è la doppia colonna
     * Sovrascritto
     */
    protected String elaboraBody() {
        String testo = ''
        LinkedHashMap mappa = null
        String chiave
        ArrayList<BioGrails> listaVoci
        ArrayList<BioGrails> listaVociOrdinate
        ArrayList<String> listaDidascalie
        int num = 0
        int maxVoci = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_LOCALITA, 100)

        mappa = getMappa(listaBiografie)

        mappa?.each {
            chiave = it.key
            listaVoci = (ArrayList<BioGrails>) mappa.get(chiave)
            listaVociOrdinate = ordinaVoci(listaVoci)
            num = listaVociOrdinate.size()
            listaDidascalie = estraeListaDidascalie(listaVociOrdinate)
            if (usaSottopagine && num >= maxVoci) {
                testo += elaboraParagrafo(chiave, listaDidascalie)
            } else {
                testo += elaboraParagrafo(chiave, listaDidascalie)
            }// fine del blocco if-else
        }// fine del ciclo each

        if (usaDoppiaColonna) {
            testo = WikiLib.listaDueColonne(testo.trim())
        }// fine del blocco if
        testo += A_CAPO
        testo = elaboraTemplate(testo)

        return testo
    }// fine del metodo

    /**
     * Incapsula il testo come parametro di un (eventuale) template
     */
    protected String elaboraTemplate(String testoBody, String titoloTemplate) {
        String testoOut = testoBody
        String testoIni = ''
        String testoEnd = '}}'
        String titoloPagina = getTitolo()
        int numVoci = listaBiografie.size()

        testoIni += "{{${titoloTemplate}"
        testoIni += A_CAPO
        testoIni += "|titolo=${titoloPagina}"
        testoIni += A_CAPO
        testoIni += "|voci=${numVoci}"
        testoIni += A_CAPO
        testoIni += "|testo="
        testoIni += A_CAPO

        testoOut = testoIni + testoBody + testoEnd
        return testoOut
    }// fine del metodo

    /**
     * Incapsula il testo come parametro di un (eventuale) template
     * Sovrascritto
     */
    protected String elaboraTemplate(String testoIn) {
        return testoIn
    }// fine del metodo

    /**
     * Suddivide le voci a seconda del parametro di riferimento
     *
     * costruisce una mappa con:
     * una chiave per ogni parametro
     * una lista di BioGrails
     *
     * @param listaVoci
     * @param tipoMappa
     * @return mappa
     */
    protected LinkedHashMap getMappa(ArrayList<BioGrails> listaVoci) {
        LinkedHashMap<String, ArrayList<BioGrails>> mappa = null
        String chiaveOld = 'xyzpippoxyz'
        String chiave = ''
        ArrayList<BioGrails> lista
        BioGrails bio

        if (usaSuddivisioneParagrafi) {
            if (listaVoci) {
                mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
                listaVoci?.each {
                    bio = it
                    chiave = getChiave(bio)
                    if (chiave.equals(chiaveOld)) {
                        lista = mappa.get(chiave)
                        lista.add(bio)
                    } else {
                        if (mappa.get(chiave)) {
                            lista = mappa.get(chiave)
                            lista.add(bio)
                        } else {
                            lista = new ArrayList<BioGrails>()
                            lista.add(bio)
                            mappa.put(chiave, lista)
                            chiaveOld = chiave
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                }// fine del ciclo each
            }// fine del blocco if

            if (mappa) {
                mappa = ordinaMappa(mappa)
            }// fine del blocco if
        } else {
            mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
            mappa.put('', listaVoci)
        }// fine del blocco if-else

        return mappa
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    protected String getChiave(BioGrails bio) {
        return ''
    }// fine del metodo

    protected static ArrayList<BioGrails> ordinaVoci(ArrayList<BioGrails> listaVoci) {
        ArrayList<BioGrails> listaVociOrdinate = listaVoci

        return listaVociOrdinate
    }// fine del metodo

    /**
     * Utilizza la didascalia prevista per il tipo di pagina in elaborazione
     * Sovrascritto
     */
    private ArrayList<String> estraeListaDidascalie(ArrayList<BioGrails> listaVoci) {
        ArrayList<String> listaDidascalie = null
        BioGrails bio
        String didascalia

        if (listaVoci && listaVoci.size() > 0) {
            listaDidascalie = new ArrayList<String>()
            listaVoci?.each {
                bio = (BioGrails) it
                didascalia = estraeDidascalia(bio)
                if (didascalia) {
                    listaDidascalie.add(didascalia)
                } else {
                    didascalia = elaboraDidascaliaMancante(bio)
                    if (didascalia) {
                        listaDidascalie.add(didascalia)
                    }// fine del blocco if
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        return listaDidascalie
    }// fine del metodo

    /**
     * Utilizza la didascalia prevista per il tipo di pagina in elaborazione
     * Sovrascritto
     */
    protected String estraeDidascalia(BioGrails bio) {
        return ''
    }// fine del metodo

    /**
     * Prova ad elaborare il bio (BioGrails) e ad estrarre nuyovamente la didascalia
     */
    private String elaboraDidascaliaMancante(BioGrails bio) {
        String didascalia = ''
        int pageid = 0

        if (bio) {
            pageid = bio.pageid
        }// fine del blocco if

        if (bioService) {
            bioService.elabora(pageid)
            didascalia = estraeDidascalia(bio)
        }// fine del blocco if

        return didascalia
    }// fine del metodo


    protected String elaboraParagrafo(String titoloParagrafo, ArrayList<String> listaDidascalie) {
        String testo = ''
        String tag = tagLivelloParagrafo

        if (titoloParagrafo) {
            testo += tag
            testo += titoloParagrafo
            testo += tag
        }// fine del blocco if

        testo += A_CAPO
        testo += getParagrafoDidascalia(listaDidascalie)
        testo += A_CAPO
        testo += A_CAPO

        return testo
    }// fine del metodo

    protected static String getParagrafoDidascalia(ArrayList<String> listaDidascalie) {
        String testo = ''
        String didascalia

        listaDidascalie?.each {
            didascalia = it
            testo += '*'
            testo += didascalia
            testo += A_CAPO
        }// fine del ciclo each

        return testo.trim()
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected elaboraFooter() {
        return ''
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     * Sovrascritto
     */
    protected elaboraListaBiografie() {
    }// fine del metodo

    /**
     * Ordina una mappa
     *
     * @param mappa non ordinata
     * @return mappa ordinata
     */
    protected LinkedHashMap ordinaMappa(LinkedHashMap mappaIn) {
        LinkedHashMap mappaOut = mappaIn
        ArrayList<String> listaChiavi
        String chiave
        def valore

        if (mappaIn && mappaIn.size() > 1) {
            listaChiavi = mappaIn.keySet()
            listaChiavi.remove(tagParagrafoNullo) //elimino l'asterisco (per metterlo in fondo)
            listaChiavi = ordinaChiavi(listaChiavi)
            if (listaChiavi) {
                mappaOut = new LinkedHashMap()
                listaChiavi?.each {
                    chiave = it
                    valore = mappaIn.get(chiave)
                    mappaOut.put(chiave, valore)
                }// fine del blocco if

                // aggiungo (in fondo) l'asterisco. Se c'è.
                valore = mappaIn.get(tagParagrafoNullo)
                if (valore) {
                    mappaOut.put(tagParagrafoNullo, valore)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappaOut
    }// fine della closure

    /**
     * Ordina le chiavi di una mappa
     * Sovrascritto
     *
     * @param chiavi non ordinate
     * @return chiavi ordinate
     */
    protected ArrayList<String> ordinaChiavi(ArrayList<String> listaChiaviIn) {
        ArrayList<String> listaChiaviOut = listaChiaviIn.sort()
        ArrayList lista = new ArrayList()
        def pos
        def mes

        listaChiaviOut?.each {
            pos = Mese.allLongList
        } // fine del ciclo each

        // valore di ritorno
        return listaChiaviOut
    }// fine della closure

}// fine della classe
