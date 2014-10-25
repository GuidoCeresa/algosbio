package it.algos.algosbio

import it.algos.algoslib.Mese

/**
 * Created by gac on 17/10/14.
 */
abstract class ListaGiorno extends ListaBio {


    public ListaGiorno(Giorno giorno) {
        super(giorno)
    }// fine del costruttore

    public ListaGiorno(String soggetto) {
        super(soggetto)
    }// fine del costruttore


    public ListaGiorno(String soggetto, boolean loggato) {
        super(soggetto, loggato)
    }// fine del costruttore


    @Override
    protected elaboraOggetto(String soggetto) {
        Giorno giorno = Giorno.findByTitolo(soggetto)

        if (giorno) {
            oggetto = giorno
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
        tagParagrafoNullo = 'senza anno'
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo
        String tag = getTagTitolo()
        String articolo = 'il'
        String articoloBis = "l'"
        Giorno giorno = getGiorno()

        if (giorno) {
            titolo = giorno.titolo
        }// fine del blocco if

        if (titolo) {
            if (titolo.startsWith('8') || titolo.startsWith('11')) {
                titolo = tag + articoloBis + titolo
            } else {
                titolo = tag + articolo + SPAZIO + titolo
            }// fine del blocco if-else
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
    protected String elaboraincipit() {
        String ritorno = ''
        Giorno giorno = this.getGiorno()

        if (giorno) {
            ritorno += "{{torna a|"
            ritorno += giorno.titolo
            ritorno += "}}"
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
        Secolo secolo
        String titolo

        Secolo.values()?.each {
            secolo = (Secolo) it
            titolo = secolo.titolo
            if (listaChiaviIn.contains(titolo)) {
                listaChiaviOut.add(titolo)
            }// fine del blocco if
        } // fine del ciclo each

        // valore di ritorno
        return listaChiaviOut
    }// fine della closure

    /**
     * Incapsula il testo come parametro di un (eventuale) template
     * Sovrascritto
     */
    @Override
    protected String elaboraTemplate(String testoIn) {
        return elaboraTemplate(testoIn, 'Lista persone per giorno')
    }// fine del metodo

    /**
     * Piede della pagina
     * Elaborazione base
     */
    protected elaboraFooter(String categoriaTxt) {
        String testo = ''
        String giornoOrdinamento = getGiornoOrdinamento()
        String titoloPagina = getTitolo()

        testo += "<noinclude>"
        testo += A_CAPO
        testo += '{{Portale|biografie}}'
        testo += A_CAPO
        testo += "[[Categoria:${categoriaTxt}| ${giornoOrdinamento}]]"
        testo += A_CAPO
        testo += "[[Categoria:${titoloPagina}| ]]"
        testo += A_CAPO
        testo += "</noinclude>"

        return testo
    }// fine del metodo

    /**
     * Recupera il singolo Giorno come numero
     */
    protected int getGiornoNumero() {
        int giornoNumero = 0
        Giorno giorno = getGiorno()

        if (giorno) {
            giornoNumero = giorno.bisestile
        }// fine del blocco if

        return giornoNumero
    }// fine del metodo

    /**
     * Recupera il singolo Giorno come ordinamento
     * Comprende il 29 febbraio per gli anni bisestili
     */
    protected String getGiornoOrdinamento() {
        String giornoTxt = ''
        int giornoNumero = 0
        Giorno giorno = getGiorno()
        String tag = '0'

        if (giorno) {
            giornoNumero = giorno.bisestile
        }// fine del blocco if

        if (giornoNumero) {
            giornoTxt = '' + giornoNumero
        }// fine del blocco if

        //--completamento a 3 cifre
        if (giornoTxt) {
            if (giornoTxt.length() == 1) {
                giornoTxt = tag + giornoTxt
            }// fine del blocco if
            if (giornoTxt.length() == 2) {
                giornoTxt = tag + giornoTxt
            }// fine del blocco if
        }// fine del blocco if

        return giornoTxt
    }// fine del metodo

    /**
     * Recupera il singolo Giorno
     */
    protected Giorno getGiorno() {
        Giorno giorno = null

        if (oggetto && oggetto instanceof Giorno) {
            giorno = (Giorno) oggetto
        }// fine del blocco if

        return giorno
    }// fine del metodo

}// fine della classe
