package it.algos.algos;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 25-5-13
 * Time: 16:48
 */
public enum TipoBoot {

    // Non carica mai i dati iniziali.
    // Indipendentemente dai records presenti nella tavola
    mai(),

    // Carica sempre i dati iniziali.
    // Indipendentemente dai records presenti nella tavola
    // Comportamento sul singolo record, stabilito da TipoImport
    sempre(),

    // Carica i dati iniziali solo se la tavola è vuota
    soloVuoto()

} // fine della enumeration

