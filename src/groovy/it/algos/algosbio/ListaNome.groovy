package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref
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

    public ListaNome(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
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
        maxVociParagrafo = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_ANTROPONIMI, 50)
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = 'Altre...'
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected elaboraTitolo() {
        if (!titoloPagina) {
            titoloPagina = 'Persone di nome ' + soggetto
        }// fine del blocco if
    }// fine del metodo

    /**
     * Voce principale a cui tornare
     * Sovrascritto
     */
    @Override
    protected String elaboraRitorno() {
        String testo = ''

        if (titoloPaginaMadre) {
            testo = "{{Torna a|" + titoloPaginaMadre + "}}"
        }// fine del blocco if

        return testo
    }// fine del metodo

    /**
     * Pagina principale a cui tornare
     * Sovrascritto
     */
    @Override
    protected String elaboraIncipit() {
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

        if (usaTitoloParagrafoConLink && !chiaveParagrafo.equals(tagParagrafoNullo) && listaVoci && listaVoci.size() > 0) {
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
     * Creazione della sottopagina
     * Sovrascritto
     */
    @Override
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaNomeAttivita(elaboraSoggettoSpecifico(chiaveParagrafo), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.elaboraPagina()
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo) {
        return 'Persone di nome ' + elaboraSoggettoSpecifico(chiaveParagrafo)
    }// fine del metodo

    /**
     * Elabora soggetto specifico
     */
    protected String elaboraSoggettoSpecifico(String chiaveParagrafo) {
        return soggetto + '/' + chiaveParagrafo
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected elaboraFooter() {
        String testo = ''
        String nome = soggetto

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
        String tagSpazio = ' '
        String tagWillCard = '%'
        String nomeSemplice = soggetto
        String nomeWillCardA = nomeSemplice + tagSpazio + tagWillCard
        String nomeWillCardB = tagWillCard + tagSpazio + nomeSemplice

        listaBiografie = BioGrails.findAllByNomeLink(antroponimo, [sort: 'forzaOrdinamento'])
          def  listaBiografie2 = BioGrails.findAllByNomeLikeOrNomeLikeOrNomeLike(nomeSemplice, nomeWillCardA, nomeWillCardB, [sort: 'forzaOrdinamento'])

//        if (usaNomeDoppio) {
//            listaBiografie = BioGrails.findAllByNomeLikeOrNomeLikeOrNomeLike(nomeSemplice, nomeWillCardA, nomeWillCardB, [sort: 'forzaOrdinamento'])
//        } else {
//            if (antroponimo) {
//            }// fine del blocco if
//        }// fine del blocco if-else

        def stop
    }// fine del metodo

}// fine della classe

