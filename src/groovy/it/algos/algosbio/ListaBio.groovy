package it.algos.algosbio

import grails.transaction.Transactional
import groovy.util.logging.Log4j
import it.algos.algoslib.LibHtml
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algoslib.Mese
import it.algos.algospref.Pref
import it.algos.algoswiki.Risultato
import it.algos.algoswiki.WikiLib

/**
 * Created by gac on 17/10/14.
 */
@Log4j
abstract class ListaBio {

    static boolean transactional = false

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    BioService bioService

    private static String PAGINA_PROVA = 'Utente:Biobot/2'
    protected static String TAG_INDICE = '__FORCETOC__'
    protected static String TAG_NO_INDICE = '__NOTOC__'

    protected static String aCapo = '\n'
    protected static String SPAZIO = ' '
    protected static String SLASH = '/'

    protected static String UOMINI = LibListe.UOMINI
    protected static String DONNE = LibListe.DONNE

    protected Object oggetto    //Giorno, Anno, Antroponimo, Attivita, ecc
    protected String soggetto   //9 marzo, 1934, Mario, Politici, ecc
    protected String soggettoMadre
    protected String titoloPagina
    //Nati il 9 marzo, Nati nel 1934, Persone di nome Mario, Progetto:Biografie/Attività/Politici, ecc
    protected String titoloPaginaMadre = ''
    protected ArrayList<BioGrails> listaBiografie
    protected int numPersone = 0
    protected int numDidascalie = 0

    protected boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI, true)
    protected boolean usaInclude = true // vero per Giorni ed Anni
    protected boolean usaHeadRitorno = false // prima del template di avviso
    protected String tagTemplateBio = 'ListaBio' // in alternativa 'StatBio'
    protected boolean usaHeadIncipit = false // dopo il template di avviso
    protected boolean usaSuddivisioneUomoDonna = false // falso per Giorni ed Anni
    protected boolean usaSuddivisioneParagrafi = false
    protected boolean usaAttivitaMultiple = false
    protected boolean usaParagrafiAlfabetici = false //utilizzabile solo se usaSuddivisioneParagrafi è vero
    protected boolean usaTitoloParagrafoConLink = false  //utilizzabile solo se usaSuddivisioneParagrafi è vero
    protected boolean usaTitoloSingoloParagrafo = false  //se c'è un solo paragrafo, niente titolo
    protected boolean usaDoppiaColonna = false // vero solo per Giorni ed Anni
    protected boolean usaSottopagine = false // falso per Giorni ed Anni
    protected int maxVociParagrafo = 100
    protected String tagLivelloParagrafo = '=='
    protected String tagParagrafoNullo = 'Altre...'
    protected static String tagAltri = 'Altri...'
    protected static String tagAltre = 'Altre...'
    protected String tagParagrafoAlfabetico = '...'
    public boolean registrata = false
    public boolean esistonoUominiDonne = false
    public boolean usaSottopaginaAltri

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
        numDidascalie = 0
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
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected elaboraParametri() {
        usaTavolaContenuti = true
        usaInclude = true
        usaHeadRitorno = false
        tagTemplateBio == Pref.getStr(LibBio.NOME_TEMPLATE_AVVISO_LISTE_GIORNI_ANNI, 'ListaBio')
        usaHeadIncipit = false
        usaSuddivisioneUomoDonna = false
        usaSuddivisioneParagrafi = true
        usaAttivitaMultiple = false
        usaParagrafiAlfabetici = false
        usaTitoloParagrafoConLink = true
        usaTitoloSingoloParagrafo = false
        usaDoppiaColonna = false
        usaSottopagine = true
        maxVociParagrafo = 100
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = 'Altre...'
        usaSottopaginaAltri == Pref.getBool(LibBio.USA_SOTTOPAGINA_ALTRI, false)
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
     * <p>
     * Costruisce head <br>
     * Costruisce body <br>
     * Costruisce footer <br>
     * Ogni blocco esce trimmato (inizio e fine) <br>
     * Gli spazi (righe) di separazione vanno aggiunti qui <br>
     * Registra la pagina <br>
     */
    protected elaboraPagina() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String summary = LibBio.getSummary()
        String testo = ''
        EditBio paginaModificata
        Risultato risultato

        if (numPersone > 0) {
            //header
            testo += this.elaboraHead()

            //body
            //a capo, ma senza senza righe di separazione
            testo += aCapo
            testo += this.elaboraBody()

            //footer
            //di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
            testo += this.elaboraFooter()
        }// fine del blocco if

        //registra la pagina
        if (testo) {
            testo = testo.trim()
            if (debug) {
                testo = LibWiki.setBold(titoloPagina) + aCapo + testo
                paginaModificata = new EditBio(PAGINA_PROVA, testo, summary)
                registrata = paginaModificata.registrata
            } else {
                paginaModificata = new EditBio(titoloPagina, testo, summary)
                registrata = paginaModificata.registrata
            }// fine del blocco if-else
        }// fine del blocco if
        def stop
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina (header)
     * <p>
     * Non sovrascrivibile <br>
     * Posiziona il TOC <br>
     * Posiziona il ritorno (eventuale) <br>
     * Posizione il template di avviso <br>
     * Posiziona l'incipit della pagina (eventuale) <br>
     * Ritorno ed avviso vanno (eventualmente) protetti con 'include' <br>
     * Ogni blocco esce trimmato (per l'inizio) e con un solo ritorno a capo per fine riga. <br>
     * Eventuali spazi gestiti da chi usa il metodo <br>
     */
    private String elaboraHead() {
        // variabili e costanti locali di lavoro
        String testo = ''
        String testoIncluso = ''

        // Posiziona il TOC
        testoIncluso += elaboraTOC()

        // Posiziona il ritorno
        testoIncluso += elaboraRitorno()

        // Posizione il template di avviso
        testoIncluso += elaboraTemplateAvviso()

        // Ritorno ed avviso vanno (eventualmente) protetti con 'include'
        testo += elaboraInclude(testoIncluso)

        // Posiziona l'incipit della pagina
        testo += elaboraIncipit()

        // valore di ritorno
        return finale(testo)
    }// fine del metodo

    /**
     * Costruisce il TOC (tavola contenuti)
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo di una delle due possibilità <br>
     */
    private String elaboraTOC() {
        String testo = ''

        if (usaTavolaContenuti) {
            testo += TAG_INDICE
        } else {
            testo += TAG_NO_INDICE
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    /**
     * Costruisce il ritorno alla pagina 'madre'
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraRitorno() {
        String testo = ''

        if (usaHeadRitorno) {
            if (titoloPaginaMadre) {
                testo += "Torna a|" + titoloPaginaMadre
                testo = LibWiki.setGraffe(testo)
            }// fine del blocco if
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Costruisce il template di avviso
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) il nome del template da usare <br>
     */
    private String elaboraTemplateAvviso() {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String personeTxt = LibTesto.formatNum(numPersone)

        testo += tagTemplateBio
        testo += "|bio="
        testo += personeTxt
        testo += "|data="
        testo += dataCorrente.trim()
        testo = LibWiki.setGraffe(testo)

        return testo
    }// fine del metodo

    /**
     * Incorpora ilo testo nel tag 'include'
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo <br>
     */
    private String elaboraInclude(String testoIn) {
        String testoOut = testoIn

        if (usaInclude) {
            testoOut = LibBio.setNoInclude(testoIn)
        }// fine del blocco if

        return testoOut
    }// fine del metodo

    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Non sovrascrivibile <br>
     */
    private String elaboraIncipit() {
        String testo = ''

        if (usaHeadIncipit) {
            testo += elaboraIncipitSpecifico()
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        return ''
    }// fine del metodo

    /**
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        String testo = ''

        boolean usaColonne = usaDoppiaColonna
        int maxRigheColonne = Pref.getInt(LibBio.MAX_RIGHE_COLONNE)
        testo = elaboraBodyDidascalie()

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
            testo += '=Uomini='
            testo += aCapo
            mappa = getMappa(listaVociMaschili)
            mappa?.each {
                testo += elaboraBodyParagrafo(it, UOMINI)
            }// fine del ciclo each
            testo += aCapo
            testo += '=Donne='
            testo += aCapo
            mappa = getMappa(listaVociFemminili)

            if (mappa.size() == 1) {
                mappa = fixMappa(mappa)
            }// fine del blocco if

            mappa?.each {
                testo += elaboraBodyParagrafo(it, DONNE)
            }// fine del ciclo each
            esistonoUominiDonne = true
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
            testo += elaboraBodyParagrafo(it, '')
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
    protected String elaboraBodyParagrafo(def mappa, String tagSesso) {
        String chiaveParagrafo
        ArrayList<BioGrails> listaVoci

        chiaveParagrafo = mappa.key
        listaVoci = mappa.value

        return elaboraBodyParagrafo(chiaveParagrafo, listaVoci, tagSesso)
    }// fine del metodo

    /**
     * Singolo paragrafo
     * Decide se ci sono sottopagine
     */
    protected String elaboraBodyParagrafo(String chiaveParagrafo, ArrayList<BioGrails> listaVoci, String tagSesso) {
        String titoloParagrafo
        ArrayList<BioGrails> listaVociOrdinate
        ArrayList<String> listaDidascalie

        listaVociOrdinate = ordinaVoci(listaVoci)
        listaDidascalie = estraeListaDidascalie(listaVociOrdinate)
        numDidascalie += listaDidascalie.size()
        titoloParagrafo = elaboraTitoloParagrafoBase(chiaveParagrafo, listaVociOrdinate)

        return elaboraParagrafo(chiaveParagrafo, titoloParagrafo, listaVociOrdinate, listaDidascalie, tagSesso)
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


    protected static String attivitaSecondaPluralePerGenere(BioGrails bio) {
        String plurale
        String singolare = bio.attivita2
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

    protected static String attivitaTerzaPluralePerGenere(BioGrails bio) {
        String plurale
        String singolare = bio.attivita3
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

        testoIni += "{{${titoloTemplate}"
        testoIni += aCapo
        testoIni += "|titolo=${titoloPagina}"
        testoIni += aCapo
        testoIni += "|voci=${numPersone}"
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
     * Se non viene incapsulato, lascia una riga vuota iniziale
     * Sovrascritto
     */
    protected String elaboraTemplate(String testoIn) {
        return aCapo + testoIn
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
        String chiave = ''
        ArrayList<BioGrails> lista = null
        BioGrails bio
        int min

        if (usaSuddivisioneParagrafi) {
            if (listaVoci) {
                mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
                listaVoci?.each {
                    bio = it
                    chiave = getChiaveParagrafoBase(bio)
                    lista = putMappa(mappa, chiave, lista, bio)

                    if (usaAttivitaMultiple) {
                        chiave = getChiaveParagrafoSecondaAttivita(bio)
                        if (chiave) {
                            lista = putMappa(mappa, chiave, lista, bio)
                        }// fine del blocco if

                        chiave = getChiaveParagrafoTerzaAttivita(bio)
                        if (chiave) {
                            lista = putMappa(mappa, chiave, lista, bio)
                        }// fine del blocco if
                    }// fine del blocco if

                }// fine del ciclo each
            }// fine del blocco if


            if (usaTitoloSingoloParagrafo) {
                min = 0
            } else {
                min = 1
            }// fine del blocco if-else

            if (mappa.size() > min) {
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

    private
    static ArrayList<BioGrails> putMappa(LinkedHashMap<String, ArrayList<BioGrails>> mappa, String chiave, ArrayList<BioGrails> lista, BioGrails bio) {
        if (mappa.get(chiave)) {
            lista = mappa.get(chiave)
            lista.add(bio)
        } else {
            lista = new ArrayList<BioGrails>()
            lista.add(bio)
            mappa.put(chiave, lista)
        }// fine del blocco if-else

        return lista
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     */
    private String getChiaveParagrafoBase(BioGrails bio) {
        String chiave

        if (usaParagrafiAlfabetici) {
            chiave = getChiaveParagrafoAlfabetico(bio)
        } else {
            chiave = getChiaveParagrafo(bio)
        }// fine del blocco if-else

        return chiave
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    protected String getChiaveParagrafo(BioGrails bio) {
        return ''
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo per la seconda attività
     * Sovrascritto
     */
    protected String getChiaveParagrafoSecondaAttivita(BioGrails bio) {
        return ''
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo per la terza attività
     * Sovrascritto
     */
    protected String getChiaveParagrafoTerzaAttivita(BioGrails bio) {
        return ''
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     */
    protected String getChiaveParagrafoAlfabetico(BioGrails bio) {
        String chiave = bio.cognome

        if (chiave) {
            chiave = chiave.substring(0, 1).toUpperCase()
        } else {
            chiave = tagParagrafoAlfabetico
        }// fine del blocco if-else

        return chiave
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con eventuali link
     */
    private String elaboraTitoloParagrafoBase(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String chiave

        if (usaParagrafiAlfabetici) {
            chiave = chiaveParagrafo
        } else {
            chiave = elaboraTitoloParagrafo(chiaveParagrafo, listaVoci)
        }// fine del blocco if-else

        return chiave
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
        Attivita attivita

        if (usaTitoloParagrafoConLink && !chiaveParagrafo.equals(tagParagrafoNullo) && !chiaveParagrafo.equals(tagAltri) && !chiaveParagrafo.equals(tagAltre) && listaVoci && listaVoci.size() > 0) {
            bio = listaVoci.get(0)
            if (bio) {
                attivita = bio.attivitaLink
                if (attivita) {
                    singolare = attivita.singolare
                } else {
                    singolare = bio.attivita
                }// fine del blocco if-else
            }// fine del blocco if

            if (singolare) {
                professione = Professione.findBySingolare(singolare)
            }// fine del blocco if
            if (professione) {
                titoloParagrafo = LibTesto.primaMaiuscola(professione.voce)
            } else {
                titoloParagrafo = LibTesto.primaMaiuscola(singolare)
            }// fine del blocco if-else

            if (!titoloParagrafo.equals(chiaveParagrafo)) {
                titoloParagrafo += '|'
                titoloParagrafo += chiaveParagrafo
            }// fine del blocco if

            if (titoloParagrafo) {
                titoloParagrafo = LibWiki.setQuadre(titoloParagrafo)
            }// fine del blocco if
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
    protected String elaboraParagrafo(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate, ArrayList<String> listaDidascalie, String tagSesso) {
        String testo = ''
        int num = listaDidascalie.size()
        boolean troppeVoci = (num >= maxVociParagrafo)

        boolean chiaveNonAlfabetica = (!chiaveParagrafo.equals(tagParagrafoAlfabetico))
        boolean paragrafoNullo = (chiaveParagrafo.equals(tagParagrafoNullo) || chiaveParagrafo.equals(tagAltri) || chiaveParagrafo.equals(tagAltre))
        boolean usaSottopaginaNormale = (!paragrafoNullo && usaSottopagine && troppeVoci && chiaveNonAlfabetica)
        boolean usaSottopaginaNulla = (paragrafoNullo && usaSottopaginaAltri)

        if (usaSottopaginaNormale || usaSottopaginaNulla) {
            testo += elaboraParagrafoSottoPagina(chiaveParagrafo, titoloParagrafo, listaVociOrdinate, tagSesso)
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
    protected String elaboraParagrafoSottoPagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate, String tagSesso) {
        creazioneSottopagina(chiaveParagrafo, titoloParagrafo, listaVociOrdinate, tagSesso)
        return elaborazioneRimando(chiaveParagrafo, titoloParagrafo, tagSesso)
    }// fine del metodo

    /**
     * Creazione della sottopagina
     * Sovrascritto
     */
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate, String tagSesso) {
    }// fine del metodo

    /**
     * Creazione del rimando
     */
    protected String elaborazioneRimando(String chiaveParagrafo, String titoloParagrafo, String tagSesso) {
        String testo = ''
        String titoloSottovoce = getTitoloSottovoce(chiaveParagrafo, tagSesso)

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
    protected String getTitoloSottovoce(String chiaveParagrafo, String tagSesso) {
        return ''
    }// fine del metodo

    /**
     * Elabora soggetto specifico
     */
    protected String elaboraSoggettoSpecifico(String chiaveParagrafo, String tagSesso) {
        return LibTesto.primaMaiuscola(soggetto) + '/' + chiaveParagrafo
    }// fine del metodo

    protected String getParagrafoDidascalia(ArrayList<String> listaDidascalie) {
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
     * <p>
     * Aggiungere (di solito) inizialmente la chiamata al metodo elaboraFooterSpazioIniziale <br>
     * Sovrascritto
     */
    protected String elaboraFooter() {
        return ''
    }// fine del metodo

    /**
     * Piede della pagina - Parte iniziale
     */
    protected String elaboraFooterSpazioIniziale() {
        String testo = ''

        testo += aCapo
        if (esistonoUominiDonne) {
            testo += aCapo
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     * Sovrascritto
     */
    protected elaboraListaBiografie() {
        if (listaBiografie) {
            numPersone = listaBiografie.size()
        }// fine del blocco if
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
            listaChiavi.remove(tagParagrafoNullo) //elimino (per metterlo in fondo)
            listaChiavi.remove(tagAltri) //elimino (per metterlo in fondo)
            listaChiavi.remove(tagAltre) //elimino (per metterlo in fondo)
            listaChiavi.remove(tagParagrafoAlfabetico) //elimino l'asterisco (per metterlo in fondo)
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
                valore = mappaIn.get(tagAltri)
                if (valore) {
                    mappaOut.put(tagAltri, valore)
                }// fine del blocco if
                valore = mappaIn.get(tagAltre)
                if (valore) {
                    mappaOut.put(tagAltre, valore)
                }// fine del blocco if
                valore = mappaIn.get(tagParagrafoAlfabetico)
                if (valore) {
                    mappaOut.put(tagParagrafoAlfabetico, valore)
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
                chiave = getChiaveParagrafoBase(bio)

                mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
                mappa.put(chiave, lista)
            }// fine del blocco if
        }// fine del blocco if

        return mappa
    }// fine del metodo

    /**
     * Voci correlate
     * Regola il livello del paragrafo
     */
    protected String getVociCorrelate() {
        String testo = ''

        if (usaSuddivisioneUomoDonna && esistonoUominiDonne) {
            testo += aCapo
            testo += '=Voci correlate='
        } else {
            testo += '==Voci correlate=='
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    /**
     * Note
     * Regola il livello del paragrafo
     */
    protected String getNote(String descrizione) {
        String testo = ''

        if (usaAttivitaMultiple) {
            if (usaSuddivisioneUomoDonna && esistonoUominiDonne) {
                testo += aCapo
                testo += '=Note='
            } else {
                testo += '==Note=='
            }// fine del blocco if-else
            testo += aCapo
            testo += descrizione
        }// fine del blocco if

        return finale(testo)
    }// fine del metodo

    /**
     * Trim iniziale
     * <p>
     * Ogni blocco esce trimmato (per l'inizio) e con un solo ritorno a capo per fine riga. <br>
     */
    protected static String finale(String testoIn) {
        String testoOut = testoIn

        // trim finale
        if (testoIn) {
            testoOut = testoIn.trim()
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    }// fine del metodo

    /**
     * Titoli dei paragrafi maschili
     * <p>
     * Maiuscoli ed ordinati alfabeticamente
     */
    protected static ArrayList<String> paragrafiAttivitaMaschili() {
        ArrayList<String> paragrafi
        String query

        query = "select distinct(plurale) from Genere where sesso='M' order by plurale asc"
        paragrafi = (ArrayList<String>) Genere.executeQuery(query)

        return paragrafi
    } // fine del metodo

    /**
     * Titoli dei paragrafi femminili
     * <p>
     * Maiuscoli ed ordinati alfabeticamente
     */
    protected static ArrayList<String> paragrafiAttivitaFemminili() {
        ArrayList<String> paragrafi
        String query

        query = "select distinct(plurale) from Genere where sesso='F' order by plurale asc"
        paragrafi = (ArrayList<String>) Genere.executeQuery(query)

        return paragrafi
    } // fine del metodo

    /**
     * Titoli dei paragrafi maschili e femminili
     * <p>
     * Maiuscoli ed ordinati alfabeticamente
     */
    protected static ArrayList<String> paragrafiAttivitaAmbigenere() {
        ArrayList<String> paragrafi
        String query

        query = "select distinct(plurale) from Genere order by plurale asc"
        paragrafi = (ArrayList<String>) Genere.executeQuery(query)

        return paragrafi
    } // fine del metodo

}// fine della classe
