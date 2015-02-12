package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki

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
     * Tabella AttNaz utilizzate
     * Sovrascritto
     */
    @Override
    protected String creaTabellaUsate() {
        return ''
    }// fine del metodo

    /**
     * Tabella AttNaz non utilizzate
     * Sovrascritto
     */
    @Override
    protected String creaTabellaNonUsate() {
        return ''
    }// fine del metodo


} // fine della classe
