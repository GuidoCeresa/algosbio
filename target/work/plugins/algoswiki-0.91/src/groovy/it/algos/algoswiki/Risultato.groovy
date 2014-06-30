package it.algos.algoswiki
import javax.swing.*;

/**
 * Codifica dei possibili risultati dell'elaborazione.
 * </p>
 * Questa classe: <ul>
 * <li> </li>
 * <li> </li>
 * </ul>
 *
 * @author Guido Andrea Ceresa, Alessandro Valbonesi
 * @author gac
 * @version 1.0    / 1-giu-2006 ore 17.27.40
 */
public enum Risultato {

    elaborata("Elaborata.", Colore.verde),
    letta("Letta la pagina.", Colore.verde),
    pagineMultiple("Lette le pagine.", Colore.verde),
    ultimeRevisioni("Lette le revisioni.", Colore.verde),
    badtoken("Invalid token.", Colore.rosso),
    noLogin("Manca il collegamento.", Colore.giallo),
    noTitolo("Manca il titolo.", Colore.giallo),
    erroreGenerico("Errore generico.", Colore.giallo),
    nonElaborata("Non elaborata.", Colore.giallo),
    nonModificata("Non modificata.", Colore.verde),
    nuovaVoce("Nuova voce.", Colore.verde),
    allineata("Pagina già allineata.", Colore.verde),
    bio("Allineata voce biobase.", Colore.verde),
    bioGia("Voce biobase già allineata.", Colore.verde),
    giorno("Allineato giorno.", Colore.verde),
    giornoGia("Giorno già allineato.", Colore.verde),
    attivita("Allineata pagina attività.", Colore.verde),
    attivitaGia("Pagina attività già allineata.", Colore.verde),
    nazionalita("Allineata pagina nazionalità.", Colore.verde),
    nazionalitaGia("Pagina nazionalità già allineata.", Colore.verde),
    anno("Allineato anno.", Colore.verde),
    annoGia("Anno gia allineato.", Colore.verde),
    citta("Città di nascita allineata.", Colore.verde),
    esistente("Pagina esistente.", Colore.verde),
    nonTrovata("Pagina inesistente.", Colore.rosso),
    registrata("Pagina registrata.", Colore.verde),
    creata("Creata nuova pagina.", Colore.verde),
    spostata("Pagina spostata.", Colore.verde),
    vuota("Pagina non esistente.", Colore.giallo),
    cancellata("Pagina esistente, ma precedentemente cancellata.", Colore.rosso),
    mancaTabBio("Manca la tabella biografica.", Colore.rosso),
    mancaTabella("Tabella mancante.", Colore.rosso),
    mancaParagrafo("Mancano dei paragrafi.", Colore.rosso),
    mancaParola("Parola non esistente.", Colore.giallo),
    mancaFrase("Frase non esistente.", Colore.giallo),
    wikilinkErrato("Wikilink errato.", Colore.rosso),
    catNonEsistente("Categoria non esistente.", Colore.giallo),
    catErrata("Categoria errata.", Colore.verde),
    redirect("La voce è un redirect.", Colore.rosso),
    catEsistente("Categoria già esistente.", Colore.giallo),
    catDoppia("La categoria era doppia.", Colore.verde),
    comuniGiusti("Nomi comuni giusti.", Colore.verde),
    comuniErrati("Nomi comuni sbagliati.", Colore.rosso),
    tabellaErrata("Tabella non conforme.", Colore.rosso),
    parametriMancanti("Nella tabella mancano dei parametri.", Colore.rosso),
    parametriAbbondanti("Nella tabella ci sono troppi parametri.", Colore.rosso),
    parametriErrati("Nella tabella ci sono parametri in più ed in meno.", Colore.rosso),
    connessione("Errore di connessione.", Colore.rosso),
    timeOut("Tempo scaduto.", Colore.rosso),
    utf8("Caratteri utf8 errati", Colore.rosso),
    nonRegistrata("Pagina non registrata (probabilmente cancellata).", Colore.rosso),
    esistenteNuova("Pagina già esistente", Colore.giallo),
    modificaRegistrata("Registrata modifica alla voce", Colore.verde),
    modificaInutile("La voce aveva già il testo richiesto", Colore.giallo)

    /**
     * voce da utilizzare
     */
    private String descrizione;

    /**
     * colore di rappresentazione del messaggio
     */
    private Colore colore;


    /**
     * Costruttore completo con parametri.
     *
     * @param descrizione utilizzato nei messaggi
     * @param colore      utilizzato nei messaggi
     */
    Risultato(String descrizione, Colore colore) {
        /* regola le variabili di istanza coi parametri */
        this.setDescrizione(descrizione);
        this.setColore(colore);
    }


    public String getDescrizione() {
        return descrizione;
    }


    private void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Colore getColore() {
        return colore;
    }


    private void setColore(Colore colore) {
        this.colore = colore;
    }


    public String getTagColore() {
        return getColore().getTag();
    }


    /**
     * descrizione colorata per pagina html
     */
    public String getDesCol() {
        /* variabili e costanti locali di lavoro */
        String html = "";
        String desc;
        String tagColore;
        String tagIni = "<font color=\"";
        String tagMed = "\">";
        String tagEnd = "</font>";

        tagColore = this.getTagColore();

        desc = this.getDescrizione();
        html = tagIni + tagColore + tagMed + desc + tagEnd;

        /* valore di ritorno */
        return html;
    }


    public JLabel getLabel() {
        /* variabili e costanti locali di lavoro */
        JLabel label = null;
        String desc;

        desc = this.getDescrizione();
        label = new JLabel(desc);
        label.setForeground(this.getColore().getColore());

        /* valore di ritorno */
        return label;
    }
} // fine della Enumeration
