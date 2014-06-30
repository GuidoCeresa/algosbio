package it.algos.algos;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 25-5-13
 * Time: 16:49
 */
public enum TipoImport {

    // Cancella la tavola
    // Aggiunge i records
    ricostruisce(),

    // Aggiorna la tavola
    // Aggiunge i records che mancano (occorre individuare un campo chiave)
    // Modifica i record già esistenti
    aggiorna(),

    // Aggiunge i records che mancano (occorre individuare un campo chiave)
    // Non modifica i records esistenti
    aggiunge(),

    // Modifica i record già esistenti (occorre individuare un campo chiave)
    // Non aggiunge nuovi records
    modifica()

} // fine della enumeration
