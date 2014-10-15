package it.algos.algosbio

import it.algos.algoslib.LibTesto

/**
 * Created by gac on 15/10/14.
 */
public enum Secolo {
    XXac('XX', 2000, 1901, false),
    XIXac('XIX', 1900, 1801, false),
    XVIIIac('XVII', 1800, 1701, false),
    XVIIac('XVII', 1700, 1601, false),
    XVIac('XVI', 1600, 1501, false),
    XVac('XV', 1500, 1401, false),
    XIVac('XIV', 1400, 1301, false),
    XIIIac('XIII', 1300, 1201, false),
    XIIac('XII', 1200, 1101, false),
    XIac('XI', 1100, 1001, false),
    Xac('X', 1000, 901, false),
    IXac('IX', 900, 801, false),
    VIIIac('VIII', 800, 701, false),
    VIIac('VII', 700, 601, false),
    VIac('VI', 600, 501, false),
    Vac('V', 500, 401, false),
    IVac('IV', 400, 301, false),
    IIIac('III', 300, 201, false),
    IIac('II', 200, 101, false),
    Iac('I', 100, 1, false),
    I('I', 1, 100, true),
    II('II', 101, 200, true),
    III('III', 201, 300, true),
    IV('IV', 301, 400, true),
    V('V', 401, 500, true),
    VI('VI', 501, 600, true),
    VII('VII', 601, 700, true),
    VIII('VIII', 701, 800, true),
    IX('IX', 801, 900, true),
    X('X', 901, 1000, true),
    XI('XI', 1001, 1100, true),
    XII('XII', 1101, 1200, true),
    XIII('XIII', 1201, 1300, true),
    XIV('XIV', 1301, 1400, true),
    XV('XV', 1401, 1500, true),
    XVI('XVI', 1501, 1600, true),
    XVII('XVII', 1601, 1700, true),
    XVIII('XVIII', 1701, 1800, true),
    XIX('XIX', 1801, 1900, true),
    XX('XX', 1901, 2000, true),
    XXI('XXI', 2001, 2100, true)

    String titolo
    int inizio
    int fine
    boolean dopoCristo

    private static String SECOLO_AC = ' secolo a.C.'
    private static String SECOLO_DC = ' secolo'

    /**
     * Costruttore completo con parametri.
     *
     * @param tag utilizzato per chiarezza
     * @param description usato solo qui
     */
    Secolo(String titolo, int inizio, int fine, boolean dopoCristo) {
        if (dopoCristo) {
            this.setTitolo(titolo + ' secolo')
        } else {
            this.setTitolo(titolo + ' secolo a.C.')
        }// fine del blocco if-else
        this.setInizio(inizio)
        this.setFine(fine)
        this.setDopoCristo(dopoCristo)
    } // fine del costruttore


    public static String getSecolo(String annoTxt) {
        String tagAC = 'a.C.'
        boolean dopoCristo = true
        int anno

        if (annoTxt.contains(tagAC)) {
            annoTxt = LibTesto.levaCoda(annoTxt, tagAC)
            dopoCristo = false
        }// fine del blocco if
        try { // prova ad eseguire il codice
            anno = Integer.decode(annoTxt)
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (anno) {
            if (dopoCristo) {
                return getSecoloDC(anno)
            } else {
                return getSecoloAC(anno)
            }// fine del blocco if-else
        } else {
            return ''
        }// fine del blocco if-else
    }// fine del metodo

    public static String getSecoloDC(int anno) {
        String nome = ''
        def secoli = values()
        Secolo secolo
        int inizio
        int fine

        secoli?.each {
            secolo = (Secolo) it
            if (secolo.dopoCristo) {
                inizio = secolo.inizio
                fine = secolo.fine
                if (anno > inizio && anno < fine) {
                    nome = secolo.titolo
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo each

        return nome
    }// fine del metodo

    public static String getSecoloAC(int anno) {
        String nome = ''
        def secoli = values()
        Secolo secolo
        int inizio
        int fine

        secoli?.each {
            secolo = (Secolo) it
            if (!secolo.dopoCristo) {
                inizio = secolo.inizio
                fine = secolo.fine
                if (anno > fine && anno < inizio) {
                    nome = secolo.titolo
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo each

        return nome
    }// fine del metodo

    String getTitolo() {
        return titolo
    }

    void setTitolo(String titolo) {
        this.titolo = titolo
    }

    int getInizio() {
        return inizio
    }

    void setInizio(int inizio) {
        this.inizio = inizio
    }

    int getFine() {
        return fine
    }

    void setFine(int fine) {
        this.fine = fine
    }

    boolean getDopoCristo() {
        return dopoCristo
    }

    void setDopoCristo(boolean dopoCristo) {
        this.dopoCristo = dopoCristo
    }
} // fine della Enumeration
