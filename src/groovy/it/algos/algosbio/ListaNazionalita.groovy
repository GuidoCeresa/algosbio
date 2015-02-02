package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiLib

/**
 * Created by gac on 18/10/14.
 */
class ListaNazionalita extends ListaBio {

    private static String TAG_PROGETTO = 'Progetto:Biografie/Nazionalità/'
    private static String TAG_PARAGRAFO = 'Progetto:Biografie/Attività/'

    public ListaNazionalita() {
        super((Object) null)
    }// fine del costruttore

    public ListaNazionalita(Nazionalita nazionalita) {
        super(nazionalita)
    }// fine del costruttore

    public ListaNazionalita(Nazionalita nazionalita, BioService bioService) {
        super(nazionalita, bioService)
    }// fine del costruttore

    public ListaNazionalita(String soggetto, boolean iniziaSubito) {
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
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaNazionalitaAttivita(elaboraSoggettoSpecifico(chiaveParagrafo), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.elaboraPagina()
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo) {
        return TAG_PROGETTO + elaboraSoggettoSpecifico(chiaveParagrafo)
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
     * Recupera il plurale
     */
    private String getPluraleMaiuscolo() {
        return LibTesto.primaMaiuscola(getPlurale())
    }// fine del metodo

//    /**
//     * Corpo della pagina
//     * Decide se c'è la doppia colonna
//     * Controlla eventuali template di rinvio
//     * Sovrascritto
//     */
//    @Override
//    protected String elaboraBody() {
//        return elaboraBodyDidascalie()
//    }// fine del metodo

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

        if (numDidascalie > listaBiografie.size()) {
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
            titoloParagrafo = elaboraTitoloParagrafoNazionalita(chiaveParagrafo, listaVoci)
        } else {
            titoloParagrafo = super.elaboraTitoloParagrafo(chiaveParagrafo, listaVoci)
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con link
     */
    private String elaboraTitoloParagrafoNazionalita(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo
        String pipe = '|'
        String singolare
        String plurale = LibTesto.primaMaiuscola(chiaveParagrafo)
        Attivita attivita
        BioGrails bio = listaVoci.get(0)
        Genere genere

        if (bio) {
            plurale = LibTesto.primaMinuscola(plurale)
            genere = Genere.findByPlurale(plurale)
            if (genere) {
                singolare = genere.singolare
                if (singolare) {
                    attivita = Attivita.findBySingolare(singolare)
                    if (attivita) {
                        plurale = attivita.plurale
                        plurale = LibTesto.primaMaiuscola(plurale)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        if (chiaveParagrafo.equals(tagParagrafoNullo)) {
            titoloParagrafo = chiaveParagrafo
        } else {
            titoloParagrafo = TAG_PARAGRAFO + plurale + pipe + chiaveParagrafo
            titoloParagrafo = LibWiki.setQuadre(titoloParagrafo)
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
        String whereNazionalita
        String query
        int numRecords
        String nazionalitaPlurale

        if (nazionalita) {
            nazionalitaPlurale = LibTesto.primaMinuscola(nazionalita.plurale)
            whereNazionalita = whereParagrafo(nazionalitaPlurale)
            numRecords = LibBio.bioGrailsCount(whereNazionalita)

            if (whereNazionalita && numRecords < Pref.getInt(LibBio.MAX_VOCI_PAGINA_NAZIONALITA, 10000)) {
                query = "from BioGrails where ($whereNazionalita)"
                listaBiografie = BioGrails.executeQuery(query)
            } else {
                elaboraNazionalitaMoltoGrande(nazionalitaPlurale)
            }// fine del blocco if-else
        }// fine del blocco if

        def stop
    }// fine del metodo

    /**
     * Elaborazione specifica per nazionalità troppo grandi
     * Seleziono i generi plurali, che sono i titoli dei paragrafi
     * Elabora il singolo paragrafo di attività
     * Se il paragrafo è piccolo, elabora il testo
     * Se il paragrafo è grande, elabora una sottopagina e la registra
     */
    protected elaboraNazionalitaMoltoGrande(String nazionalitaPlurale) {
        ArrayList<String> paragrafiAttMaschili
        ArrayList<String> paragrafiAttFemminili
        ArrayList<String> paragrafiAttAmbigenere

        if (usaSuddivisioneUomoDonna) {
            paragrafiAttMaschili = paragrafiAttivitaMaschili()
            paragrafiAttMaschili?.each {
                elaboraParagrafo(nazionalitaPlurale, it)
            } // fine del ciclo each

            paragrafiAttFemminili = paragrafiAttivitaFemminili()
            paragrafiAttFemminili?.each {
                elaboraParagrafo(nazionalitaPlurale, it)
            } // fine del ciclo each
        } else {
            paragrafiAttAmbigenere = paragrafiAttivitaAmbigenere()
            paragrafiAttAmbigenere?.each {
                elaboraParagrafo(nazionalitaPlurale, it)
            } // fine del ciclo each
        }// fine del blocco if-else

    }// fine del metodo


    /**
     * Elabora il singolo paragrafo
     * <p>
     * Decide se il testo rimane nella pagina principale o se occorre creare una sottopagina
     */
    private void elaboraParagrafo(String nazionalita, String paragrafo) {
        String where
        int numRecords = 0
        ArrayList<BioGrails> lista

        if (nazionalita && paragrafo) {
            where = whereParagrafo(nazionalita, paragrafo)
            numRecords = LibBio.bioGrailsCount(where)
        }// fine del blocco if

        if (numRecords) {
            lista = creaLista(nazionalita, paragrafo)
            paragrafo = LibTesto.primaMaiuscola(paragrafo)
            if (numRecords < maxVociParagrafo) {
                creaParagrafoPrincipale(nazionalita, paragrafo, lista)
            } else {
                creaParagrafoSottopagina(nazionalita, paragrafo, lista)
            }// fine del blocco if-else
        }// fine del blocco if

        def stop
    } // fine del metodo

    /**
     * Elabora la query per il singolo paragrafo
     */
    private static String whereParagrafo(String nazionalita) {
        String where = ''

        if (nazionalita) {
            where = whereParagrafoNazionalita(nazionalita)
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * Elabora la query per il singolo paragrafo
     */
    private static String whereParagrafo(String nazionalita, String paragrafo) {
        String where = ''
        String whereNaz
        String whereAtt

        if (nazionalita && paragrafo) {
            whereNaz = whereParagrafoNazionalita(nazionalita)
            whereAtt = whereParagrafoAttivita(paragrafo)
            if (whereNaz && whereAtt) {
                whereNaz = '(' + whereNaz + ')'
                whereAtt = '(' + whereAtt + ')'
                where = whereNaz + ' AND ' + whereAtt
            }// fine del blocco if
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * Crea il singolo paragrafo nella pagina principale
     */
    private static ArrayList<BioGrails> creaLista(String nazionalita, String attivita) {
        ArrayList<BioGrails> lista
        String query
        String where = whereParagrafo(nazionalita, attivita)

        query = "from BioGrails where ($where)"
        lista = BioGrails.executeQuery(query)

        return lista
    } // fine del metodo

    /**
     * Crea il singolo paragrafo nella pagina principale
     */
    private static void creaParagrafoPrincipale(String nazionalita, String attivita, ArrayList<BioGrails> lista) {
        def stop
    } // fine del metodo

    /**
     * Crea la sottopagina per il paragrafo troppo grande
     * Lascia il rinvio nella pagina principale
     */
    private void creaParagrafoSottopagina(String nazionalita, String paragrafo, ArrayList<BioGrails> lista) {
        creazioneSottopagina(paragrafo, '', lista)
    } // fine del metodo

    /**
     * Elabora la where nazionalità per la query del singolo paragrafo
     */
    private static String whereParagrafoNazionalita(String nazionalitaPlurale) {
        String where = ''
        String tag = ' nazionalita_link_id='
        String tagOr = ' OR'
        ArrayList<Nazionalita> listaNazionalita = getListaNazionalita(nazionalitaPlurale)
        Nazionalita nazionalita
        long codice

        if (nazionalitaPlurale) {
            listaNazionalita?.each {
                nazionalita = it
                codice = nazionalita.id
                where += tag + codice + tagOr
            } // fine del ciclo each
            where = LibTesto.levaCoda(where, tagOr)
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * Elabora la where attività per la query del singolo paragrafo
     */
    private static String whereParagrafoAttivita(String paragrafo) {
        String where = ''
        String tag = ' attivita_link_id='
        String tag2 = ' attivita2link_id='
        String tag3 = ' attivita3link_id='
        String tagOr = ' OR'
        ArrayList<Attivita> listaAttivita = getListaAttivita('distillatori')
        boolean attivitaMultiple = true
        Attivita attivita
        long codice

        if (paragrafo) {
            listaAttivita?.each {
                attivita = it
                codice = attivita.id
                where += tag + codice + tagOr
                if (attivitaMultiple) {
                    where += tag2 + codice + tagOr
                    where += tag3 + codice + tagOr
                }// fine del blocco if
            } // fine del ciclo each
            where = LibTesto.levaCoda(where, tagOr)
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * Crea una lista di id di nazionalità utilizzate
     * Per ogni plurale, ci possono essere diversi 'singolari' richiamati dalle voci di BioGrails
     */
    private static ArrayList<Nazionalita> getListaNazionalita(String nazionalitaPlurale) {
        ArrayList<Nazionalita> listaNazionalita = null

        if (nazionalitaPlurale) {
            listaNazionalita = Nazionalita.findAllByPlurale(nazionalitaPlurale)
        }// fine del blocco if

        return listaNazionalita
    } // fine del metodo

    /**
     * Crea una lista di attività utilizzate nel paragrafo
     * Il titolo del paragrafo deriva dal plurale di genere
     * Ogni plurale di genere può avere diversi singolari di genere
     * Ogni singolare di genere diventa singolare di attività
     */
    private static ArrayList<Attivita> getListaAttivita(String paragrafo) {
        ArrayList<Attivita> listaAttivita = null
        ArrayList<Genere> listaGenere
        Genere genere
        String singolare
        Attivita attivita

        if (paragrafo) {
            listaGenere = getListaGenere(paragrafo)
            if (listaGenere) {
                listaAttivita = new ArrayList<Attivita>()
                listaGenere?.each {
                    genere = it
                    singolare = genere.singolare
                    attivita = Attivita.findBySingolare(singolare)
                    if (attivita) {
                        if (!listaAttivita.contains(attivita)) {
                            listaAttivita.add(attivita)
                        }// fine del blocco if
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine del blocco if
        }// fine del blocco if

        return listaAttivita
    } // fine del metodo

    /**
     * Crea una lista di genere che ha come singolare il paragrafo
     */
    private static ArrayList<Genere> getListaGenere(String paragrafo) {
        ArrayList<Genere> listaGenere = null

        if (paragrafo) {
            listaGenere = Genere.findAllByPlurale(paragrafo)
        }// fine del blocco if

        return listaGenere
    } // fine del metodo

    /**
     * Elabora e crea la lista della nazionalità indicata e la uploada sul server wiki
     */
    public static boolean uploadNazionalita(Nazionalita nazionalita, BioService bioService) {
        boolean registrata = false
        ListaNazionalita listaNazionalita

        if (nazionalita) {
            listaNazionalita = new ListaNazionalita(nazionalita, bioService)
            if (listaNazionalita.registrata) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe


