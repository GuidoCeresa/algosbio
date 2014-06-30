package it.algos.algoswiki

import it.algos.algoslib.LibTime

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 18-8-13
 * Time: 06:48
 */
public enum ParPagina {

    pageid('pageid', Integer),
    ns('ns', Integer),
    title('title', String),
    touched('touched', Date),
    lastrevid('lastrevid', Integer),
    counter('counter', Integer),
    length('length', Integer),
    starttimestamp('starttimestamp', Date),
    edittoken('edittoken', String),
    revid('revid', Integer),
    parentid('parentid', Integer),
    minor('minor', Boolean),
    user('user', String),
    timestamp('timestamp', Date),
    comment('comment', String),
    text('*', String),
    result('result', String),
    newtimestamp('newtimestamp', Date),
    newrevid('newrevid', Integer),
    oldrevid('oldrevid', Integer),
    nochange('nochange', String)

    String tag
    def classe



    ParPagina(String tag, classe) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag)
        this.setClasse(classe)
    }



    public getVal(valoreIn) {
        def valore = null
        def classe = this.getClasse()

        if (valoreIn && classe) {
            switch (classe) {
                case String:
                    if (valoreIn in String) {
                        valore = valoreIn
                    } else {
                        valore = getValNullo()
                    }// fine del blocco if-else
                    break
                case Integer:
                    if (valoreIn in String) {
                        try { // prova ad eseguire il codice;
                            valore = Integer.decode(valStringa)
                        } catch (Exception unErrore) { // intercetta l'errore
                        }// fine del blocco try-catch
                    } else {
                        if (valoreIn in Integer) {
                            valore = valoreIn
                        } else {
                            valore = getValNullo()
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                    break
                case Date:
                    if (valoreIn in String) {
                        try { // prova ad eseguire il codice;
                            valore = LibTime.getData(valoreIn)
                        } catch (Exception unErrore) { // intercetta l'errore
                        }// fine del blocco try-catch
                    } else {
                        if (valoreIn in Date) {
                            valore = valoreIn
                        } else {
                            valore = getValNullo()
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                    break
                case Boolean:
                    if (valoreIn in String) {
                        try { // prova ad eseguire il codice;
                            valore = Boolean.getBoolean(valoreIn)
                        } catch (Exception unErrore) { // intercetta l'errore
                        }// fine del blocco try-catch
                    } else {
                        if (valoreIn in Boolean) {
                            valore = valoreIn
                        } else {
                            valore = getValNullo()
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch
        }// fine del blocco if

        // valore di ritorno
        return valore
    }// fine del metodo


    public getValNullo() {
        def valore = null
        def classe = this.getClasse()

        if (classe) {
            switch (classe) {
                case String:
                    valore = ''
                    break
                case Integer:
                    valore = 0
                    break
                case Date:
                    valore = new Date(0)
                    break
                case Boolean:
                    valore = false
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch
        }// fine del blocco if

        return valore
    }



    private void setTag(String tag) {
        this.tag = tag
    }



    public String getTag() {
        return tag
    }



    private Object getClasse() {
        return classe
    }



    private void setClasse(Object classe) {
        this.classe = classe
    }

}