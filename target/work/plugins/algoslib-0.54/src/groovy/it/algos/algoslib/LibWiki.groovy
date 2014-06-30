package it.algos.algoslib

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-8-13
 * Time: 20:30
 */
class LibWiki {
    /**
     * tag per la stringa a capo
     */
    public static final String CR = '\n'
    /**
     * tag per la stringa a capo
     */
    public static final String ACAPO = '\n'
    /**
     * tag per il carattere bold (grassetto)
     */
    public static final String BOLD = "'''"
    /**
     * tag per la singola quadra di apertura
     */
    public static final String QUADRA_INI = '['
    /**
     * tag per le doppie quadre di apertura
     */
    public static final String QUADRE_INI = QUADRA_INI + QUADRA_INI
    /**
     * tag per la singola quadra di chiusura
     */
    public static final String QUADRA_END = ']'
    /**
     * tag per le doppie quadre di chiusura
     */
    public static final String QUADRE_END = QUADRA_END + QUADRA_END
    /**
     * tag per la singola graffa di apertura
     */
    public static final String GRAFFA_INI = '{'
    /**
     * tag per le doppie graffe di apertura
     */
    public static final String GRAFFE_INI = GRAFFA_INI + GRAFFA_INI
    /**
     * tag per la singola graffa di apertura
     */
    public static final String GRAFFA_END = '}'
    /**
     * tag per le doppie graffe di chiusura
     */
    public static final String GRAFFE_END = GRAFFA_END + GRAFFA_END
    /**
     * tag di apertura delle note
     */
    public static final String REF_INI = '<ref>'
    /**
     * tag di chiusura delle note
     */
    public static final String REF_END = '</ref>'
    /**
     * tag di chiusura delle note
     */
    public static final String REF_END_DUE = '<ref/>'

    //--tag usati dal software wediawiki per suddividere in colonne
    public static final String TAG_INIZIO_COLONNA = '{{MultiCol}}'
    public static final String TAG_SPEZZA_COLONNA = '{{ColBreak}}'
    public static final String TAG_FINE_COLONNA = '{{EndMultiCol}}'

    /**
     * Elimina (eventuali) tripli apici (grassetto) in testa e coda della stringa.
     * Funziona solo se gli apici sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con tripli apici eliminati
     */
    public static String setNoBold(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (stringaIn.length() > 0) {
                stringaOut = stringaIn.trim()
                stringaOut = Lib.Text.levaTesta(stringaOut, BOLD)
                stringaOut = Lib.Text.levaCoda(stringaOut, BOLD)
                stringaOut = stringaOut.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Aggiunge tripli apici (grassetto) in testa e coda della stringa.
     * Aggiunge SOLO se gia non esistono
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali apici già presenti, per evitare di metterli doppi
     *
     * @param stringa in ingresso
     * @return stringa con tripli apici aggiunte
     */
    public static String setBold(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (setNoBold(stringaIn)) {
                stringaOut = BOLD + setNoBold(stringaIn) + BOLD
            }// fine del blocco if-else
            stringaOut = stringaOut.trim()
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Elimina (eventuali) doppie quadre in testa e coda della stringa.
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con doppie quadre eliminate
     */
    public static String setNoQuadre(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (stringaIn.length() > 0) {
                stringaOut = stringaIn.trim()
                stringaOut = Lib.Text.levaTesta(stringaOut, QUADRA_INI)
                stringaOut = Lib.Text.levaTesta(stringaOut, QUADRA_INI)
                stringaOut = Lib.Text.levaCoda(stringaOut, QUADRA_END)
                stringaOut = Lib.Text.levaCoda(stringaOut, QUADRA_END)
                stringaOut = stringaOut.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Aggiunge doppie quadre in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali quadre già presenti, per evitare di metterle doppi
     *
     * @param stringa in ingresso
     * @return stringa con doppie quadre aggiunte
     */
    public static String setQuadre(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (setNoQuadre(stringaIn)) {
                stringaOut = QUADRE_INI + setNoQuadre(stringaIn) + QUADRE_END
            }// fine del blocco if-else
            stringaOut = stringaOut.trim()
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Elimina (eventuali) doppie graffe in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con doppie graffe eliminate
     */
    public static String setNoGraffe(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (stringaIn.length() > 0) {
                stringaOut = stringaIn.trim()
                stringaOut = Lib.Text.levaTesta(stringaOut, GRAFFA_INI)
                stringaOut = Lib.Text.levaTesta(stringaOut, GRAFFA_INI)
                stringaOut = Lib.Text.levaCoda(stringaOut, GRAFFA_END)
                stringaOut = Lib.Text.levaCoda(stringaOut, GRAFFA_END)
                stringaOut = stringaOut.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Aggiunge doppie graffe in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole)
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali graffe già presenti, per evitare di metterle doppi
     *
     * @param stringa in ingresso
     * @return stringa con doppie graffe aggiunte
     */
    public static String setGraffe(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (setNoGraffe(stringaIn)) {
                stringaOut = GRAFFE_INI + setNoGraffe(stringaIn) + GRAFFE_END
            }// fine del blocco if-else
            stringaOut = stringaOut.trim()
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Elimina (eventuali) tag ref in testa e coda della stringa.
     * Pulisce la stringa da inizio/fine vuoto (trim)
     * Funziona solo se i tag sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa col tag ref iniziale e finale eliminato
     */
    public static String setNoRef(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (stringaIn.length() > 0) {
                stringaOut = stringaIn.trim()
                stringaOut = Lib.Text.levaTesta(stringaOut, REF_INI)
                stringaOut = Lib.Text.levaCoda(stringaOut, REF_END)
                stringaOut = Lib.Text.levaCoda(stringaOut, REF_END_DUE)
                stringaOut = stringaOut.trim()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Aggiunge tag ref in testa e coda alla stringa.
     * Aggiunge SOLO se gia non esiste
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     * Elimina eventuali ref già presenti, per evitare di metterli doppi
     *
     * @param stringa in ingresso
     * @return stringa con tag ref aggiunti
     */
    public static String setRef(def stringaIn) {
        // variabili e costanti locali di lavoro
        String stringaOut = null

        if (stringaIn instanceof String) {
            stringaOut = stringaIn
            if (setNoRef(stringaIn)) {
                stringaOut = REF_INI + setNoRef(stringaIn) + REF_END
            }// fine del blocco if-else
            stringaOut = stringaOut.trim()
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Elimina (eventuali) quadre (singole o doppie) in testa e coda della stringa.
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con singole/doppie quadre eliminate
     */
    public static levaQuadre(def stringa) {
        return setNoQuadre(stringa)
    } // fine del metodo

    /**
     * Elimina (eventuali) quadre (singole o doppie) in testa e coda della stringa.
     * Funziona solo se le quadre sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con singole/doppie quadre eliminate
     */
    public static levaQuadreDoppie(def stringa) {
        return setNoQuadre(stringa)
    } // fine del metodo

    /**
     * Elimina (eventuali) tonde (singole) in testa e coda della stringa.
     * Funziona solo se le tonde sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con tonde eliminate
     */
    public static levaTonde(def stringa) {
        if (stringa instanceof String) {
            return Lib.Text.levaBase((String) stringa, '(', ')').trim()
        } else {
            return null
        }// fine del blocco if-else
    } // fine del metodo

    /**
     * Elimina (eventuali) tonde (doppie) in testa e coda della stringa.
     * Funziona solo se le tonde sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con tonde eliminate
     */
    public static levaTondeDoppie(def stringa) {
        if (stringa instanceof String) {
            return Lib.Text.levaBase((String) stringa, '((', '))').trim()
        } else {
            return null
        }// fine del blocco if-else
    } // fine del metodo

    /**
     * Elimina (eventuali) apicetti (singoli) in testa e coda della stringa.
     * Funziona solo se gli apicetti sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con tonde eliminate
     */
    public static levaApicetti(def stringa) {
        if (stringa instanceof String) {
            return Lib.Text.levaBase((String) stringa, "'", "'").trim()
        } else {
            return null
        }// fine del blocco if-else
    } // fine del metodo

    /**
     * Elimina (eventuali) apicetti (doppi) in testa e coda della stringa.
     * Funziona solo se gli apicetti sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringa in ingresso
     * @return stringa con tonde eliminate
     */
    public static levaApicettiDoppi(def stringa) {
        if (stringa instanceof String) {
            return Lib.Text.levaBase((String) stringa, "''", "''").trim()
        } else {
            return null
        }// fine del blocco if-else
    } // fine del metodo

    /**
     * Suddivide la lista in due colonne, inserendo i tag per il software mediawiki.
     * Se il numero di righe è pari, ogni colonna ha lo stesso numero di righe
     * Se il numero di righe è dispari, la prima colonna è più lunga di una riga
     *
     * @param listaIn in ingresso
     * @return listaOut in uscita
     */
    public static ArrayList listaDueColonne(ArrayList listaIn) {
        // variabili e costanti locali di lavoro
        ArrayList listaOut = null
        String tagIni = TAG_INIZIO_COLONNA
        String tagColonna = TAG_SPEZZA_COLONNA
        String tagEnd = TAG_FINE_COLONNA
        int dim
        int lunghezza

        if (listaIn) {
            dim = listaIn.size()
            lunghezza = dim / 2
            lunghezza++
            if (Lib.Math.isDispari(dim)) {
                lunghezza++
            }// fine del blocco if
            listaOut = listaIn
            listaOut.add(0, tagIni)
            listaOut.add(lunghezza, tagColonna)
            listaOut.add(tagEnd)
        }// fine del blocco if

        // valore di ritorno
        return listaOut
    } // fine del metodo

    /**
     * Calcola il numero di righe della lista.
     *
     * @param lista in ingresso
     * @return numero di righe
     */
    public static int dimLista(lista) {
        // variabili e costanti locali di lavoro
        int righe = 0
        def righeElemento
        boolean continua = false

        if (lista && lista.size() > 0) {
            continua = true
        }// fine del blocco if

        if (continua) {
            lista.each {
                righeElemento = it.split(ACAPO)
                righe += righeElemento.size()
            }// fine di each

        }// fine del blocco if

        // valore di ritorno
        return righe
    } // fine del metodo

    /**
     * Controlla se nel testo ci sono occorrenze pari dei tag
     * Le occorrenze devono anche essere nel giusto ordine
     *
     * @param testo in ingresso
     * @param tagOpen di apertura
     * @param tagClose di chiusura
     * @return vero se le occorrenze di apertura e chiusura sono uguali
     */
    public static boolean isTagPari(String testo, String tagOpen, String tagClose) {
        // variabili e costanti locali di lavoro
        boolean pari = false
        String[] open
        String[] close
        String patch = 'aggiungo in fondo per coprire il caso in cui il tag sia alla fine'
        int firstOpen
        int firstClose
        int lastOpen
        int lastClose

        if (testo && tagOpen && tagClose) {
            testo += patch
            open = testo.split(tagOpen)
            close = testo.split(tagClose)

            pari = (open.size() == close.size())

            //se è pari deve comunque avere anche un certo ordine
            //il primo deve essere un tagOpen e l'ultimo un tagClose
            if (pari) {
                tagOpen = Lib.Text.levaTesto(tagOpen, '\\')
                tagClose = Lib.Text.levaTesto(tagClose, '\\')
                firstOpen = testo.indexOf(tagOpen)
                if (firstOpen == -1) {
                    return true
                }// fine del blocco if

                firstClose = testo.indexOf(tagClose)
                lastOpen = testo.lastIndexOf(tagOpen)
                lastClose = testo.lastIndexOf(tagClose)
                if ((firstOpen < firstClose) && (lastOpen < lastClose)) {
                    pari = true
                } else {
                    pari = false
                }// fine del blocco if-else
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return pari
    } // fine del metodo

    /**
     * Controlla se nel testo ci sono occorrenze pari delle quadre.
     * Le quadre devono anche essere nel giusto ordine
     *
     * @param testo in ingresso
     * @return vero se le occorrenze di apertura e chiusura sono uguali
     */
    public static boolean isQuadrePari(String testo) {
        // variabili e costanti locali di lavoro
        boolean pari = false
        String tagQuadraOpen = "\\[\\["
        String tagQuadraClose = "\\]\\]"

        if (testo) {
            pari = isTagPari(testo, tagQuadraOpen, tagQuadraClose)
        }// fine del blocco if

        // valore di ritorno
        return pari
    } // fine del metodo

    /**
     * Controlla se nel testo ci sono occorrenze pari delle graffe.
     * Le graffe devono anche essere nel giusto ordine
     *
     * @param testo in ingresso
     * @return vero se le occorrenze di apertura e chiusura sono uguali
     */
    public static boolean isGraffePari(String testo) {
        // variabili e costanti locali di lavoro
        boolean pari = false
        String tagGraffaOpen = "\\{\\{"
        String tagGraffaClose = "\\}\\}"

        if (testo) {
            pari = isTagPari(testo, tagGraffaOpen, tagGraffaClose)
        }// fine del blocco if

        // valore di ritorno
        return pari
    } // fine del metodo

    /**
     * Inserisce il testo in un cassetto
     *
     * @param testoIn in ingresso
     * @return testoOut in uscita
     */
    public static String cassetto(String testoIn, String titolo) {
        // variabili e costanti locali di lavoro
        String testoOut = ''
        String aCapo = '\n'

        // controllo di congruità
        if (testoIn && testoIn) {
            if (isGraffePari(testoIn)) {
                testoOut += aCapo
                testoOut += '{{cassetto'
                testoOut += aCapo
                testoOut += '|larghezza=100%'
                testoOut += aCapo
                testoOut += '|allineamento=sinistra'
                testoOut += aCapo
                testoOut += "|titolo= $titolo"
                testoOut += aCapo
                testoOut += '|testo='
                testoOut += aCapo
                testoOut += testoIn
                testoOut += aCapo
                testoOut += '}}'
                testoOut += aCapo
            } else {
                testoOut = testoIn
                //log.error "Ci sono degli errori nel testo del cassetto di $giorno"
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    } // fine del metodo

} // fine della classe
