package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
class ListaNazionalita extends ListaBio {

    private static String TAG_PROGETTO = 'Progetto:Biografie/Nazionalità/'
    private static String TAG_PARAGRAFO = 'Progetto:Biografie/Attività/'

    public ListaNazionalita() {
        super((Object) null)
    }// fine del costruttore

    public ListaNazionalita(Nazionalita nazionalita) {
        super(nazionalita)
    }// fine del costruttore

    public ListaNazionalita(Nazionalita nazionalita, BioService bioService) {
        super(nazionalita, bioService)
    }// fine del costruttore

    public ListaNazionalita(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore

    /**
     * Costruisce un oggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraOggetto(String soggetto) {
        Nazionalita nazionalita = Nazionalita.findByPlurale(soggetto)

        if (nazionalita) {
            oggetto = nazionalita
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce un soggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraSoggetto(Object oggetto) {
        Nazionalita nazionalita

        if (oggetto && oggetto instanceof Nazionalita) {
            nazionalita = (Nazionalita) oggetto
            soggetto = nazionalita.plurale
            soggettoMadre = soggetto
        }// fine del blocco if

    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        super.elaboraParametri()
        usaInclude = false
        tagTemplateBio = Pref.getStr(LibBio.NOME_TEMPLATE_AVVISO_LISTE_NAZ_ATT, 'ListaBio')
        usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_NAZ, true)
        usaAttivitaMultiple = Pref.getBool(LibBio.USA_ATTIVITA_MULTIPLE, true)
        maxVociParagrafo = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_NAZIONALITA, 100)
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected void elaboraTitolo() {
        String titolo = getPlurale()

        if (titolo) {
            titolo = LibTesto.primaMaiuscola(titolo)
            if (!titoloPagina) {
                titoloPagina = TAG_PROGETTO + titolo
            }// fine del blocco if
        }// fine del blocco if

    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave = attivitaPluralePerGenere(bio)

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo per la seconda attività
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafoSecondaAttivita(BioGrails bio) {
        return attivitaSecondaPluralePerGenere(bio)
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo per la terza attività
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafoTerzaAttivita(BioGrails bio) {
        return attivitaTerzaPluralePerGenere(bio)
    }// fine del metodo

    /**
     * Creazione della sottopagina
     * Sovrascritto
     */
    @Override
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaNazionalitaAttivita(elaboraSoggettoSpecifico(chiaveParagrafo), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.elaboraPagina()
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo) {
        return TAG_PROGETTO + elaboraSoggettoSpecifico(chiaveParagrafo)
    }// fine del metodo

    /**
     * Recupera il plurale
     */
    private String getPlurale() {
        String plurale = ''

        if (oggetto && oggetto instanceof Nazionalita) {
            plurale = oggetto.plurale
        }// fine del blocco if

        if (!plurale) {
            plurale = soggetto
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Recupera il plurale
     */
    private String getPluraleMaiuscolo() {
        return LibTesto.primaMaiuscola(getPlurale())
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected elaboraFooter() {
        String testo = ''
        String nazionalita = soggettoMadre
        nazionalita = LibTesto.primaMaiuscola(nazionalita)
        String tagCategoria = "[[Categoria:Bio nazionalità|${nazionalita}]]"

        if (numDidascalie > listaBiografie.size()) {
            testo += super.getNote('Alcune persone sono citate più volte perché hanno diverse attività')
            if (usaSuddivisioneUomoDonna && esistonoUominiDonne) {
                testo += aCapo
            }// fine del blocco if
            testo += aCapo
        }// fine del blocco if

        testo += super.getVociCorrelate()
        testo += aCapo
        testo += "*[[:Categoria:${nazionalita}]]"
        testo += aCapo
        testo += '*[[Progetto:Biografie/Nazionalità]]'
        testo += aCapo
        testo += aCapo
        testo += '{{Portale|biografie}}'
        testo += aCapo
        testo += aCapo
        if (usaInclude) {
            testo += LibBio.setNoInclude(tagCategoria)
        } else {
            testo += tagCategoria
        }// fine del blocco if-else

        return finale(testo)
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con eventuali link
     * Sovrascritto
     */
    @Override
    protected String elaboraTitoloParagrafo(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo

        if (Pref.getBool(LibBio.USA_TITOLO_PARAGRAFO_NAZ_ATT_LINK_PROGETTO, true)) {
            titoloParagrafo = elaboraTitoloParagrafoNazionalita(chiaveParagrafo, listaVoci)
        } else {
            titoloParagrafo = super.elaboraTitoloParagrafo(chiaveParagrafo, listaVoci)
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con link
     */
    private String elaboraTitoloParagrafoNazionalita(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo
        String pipe = '|'
        String singolare
        String plurale = ''
        Attivita attivita
        BioGrails bio = listaVoci.get(0)

        if (bio) {
            attivita = bio.attivitaLink
            if (!attivita) {
                singolare = bio.attivita
                attivita = Attivita.findBySingolare(singolare)
            }// fine del blocco if
            if (attivita) {
                plurale = attivita.plurale
            }// fine del blocco if
        }// fine del blocco if

        plurale = LibTesto.primaMaiuscola(plurale)
        if (chiaveParagrafo.equals(tagParagrafoNullo)) {
            titoloParagrafo = chiaveParagrafo
        } else {
            titoloParagrafo = TAG_PARAGRAFO + plurale + pipe + chiaveParagrafo
            titoloParagrafo = LibWiki.setQuadre(titoloParagrafo)
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * Recupera la Nazionalita
     */
    protected Nazionalita getNazionalita() {
        Nazionalita nazionalita = null

        if (oggetto && oggetto instanceof Nazionalita) {
            nazionalita = (Nazionalita) oggetto
        }// fine del blocco if

        return nazionalita
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        String nazionalitaPlurale = getListaNazionalitaWhere()
        String query

        if (nazionalita) {
            query = "from BioGrails where ($nazionalitaPlurale)"
            listaBiografie = BioGrails.executeQuery(query)
        }// fine del blocco if
    }// fine del metodo

    /**
     * Crea una lista di nazionalità per la WHERE condition
     */
    private String getListaNazionalitaWhere() {
        String listaNazionalitaWhere = ''
        String tag = ' nazionalita_link_id='
        String tagOr = ' OR'
        ArrayList<Long> listaSingolariID = getListaID()

        listaSingolariID?.each {
            listaNazionalitaWhere += tag + it + tagOr
        } // fine del ciclo each
        listaNazionalitaWhere = LibTesto.levaCoda(listaNazionalitaWhere, tagOr)

        return listaNazionalitaWhere
    } // fine del metodo

    /**
     * Crea una lista di id di attività utilizzate
     * Per ogni plurale, ci possono essere diversi 'singolari' richiamati dalle voci di BioGrails
     */
    private ArrayList<Long> getListaID() {
        ArrayList<Long> listaSingolariID = null
        String nazionalitaPlurale = this.getPlurale()
        String query
        String tag = "'"

        if (nazionalitaPlurale) {
            if (!nazionalitaPlurale.contains(tag)) {
                query = "select id from Nazionalita where plurale='$nazionalitaPlurale'"
                listaSingolariID = (ArrayList<Long>) Nazionalita.executeQuery(query)
            }// fine del blocco if
        }// fine del blocco if

        return listaSingolariID
    } // fine del metodo

    /**
     * Elabora e crea la lista della nazionalità indicata e la uploada sul server wiki
     */
    public static boolean uploadNazionalita(Nazionalita nazionalita, BioService bioService) {
        boolean registrata = false
        ListaNazionalita listaNazionalita

        if (nazionalita) {
            listaNazionalita = new ListaNazionalita(nazionalita, bioService)
            if (listaNazionalita.registrata || listaNazionalita.listaBiografie.size() == 0) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe


