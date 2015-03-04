package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref

/**
 * Created by gac on 11/02/15.
 */
class StatisticheAttivita extends Statistiche {

    public StatisticheAttivita() {
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
        nomeAttNaz = 'Attività'
        inversoNomeAttNaz = 'Nazionalità'
    }// fine del metodo

    /**
     * Numero AttNaz utilizzate
     * Sovrascritto
     */
    @Override
    protected int vociUsate() {
        return AttivitaService.numAttivita()
    }// fine del metodo

    /**
     * Numero AttNaz non utilizzate
     * Sovrascritto
     */
    @Override
    protected int vociNonUsate() {
        return AttivitaService.numAttivitaNonUsate()
    }// fine del metodo

    /**
     * Restituisce l'array delle riga del titolo della tabella delle attività
     * Sovrascritto
     */
    @Override
    protected ArrayList arrayTitolo() {
        ArrayList lista = new ArrayList()
        boolean usaDueColonne = Pref.getBool(LibBio.USA_DUE_COLONNE_STATISTICHE_ATTIVITA, true)
        String ref1 = "Nelle liste le biografie sono suddivise per nazionalità della persona. "
        ref1 += "Se il numero di voci di un paragrafo diventa rilevante, vengono create delle sottopagine specifiche di quella nazionalità. "
        ref1 += "Le sottopagine sono suddivise a loro volta in paragrafi alfabetici secondo l'iniziale del cognome."
        ref1 = LibWiki.setRef(ref1)
        String ref2 = "Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]]."
        ref2 = LibWiki.setRef(ref2)

        if (usaDueColonne) {
            lista.add(SPAZIO + LibWiki.setBold('lista') + " $ref1")
            lista.add(SPAZIO + LibWiki.setBold('categoria') + " $ref2")
        } else {
            lista.add(LibWiki.setBold('attività utilizzate'))
        }// fine del blocco if-else
        lista.add(SPAZIO + SPAZIO + LibWiki.setBold('1° att'))
        lista.add(SPAZIO + SPAZIO + LibWiki.setBold('2° att'))
        lista.add(SPAZIO + SPAZIO + LibWiki.setBold('3° att'))
        lista.add(SPAZIO + SPAZIO + LibWiki.setBold('totale'))

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
        ArrayList<Attivita> listaAttivita
        int numVoci

        listaAttivita = AttivitaService.getLista()
        for (Object mappa : listaAttivita) {
            if (mappa instanceof Map) {
                numVoci = (int) mappa['totale']
                if (numVoci > 0) {
                    listaRighe.add(getRigaAttivita( mappa))
                }// fine del blocco if
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
    public ArrayList getRigaAttivita( Map mappa) {
        // variabili e costanti locali di lavoro
        ArrayList riga = new ArrayList()
        String tagCat = ':Categoria:'
        String tagListe = StatisticheService.PATH + nomeAttNaz
        String pipe = '|'
        String plurale = ''
        String lista
        String categoria = ''
        boolean usaDueColonne = Pref.getBool(LibBio.USA_DUE_COLONNE_STATISTICHE_ATTIVITA, true)
        int numAtt1 = 0
        int numAtt2 = 0
        int numAtt3 = 0
        int numTot = 0

        if (mappa) {
            plurale = mappa.plurale
            lista = tagListe + '/' + LibTesto.primaMaiuscola(plurale) + pipe + LibTesto.primaMinuscola(plurale)
            lista = LibWiki.setQuadre(lista)
            lista = SPAZIO + lista
            if (usaDueColonne) {
                categoria = tagCat + LibTesto.primaMinuscola(plurale) + pipe + plurale
                categoria = LibWiki.setQuadre(categoria)
                categoria = SPAZIO + categoria
            }// fine del blocco if
            numAtt1 = mappa.attivita
            numAtt2 = mappa.attivita2
            numAtt3 = mappa.attivita3
            numTot = numAtt1 + numAtt2 + numAtt3
        } else {
            lista = plurale
        }// fine del blocco if-else

        //riga.add(getColore(mappa))
        riga.add(lista)
        if (usaDueColonne) {
            riga.add(categoria)
        }// fine del blocco if
        riga.add(numAtt1)
        riga.add(numAtt2)
        riga.add(numAtt3)
        riga.add(numTot)

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
        ArrayList listaAttivita

        listaAttivita = AttivitaService.getListaNonUsate()
        for (String attivita : listaAttivita) {
            listaRighe.add(AttivitaService.getRigaAttivitaNonUsate( attivita))
        } // fine del ciclo for-each

        // valore di ritorno
        return listaRighe
    }// fine del metodo


} // fine della classe
