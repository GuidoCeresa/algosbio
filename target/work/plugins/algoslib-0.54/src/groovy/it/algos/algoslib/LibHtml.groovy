package it.algos.algoslib

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 17-7-13
 * Time: 06:59
 */
class LibHtml {

    private static String COLOR_VERDE = 'mediumseagreen'
    private static String COLOR_BLU = 'royalblue'
    private static String COLOR_ROSSO = 'deeppink'

    /**
     * Inserisce tag in testa e coda della stringa.
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se il tag è nullo o vuoto o non stringa, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param String : stringaIn  in ingresso
     * @param String : tag da aggiungere in testa ed in coda alla stringa
     * @param String : attr attributo da aggiungere dopo l'apertura del tag
     * @return stringa coi tag aggiunti
     */
    public static setTag(stringaIn, tag, attr) {
        // variabili e costanti locali di lavoro
        String stringaOut = null
        String spazio = ' '
        String apre = '<'
        String chiude = '>'
        String slash = '/'

        if (stringaIn && stringaIn in String) {
            if (tag && tag in String) {
                stringaIn = stringaIn.trim()

                stringaOut = apre + tag
                if (attr && attr in String) {
                    stringaOut += spazio + attr
                }// fine del blocco if
                stringaOut += chiude
                stringaOut += stringaIn
                stringaOut += apre
                stringaOut += slash
                stringaOut += tag
                stringaOut += chiude
            } else {
                stringaOut = stringaIn
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return stringaOut
    } // fine del metodo

    /**
     * Inserisce tag in testa e coda della stringa.
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se il tag è nullo o vuoto o non stringa, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param String : stringaIn  in ingresso
     * @param String : tag da aggiungere in testa ed in coda alla stringa
     * @return stringa coi tag aggiunti
     */
    public static setTag(stringaIn, tag) {
        return setTag(stringaIn, tag, '')
    } // fine del metodo

    /**
     * Inserisce tag form in testa e coda della stringa.
     * Se arriva un oggetto non stringa, restituisce nullo
     * Se il tag è nullo o vuoto o non stringa, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param String : stringaIn  in ingresso
     * @param String : attr attributo da aggiungere dopo l'apertura del tag
     * @return stringa coi tag aggiunti
     */
    public static setTagForm(stringaIn, attr) {
        return setTag(stringaIn, 'form', attr)
    } // fine del metodo

    // Estrae dei blocchi di testo separati dai tags
    public static ArrayList<String> getTagsHtml(String testo) {
        // variabili e costanti locali di lavoro
        return getTags(testo, '<', '>')
    } // fine del metodo

    // Estrae dei blocchi di testo separati dai tags
    public static ArrayList<String> getTags(String testo, String tagIni, String tagEnd) {
        // variabili e costanti locali di lavoro
        ArrayList<String> blocchi = null
        String sep
        int pos

        if (testo && tagIni && tagEnd) {
            sep = tagEnd + tagIni
            blocchi = Lib.Array.getBlocchi(testo, sep)
            pos = blocchi.size() - 1
            if (blocchi && blocchi instanceof ArrayList && pos >= 0) {
                blocchi[0] = Lib.Txt.levaTesta(blocchi[0], tagIni)
                blocchi[pos] = Lib.Txt.levaCoda(blocchi[pos], tagEnd)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return blocchi
    } // fine del metodo

    public static String getRigaMenu(String app, String cont, String action, String icon, String title) {
        String testo = ''
        String tagIni = '<li>'
        String tagEnd = '</li>'
        String aCapo = '\n'

        testo += tagIni
        testo += getTitoloMenu(app, cont, action, icon, title)
        testo += tagEnd
        testo += aCapo

        return testo
    }// fine del metodo

    public static String getTitoloMenu(String app, String cont, String action, String icon, String title) {
        return "<a href=\"/${app}/${cont}/${action}\" class=\"${icon}\">${title}</a>"
    }// fine del metodo

    public static String getTitoloTabellaSorted(String app, String cont, String sort, String order, String title) {
        return "<th class=\"sortable\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
    }// fine del metodo

    public static String getTitoloTabellaNotSorted(String app, String cont, String sort, String order, String title) {
        String testo

        if (order && order.equals('desc')) {
            testo = "<th class=\"sortable sorted desc\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
        } else {
            testo = "<th class=\"sortable sorted asc\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    /**
     * Scrive il testo nel colore verde
     *
     * @param testoIn ingresso
     * @return stringa nel colore
     */
    public static String setVerde(String testoIn) {
        return setColore(testoIn, COLOR_VERDE)
    } // fine del metodo

    /**
     * Scrive il testo nel colore blu
     *
     * @param testoIn ingresso
     * @return stringa nel colore
     */
    public static String setBlu(String testoIn) {
        return setColore(testoIn, COLOR_BLU)
    } // fine del metodo

    /**
     * Scrive il testo nel colore rosso
     *
     * @param testoIn ingresso
     * @return stringa nel colore
     */
    public static String setRosso(String testoIn) {
        return setColore(testoIn, COLOR_ROSSO)
    } // fine del metodo

    /**
     * Scrive il testo nel colore indicato
     *
     * @param testoIn ingresso
     * @param colore da regolare
     * @return stringa nel colore
     */
    private static String setColore(String testoIn, String colore) {
        String testoOut = testoIn
        String tagIni = "<span style=\"color:${colore}\">"
        String tagOut = '</span>'

        if (testoIn) {
            testoOut = tagIni
            testoOut += testoIn
            testoOut += tagOut
        }// fine del blocco if

        return testoOut
    } // fine del metodo

    /**
     * Scrive il testo nel colore verde (bold)
     *
     * @param testoIn ingresso
     * @return stringa nel colore
     */
    public static String setVerdeBold(String testoIn) {
        return setColoreBold(testoIn, COLOR_VERDE)
    } // fine del metodo

    /**
     * Scrive il testo nel colore blu (bold)
     *
     * @param testoIn ingresso
     * @return stringa nel colore
     */
    public static String setBluBold(String testoIn) {
        return setColoreBold(testoIn, COLOR_BLU)
    } // fine del metodo

    /**
     * Scrive il testo nel colore rosso (bold)
     *
     * @param testoIn ingresso
     * @return stringa nel colore
     */
    public static String setRossoBold(String testoIn) {
        return setColoreBold(testoIn, COLOR_ROSSO)
    } // fine del metodo

    /**
     * Scrive il testo nel colore indicato (bold)
     *
     * @param testoIn ingresso
     * @param colore da regolare
     * @return stringa nel colore
     */
    private static String setColoreBold(String testoIn, String colore) {
        String testoOut = testoIn

        if (testoIn) {
            testoIn = LibWiki.setBold(testoIn)
            testoOut = setColore(testoIn, colore)
        }// fine del blocco if

        return testoOut
    } // fine del metodo
}// fine della classe statica
