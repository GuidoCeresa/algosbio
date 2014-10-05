/* Created by Algos s.r.l. */
/* Date: mag 2013 */
/* Il plugin Algos ha creato o sovrascritto il templates che ha creato questo file. */
/* L'header del templates serve per controllare le successive release */
/* (tramite il flag di controllo aggiunto) */
/* Tipicamente VERRA sovrascritto (il template, non il file) ad ogni nuova release */
/* del plugin per rimanere aggiornato. */
/* Se vuoi che le prossime release del plugin NON sovrascrivano il template che */
/* genera questo file, perdendo tutte le modifiche precedentemente effettuate, */
/* regola a false il flag di controllo flagOverwrite© del template stesso. */
/* (non quello del singolo file) */
/* flagOverwrite = true */

package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algospref.Preferenze
import it.algos.algoswiki.Risultato
import it.algos.algoswiki.WikiLib

class BioGrailsService {

    def grailsApplication
    def logWikiService

    public static String aCapo = '\n'
    public static String ast = '*'

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati
    //--elabora e crea tutti gli anni modificati
    def uploadAll() {
        def giorniNati
        def giorniMorti
        def anniNati
        def anniMorti
        def giorniNatiNew
        def giorniMortiNew
        def anniNatiNew
        def anniMortiNew
        String infoOld
        String infoNew
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        long durataSec
        long durataMin

        giorniNati = Giorno.countBySporcoNato(true) + ''
        giorniMorti = Giorno.countBySporcoMorto(true) + ''
        anniNati = Anno.countBySporcoNato(true) + ''
        anniMorti = Anno.countBySporcoMorto(true) + ''
        giorniNati = LibWiki.setBold(giorniNati)
        giorniMorti = LibWiki.setBold(giorniMorti)
        anniNati = LibWiki.setBold(anniNati)
        anniMorti = LibWiki.setBold(anniMorti)
        infoOld = "Upload voci cronologiche: giorniNati=${giorniNati}, giorniMorti=${giorniMorti}, anniNati=${anniNati}, anniMorti=${anniMorti}"

        giorniNatiNew = uploadGiorniNascita() + ''
        giorniMortiNew = uploadGiorniMorte() + ''
        anniNatiNew = uploadAnniNascita() + ''
        anniMortiNew = uploadAnniMorte() + ''
        giorniNatiNew = LibWiki.setBold(giorniNatiNew)
        giorniMortiNew = LibWiki.setBold(giorniMortiNew)
        anniNatiNew = LibWiki.setBold(anniNatiNew)
        anniMortiNew = LibWiki.setBold(anniMortiNew)

        fine = System.currentTimeMillis()
        durata = fine - inizio
        durata = durata / 1000
        durataSec = durata.intValue()
        durataMin = durataSec / 60
        infoNew = "Upload voci cronologiche in ${durataMin} min.: giorniNati=${giorniNatiNew}, giorniMorti=${giorniMortiNew}, anniNati=${anniNatiNew}, anniMorti=${anniMortiNew}"
        log.info(infoOld)
        log.info(infoNew)
        logWikiService.info(infoNew)
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni modificati
    def uploadGiorni() {
        Map mappa = new HashMap()

        uploadGiorniNascita()
        uploadGiorniMorte()
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni di nascita modificati
    def int uploadGiorniNascita() {
        int giorniModificati = 0
        boolean registrata = false
        ArrayList listaGiorniModificati

        listaGiorniModificati = Giorno.findAllBySporcoNato(true)
        listaGiorniModificati?.each {
            registrata = uploadGiornoNascita((Giorno) it)
            if (registrata) {
                giorniModificati++
            }// fine del blocco if
        } // fine del ciclo each

        return giorniModificati
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea la lista del giorno di nascita
    def boolean uploadGiornoNascita(Giorno giorno) {
        boolean registrata = false
        ArrayList listaPersone = new ArrayList()
        ArrayList listaPersoneSenzaAnno
        ArrayList listaPersoneConAnno
        String querySenza
        String queryAnno
        long giornoId
        int numPersone
        String tagNatiMorti = 'Nati'
        String tagNateMorte = 'nate'

        if (giorno) {
            giornoId = giorno.id
        }// fine del blocco if

        if (giornoId) {
            querySenza = "select didascaliaGiornoNato from BioGrails where (giornoMeseNascitaLink=${giornoId} and annoNascitaLink is null) order by cognome asc"
            queryAnno = "select didascaliaGiornoNato from BioGrails where (giornoMeseNascitaLink=${giornoId} and annoNascitaLink>0) order by annoNascitaLink,cognome asc"
            listaPersoneSenzaAnno = BioGrails.executeQuery(querySenza)
            listaPersoneConAnno = BioGrails.executeQuery(queryAnno)

            listaPersoneSenzaAnno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            listaPersoneConAnno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            if (listaPersone) {
                numPersone = listaPersone.size()
                registrata = this.caricaPagina(giorno, listaPersone, tagNatiMorti, tagNateMorte)
            }// fine del blocco if
        }// fine del blocco if

        if (giorno) {
            giorno.sporcoNato = false
            giorno.save(flush: true)
        }// fine del blocco if

        return registrata
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti i giorni di morte modificati
    def int uploadGiorniMorte() {
        int giorniModificati = 0
        boolean registrata = false
        ArrayList listaGiorniModificati

        listaGiorniModificati = Giorno.findAllBySporcoMorto(true)
        listaGiorniModificati?.each {
            registrata = uploadGiornoMorte((Giorno) it)
            if (registrata) {
                giorniModificati++
            }// fine del blocco if
        } // fine del ciclo each

        return giorniModificati
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea la lista del giorno di morte
    def boolean uploadGiornoMorte(Giorno giorno) {
        boolean registrata = false
        ArrayList listaPersone = new ArrayList()
        ArrayList listaPersoneSenzaAnno
        ArrayList listaPersoneConAnno
        String querySenza
        String queryAnno
        long giornoId
        String tagNatiMorti = 'Morti'
        String tagNateMorte = 'morte'

        if (giorno) {
            giornoId = giorno.id
        }// fine del blocco if

        if (giornoId) {
            querySenza = "select didascaliaGiornoMorto from BioGrails where (giornoMeseMorteLink=${giornoId} and annoMorteLink is null) order by cognome asc"
            queryAnno = "select didascaliaGiornoMorto from BioGrails where (giornoMeseMorteLink=${giornoId} and annoMorteLink>0) order by annoMorteLink, cognome asc"
            listaPersoneSenzaAnno = BioGrails.executeQuery(querySenza)
            listaPersoneConAnno = BioGrails.executeQuery(queryAnno)
            listaPersoneSenzaAnno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            listaPersoneConAnno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            if (listaPersone) {
                registrata = this.caricaPagina(giorno, listaPersone, tagNatiMorti, tagNateMorte)
            }// fine del blocco if
        }// fine del blocco if

        if (giorno) {
            giorno.sporcoMorto = false
            giorno.save(flush: true)
        }// fine del blocco if

        return registrata
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti gli anni modificati
    def uploadAnni() {
        uploadAnniNascita()
        uploadAnniMorte()
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti gli anni di nascita modificati
    def int uploadAnniNascita() {
        int anniiModificati = 0
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        boolean registrata = false
        ArrayList listaAnniModificati
        String titolo
        Anno anno

        if (debug) {
            titolo = Pref.getStr(LibBio.ANNO_DEBUG, '1901')
            anno = Anno.findByTitolo(titolo)
            uploadAnnoNascita(anno)
        } else {
            listaAnniModificati = Anno.findAllBySporcoNato(true)
            listaAnniModificati?.each {
                registrata = uploadAnnoNascita((Anno) it)
                if (registrata) {
                    anniiModificati++
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if-else

        return anniiModificati
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea la lista dell'anno di nascita
    def boolean uploadAnnoNascita(Anno anno) {
        boolean registrata = false
        ArrayList listaPersone = new ArrayList()
        ArrayList listaPersoneSenzaGiorno
        ArrayList listaPersoneConGiorno
        String querySenza
        String queryGiorno
        long annoId
        String tagNatiMorti = 'Nati'
        String tagNateMorte = 'nate'

        if (anno) {
            annoId = anno.id
        }// fine del blocco if

        if (annoId) {
            querySenza = "select didascaliaAnnoNato from BioGrails where (annoNascitaLink=${annoId} and giornoMeseNascitaLink is null) order by cognome asc"
            queryGiorno = "select didascaliaAnnoNato from BioGrails where (annoNascitaLink=${annoId} and giornoMeseNascitaLink>0) order by giornoMeseNascitaLink,cognome asc"
            listaPersoneSenzaGiorno = BioGrails.executeQuery(querySenza)
            listaPersoneConGiorno = BioGrails.executeQuery(queryGiorno)
            listaPersoneSenzaGiorno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            listaPersoneConGiorno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            if (listaPersone) {
                registrata = caricaPagina(anno, listaPersone, tagNatiMorti, tagNateMorte)
            }// fine del blocco if
        }// fine del blocco if

        if (anno) {
            anno.sporcoNato = false
            anno.save(flush: true)
        }// fine del blocco if

        return registrata
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutti gli anni di morte modificati
    def int uploadAnniMorte() {
        int anniiModificati = 0
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        boolean registrata = false
        ArrayList listaAnniModificati
        String titolo
        Anno anno

        if (debug) {
            titolo = Pref.getStr(LibBio.ANNO_DEBUG, '1952')
            anno = Anno.findByTitolo(titolo)
            uploadAnnoMorte(anno)
        } else {
            listaAnniModificati = Anno.findAllBySporcoMorto(true)
            listaAnniModificati?.each {
                registrata = uploadAnnoMorte((Anno) it)
                if (registrata) {
                    anniiModificati++
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if-else

        return anniiModificati
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea la lista dell'anno di morte
    def boolean uploadAnnoMorte(Anno anno) {
        boolean registrata = false
        ArrayList listaPersone = new ArrayList()
        ArrayList listaPersoneSenzaGiorno
        ArrayList listaPersoneConGiorno
        String querySenza
        String queryGiorno
        long annoId
        int numPersone
        String tagNatiMorti = 'Morti'
        String tagNateMorte = 'morte'

        if (anno) {
            annoId = anno.id
        }// fine del blocco if

        if (annoId) {
            querySenza = "select didascaliaAnnoMorto from BioGrails where (annoMorteLink=${annoId} and giornoMeseMorteLink is null) order by cognome asc"
            queryGiorno = "select didascaliaAnnoMorto from BioGrails where (annoMorteLink=${annoId} and giornoMeseMorteLink>0) order by giornoMeseMorteLink,cognome asc"
            listaPersoneSenzaGiorno = BioGrails.executeQuery(querySenza)
            listaPersoneConGiorno = BioGrails.executeQuery(queryGiorno)
            listaPersoneSenzaGiorno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            listaPersoneConGiorno?.each {
                if (it && checkNameSpace(it)) {
                    listaPersone.add(it)
                }// fine del blocco if
            } // fine del ciclo each
            if (listaPersone) {
                numPersone = listaPersone.size()
                registrata = caricaPagina(anno, listaPersone, tagNatiMorti, tagNateMorte)
            }// fine del blocco if
        }// fine del blocco if

        if (anno) {
            anno.sporcoMorto = false
            anno.save(flush: true)
        }// fine del blocco if

        return registrata
    } // fine del metodo

    private static boolean checkNameSpace(String didascalia) {
        boolean valida = true
        String tagUtente = '- [[Utente:'

        if (didascalia.contains(tagUtente)) {
            valida = false
        }// fine del blocco if

        return valida
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutte le attività
    def uploadAttivita() {
    } // fine del metodo

    //--creazione delle liste partendo da BioGrails
    //--elabora e crea tutte le nazionalità
    def uploadNazionalita() {
    } // fine del metodo

    /**
     * Carica su wiki la pagina
     */
    private boolean caricaPagina(Giorno giorno, ArrayList lista, String tagNatiMorti, String tagNateMorte) {
        // variabili e costanti locali di lavoro
        boolean registrata = false
        String titolo = ''
        String testo = ''
        String summary = LibBio.getSummary()
        EditBio paginaModificata
        Risultato risultato
        boolean debug = Preferenze.getBool((String) grailsApplication.config.debug)

        // controllo di congruità
        if (giorno && lista) {
            titolo = getTitolo(giorno, tagNatiMorti)
            testo = getTesto(titolo, giorno, lista, tagNatiMorti, tagNateMorte)
        }// fine del blocco if

        if (titolo && testo) {
            if (debug) {
                titolo = 'Utente:Gac/Sandbox4280'
            }// fine del blocco if
            paginaModificata = new EditBio(titolo, testo, summary)
            risultato = paginaModificata.getRisultato()

            if ((risultato == Risultato.modificaRegistrata) || (risultato == Risultato.allineata)) {
                registrata = true
            } else {
//                log.warn "La pagina $titolo è $risultato"
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return registrata
    }// fine del metodo

    /**
     * Carica su wiki la pagina
     */
    private
    static boolean caricaPagina(Anno anno, ArrayList lista, String tagNatiMorti, String tagNateMorte) {
        // variabili e costanti locali di lavoro
        boolean registrata = false
        String titolo = ''
        String testo = ''
        String summary = LibBio.getSummary()
        EditBio paginaModificata
        Risultato risultato
        boolean debug = Pref.getBool(LibBio.DEBUG, false)

        // controllo di congruità
        if (anno && lista) {
            titolo = getTitolo(anno, tagNatiMorti)
            testo = getTesto(titolo, anno, lista, tagNatiMorti, tagNateMorte)
        }// fine del blocco if

        if (titolo && testo) {
            if (debug) {
                titolo = 'Utente:Gac/Sandbox4280'
            }// fine del blocco if
            paginaModificata = new EditBio(titolo, testo, summary)
            risultato = paginaModificata.getRisultato()

            if ((risultato == Risultato.modificaRegistrata) || (risultato == Risultato.allineata)) {
                registrata = true
            } else {
//                log.warn "La pagina $titolo è $risultato"
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return registrata
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    private static String getTitolo(Giorno giorno, String tagNatiMorti) {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String spazio = ' '
        String articolo = 'il'
        String articoloBis = "l'"

        // controllo di congruità
        if (giorno && tagNatiMorti) {
            titolo = giorno.titolo
            if (titolo.startsWith('8') || titolo.startsWith('11')) {
                titolo = tagNatiMorti + spazio + articoloBis + titolo
            } else {
                titolo = tagNatiMorti + spazio + articolo + spazio + titolo
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    private static String getTitolo(Anno anno, String tagNatiMorti) {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String spazio = ' '
        String articolo = 'nel'
        String articoloBis = "nell'"
        String tagAC = ' a.C.'

        // controllo di congruità
        if (anno && tagNatiMorti) {
            titolo = anno.titolo
            if (titolo == '1'
                    || titolo == '1' + tagAC
                    || titolo == '11'
                    || titolo == '11' + tagAC
                    || titolo.startsWith('8')
            ) {
                titolo = tagNatiMorti + spazio + articoloBis + titolo
            } else {
                titolo = tagNatiMorti + spazio + articolo + spazio + titolo
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Costruisce il testo della pagina
     */
    private
    static String getTesto(String titolo, Giorno giorno, ArrayList lista, String tagNatiMorti, String tagNateMorte) {
        // variabili e costanti locali di lavoro
        String testo = ''

        if (giorno && lista) {
            testo = getTestoTop(giorno, lista.size())
            testo += getTestoBodyGiorno(titolo, lista, tagNateMorte)
            testo += getTestoBottom(giorno, tagNatiMorti)
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Costruisce il testo della pagina
     */
    private
    static String getTesto(String titolo, Anno anno, ArrayList lista, String tagNatiMorti, String tagNateMorte) {
        // variabili e costanti locali di lavoro
        String testo = ''

        if (anno && lista) {
            testo = getTestoTop(anno, lista.size())
            testo += getTestoBodyAnno(titolo, lista, tagNateMorte)
            testo += getTestoBottom(anno, tagNatiMorti)
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina
     */
    private static String getTestoTop(Giorno giorno, int numPersone) {
        // variabili e costanti locali di lavoro
        String testo = ''
        String torna
        String dataCorrente
        String personeTxt

        // controllo di congruità
        if (giorno && numPersone) {
            dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
            personeTxt = LibTesto.formatNum(numPersone)
            torna = giorno.titolo

            testo = "<noinclude>"
            testo += aCapo
            testo += "{{ListaBio"
            testo += "|bio="
            testo += personeTxt
            testo += "|data="
            testo += dataCorrente.trim()
            testo += "}}"
            testo += aCapo
            testo += "{{torna a|$torna}}"
            testo += aCapo
            testo += "</noinclude>"
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina
     */
    private static String getTestoTop(Anno anno, int numPersone) {
        // variabili e costanti locali di lavoro
        String testo = ''
        String torna
        String dataCorrente
        String personeTxt

        // controllo di congruità
        if (anno && numPersone) {
            dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
            personeTxt = LibTesto.formatNum(numPersone)
            torna = anno.titolo

            testo = "<noinclude>"
            testo += aCapo
            testo += "{{ListaBio"
            testo += "|bio="
            testo += personeTxt
            testo += "|data="
            testo += dataCorrente.trim()
            testo += "}}"
            testo += aCapo
            testo += "{{torna a|$torna}}"
            testo += aCapo
            testo += "</noinclude>"
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Costruisce il testo variabile della pagina
     * I giorni hanno sempre un numero notevole di persone
     * quindi vanno sempre col cassetto e su due colonne
     *
     * @param lista degli elementi
     * @return testo con un ritorno a capo iniziale ed uno finale
     */
    private
    static String getTestoBody(String titolo, ArrayList lista, String tagNateMorte, boolean usaSempreCassetto, String tag) {
        // variabili e costanti locali di lavoro
        String testoBody = ''
        boolean usaDueColonne = true
        String testo = ''
        int numPersone = 0

        // testo della lista
        if (lista) {
            numPersone = lista.size()
            lista.each {
                testo += ast
                testo += it
                testo += aCapo
            }//fine di each
            testoBody = testo.trim()
        }// fine del blocco if

        // eventuale doppia colonna
        testoBody = fixTestoColonne(testoBody, numPersone)

        // eventuale cassetto
        testoBody = fixTestoCassetto(titolo, testoBody, numPersone, tagNateMorte, usaSempreCassetto, tag)

        // valore di ritorno
        return testoBody.trim()
    }// fine del metodo

    /**
     * Costruisce il testo variabile della pagina
     * I giorni hanno sempre un numero notevole di persone
     * quindi vanno sempre col cassetto e su due colonne
     *
     * @param lista degli elementi
     * @return testo con un ritorno a capo iniziale ed uno finale
     */
    private static String getTestoBodyGiorno(String titolo, ArrayList lista, String tagNateMorte) {
        return getTestoBody(titolo, lista, tagNateMorte, true, 'giorno')
    }// fine del metodo

    /**
     * Costruisce il testo variabile della pagina
     * I giorni hanno sempre un numero notevole di persone
     * quindi vanno sempre col cassetto e su due colonne
     *
     * @param lista degli elementi
     * @return testo con un ritorno a capo iniziale ed uno finale
     */
    private static String getTestoBodyAnno(String titolo, ArrayList lista, String tagNateMorte) {
        return getTestoBody(titolo, lista, tagNateMorte, true, 'anno')
    }// fine del metodo


    private static fixTestoColonne(String testoIn, int numPersone) {
        String testoOut = testoIn
        boolean usaColonne = Pref.getBool(LibBio.USA_COLONNE)
        int maxRigheColonne = Pref.getInt(LibBio.MAX_RIGHE_COLONNE)

        if (usaColonne && (numPersone > maxRigheColonne)) {
            testoOut = WikiLib.listaDueColonne(testoIn)
        }// fine del blocco if

        // valore di ritorno
        return testoOut.trim()
    }// fine del metodo

    /**
     * Inserisce l'eventuale cassetto
     */
    private
    static String fixTestoCassetto(String titolo, String testoIn, int numPersone, String tagNateMorte, boolean usaSempreCassetto, String tag) {
        // variabili e costanti locali di lavoro
        String testoOut = testoIn
        String titoloCassetto
        boolean usaCassetto = Pref.getBool(LibBio.USA_CASSETTO)
        int maxRigheCassetto = Pref.getInt(LibBio.MAX_RIGHE_CASSETTO)
        String nateMorte

        nateMorte = tagNateMorte.toLowerCase()
        nateMorte = nateMorte.substring(0, nateMorte.length() - 1).trim()
        nateMorte += 'e'
        titoloCassetto = "Lista di persone $tagNateMorte in questo $tag"

        if (usaSempreCassetto) {
//            testoOut = WikiLib.cassettoInclude(testoIn, titolo)
            testoOut = cassettoListe(titolo, numPersone, testoIn, tag)
        } else {
            if (usaCassetto && (numPersone > maxRigheCassetto)) {
//                testoOut = WikiLib.cassettoInclude(testoIn, titolo)
                testoOut = cassettoListe(titolo, numPersone, testoIn, tag)
            } else {
                testoOut = testoIn
            }// fine del blocco if-else
        }// fine del blocco if-else

        // valore di ritorno
        return testoOut.trim()
    }// fine del metodo


    private static String cassettoListe(String titolo, int numPersone, String testoIn, String tag) {
        String testoOut = testoIn

        if (testoIn) {
            testoOut = "{{Lista persone per $tag"
            testoOut += aCapo
            testoOut += '|titolo='
            testoOut += titolo
            testoOut += aCapo
            testoOut += '|voci='
            testoOut += numPersone
            testoOut += aCapo
            testoOut += '|testo='
            testoOut += aCapo
            testoOut += testoIn
            testoOut += aCapo
            testoOut += '}}'
        }// fine del blocco if

        return testoOut
    }// fine del metodo

    /**
     * Costruisce il testo finale della pagina
     */
    private static String getTestoBottom(Giorno giorno, String tagNatiMorti) {
        // variabili e costanti locali di lavoro
        String testo = ''
        String prog
        String titolo
        String tag = tagNatiMorti.toLowerCase()

        // controllo di congruità
        if (giorno) {
            prog = getProgTre(giorno)
            titolo = getTitolo(giorno, tagNatiMorti)
            testo += "<noinclude>"
            testo += aCapo
            testo += '{{Portale|biografie}}'
            testo += aCapo
            testo += "[[Categoria:Liste di ${tag} per giorno| $prog]]"
            testo += aCapo
            testo += "[[Categoria:$titolo| ]]"
            testo += aCapo
            testo += "</noinclude>"
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Costruisce il testo finale della pagina
     */
    private static String getTestoBottom(Anno anno, String tagNatiMorti) {
        // variabili e costanti locali di lavoro
        String testo = ''
        String prog
        String titolo
        String tag = tagNatiMorti.toLowerCase()

        // controllo di congruità
        if (anno) {
            prog = anno.progressivoCategoria
            titolo = getTitolo(anno, tagNatiMorti)
            testo += "<noinclude>"
            testo += aCapo
            testo += '{{Portale|biografie}}'
            testo += aCapo
            testo += "[[Categoria:Liste di ${tag} nell'anno| $prog]]"
            testo += aCapo
            testo += "[[Categoria:$titolo| ]]"
            testo += aCapo
            testo += "</noinclude>"
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine del metodo

    /**
     * Stringa progressivo del giorno nell'anno
     * Tre cifre (per omogeneità nell'ordinamento della categoria)
     *
     * Utilizzo l'anno bisestile per essere sicuro di prenderli comunque tutti
     */
    private static String getProgTre(Giorno giorno) {
        // variabili e costanti locali di lavoro
        String progTre = ''
        int cifre = 3
        int prog
        String tagIni = '0'

        if (giorno) {
            prog = giorno.bisestile
            progTre = prog + ''
            while (progTre.length() < cifre) {
                progTre = tagIni + progTre
            }// fine di while
        }// fine del blocco if

        // valore di ritorno
        return progTre
    }// fine del metodo

} // fine della service classe
