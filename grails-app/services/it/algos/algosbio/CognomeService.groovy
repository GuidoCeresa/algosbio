package it.algos.algosbio

import it.algos.algoslib.LibMat
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit
import it.algos.algoswiki.WikiLib

//@Transactional(readOnly = false)
class CognomeService {


    static boolean transactional = false
    public static String PATH = 'Progetto:Biografie/Cognomi/Persone di cognome '
    private static String aCapo = '\n'

    private static String PAGINA_PROVA = 'Utente:Biobot/2'
    private tagTitolo = 'Persone di cognome '
    private tagPunti = 'Altre...'
    private boolean titoloParagrafoConLink = true
    private String progetto = 'Progetto:Antroponimi/'

    /**
     * azzera i link tra BioGrails e Cognome
     * cancella i records di antroponimi
     */
    public static void cancellaTutto() {
        cancellaLink()
        cancellaCognomi()
    }// fine del metodo

    /**
     * azzera i link tra BioGrails e Cognome
     */
    public static void cancellaLink() {
        String query = "update BioGrails set cognomeLink=null"
        BioGrails.executeUpdate(query)

        query = "update Cognome set voceRiferimento=null"
        Cognome.executeUpdate(query)
    }// fine del metodo

    /**
     * cancella i records di antroponimi
     */
    public static void cancellaCognomi() {
        def recs = Cognome.list()

        recs?.each {
            it.delete(flush: true)
        } // fine del ciclo each
    }// fine del metodo

    /**
     * Aggiunta nuovi records
     * Vengono creati nuovi records per i cognomi presenti nelle voci (bioGrails) che superano la soglia minima
     * listaCognomiCompleta: circa 263.000
     * listaCognomiUnici: circa 136.000
     * listaCognomiValidi: circa 135.000
     */
    public void aggiunge() {
        ArrayList listaCognomiUniciGrails
        ArrayList<String> listaCognomiValidiGrails
        ArrayList listaCognomiEsistenti
        ArrayList listaCognomiNuovi

        //--recupera una lista 'grezza' di tutti i cognomi usati in BioGrails
        listaCognomiUniciGrails = creaListaCognomiUniciGrails()

        //--elimina tutto ciò che compare oltre al cognome
        listaCognomiValidiGrails = elaboraCognomiValidiGrails(listaCognomiUniciGrails)

        //--recupera una lista di tutti i cognomi
        listaCognomiEsistenti = creaListaCognomi()

        //--considera solo i cognomi non già presenti nel database
        listaCognomiNuovi = differenzaCognomi(listaCognomiValidiGrails, listaCognomiEsistenti)

        //--(ri)costruisce i records di cognomi
        spazzolaPacchetto(listaCognomiNuovi)
    }// fine del metodo

    /**
     * Recupera una lista 'grezza' di tutti i cognomi unici da BioGrails
     * Circa 136.000
     */
    public static ArrayList creaListaCognomiUniciGrails() {
        String query = "select distinct cognome from BioGrails where cognome <>'' order by cognome asc"

        return BioGrails.executeQuery(query)
    }// fine del metodo

    /**
     * Recupera una lista di tutti i cognomi esistenti
     */
    public static ArrayList creaListaCognomi() {
        String query = "select testo  from Cognome order by testo asc"

        return Cognome.executeQuery(query)
    }// fine del metodo

    /**
     * Considera solo i cognomi non già presenti nel database
     *
     * Prende l'ultimo record registrato di Cognome
     * Spazzola la lista di cognomiValidi finché trova l'ultimo cognome già inserito
     * Considera la lista da lì in avanti
     */
    public static ArrayList<String> differenzaCognomi(ArrayList<String> listaCognomiValidiGrails, ArrayList listaCognomiEsistenti) {
        ArrayList<String> listaCognomiNuovi = new ArrayList<String>()
        String cognomeCorrente = ''
        String ultimoCognomeInserito = ''
        boolean copia = false

        if (listaCognomiEsistenti && listaCognomiEsistenti.size() > 0) {
            ultimoCognomeInserito = listaCognomiEsistenti.last()

            listaCognomiValidiGrails?.each {
                cognomeCorrente = it
                if (cognomeCorrente.equals(ultimoCognomeInserito)) {
                    copia = true
                }// fine del blocco if
                if (copia) {
                    listaCognomiNuovi.add(cognomeCorrente)
                }// fine del blocco if
            } // fine del ciclo each
        } else {
            listaCognomiNuovi = listaCognomiValidiGrails
        }// fine del blocco if-else

        return listaCognomiNuovi
    }// fine del metodo

    /**
     * Elabora tutti i cognomi
     * Costruisce una lista di cognomi ''validi'
     * Circa 135.000
     */
    public static ArrayList<String> elaboraCognomiValidiGrails(ArrayList listaCognomiUniciGrails) {
        ArrayList<String> listaCognomiValidi = new ArrayList<String>()
        def valore
        String cognomeDaControllare
        String cognomeValido

        //--costruisce una lista di nomi 'unici'
        listaCognomiUniciGrails?.each {
            valore = it
            if (valore && valore in String) {
                cognomeValido = ' '
                cognomeDaControllare = (String) valore
                if (checkCognome(cognomeDaControllare)) {
                    cognomeValido = cognomeDaControllare
                }// fine del blocco if
                if (cognomeValido) {
                    if (!listaCognomiValidi.contains(cognomeValido)) {
                        listaCognomiValidi.add(cognomeValido)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del ciclo each

        return listaCognomiValidi
    }// fine del metodo

    private static boolean checkCognome(String cognomeIn) {
        return LibBio.checkNome(cognomeIn)
    }// fine del metodo

//    /**
//     * Elabora il singolo cognome
//     * Elimina caratteri 'anomali' dal cognome
//     */
//    private static String checkCognomeOld(String cognomeIn) {
//        String cognomeOut = ''
//        ArrayList listaTagContenuto = new ArrayList()
//        ArrayList listaTagIniziali = new ArrayList()
//        int pos
//        String tagSpazio = ' '
//
//        listaTagContenuto.add('(')
//
//        listaTagIniziali.add('"')
//        listaTagIniziali.add("''")//doppio apice
//        listaTagIniziali.add('ʿʿ')//doppio apostrofo
//        listaTagIniziali.add('‘')//altro tipo di apostrofo
//        listaTagIniziali.add('‛')//altro tipo di apostrofo
//        listaTagIniziali.add('[')
//        listaTagIniziali.add('(')
//        listaTagIniziali.add('.')
//        listaTagIniziali.add('<')
//        listaTagIniziali.add('{')
//        listaTagIniziali.add('&')
//        listaTagIniziali.add('A.')
//        listaTagIniziali.add('-')
//        listaTagIniziali.add('Al ') //arabo - Al più spazio
//
//        String tag = ''
//
//        if (cognomeIn && cognomeIn.length() > 2 && cognomeIn.length() < 100) {
//            cognomeOut = cognomeIn.trim()
//
//            // @todo Maria e Maria Cristina sono uguali
//            if (false) {
//                if (cognomeOut.contains(tagSpazio)) {
//                    pos = cognomeOut.indexOf(tagSpazio)
//                    cognomeOut = cognomeOut.substring(0, pos)
//                    cognomeOut = cognomeOut.trim()
//                }// fine del blocco if
//            }// fine del blocco if
//
//            listaTagContenuto?.each {
//                tag = (String) it
//                if (cognomeOut.contains(tag)) {
//                    pos = cognomeOut.indexOf((String) it)
//                    cognomeOut = cognomeOut.substring(0, pos)
//                    cognomeOut = cognomeOut.trim()
//                }// fine del blocco if
//            } // fine del ciclo each
//
//            listaTagIniziali?.each {
//                tag = (String) it
//
//                if (cognomeOut.startsWith(tag)) {
//                    cognomeOut = ''
//                }// fine del blocco if
//            } // fine del ciclo each
//
//            //nomeOut = nomeOut.toLowerCase()       //@todo va in errore per GianCarlo
//            cognomeOut = LibTesto.primaMaiuscola(cognomeOut)
//
//            //
//            if (false) {
//                cognomeOut = Normalizer.normalize(cognomeOut, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
//            }// fine del blocco if
//
//            if (cognomeOut.length() < 2) {
//                cognomeOut = ''
//            }// fine del blocco if
//        }// fine del blocco if
//
//        return cognomeOut
//    }// fine del metodo

    /**
     * Spazzola la lista di cognomi
     */
    public void spazzolaPacchetto(ArrayList<String> listaCognomiValidi) {
        String testoCognome
        int soglia = Pref.getInt(LibBio.SOGLIA_COGNOMI)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info

        listaCognomiValidi?.each {
            testoCognome = it
            spazzolaCognome(testoCognome, soglia)
            k++
            if (LibMat.avanzamento(k, 10000)) {
                fine = System.currentTimeMillis()
                durata = fine - inizio
                durata = durata / 1000

                info = 'Spazzolati '
                info += LibTesto.formatNum(k)
                info += ' cognomi su '
                info += LibTesto.formatNum(listaCognomiValidi.size())
                info += ' in '
                info += LibTesto.formatNum(durata)
                info += ' sec. totali'
                log.info info
            }// fine del blocco if
        } // fine del ciclo each
        log.info 'Spazzolati tutti'
    }// fine del metodo

    /**
     * Controlla il singolo cognome
     * Controlla la soglia minima
     * Crea un record per ogni cognome non ancora esistente (se supera la soglia)
     */
    public void spazzolaCognome(String testoCognome, int soglia) {
        int numVoci

        if (testoCognome.equals('Acquaviva')) {
            def stop
        }// fine del blocco if

        if (testoCognome) {
            numVoci = numBioCheUsanoCognome(testoCognome)
            if (numVoci > soglia) {
                numeroVociCheUsanoCognome(testoCognome)
                registraSingoloCognome(testoCognome, numVoci, true)
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo

    /**
     * Registra il singolo record
     */
    public static Cognome registraSingoloCognome(String cognome, int numVoci, boolean vocePrincipale) {
        return registraSingoloCognome(cognome, numVoci, vocePrincipale, null)
    }// fine del metodo

    /**
     * Registra il singolo record
     */
    public
    static Cognome registraSingoloCognome(String testo, int numVoci, boolean vocePrincipale, Cognome voceRiferimento) {
        Cognome cognome = Cognome.findByTesto(testo)

        if (cognome == null) {
            cognome = new Cognome()
            cognome.testo = testo
            cognome.voci = numVoci
            cognome.lunghezza = testo.length()
            cognome.voceRiferimento = voceRiferimento
            cognome.isVocePrincipale = vocePrincipale
            cognome.save(flush: true)
        }// fine del blocco if

        return cognome
    }// fine del metodo

    private int numBioCheUsanoCognome(String testo) {
        int numVoci = 0
        String sep = "'"

        if (testo.contains(sep)) {
            testo = testo.replace(sep, sep + sep)
        }// fine del blocco if

        if (testo) {
            numVoci = BioGrails.countByCognome(testo)
        }// fine del blocco if-else

        return numVoci
    }// fine del metodo

    private int numeroVociCheUsanoCognome(String testo) {
        return numeroVociCheUsanoCognome(testo, null)
    }// fine del metodo

    private numeroVociCheUsanoCognome(String testo, Cognome cognome) {
        int numVoci = 0
        String query = ''
        String sep = "'"
        String tagSpazio = ' '
        String tagWillCard = '%'
        String testoWillCardA
        String nomeWillCardB = tagWillCard + tagSpazio + testo // non usato

        if (testo.contains(sep)) {
            testo = testo.replace(sep, sep + sep)
        }// fine del blocco if
        testoWillCardA = testo + tagSpazio + tagWillCard

        if (cognome) {
            query += "update BioGrails set cognomeLink="
            query += cognome.id
            query += " where cognome="
            query += sep + testo + sep
//            query += " or nome like "
//            query += sep + testoWillCardA + sep
            try { // prova ad eseguire il codice
                numVoci = BioGrails.executeUpdate(query)
            } catch (Exception unErrore) { // intercetta l'errore
                log.warn 'Errore numeroVociCheUsanoCognome = ' + testo
            }// fine del blocco try-catch
        } else {
//            numVoci = BioGrails.countByCognomeLikeOrCognomeLike(testo, testoWillCardA)
        }// fine del blocco if-else

        return numVoci
    }// fine del metodo

    /**
     * Ricalcolo records esistenti <br>
     * Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (cognome) <br>
     * Cancella i records che non superano il check di validità <br>
     * Cancella i records che non superano la soglia minima <br>
     */
    public void ricalcola() {
        ArrayList<Cognome> listaCognomi
//        int numVociDaRicalcolare = Pref.getInt(LibBio.MAX_RICALCOLA_COGNOMI)
//        int soglia = Pref.getInt(LibBio.SOGLIA_COGNOMI)
//        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info

//        listaCognomi = Cognome.listOrderByVoci([max: numVociDaRicalcolare, sort: 'voci', order: 'asc'])
//        listaCognomi = Cognome.listOrderByTesto([max: numVociDaRicalcolare])//@todo via
        listaCognomi = Cognome.listOrderByTesto()
        listaCognomi?.each {
            ricalcolaCognome(it)
            k++
            if (LibMat.avanzamento(k, 100)) {
                fine = System.currentTimeMillis()
                durata = fine - inizio
                durata = durata / 1000

                info = 'Ricalcolate '
                info += LibTesto.formatNum(k)
                info += ' voci di cognomi su '
                info += LibTesto.formatNum(listaCognomi.size())
                info += ' in '
                info += LibTesto.formatNum(durata)
                info += ' sec. totali'
                log.info info
            }// fine del blocco if
        } // fine del ciclo each
        log.info 'Ricalcolati tutti i cognomi'

//        cancellaControllati()
    }// fine del metodo

    private static void cancellaControllati() {
//        String query = 'delete from Cognome where voci=' + TAG_CONTROLLATE
//        Cognome.executeUpdate(query)
    }// fine del metodo

    private void ricalcolaCognome(Cognome cognome) {
        if (cognome) {
            cognome.voci = numeroVociCheUsanoCognome(cognome.testo, cognome)
            cognome.save(flush: true)
        }// fine del blocco if
    }// fine del metodo

//    private void ricalcolaCognome(Cognome cognome, int soglia, int taglio) {
//        int numVoci
//
//        if (checkCognome(cognome.testo)) {
//            numVoci = numeroVociCheUsanoCognome(cognome.testo, cognome)
//            if (numVoci > soglia) {
//                cognome.voci = numVoci
//                cognome.save(flush: true)
//            } else {
////                cognome.voci = TAG_CONTROLLATE
////                cognome.save(flush: true)
//            }// fine del blocco if-else
//
//            //--riempimento del campo wikiUrl di Cognomi
//            regolaWikilink(cognome, taglio)
//
//        } else {
//            try { // prova ad eseguire il codice
////                cognome.delete()
//            } catch (Exception unErrore) { // intercetta l'errore
//                log.error unErrore
//            }// fine del blocco try-catch
//        }// fine del blocco if-else
//
//    }// fine del metodo

    // Elabora tutte le pagine
    def elabora() {
        ArrayList<String> listaCognomi
        String query = 'select distinct cognome from BioGrails order by cognome asc'
        String cognome
        int numCicliMax = Pref.getInt(LibBio.MAX_CICLI_ELABORA_COGNOMI, 10000)
        int numCicli
        int numListaCognomi
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String tempoTxt

        resetAll()
        listaCognomi = (ArrayList<String>) BioGrails.executeQuery(query)
        numListaCognomi = listaCognomi.size()
        fine = System.currentTimeMillis()
        durata = fine - inizio
        durata = durata / 1000
        tempoTxt = LibTesto.formatNum(durata)
        log.info "Cancellati tutti i cognomi e caricata la lista iniziale in ${tempoTxt} secondi"

        numCicli = LibMat.minimoPositivo(numListaCognomi, numCicliMax)
        for (int k = 0; k < numCicli; k++) {
            cognome = listaCognomi.get(k)
            if (cognome && checkCognome(cognome)) {
                elaboraCognome(cognome)
            }// fine del blocco if
        } // fine del ciclo for
        fine = System.currentTimeMillis()
        durata = fine - inizio
        durata = durata / 1000
        tempoTxt = LibTesto.formatNum(durata)
        log.info "Ciclo completo di creazione di ${numCicli} records di cognomi in ${tempoTxt} secondi"
    }// fine del metodo

    //--upload di tutti i cognomi
    public upload() {
        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI, 100)
        ArrayList<Cognome> listaCognomi = Cognome.findAllByVociGreaterThanEquals(taglio, [order: 'cognome'])
        Cognome cognome
        String testo

        listaCognomi?.each {
            cognome = (Cognome) it
            testo = cognome.testo
            if (LibBio.checkNome(testo)) {
                upload(cognome)
            }// fine del blocco if
        }// fine del ciclo each

        // creaPaginaControllo()
        def stop
    }// fine del metodo

    //--creazione ed upload sul server della singola pagina
    //--comprese eventuali sottopagine
    public upload(Cognome cognome) {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        ArrayList<BioGrails> listaBiografie = getListaBiografie(cognome)
        String summary = LibBio.getSummary()
        String testo = ''
        String titolo
        String cognomeTxt = ''

        if (cognome) {
            cognomeTxt = cognome.testo
        }// fine del blocco if

        titolo = PATH + cognomeTxt

        //header
        testo += this.getHead(cognome, listaBiografie.size())

        //body
        testo += this.getBody(listaBiografie, true)

        //footer
        testo += this.getFooter(cognome)

        testo = testo.trim()

        //registra la pagina
        if (!debug) {
            new EditBio(titolo, testo, summary)
        }// fine del blocco if

        def stop
    }// fine del metodo

    public String getHead(Cognome cognome, int num) {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI)
        String numero = ''
        String incipit
        String tagIndice = '__FORCETOC__'
        String tagNoIndice = '__NOTOC__'
        String cognomeTxt = cognome.testo

        if (num) {
            numero = LibTesto.formatNum(num)
        }// fine del blocco if

        if (usaTavolaContenuti) {
            testo += tagIndice
        } else {
            testo += tagNoIndice
        }// fine del blocco if-else
        testo += aCapo

        testo += '<noinclude>'
        testo += aCapo
        testo += "{{StatBio"
        if (numero) {
            testo += "|bio=$numero"
        }// fine del blocco if
        testo += "|data=$dataCorrente}}"
        testo += aCapo

        incipit = "Questa è una lista di persone presenti nell'enciclopedia che hanno il cognome '''${cognomeTxt}''', suddivise per attività principale."
        testo += incipit
        testo += aCapo
        testo += aCapo

        return testo
    }// fine del metodo

    public String getBody(ArrayList<BioGrails> listaBiografie, boolean usaSottopagine) {
        String testo = ''
        Map mappa
        String chiave
        ArrayList<String> listaDidascalie
        int num = 0
        int maxVoci = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_LOCALITA, 100)

        mappa = antroponimoService.getMappaAttività(listaBiografie)
        mappa = antroponimoService.ordinaMappa(mappa)
        if (mappa) {
            mappa?.each {
                chiave = it.key
                listaDidascalie = (ArrayList<String>) mappa.get(chiave)
                num = listaDidascalie.size()
                if (usaSottopagine && num >= maxVoci) {
                    testo += getBodyPagina(chiave, listaDidascalie)
                } else {
                    testo += getBodyPagina(chiave, listaDidascalie)
                }// fine del blocco if-else
            }// fine del ciclo each
        }// fine del blocco if

        return testo
    }// fine del metodo

    public String getBodyPagina(String chiave, ArrayList listaDidascalie) {
        String testo = ''
        String tag = '=='

        testo += tag
        testo += chiave
        testo += tag
        testo += aCapo
        testo += antroponimoService.getParagrafoDidascalia(listaDidascalie)
        testo += aCapo
        testo += aCapo

        return testo
    }// fine del metodo

    public String getFooter(Cognome cognome) {
        String testo = ''
        String cognomeTxt = cognome.testo

        testo += '<noinclude>'
        testo += "[[Categoria:Bio cognomi|${cognomeTxt}]]"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo

    //--cancella tutti i records
    private static resetAll() {
        Cognome.executeUpdate('delete Cognome')
    }// fine del metodo


    private static elaboraCognome(String testo) {
        Cognome cognome = new Cognome()
        int numVoci = BioGrails.countByCognome(testo)

        cognome.testo = testo
        cognome.voci = numVoci
        cognome.lunghezza = testo.length()
        cognome.save(flush: true)
    }// fine del metodo

    //--costruisce una lista di biografie che 'usano' il cognome
    private static ArrayList<BioGrails> getListaBiografie(Cognome cognome) {
        ArrayList<BioGrails> listaBiografie = null
        String cognomeTxt

        if (cognome) {
            cognomeTxt = cognome.testo
        }// fine del blocco if

        if (cognomeTxt) {
            listaBiografie = BioGrails.findAllByCognome(cognomeTxt, [sort: 'forzaOrdinamento'])
        }// fine del blocco if

        return listaBiografie
    }// fine del metodo

    //--riempimento del campo wikiUrl di Cognomi
    private static void regolaWikilink(Cognome cognome, int taglio) {
        Cognome cognomeRiferimento
        String url

        url = 'https://it.wikipedia.org/wiki/Persone di cognome ' + cognome.testo
        if (cognome.isVocePrincipale) {
            if (cognome.wikiUrl) {
            } else {
                if (cognome.voci > taglio) {
                    cognome.wikiUrl = url
                } else {
                    cognome.wikiUrl = ''
                }// fine del blocco if-else
                cognome.save(flush: true)
            }// fine del blocco if-else
        } else {
            cognomeRiferimento = cognome.voceRiferimento
            if (cognomeRiferimento) {
                cognome.wikiUrl = cognomeRiferimento.wikiUrl
            } else {
                cognome.wikiUrl = ''
            }// fine del blocco if-else
            cognome.save(flush: true)
        }// fine del blocco if-else
    }// fine del metodo


    public creaPagineControllo() {
        //crea la pagina riepilogativa
        paginaCognomi()

        //crea le pagine di riepilogo di tutti i cognomi
        paginaListe()

        //crea la pagina di controllo didascalie
//        paginaDidascalie()
        def stop
    }// fine del metodo

    /**
     * Crea la pagina riepilogativa
     */
    public paginaCognomi() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        ArrayList<Cognome> listaVoci = getListaCognomiNomi()
        String testo = ''
        String titolo = progetto + 'Cognomi'
        String summary = 'Biobot'
        def nonServe

        if (listaVoci) {
            testo += getNomiHead()
            testo += getNomiBody(listaVoci)
            testo += getNomiFooter()
        }// fine del blocco if

        //registra la pagina
        if (testo) {
            testo = testo.trim()
            if (debug) {
                testo = LibWiki.setBold(titolo) + aCapo + testo
                nonServe = new Edit(PAGINA_PROVA, testo, summary)
            } else {
                nonServe = new Edit(titolo, testo, summary)
            }// fine del blocco if-else
        }// fine del blocco if
        def stop
    }// fine del metodo


    public String getNomiHead() {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())

        testo += '__NOTOC__'
        testo += '<noinclude>'
        testo += "{{StatBio|data=$dataCorrente}}"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo


    public String getNomiBody(ArrayList<Cognome> listaVoci) {
        String testo = ''
        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI)
        Cognome cognome
        def ricorrenze = LibTesto.formatNum(taglio)

        testo += '==Cognomi=='
        testo += aCapo
        testo += 'Elenco dei '
        testo += "''' "
        testo += LibTesto.formatNum(listaVoci.size())
        testo += "'''"
        testo += ' cognomi che hanno più di '
        testo += "'''"
        testo += ricorrenze
        testo += "'''"
        testo += ' ricorrenze nelle voci biografiche'
        testo += aCapo

        testo += aCapo
        testo += '{{Div col|cols=4}}'
        if (listaVoci) {
            listaVoci.each {
                cognome = it
                testo += aCapo
                testo += this.getRiga(cognome)
            }// fine del ciclo each
        }// fine del blocco if
        testo += aCapo
        testo += '{{Div col end}}'
        testo += aCapo

        return testo
    }// fine del metodo


    public String getRiga(Cognome cognome) {
        String testo = ''
        String nome = cognome.testo
        String tag = tagTitolo + nome
        int numVoci

        if (nome) {
            testo += '*'
            testo += '[['
            testo += tag
            testo += '|'
            testo += nome
            testo += ']]'
            if (Pref.getBool(LibBio.USA_OCCORRENZE_ANTROPONIMI)) {
                numVoci = ListaCognome.getNumVoci(cognome)
                testo += ' ('
                testo += "'''"
                testo += LibTesto.formatNum(numVoci)
                testo += "'''"
                testo += ' )'
            }// fine del blocco if
            testo += aCapo
        }// fine del blocco if

        return testo.trim()
    }// fine del metodo


    public String getNomiFooter() {
        String testo = ''

//        testo += getCriteri() @todo da fare

        testo += aCapo
        testo += '==Voci correlate=='
        testo += aCapo
        testo += '*[[Progetto:Antroponimi]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Liste cognomi]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Didascalie]]'
        testo += aCapo
        testo += aCapo
        testo += '<noinclude>'
        testo += '[[Categoria:Liste di persone per cognome| ]]'
        testo += aCapo
        testo += '[[Categoria:Progetto Antroponimi|Cognomi]]'
        testo += '</noinclude>'

        return testo
    }// fine del metodo


    def paginaListe() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI)
        String testo = ''
        String titolo = progetto + 'Liste cognomi'
        String summary = LibBio.getSummary()
        int k = 0
        ArrayList<Cognome> listaVoci = getListaCognomiVoci()
        Cognome cognome
        ArrayList lista = new ArrayList()
        String nome
        int numVoci
        String vociTxt
        def nonServe

        listaVoci?.each {
            vociTxt = ''
            cognome = (Cognome) it
            nome = cognome.testo
            numVoci = ListaCognome.getNumVoci(cognome)
            if (numVoci > taglio) {
                nome = "'''[[Persone di cognome " + nome + "|" + nome + "]]'''"
            }// fine del blocco if-else
            k++
            vociTxt = LibTesto.formatNum(numVoci)
            lista.add([nome, numVoci])
        } // fine del ciclo each

        testo += getListeHead(k)
        testo += getListeBody(lista)
        testo += getListeFooter()

        //registra la pagina
        if (testo) {
            testo = testo.trim()
            if (debug) {
                testo = LibWiki.setBold(titolo) + aCapo + testo
                nonServe = new Edit(PAGINA_PROVA, testo, summary)
            } else {
                nonServe = new Edit(titolo, testo, summary)
            }// fine del blocco if-else
        }// fine del blocco if
        def stop
    }// fine del metodo


    private static String getListeHead(int numCognomi) {
        String testoTitolo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        int soglia = Pref.getInt(LibBio.SOGLIA_COGNOMI)
        int numBio = BioGrails.count()

        testoTitolo += '<noinclude>'
        testoTitolo += "{{StatBio|data=$dataCorrente}}"
        testoTitolo += '</noinclude>'
        testoTitolo += aCapo
        testoTitolo += '==Cognomi=='
        testoTitolo += aCapo
        testoTitolo += "Elenco dei '''"
        testoTitolo += LibTesto.formatNum(numCognomi)
        testoTitolo += "''' cognomi '''differenti''' "
        testoTitolo += " utilizzati nelle '''"
        testoTitolo += LibTesto.formatNum(numBio)
        testoTitolo += "''' voci biografiche con occorrenze maggiori di '''"
        testoTitolo += soglia
        testoTitolo += "'''"
        testoTitolo += aCapo
        testoTitolo += aCapo

        return testoTitolo
    }// fine del metodo

    //costruisce il testo della tabella
    private static String getListeBody(ArrayList listaVoci) {
        String testoTabella
        Map mappa = new HashMap()
        ArrayList titoli = new ArrayList()
        titoli.add(LibWiki.setBold('Cognome'))
        titoli.add(LibWiki.setBold('Voci'))

        mappa.put(WikiLib.MAPPA_TITOLI, titoli)
        mappa.put(WikiLib.MAPPA_LISTA, listaVoci)
        testoTabella = WikiLib.creaTable(mappa)

        return testoTabella
    }// fine del metodo


    private static String getListeFooter() {
        String testoFooter = ''

//        testoFooter += getCriteri() @todo

        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '==Voci correlate=='
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Cognomi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Didascalie]]'
        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '<noinclude>'
        testoFooter += '[[Categoria:Liste di persone per cognome| ]]'
        testoFooter += aCapo
        testoFooter += '[[Categoria:Progetto Antroponimi|Liste cognomi]]'
        testoFooter += '</noinclude>'

        return testoFooter
    }// fine del metodo

    /**
     * Elabora il singolo cognome
     * Elimina caratteri 'anomali' dal cognome
     */
    private static String check(String cognomeIn) {
        String cognomeOut = cognomeIn

        return cognomeOut
    }// fine del metodo

    private static numeroVociCheUsanoNome(String nome) {
        return BioGrails.countByCognome(nome)
    }// fine del metodo

    /**
     * Ritorna il cognome dal link alla voce
     * Se non esiste, lo crea
     */
    public Cognome getCognome(String nomeDaControllare) {
        Cognome cognome = null
        String nome = ''
        int soglia = Pref.getInt(LibBio.SOGLIA_COGNOMI)
        int voci = 0

        if (nomeDaControllare) {
            nome = check(nomeDaControllare)
        }// fine del blocco if

        if (nome) {
            voci = numeroVociCheUsanoNome(nome)
        }// fine del blocco if

        if (nome) {
            cognome = Cognome.findByTesto(nome)
            if (!cognome) {
                if (voci > soglia) {
                    cognome = new Cognome(testo: nome, voci: voci)
                    cognome.save(flush: true)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return cognome
    } // fine del metodo

    //--costruisce una lista di cognomi
    public static ArrayList<String> getListaTxtCognomi() {
        ArrayList<String> listaCognomi
        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI, 100)
        String query = "select testo from Cognome where voci>'${taglio}' order by testo asc"

        //esegue la query
        listaCognomi = (ArrayList<String>) Cognome.executeQuery(query)

        return listaCognomi
    }// fine del metodo

    //--costruisce una lista di cognomi
    public static ArrayList<Cognome> getListaCognomi() {
        return getListaCognomiNomi()
    }// fine del metodo

    //--costruisce una lista di cognomi
    public static ArrayList<Cognome> getListaCognomiNomi() {
        return getListaCognomi(Pref.getInt(LibBio.TAGLIO_COGNOMI), 'testo', 'asc')
    }// fine del metodo

    //--costruisce una lista di cognomi
    public static ArrayList<Cognome> getListaCognomiVoci() {
        return getListaCognomi(Pref.getInt(LibBio.SOGLIA_COGNOMI), 'voci', 'desc')
    }// fine del metodo

    //--costruisce una lista di cognomi
    public static ArrayList<Cognome> getListaCognomi(int sogliaTaglio, String sort, String order) {
        ArrayList<Cognome> lista = null
        ArrayList<Cognome> listaTmp

        listaTmp = Cognome.findAllByVociGreaterThan(sogliaTaglio, [sort: sort, order: order])

        if (listaTmp) {
            lista = new ArrayList<Cognome>()
            listaTmp.each {
                if (LibBio.checkNome(it.testo)) {
                    lista.add(it)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return lista
    }// fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea tutti i cognomi
     */
    def int uploadAllCognomi(BioService bioService) {
        int cognomiModificati = 0
        ArrayList<Cognome> listaCognomi = getListaCognomi()
        Cognome cognome
        String testo

        listaCognomi?.each {
            cognome = (Cognome) it
            testo = cognome.testo
            if (LibBio.checkNome(testo)) {
                if (cognome.isVocePrincipale) {
                    if (ListaCognome.uploadCognome(it, bioService)) {
                        cognomiModificati++
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del ciclo each

        def stop

        return cognomiModificati
    } // fine del metodo

} // fine della service classe
