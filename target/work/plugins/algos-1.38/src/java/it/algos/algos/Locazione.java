package it.algos.algos;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 25-5-13
 * Time: 16:48
 */
public enum Locazione {

    // I dati vengono recuperati da una directory locale
    // il path è relativo ad una posizione interna al progetto Grails-Idea
    localeGrails(),

    // I dati vengono recuperati da una directory locale
    // il path è assoluto e riferito al disco rigido
    localeRete(),

    // I dati vengono recuperati da un server remoto
    serverRemoto()

} // fine della enumeration

