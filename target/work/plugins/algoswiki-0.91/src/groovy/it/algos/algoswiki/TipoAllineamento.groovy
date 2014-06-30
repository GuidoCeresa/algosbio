package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 2-2-14
 * Time: 19:59
 */
public enum TipoAllineamento {

    left('left', '', ''),
    right('right', '', ''),
    randomBaseSin('left', '', '"right"'),
    randomBaseDex('right', '"left"', ''),
    secondaSinistra('left', '"left"', 'right')

    private static String BASE_TITOLO = 'text-align:'
    private static String BASE_CELLA = '|align='
    private static String TAG_FINALE = ';'
    String titolo
    String testo
    String numero


    TipoAllineamento(String titolo, String testo, String numero) {
        /* regola le variabili di istanza coi parametri */
        this.setTitolo(titolo)
        this.setTesto(testo)
        this.setNumero(numero)
    }


    private void setTitolo(String titolo) {
        this.titolo = titolo
    }


    public String getTitolo() {
        return BASE_TITOLO + titolo + TAG_FINALE
    }


    private void setTesto(String testo) {
        this.testo = testo
    }


    public String getTesto() {
        String tag = ''
        if (testo) {
            tag = BASE_CELLA + testo + TAG_FINALE
        }// fine del blocco if
        return tag
    }


    private void setNumero(String numero) {
        this.numero = numero
    }


    public String getNumero() {
        String tag = ''
        if (numero) {
            tag = BASE_CELLA + numero + TAG_FINALE
        }// fine del blocco if
        return tag
    }

} // fine della Enumeration
