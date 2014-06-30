package it.algos.algoswiki

import org.codehaus.groovy.grails.web.json.JSONArray
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 6-11-12
 * Time: 08:07
 */
// Query per leggere tutte le pagine di una categoria
// Legge solamente
// Non necessita di Login
// List of pages that belong to a given category, ordered by page sort title.
// Prefix CM
// Mantiene una lista di title
class QueryCatTitle extends QueryCat {

    private static String QUERY_TAG_TITLE = '&cmprop=title'

    // Pagine
    protected ArrayList<String> listaTitles

    public QueryCatTitle(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    public QueryCatTitle(Login login, String titolo) {
        super(login, titolo)
    }// fine del metodo costruttore

    public QueryCatTitle(int pageid) {
        super(pageid)
    }// fine del metodo costruttore

    /**
     * Restituisce il tag della query
     */
    protected String getTag() {
        return QUERY_TAG_TITLE + super.getTag()
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        ArrayList<String> listaTitles = getListaTitles()
        JSONArray mappaJSON = this.estraeMappa()
        String title

        if (mappaJSON) {
            if (listaTitles == null) {
                listaTitles = new ArrayList<Integer>()
            }// fine del blocco if

            mappaJSON.each {
                title = it.get(Const.TAG_TITlE)
                listaTitles.add(title)
            } // fine del ciclo each
        }// fine del blocco if

        if (listaTitles) {
            this.setListaTitles(listaTitles)
        }// fine del blocco if

        this.fixNextContinue()
    } // fine del metodo

    public ArrayList<String> getListaTitles() {
        return listaTitles
    }

    private void setListaTitles(ArrayList<String> listaTitles) {
        this.listaTitles = listaTitles
    }

} // fine della classe
