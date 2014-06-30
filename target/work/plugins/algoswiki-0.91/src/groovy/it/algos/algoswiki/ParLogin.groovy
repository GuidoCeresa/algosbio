package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:25
 */

public enum ParLogin {

    firstresult('result'),
    result('result'),
    lguserid('lguserid'),
    lgusername('lgusername'),
    lgtoken('lgtoken'),
    cookieprefix('cookieprefix'),
    sessionid('sessionid'),
    token('token')

    String tag


    ParLogin(String tag) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag)
    }



    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getTag() {
        return tag;
    }

} // fine della Enumeration
