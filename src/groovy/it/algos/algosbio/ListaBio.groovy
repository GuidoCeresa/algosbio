package it.algos.algosbio

import grails.transaction.Transactional
import groovy.util.logging.Log4j
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.Mese
import it.algos.algospref.Pref
import it.algos.algoswiki.Risultato
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

    protected static String TAG_INDICE = '__FORCETOC__'
    protected static String TAG_NO_INDICE = '__NOTOC__'

    protected static String aCapo = '\n'
    protected static String SPAZIO = ' '

    protected Object oggetto    //Giorno, Anno, Antroponimo, Attivita, ecc
    protected String soggetto   //9 marzo, 1934, Mario, Politici, ecc
    protected String soggettoMadre
    protected String titoloPagina
    //Nati il 9 marzo, Nati nel 1934, Persone di nome Mario, Progetto:Biografie/Attività/Politici, ecc
    protected String titoloPaginaMadre = ''
    protected ArrayList<BioGrails> listaBiografie

    protected boolean usaHeadRitorno = false // prima del template di avviso
    protected boolean usaHeadIncipit = false // dopo il template di avviso
    protected boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI, true)
    protected boolean usaSuddivisioneUomoDonna = false // falso per Giorni ed Anni
    protected boolean usaSuddivisioneParagrafi = false
    protected boolean usaParagrafiAlfabetici = false //utilizzabile solo se usaSuddivisioneParagrafi è vero
    protected boolean usaTitoloParagrafoConLink = false  //utilizzabile solo se usaSuddivisioneParagrafi è vero
    protected boolean usaDoppiaColonna = false // vero solo per Giorni ed Anni
    protected boolean usaSottopagine = false // falso per Giorni ed Anni
    protected int maxVociParagrafo = 100
    protected String tagTemplateBio = 'ListaBio' // in alternativa 'StatBio'
    protected String tagLivelloParagrafo = '=='
    protected String tagParagrafoNullo = 'Altre...'
    public boolean registrata = false


    public ListaBio(Object oggetto) {
        this(oggetto, null)
    }// fine del costruttore

    public ListaBio(Object oggetto, BioService bioService) {
        this.oggetto = oggetto
        elaboraSoggetto(oggetto)
        this.bioService = bioService
        inizia(true)
    }// fine del costruttore


    public ListaBio(String soggetto) {
        this(soggetto, true)
    }// fine del costruttore


    public ListaBio(String soggetto, boolean iniziaSubito) {
        this.soggetto = soggetto
        this.soggettoMadre = soggetto
        elaboraOggetto(soggetto)
        this.inizia(iniziaSubito)
    }// fine del costruttore

    protected inizia(boolean iniziaSubito) {
        elaboraParametri()
        elaboraTitolo()

        if (iniziaSubito) {
            elaboraListaBiografie()
            elaboraPagina()
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce un oggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    protected elaboraOggetto(String soggetto) {
    }// fine del metodo

    /**
     * Costruisce un soggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    protected elaboraSoggetto(Object oggetto) {
    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    protected elaboraParametri() {
    }// fine del metodo

    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected void elaboraTitolo() {
    }// fine del metodo

    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected String getTitolo() {
        return ''
    }// fine del metodo

    /**
     * Elaborazione principale della pagina
     */
    protected elaboraPagina() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String summary = LibBio.getSummary()
        String testo = ''
        EditBio paginaModificata
        Risultato risultato

        if (listaBiografie) {
            //header
            testo += this.elaboraHead()

            //body
            testo += this.elaboraBody()

            //footer
            testo += this.elaboraFooter()
        }// fine del blocco if

        //registra la pagina
        if (testo) {
            testo = testo.trim()
            if (debug) {
                paginaModificata = new EditBio('Utente:Biobot/2', testo, summary)
                registrata = paginaModificata.registrata
            } else {
                paginaModificata = new EditBio(titoloPagina, testo, summary)
                registrata = paginaModificata.registrata
            }// fine del blocco if-else
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina
     */
    protected String elaboraHead() {
        // variabili e costanti locali di lavoro
        String testo = ''
        String incipit = elaboraIncipit()
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        int numPersone = listaBiografie.size()
        String personeTxt = LibTesto.formatNum(numPersone)
        String template = tagTemplateBio

        if (usaTavolaContenuti) {
            testo += TAG_INDICE
        } else {
            testo += TAG_NO_INDICE
        }// fine del blocco if-else
        testo += aCapo

        if (usaHeadRitorno) {
            testo += elaboraRitorno()
            testo += aCapo
        }// fine del blocco if

        testo += "<noinclude>"
        testo += aCapo
        testo += "{{${template}"
        testo += "|bio="
        testo += personeTxt
        testo += "|data="
        testo += dataCorrente.trim()
        testo += "}}"
        testo += aCapo

        if (incipit) {
            testo += incipit
            testo += aCapo
        }// fine del blocco if
        testo += "</noinclude>"

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Voce principale a cui tornare
     */
    protected String elaboraRitorno() {
        String testo = ''

        if (titoloPaginaMadre) {
            testo = "{{Torna a|" + titoloPaginaMadre + "}}"
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Frase inziale della pagina (incipit)
     * Sovrascritto
     */
    protected String elaboraIncipit() {
        return ''
    }// fine del metodo

    /**
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        int numPersone = listaBiografie.size()
        boolean usaColonne = usaDoppiaColonna
        int maxRigheColonne = Pref.getInt(LibBio.MAX_RIGHE_COLONNE)
        String testo = elaboraBodyDidascalie()

        if (usaColonne && (numPersone > maxRigheColonne)) {
            testo = WikiLib.listaDueColonne(testo.trim())
        }// fine del blocco if

        testo = elaboraTemplate(testo)

        return testo
    }// fine del metodo

    /**
     * Corpo della pagina - didascalia
     * Decide se c'è la suddivisione in paragrafi
     * Costruisce una mappa in funzione della suddivisione in paragrafi
     *  chiave=una chiave per ogni parametro/paragrafo
     *  valore=una lista di BioGrails
     * Se non c'è suddivisione, la mappa ha un unico valore con chiave vuota
     * Decide se ci sono sottopagine
     * Sovrascritto
     */
    protected String elaboraBodyDidascalie() {
        if (usaSuddivisioneUomoDonna) {
            return elaboraBodyDidascalieNome()
        } else {
            return elaboraBodyDidascalieIndifferenziate()
        }// fine del blocco if-else
    }// fine del metodo

    /**
     * Corpo della pagina
     * Controlla se ci sono voci differenziate uomini/donne
     * Se ci sono divide in due la pagina
     * Se non ci sono, procede come la superclasse
     */
    protected String elaboraBodyDidascalieNome() {
        String testo = ''
        ArrayList<BioGrails> listaVociMaschili
        ArrayList<BioGrails> listaVociFemminili
        String tagMaschio = 'M'
        String tagFemmina = 'F'
        LinkedHashMap<String, ArrayList<BioGrails>> mappa

        listaVociMaschili = selezionaGenere(listaBiografie, tagMaschio)
        listaVociFemminili = selezionaGenere(listaBiografie, tagFemmina)

        if (listaVociMaschili && listaVociFemminili) {
            testo += aCapo
            testo += '=Uomini='
            testo += aCapo
            mappa = getMappa(listaVociMaschili)
            mappa?.each {
                testo += elaboraBodyParagrafo(it)
            }// fine del ciclo each
            testo += aCapo
            testo += '=Donne='
            testo += aCapo
            mappa = getMappa(listaVociFemminili)

            if (mappa.size() == 1) {
                mappa = fixMappa(mappa)
            }// fine del blocco if

            mappa?.each {
                testo += elaboraBodyParagrafo(it)
            }// fine del ciclo each
        } else {
            testo = elaboraBodyDidascalieIndifferenziate()
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    /**
     * Corpo della pagina
     */
    protected String elaboraBodyDidascalieIndifferenziate() {
        String testo = ''
        LinkedHashMap<String, ArrayList<BioGrails>> mappa

        mappa = getMappa(listaBiografie)
        mappa?.each {
            testo += elaboraBodyParagrafo(it)
        }// fine del ciclo each

        testo = testo.trim()
        testo += aCapo

        return testo
    }// fine del metodo

    /**
     * Singolo paragrafo
     * Mappa in funzione della suddivisione in paragrafi
     *  chiave=una chiave per ogni parametro/paragrafo
     *  valore=una lista di BioGrails
     * Decide se ci sono sottopagine
     */
    protected String elaboraBodyParagrafo(def mappa) {
        String chiaveParagrafo
        String titoloParagrafo
        ArrayList<BioGrails> listaVoci
        ArrayList<BioGrails> listaVociOrdinate
        ArrayList<String> listaDidascalie

        chiaveParagrafo = mappa.key
        listaVoci = mappa.value
        listaVociOrdinate = ordinaVoci(listaVoci)
        listaDidascalie = estraeListaDidascalie(listaVociOrdinate)
        titoloParagrafo = elaboraTitoloParagrafo(chiaveParagrafo, listaVociOrdinate)

        return elaboraParagrafo(chiaveParagrafo, titoloParagrafo, listaVociOrdinate, listaDidascalie)
    }// fine del metodo

    protected static String attivitaPluralePerGenere(BioGrails bio) {
        String plurale
        String singolare = bio.attivita
        String sesso = bio.sesso
        Genere genere = Genere.findBySingolareAndSesso(singolare, sesso)

        if (genere) {
            plurale = genere.plurale
        }// fine del blocco if

        if (plurale) {
            plurale = LibTesto.primaMaiuscola(plurale)
            plurale = plurale.trim()
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Incapsula il testo come parametro di un (eventuale) template
     */
    protected String elaboraTemplate(String testoBody, String titoloTemplate) {
        String testoOut = testoBody
        String testoIni = ''
        String testoEnd = '}}'
        int numVoci = listaBiografie.size()

        testoIni += "{{${titoloTemplate}"
        testoIni += aCapo
        testoIni += "|titolo=${titoloPagina}"
        testoIni += aCapo
        testoIni += "|voci=${numVoci}"
        testoIni += aCapo
        testoIni += "|testo="
        testoIni += aCapo

        if (testoBody) {
            testoOut = testoIni + testoBody + testoEnd
        }// fine del blocco if

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
    protected LinkedHashMap<String, ArrayList<BioGrails>> getMappa(ArrayList<BioGrails> listaVoci) {
        LinkedHashMap<String, ArrayList<BioGrails>> mappa = null
        String chiaveOld = 'xyzpippoxyz'
        String chiave = ''
        ArrayList<BioGrails> lista = null
        BioGrails bio

        if (usaSuddivisioneParagrafi) {
            if (listaVoci) {
                mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
                listaVoci?.each {
                    bio = it
                    chiave = getChiaveParagrafo(bio)
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

            if (mappa.size() > 1) {
                mappa = ordinaMappa(mappa)
            } else {
                mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
                mappa.put('', lista)
            }// fine del blocco if-else
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
    protected String getChiaveParagrafo(BioGrails bio) {
        return ''
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con eventuali link
     * Sovrascritto
     */
    protected String elaboraTitoloParagrafo(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo = chiaveParagrafo
        String singolare
        Professione professione
        BioGrails bio

        if (usaTitoloParagrafoConLink && !chiaveParagrafo.equals(tagParagrafoNullo) && listaVoci && listaVoci.size() > 0) {
            titoloParagrafo = '[['
            bio = listaVoci.get(0)
            if (bio) {
                singolare = bio.attivita
            }// fine del blocco if
            if (singolare) {
                professione = Professione.findBySingolare(singolare)
            }// fine del blocco if
            if (professione) {
                titoloParagrafo += LibTesto.primaMaiuscola(professione.voce)
            } else {
                titoloParagrafo += LibTesto.primaMaiuscola(singolare)
            }// fine del blocco if-else
            titoloParagrafo += '|'
            titoloParagrafo += chiaveParagrafo
            titoloParagrafo += ']]'
        }// fine del blocco if-else

        return titoloParagrafo
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
        return bio.didascaliaListe
    }// fine del metodo

    /**
     * Prova ad elaborare il bio (BioGrails) e ad estrarre nuovamente la didascalia
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

    /**
     * Decide se ci sono sottopagine
     * Sovrascritto
     */
    protected String elaboraParagrafo(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate, ArrayList<String> listaDidascalie) {
        String testo = ''
        int num = listaDidascalie.size()

        if (usaSottopagine && num >= maxVociParagrafo) {
            testo += elaboraParagrafoSottoPagina(chiaveParagrafo, titoloParagrafo, listaVociOrdinate)
        } else {
            testo += elaboraParagrafoNormale(titoloParagrafo, listaDidascalie)
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    protected String elaboraParagrafoNormale(String titoloParagrafo, ArrayList<String> listaDidascalie) {
        String testo = ''

        testo += elaboraTitoloParagrafo(titoloParagrafo)
        testo += getParagrafoDidascalia(listaDidascalie)

        return testo + aCapo + aCapo
    }// fine del metodo

    /**
     * Creazione della sottopagina e del rimando
     */
    protected String elaboraParagrafoSottoPagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
        creazioneSottopagina(chiaveParagrafo, titoloParagrafo, listaVociOrdinate)
        return elaborazioneRimando(chiaveParagrafo, titoloParagrafo)
    }// fine del metodo

    /**
     * Creazione della sottopagina
     * Sovrascritto
     */
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
    }// fine del metodo

    /**
     * Creazione del rimando
     */
    protected String elaborazioneRimando(String chiaveParagrafo, String titoloParagrafo) {
        String testo = ''
        String titoloSottovoce = getTitoloSottovoce(chiaveParagrafo)

        testo += elaboraTitoloParagrafo(titoloParagrafo)
        testo += "*{{Vedi anche|${titoloSottovoce}}}"

        return testo + aCapo + aCapo
    }// fine del metodo

    /**
     * Titolo del paragrafo
     */
    protected String elaboraTitoloParagrafo(String titoloParagrafo) {
        String testo = ''

        if (usaSuddivisioneParagrafi && titoloParagrafo) {
            testo += tagLivelloParagrafo
            testo += titoloParagrafo
            testo += tagLivelloParagrafo
            testo += aCapo
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    protected String getTitoloSottovoce(String chiaveParagrafo) {
        return ''
    }// fine del metodo

    /**
     * Elabora soggetto specifico
     */
    protected String elaboraSoggettoSpecifico(String chiaveParagrafo) {
        return LibTesto.primaMaiuscola(soggetto) + '/' + chiaveParagrafo
    }// fine del metodo

    protected static String getParagrafoDidascalia(ArrayList<String> listaDidascalie) {
        String testo = ''
        String didascalia

        listaDidascalie?.each {
            didascalia = it
            testo += '*'
            testo += didascalia
            testo += aCapo
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

    protected static ArrayList<BioGrails> selezionaGenere(ArrayList<BioGrails> listaVoci, String tag) {
        ArrayList<BioGrails> lista = null
        BioGrails bio

        if (listaVoci && listaVoci.size() > 0 && tag) {
            lista = new ArrayList<BioGrails>()
            listaVoci?.each {
                bio = it
                if (bio.sesso.equals(tag)) {
                    lista.add(bio)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return lista
    }// fine del metodo

    protected LinkedHashMap<String, ArrayList<BioGrails>> fixMappa(LinkedHashMap<String, ArrayList<BioGrails>> mappa) {
        ArrayList<BioGrails> lista
        String chiave
        BioGrails bio

        if (mappa.size() == 1) {
            if (mappa.containsKey('')) {
                lista = mappa.get('')
                bio = lista.get(0)
                chiave = getChiaveParagrafo(bio)

                mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
                mappa.put(chiave, lista)
            }// fine del blocco if
        }// fine del blocco if

        return mappa
    }// fine del metodo

}// fine della classe
