package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit
import it.algos.algoswiki.TipoAllineamento
import it.algos.algoswiki.WikiLib

/**
 * Created by gac on 11/02/15.
 */
class StatisticheNazionalita extends Statistiche {

    public StatisticheNazionalita() {
        super()
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
        nomeAttNaz = 'Nazionalità'
        inversoNomeAttNaz = 'Attività'
    }// fine del metodo

    /**
     * Numero AttNaz utilizzate
     * Sovrascritto
     */
    @Override
    protected int vociUsate() {
        return NazionalitaService.numNazionalita()
    }// fine del metodo

    /**
     * Numero AttNaz non utilizzate
     * Sovrascritto
     */
    @Override
    protected int vociNonUsate() {
        return NazionalitaService.numNazionalitaNonUsate()
    }// fine del metodo

    /**
     * Restituisce l'array delle riga del titolo della tabella delle attività
     * Sovrascritto
     */
    @Override
    protected ArrayList arrayTitolo() {
        ArrayList lista = new ArrayList()
        boolean usaDueColonne = Pref.getBool(LibBio.USA_DUE_COLONNE_STATISTICHE_NAZIONALITA, true)
        String ref1 = "Nelle liste le biografie sono suddivise per attività rilevanti della persona. "
        ref1 += "Se il numero di voci di un paragrafo diventa rilevante, vengono create delle sottopagine specifiche di quella attività. "
        ref1 += "Le sottopagine sono suddivise a loro volta in paragrafi alfabetici secondo l'iniziale del cognome."
        ref1 = LibWiki.setRef(ref1)
        String ref2 = "Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]]."
        ref2 = LibWiki.setRef(ref2)

        if (usaDueColonne) {
            lista.add(LibWiki.setBold('lista') + " $ref1")
            lista.add(LibWiki.setBold('categoria') + " $ref2")
        } else {
            lista.add(LibWiki.setBold('nazionalità utilizzate'))
        }// fine del blocco if-else
        lista.add(LibWiki.setBold('voci'))

        // valore di ritorno
        return lista
    }// fine del metodo

    /**
     * Singole righe della tabella
     * Sovrascritto
     */
    @Override
    protected ArrayList listaRigheUsate() {
        ArrayList listaRighe = new ArrayList()
        ArrayList<Nazionalita> listaNazionalita
        int numVoci

        listaNazionalita = NazionalitaService.getListaAllNazionalita()
        for (Nazionalita nazionalita : listaNazionalita) {
            numVoci = NazionalitaService.bioGrailsCount(nazionalita)
            if (numVoci > 0) {
                listaRighe.add(getRigaNazionalita(nazionalita, numVoci))
            }// fine del blocco if
        } // fine del ciclo for-each

        return listaRighe
    }// fine del metodo

    /**
     * Restituisce l'array delle riga del parametro per le nazionalita
     * La mappa contiene:
     *  -plurale dell'attività
     *  -numero di voci che nel campo nazionalita usano tutti records di nazionalita che hanno quel plurale
     */
    public static ArrayList getRigaNazionalita(Nazionalita nazionalita, int numVoci) {
        // variabili e costanti locali di lavoro
        ArrayList riga = new ArrayList()
        String tagCat = ':Categoria:'
        String tagListe = StatisticheService.PATH + 'Nazionalità/'
        String pipe = '|'
        String plurale = ''
        String lista
        String categoria = ''
        boolean usaDueColonne = Pref.getBool(LibBio.USA_DUE_COLONNE_STATISTICHE_NAZIONALITA, true)

        if (nazionalita) {
            plurale = nazionalita.plurale
            numVoci = NazionalitaService.bioGrailsCount(plurale)
            lista = tagListe + LibTesto.primaMaiuscola(plurale) + pipe + LibTesto.primaMinuscola(plurale)
            lista = LibWiki.setQuadre(lista)
            if (usaDueColonne) {
                categoria = tagCat + LibTesto.primaMinuscola(plurale) + pipe + plurale
                categoria = LibWiki.setQuadre(categoria)
            }// fine del blocco if
        } else {
            lista = plurale
        }// fine del blocco if-else

        //riga.add(getColore(mappa))
        riga.add(lista)
        if (usaDueColonne) {
            riga.add(categoria)
        }// fine del blocco if
        riga.add('{{formatnum' + ':' + numVoci + '}}')

        // valore di ritorno
        return riga
    } // fine della closure

    /**
     * Singole righe della tabella
     * Sovrascritto
     */
    @Override
    protected ArrayList listaRigheNonUsate() {
        ArrayList listaRighe = new ArrayList()
        ArrayList listaNazionalita

        listaNazionalita = NazionalitaService.getListaNonUsate()
        for (String nazionalita : listaNazionalita) {
            listaRighe.add(NazionalitaService.getRigaNazionalitaNonUsate(nazionalita))
        } // fine del ciclo for-each

        // valore di ritorno
        return listaRighe
    }// fine del metodo

} // fine della classe
