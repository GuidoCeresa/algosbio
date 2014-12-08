package it.algos.algosbio

import grails.transaction.Transactional
import it.algos.algoslib.LibMat
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algospref.Pref

import java.text.Normalizer

@Transactional
class CognomeService {


    public static String PATH = 'Progetto:Biografie/Cognomi/Persone di cognome '
    private static String aCapo = '\n'

    private tagTitolo = 'Persone di cognome '
    private tagPunti = 'Altre...'
    private boolean titoloParagrafoConLink = true
    private String progetto = 'Progetto:Antroponimi/'
    public static int TAG_DA_CONTROLLARE = -1
    public static int TAG_CONTROLLATE = -2

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
     * listaCognomiCompleta: circa 132.000
     * listaCognomiUnici: circa 130.000
     */
    public void aggiunge() {
        ArrayList<String> listaCognomiCompleta
        ArrayList<String> listaCognomiUnici

        //--recupera una lista 'grezza' di tutti i nomi
        listaCognomiCompleta = creaListaCognomiCompleta()

        //--elimina tutto ciò che compare oltre al cognome
        listaCognomiUnici = elaboraCognomiUnici(listaCognomiCompleta)

        //--(ri)costruisce i records di cognomi
        spazzolaPacchetto(listaCognomiUnici)

        //--aggiunge i riferimenti alla voce principale di ogni record
//        elaboraVocePrincipale()
    }// fine del metodo

    /**
     * Recupera una lista 'grezza' di tutti i cognomi
     * Circa 130.000
     */
    private static ArrayList<String> creaListaCognomiCompleta() {
        String query = "select distinct cognome from BioGrails where cognome <>'' order by cognome asc"
        return (ArrayList<String>) BioGrails.executeQuery(query)
    }// fine del metodo

    /**
     * Elabora tutti i cognomi
     * Costruisce una lista di cognomi ''validi' e 'unici'
     */
    public static ArrayList<String> elaboraCognomiUnici(ArrayList<String> listaCognomiCompleta) {
        ArrayList<String> listaCognomiUnici = new ArrayList<String>()
        String cognomeDaControllare
        String cognomeValido

        //--costruisce una lista di nomi 'unici'
        listaCognomiCompleta?.each {
            cognomeValido = ' '
            cognomeDaControllare = (String) it
            if (checkCognome(cognomeDaControllare)) {
                cognomeValido = cognomeDaControllare
            }// fine del blocco if
            if (cognomeValido) {
                if (!listaCognomiUnici.contains(cognomeValido)) {
                    listaCognomiUnici.add(cognomeValido)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del ciclo each

        return listaCognomiUnici
    }// fine del metodo

    private static boolean checkCognome(String cognomeIn) {
        return LibBio.checkNome(cognomeIn)
    }// fine del metodo

    /**
     * Elabora il singolo cognome
     * Elimina caratteri 'anomali' dal cognome
     */
    private static String checkCognomeOld(String cognomeIn) {
        String cognomeOut = ''
        ArrayList listaTagContenuto = new ArrayList()
        ArrayList listaTagIniziali = new ArrayList()
        int pos
        String tagSpazio = ' '

        listaTagContenuto.add('(')

        listaTagIniziali.add('"')
        listaTagIniziali.add("''")//doppio apice
        listaTagIniziali.add('ʿʿ')//doppio apostrofo
        listaTagIniziali.add('‘')//altro tipo di apostrofo
        listaTagIniziali.add('‛')//altro tipo di apostrofo
        listaTagIniziali.add('[')
        listaTagIniziali.add('(')
        listaTagIniziali.add('.')
        listaTagIniziali.add('<')
        listaTagIniziali.add('{')
        listaTagIniziali.add('&')
        listaTagIniziali.add('A.')
        listaTagIniziali.add('-')
        listaTagIniziali.add('Al ') //arabo - Al più spazio

        String tag = ''

        if (cognomeIn && cognomeIn.length() > 2 && cognomeIn.length() < 100) {
            cognomeOut = cognomeIn.trim()

            // @todo Maria e Maria Cristina sono uguali
            if (false) {
                if (cognomeOut.contains(tagSpazio)) {
                    pos = cognomeOut.indexOf(tagSpazio)
                    cognomeOut = cognomeOut.substring(0, pos)
                    cognomeOut = cognomeOut.trim()
                }// fine del blocco if
            }// fine del blocco if

            listaTagContenuto?.each {
                tag = (String) it
                if (cognomeOut.contains(tag)) {
                    pos = cognomeOut.indexOf((String) it)
                    cognomeOut = cognomeOut.substring(0, pos)
                    cognomeOut = cognomeOut.trim()
                }// fine del blocco if
            } // fine del ciclo each

            listaTagIniziali?.each {
                tag = (String) it

                if (cognomeOut.startsWith(tag)) {
                    cognomeOut = ''
                }// fine del blocco if
            } // fine del ciclo each

            //nomeOut = nomeOut.toLowerCase()       //@todo va in errore per GianCarlo
            cognomeOut = LibTesto.primaMaiuscola(cognomeOut)

            //
            if (false) {
                cognomeOut = Normalizer.normalize(cognomeOut, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
            }// fine del blocco if

            if (cognomeOut.length() < 2) {
                cognomeOut = ''
            }// fine del blocco if
        }// fine del blocco if

        return cognomeOut
    }// fine del metodo

    /**
     * Spazzola la lista di cognomi
     */
    public void spazzolaPacchetto(ArrayList<String> listaCognomiUnici) {
        int soglia = Pref.getInt(LibBio.SOGLIA_COGNOMI)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info

//        for (int j = 0; j < 2000; j++) {
//            spazzolaCognome(listaCognomiUnici.get(j), soglia)
//        } // fine del ciclo for

        listaCognomiUnici?.each {
            spazzolaCognome(it, soglia)
            k++
            if (LibMat.avanzamento(k, 1000)) {
                fine = System.currentTimeMillis()
                durata = fine - inizio
                durata = durata / 1000

                info = 'Spazzolati '
                info += LibTesto.formatNum(k)
                info += ' cognomi su '
                info += LibTesto.formatNum(listaCognomiUnici.size())
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
    private void spazzolaCognome(String cognome, int soglia) {
        int numVoci

//        if (cognome) {
//            numVoci = numeroVociCheUsanoCognome(cognome)
//            if (numVoci > soglia) {
//                registraSingoloCognome(cognome, numVoci, true)
//            }// fine del blocco if
//        }// fine del blocco if

        if (cognome) {
            numVoci = TAG_DA_CONTROLLARE
            registraSingoloCognome(cognome, numVoci, true)
        }// fine del blocco if
    }// fine del metodo

    /**
     * Registra il singolo record
     */
    private static Cognome registraSingoloCognome(String cognome, int numVoci, boolean vocePrincipale) {
        return registraSingoloCognome(cognome, numVoci, vocePrincipale, null)
    }// fine del metodo

    /**
     * Registra il singolo record
     */
    private
    static Cognome registraSingoloCognome(String testo, int numVoci, boolean vocePrincipale, Cognome voceRiferimento) {
        Cognome cognome = Cognome.findByTesto(testo)

        if (cognome == null) {
            cognome = new Cognome()
            cognome.testo = testo
            cognome.voci = numVoci
            cognome.lunghezza = testo.length()
            cognome.voceRiferimento = voceRiferimento
            cognome.isVocePrincipale = vocePrincipale
            cognome.save()
        }// fine del blocco if

        return cognome
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
            numVoci = BioGrails.countByCognomeLikeOrCognomeLike(testo, testoWillCardA)
        }// fine del blocco if-else

        return numVoci
    }// fine del metodo

    /**
     * Ricalcolo records esistenti <br>
     * Controllo della pagina Progetto:Antroponimi/Nomi doppi <br>
     * Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (antroponimo) <br>
     * Cancella i records che non superano il check di validità <br>
     * Cancella i records che non superano la soglia minima <br>
     */
    public void ricalcola() {
        ArrayList<Cognome> listaCognomi
        int numVociDaRicalcolare = Pref.getInt(LibBio.MAX_RICALCOLA_COGNOMI)
        int soglia = Pref.getInt(LibBio.SOGLIA_COGNOMI)
        int taglio = Pref.getInt(LibBio.TAGLIO_COGNOMI)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info

        listaCognomi = Cognome.listOrderByVoci([max: numVociDaRicalcolare, sort: 'voci', order: 'asc'])
        listaCognomi?.each {
            ricalcolaCognome(it, soglia, taglio)
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

        cancellaControllati()
    }// fine del metodo

    private static void cancellaControllati() {
        String query = 'delete from Cognome where voci=' + TAG_CONTROLLATE
        Cognome.executeUpdate(query)
    }// fine del metodo

    private void ricalcolaCognome(Cognome cognome, int soglia, int taglio) {
        int numVoci

        if (checkCognome(cognome.testo)) {
            numVoci = numeroVociCheUsanoCognome(cognome.testo)
            if (numVoci > soglia) {
                cognome.voci = numVoci
                cognome.save()
            } else {
                cognome.voci = TAG_CONTROLLATE
                cognome.save()
            }// fine del blocco if-else

            //--riempimento del campo wikiUrl di Cognomi
            regolaWikilink(cognome, taglio)

        } else {
            cognome.delete()
        }// fine del blocco if-else

    }// fine del metodo

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

} // fine della service classe
