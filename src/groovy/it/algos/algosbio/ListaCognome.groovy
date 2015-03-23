package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
class ListaCognome extends ListaBio {


    public ListaCognome(Cognome cognome, BioService bioService) {
        super(cognome, bioService)
    }// fine del costruttore

    public ListaCognome(Cognome cognome) {
        super(cognome)
    }// fine del costruttore


    public ListaCognome(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    public ListaCognome(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Cognome cognome = Cognome.findByTesto(LibTesto.primaMaiuscola(soggetto))

        if (cognome) {
            oggetto = cognome
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce un soggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraSoggetto(Object oggetto) {
        Cognome cognome

        if (oggetto && oggetto instanceof Cognome) {
            cognome = (Cognome) oggetto
            soggetto = cognome.testo
            soggettoMadre = soggetto
        }// fine del blocco if

    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = true
        tagTemplateBio = Pref.getStr(LibBio.NOME_TEMPLATE_AVVISO_LISTE_NOMI_COGNOMI, 'StatBio')
        usaHeadIncipit = true
        usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA, true)
        usaSuddivisioneParagrafi = true
        usaTitoloParagrafoConLink = true
        usaDoppiaColonna = false
        usaSottopagine = true
        maxVociParagrafo = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_COGNOMI, 50)
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = 'Altre...'
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected void elaboraTitolo() {
        if (!titoloPagina) {
            titoloPagina = 'Persone di cognome ' + getNome()
        }// fine del blocco if
    }// fine del metodo

    /**
     * Voce principale a cui tornare
     * Sovrascritto
     */
    @Override
    protected String elaboraRitorno() {
        String testo = ''

        if (titoloPaginaMadre) {
            testo = "{{Torna a|" + titoloPaginaMadre + "}}"
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = 'Persone di cognome '
        Cognome cognome = getCognome()

        if (cognome) {
            titolo = cognome.testo
            titolo = tag + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
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
     * Creazione della sottopagina
     * Sovrascritto
     */
    @Override
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate, String tagSesso) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaCognomeAttivita(elaboraSoggettoSpecifico(chiaveParagrafo, tagSesso), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.numPersone = listaVociOrdinate.size()
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.elaboraPagina()
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo, String tagSesso) {
        return 'Persone di cognome ' + elaboraSoggettoSpecifico(chiaveParagrafo, tagSesso)
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected String elaboraFooter() {
        String testo = ''
        String cognome = getNome()

        testo += "<noinclude>"
        testo += "[[Categoria:Liste di persone per cognome|${cognome}]]"
        testo += "</noinclude>"

        return testo
    }// fine del metodo

    /**
     * Recupera il singolo cognome
     */
    protected Cognome getCognome() {
        Cognome cognome = null

        if (oggetto && oggetto instanceof Cognome) {
            cognome = (Cognome) oggetto
        }// fine del blocco if

        return cognome
    }// fine del metodo

    /**
     * Recupera il singolo cognome come nome/testo
     */
    protected String getNome() {
        String nome
        Cognome cognome = getCognome()

        if (cognome) {
            nome = cognome.testo
        } else {
            nome = soggetto
        }// fine del blocco if-else

        return nome
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Cognome cognome = getCognome()

        if (cognome) {
            listaBiografie = BioGrails.findAllByCognome(cognome.testo, [sort: 'forzaOrdinamento'])
        }// fine del blocco if

        super.elaboraListaBiografie()
    }// fine del metodo

    /**
     * Elabora e crea la lista del nome indicato e la uploada sul server wiki
     */
    public static boolean uploadCognome(Cognome cognome, BioService bioService) {
        boolean registrata = false
        ListaCognome listaCognome

        if (cognome) {
            listaCognome = new ListaCognome(cognome, bioService)
            if (listaCognome.registrata || listaCognome.listaBiografie.size() == 0) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe


