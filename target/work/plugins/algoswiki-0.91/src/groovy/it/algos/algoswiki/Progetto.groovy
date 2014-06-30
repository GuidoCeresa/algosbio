package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:27
 */
public enum Progetto {

    wikipedia('wikipedia'),
    meta('meta.wikimedia'),
    dizionario('wiktionary'),
    books('wikibooks'),
    quote('wikiquote'),
    source('wikisource'),
    commons('commons.wikimedia'),
    notizie('wikinews'),
    wikiversita('wikiversity'),
    mediawiki('mediawiki'),
    species('species.wikimedia')

    String tag


    Progetto(String tag) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag)
    }


    private void setTag(String tag) {
        this.tag = tag
    }


    public String getTag() {
        return tag
    }


    public String toString() {
        return getTag()
    }
} // fine della Enumeration
