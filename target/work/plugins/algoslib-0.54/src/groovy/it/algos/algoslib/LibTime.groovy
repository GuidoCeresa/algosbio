package it.algos.algoslib

import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 12-8-13
 * Time: 07:34
 */
class LibTime {

    /**
     * Primo giorno del mese
     */
    public static String PRIMO = '1º'

    /**
     * numero di millisecondi in un giorno (86.400 secondi)
     */
    private static final long MSEC_PER_DAY = 86400000

    /**
     * Istanza del calendario del programma
     */
    private static GregorianCalendar calendario = null

    /**
     * Oggetto formattatore per le date.
     * <p/>
     * Converte data->testo e testo->data
     */
    private static SimpleDateFormat dateFormat = null

    /**
     * Oggetto formattatore per le date.
     * <p/>
     * Converte data->testo e testo->data interpreta l'anno a 2 cifre
     */
    private static SimpleDateFormat shortDateFormat = null

    /**
     * Data convenzionalmente interpretata come vuota
     */
    private static Date dataVuota = null

    /**
     * Oggetto formattatore per le date proventiente dalla wikipedia.
     */
    private static SimpleDateFormat wikiDateFormat = null

    /**
     * Metodo statico.
     * Invocato la prima volta che la classe statica viene richiamata nel programma <br>
     */
    static {
        /* variabili e costanti locali di lavoro */
        GregorianCalendar calendari
        long millisec
        Date data
        SimpleDateFormat df
        SimpleDateFormat sdf

        /* crea il calendario */
        calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0)

        /* crea la data convenzionalmente vuota */
        millisec = calendario.getTimeInMillis()
        dataVuota = new Date(millisec)

        /**
         * regola il calendario come non-lenient
         * (se la data non è valida non effettua la
         * rotazione automatica dei valori dei campi,
         * es. 32-12-2004 non diventa 01-01-2005)
         */
        calendario.setLenient(false)

        /* crea i formattatori per le date */
        dateFormat = new SimpleDateFormat("dd-MM-yyyy")
        dateFormat.setCalendar(calendario)

        shortDateFormat = new SimpleDateFormat("dd-MM-yy")
        shortDateFormat.setCalendar(calendario)

        wikiDateFormat = new SimpleDateFormat("yyyy-MM-dd-z-HH:mm:ss-z")
        wikiDateFormat.setCalendar(calendario)
    } // fine del metodo

    /**
     * Costruisce tutti i giorni dell'anno
     * Considera anche l'anno bisestile
     *
     * Restituisce un array di Map
     * Ogni mappa ha:
     * numeroMese
     * #progressivoNormale
     * #progressivoBisestile
     * nome  (numero per il primo del mese)
     * titolo (1° per il primo del mese)
     */
    public static ArrayList<Map> getAllGiorni() {
        // variabili e costanti locali di lavoro
        ArrayList<Map> lista = new ArrayList<Map>()
        def mesi = 1..12
        int numMese

        mesi.each {
            numMese = (Integer) it
            lista = getGiorniMese(lista, numMese)
        } // fine del ciclo each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Costruisce tutti i giorni del mese
     * Considera anche l'anno bisestile
     *
     * Restituisce un array di Map
     * Ogni mappa ha:
     * numeroMese
     * #progressivoNormale
     * #progressivoBisestile
     * nome  (numero per il primo del mese)
     * titolo (1° per il primo del mese)
     */
    public static ArrayList<Map> getGiorniMese(ArrayList<Map> lista, int numMese) {
        // variabili e costanti locali di lavoro
        Map mappa
        def giorni
        String nomeMese
        int prog = lista.size()
        nomeMese = Mese.getLong(numMese)
        giorni = Mese.getGiorni(numMese)
        int taglioBisestile = 60

        //--patch per febbraio
        if (numMese == 2) {
            giorni++
        }// fine del blocco if

        for (int k = 1; k <= giorni; k++) {
            prog++
            mappa = new HashMap()
            mappa.mese = numMese
            mappa.normale = prog
            mappa.bisestile = prog
            mappa.nome = k + ' ' + nomeMese
            if (k == 1) {
                mappa.titolo = PRIMO + ' ' + nomeMese
            } else {
                mappa.titolo = k + ' ' + nomeMese
            }// fine del blocco if-else

            //--gestione degli anni bisestili
            if (prog == taglioBisestile) {
                mappa.normale = 0
            }// fine del blocco if
            if (prog > taglioBisestile) {
                mappa.normale = prog - 1
            }// fine del blocco if

            lista.add(mappa)
        } // fine del ciclo for

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Restituisce una data vuota.
     * <p/>
     * La data vuota è una data particolare convenzionalmente interpretata come vuota
     *
     * @return la data vuota
     */
    static Date getVuota() {
        /* variabili e costanti locali di lavoro */
        Date data = null
        GregorianCalendar calendario
        long millisec

        calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0)

        /* crea la data convenzionalmente vuota */
        millisec = calendario.getTimeInMillis()
        data = new Date(millisec)

        /* valore di ritorno */
        return data
    } // fine del metodo

    /**
     * Converte una stringa in data.
     * <p/>
     *
     * @param stringa da convertire
     *
     * @return la data corrispondente
     */
    public static Date getData(String stringa) {
        /* variabili e costanti locali di lavoro */
        Date data = null
        DateFormat formatter

        data = getVuota()
        formatter = shortDateFormat
        try { // prova ad eseguire il codice
            data = formatter.parse(stringa)
        } catch (ParseException unErrore) { // intercetta l'errore
            def errore = unErrore
            def stop
        }// fine del blocco try-catch

        /* valore di ritorno */
        return data
    } // fine del metodo

    /**
     * Converte una stringa formatta wiki in data.
     *
     * @param stringa da convertire
     * @return la data corrispondente
     */
    public static Date getWikiData(String stringa) {
        /* variabili e costanti locali di lavoro */
        Date data
        DateFormat formatter

        data = getVuota()

        GregorianCalendar calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0)
        SimpleDateFormat wikiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        wikiDateFormat.setCalendar(calendario)

        formatter = wikiDateFormat
        try { // prova ad eseguire il codice
            data = formatter.parse(stringa)
        } catch (ParseException unErrore) { // intercetta l'errore
            def errore = unErrore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return data
    } // fine del metodo

    /**
     * Converte una stringa formatta wiki in timestamp.
     *
     * @param stringa da convertire
     * @return il timestamp corrispondente
     */
    public static Timestamp getWikiTimestamp(String stringa) {
        /* variabili e costanti locali di lavoro */
        Timestamp timestamp = null
        Date data = getWikiData(stringa)

        if (data) {
            timestamp = new Timestamp(data.getTime())
        }// fine del blocco if

        /* valore di ritorno */
        return timestamp
    } // fine del metodo

    /**
     * Converte una stringa formatta wiki in long.
     *
     * @param stringa da convertire
     * @return il valore long corrispondente
     */
    public static long getWikiLongTimestamp(String stringa) {
        /* variabili e costanti locali di lavoro */
        long longValue
        Timestamp timestamp = getWikiTimestamp(stringa)

        if (timestamp) {
            longValue = timestamp.time
        }// fine del blocco if

        /* valore di ritorno */
        return longValue
    } // fine del metodo

    /**
     * Converte una data in stringa.
     * <p/>
     *
     * @param data da convertire
     *
     * @return la stringa corrispondente
     */
    static String getStringa(Date data) {
        /* variabili e costanti locali di lavoro */
        String stringa = ""
        DateFormat formatter

        try { // prova ad eseguire il codice
            if (data) {
                formatter = shortDateFormat
                stringa = formatter.format(data)
            }// fine del blocco if
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return stringa
    } // fine del metodo

    /**
     * Restituisce la data corrente.
     *
     * @return la data del giorno
     */
    static Date getCorrente() {
        /* variabili e costanti locali di lavoro */
        Date dataOut = null
        Date data
        String stringa

        /* recupera la data di sistema */
        data = new Date(System.currentTimeMillis())

        // todo provvisorio!!
//            data = Lib.Date.creaData(11,7,2008);

        /* converte in stringa e poi di nuovo in data per forzare
         * la perdita della parte relativa a ore, minuti, secondi ecc...
         * che causa problemi nel confronto di date uguali */
        stringa = getStringa(data)
        dataOut = getData(stringa)

        /* valore di ritorno */
        return dataOut
    } // fine del metodo

    /**
     * Restituisce i secondi passati dalla mezzanotte di oggi.
     *
     * @return i secondi passati
     */
    static int getSecondiCorrenti() {
        /* variabili e costanti locali di lavoro */
        int secondi = 0
        long mezzanotte
        long adesso
        long diff

        Calendar cal = new GregorianCalendar()
        cal.setTime(getCorrente())
        mezzanotte = cal.getTimeInMillis()
        adesso = System.currentTimeMillis()
        diff = adesso - mezzanotte
        secondi = (int) diff / 1000

        /* valore di ritorno */
        return secondi
    } // fine del metodo

    /**
     * Crea la data da un timestamp.
     * Azzera eventuali valori di ore, minuti, secondi e millisecondi
     * La data parte dalla mezzanotte
     *
     * @param timestamp
     * @return la data creata
     */
    public static Date creaData(Timestamp timestamp) {
        /* variabili e costanti locali di lavoro */
        Date giorno = null
        Calendar cal

        try { // prova ad eseguire il codice
            cal = Calendar.getInstance()
            cal.setTime(timestamp)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            giorno = new Date(cal.getTime().getTime());

        } catch (Exception unErrore) { // intercetta l'errore
            def nonServe = unErrore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return giorno
    }// fine del metodo

    static boolean isData(String value) {
        boolean isData = false
        String tag0 = '0'
        String tag00 = '00'
        String timeTxt = value
        String ore = timeTxt.substring(11, 13)
        String minuti = timeTxt.substring(14, 16)
        String secondi = timeTxt.substring(17, 19)
        String decimi = timeTxt.substring(20, 21)

        if (ore.equals(tag00) && minuti.equals(tag00) && secondi.equals(tag00) && decimi.equals(tag0)) {
            isData = true
        }// fine del blocco if

        return isData
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    public static String getGioMeseAnno() {
        return getGioMeseAnno(getCorrente())
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    private static String getGioMeseAnnoBase(Date data, boolean corta) {
        /* variabili e costanti locali di lavoro */
        String dataFormattata = ''
        GregorianCalendar cal = new GregorianCalendar()
        def giorno
        def mese
        def anno
        String sep = '-'

        if (corta) {
            sep = '-'
        } else {
            sep = ' '
        }// fine del blocco if-else

        try { // prova ad eseguire il codice
            if (data) {
                cal.setTime(data)
                giorno = cal.get(Calendar.DAY_OF_MONTH)
                mese = cal.get(Calendar.MONTH)
                mese++
                mese = Mese.getShort(mese)  //scrive il nome del mese, ma allarga la colonna
                anno = cal.get(Calendar.YEAR)
                anno = anno + ''
                if (corta) {
                    anno = anno.substring(2)
                }// fine del blocco if
                dataFormattata += giorno
                dataFormattata += sep
                dataFormattata += mese
                dataFormattata += sep
                dataFormattata += anno
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return dataFormattata
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    public static String getGioMeseAnno(Date data) {
        return getGioMeseAnnoBase(data, true)
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    public static String getGioMeseAnnoLungo(Date data) {
        return getGioMeseAnnoBase(data, false)
    }// fine del metodo

    /**
     * Presentazione dell'orario (date and time).
     */
    public static String getGioMeseAnnoTime() {
        return getGioMeseAnnoTime(new Date(System.currentTimeMillis()))
    }// fine del metodo

    /**
     * Presentazione della data.
     */
    public static String getGioMeseAnnoTime(Date tempo) {
        /* variabili e costanti locali di lavoro */
        String dataFormattata = ''
        GregorianCalendar cal = new GregorianCalendar()
        def giorno
        def mese
        def anno
        def ore
        def minuti
        String sepA = '-'
        String sepB = ' '
        String sepC = ':'

        try { // prova ad eseguire il codice
            if (tempo) {
                cal.setTime(tempo)
                giorno = cal.get(Calendar.DAY_OF_MONTH)
                mese = cal.get(Calendar.MONTH)
                mese++
                mese = Mese.getShort(mese)  //scrive il nome del mese, ma allarga la colonna
                anno = cal.get(Calendar.YEAR)
                anno = anno + ''
                anno = anno.substring(2)
                ore = cal.get(Calendar.HOUR_OF_DAY)
                minuti = cal.get(Calendar.MINUTE)
                if (minuti < 10) {
                    minuti = '0' + minuti
                }// fine del blocco if
                dataFormattata += giorno
                dataFormattata += sepA
                dataFormattata += mese
                dataFormattata += sepA
                dataFormattata += anno
                dataFormattata += sepB
                dataFormattata += ore
                dataFormattata += sepC
                dataFormattata += minuti
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return dataFormattata
    }// fine del metodo

} // fine della classe statica
