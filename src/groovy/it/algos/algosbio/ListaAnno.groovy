package it.algos.algosbio

import it.algos.algoslib.Mese

/**
 * Created by gac on 18/10/14.
 */
abstract class ListaAnno extends ListaBio {

    public ListaAnno(String soggetto) {
        this(soggetto, false)
    }// fine del costruttore

    public ListaAnno(String soggetto, boolean loggato) {
        super(soggetto, loggato)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Anno anno = Anno.findByTitolo(soggetto)

        if (anno) {
            oggetto = anno
        }// fine del blocco if
    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = false
        usaSuddivisioneParagrafi = true
        usaDoppiaColonna = true
        usaSottopagine = false
        tagLivelloParagrafo = '==='
        tagParagrafoNullo = 'senza giorno'
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = getTagTitolo()
        String articolo = 'nel'
        Anno anno = getAnno()

        if (anno) {
            titolo = anno.titolo
            titolo = tag + articolo + SPAZIO + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Recupera il tag specifico nati/morti
     */
    protected String getTagTitolo() {
        return ''
    }// fine del metodo

    /**
     * Pagina principale a cui tornare
     * Sovrascritto
     */
    @Override
    protected String elaboraRitornoPrincipale() {
        String ritorno = ''
        Anno anno = this.getAnno()

        if (anno) {
            ritorno = anno.titolo
        }// fine del blocco if

        return ritorno
    }// fine del metodo

    /**
     * Ordina le chiavi di una mappa
     * Sovrascritto
     *
     * @param chiavi non ordinate
     * @return chiavi ordinate
     */
    @Override
    protected ArrayList<String> ordinaChiavi(ArrayList<String> listaChiaviIn) {
        ArrayList<String> listaChiaviOut = new ArrayList<String>()
        ArrayList mesi = Mese.allLongList

        mesi?.each {
            if (listaChiaviIn.contains(it)) {
                listaChiaviOut.add((String) it)
            }// fine del blocco if
        } // fine del ciclo each

        // valore di ritorno
        return listaChiaviOut
    }// fine della closure

    /**
     * Recupera il singolo Anno come numero di ordinamento
     */
    protected int getAnnoNumeroOrdinamento() {
        return getAnnoNumero() + 2000
    }// fine del metodo

    /**
     * Recupera il singolo Anno come numero
     */
    protected int getAnnoNumero() {
        int annoNumero = 0
        Anno anno = getAnno()

        if (anno) {
            try { // prova ad eseguire il codice
                annoNumero = Integer.decode(anno.titolo)
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        return annoNumero
    }// fine del metodo

    /**
     * Recupera il singolo Anno
     */
    protected Anno getAnno() {
        Anno anno = null

        if (oggetto && oggetto instanceof Anno) {
            anno = (Anno) oggetto
        }// fine del blocco if

        return anno
    }// fine del metodo


}// fine della classe
