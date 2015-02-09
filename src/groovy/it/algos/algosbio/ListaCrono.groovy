package it.algos.algosbio

import it.algos.algospref.Pref

/**
 * Created by gac on 21/01/15.
 */
class ListaCrono extends ListaBio {

    static boolean transactional = false

    public ListaCrono(def oggetto) {
        super(oggetto)
    }// fine del costruttore


    public ListaCrono(String soggetto) {
        super(soggetto)
    }// fine del costruttore


    public ListaCrono(def crono, BioService bioService) {
        super(crono, bioService)
    }// fine del costruttore

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        super.elaboraParametri()
        usaTavolaContenuti = false
        usaSuddivisioneUomoDonna = false
        usaTitoloParagrafoConLink = false
        usaDoppiaColonna = true
        usaSottopagine = false
        tagLivelloParagrafo = '==='
        if (Pref.getBool(LibBio.USA_PARAGRAFO_PUNTI_GIORNI_ANNI, true)) {
            tagParagrafoNullo = '...'
        }// fine del blocco if
    }// fine del metodo

    /**
     * Recupera il tag specifico nati/morti
     */
    protected String getTagTitolo() {
        return ''
    }// fine del metodo

    protected String getParagrafoDidascalia(ArrayList<String> listaDidascalie) {
        String testo = ''
        String didascalia
        String voceTmp
        String tag = ']]'
        String sep = ' - '
        String tagSep = tag + sep
        int pos
        int posVoce
        String dataTmp
        LinkedHashMap mappa = new LinkedHashMap()
        ArrayList lista = null

        if (Pref.getBool(LibBio.USA_GIORNI_ANNI_RAGGRUPPATI, true)) {
            listaDidascalie?.each {
                didascalia = it
                if (didascalia.contains(tagSep)) {
                    pos = didascalia.indexOf(tag)
                    pos += tag.length()
                    dataTmp = didascalia.substring(0, pos)
                    dataTmp = dataTmp.trim()
                    posVoce = didascalia.indexOf(tagSep)
                    posVoce += tagSep.length()
                    voceTmp = didascalia.substring(posVoce)
                    voceTmp = voceTmp.trim()
                } else {
                    dataTmp = ''
                    voceTmp = didascalia
                }// fine del blocco if-else
                if (mappa.containsKey(dataTmp)) {
                    lista = (ArrayList) mappa.get(dataTmp)
                    lista.add(voceTmp)
                } else {
                    lista = new ArrayList()
                    lista.add(voceTmp)
                    mappa.put(dataTmp, lista)
                }// fine del blocco if-else
            }// fine del ciclo each

            mappa?.each { key, value ->
                if (key.equals('')) {
                    value?.each {
                        testo += '*'
                        testo += it
                        testo += aCapo
                    } // fine del ciclo each
                } else {
                    if (value && value instanceof ArrayList && value.size() == 1) {
                        testo += '*'
                        testo += key + sep + value.get(0)
                        testo += aCapo
                    } else {
                        testo += '*'
                        testo += key
                        testo += aCapo
                        value?.each {
                            testo += '**'
                            testo += it
                            testo += aCapo
                        } // fine del ciclo each
                    }// fine del blocco if-else
                }// fine del blocco if-else
            } // fine del ciclo each

        } else {
            testo = super.getParagrafoDidascalia(listaDidascalie)
        }// fine del blocco if-else

        return testo.trim()
    }// fine del metodo

}// fine della classe
