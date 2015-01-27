package it.algos.algosbio

import it.algos.algoslib.Mese
import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
abstract class ListaAnno extends ListaBio {

    static boolean transactional = false

    public ListaAnno(Anno anno, BioService bioService) {
        super(anno, bioService)
    }// fine del costruttore

    public ListaAnno(Anno anno) {
        super(anno)
    }// fine del costruttore


    public ListaAnno(String soggetto) {
        super(soggetto)
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
        usaSuddivisioneUomoDonna = false
        usaSuddivisioneParagrafi = Pref.getBool(LibBio.USA_SUDDIVISIONE_PARAGRAFI_ANNI, false)
        usaTitoloParagrafoConLink = false
        usaDoppiaColonna = true
        usaSottopagine = false
        tagLivelloParagrafo = '==='
        tagParagrafoNullo = 'senza giorno'
    }// fine del metodo

    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected void elaboraTitoloOld() {
        String titolo = ''
        String tag = getTagTitolo()
        String articolo = 'nel'
        Anno anno = getAnno()

        if (anno) {
            titolo = anno.titolo
            titolo = tag + articolo + SPAZIO + titolo
        }// fine del blocco if

        titoloPagina = titolo
    }// fine del metodo

    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected void elaboraTitolo() {
        String titolo = ''
        Anno anno = getAnno()
        String tag = getTagTitolo()
        String spazio = ' '
        String articolo = 'nel'
        String articoloBis = "nell'"
        String tagAC = ' a.C.'

        if (anno) {
            titolo = anno.titolo
            if (titolo == '1'
                    || titolo == '1' + tagAC
                    || titolo == '11'
                    || titolo == '11' + tagAC
                    || titolo.startsWith('8')
            ) {
                titolo = tag + articoloBis + titolo
            } else {
                titolo = tag + articolo + spazio + titolo
            }// fine del blocco if-else
        }// fine del blocco if

        titoloPagina = titolo
    }// fine del metodo

    /**
     * Recupera il tag specifico nati/morti
     */
    protected String getTagTitolo() {
        return ''
    }// fine del metodo

    /**
     * Voce principale a cui tornare
     * Sovrascritto
     */
    @Override
    protected String elaboraRitorno() {
        String ritorno = ''
        Anno anno = this.getAnno()

        if (anno) {
            ritorno += "{{torna a|"
            ritorno += anno.titolo
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
     * Incapsula il testo come parametro di un (eventuale) template
     * Sovrascritto
     */
    @Override
    protected String elaboraTemplate(String testoIn) {
        return elaboraTemplate(testoIn, 'Lista persone per anno')
    }// fine del metodo

    /**
     * Piede della pagina
     * Elaborazione base
     */
    protected elaboraFooter(String categoriaTxt, String natiMorti) {
        String testo = ''
        int progressivoCategoria = getAnnoProgressivo()
        String titoloPagina = titoloPagina

        testo += "<noinclude>"
        testo += aCapo
        testo += '{{Portale|biografie}}'
        testo += aCapo
        testo += "[[Categoria:${categoriaTxt}| ${progressivoCategoria}]]"
        testo += aCapo
        testo += "[[Categoria:${titoloPagina}| ]]"
        testo += aCapo
        testo += "</noinclude>"

        return testo
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
     * Recupera il singolo Anno come progressivo dall'inizio
     */
    protected int getAnnoProgressivo() {
        int annoProgressivo = 0
        Anno anno = getAnno()

        if (anno) {
            annoProgressivo = anno.progressivoCategoria
        }// fine del blocco if

        return annoProgressivo
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

    /**
     * Elabora e crea le liste dell'anno indicato (nascita e morte) e le uploada sul server wiki
     */
    public static void uploadAnno(String titolo) {
        Anno anno

        if (titolo) {
            anno = Anno.findByTitolo(titolo)
            if (anno) {
                uploadAnno(anno, null)
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo

    /**
     * Elabora e crea le liste dell'anno indicato (nascita e morte) e le uploada sul server wiki
     */
    public static boolean uploadAnno(Anno anno, BioService bioService) {
        boolean nonUsato = false

        if (anno) {
            ListaAnnoNato.uploadAnno(anno, bioService)
            ListaAnnoMorto.uploadAnno(anno, bioService)
        }// fine del blocco if

        return nonUsato
    }// fine del metodo

}// fine della classe
