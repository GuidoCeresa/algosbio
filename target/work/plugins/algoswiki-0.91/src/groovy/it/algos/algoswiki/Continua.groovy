package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 9-11-12
 * Time: 09:46
 */
class Continua {

    private String type
    private String hexsortkey
    private int nextPageId
    private String nextTitolo
    private String sortkeyprefix

    Continua(String nextTitolo, String sortkeyprefix, int nextPageId) {
        this.nextTitolo = fixTitolo(nextTitolo)
        this.sortkeyprefix = fixTitolo(sortkeyprefix)
        this.nextPageId = nextPageId
    } // fine del costruttore

    Continua(String parametroRestituitoDallaRequest) {
        String tagStr = "|"
        String tagSplit = "\\|"
        def parti
        String type
        String hexsortkey
        String pageidTxt
        int nextPageId
        String nextTitolo

        if (parametroRestituitoDallaRequest.contains(tagStr)) {
            parti = parametroRestituitoDallaRequest.split(tagSplit)

            type = parti[0]
            if (type) {
                this.setType(type)
            }// fine del blocco if

            hexsortkey = parti[1]
            if (hexsortkey) {
                this.setHexsortkey(hexsortkey)
            }// fine del blocco if

            pageidTxt = parti[2]
            if (pageidTxt) {
                nextPageId = Integer.decode(pageidTxt)
                if (nextPageId) {
                    this.setNextPageId(nextPageId)
                    nextTitolo = QueryVocePageid.leggeTitolo(nextPageId)
                    nextTitolo = fixTitolo(nextTitolo)
                    this.setNextTitolo(nextTitolo)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

    } // fine del costruttore


    public String fixTitolo(String titoloIn) {
        String titoloOut = titoloIn

        if (titoloIn) {
            if (titoloOut.contains('_')) {
                titoloOut = titoloOut.replaceAll('_', '+')
            }// fine del blocco if
            if (titoloOut.contains(' ')) {
                titoloOut = titoloOut.replaceAll(' ', '+')
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return titoloOut
    } // fine del metodo

    public String get() {
        String tag
        String pipe = '|'

        tag = this.type
        tag += pipe
        tag += this.hexsortkey
        tag += pipe
        tag += this.nextPageId

        // valore di ritorno
        return tag
    } // fine del metodo

    String getType() {
        return type
    }

    void setType(String type) {
        this.type = type
    }

    String getHexsortkey() {
        return hexsortkey
    }

    void setHexsortkey(String hexsortkey) {
        this.hexsortkey = hexsortkey
    }

    int getNextPageId() {
        return nextPageId
    }

    void setNextPageId(int nextPageId) {
        this.nextPageId = nextPageId
    }

    String getNextTitolo() {
        return nextTitolo
    }

    void setNextTitolo(String nextTitolo) {
        this.nextTitolo = nextTitolo
    }

    String getSortkeyprefix() {
        return sortkeyprefix
    }

    void setSortkeyprefix(String sortkeyprefix) {
        this.sortkeyprefix = sortkeyprefix
    }
} // fine della classe
