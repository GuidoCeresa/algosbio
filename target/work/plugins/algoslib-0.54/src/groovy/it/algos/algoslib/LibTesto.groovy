package it.algos.algoslib

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 17-7-13
 * Time: 07:00
 */
class LibTesto {

    /**
     * tag per il carattere punto
     */
    public static final String PUNTO = '.'

    /**
     * tag per il carattere barra
     */
    public static final String BARRA = '/'

    /**
     * tag per la stringa vuota
     */
    public static final String VUOTA = ''

    /**
     * tag per la stringa spazio
     */
    public static final String SPAZIO = ' '

    /**
     * tag per la stringa virgola
     */
    public static final String VIRGOLA = ','

    /**
     * tag per la stringa tabulatore
     */
    public static final String TAB = '\t'

    /**
     * tag per la stringa a capo
     */
    public static final String CR = '\n'

    /**
     * tag per il valore falso per una posizione
     */
    public static final int INT_NULLO = -1

    /**
     * Forza il primo carattere della stringa a maiuscolo
     *
     * Restituisce una stringa
     * Un numero ritorna un numero
     * Un nullo ritorna un nullo
     * Un oggetto non stringa ritorna uguale
     * @param entrata stringa in ingresso
     * @return uscita string in uscita
     */
    public static primaMaiuscola(def entrata) {
        // variabili e costanti locali di lavoro
        def uscita = entrata
        String primo
        String rimanente

        if (entrata && entrata instanceof String) {
            primo = entrata.substring(0, 1)
            primo = primo.toUpperCase()
            rimanente = entrata.substring(1)
            uscita = primo + rimanente
        }// fine del blocco if

        // valore di ritorno
        return uscita
    } // fine del metodo

    /**
     * Forza il primo carattere della stringa a minuscolo
     *
     * Restituisce una stringa
     * Un numero ritorna un numero
     * Un nullo ritorna un nullo
     * Un oggetto non stringa ritorna uguale
     * @param entrata stringa in ingresso
     * @return uscita string in uscita
     */
    public static primaMinuscola(def entrata) {
        // variabili e costanti locali di lavoro
        def uscita = entrata
        String primo
        String rimanente

        if (entrata && entrata instanceof String) {
            primo = entrata.substring(0, 1)
            primo = primo.toLowerCase()
            rimanente = entrata.substring(1)
            uscita = primo + rimanente
        }// fine del blocco if

        // valore di ritorno
        return uscita
    } // fine del metodo

    /**
     * Elimina la testa iniziale della stringa, se esiste.
     * <p/>
     * Esegue solo se la stringa è valida
     * Elimina spazi vuoti iniziali e finali
     * Se manca la testa, restituisce la stringa
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     * @param testa da eliminare
     *
     * @return uscita stringa convertita
     */
    public static levaTesta(def entrata, def testa) {
        // variabili e costanti locali di lavoro
        def uscita = entrata

        if (entrata && entrata instanceof String && testa && testa instanceof String) {
            uscita = entrata.trim()
            if (testa && uscita.startsWith(testa)) {
                uscita = uscita.substring(testa.length())
                uscita = uscita.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return uscita
    } // fine del metodo

    /**
     * Elimina la coda terminale della stringa, se esiste.
     * <p/>
     * Esegue solo se la stringa è valida
     * Elimina spazi vuoti iniziali e finali
     * Se manca la coda, restituisce la stringa
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     * @param coda da eliminare
     *
     * @return uscita stringa convertita
     */
    public static levaCoda(def entrata, def coda) {
        // variabili e costanti locali di lavoro
        def uscita = entrata

        if (entrata && entrata instanceof String && coda && coda instanceof String) {
            uscita = entrata.trim()
            if (coda && uscita.endsWith(coda)) {
                uscita = uscita.substring(0, uscita.length() - coda.length())
                uscita = uscita.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return uscita
    } // fine del metodo

    /**
     * Elimina la parte di stringa successiva al tag, se esiste.
     * <p/>
     * Esegue solo se la stringa è valida
     * Elimina spazi vuoti iniziali e finali
     * Se manca il tag, restituisce la stringa
     *
     * @param testoIn stringa in ingresso
     * @param tagFinale da eliminare
     * @return testoOut stringa ridotta
     */
    public static levaDopo(String testoIn, String tagFinale) {
        // variabili e costanti locali di lavoro
        String testoOut = testoIn
        int pos

        if (testoOut && tagFinale) {
            testoOut = testoOut.trim()
            if (testoOut.contains(tagFinale)) {
                pos = testoOut.indexOf(tagFinale)
                testoOut = testoOut.substring(0, pos)
                testoOut = testoOut.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    } // fine del metodo

    /**
     * Elimina la parte di stringa successiva al tag <ref>, se esiste.
     *
     * Esegue solo se la stringa è valida
     * Elimina spazi vuoti iniziali e finali
     * Se manca il tag, restituisce la stringa
     *
     * @param testoIn stringa in ingresso
     * @return testoOut stringa ridotta
     */
    public static levaDopoRef(String testoIn) {
        return levaDopo(testoIn, '<ref')
    } // fine del metodo

    /**
     * Elimina la parte di stringa successiva al tag <!--, se esiste.
     *
     * Esegue solo se la stringa è valida
     * Elimina spazi vuoti iniziali e finali
     * Se manca il tag, restituisce la stringa
     *
     * @param testoIn stringa in ingresso
     * @return testoOut stringa ridotta
     */
    public static levaDopoNote(String testoIn) {
        return levaDopo(testoIn, '<!--')
    } // fine del metodo

    /**
     * Elimina la parte di stringa successiva al tag {{, se esiste.
     *
     * Esegue solo se la stringa è valida
     * Elimina spazi vuoti iniziali e finali
     * Se manca il tag, restituisce la stringa
     *
     * @param testoIn stringa in ingresso
     * @return testoOut stringa ridotta
     */
    public static levaDopoGraffe(String testoIn) {
        return levaDopo(testoIn, '{{')
    } // fine del metodo

    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param testoIn in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     * @return testoOut convertito
     */
    public static sostituisce(def testoIn, String oldStringa, String newStringa) {
        // variabili e costanti locali di lavoro
        def testoOut = testoIn
        int pos = 0
        String prima = VUOTA

        if (testoIn && testoIn instanceof String && oldStringa && newStringa instanceof String) {
            testoOut = testoIn.trim()
            if (testoIn.contains(oldStringa)) {

                while (pos != INT_NULLO) {
                    pos = testoIn.indexOf(oldStringa)
                    if (pos != INT_NULLO) {
                        prima += testoIn.substring(0, pos)
                        prima += newStringa
                        pos += oldStringa.length()
                        testoIn = testoIn.substring(pos)
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + testoIn
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    } // fine del metodo

    /**
     * Elimina tutti i caratteri contenuti nella stringa.
     * Esegue solo se il testo è valido
     *
     * @param testoIn in ingresso
     * @param subStringa da eliminare
     * @return testoOut stringa convertita
     */
    public static levaTesto(def testoIn, String subStringa) {
        // variabili e costanti locali di lavoro
        def testoOut = testoIn

        if (testoIn && testoIn instanceof String) {
            testoOut = testoIn.trim()
            if (testoOut.contains(subStringa)) {
                testoOut = sostituisce(testoOut, subStringa, VUOTA)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    } // fine del metodo

    /**
     * Elimina tutti i punti contenuti nella stringa.
     * Esegue solo se la stringa è valida
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     * @return uscita stringa convertita
     */
    public static levaPunti(def entrata) {
        return levaTesto(entrata, PUNTO)
    } // fine del metodo

    /**
     * Elimina tutte le virgole contenute nella stringa.
     * Esegue solo se la stringa è valida
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     * @return uscita stringa convertita
     */
    public static levaVirgole(def entrata) {
        return levaTesto(entrata, VIRGOLA)
    } // fine del metodo

    public static levaZero(String inputString) {
        String outputString = ''
        String zero = '0'

        // controllo di congruita
        if (inputString) {
            outputString = inputString

            if (inputString.startsWith(zero)) {
                outputString = inputString.substring(zero.length())
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return outputString
    } // fine del metodo

    public static levaSpazio(String inputString) {
        String outputString = ''
        String spazio = ' '
        String under = '_'

        // controllo di congruita
        if (inputString) {
            outputString = inputString

            if (outputString.contains(spazio)) {
                outputString = outputString.replaceAll(spazio, under)
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return outputString
    } // fine del metodo

    /**
     * Elimina il carattere in testa ed in coda alla stringa.
     * Funziona solo se i caratteri sono IN TESTA ed IN CODA alla stringa
     *
     * @return stringa con i caratteri eliminati
     */
    public static levaBase(String stringa, String tagIni, String tagEnd) {
        /* variabili e costanti locali di lavoro */
        String testo = ''
        boolean continua = false

        if (stringa) {
            testo = stringa
            continua = true
        }// fine del blocco if

        if (continua) {
            continua = (stringa.startsWith(tagIni))
        }// fine del blocco if

        if (continua) {
            testo = stringa.substring(tagIni.length())
            continua = (testo.endsWith(tagEnd))
        }// fine del blocco if

        if (continua) {
            testo = testo.substring(0, testo.length() - tagEnd.length())
        }// fine del blocco if

        /* valore di ritorno */
        return testo
    } // fine del metodo

    /**
     * Estrae un parte di un testo compresa tra due tag inizio/fine uguali
     * Gli estremi sono COMPRESI
     * Esegue solo se il testo ed i tag sono validi
     * Se manca uno dei due tag, restituisce il testo originale
     * Se i tag sono invertiti, restituisce il testo originale
     * Elimina spazi vuoti
     *
     * @param testo completo da esaminare
     * @param tagUnicoInizialeFinale tag iniziale e finale per il contenuto richiesto
     * @return contenuto richiesto tra i due tag
     */
    public static estraeCompresi(String testo, String tagUnicoInizialeFinale) {
        return estraeCompresi(testo, tagUnicoInizialeFinale, tagUnicoInizialeFinale)
    } // fine del metodo statico

    /**
     * Estrae un parte di un testo compresa tra due tag inizio/fine
     * Gli estremi sono COMPRESI
     * Esegue solo se il testo ed i tag sono validi
     * Se manca uno dei due tag, restituisce il testo originale
     * Se i tag sono invertiti, restituisce il testo originale
     * Elimina spazi vuoti
     *
     * @param testo completo da esaminare
     * @param tagIni tag iniziale per il contenuto richiesto
     * @param tagEnd tag finale per il contenuto richiesto
     * @return contenuto richiesto tra i due tag
     */
    public static String estraeCompresi(String testo, String tagIni, String tagEnd) {
        return estraeBase(testo, tagIni, tagEnd, true)
    } // fine del metodo statico

    /**
     * Estrae un parte di un testo compresa tra due tag inizio/fine uguali
     * Gli estremi sono ESCLUSI
     * Esegue solo se il testo ed i tag sono validi
     * Se manca uno dei due tag, restituisce il testo originale
     * Se i tag sono invertiti, restituisce il testo originale
     * Elimina spazi vuoti
     *
     * @param testo completo da esaminare
     * @param tagUnicoInizialeFinale tag iniziale e finale per il contenuto richiesto
     * @return contenuto richiesto tra i due tag
     */
    public static estraeEsclusi(String testo, String tagUnicoInizialeFinale) {
        return estraeEsclusi(testo, tagUnicoInizialeFinale, tagUnicoInizialeFinale)
    } // fine del metodo statico

    /**
     * Estrae un parte di un testo compresa tra due tag inizio/fine
     * Gli estremi sono ESCLUSI
     * Esegue solo se il testo ed i tag sono validi
     * Se manca uno dei due tag, restituisce il testo originale
     * Se i tag sono invertiti, restituisce il testo originale
     * Elimina spazi vuoti
     *
     * @param testo completo da esaminare
     * @param tagIni tag iniziale per il contenuto richiesto
     * @param tagEnd tag finale per il contenuto richiesto
     * @return contenuto richiesto tra i due tag
     */
    public static String estraeEsclusi(String testo, String tagIni, String tagEnd) {
        return estraeBase(testo, tagIni, tagEnd, false)
    }// fine del metodo

    /**
     * Estrae un parte di un testo compresa tra due tag inizio/fine
     * Gli estremi sono COMPRESI o ESCLUSI a seconda del flag
     * Esegue solo se il testo ed i tag sono validi
     * Se manca uno dei due tag, restituisce il testo originale
     * Se i tag sono invertiti, restituisce il testo originale
     * Elimina spazi vuoti
     *
     * @param testo completo da esaminare
     * @param tagIni tag iniziale per il contenuto richiesto
     * @param tagEnd tag finale per il contenuto richiesto
     * @return contenuto richiesto tra i due tag
     */
    private static String estraeBase(String testo, String tagIni, String tagEnd, boolean estremiCompresi) {
        String contenuto = testo
        int posIni
        int posEnd

        if (testo && tagIni && tagEnd) {
            posIni = testo.indexOf(tagIni)
            posEnd = testo.indexOf(tagEnd, posIni + tagIni.length())

            if (contenuto.contains(tagIni) && contenuto.contains(tagEnd)) {
                if (posIni > -1 && posEnd > -1) {
                    posIni += tagIni.length()
                    if (posEnd >= posIni) {
                        if (estremiCompresi) {
                            contenuto = testo.substring(posIni - tagIni.length(), posEnd + tagEnd.length())
                        } else {
                            contenuto = testo.substring(posIni, posEnd)
                        }// fine del blocco if-else
                        contenuto = contenuto.trim()
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return contenuto
    }// fine del metodo

    /**
     * Formattazione di un numero.
     * <p/>
     * Il numero può arrivare come stringa, intero o double
     * Se la stringa contiene punti e virgole, viene pulita
     * Se la stringa non è convertibile in numero, viene restituita uguale
     * Inserisce il punto separatore ogni 3 cifre
     * Se arriva un oggetto non previsto, restituisce l'oggetto

     * @param numero da formattare (stringa, intero o double)
     * @return numero formattato
     */
    public static formatNum = { numero ->
        // variabili e costanti locali di lavoro
        def formattato = numero
        String sep = PUNTO
        int len
        String num3
        String num6
        String num9
        String num12

        //--
        if (numero instanceof Integer || numero instanceof Long) {
            numero = (String) numero
        }// fine del blocco if

        // controllo di congruità
        if (numero instanceof String || numero instanceof Integer || numero instanceof Double) {
            formattato = VUOTA

            if (numero instanceof String) {
                numero = levaVirgole(numero)
                numero = levaPunti(numero)
                formattato = numero

                try { // prova ad eseguire il codice
                    Integer.decode(numero)
                    len = numero.length()
                    if (len > 3) {
                        num3 = numero.substring(0, len - 3)
                        num3 += sep
                        num3 += numero.substring(len - 3)
                        formattato = num3
                        if (len > 6) {
                            num6 = num3.substring(0, len - 6)
                            num6 += sep
                            num6 += num3.substring(len - 6)
                            formattato = num6
                            if (len > 9) {
                                num9 = num6.substring(0, len - 9)
                                num9 += sep
                                num9 += num6.substring(len - 9)
                                formattato = num9
                                if (len > 12) {
                                    num12 = num9.substring(0, len - 12)
                                    num12 += sep
                                    num12 += num9.substring(len - 12)
                                    formattato = num12
                                }// fine del blocco if
                            }// fine del blocco if
                        }// fine del blocco if
                    }// fine del blocco if
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return formattato
    } // fine della closure

    public static String formatPercentuale(int valore, int totale) {
        String percentuale = ''
        BigDecimal risultatoDec
        int intero
        def interoGiusto
        int posPunto

        if (valore && totale) {
            risultatoDec = valore / totale
            risultatoDec = risultatoDec * 100 * 100
            intero = risultatoDec.intValue()
            interoGiusto = intero / 100
            percentuale = interoGiusto.toString()
            posPunto = percentuale.indexOf('.')
            if (posPunto != -1) {
                percentuale = percentuale.substring(0, posPunto) + ',' + percentuale.substring(posPunto + 1)
            }// fine del blocco if
            percentuale += '%'
        }// fine del blocco if

        return percentuale
    } // fine del metodo

    /**
     * Restituisce la posizione di un tag in un testo
     * Riceve una lista di tag da provare
     * Restituisce la prima posizione tra tutti i tag trovati
     *
     * @param testo in ingresso
     * @param lista di stringhe/interi, oppure singola stringa/intero
     *
     * @return posizione della prima stringa trovata
     *         -1 se non ne ha trovato nessuna
     *         -1 se il primo parametro è nullo o vuoto, oppure non è una stringa
     *         -1 se il secondo parametro è nullo
     *         -1 se il secondo parametro non è ne una lista di stringhe/interi, ne una stringa/intero
     */
    public static getPos = { testo, lista ->
        // variabili e costanti locali di lavoro
        int pos = INT_NULLO
        int posTmp
        List posizioni = new ArrayList()
        def minimo

        if (testo && testo in String && lista) {

            if (lista in Integer) {
                pos = testo.indexOf(lista.toString())
            }// fine del blocco if

            if (lista in String) {
                pos = testo.indexOf(lista)
            }// fine del blocco if

            if (lista in List) {
                lista.each {
                    posTmp = testo.indexOf(it)
                    if (posTmp != INT_NULLO) {
                        posizioni.add(posTmp)
                    }// fine del blocco if
                }// fine di each

                if (posizioni.size() > 0) {
                    minimo = posizioni.min()
                    if (minimo && minimo in Integer) {
                        pos = (int) minimo
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return pos
    } // fine del metodo

    /**
     * Controlla l'esistenza del tag nel testo
     *
     * @testo da controllare
     * @return vero se esiste il tag
     */
    public static hasTag(String testo, String tag) {
        // variabili e costanti locali di lavoro
        boolean esiste = false
        Pattern pattern
        Matcher matcher

        if (testo) {
            pattern = Pattern.compile(tag)
            matcher = pattern.matcher(testo)
            if (matcher.find()) {
                esiste = true
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return esiste
    }// fine del metodo

}// fine della classe statica
