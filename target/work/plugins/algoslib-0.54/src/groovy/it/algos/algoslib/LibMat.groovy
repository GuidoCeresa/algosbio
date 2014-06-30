package it.algos.algoslib

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 17-7-13
 * Time: 07:00
 */
class LibMat {
    /**
     * Controlla se il numero è pari
     *
     * @param num da controllare
     * @return vero se è pari
     */
    public static isPari(def num) {
        // variabili e costanti locali di lavoro
        boolean pari = false
        int meta
        int doppio

        if (num) {
            meta = num / 2
            doppio = meta * 2
            if (doppio == num) {
                pari = true
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return pari
    } // fine del metodo

    /**
     * Controlla se il numero è dispari
     *
     * @param num da controllare
     * @return vero se è dispari
     */
    public static isDispari(def num) {
        // variabili e costanti locali di lavoro
        boolean dispari = false
        boolean pari

        if (num) {
            pari = isPari(num)
            dispari = !pari
        }// fine del blocco if

        // valore di ritorno
        return dispari
    } // fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     * Considera SOLO i numeri positivi
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoPositivo(int primo, int secondo) {
        return minimoPositivo([primo, secondo])
    }// fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     * Considera SOLO i numeri positivi
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoPositivo(int primo, int secondo, int terzo) {
        return minimoPositivo([primo, secondo, terzo])
    }// fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     * Considera SOLO i numeri positivi
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoPositivo(ArrayList numeri) {
        return minimoPositivoNegativo(numeri, true)
    }// fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     * Considera ANCHE i numeri negativi
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoNegativo(int primo, int secondo) {
        return minimoNegativo([primo, secondo])
    }// fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     * Considera ANCHE i numeri negativi
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoNegativo(int primo, int secondo, int terzo) {
        return minimoNegativo([primo, secondo, terzo])
    }// fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     * Considera ANCHE i numeri negativi
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoNegativo(ArrayList numeri) {
        return minimoPositivoNegativo(numeri, false)
    }// fine del metodo

    /**
     * Restituisce il minore dei numeri passati.
     * I numeri possono essere passati come lista o come coppia o tripletta di numeri
     * I numeri possono essere int, double, float e long
     *
     * @param numeri lista dei numeri da confrontare
     * @return numero minore
     */
    public static minimoPositivoNegativo(ArrayList numeri, boolean soloPositivi) {
        /* variabili e costanti locali di lavoro */
        int minore = 99999
        boolean continua
        int dim

        continua = (numeri != null && numeri.size() > 0)
        if (continua) {
            dim = numeri.size()
            for (int k = 0; k < dim; k++) {
                if (soloPositivi) {
                    if (numeri[k] >= 0) {
                        minore = Math.min(minore, (int) numeri[k])
                    }// fine del blocco if
                } else {
                    minore = Math.min(minore, (int) numeri[k])
                }// fine del blocco if-else
            } // fine del ciclo for
        }// fine del blocco if

        /* valore di ritorno */
        return minore
    }// fine del metodo
}// fine della classe statica
