package it.algos.algos;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 25-5-13
 * Time: 16:48
 */
public enum TipoFile {

    tab("\t", "Tab.txt", true, ""),
    csvit(";", "CsvIt.txt", false, ""),
    csven(",", "CsvEn.txt", false, ""),
    silk(",", "Silk.txt", false, ""),
    xml("", "", false, "XML"),
    json("", "", false, "JSON");

    // separatore di campo
    private String separatore;

    // suffisso del file
    private String suffisso;

    // titoli nbella prima riga
    private boolean usaPrimaRiga;

    // formato dei dati sul webservice
    private String tag;


    /**
     * Costruttore completo con parametri.
     *
     * @param separatore utilizzato per splittare la riga
     * @param suffisso   utilizzato per costruire il path del file
     * @param primaRiga  con i titoli descrittivi
     * @param tag        per il formato dei dati sul webservice
     */
    TipoFile(String separatore, String suffisso, boolean primaRiga, String tag) {
        try { // prova ad eseguire il codice
            /* regola le variabili di istanza coi parametri */
            this.setSeparatore(separatore);
            this.setSuffisso(suffisso);
            this.setTag(tag);
            this.setUsaPrimaRiga(primaRiga);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
    }


    public String getSeparatore() {
        return separatore;
    }


    private void setSeparatore(String separatore) {
        this.separatore = separatore;
    }


    public String getSuffisso() {
        return suffisso;
    }


    private void setSuffisso(String suffisso) {
        this.suffisso = suffisso;
    }


    public boolean isUsaPrimaRiga() {
        return usaPrimaRiga;
    }


    public void setUsaPrimaRiga(boolean usaPrimaRiga) {
        this.usaPrimaRiga = usaPrimaRiga;
    }


    public String getTag() {
        return tag;
    }


    private void setTag(String tag) {
        this.tag = tag;
    }

} // fine della enumeration
