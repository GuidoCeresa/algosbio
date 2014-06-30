package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 6-11-12
 * Time: 07:16
 */

import java.sql.Timestamp

// Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
// Quattro (4) parametri letti SEMPRE: titolo, pageid, ns, testo
// Nove (9) ulteriori parametri letti solo se serve: contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat
class PaginaRev extends Pagina {

    // format wiki
    private String contentformat

    // revisione (il tag della query è "ids")
    private int revid

    // The rev_id of the previous revision to the page  (il tag della query è "ids")
    private int parentid

    // The date and time the revision was made
    private Timestamp timestamp

    // modifica minore (il tag della query è "flags")
    private boolean minor

    // utente ultima modifica
    private String user

    // userid ultima modifica
    private int userid

    // dimensioni del contenuto
    private int size

    // oggetto della modifica
    private String comment


    public PaginaRev(String titolo) {
        super(titolo)
        Query query
        HashMap mappa

        query = new QueryRev(titolo)
        mappa = query.mappa
        this.inizializza(mappa)
    }// fine del metodo costruttore

    public PaginaRev(int pageid) {
        super(pageid)
        Query query
        HashMap mappa

        query = new QueryRev(pageid)
        mappa = query.mappa
        this.inizializza(mappa)
    }// fine del metodo costruttore

    public PaginaRev(HashMap mappa) {
        super(mappa)
    }// fine del metodo costruttore

    protected inizializza(HashMap mappa) {
        super.inizializza(mappa)

        if (mappa) {

            if (mappa[Const.TAG_NS]) {
                this.setNs((Integer) mappa[Const.TAG_NS])
            }// fine del blocco if
            if (mappa[Const.TAG_CONTENT_FORMAT]) {
                this.setContentFormat((String) mappa[Const.TAG_CONTENT_FORMAT])
            }// fine del blocco if
            if (mappa[Const.TAG_USER]) {
                this.setUser((String) mappa[Const.TAG_USER])
            }// fine del blocco if
            if (mappa[Const.TAG_USER_ID]) {
                this.setUserid((Integer) mappa[Const.TAG_USER_ID])
            }// fine del blocco if
            if (mappa[Const.TAG_SIZE]) {
                this.setSize((Integer) mappa[Const.TAG_SIZE])
            }// fine del blocco if
            if (mappa[Const.TAG_COMMENT]) {
                this.setComment((String) mappa[Const.TAG_COMMENT])
            }// fine del blocco if
            if (mappa[Const.TAG_REV_ID]) {
                this.setRevid((Integer) mappa[Const.TAG_REV_ID])
            }// fine del blocco if
            if (mappa[Const.TAG_PARENT_ID]) {
                this.setParentid((Integer) mappa[Const.TAG_PARENT_ID])
            }// fine del blocco if
            if (mappa[Const.TAG_MINOR]) {
                this.setMinor((Boolean) mappa[Const.TAG_MINOR])
            }// fine del blocco if
            if (mappa[Const.TAG_TIMESTAMP]) {
                this.setTimestamp((Timestamp) mappa[Const.TAG_TIMESTAMP])
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo costruttore


    String getContentFormat() {
        return contentformat
    }

    void setContentFormat(String contentformat) {
        this.contentformat = contentformat
    }

    public String getUser() {
        return user
    }

    void setUser(String user) {
        this.user = user
    }

    public int getUserid() {
        return userid
    }

    void setUserid(int userid) {
        this.userid = userid
    }

    int getRevid() {
        return revid
    }

    void setRevid(int revid) {
        this.revid = revid
    }

    int getParentid() {
        return parentid
    }

    void setParentid(int parentid) {
        this.parentid = parentid
    }

    Timestamp getTimestamp() {
        return timestamp
    }

    void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp
    }

    boolean getMinor() {
        return minor
    }

    void setMinor(boolean minor) {
        this.minor = minor
    }

    int getSize() {
        return size
    }

    void setSize(int size) {
        this.size = size
    }

    String getComment() {
        return comment
    }

    void setComment(String comment) {
        this.comment = comment
    }

} //fine della classe
