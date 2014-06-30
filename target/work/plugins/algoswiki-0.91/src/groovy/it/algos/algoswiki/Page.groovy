package it.algos.algoswiki

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 7-11-12
 * Time: 08:38
 */
// Wrapper coi dati sempre presenti per una pagina
class Page {

    // titolo della pagina
    // incluso il prefisso per i namespaces # da zero
    private String title

    // codice pagina wiki
    private int pageid

    // namespace wiki
    private int ns

    // titolo della pagina
    // escluso il prefisso
    private String titolo

    private TypeCat type

    /**
     * Costruttore completo con parametri.
     *
     * @param title incluso il prefisso
     * @param pageid wiki
     * @param ns namespace
     */
    Page(String title, int pageid, int ns, TypeCat type) {
        this.setTitle(title)
        this.setPageid(pageid)
        this.setNs(ns)
        this.setType(type)

        this.regolaTitolo()
    } // fine del costruttore

    /**
     * Costruttore completo con parametri.
     *
     * @param title incluso il prefisso
     * @param pageid wiki
     * @param ns namespace
     */
    Page(JSONObject mappa) {
        if (mappa.containsKey(Const.TAG_TITlE)) {
            this.setTitle((String) mappa.get(Const.TAG_TITlE))
        }// fine del blocco if
        if (mappa.containsKey(Const.TAG_PAGE_ID)) {
            this.setPageid((int) mappa.get(Const.TAG_PAGE_ID))
        }// fine del blocco if
        if (mappa.containsKey(Const.TAG_NS)) {
            this.setNs((int) mappa.get(Const.TAG_NS))
        }// fine del blocco if
        if (mappa.containsKey(Const.TAG_TYPE)) {
            this.setType((TypeCat) mappa.get(Const.TAG_TYPE))
        }// fine del blocco if

        this.regolaTitolo()
    } // fine del costruttore

    private void regolaTitolo() {
        String title = this.getTitle()
        String titolo = ''
        String tag = ':'

        if (title) {
            titolo = title
            if (this.ns > 0) {
                if (title.contains(tag)) {
                    titolo = title.substring(title.indexOf(tag) + 1)
                }// fine del blocco if
            }// fine del blocco if
            this.setTitolo(titolo)
        }// fine del blocco if
    } // fine del metodo

    String getTitle() {
        return title
    }

    void setTitle(String title) {
        this.title = title
    }

    int getPageid() {
        return pageid
    }

    void setPageid(int pageid) {
        this.pageid = pageid
    }

    int getNs() {
        return ns
    }

    void setNs(int ns) {
        this.ns = ns
    }

    String getTitolo() {
        return titolo
    }

    void setTitolo(String titolo) {
        this.titolo = titolo
    }

    TypeCat getType() {
        return type
    }

    void setType(TypeCat type) {
        this.type = type
    }
} // fine della classe
