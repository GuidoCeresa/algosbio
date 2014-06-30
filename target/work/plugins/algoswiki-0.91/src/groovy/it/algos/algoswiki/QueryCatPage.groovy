package it.algos.algoswiki

import org.codehaus.groovy.grails.web.json.JSONObject
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
// Mantiene una lista di Pages
// Tipicamente usato per categorie non enormi (qualche migliaio di voci al massimo)
class QueryCatPage extends QueryCat {

    private static String QUERY_TAG_PAGE = '&cmprop=ids|title|sortkey|type'

    // Pagine
    private ArrayList<Page> listaVoci

    // Categorie
    private ArrayList<Page> listaCat

    public QueryCatPage(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    public QueryCatPage(int pageid) {
        super(pageid)
    }// fine del metodo costruttore

    /**
     * Restituisce il tag della query
     */
    protected String getTag() {
        return QUERY_TAG_PAGE + super.getTag()
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        ArrayList<Page> listaCat = getListaCat()
        ArrayList<Page> listaVoci = getListaVoci()
        JSONArray mappaJSON = this.estraeMappa()
        Page page

        if (mappaJSON) {
            if (listaCat == null) {
                listaCat = new ArrayList<Page>()
            }// fine del blocco if
            if (listaVoci == null) {
                listaVoci = new ArrayList<Page>()
            }// fine del blocco if

            mappaJSON.each {
                page = new Page((JSONObject) it)
                switch (page.type) {
                    case TypeCat.subcat:
                        listaCat.add(page)
                        break
                    case TypeCat.page:
                        listaVoci.add(page)
                        break
                    case TypeCat.file:
                        break
                    default: // caso non definito
                        break
                } // fine del blocco switch
            } // fine del ciclo each
        }// fine del blocco if

        if (listaCat) {
            this.setListaCat(listaCat)
        }// fine del blocco if
        if (listaVoci) {
            this.setListaVoci(listaVoci)
        }// fine del blocco if

        this.fixNextContinue()
    } // fine del metodo


    ArrayList<Page> getListaVoci() {
        return listaVoci
    }

    void setListaVoci(ArrayList<Page> listaVoci) {
        this.listaVoci = listaVoci
    }

    ArrayList<Page> getListaCat() {
        return listaCat
    }

    void setListaCat(ArrayList<Page> listaCat) {
        this.listaCat = listaCat
    }
} // fine della classe
