package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiLib

/**
 * Created by gac on 18/10/14.
 */
class ListaNazionalitaGrandi extends ListaBio {

    private static String TAG_PROGETTO = 'Progetto:Biografie/Nazionalità/'
    private static String TAG_PARAGRAFO = 'Progetto:Biografie/Attività/'
    private static String tagAltri = 'Altri...'
    private static String tagAltre = 'Altre...'


    public ListaNazionalitaGrandi() {
        super((Object) null)
    }// fine del costruttore

    public ListaNazionalitaGrandi(Nazionalita nazionalita) {
        super(nazionalita)
    }// fine del costruttore

    public ListaNazionalitaGrandi(Nazionalita nazionalita, BioService bioService) {
        super(nazionalita, bioService)
    }// fine del costruttore

    public ListaNazionalitaGrandi(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore

    /**
     * Costruisce un oggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraOggetto(String soggetto) {
        Nazionalita nazionalita = Nazionalita.findByPlurale(soggetto)

        if (nazionalita) {
            oggetto = nazionalita
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce un soggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraSoggetto(Object oggetto) {
        Nazionalita nazionalita

        if (oggetto && oggetto instanceof Nazionalita) {
            nazionalita = (Nazionalita) oggetto
            soggetto = nazionalita.plurale
            soggettoMadre = soggetto
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
        usaInclude = false
        usaHeadRitorno = false
        tagTemplateBio = Pref.getStr(LibBio.NOME_TEMPLATE_AVVISO_LISTE_NAZ_ATT, 'ListaBio')
        usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_NAZ, true)
        usaTitoloSingoloParagrafo = true
        usaAttivitaMultiple = Pref.getBool(LibBio.USA_ATTIVITA_MULTIPLE, true)
        maxVociParagrafo = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_NAZIONALITA, 50)
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected void elaboraTitolo() {
        String titolo = getPlurale()

        if (titolo) {
            titolo = LibTesto.primaMaiuscola(titolo)
            if (!titoloPagina) {
                titoloPagina = TAG_PROGETTO + titolo
            }// fine del blocco if
        }// fine del blocco if

    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave = attivitaPluralePerGenere(bio)

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo per la seconda attività
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafoSecondaAttivita(BioGrails bio) {
        return attivitaSecondaPluralePerGenere(bio)
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo per la terza attività
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafoTerzaAttivita(BioGrails bio) {
        return attivitaTerzaPluralePerGenere(bio)
    }// fine del metodo

    /**
     * Creazione della sottopagina
     * Sovrascritto
     */
    @Override
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate, String tagSesso) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaNazionalitaAttivita(elaboraSoggettoSpecifico(chiaveParagrafo, tagSesso), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.numPersone = listaVociOrdinate.size()
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.elaboraPagina()
    }// fine del metodo


    /**
     * Elabora soggetto specifico
     */
    protected String elaboraSoggettoSpecifico(String chiaveParagrafo, String tagSesso) {
        LinkedHashMap<String, ?> mappa = LibListe.getAttivitaMappaMultiplaUomoDonna(chiaveParagrafo)
        String titoloParagrafo=''

        if (tagSesso.equals(UOMINI)) {
            if (mappa[LibListe.MAPPA_PARAGRAFO_UOMINI]) {
                titoloParagrafo = mappa[LibListe.MAPPA_PARAGRAFO_UOMINI]
            }// fine del blocco if
        }// fine del blocco if

        if (tagSesso.equals(DONNE)) {
            if (mappa[LibListe.MAPPA_PARAGRAFO_DONNE]) {
                titoloParagrafo = mappa[LibListe.MAPPA_PARAGRAFO_DONNE]
            }// fine del blocco if
        }// fine del blocco if

        if (chiaveParagrafo.equals('Cantanti')) {
            def stop
        }// fine del blocco if

        return super.elaboraSoggettoSpecifico(titoloParagrafo, '')
    }// fine del metodo


    /**
     * Recupera il plurale
     */
    private String getPlurale() {
        String plurale = ''

        if (oggetto && oggetto instanceof Nazionalita) {
            plurale = oggetto.plurale
        }// fine del blocco if

        if (!plurale) {
            plurale = soggetto
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Elaborazione specifica per nazionalità troppo grandi
     * Seleziono i generi plurali, che sono i titoli dei paragrafi
     * Elabora il singolo paragrafo di attività
     * Se il paragrafo è piccolo, elabora il testo
     * Se il paragrafo è grande, elabora una sottopagina e la registra
     */
    @Override
    protected String elaboraBody() {
        String testo = ''
        ArrayList<String> paragrafiAttMaschili
        ArrayList<String> paragrafiAttFemminili
        ArrayList<String> paragrafiAttAmbigenere
        String nazionalitaPlurale = getPlurale()

        if (usaSuddivisioneUomoDonna) {
            testo += '=Uomini='
            testo += aCapo
            paragrafiAttMaschili = paragrafiAttivitaMaschili()
            paragrafiAttMaschili?.each {
                testo += elaboraParagrafo(nazionalitaPlurale, it, LibListe.UOMO)
            } // fine del ciclo each
            testo += elaboraParagrafo(nazionalitaPlurale, '', LibListe.UOMO)

            testo += aCapo
            testo += '=Donne='
            testo += aCapo
            paragrafiAttFemminili = paragrafiAttivitaFemminili()
            paragrafiAttFemminili?.each {
                testo += elaboraParagrafo(nazionalitaPlurale, it, LibListe.DONNA)
            } // fine del ciclo each
            testo += elaboraParagrafo(nazionalitaPlurale, '', LibListe.DONNA)

            esistonoUominiDonne = true
        } else {
            paragrafiAttAmbigenere = paragrafiAttivitaAmbigenere()
            paragrafiAttAmbigenere?.each {
                testo += elaboraParagrafo(nazionalitaPlurale, it)
            } // fine del ciclo each
            testo += elaboraParagrafo(nazionalitaPlurale, '')
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected String elaboraFooter() {
        String testo = ''
        String nazionalita = soggetto
        String nazionalitaMadre = soggettoMadre
        nazionalita = LibTesto.primaMaiuscola(nazionalita)
        nazionalitaMadre = LibTesto.primaMaiuscola(nazionalitaMadre)
        String tagCategoria = "[[Categoria:Bio nazionalità|${nazionalita}]]"

        if (numDidascalie > listaBiografie?.size()) {
            testo += aCapo
            testo += super.getNote('Alcune persone sono citate più volte perché hanno diverse attività')
            if (usaSuddivisioneUomoDonna && esistonoUominiDonne) {
                testo += aCapo
            }// fine del blocco if
        }// fine del blocco if

        testo += aCapo
        testo += super.getVociCorrelate()
        testo += aCapo
        testo += "*[[:Categoria:${nazionalitaMadre}]]"
        testo += aCapo
        testo += '*[[Progetto:Biografie/Nazionalità]]'
        testo += aCapo
        testo += aCapo
        testo += '{{Portale|biografie}}'
        testo += aCapo
        if (usaInclude) {
            testo += LibBio.setNoInclude(tagCategoria)
        } else {
            testo += tagCategoria
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con eventuali link
     * Sovrascritto
     */
    @Override
    protected String elaboraTitoloParagrafo(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo

        if (Pref.getBool(LibBio.USA_TITOLO_PARAGRAFO_NAZ_ATT_LINK_PROGETTO, true)) {
            if (chiaveParagrafo.equals(tagAltri) || chiaveParagrafo.equals(tagAltre)) {
                titoloParagrafo = chiaveParagrafo
            } else {
                titoloParagrafo = NazionalitaService.elaboraTitoloParagrafoNazionalita(chiaveParagrafo, tagParagrafoNullo)
            }// fine del blocco if-else
        } else {
            titoloParagrafo = super.elaboraTitoloParagrafo(chiaveParagrafo, listaVoci)
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * Recupera la Nazionalita
     */
    protected Nazionalita getNazionalita() {
        Nazionalita nazionalita = null

        if (oggetto && oggetto instanceof Nazionalita) {
            nazionalita = (Nazionalita) oggetto
        }// fine del blocco if

        return nazionalita
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        String nazionalitaPlurale
        String whereNazionalita

        if (nazionalita) {
            nazionalitaPlurale = LibTesto.primaMinuscola(nazionalita.plurale)
            whereNazionalita = NazionalitaService.whereParagrafoNazionalita(nazionalitaPlurale)
            numPersone = LibBio.bioGrailsCount(whereNazionalita)
        }// fine del blocco if

    }// fine del metodo

    /**
     * Elabora il singolo paragrafo
     * <p>
     * Decide se il testo rimane nella pagina principale o se occorre creare una sottopagina
     */
    private String elaboraParagrafo(String nazionalita, String paragrafo) {
        return elaboraParagrafo(nazionalita, paragrafo, '')
    }// fine del metodo

    /**
     * Elabora il singolo paragrafo
     * <p>
     * Decide se il testo rimane nella pagina principale o se occorre creare una sottopagina
     */
    private String elaboraParagrafo(String nazionalita, String chiaveParagrafo, String sesso) {
        String testo = ''
        String where
        int numRecords = 0
        ArrayList<BioGrails> lista
        String titoloParagrafo

        if (chiaveParagrafo.startsWith('cest')) {
            def stop
        }// fine del blocco if

        if (nazionalita) {
            where = whereParagrafo(nazionalita, chiaveParagrafo, sesso)
            try { // prova ad eseguire il codice
                numRecords = LibBio.bioGrailsCount(where)
            } catch (Exception unErrore) { // intercetta l'errore
                log.error unErrore
            }// fine del blocco try-catch
        }// fine del blocco if

        if (numRecords) {
            lista = creaLista(nazionalita, chiaveParagrafo, sesso)
            if (chiaveParagrafo) {
                chiaveParagrafo = LibTesto.primaMaiuscola(chiaveParagrafo)
            } else {
                if (sesso && sesso.equals(LibListe.DONNA)) {
                    chiaveParagrafo = tagAltre
                } else {
                    chiaveParagrafo = tagAltri
                }// fine del blocco if-else
            }// fine del blocco if-else
            if (numRecords < maxVociParagrafo) {
                testo += super.elaboraBodyParagrafo(chiaveParagrafo, lista, sesso)
            } else {
                titoloParagrafo = elaboraTitoloParagrafo(chiaveParagrafo, lista)
                testo += super.elaboraParagrafoSottoPagina(chiaveParagrafo, titoloParagrafo, lista, sesso)
            }// fine del blocco if-else
        }// fine del blocco if

        return testo
    } // fine del metodo

    /**
     * Creazione del rimando
     */
    protected String elaborazioneRimando(String chiaveParagrafo, String titoloParagrafo, String tagSesso) {
        String testo = ''
        String titoloSottovoce = getTitoloSottovoce(chiaveParagrafo, tagSesso)

        testo += elaboraTitoloParagrafo(titoloParagrafo)
        testo += "*{{Vedi anche|${titoloSottovoce}}}"

        return testo + aCapo + aCapo
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo, String tagSesso) {
        return TAG_PROGETTO + elaboraSoggettoSpecifico(chiaveParagrafo, tagSesso)
    }// fine del metodo

    /**
     * Elabora la query per il singolo paragrafo
     */
    public static String whereParagrafo(String nazionalita, String paragrafo) {
        return whereParagrafo(nazionalita, paragrafo, '')
    } // fine del metodo

    /**
     * Elabora la query per il singolo paragrafo
     */
    public static String whereParagrafo(String nazionalita, String paragrafo, String sesso) {
        String where = ''
        String whereNaz
        String whereAtt
        String whereSex

        if (nazionalita) {
            whereNaz = NazionalitaService.whereParagrafoNazionalita(nazionalita)
            whereAtt = NazionalitaService.whereParagrafoAttivita(paragrafo, sesso)
            if (whereNaz && whereAtt) {
                whereNaz = '(' + whereNaz + ')'
                whereAtt = '(' + whereAtt + ')'
                where = whereNaz + ' AND ' + whereAtt

                if (sesso) {
                    if (sesso.equals('M') || sesso.equals('F')) {
                        whereSex = " AND (sesso='$sesso')"
                        where += whereSex
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * Crea il singolo paragrafo nella pagina principale
     */
    private static ArrayList<BioGrails> creaLista(String nazionalita, String paragrafo) {
        return creaLista(nazionalita, paragrafo, '')
    } // fine del metodo

    /**
     * Crea il singolo paragrafo nella pagina principale
     */
    private static ArrayList<BioGrails> creaLista(String nazionalita, String paragrafo, String sesso) {
        ArrayList<BioGrails> lista
        String query
        String where = whereParagrafo(nazionalita, paragrafo, sesso)

        query = "from BioGrails where ($where)"
        lista = BioGrails.executeQuery(query)

        return lista
    } // fine del metodo

    /**
     * Elabora e crea la lista della nazionalità indicata e la uploada sul server wiki
     */
    public static boolean uploadNazionalita(Nazionalita nazionalita, BioService bioService) {
        boolean registrata = false
        ListaNazionalitaGrandi listaNazionalitaGrande

        if (nazionalita) {
            listaNazionalitaGrande = new ListaNazionalitaGrandi(nazionalita, bioService)
            if (listaNazionalitaGrande.registrata) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe


