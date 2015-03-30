package it.algos.algosbio

import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
class ListaLuogo extends ListaBio {

    public ListaLuogo(Localita luogo, BioService bioService) {
        super(luogo, bioService)
    }// fine del costruttore

    public ListaLuogo(Localita luogo) {
        super(luogo)
    }// fine del costruttore


    public ListaLuogo(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    public ListaLuogo(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore


    @Override
    protected elaboraOggetto(String soggetto) {
        Localita luogo = Localita.findByNome(soggetto)

        if (luogo) {
            oggetto = luogo
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce un soggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraSoggetto(Object oggetto) {
        Localita luogo

        if (oggetto && oggetto instanceof Localita) {
            luogo = (Localita) oggetto
            soggetto = luogo.nome
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
            titoloPagina = 'Persone nate a ' + getNome()
        }// fine del blocco if
    }// fine del metodo


    /**
     * Recupera il tag specifico nati/morti
     */
    protected String getTagTitolo() {
        return ''
    }// fine del metodo


    /**
     * Recupera la singola Localita
     */
    protected Localita getLocalita() {
        Localita localita = null

        if (oggetto && oggetto instanceof Localita) {
            localita = (Localita) oggetto
        }// fine del blocco if

        return localita
    }// fine del metodo

    /**
     * Recupera il singolo nome di luogo
     */
    protected String getNome() {
        String nome
        Localita localita = getLocalita()

        if (localita) {
            nome = localita.nome
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
        Localita localita = getLocalita()

        if (localita) {
            listaBiografie = BioGrails.findAllByLuogoNatoLink(localita, [sort: 'forzaOrdinamento'])
        }// fine del blocco if

        super.elaboraListaBiografie()
    }// fine del metodo

    /**
     * Elabora e crea la lista del luogo indicato e la uploada sul server wiki
     */
    public static boolean uploadLocalita(Localita localita, BioService bioService) {
        boolean registrata = false
        ListaLuogo listaLuogo

        if (localita) {
            listaLuogo = new ListaLuogo(localita, bioService)
            if (listaLuogo.registrata || listaLuogo.listaBiografie?.size() == 0) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe