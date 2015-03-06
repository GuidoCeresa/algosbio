package it.algos.algosbio

import it.algos.algosbio.Nazionalita
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Classe astratta con solo metodi statici
 * Libreria per le ricerche delle lista
 *
 * Nelle nazionalità, la lista dei plurali e quella dei titoli dei paragrafi (usati in attività) coincide
 *
 * Nelle attività, queste vengono raggruppate e spazzolate secondo la lista dei plurali (Abati e badesse) che non
 * coincide con i titoli dei paragrafi (Abati) che sono inoltre suddivisi tra maschi: Cantanti (uomini) e
 * femmine: Cantanti (donne)
 * Inoltre le attività vanno recuperate tra 3 campi di BioGrails: attivitàLink, attività2Link e attività3Link
 */
abstract class LibListe {

    public static String MAPPA_NOME_ATTIVITA = 'nomeAttivita'
    public static String MAPPA_NOME_NAZIONALITA = 'nomeNazionalita'
    public static String MAPPA_ATTIVITA = 'attivita'
    public static String MAPPA_NAZIONALITA = 'nazionalita'
    public static String MAPPA_PARAGRAFO = 'titoloParagrafo'
    public static String MAPPA_PARAGRAFO_UOMINI = 'titoloParagrafoUomini'
    public static String MAPPA_PARAGRAFO_DONNE = 'titoloParagrafoDonne'
    public static String MAPPA_SOTTOPAGINA_UOMINI = 'titoloSottopaginaUomini'
    public static String MAPPA_SOTTOPAGINA_DONNE = 'titoloSottopaginaDonne'
    public static String MAPPA_LISTA_NAZIONALITA = 'listaNazionalita'
    public static String MAPPA_LISTA_ATTIVITA = 'listaAttivita'
    public static String MAPPA_BIO_PRIMA = 'bioPrima'      // usata da Attivita
    public static String MAPPA_BIO_SECONDA = 'bioSeconda'  // usata da Attivita
    public static String MAPPA_BIO_TERZA = 'bioTerza'      // usata da Attivita
    public static String MAPPA_BIO_UOMINI = 'bioUomini'                 // usata da Nazionalita
    public static String MAPPA_BIO_UOMINI_PRIMA = 'bioUominiPrima'      // usata da Attivita
    public static String MAPPA_BIO_UOMINI_SECONDA = 'bioUominiSeconda'  // usata da Attivita
    public static String MAPPA_BIO_UOMINI_TERZA = 'bioUominiTerza'      // usata da Attivita
    public static String MAPPA_BIO_DONNE = 'bioDonne'                 // usata da Nazionalita
    public static String MAPPA_BIO_DONNE_PRIMA = 'bioDonnePrima'      // usata da Attivita
    public static String MAPPA_BIO_DONNE_SECONDA = 'bioDonneSeconda'  // usata da Attivita
    public static String MAPPA_BIO_DONNE_TERZA = 'bioDonneTerza'      // usata da Attivita
    public static String MAPPA_BIO_TOTALE = 'bioTotale'                 // usata da Nazionalita
    public static String MAPPA_BIO_TOTALE_PRIMA = 'bioTotalePrima'      // usata da Attivita
    public static String MAPPA_BIO_TOTALE_SECONDA = 'bioTotaleSeconda'  // usata da Attivita
    public static String MAPPA_BIO_TOTALE_TERZA = 'bioTotaleTerza'      // usata da Attivita
    public static String MAPPA_USATA_UOMINI = 'usataUomini'
    public static String MAPPA_USATA_DONNE = 'usataDonne'
    public static String MAPPA_USATA = 'usata'
    public static String UOMO = 'M'
    public static String DONNA = 'F'
    public static String UOMINI = '(uomini)'
    public static String DONNE = '(donne)'

    private static int div = 100
    private static String sec = 'decimi di secondo'

    /**
     * Lista dei nomi plurali tutte le nazionalità distinte (per plurale)
     */
    public static ArrayList<String> getNazioniPlurale() {
        return (ArrayList<String>) Nazionalita.executeQuery("select distinct plurale from Nazionalita order by plurale")
    } // fine del metodo

    /**
     * Numero totale delle nazionalità distinte presenti nel database
     */
    public static int getNazioniNum() {
        int numeroNazioni = 0
        ArrayList<String> lista = getNazioniPlurale()

        if (lista) {
            numeroNazioni = lista.size()
        }// fine del blocco if

        return numeroNazioni
    } // fine del metodo statico

    /**
     * Numero totale delle nazionalità distinte presenti nel database ed utilizzate
     */
    public static int getNazioniUsateNum() {
        int numeroNazioni = 0
        ArrayList<Nazionalita> lista = getNazioniUsate()

        if (lista) {
            numeroNazioni = lista.size()
        }// fine del blocco if

        return numeroNazioni
    } // fine del metodo statico

    /**
     * Numero totale delle nazionalità distinte presenti nel database ed non utilizzate
     */
    public static int getNazioniNonUsateNum() {
        int numeroNazioni = 0
        ArrayList<Nazionalita> lista = getNazioniNonUsate()

        if (lista) {
            numeroNazioni = lista.size()
        }// fine del blocco if

        return numeroNazioni
    } // fine del metodo statico

    /**
     * Lista di tutte le nazionalità distinte
     */
    public static ArrayList<Nazionalita> getNazioni() {
        ArrayList<Nazionalita> lista = null
        ArrayList<String> listaPlurali
        String plurale
        Nazionalita nazionalita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getNazioniPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            lista = new ArrayList<Nazionalita>()

            listaPlurali?.each {
                plurale = it
                nazionalita = Nazionalita.findByPlurale(plurale)
                if (nazionalita) {
                    lista.add(nazionalita)
                }// fine del blocco if
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getNazioni: in $tempo $sec")
        }// fine del blocco if

        return lista
    } // fine del metodo

    /**
     * Lista di tutte le nazionalità distinte, usate
     */
    public static ArrayList<Nazionalita> getNazioniUsate() {
        ArrayList<Nazionalita> lista = null
        ArrayList<String> listaPlurali
        ArrayList<Nazionalita> listaTmp = null
        String plurale
        Nazionalita nazionalitaTmp
        Nazionalita nazionalita
        int numNaz = 0
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getNazioniPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            lista = new ArrayList<Nazionalita>()
            listaPlurali?.each {
                plurale = it
                numNaz = 0
                listaTmp = Nazionalita.findAllByPlurale(plurale)
                if (listaTmp && listaTmp.size() > 0) {
                    listaTmp.each {
                        nazionalitaTmp = it
                        numNaz += BioGrails.countByNazionalitaLink(nazionalitaTmp)
                    }// fine di each
                }// fine del blocco if
                if (numNaz > 0) {
                    nazionalita = Nazionalita.findByPlurale(plurale)
                    if (nazionalita) {
                        lista.add(nazionalita)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getNazioniUsate: in $tempo $sec")
        }// fine del blocco if

        return lista
    } // fine del metodo

    /**
     * Lista di tutte le nazionalità distinte, non usate
     */
    public static ArrayList<Nazionalita> getNazioniNonUsate() {
        ArrayList<Nazionalita> listaNonUsate = null
        ArrayList<Nazionalita> listaAll
        ArrayList<Nazionalita> listaUsate
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaAll = getNazioni()
        listaUsate = getNazioniUsate()
        if (listaAll && listaUsate) {
            listaNonUsate = listaAll - listaUsate
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getNazioniNonUsate: in $tempo $sec")
        }// fine del blocco if

        return listaNonUsate
    } // fine del metodo

    /**
     * Lista dei nomi plurali per le nazionalità usate
     */
    public static ArrayList<String> getNazioniPluraleUsate() {
        ArrayList<String> listaPlurali = null
        ArrayList<Nazionalita> listaUsate
        Nazionalita nazionalita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaUsate = getNazioniUsate()
        if (listaUsate && listaUsate.size() > 0) {
            listaPlurali = new ArrayList<String>()
            listaUsate?.each {
                nazionalita = it
                listaPlurali.add(nazionalita.plurale)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getNazioniPluraleUsate: in $tempo $sec")
        }// fine del blocco if

        return listaPlurali
    } // fine del metodo

    /**
     * Lista dei nomi plurali per le nazionalità non usate
     */
    public static ArrayList<String> getNazioniPluraleNonUsate() {
        ArrayList<String> listaPlurali = null
        ArrayList<Nazionalita> listaNonUsate
        Nazionalita nazionalita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaNonUsate = getNazioniNonUsate()
        if (listaNonUsate && listaNonUsate.size() > 0) {
            listaPlurali = new ArrayList<String>()
            listaNonUsate?.each {
                nazionalita = it
                listaPlurali.add(nazionalita.plurale)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getNazioniPluraleNonUsate: in $tempo $sec")
        }// fine del blocco if

        return listaPlurali
    } // fine del metodo

    /**
     * Mappa di una nazionalità
     *
     * La mappa contiene:
     *  -plurale della nazionalità
     *  -lista degli id dei records di Nazionalità che hanno quel plurale
     *  -numero di voci BioGrails maschili che nel campo nazionalitàLink usano i records della lista
     *  -numero di voci BioGrails femminili che nel campo nazionalitàLink usano i records della lista
     */
    public static HashMap<String, ?> getNazioneMappa(String nome) {
        HashMap<String, ?> mappa = new HashMap<String, ?>()
        ArrayList<Nazionalita> listaNazionalita
        Nazionalita nazionalitaSingola
        Nazionalita nazionalitaTmp
        int bioUomini = 0
        int bioDonne = 0
        int bioTotale
        String nomeMinuscolo = LibTesto.primaMinuscola(nome)
        String titoloParagrafo = ''

        listaNazionalita = Nazionalita.findAllByPlurale(nomeMinuscolo)
        if (listaNazionalita && listaNazionalita.size() > 0) {
            listaNazionalita.each {
                nazionalitaTmp = it
                bioUomini += BioGrails.countByNazionalitaLinkAndSesso(nazionalitaTmp, UOMO)
                bioDonne += BioGrails.countByNazionalitaLinkAndSesso(nazionalitaTmp, DONNA)
            }// fine di each
            if (nazionalitaTmp) {
                titoloParagrafo = nazionalitaTmp.plurale
            }// fine del blocco if
        } else {
            nazionalitaSingola = Nazionalita.findBySingolare(nomeMinuscolo)
            if (nazionalitaSingola) {
                bioUomini += BioGrails.countByNazionalitaLinkAndSesso(nazionalitaSingola, UOMO)
                bioDonne += BioGrails.countByNazionalitaLinkAndSesso(nazionalitaSingola, DONNA)
                titoloParagrafo = nazionalitaSingola.plurale
            }// fine del blocco if
        }// fine del blocco if-else
        titoloParagrafo = LibTesto.primaMaiuscola(titoloParagrafo)
        bioTotale = bioUomini + bioDonne

        mappa.put(MAPPA_NAZIONALITA, nomeMinuscolo)
        mappa.put(MAPPA_PARAGRAFO, titoloParagrafo)
        mappa.put(MAPPA_LISTA_NAZIONALITA, listaNazionalita)
        mappa.put(MAPPA_BIO_UOMINI, bioUomini)
        mappa.put(MAPPA_BIO_DONNE, bioDonne)
        mappa.put(MAPPA_BIO_TOTALE, bioTotale)
        if (bioUomini > 0) {
            mappa.put(MAPPA_USATA_UOMINI, true)
        } else {
            mappa.put(MAPPA_USATA_UOMINI, false)
        }// fine del blocco if-else
        if (bioDonne > 0) {
            mappa.put(MAPPA_USATA_DONNE, true)
        } else {
            mappa.put(MAPPA_USATA_DONNE, false)
        }// fine del blocco if-else
        if (bioTotale > 0) {
            mappa.put(MAPPA_USATA, true)
        } else {
            mappa.put(MAPPA_USATA, false)
        }// fine del blocco if-else

        return mappa
    } // fine del metodo

    /**
     * Lista di una mappa per ogni nazionalità distinta
     *
     * La mappa contiene:
     *  -plurale della nazionalità
     *  -lista degli id dei records di Nazionalità che hanno quel plurale
     *  -numero di voci BioGrails maschili che nel campo nazionalitàLink usano i records della lista
     *  -numero di voci BioGrails femminili che nel campo nazionalitàLink usano i records della lista
     */
    public static ArrayList<HashMap<String, ?>> getNazioniMappaAll() {
        ArrayList<HashMap<String, ?>> listaMappe = null
        HashMap<String, ?> mappa = null
        ArrayList<String> listaPlurali
        String plurale
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getNazioniPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            listaMappe = new ArrayList<HashMap<String, ?>>()
            listaPlurali?.each {
                plurale = it
                mappa = getNazioneMappa(plurale)
                listaMappe.add(mappa)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getNazioniMappa: in $tempo $sec")
        }// fine del blocco if

        return listaMappe
    } // fine del metodo

    /**
     * Riga di statistiche delle nazionalità
     *
     */
    public static ArrayList<ArrayList<String>> righeStatistiche() {
        ArrayList<ArrayList<String>> righe = null
        ArrayList<HashMap<String, ?>> listaMappe = null
        HashMap mappa = null
        ArrayList<String> listaColonne
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaMappe = getNazioniMappaAll()
        if (listaMappe && listaMappe.size() > 0) {
            righe = new ArrayList<ArrayList<String>>()
            listaMappe?.each {
                mappa = it
                listaColonne = new ArrayList<String>()
                listaColonne.add((String) mappa[MAPPA_PLURALE])
            } // fine del ciclo each

        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.rigaStatistiche: in $tempo $sec")
        }// fine del blocco if

        return righe
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le nazionalità distinte (per plurale)
     */
    public static ArrayList<String> getNazioniParagrafi() {
        return getNazioniPlurale()
    } // fine del metodo

    /**
     * Lista dei nomi plurali tutte le attività distinte (per plurale)
     */
    public static ArrayList<String> getAttivitaPlurale() {
        return (ArrayList<String>) Attivita.executeQuery("select distinct plurale from Attivita order by plurale")
    } // fine del metodo

    /**
     * Numero totale delle attività distinte presenti nel database
     */
    public static int getAttivitaNum() {
        int numeroAttivita = 0
        ArrayList<String> lista = getAttivitaPlurale()

        if (lista) {
            numeroAttivita = lista.size()
        }// fine del blocco if

        return numeroAttivita
    } // fine del metodo statico

    /**
     * Numero totale delle attività distinte presenti nel database ed utilizzate
     */
    public static int getAttivitaUsateNum() {
        int numeroAttivita = 0
        ArrayList<Attivita> lista = getAttivitaUsate()

        if (lista) {
            numeroAttivita = lista.size()
        }// fine del blocco if

        return numeroAttivita
    } // fine del metodo statico

    /**
     * Numero totale delle attività distinte presenti nel database ed non utilizzate
     */
    public static int getAttivitaNonUsateNum() {
        int numeroAttivita = 0
        ArrayList<Attivita> lista = getAttivitaNonUsate()

        if (lista) {
            numeroAttivita = lista.size()
        }// fine del blocco if

        return numeroAttivita
    } // fine del metodo statico

    /**
     * Lista di tutte le attività distinte
     */
    public static ArrayList<Attivita> getAttivita() {
        ArrayList<Attivita> lista = null
        ArrayList<String> listaPlurali
        String plurale
        Attivita attivita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getAttivitaPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            lista = new ArrayList<Attivita>()

            listaPlurali?.each {
                plurale = it
                attivita = Attivita.findByPlurale(plurale)
                if (attivita) {
                    lista.add(attivita)
                }// fine del blocco if
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivita: in $tempo $sec")
        }// fine del blocco if

        return lista
    } // fine del metodo

    /**
     * Lista di tutte le attività distinte, usate
     */
    public static ArrayList<Attivita> getAttivitaUsate() {
        ArrayList<Attivita> lista = null
        ArrayList<String> listaPlurali
        ArrayList<Attivita> listaTmp = null
        String plurale
        Attivita attivitaTmp
        Attivita attivita
        int numAtt = 0
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        boolean attivitaMultiple = Pref.getBool(LibBio.USA_ATTIVITA_MULTIPLE, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getAttivitaPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            lista = new ArrayList<Attivita>()
            listaPlurali?.each {
                plurale = it
                numAtt = 0
                listaTmp = Attivita.findAllByPlurale(plurale)
                if (listaTmp && listaTmp.size() > 0) {
                    listaTmp.each {
                        attivitaTmp = it
                        if (attivitaMultiple) {
                            numAtt += BioGrails.countByAttivitaLinkOrAttivita2LinkOrAttivita3Link(attivitaTmp, attivitaTmp, attivitaTmp)
                        } else {
                            numAtt += BioGrails.countByAttivitaLink(attivitaTmp)
                        }// fine del blocco if-else
                    }// fine di each
                }// fine del blocco if
                if (numAtt > 0) {
                    attivita = Attivita.findByPlurale(plurale)
                    if (attivita) {
                        lista.add(attivita)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaUsate: in $tempo $sec")
        }// fine del blocco if

        return lista
    } // fine del metodo

    /**
     * Lista di tutte le attività distinte, non usate
     */
    public static ArrayList<Attivita> getAttivitaNonUsate() {
        ArrayList<Attivita> listaNonUsate = null
        ArrayList<Attivita> listaAll
        ArrayList<Attivita> listaUsate
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaAll = getAttivita()
        listaUsate = getAttivitaUsate()
        if (listaAll && listaUsate) {
            listaNonUsate = listaAll - listaUsate
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaNonUsate: in $tempo $sec")
        }// fine del blocco if

        return listaNonUsate
    } // fine del metodo

    /**
     * Lista dei nomi plurali per le attività usate
     */
    public static ArrayList<String> getAttivitaPluraleUsate() {
        ArrayList<String> listaPlurali = null
        ArrayList<Attivita> listaUsate
        Attivita attivita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaUsate = getAttivitaUsate()
        if (listaUsate && listaUsate.size() > 0) {
            listaPlurali = new ArrayList<String>()
            listaUsate?.each {
                attivita = it
                listaPlurali.add(attivita.plurale)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaPluraleUsate: in $tempo $sec")
        }// fine del blocco if

        return listaPlurali
    } // fine del metodo

    /**
     * Lista dei nomi plurali per le attività non usate
     */
    public static ArrayList<String> getAttivitaPluraleNonUsate() {
        ArrayList<String> listaPlurali = null
        ArrayList<Attivita> listaNonUsate
        Attivita attivita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaNonUsate = getAttivitaNonUsate()
        if (listaNonUsate && listaNonUsate.size() > 0) {
            listaPlurali = new ArrayList<String>()
            listaNonUsate?.each {
                attivita = it
                listaPlurali.add(attivita.plurale)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaPluraleNonUsate: in $tempo $sec")
        }// fine del blocco if

        return listaPlurali
    } // fine del metodo

    /**
     * Mappa di una attività multipla con suddivisione uomo/donna
     */
    public static LinkedHashMap<String, ?> getAttivitaMappaMultiplaUomoDonna(String nome) {
        LinkedHashMap<String, ?> mappa = new LinkedHashMap<String, ?>()
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        ArrayList<Attivita> listaAttivita
        Attivita attivitaSingola = null
        Attivita attivitaTmp
        int bioPrima = 0
        int bioSeconda = 0
        int bioTerza = 0
        int bioUominiPrima = 0
        int bioUominiSeconda = 0
        int bioUominiTerza = 0
        int bioDonnePrima = 0
        int bioDonneSeconda = 0
        int bioDonneTerza = 0
        int bioTotale
        int bioTotalePrima
        int bioTotaleSeconda
        int bioTotaleTerza
        String nomeMinuscolo = LibTesto.primaMinuscola(nome)
        String titoloParagrafoUomini = ''
        String titoloParagrafoDonne = ''
        boolean usaPlurale
        def generi
        Genere genere
        Genere genereTmp
        String singolare = ''
        String singolareTmp
        boolean usataUomini = false
        boolean usataDonne = false
        int totaleErrato
        String titoloSottopaginaUomini
        String titoloSottopaginaDonne

        listaAttivita = Attivita.findAllByPlurale(nomeMinuscolo)
        if (listaAttivita && listaAttivita.size() > 0) {
            usaPlurale = true
            listaAttivita.each {
                attivitaTmp = it
                bioUominiPrima += BioGrails.countByAttivitaLinkAndSesso(attivitaTmp, UOMO)
                bioDonnePrima += BioGrails.countByAttivitaLinkAndSesso(attivitaTmp, DONNA)
                bioUominiSeconda += BioGrails.countByAttivita2LinkAndSesso(attivitaTmp, UOMO)
                bioDonneSeconda += BioGrails.countByAttivita2LinkAndSesso(attivitaTmp, DONNA)
                bioUominiTerza += BioGrails.countByAttivita3LinkAndSesso(attivitaTmp, UOMO)
                bioDonneTerza += BioGrails.countByAttivita3LinkAndSesso(attivitaTmp, DONNA)

                singolare = attivitaTmp.singolare
                generi = Genere.findAllBySingolare(singolare)
                generi?.each {
                    genere = it
                    if (genere.sesso.equals(UOMO)) {
                        titoloParagrafoUomini = genere.plurale
                    }// fine del blocco if
                    if (genere.sesso.equals(DONNA)) {
                        titoloParagrafoDonne = genere.plurale
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine di each
        } else {
            usaPlurale = false
            attivitaSingola = Attivita.findBySingolare(nomeMinuscolo)
            if (!attivitaSingola) {
                genereTmp = Genere.findByPlurale(nomeMinuscolo)
                if (genereTmp) {
                    singolareTmp = genereTmp.singolare
                    attivitaSingola = Attivita.findBySingolare(singolareTmp)
                }// fine del blocco if
            }// fine del blocco if

            if (attivitaSingola) {
                bioPrima += BioGrails.countByAttivitaLink(attivitaSingola)
                bioSeconda += BioGrails.countByAttivita2Link(attivitaSingola)
                bioTerza += BioGrails.countByAttivita3Link(attivitaSingola)

                singolare = attivitaSingola.singolare
                generi = Genere.findAllBySingolare(singolare)
                generi?.each {
                    genere = it
                    if (genere.sesso.equals(UOMO)) {
                        titoloParagrafoUomini = genere.plurale
                    }// fine del blocco if
                    if (genere.sesso.equals(DONNA)) {
                        titoloParagrafoDonne = genere.plurale
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine del blocco if
        }// fine del blocco if-else

        titoloParagrafoUomini = LibTesto.primaMaiuscola(titoloParagrafoUomini)
        titoloParagrafoDonne = LibTesto.primaMaiuscola(titoloParagrafoDonne)

        mappa.put(MAPPA_NOME_ATTIVITA, nomeMinuscolo)
        if (usaPlurale) {
            bioTotalePrima = bioUominiPrima + bioDonnePrima
            bioTotaleSeconda = bioUominiSeconda + bioDonneSeconda
            bioTotaleTerza = bioUominiTerza + bioDonneTerza
            bioTotale = bioTotalePrima + bioTotaleSeconda + bioTotaleTerza

            if ((bioUominiPrima + bioUominiSeconda + bioUominiTerza) > 0) {
                if (titoloParagrafoUomini) {
                    usataUomini = true
                } else {
                    usataUomini = false
                    if (false) {
                        totaleErrato = bioUominiPrima + bioUominiSeconda + bioUominiTerza
                        println("Ci sono $totaleErrato voci di $nome che hanno il genere maschile errato/mancante")
                    }// fine del blocco if
                }// fine del blocco if-else
            }// fine del blocco if
            if ((bioDonnePrima + bioDonneSeconda + bioDonneTerza) > 0) {
                if (titoloParagrafoDonne) {
                    usataDonne = true
                } else {
                    usataDonne = false
                    if (false) {
                        totaleErrato = bioDonnePrima + bioDonneSeconda + bioDonneTerza
                        println("Ci sono $totaleErrato voci di $nome che hanno il genere femminile errato/mancante")
                    }// fine del blocco if
                }// fine del blocco if-else
            }// fine del blocco if
            if (false) {
                println(nome)
            }// fine del blocco if

            if (bioTotale > 0) {
                mappa.put(MAPPA_USATA, true)
            } else {
                mappa.put(MAPPA_USATA, false)
            }// fine del blocco if-else
            mappa.put(MAPPA_USATA_UOMINI, usataUomini)
            mappa.put(MAPPA_USATA_DONNE, usataDonne)
            mappa.put(MAPPA_LISTA_ATTIVITA, listaAttivita)
            mappa.put(MAPPA_PARAGRAFO_UOMINI, titoloParagrafoUomini)
            mappa.put(MAPPA_PARAGRAFO_DONNE, titoloParagrafoDonne)

            if (titoloParagrafoUomini.equals(titoloParagrafoDonne)) {
                mappa.put(MAPPA_SOTTOPAGINA_UOMINI, titoloParagrafoUomini + ' ' + UOMINI)
                mappa.put(MAPPA_SOTTOPAGINA_DONNE, titoloParagrafoDonne + ' ' + DONNE)
            } else {
                mappa.put(MAPPA_SOTTOPAGINA_UOMINI, titoloParagrafoUomini)
                mappa.put(MAPPA_SOTTOPAGINA_DONNE, titoloParagrafoDonne)
            }// fine del blocco if-else

            mappa.put(MAPPA_BIO_UOMINI_PRIMA, bioUominiPrima)
            mappa.put(MAPPA_BIO_DONNE_PRIMA, bioDonnePrima)
            mappa.put(MAPPA_BIO_TOTALE_PRIMA, bioTotalePrima)
            mappa.put(MAPPA_BIO_UOMINI_SECONDA, bioUominiSeconda)
            mappa.put(MAPPA_BIO_DONNE_SECONDA, bioDonneSeconda)
            mappa.put(MAPPA_BIO_TOTALE_SECONDA, bioTotaleSeconda)
            mappa.put(MAPPA_BIO_UOMINI_TERZA, bioUominiTerza)
            mappa.put(MAPPA_BIO_DONNE_TERZA, bioDonneTerza)
            mappa.put(MAPPA_BIO_TOTALE_TERZA, bioTotaleTerza)
            mappa.put(MAPPA_BIO_TOTALE, bioTotale)
        } else {
            bioTotale = bioPrima + bioSeconda + bioTerza

            if (bioTotale > 0) {
                mappa.put(MAPPA_USATA, true)
            } else {
                mappa.put(MAPPA_USATA, false)
            }// fine del blocco if-else
            if (titoloParagrafoUomini) {
                mappa.put(MAPPA_USATA_UOMINI, true)
            }// fine del blocco if
            if (titoloParagrafoDonne) {
                mappa.put(MAPPA_USATA_DONNE, true)
            }// fine del blocco if
            mappa.put(MAPPA_ATTIVITA, attivitaSingola)
            if (titoloParagrafoUomini) {
                mappa.put(MAPPA_PARAGRAFO_UOMINI, titoloParagrafoUomini)
            }// fine del blocco if
            if (titoloParagrafoDonne) {
                mappa.put(MAPPA_PARAGRAFO_DONNE, titoloParagrafoDonne)
            }// fine del blocco if

            titoloSottopaginaUomini = titoloParagrafoUomini
            titoloSottopaginaDonne = titoloParagrafoDonne
            if (titoloParagrafoUomini.equals(titoloParagrafoDonne)) {
                titoloSottopaginaUomini += ' ' + UOMINI
                titoloSottopaginaDonne += ' ' + DONNE
            }// fine del blocco if
            if (titoloSottopaginaUomini) {
                mappa.put(MAPPA_SOTTOPAGINA_UOMINI, titoloSottopaginaUomini)
            }// fine del blocco if
            if (titoloSottopaginaDonne) {
                mappa.put(MAPPA_SOTTOPAGINA_DONNE, titoloSottopaginaDonne)
            }// fine del blocco if

            mappa.put(MAPPA_BIO_PRIMA, bioPrima)
            mappa.put(MAPPA_BIO_SECONDA, bioSeconda)
            mappa.put(MAPPA_BIO_TERZA, bioTerza)
            mappa.put(MAPPA_BIO_TOTALE, bioTotale)

        }// fine del blocco if-else

        return mappa
    } // fine del metodo

    /**
     * Mappa di una attività multipla indifferenziata uomo/donna
     */
    public static LinkedHashMap<String, ?> getAttivitaMappaMultiplaIndifferenziata(String nome) {
        LinkedHashMap<String, ?> mappa = new LinkedHashMap<String, ?>()
        ArrayList<Attivita> listaAttivita
        Attivita attivitaSingola
        Attivita attivitaTmp = null
        int bioPrima = 0
        int bioSeconda = 0
        int bioTerza = 0
        int bioTotale
        String nomeMinuscolo = LibTesto.primaMinuscola(nome)
        String titoloParagrafo = ''
        String plurale

        listaAttivita = Attivita.findAllByPlurale(nomeMinuscolo)
        if (!listaAttivita) {
            attivitaSingola = Attivita.findBySingolare(nomeMinuscolo)
            if (attivitaSingola) {
                plurale = attivitaSingola.plurale
                listaAttivita = Attivita.findAllByPlurale(plurale)
            }// fine del blocco if
        }// fine del blocco if

        if (listaAttivita && listaAttivita.size() > 0) {
            listaAttivita.each {
                attivitaTmp = it
                bioPrima += BioGrails.countByAttivitaLink(attivitaTmp)
                bioSeconda += BioGrails.countByAttivita2Link(attivitaTmp)
                bioTerza += BioGrails.countByAttivita3Link(attivitaTmp)
            }// fine di each
            if (attivitaTmp) {
                titoloParagrafo = attivitaTmp.plurale
            }// fine del blocco if
        }// fine del blocco if
        bioTotale = bioPrima + bioSeconda + bioTerza
        titoloParagrafo = LibTesto.primaMaiuscola(titoloParagrafo)

        mappa.put(MAPPA_NOME_ATTIVITA, nomeMinuscolo)
        if (bioTotale > 0) {
            mappa.put(MAPPA_USATA, true)
        } else {
            mappa.put(MAPPA_USATA, false)
        }// fine del blocco if-else
        if (listaAttivita && listaAttivita.size() > 1) {
            mappa.put(MAPPA_LISTA_ATTIVITA, listaAttivita)
        } else {
            mappa.put(MAPPA_ATTIVITA, attivitaTmp)
        }// fine del blocco if-else
        mappa.put(MAPPA_PARAGRAFO, titoloParagrafo)
        mappa.put(MAPPA_BIO_PRIMA, bioPrima)
        mappa.put(MAPPA_BIO_SECONDA, bioSeconda)
        mappa.put(MAPPA_BIO_TERZA, bioTerza)
        mappa.put(MAPPA_BIO_TOTALE, bioTotale)

        return mappa
    } // fine del metodo

    /**
     * Mappa di una attività singola con suddivisione uomo/donna
     */
    public static LinkedHashMap<String, ?> getAttivitaMappaSingolaUomoDonna(String nome) {
        LinkedHashMap<String, ?> mappa = new LinkedHashMap<String, ?>()
        ArrayList<Attivita> listaAttivita
        Attivita attivitaSingola = null
        Attivita attivitaTmp
        int bioUomini = 0
        int bioDonne = 0
        int bioTotale
        int bioSingolo = 0
        String nomeMinuscolo = LibTesto.primaMinuscola(nome)
        String titoloParagrafoUomini = ''
        String titoloParagrafoDonne = ''
        boolean usaPlurale
        def generi
        Genere genere
        String singolare
        boolean usataUomini = false
        boolean usataDonne = false

        listaAttivita = Attivita.findAllByPlurale(nomeMinuscolo)
        if (listaAttivita && listaAttivita.size() > 0) {
            usaPlurale = true
            listaAttivita.each {
                attivitaTmp = it
                bioUomini += BioGrails.countByAttivitaLinkAndSesso(attivitaTmp, UOMO)
                bioDonne += BioGrails.countByAttivitaLinkAndSesso(attivitaTmp, DONNA)

                singolare = attivitaTmp.singolare
                generi = Genere.findAllBySingolare(singolare)
                generi?.each {
                    genere = it
                    if (genere.sesso.equals(UOMO)) {
                        titoloParagrafoUomini = genere.plurale
                    }// fine del blocco if
                    if (genere.sesso.equals(DONNA)) {
                        titoloParagrafoDonne = genere.plurale
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine di each
        } else {
            usaPlurale = false
            attivitaSingola = Attivita.findBySingolare(nomeMinuscolo)
            if (attivitaSingola) {
                bioSingolo += BioGrails.countByAttivitaLink(attivitaSingola)

                singolare = attivitaSingola.singolare
                generi = Genere.findAllBySingolare(singolare)
                generi?.each {
                    genere = it
                    if (genere.sesso.equals(UOMO)) {
                        titoloParagrafoUomini = genere.plurale
                    }// fine del blocco if
                    if (genere.sesso.equals(DONNA)) {
                        titoloParagrafoDonne = genere.plurale
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine del blocco if
        }// fine del blocco if-else

        titoloParagrafoUomini = LibTesto.primaMaiuscola(titoloParagrafoUomini)
        titoloParagrafoDonne = LibTesto.primaMaiuscola(titoloParagrafoDonne)

        mappa.put(MAPPA_NOME_ATTIVITA, nomeMinuscolo)
        if (usaPlurale) {
            bioTotale = bioUomini + bioDonne

            if ((bioUomini) > 0) {
                usataUomini = true
            }// fine del blocco if
            if ((bioDonne) > 0) {
                usataDonne = true
            }// fine del blocco if

            if (bioTotale > 0) {
                mappa.put(MAPPA_USATA, true)
            } else {
                mappa.put(MAPPA_USATA, false)
            }// fine del blocco if-else
            mappa.put(MAPPA_USATA_UOMINI, usataUomini)
            mappa.put(MAPPA_USATA_DONNE, usataDonne)
            mappa.put(MAPPA_LISTA_ATTIVITA, listaAttivita)
            mappa.put(MAPPA_PARAGRAFO_UOMINI, titoloParagrafoUomini)
            mappa.put(MAPPA_PARAGRAFO_DONNE, titoloParagrafoDonne)
            mappa.put(MAPPA_BIO_UOMINI, bioUomini)
            mappa.put(MAPPA_BIO_DONNE, bioDonne)
            mappa.put(MAPPA_BIO_TOTALE, bioTotale)
        } else {
            if (bioSingolo > 0) {
                mappa.put(MAPPA_USATA, true)
            } else {
                mappa.put(MAPPA_USATA, false)
            }// fine del blocco if-else
            if (titoloParagrafoUomini) {
                mappa.put(MAPPA_USATA_UOMINI, true)
            }// fine del blocco if
            if (titoloParagrafoDonne) {
                mappa.put(MAPPA_USATA_DONNE, true)
            }// fine del blocco if
            mappa.put(MAPPA_ATTIVITA, attivitaSingola)
            if (titoloParagrafoUomini) {
                mappa.put(MAPPA_PARAGRAFO_UOMINI, titoloParagrafoUomini)
            }// fine del blocco if
            if (titoloParagrafoDonne) {
                mappa.put(MAPPA_PARAGRAFO_DONNE, titoloParagrafoDonne)
            }// fine del blocco if
            mappa.put(MAPPA_BIO_TOTALE, bioSingolo)
        }// fine del blocco if-else

        return mappa
    } // fine del metodo

    /**
     * Mappa di una attività singola indifferenziata uomo/donna
     */
    public static LinkedHashMap<String, ?> getAttivitaMappaSingolaIndifferenziata(String nome) {
        LinkedHashMap<String, ?> mappa = new LinkedHashMap<String, ?>()
        ArrayList<Attivita> listaAttivita
        Attivita attivitaSingola
        Attivita attivitaTmp = null
        int bioTotale = 0
        String nomeMinuscolo = LibTesto.primaMinuscola(nome)
        String titoloParagrafo = ''
        String plurale

        listaAttivita = Attivita.findAllByPlurale(nomeMinuscolo)
        if (!listaAttivita) {
            attivitaSingola = Attivita.findBySingolare(nomeMinuscolo)
            if (attivitaSingola) {
                plurale = attivitaSingola.plurale
                listaAttivita = Attivita.findAllByPlurale(plurale)
            }// fine del blocco if
        }// fine del blocco if

        if (listaAttivita && listaAttivita.size() > 0) {
            listaAttivita.each {
                attivitaTmp = it
                bioTotale += BioGrails.countByAttivitaLink(attivitaTmp)
            }// fine di each
            if (attivitaTmp) {
                titoloParagrafo = attivitaTmp.plurale
            }// fine del blocco if
        }// fine del blocco if
        titoloParagrafo = LibTesto.primaMaiuscola(titoloParagrafo)

        mappa.put(MAPPA_NOME_ATTIVITA, nomeMinuscolo)
        if (bioTotale > 0) {
            mappa.put(MAPPA_USATA, true)
        } else {
            mappa.put(MAPPA_USATA, false)
        }// fine del blocco if-else
        if (listaAttivita && listaAttivita.size() > 1) {
            mappa.put(MAPPA_LISTA_ATTIVITA, listaAttivita)
        } else {
            mappa.put(MAPPA_ATTIVITA, attivitaTmp)
        }// fine del blocco if-else
        mappa.put(MAPPA_PARAGRAFO, titoloParagrafo)
        mappa.put(MAPPA_BIO_TOTALE, bioTotale)

        return mappa
    } // fine del metodo

    /**
     * Mappa di una attività
     *
     * La mappa contiene:
     *  -plurale dell'attività
     *  -lista degli id dei records di Attivita che hanno quel plurale
     *  -numero di voci BioGrails maschili che nel campo attivitàLink (attività2Link e attività3Link) usano i records della lista
     *  -numero di voci BioGrails femminili che nel campo attivitàLink (attività2Link e attività3Link) usano i records della lista
     */
    public static LinkedHashMap<String, ?> getAttivitaMappa(String nome) {
        LinkedHashMap<String, ?> mappa
        boolean usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_ATT, false)
        boolean attivitaMultiple = Pref.getBool(LibBio.USA_ATTIVITA_MULTIPLE, false)

        if (usaSuddivisioneUomoDonna) {
            if (attivitaMultiple) {
                mappa = getAttivitaMappaMultiplaUomoDonna(nome)
            } else {
                mappa = getAttivitaMappaSingolaUomoDonna(nome)
            }// fine del blocco if-else
        } else {
            if (attivitaMultiple) {
                mappa = getAttivitaMappaMultiplaIndifferenziata(nome)
            } else {
                mappa = getAttivitaMappaSingolaIndifferenziata(nome)
            }// fine del blocco if-else
        }// fine del blocco if-else

        return mappa
    } // fine del metodo

    /**
     * Lista di una mappa per ogni attività distinta
     *
     * La mappa contiene:
     *  -plurale dell'attività
     *  -lista degli id dei records di Attivita che hanno quel plurale
     *  -numero di voci BioGrails maschili che nel campo attivitàLink (attività2Link e attività3Link) usano i records della lista
     *  -numero di voci BioGrails femminili che nel campo attivitàLink (attività2Link e attività3Link) usano i records della lista
     */
    public static ArrayList<HashMap<String, ?>> getAttivitaMappaAll() {
        ArrayList<HashMap<String, ?>> listaMappe = null
        LinkedHashMap<String, ?> mappa = null
        ArrayList<String> listaPlurali
        String plurale
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getAttivitaPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            listaMappe = new ArrayList<LinkedHashMap<String, ?>>()
            listaPlurali?.each {
                plurale = it
                mappa = getAttivitaMappa(plurale)
                listaMappe.add(mappa)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / 1000
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaMappa: in $tempo secondi")
        }// fine del blocco if

        return listaMappe
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     * Suddivisi tra uomini e donne
     */
    public static ArrayList<String> getAttivitaParagrafiUomo() {
        ArrayList<String> listaParagrafi = null
        ArrayList<String> listaPlurali
        LinkedHashMap<String, ?> mappaUomini = null
        String plurale
        String titoloParagrafo

        listaPlurali = getAttivitaPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            listaParagrafi = new ArrayList<String>()

            listaPlurali?.each {
                plurale = it
                mappaUomini = getAttivitaMappaMultiplaUomoDonna(plurale)
                if (mappaUomini[MAPPA_USATA_UOMINI]) {
                    titoloParagrafo = mappaUomini[MAPPA_PARAGRAFO_UOMINI]
                    listaParagrafi.add(titoloParagrafo)
                }// fine del blocco if
            } // fine del ciclo each


        }// fine del blocco if

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     * Suddivisi tra uomini e donne
     * Manca: Soprano, Ammiraglio
     */
    public static ArrayList<String> getAttivitaParagrafiDonna() {
        ArrayList<String> listaParagrafi = null
        ArrayList<String> listaPlurali
        LinkedHashMap<String, ?> mappaDonne = null
        String plurale
        String titoloParagrafo

        listaPlurali = getAttivitaPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            listaParagrafi = new ArrayList<String>()

            listaPlurali?.each {
                plurale = it
                mappaDonne = getAttivitaMappaMultiplaUomoDonna(plurale)
                if (mappaDonne[MAPPA_USATA_DONNE]) {
                    titoloParagrafo = mappaDonne[MAPPA_PARAGRAFO_DONNE]
                    listaParagrafi.add(titoloParagrafo)
                }// fine del blocco if
            } // fine del ciclo each

        }// fine del blocco if

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     * Suddivisi tra uomini e donne
     */
    public static ArrayList<String> getAttivitaParagrafiUomoDonna() {
        ArrayList<String> listaParagrafi

        listaParagrafi = getAttivitaParagrafiUomo()
        listaParagrafi += getAttivitaParagrafiDonna()

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     * Indifferenziati. Senza suddivisione tra uomini e donne
     */
    public static ArrayList<String> getAttivitaParagrafiIndifferenziati() {
        ArrayList<String> listaParagrafi = null
        ArrayList<String> listaPlurali
        String plurale
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaPlurali = getAttivitaPlurale()
        if (listaPlurali && listaPlurali.size() > 0) {
            listaParagrafi = new ArrayList<String>()

            listaPlurali?.each {
                plurale = it
                plurale = LibTesto.primaMaiuscola(plurale)
                listaParagrafi.add(plurale)
            } // fine del ciclo each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaParagrafiIndifferenziati: in $tempo $sec")
        }// fine del blocco if

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     * Indifferenziati. Senza suddivisione tra uomini e donne
     */
    public static ArrayList<String> getAttivitaParagrafiIndifferenziatiUsati() {
        ArrayList<String> listaParagrafi = null
        ArrayList<Attivita> listaUsate
        String plurale
        String paragrafo
        Attivita attivita
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempo

        listaUsate = getAttivitaUsate()
        if (listaUsate && listaUsate.size() > 0) {
            listaParagrafi = new ArrayList<String>()
            listaUsate?.each {
                attivita = it
                plurale = attivita.plurale
                paragrafo = LibTesto.primaMaiuscola(plurale)
                listaParagrafi.add(paragrafo)
            }// fine di each
        }// fine del blocco if

        if (debug) {
            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / div
            tempo = LibTesto.formatNum(durata)
            println("LibListe.getAttivitaParagrafiIndifferenziatiUsati: in $tempo $sec")
        }// fine del blocco if

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     */
    public static ArrayList<String> getAttivitaParagrafi() {
        ArrayList<String> listaParagrafi
        boolean usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_ATT, false)

        if (usaSuddivisioneUomoDonna) {
            listaParagrafi = getAttivitaParagrafiUomoDonna()
        } else {
            listaParagrafi = getAttivitaParagrafiIndifferenziati()
        }// fine del blocco if-else

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi per tutte le attività
     */
    public static ArrayList<String> getAttivitaParagrafiUsati() {
        ArrayList<String> listaParagrafi
        boolean usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_ATT, false)

        if (usaSuddivisioneUomoDonna) {
            listaParagrafi = getAttivitaParagrafiUomoDonna()
        } else {
            listaParagrafi = getAttivitaParagrafiIndifferenziatiUsati()
        }// fine del blocco if-else

        return listaParagrafi
    } // fine del metodo

    /**
     * Lista dei titoli dei paragrafi comuni tra uomini e donne
     */
    public static ArrayList<String> getAttivitaParagrafiComuniUomoDonna() {
        ArrayList<String> listaComuni = new ArrayList<String>()
        ArrayList<String> listaParagrafiUomo = getAttivitaParagrafiUomo()
        ArrayList<String> listaParagrafiDonna = getAttivitaParagrafiDonna()
        String paragrafo

        listaParagrafiUomo?.each {
            paragrafo = it

            if (listaParagrafiDonna.contains(paragrafo)) {
                listaComuni.add(paragrafo)
            }// fine del blocco if
        } // fine del ciclo each

        return listaComuni
    } // fine del metodo

    /**
     * Controlla se è un paragrafo comune tra uomini e donne
     */
    public static boolean isAttivitaParagrafoComuneUomoDonna(String titoloParagrafo) {
        boolean status = false
//        ArrayList<String> listaParagrafiUomo = getAttivitaParagrafiUomo()
//        ArrayList<String> listaParagrafiDonna = getAttivitaParagrafiDonna()
        LinkedHashMap<String, ?> mappa = getAttivitaMappaMultiplaUomoDonna(titoloParagrafo)

        def stop
//        if (listaParagrafiUomo.contains(titoloParagrafo) && listaParagrafiDonna.contains(titoloParagrafo)) {
//            status = true
//        }// fine del blocco if

        return status
    } // fine del metodo

}// fine della classe statica
