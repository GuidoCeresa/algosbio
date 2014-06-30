package it.algos.algoswiki
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 6-10-13
 * Time: 22:05
 */
class QueryInfoCat extends Query {

    private int size
    private int pages
    private int files
    private int subcats
    private int hidden

    public QueryInfoCat(String titoloCategoria) {
        super(titoloCategoria)
    }// fine del metodo costruttore

    /**
     * Costruisce il domain per l'URL dal titolo
     *
     * @param titolo
     * @return domain
     */
    protected String getDomain() {
        // variabili e costanti locali di lavoro
        String domain = ''
        String titolo = this.getTitolo()
        Login login = this.getLogin()
        String tag

        if (titolo && login) {
            titolo = URLEncoder.encode(titolo, Const.ENC)
            titolo = 'Category:' + titolo
            domain += Const.API_HTTP
            domain += login.getLingua()
            domain += '.'
            domain += login.getProgetto().toString()
            domain += Const.API_QUERY
            domain += '&prop=categoryinfo&titles='
            domain += titolo
            domain += Const.API_FORMAT
        }// fine del blocco if

        return domain
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        String risultatoRequest
        HashMap mappa = null
        String tagSize = Const.TAG_SIZE
        String tagPages = Const.TAG_PAGES
        String tagFiles = Const.TAG_FILES
        String tagSubcats = Const.TAG_SUBCATS
        String tagHidden = Const.TAG_HIDDEN
        int size
        int pages
        int files
        int subcats
        int hidden

        risultatoRequest = this.getRisultato()
        if (risultatoRequest) {
            mappa = (HashMap) WikiLib.getMappaJsonQuery(risultato)
        }// fine del blocco if

        if (mappa) {
            if (mappa[tagSize]) {
                size = (int) mappa[tagSize]
                this.setSize(size)
            }// fine del blocco if
            if (mappa[tagPages]) {
                pages = (int) mappa[tagPages]
                this.setPages(pages)
            }// fine del blocco if
            if (mappa[tagFiles]) {
                files = (int) mappa[tagFiles]
                this.setFiles(files)
            }// fine del blocco if
            if (mappa[tagSubcats]) {
                subcats = (int) mappa[tagSubcats]
                this.setSubcats(subcats)
            }// fine del blocco if
            if (mappa[tagHidden]) {
                hidden = (int) mappa[tagHidden]
                this.setHidden(hidden)
            }// fine del blocco if
        }// fine del blocco if

    } // fine del metodo

    int getSize() {
        return size
    }

    void setSize(int size) {
        this.size = size
    }

    int getPages() {
        return pages
    }

    void setPages(int pages) {
        this.pages = pages
    }

    int getFiles() {
        return files
    }

    void setFiles(int files) {
        this.files = files
    }

    int getHidden() {
        return hidden
    }

    void setHidden(int hidden) {
        this.hidden = hidden
    }

    int getSubcats() {
        return subcats

    }

    void setSubcats(int subcats) {
        this.subcats = subcats
    }

} // fine della classe
