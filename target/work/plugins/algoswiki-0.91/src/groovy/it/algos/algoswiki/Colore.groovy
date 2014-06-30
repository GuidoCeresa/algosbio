package it.algos.algoswiki

import java.awt.Color
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 1-11-12
 * Time: 07:30
 */
public enum Colore {

    verde(Color.green, "green"),
    giallo(Color.yellow, "yellowred"),
    rosso(Color.red, "red");

    /**
     * colore
     */
    private Color colore;

    /**
     * wpTextbox1 html per pagina log
     */
    private String tag;

    /**
     * Costruttore completo con parametri.
     *
     * @param colore
     * @param tag html per pagina log
     */
    Colore(Color colore, String tag) {
        /* regola le variabili di istanza coi parametri */
        this.setColore(colore);
        this.setTag(tag);
    }


    public Color getColore() {
        return colore;
    }


    private void setColore(Color colore) {
        this.colore = colore;
    }


    private String getTag() {
        return tag;
    }


    private void setTag(String tag) {
        this.tag = tag;
    }
} // fine della Enumeration
