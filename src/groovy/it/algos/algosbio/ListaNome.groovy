package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoswiki.Login

/**
 * Created by gac on 18/10/14.
 */
class ListaNome extends ListaBio {
    private boolean titoloParagrafoConLink = true

    public ListaNome(Antroponimo antroponimo, Login login) {
        super(antroponimo, login)
    }// fine del costruttore


    public ListaNome(String soggetto, Login login) {
        super(soggetto, login)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Antroponimo antroponimo = Antroponimo.findByNome(LibTesto.primaMaiuscola(soggetto))

        if (antroponimo) {
            oggetto = antroponimo
        }// fine del blocco if
    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = true
        tagTemplateBio = 'StatBio'
        usaSuddivisioneParagrafi = true
        titoloParagrafoConLink = true
        usaDoppiaColonna = false
        usaSottopagine = false
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = 'Altre...'
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        return 'Persone di nome ' + getNome()
    }// fine del metodo

    /**
     * Pagina principale a cui tornare
     * Sovrascritto
     */
    @Override
    protected String elaboraincipit() {
        String ritorno = ''
        String nome = this.getNome()

        if (nome) {
            ritorno = "{{incipit lista nomi|nome=${nome}}}"
        }// fine del blocco if

        return ritorno
    }// fine del metodo

    /**
     * Utilizza la didascalia prevista per il tipo di pagina in elaborazione
     * Sovrascritto
     */
    @Override
    protected String estraeDidascalia(BioGrails bio) {
        return bio.didascaliaListe
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiave(BioGrails bio) {
        String chiave = getAttivita(bio)

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected elaboraFooter() {
        String testo = ''
        String nome = getNome()

        testo += "<noinclude>"
        testo += "[[Categoria:Liste di persone per nome|${nome}]]"
        testo += "</noinclude>"

        return testo
    }// fine del metodo

    /**
     * Recupera il singolo Antroponimo
     */
    private Antroponimo getAntroponimo() {
        Antroponimo antroponimo = null

        if (oggetto && oggetto instanceof Antroponimo) {
            antroponimo = (Antroponimo) oggetto
        }// fine del blocco if

        return antroponimo
    }// fine del metodo

    /**
     * Recupera il singolo nome
     */
    private String getNome() {
        String nome = ''
        Antroponimo antroponimo = getAntroponimo()

        if (antroponimo) {
            nome = antroponimo.nome
        }// fine del blocco if

        return nome
    }// fine del metodo

    // restituisce il nome dell'attività
    // restituisce il plurale
    // restituisce il primo carattere maiuscolo
    // aggiunge un link alla voce di riferimento
    private String getAttivita(BioGrails bio) {
        String attivitaLinkata = ''
        String singolare
        boolean link = titoloParagrafoConLink
        String attivita
        String genere
        Professione professione

        if (bio) {
            singolare = bio.attivita
            genere = bio.sesso
            if (singolare) {
                attivita = attivitaPluralePerGenere(singolare, genere)
                if (attivita) {
                    if (link) {
                        professione = Professione.findBySingolare(singolare)
                        attivitaLinkata = '[['
                        if (professione) {
                            attivitaLinkata += LibTesto.primaMaiuscola(professione.voce)
                        } else {
                            attivitaLinkata += LibTesto.primaMaiuscola(singolare)
                        }// fine del blocco if-else
                        attivitaLinkata += '|'
                        attivitaLinkata += attivita
                        attivitaLinkata += ']]'
                    } else {
                        attivitaLinkata = attivita
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return attivitaLinkata
    }// fine del metodo

    private static String attivitaPluralePerGenere(String singolare, String sesso) {
        String plurale
        Genere genere = Genere.findBySingolareAndSesso(singolare, sesso)

        if (genere) {
            plurale = genere.plurale
        }// fine del blocco if

        if (plurale) {
            plurale = LibTesto.primaMaiuscola(plurale)
            plurale = plurale.trim()
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Antroponimo antroponimo = getAntroponimo()

        if (antroponimo) {
            listaBiografie = BioGrails.findAllByNomeLink(antroponimo, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
        def stop
    }// fine del metodo

}// fine della classe

