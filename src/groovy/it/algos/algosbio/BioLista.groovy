package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit
import it.algos.algoswiki.Risultato
import org.apache.commons.logging.LogFactory

/**
 * Created by IntelliJ IDEA.
 * User: Gac
 * Date: 11/02/11
 * Time: 16.41
 */
class BioLista {

    private static def log = LogFactory.getLog(this)
    protected static String PUNTI = '...'
    public static boolean TRIPLA_ATTIVITA = true //@todo eventuale preferenza
    protected static boolean DOPPIO_SPAZIO = false
    protected static boolean DIM_PARAGRAFO = false
    protected static boolean SOTTOPAGINE = true
    protected static int NUM_RIGHE_PER_SOTTOPAGINA = 50
    protected static boolean CARATTERE_SOTTOPAGINA = true
    protected static int NUM_RIGHE_PER_CARATTERE_SOTTOPAGINA = 50
    protected static String INI_RIGA = '*'
    protected static String A_CAPO = '\n'
    private static TAG_ALTRE = 'Altre...'
    private static boolean TITOLO_PARAGRAFO_CON_LINK = true

    private ArrayList listaWrapper
    private String plurale
    private String categoria
    protected String titoloPagina
    protected String titoloParagrafo
    protected String campoParagrafo
    protected ArrayList campiParagrafi
    protected String testo
    protected Ordinamento ordinamento
    protected BioLista bioLista


    public BioLista() {
        // rimanda al costruttore della superclasse
        super()
    }// fine del metodo costruttore completo

    public BioLista(String plurale, ArrayList listaWrapper, def bioLista) {
        // rimanda al costruttore della superclasse
        this(plurale, listaWrapper, bioLista, null)
    }// fine del metodo costruttore completo

    public BioLista(String plurale, ArrayList listaDidascalie, Ordinamento ordinamento) {
        // rimanda al costruttore della superclasse
        this(plurale, listaDidascalie, (BioLista) null, ordinamento)
    }// fine del metodo costruttore completo

    public BioLista(String plurale, ArrayList listaWrapper, def bioLista, Ordinamento ordinamento) {
        // rimanda al costruttore della superclasse
        super()

        // regola le variabili di istanza coi parametri
        this.setPlurale(plurale)
        this.setCategoria(plurale)
        this.setListaWrapper(listaWrapper)
        this.bioLista = bioLista
        this.ordinamento = ordinamento

        // Metodo iniziale con il plurale dell'attività
        this.inizializza()
    }// fine del metodo costruttore completo

    /**
     * Metodo iniziale con la lista delle didascalie
     */

    private inizializza() {

        // Regola i tag
        this.regolaTag()

        // regola l'ordinamento
        this.regolaOrdinamento()

        // Crea il titolo del paragrafo/pagina
        this.creaTitolo()

        // regola il contenuto
        this.regolaContenuto()
    }// fine del metodo

    /**
     * Regola i tag
     */

    protected regolaTag() {
    }// fine del metodo

    /**
     * Regola l'ordinamento
     */

    protected regolaOrdinamento() {
    }// fine del metodo

    /**
     * Crea il titolo del paragrafo/pagina
     */

    protected creaTitolo() {
    }// fine del metodo

    /**
     * Regola il contenuto
     */

    protected regolaContenuto() {
    }// fine del metodo

    /**
     * Contenuto
     */

    protected String getContenuto() {
        return ''
    }// fine del metodo

    /**
     * Registra la pagina
     */

    public boolean registra() {
        boolean registrata = false
        String titolo = this.getTitoloPagina()
        String testo = this.getContenuto()
        String summary = LibBio.getSummary()
        EditBio paginaModificata
        Risultato risultato

        if (!Pref.getBool(LibBio.DEBUG)) {
            // registra la pagina solo se ci sono differenze significative
            // al di la della prima riga con il richiamo al template e che contiene la data
            if (titolo && testo && this.listaWrapper && this.listaWrapper.size() > 0) {
                try { // prova ad eseguire il codice
                    paginaModificata = new EditBio(titolo, testo, summary)
                    risultato = paginaModificata.getRisultato()

                    if ((risultato == Risultato.modificaRegistrata) || (risultato == Risultato.allineata)) {
                        registrata = true
                    } else {
//                log.warn "La pagina $titolo è $risultato"
                    }// fine del blocco if-else

                } catch (Exception unErrore) { // intercetta l'errore
                    log.error titolo + ' - ' + unErrore
                }// fine del blocco try-catch
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo


    protected static String getTestoIni(int numRec) {
        // variabili e costanti locali di lavoro
        String testo = ''
        boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI)
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String numero = LibTesto.formatNum(numRec)
        String tagIndice = '__FORCETOC__'
        String tagNoIndice = '__NOTOC__'
        String aCapo = '\n'

        if (usaTavolaContenuti) {
            testo += tagIndice
        } else {
            testo += tagNoIndice
        }// fine del blocco if-else
        testo += aCapo

        testo += "<noinclude>"
        testo += "{{StatBio"
        testo += "|bio="
        testo += numero
        testo += "|data="
        testo += dataCorrente.trim()
        testo += "}}"
        testo += "</noinclude>"

        // valore di ritorno
        return testo.trim()
    }// fine del metodo


    protected String getTestoEnd(String attNaz) {
        // variabili e costanti locali di lavoro
        String testo = ''
        String aCapo = '\n'
        String categoria = this.getCategoria()
        String plurale = this.getPlurale()
        String attNazMaiuscola
        String attNazMinuscola

        categoria = LibTesto.primaMaiuscola(categoria)
        plurale = LibTesto.primaMaiuscola(plurale)
        attNazMaiuscola = LibTesto.primaMaiuscola(attNaz)
        attNazMinuscola = LibTesto.primaMinuscola(attNaz)

        testo += aCapo
        testo += '==Voci correlate=='
        testo += aCapo
        if (categoria) {
            testo += "*[[:Categoria:${categoria}]]"
            testo += aCapo
        }// fine del blocco if
        testo += "*[[Progetto:Biografie/${attNazMaiuscola}]]"
        testo += aCapo
        testo += aCapo
        testo += '{{Portale|biografie}}'
        testo += aCapo
        testo += aCapo
        if (categoria) {
            testo += "<noinclude>[[Categoria:Bio ${attNazMinuscola}|${plurale}]]</noinclude>"
        } else {
            testo += "<noinclude>[[Categoria:Bio ${attNazMinuscola}]]</noinclude>"
        }// fine del blocco if-else

        // valore di ritorno
        return testo
    }// fine del metodo

    protected int dimWrapper() {
        // variabili e costanti locali di lavoro
        int dimWrapper = 0

        if (this.listaWrapper) {
            if (this.listaWrapper.size() > 0 && this.listaWrapper[0] in BioLista) {
                this.listaWrapper?.each {
                    dimWrapper += it.listaWrapper.size()
                }// fine di each
            } else {
                dimWrapper = this.listaWrapper.size()
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return dimWrapper
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
    private static LinkedHashMap getMappa(ArrayList<BioGrails> listaVoci, TipoMappa tipoMappa) {
        LinkedHashMap<String, ArrayList<BioGrails>> mappa = null
        String chiaveOld = 'xyzpippoz'
        String chiave = ''
        ArrayList<BioGrails> lista
        BioGrails bio

        if (listaVoci) {
            mappa = new LinkedHashMap<String, ArrayList<BioGrails>>()
            listaVoci?.each {
                bio = it
                chiave = getChiave(bio, tipoMappa)
//                attivita = bio.attivita
//                if (attivita) {
//                    chiave = getAttivita(bio)
//                    if (!chiave) {
//                        chiave = TAG_ALTRE
//                    }// fine del blocco if
//                } else {
//                    chiave = TAG_ALTRE
//                }// fine del blocco if-else

                chiave = chiaveUnica(mappa, chiave)

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
            mappa = (LinkedHashMap) ordinaMappa(mappa)
        }// fine del blocco if

        return mappa
    }// fine del metodo

    public static String getChiave(BioGrails bio, TipoMappa tipoMappa) {
        String chiave = ''
        String attivita
        String iniziale

        switch (tipoMappa) {
            case TipoMappa.attivita:
                attivita = bio.attivita
                if (attivita) {
                    chiave = getAttivita(bio)
                    if (!chiave) {
                        chiave = TAG_ALTRE
                    }// fine del blocco if
                } else {
                    chiave = TAG_ALTRE
                }// fine del blocco if-else
                break
            case TipoMappa.nazionalita:
                break
            case TipoMappa.lettera:
                iniziale = bio.cognome
                if (iniziale) {
                    chiave = iniziale.substring(0, 1)
                } else {
                    chiave = TAG_ALTRE
                }// fine del blocco if-else
                break
            case TipoMappa.localita:
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        return chiave
    }// fine del metodo

    // restituisce il nome dell'attività
    // restituisce il plurale
    // restituisce il primo carattere maiuscolo
    // aggiunge un link alla voce di riferimento
    public static String getAttivita(BioGrails bio) {
        String attivitaLinkata = ''
        String singolare
        boolean link = TITOLO_PARAGRAFO_CON_LINK
        String attivita
        String genere
        Professione professione

        if (bio) {
            singolare = bio.attivita
            genere = bio.sesso
            if (singolare) {
                attivita = attivitaPluralePerGenere(singolare, genere)
                if (attivita) {
                    if (link) {
                        professione = Professione.findBySingolare(singolare)
                        attivitaLinkata = '[['
                        if (professione) {
                            attivitaLinkata += LibTesto.primaMaiuscola(professione.voce)
                        } else {
                            attivitaLinkata += LibTesto.primaMaiuscola(singolare)
                        }// fine del blocco if-else
                        attivitaLinkata += '|'
                        attivitaLinkata += attivita
                        attivitaLinkata += ']]'
                    } else {
                        attivitaLinkata = attivita
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return attivitaLinkata
    }// fine del metodo

    public static String chiaveUnica(Map mappa, String chiaveIn) {
        String chiaveOut = chiaveIn
        String chiaveAttivita = chiaveIn.substring(chiaveIn.indexOf('|') + 1, chiaveIn.length())
        ArrayList<String> listaChiaviGiaUsate = mappa.keySet()
        String chiaveGiaUsata
        String chiaveRidotta

        listaChiaviGiaUsate?.each {
            chiaveGiaUsata = it
            chiaveRidotta = chiaveGiaUsata.substring(chiaveGiaUsata.indexOf('|') + 1, chiaveGiaUsata.length())
            if (chiaveRidotta.equals(chiaveAttivita)) {
                chiaveOut = chiaveGiaUsata
            }// fine del blocco if
        }// fine del blocco if

        return chiaveOut
    }// fine del metodo

    /**
     * Suddivide le voci a seconda delle attività
     *
     * costruisce una mappa con:
     * una chiave per ogni attività
     * una lista di BioGrails
     *
     * @param listaVoci
     * @return mappa
     */
    public static LinkedHashMap getMappaAttività(ArrayList<BioGrails> listaVoci) {
        return getMappa(listaVoci, TipoMappa.attivita)
    }// fine del metodo

    /**
     * Suddivide le voci a seconda delle nazionalità
     *
     * costruisce una mappa con:
     * una chiave per ogni nazionalità
     * una lista di BioGrails
     *
     * @param listaVoci
     * @return mappa
     */
    public static LinkedHashMap getMappaNazionalita(ArrayList<BioGrails> listaVoci) {
        return getMappa(listaVoci, TipoMappa.nazionalita)
    }// fine del metodo

    /**
     * Suddivide le voci a seconda della lettera iniziale del cognome
     *
     * costruisce una mappa con:
     * una chiave per ogni lettera (A, B, C, D, ...)
     * una lista di BioGrails
     *
     * @param listaVoci
     * @return mappa
     */
    public static LinkedHashMap getMappaLetteraIniziale(ArrayList<BioGrails> listaVoci) {
        return getMappa(listaVoci, TipoMappa.lettera)
    }// fine del metodo

    /**
     * Suddivide le voci a seconda della località
     *
     * costruisce una mappa con:
     * una chiave per ogni località
     * una lista di BioGrails
     *
     * @param listaVoci
     * @return mappa
     */
    public static LinkedHashMap getMappaLocalita(ArrayList<BioGrails> listaVoci) {
        return getMappa(listaVoci, TipoMappa.localita)
    }// fine del metodo

    private static String attivitaPluralePerGenere(String singolare, String sesso) {
        String plurale
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
     * Ordina una mappa
     *
     * @param mappa non ordinata
     * @return mappa ordinata
     */
    private static Map ordinaMappa(Map mappaIn) {
        // variabili e costanti locali di lavoro
        Map mappaOut = mappaIn
        ArrayList<String> listaChiavi
        String chiave
        def valore

        if (mappaIn && mappaIn.size() > 1) {
            listaChiavi = mappaIn.keySet()
            listaChiavi.remove(TAG_ALTRE) //elimino l'asterisco (per metterlo in fondo)
            listaChiavi = ordinaChiavi(listaChiavi)
            if (listaChiavi) {
                mappaOut = new LinkedHashMap()
                listaChiavi?.each {
                    chiave = it
                    valore = mappaIn.get(chiave)
                    mappaOut.put(chiave, valore)
                }// fine del blocco if

                // aggiungo (in fondo) l'asterisco. Se c'è.
                valore = mappaIn.get(TAG_ALTRE)
                if (valore) {
                    mappaOut.put(TAG_ALTRE, valore)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappaOut
    }// fine della closure

    /**
     * Ordina una mappa
     *
     * @param mappa non ordinata
     * @return mappa ordinata
     */
    private static ArrayList<String> ordinaChiavi(ArrayList<String> listaChiaviIn) {
        ArrayList<String> listaChiaviOut = listaChiaviIn.sort()
        String chiaveCompleta
        String chiaveRidotta
        String chiaveDoppia
        ArrayList<String> listaChiavi
        String chiave
        String valore
        ArrayList<String> lista = new ArrayList<String>()
        String tag = 'x3x7x4x'

        if (true) {
            listaChiaviOut?.each {
                chiaveCompleta = it
                chiaveRidotta = chiaveCompleta.substring(chiaveCompleta.indexOf('|') + 1, chiaveCompleta.length())
                lista.add(chiaveRidotta + tag + chiaveCompleta)
            } // fine del ciclo each

            lista.sort()

            if (lista) {
                listaChiaviOut = new ArrayList<String>()
                lista?.each {
                    chiaveDoppia = it
                    chiaveCompleta = chiaveDoppia.substring(chiaveDoppia.indexOf(tag) + tag.length(), chiaveDoppia.length())
                    listaChiaviOut.add(chiaveCompleta)
                } // fine del ciclo each
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return listaChiaviOut
    }// fine della closure

    public static ArrayList<String> getListaDidascalie(ArrayList<BioGrails> listaGrails) {
        ArrayList<String> listaDidascalie
        BioGrails bio
        String didascalia

        listaDidascalie = new ArrayList<String>()
        listaGrails?.each {
            bio = (BioGrails) it
            try { // prova ad eseguire il codice
                if (bio.didascaliaListe) {
                    didascalia = bio.didascaliaListe
                } else {
                    didascalia = creaDidascaliaAlVolo(bio)
                }// fine del blocco if-else
            } catch (Exception unErrore) { // intercetta l'errore
                didascalia = creaDidascaliaAlVolo(bio)
            }// fine del blocco try-catch
            listaDidascalie.add(didascalia)
        } // fine del ciclo each

        return listaDidascalie
    }// fine del metodo

    // se manca la didascalia, la crea al volo
    public static String creaDidascaliaAlVolo(BioGrails bio) {
        String didascaliaTxt = ''
        long grailsId
        DidascaliaBio didascaliaObj

        if (bio) {
            grailsId = bio.id
            didascaliaObj = new DidascaliaBio(grailsId)
            didascaliaObj.setInizializza()
            didascaliaTxt = didascaliaObj.getTestoEstesaSimboli()
        }// fine del blocco if

        return didascaliaTxt
    }// fine del metodo

    protected void setListaWrapper(ArrayList listaWrapper) {
        this.listaWrapper = listaWrapper
    }


    public ArrayList getListaWrapper() {
        return listaWrapper
    }


    protected void setCampoParagrafo(String campoParagrafo) {
        this.campoParagrafo = campoParagrafo
    }


    protected String getCampoParagrafo() {
        return campoParagrafo
    }


    protected void setCampiParagrafi(ArrayList campiParagrafi) {
        this.campiParagrafi = campiParagrafi
    }


    protected ArrayList getCampiParagrafi() {
        return campiParagrafi
    }

    String getTesto() {
        return testo
    }

    void setTesto(String testo) {
        this.testo = testo
    }

    String getTitoloPagina() {
        return titoloPagina
    }

    void setTitoloPagina(String titoloPagina) {
        this.titoloPagina = titoloPagina
    }

    String getCategoria() {
        return categoria
    }

    void setCategoria(String categoria) {
        this.categoria = categoria
    }

    String getPlurale() {
        return plurale
    }

    void setPlurale(String plurale) {
        this.plurale = plurale
    }

    private static enum TipoMappa {
        attivita, nazionalita, lettera, localita
    } // fine della Enumeration

}// fine della classe
