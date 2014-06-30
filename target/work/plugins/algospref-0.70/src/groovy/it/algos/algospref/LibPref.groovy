package it.algos.algospref

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 23-9-12
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
class LibPref {

    public static int getNewOrdine() {
        def valore
        int ordine = 1
        def risultato = Pref.executeQuery('select max(ordine) from Pref')
        if (risultato && risultato instanceof ArrayList && risultato.size() > 0) {
            valore = risultato.get(0)
            if (valore) {
                ordine = valore + 1
            }// fine del blocco if
        }// fine del blocco if

        return ordine
    }// fine del metodo

}// fine della classe libreria
