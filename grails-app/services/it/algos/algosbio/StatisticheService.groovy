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

import it.algos.algoslib.LibArray
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algospref.LibPref
import it.algos.algospref.Pref
import it.algos.algospref.Preferenze
import it.algos.algospref.Type
import it.algos.algoswiki.Edit
import it.algos.algoswiki.TipoAllineamento
import it.algos.algoswiki.WikiLib

class StatisticheService {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def bioGrailsService
    def giornoService
    def annoService
    def bioService
    def attivitaService
    def nazionalitaService

    public static String PATH = 'Progetto:Biografie/'
    private static String A_CAPO = '\n'
    private static HashMap mappaSintesi = new HashMap()
    private static int NUOVA_ATTESA = 5
    private static String SPAZIO = '&nbsp;'
    private static boolean USA_SPAZI = true

    /**
     * Aggiorna la pagina wiki di servizio delle attività
     */
    public attivitaUsate() {
        new StatisticheAttivita()
    }// fine del metodo

    /**
     * Aggiorna la pagina wiki di servizio delle nazionalità
     */
    public nazionalitaUsate() {
        new StatisticheNazionalita()
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina statistiche
     */
    private static String getTestoTop() {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())

        // controllo di congruità
        if (dataCorrente) {
            testo += '<noinclude>'
            testo += "{{StatBio|data=$dataCorrente}}"
            testo += '</noinclude>'
            testo += A_CAPO
        }// fine del blocco if

        // valore di ritorno
        return testo
    }// fine del metodo








    /**
     * Restituisce l'array delle riga del titolo della tabella delle attività
     */
    private ArrayList getArrayTitolo(AttivitaNazionalita tipoAttNaz) {
        def lista = new ArrayList()
        String tagSpazio = '&nbsp;'
        boolean usaDueColonne = Pref.getBool(LibBio.USA_DUE_COLONNE_STATISTICHE_NAZIONALITA, false)
        String ref1 = "Nelle liste le biografie sono suddivise per attività rilevanti della persona. "
        ref1 += "Se il numero di voci di un paragrafo diventa rilevante, vengono create delle sottopagine specifiche di quella attività. "
        ref1 += "Le sottopagine sono suddivise a loro volta in paragrafi alfabetici secondo l'iniziale del cognome."
        ref1 = LibWiki.setRef(ref1)
        String ref2 = "Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]]."
        ref2 = LibWiki.setRef(ref2)

        switch (tipoAttNaz) {
            case AttivitaNazionalita.attivita:
                lista.add(LibWiki.setBold('#'))
                lista.add(LibWiki.setBold('attività utilizzate'))
                lista.add(LibWiki.setBold('prima'))
                lista.add(LibWiki.setBold('seconda'))
                lista.add(LibWiki.setBold('terza'))
                lista.add(LibWiki.setBold('totale'))
                break
            case AttivitaNazionalita.nazionalita:
                lista.add(LibWiki.setBold(tagSpazio + tagSpazio + tagSpazio + tagSpazio + '#'))
                if (usaDueColonne) {
                    lista.add(tagSpazio + LibWiki.setBold('lista') + " $ref1")
                    lista.add(tagSpazio + LibWiki.setBold('categoria') + " $ref2")
                } else {
                    lista.add(LibWiki.setBold('nazionalità utilizzate'))
                }// fine del blocco if-else
                lista.add(LibWiki.setBold(tagSpazio + tagSpazio + tagSpazio + tagSpazio + 'voci'))
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        // valore di ritorno
        return lista
    }// fine del metodo

    public enum AttivitaNazionalita {
        attivita, nazionalita
    } // fine della Enumeration

    /**
     * Crea la tabella
     *
     * Legge la terza colonna della tabella esistente
     * Recupera i dati per costruire la terza colonna
     * Elabora i dati per costruire la quarta colonna
     */
    def paginaSintesi() {
        String titolo = PATH + 'Statistiche'
        String testo = ''
        String summary = Pref.getStr(LibBio.SUMMARY)
        def nonServe
        boolean debug = Pref.getBool(LibBio.DEBUG, true)
        testo += getTestoTop()
        testo += getTestoBodySintesi()
        testo += getTestoBottomSintesi()

        if (debug) {
            titolo = 'Utente:Biobot/2'
        }// fine del blocco if

        if (titolo && testo && summary) {
            nonServe = new Edit(titolo, testo, summary)
            if (!debug) {
                registraPreferenze()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo


    def getTestoBodySintesi() {
        def mappa = new HashMap()
        def lista = new ArrayList()

        lista.add(getRigaVoci())
        lista.add(getRigaGiorni())
        lista.add(getRigaAnni())
        lista.add(getRigaAttivita())
        lista.add(getRigaNazionalita())
        lista.add(getRigaAttesa())

        mappa.put('width', '50')
        mappa.put('sortable', false)
        mappa.put('titoli', getTitoliSintesi())
        mappa.put('align', TipoAllineamento.right)
        mappa.put('lista', lista)
        return WikiLib.creaTabellaSortable(mappa)
    } // fine della closure

    /**
     * Titoli della tabella di sintesi
     */
    private static getTitoliSintesi() {
        def titoli
        String statistiche = 'statistiche'
        String vecchiaData = Pref.getStr(LibBio.ULTIMA_SINTESI)
        String nuovaData = LibTime.getGioMeseAnnoLungo(new Date())
        String differenze = 'diff.'

        //valore per le preferenze
        mappaSintesi.put(LibBio.ULTIMA_SINTESI, nuovaData)

        nuovaData = LibWiki.setBold(nuovaData)

        statistiche = regolaSpazi(statistiche)
        vecchiaData = regolaSpazi(vecchiaData)
        nuovaData = regolaSpazi(nuovaData)
        differenze = regolaSpazi(differenze)

        titoli = [statistiche, vecchiaData, nuovaData, differenze]

        // valore di ritorno
        return titoli
    }// fine del metodo

    /**
     * Riga col numero di voci
     */
    private static getRigaVoci() {
        ArrayList riga = new ArrayList()
        String descrizione = ':Categoria:BioBot|Template bio'
        def oldValue = Pref.getInt(LibBio.VOCI)
        def newValue = BioGrails.count()
        def differenze

        //valore per le preferenze
        mappaSintesi.put(LibBio.VOCI, newValue)

        descrizione = LibWiki.setQuadre(descrizione)
        descrizione = LibWiki.setBold(descrizione)
        differenze = newValue - oldValue
        oldValue = (String) LibTesto.formatNum(oldValue)
        newValue = (String) LibTesto.formatNum(newValue)
        newValue = LibWiki.setBold(newValue)
        if (differenze != 0) {
            differenze = (String) LibTesto.formatNum(differenze)
        } else {
            differenze = ''
        }// fine del blocco if-else
        riga.add(regolaSpazi(descrizione))
        riga.add(regolaSpazi(oldValue))
        riga.add(regolaSpazi(newValue))
        riga.add(regolaSpazi(differenze))

        // valore di ritorno
        return riga
    }// fine del metodo

    /**
     * Riga col numero di giorni
     */
    private static getRigaGiorni() {
        ArrayList riga = new ArrayList()
        String descrizione = 'Giorni interessati'
        def oldValue = Pref.getInt(LibBio.GIORNI)
        def newValue = Giorno.count()
        def differenze
        String nota = 'Previsto il [[29 febbraio]] per gli [[Anno bisestile|anni bisestili]]'

        //valore per le preferenze
        mappaSintesi.put(LibBio.GIORNI, newValue)

        descrizione = LibWiki.setBold(descrizione)
        nota = SPAZIO + LibWiki.setRef(nota)
        descrizione += nota
        differenze = newValue - oldValue
        oldValue = (String) LibTesto.formatNum(oldValue)
        newValue = (String) LibTesto.formatNum(newValue)
        if (differenze != 0) {
            differenze = (String) LibTesto.formatNum(differenze)
        } else {
            differenze = ''
        }// fine del blocco if-else

        riga.add(regolaSpazi(descrizione))
        riga.add(regolaSpazi(oldValue))
        riga.add(regolaSpazi(newValue))
        riga.add(regolaSpazi(differenze))

        // valore di ritorno
        return riga
    }// fine del metodo

    /**
     * Riga col numero di anni
     */
    private static getRigaAnni() {
        int anniPreCristo = 1000
        ArrayList riga = new ArrayList()
        String descrizione = 'Anni interessati'
        def oldValue = Pref.getInt(LibBio.ANNI)
        def newValue = getAnnoCorrenteNum() + anniPreCristo
        def differenze
        String nota = 'Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]]'

        //valore per le preferenze
        mappaSintesi.put(LibBio.ANNI, newValue)

        descrizione = LibWiki.setBold(descrizione)
        nota = SPAZIO + LibWiki.setRef(nota)
        descrizione += nota
        differenze = newValue - oldValue
        oldValue = (String) LibTesto.formatNum(oldValue)
        newValue = (String) LibTesto.formatNum(newValue)
        if (differenze != 0) {
            differenze = (String) LibTesto.formatNum(differenze)
        } else {
            differenze = ''
        }// fine del blocco if-else

        riga.add(regolaSpazi(descrizione))
        riga.add(regolaSpazi(oldValue))
        riga.add(regolaSpazi(newValue))
        riga.add(regolaSpazi(differenze))

        // valore di ritorno
        return riga
    }// fine del metodo

    /**
     * Riga col numero di attività
     */
    private static getRigaAttivita() {
        ArrayList riga = new ArrayList()
        String descrizione = PATH + 'Attività|Attività utilizzate'
        def oldValue = Pref.getInt(LibBio.ATTIVITA)
        def newValue = Attivita.executeQuery('select distinct plurale from Attivita').size()
        def differenze

        //valore per le preferenze
        mappaSintesi.put(LibBio.ATTIVITA, newValue)

        descrizione = LibWiki.setQuadre(descrizione)
        descrizione = LibWiki.setBold(descrizione)
        differenze = newValue - oldValue
        oldValue = (String) LibTesto.formatNum(oldValue)
        newValue = (String) LibTesto.formatNum(newValue)
        newValue = LibWiki.setBold(newValue)
        if (differenze != 0) {
            differenze = (String) LibTesto.formatNum(differenze)
        } else {
            differenze = ''
        }// fine del blocco if-else

        riga.add(regolaSpazi(descrizione))
        riga.add(regolaSpazi(oldValue))
        riga.add(regolaSpazi(newValue))
        riga.add(regolaSpazi(differenze))

        // valore di ritorno
        return riga
    }// fine del metodo

    /**
     * Riga col numero di nazionalità
     */
    private static getRigaNazionalita() {
        ArrayList riga = new ArrayList()
        String descrizione = PATH + 'Nazionalità|Nazionalità utilizzate'
        def oldValue = Pref.getInt(LibBio.NAZIONALITA)
        def newValue = Nazionalita.executeQuery('select distinct plurale from Nazionalita').size()
        def differenze

        //valore per le preferenze
        mappaSintesi.put(LibBio.NAZIONALITA, newValue)

        descrizione = LibWiki.setQuadre(descrizione)
        descrizione = LibWiki.setBold(descrizione)
        differenze = newValue - oldValue
        oldValue = (String) LibTesto.formatNum(oldValue)
        newValue = (String) LibTesto.formatNum(newValue)
        newValue = LibWiki.setBold(newValue)
        if (differenze != 0) {
            differenze = (String) LibTesto.formatNum(differenze)
        } else {
            differenze = ''
        }// fine del blocco if-else

        riga.add(regolaSpazi(descrizione))
        riga.add(regolaSpazi(oldValue))
        riga.add(regolaSpazi(newValue))
        riga.add(regolaSpazi(differenze))

        // valore di ritorno
        return riga
    }// fine del metodo

    /**
     * Riga coi giorni di attesa
     */
    private static getRigaAttesa() {
        ArrayList riga = new ArrayList()
        String descrizione = 'Giorni di attesa'
        def oldValue = Pref.getInt(LibBio.ATTESA)
        def newValue = NUOVA_ATTESA
        def differenze
        String nota = 'Giorni di attesa indicativi prima che ogni singola voce venga ricontrollata per registrare eventuali modifiche intervenute nei parametri significativi.'

        //valore per le preferenze
        mappaSintesi.put(LibBio.ATTESA, newValue)

        descrizione = LibWiki.setBold(descrizione)
        nota = SPAZIO + LibWiki.setRef(nota)
        descrizione += nota
        differenze = newValue - oldValue
        oldValue = (String) LibTesto.formatNum(oldValue)
        newValue = (String) LibTesto.formatNum(newValue)
        if (differenze != 0) {
            differenze = (String) LibTesto.formatNum(differenze)
        } else {
            differenze = ''
        }// fine del blocco if-else

        riga.add(regolaSpazi(descrizione))
        riga.add(regolaSpazi(oldValue))
        riga.add(regolaSpazi(newValue))
        riga.add(regolaSpazi(differenze))

        // valore di ritorno
        return riga
    }// fine del metodo

    /**
     * Costruisce il testo finale della pagina
     */
    private static String getTestoBottomSintesi() {
        String testo = ''

        testo += A_CAPO
        testo += '==Note=='
        testo += A_CAPO
        testo += '<references />'
        testo += A_CAPO
        testo += '<noinclude>'
        testo += '[[Categoria:Progetto Biografie|{{PAGENAME}}]]'
        testo += '</noinclude>'

        // valore di ritorno
        return testo
    }// fine del metodo

    /**
     * Registra nelle preferenze i nuovi valori che diventeranno i vecchi per la prossima sintesi
     */
    public static registraPreferenze() {
        def pref
        String chiave
        def valore
        Type type

        mappaSintesi?.each {
            chiave = (String) it.getKey()
            valore = it.getValue()
            pref = Pref.findByCode(chiave)

            if (pref) {
                type = pref.type

                switch (type) {
                    case Type.booleano:
                        if (valore instanceof Boolean) {
                            pref.setBool(valore)
                        }// fine del blocco if
                        break
                    case Type.intero:
                        if (valore instanceof Integer) {
                            pref.setIntero(valore)
                        }// fine del blocco if
                        break
                    case Type.stringa:
                        if (valore instanceof String) {
                            pref.setStringa(valore)
                        }// fine del blocco if
                        break
                    default: // caso non definito
                        break
                } // fine del blocco switch

                pref.save(flush: true)
            }// fine del blocco if
        } // fine del ciclo each
        mappaSintesi.clear()
    }// fine del metodo

    /**
     * Eventuali spazi vuoti prima e dopo il testo
     * Vale per tutte le righe
     */
    private static String regolaSpazi(String testoIn) {
        String testoOut = testoIn

        if (testoOut) {
            if (USA_SPAZI) {
                testoOut = SPAZIO + testoIn + SPAZIO
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    }// fine del metodo

    /**
     * Anno corrente.
     */
    public static int getAnnoCorrenteNum() {
        int anno = 0
        String annoTxt = getAnnoCorrenteTxt()

        if (annoTxt) {
            try { // prova ad eseguire il codice
                anno = Integer.decode(annoTxt)
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        /* valore di ritorno */
        return anno
    }// fine del metodo

    /**
     * Anno corrente.
     */
    public static String getAnnoCorrenteTxt() {
        String anno = ''
        Date data = new Date(System.currentTimeMillis())
        GregorianCalendar cal = new GregorianCalendar()

        if (data) {
            cal.setTime(data)
            anno = cal.get(Calendar.YEAR)
        }// fine del blocco if

        /* valore di ritorno */
        return anno
    }// fine del metodo

    /**
     *
     */
    public uploadAll() {
        if (Pref.getBool(LibBio.USA_LISTE_BIO_GIORNI)) {
            if (bioService && giornoService) {
                giornoService.uploadGiorniNascita(bioService)
                giornoService.uploadGiorniMorte(bioService)
            }// fine del blocco if
        } else {
            if (bioGrailsService) {
                bioGrailsService.uploadGiorniNascita()
                bioGrailsService.uploadGiorniMorte()
            }// fine del blocco if
        }// fine del blocco if-else

        if (Pref.getBool(LibBio.USA_LISTE_BIO_ANNI)) {
            if (bioService && annoService) {
                annoService.uploadAnniNascita(bioService)
                annoService.uploadAnniMorte(bioService)
            }// fine del blocco if
        } else {
            if (bioGrailsService) {
                bioGrailsService.uploadAnniNascita()
                bioGrailsService.uploadAnniMorte()
            }// fine del blocco if
        }// fine del blocco if-else
    }// fine del metodo

} // fine della service classe
