package it.algos.algosbio

import it.algos.algoslib.Mese
import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
abstract class ListaAnno extends ListaCrono {

    static boolean transactional = false


    public ListaAnno(Anno anno) {
        super(anno)
    }// fine del costruttore


    public ListaAnno(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    public ListaAnno(Anno anno, BioService bioService) {
        super(anno, bioService)
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
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        super.elaboraParametri()
        usaSuddivisioneParagrafi = Pref.getBool(LibBio.USA_SUDDIVISIONE_PARAGRAFI_ANNI, false)
        if (!Pref.getBool(LibBio.USA_PARAGRAFO_PUNTI_GIORNI_ANNI, true)) {
            tagParagrafoNullo = 'senza giorno'
        }// fine del blocco if
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

    protected String getParagrafoDidascalia(ArrayList<String> listaDidascalie) {
        String testo = ''
        String didascalia
        String tag = ']]'
        String tagSep = tag + ' -'
        int pos
        String giornoOld = ''
        String giornoTmp

        if (Pref.getBool(LibBio.USA_GIORNI_RAGGRUPPATI, true)) {
            listaDidascalie?.each {
                didascalia = it
                if (didascalia.contains(tagSep)) {
                    pos = didascalia.indexOf(tag)
                    pos += tag.length()
                    giornoTmp = didascalia.substring(0, pos)
                    giornoTmp = giornoTmp.trim()
                    if (!giornoTmp.equals(giornoOld)) {
                        testo += '*'
                        testo += giornoTmp
                        testo += aCapo
                    }// fine del blocco if
                    pos = didascalia.indexOf(tagSep)
                    pos += tagSep.length()
                    didascalia = didascalia.substring(pos)
                    didascalia = didascalia.trim()
                    giornoOld = giornoTmp
                    testo += '*'
                }// fine del blocco if
                testo += '*'
                testo += didascalia
                testo += aCapo
            }// fine del ciclo each
        } else {
            testo = super.getParagrafoDidascalia(listaDidascalie)
        }// fine del blocco if-else

        return testo.trim()
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

        return finale(testo)
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
    public static boolean uploadAnno(Anno anno, BioService bioService) {
        boolean nonUsato = false

        if (anno) {
            ListaAnnoNato.uploadAnno(anno, bioService)
            ListaAnnoMorto.uploadAnno(anno, bioService)
        }// fine del blocco if

        return nonUsato
    }// fine del metodo

}// fine della classe
