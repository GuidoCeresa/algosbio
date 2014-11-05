package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoswiki.Login

/**
 * Created by gac on 18/10/14.
 */
class ListaNome extends ListaBio {

    public ListaNome(Antroponimo antroponimo) {
        super(antroponimo)
    }// fine del costruttore


    public ListaNome(String soggetto) {
        super(soggetto)
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
        usaTitoloParagrafoConLink = true
        usaDoppiaColonna = false
        usaSottopagine = true
        maxVociParagrafo = 20
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
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave = attivitaPluralePerGenere(bio)

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con eventuali link
     * Sovrascritto
     */
    @Override
    protected String elaboraTitoloParagrafo(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo = chiaveParagrafo
        String singolare
        Professione professione
        BioGrails bio

        if (usaTitoloParagrafoConLink && listaVoci && listaVoci.size() > 0) {
            titoloParagrafo = '[['
            bio = listaVoci.get(0)
            if (bio) {
                singolare = bio.attivita
            }// fine del blocco if
            if (singolare) {
                professione = Professione.findBySingolare(singolare)
            }// fine del blocco if
            if (professione) {
                titoloParagrafo += LibTesto.primaMaiuscola(professione.voce)
            } else {
                titoloParagrafo += LibTesto.primaMaiuscola(singolare)
            }// fine del blocco if-else
            titoloParagrafo += '|'
            titoloParagrafo += chiaveParagrafo
            titoloParagrafo += ']]'
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * Creazione della sottopagina e del rimando
     * Sovrascritto
     */
    @Override
    protected String elaboraParagrafoSottoPagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<String> listaDidascalie) {
        String testo = ''
        String tag = tagLivelloParagrafo
        String titoloSottovoce = 'Persone di nome ' + soggetto + '/' + chiaveParagrafo

        if (titoloParagrafo) {
            testo += tag + titoloParagrafo + tag
        }// fine del blocco if

        testo += A_CAPO
        testo += "*{{Vedi anche|${titoloSottovoce}}}"
        testo += A_CAPO
        testo += A_CAPO

        //creazione della sottopagina
        new ListaNomeAttivita(titoloSottovoce)

        return testo
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


    private static String attivitaPluralePerGenere(BioGrails bio) {
        String plurale
        String singolare = bio.attivita
        String sesso = bio.sesso
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

    private static String attivitaPluralePerGenere2(String singolare, String sesso) {
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

