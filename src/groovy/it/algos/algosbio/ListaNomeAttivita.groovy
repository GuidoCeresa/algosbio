package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoswiki.Login

/**
 * Created by gac on 18/10/14.
 */
class ListaNomeAttivita extends ListaNome {


    public ListaNomeAttivita(Antroponimo antroponimo) {
        super(antroponimo)
    }// fine del costruttore


    public ListaNomeAttivita(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = false
        tagTemplateBio = 'StatBio'
        usaSuddivisioneParagrafi = true
        usaTitoloParagrafoConLink = false
        usaDoppiaColonna = false
        usaSottopagine = false
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = '...'
    }// fine del metodo

//    /**
//     * Costruisce il titolo della pagina
//     */
//    @Override
//    protected String getTitolo() {
//        return 'Persone di nome ' + getNome()
//    }// fine del metodo


    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave = bio.nome

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

//    /**
//     * Piede della pagina
//     * Sovrascritto
//     */
//    @Override
//    protected elaboraFooter() {
//        String testo = ''
//        String nome = getNome()
//
//        testo += "<noinclude>"
//        testo += "[[Categoria:Liste di persone per nome|${nome}]]"
//        testo += "</noinclude>"
//
//        return testo
//    }// fine del metodo

//    /**
//     * Recupera il singolo Antroponimo
//     */
//    private Antroponimo getAntroponimo() {
//        Antroponimo antroponimo = null
//
//        if (oggetto && oggetto instanceof Antroponimo) {
//            antroponimo = (Antroponimo) oggetto
//        }// fine del blocco if
//
//        return antroponimo
//    }// fine del metodo

//    /**
//     * Recupera il singolo nome
//     */
//    private String getNome() {
//        String nome = ''
//        Antroponimo antroponimo = getAntroponimo()
//
//        if (antroponimo) {
//            nome = antroponimo.nome
//        }// fine del blocco if
//
//        return nome
//    }// fine del metodo



//    /**
//     * Costruisce una lista di biografie
//     */
//    @Override
//    protected elaboraListaBiografie() {
//        Antroponimo antroponimo = getAntroponimo()
//
//        if (antroponimo) {
//            listaBiografie = BioGrails.findAllByNomeLink(antroponimo, [sort: 'forzaOrdinamento'])
//        }// fine del blocco if
//        def stop
//    }// fine del metodo

}// fine della classe

