package it.algos.algoswiki

import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 27-8-13
 * Time: 19:20
 */
class WrapTime {

    int pageid
    Timestamp timestamp

    public WrapTime(int pageid, Timestamp timestamp) {
        this.setPageid(pageid)
        this.setTimestamp(timestamp)
    }// fine del metodo costruttore

    int getPageid() {
        return pageid
    }

    void setPageid(int pageid) {
        this.pageid = pageid
    }

    Timestamp getTimestamp() {
        return timestamp
    }

    void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp
    }
} // fine della classe
