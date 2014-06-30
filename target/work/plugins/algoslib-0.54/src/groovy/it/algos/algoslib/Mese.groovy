package it.algos.algoslib

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 14-6-13
 * Time: 14:36
 */
public enum Mese {
    gennaio('gen', 'gennaio', 31),
    febbraio('feb', 'febbraio', 28),
    marzo('mar', 'marzo', 31),
    aprile('apr', 'aprile', 30),
    maggio('mag', 'maggio', 31),
    giugno('giu', 'giugno', 30),
    luglio('lug', 'luglio', 31),
    agosto('ago', 'agosto', 31),
    settembre('set', 'settembre', 30),
    ottobre('ott', 'ottobre', 31),
    novembre('nov', 'novembre', 30),
    dicembre('dic', 'dicembre', 31)

    String breve
    String lungo
    int giorni

    Mese(String breve, String lungo, giorni) {
        this.breve = breve
        this.lungo = lungo
        this.giorni = giorni
    }// fine del metodo costruttore

    // l'anno parte da gennaio che è il numero 1
    public static int getGiorni(int ord) {
        int giorni  =0
        Mese mese = getMese(ord)

        if (mese) {
            giorni = mese.giorni
        }// fine del blocco if

        return giorni
    }// fine del metodo statico

    private static Mese getMese(int ord) {
        Mese mese = null
        ord = ord - 1

        values()?.each {
            if (it.ordinal() == ord) {
                mese = it
            }// fine del blocco if
        }// fine del ciclo each

        return mese
    }// fine del metodo statico

    // l'anno parte da gennaio che è il numero 1
    private static String getMese(int ord, boolean flagBreve) {
        String nome = ''
        Mese mese = getMese(ord)

        if (mese) {
            if (flagBreve) {
                nome = mese.breve
            } else {
                nome = mese.lungo
            }// fine del blocco if-else
        }// fine del blocco if

        return nome
    }// fine del metodo statico


    public static String getShort(int ord) {
        return getMese(ord, true)
    }// fine del metodo statico


    public static String getLong(int ord) {
        return getMese(ord, false)
    }// fine del metodo statico


    public static String getAllShortString() {
        String stringa = ''
        String sep = ', '

        values()?.each {
            stringa += it.getBreve()
            stringa += sep
        }// fine del ciclo each
        stringa = LibTesto.levaCoda(stringa, sep)

        return stringa
    }// fine del metodo statico


    public static String getAllLongString() {
        String stringa = ''
        String sep = ', '

        values()?.each {
            stringa += it.getLungo()
            stringa += sep
        }// fine del ciclo each
        stringa = LibTesto.levaCoda(stringa, sep)

        return stringa
    }// fine del metodo statico


    public static ArrayList getAllShortList() {
        ArrayList lista = new ArrayList()

        values()?.each {
            lista.add(it.getBreve())
        }// fine del ciclo each

        return lista
    }// fine del metodo statico


    public static ArrayList getAllLongList() {
        ArrayList lista = new ArrayList()

        values()?.each {
            lista.add(it.getLungo())
        }// fine del ciclo each

        return lista
    }// fine del metodo statico


    public int getOrd() {
        return this.ordinal() + 1
    }// fine del metodo

}// fine della classe Enumeration
