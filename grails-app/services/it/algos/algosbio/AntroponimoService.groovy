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
import it.algos.algospref.LibPref
import it.algos.algospref.Pref
import it.algos.algospref.Preferenze
import it.algos.algoswiki.Edit
import it.algos.algoswiki.TipoAllineamento
import it.algos.algoswiki.WikiLib
import it.algos.algoswiki.WikiService

class AntroponimoService {

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    WikiService wikiService = new WikiService()

    private tagTitolo = 'Persone di nome '
    private static String aCapo = '\n'
    private tagPunti = 'Altre...'
    private boolean titoloParagrafoConLink = true
    private String progetto = 'Progetto:Antroponimi/'
    private String templateIncipit = 'incipit lista nomi'

    /**
     * cancella i records di antroponimi
     * @deprecated
     */
    public static void cancellaTutto() {
        def recs = Antroponimo.list()

        recs?.each {
            it.delete(flush: true)
        } // fine del ciclo each
    }// fine del metodo

    /**
     * costruisce i records
     * @deprecated
     */
    public void costruisce() {
        ArrayList<String> listaNomiParziale
        ArrayList<String> listaNomiUniciDiversiPerAccento
        String query = "select nome from BioGrails where nome <>'' order by nome asc"
        int delta = 1000
        int totaleVoci = BioGrails.count()
        log.info 'Inizio costruzione antroponimi'
        long inizio
        long fine
        long durata
        String mess
        def numAntro

        cancellaTutto()

        //ciclo
        for (int k = 0; k < totaleVoci; k += delta) {
            inizio = System.currentTimeMillis()
            listaNomiParziale = (ArrayList<String>) BioGrails.executeQuery(query, [max: delta, offset: k])
            listaNomiUniciDiversiPerAccento = elaboraNomiUnici(listaNomiParziale)
            spazzolaPacchetto(listaNomiUniciDiversiPerAccento)

            fine = System.currentTimeMillis()
            durata = fine - inizio
            durata = durata / 1000
            numAntro = Antroponimo.count()
            mess = 'Elaborate ' + LibTesto.formatNum(delta) + ' voci in ' + durata + ' sec. per un totale di '
            mess += LibTesto.formatNum(k + delta) + '/' + LibTesto.formatNum(totaleVoci) + ' voci.'
            mess += ' Creati ' + LibTesto.formatNum(numAntro) + ' nuovi antroponimi'
            println(mess)
        } // fine del ciclo for

        log.info 'Fine costruzione antroponimi'
    }// fine del metodo

    /**
     * Aggiunta nuovi records
     * Vengono creati nuovi records per i nomi presenti nelle voci (bioGrails) che superano la soglia minima
     */
    public void aggiunge() {
        ArrayList<String> listaNomiCompleta
        ArrayList<String> listaNomiUniciDiversiPerAccento

        //--recupera una lista 'grezza' di tutti i nomi
        listaNomiCompleta = creaListaNomiCompleta()

        //--elimina tutto ciò che compare oltre al nome
        listaNomiUniciDiversiPerAccento = elaboraNomiUnici(listaNomiCompleta)

        //--ricostruisce i records di antroponimi
        spazzolaPacchetto(listaNomiUniciDiversiPerAccento)
    }// fine del metodo

    //--recupera una lista 'grezza' di tutti i nomi
    private static ArrayList<String> creaListaNomiCompleta() {
        ArrayList<String> listaNomiCompleta
        String query = "select distinct nome from BioGrails where nome <>'' order by nome asc"

//        listaNomiCompleta = (ArrayList<String>) BioGrails.executeQuery(query, [max: 1000])
        listaNomiCompleta = (ArrayList<String>) BioGrails.executeQuery(query)

        return listaNomiCompleta
    }// fine del metodo

    //--elimina tutto ciò che compare oltre al nome
    public static ArrayList<String> elaboraNomiUnici(ArrayList listaNomiCompleta) {
        ArrayList<String> listaNomiUniciDiversiPerAccento = null
        String nomeDaControllare
        String nomeValido = ' '
        int k = 0

        if (listaNomiCompleta && listaNomiCompleta.size() > 0) {
            listaNomiUniciDiversiPerAccento = new ArrayList<String>()

            //--costruisce una lista di nomi 'unici'
            //--i nomi sono differenziati in base all'accento
            listaNomiCompleta.each {
                nomeDaControllare = (String) it
                nomeValido = check(nomeDaControllare)
                if (nomeValido) {
                    if (!listaNomiUniciDiversiPerAccento.contains(nomeValido)) {
                        listaNomiUniciDiversiPerAccento.add(nomeValido)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del ciclo each
        }// fine del blocco if

        return listaNomiUniciDiversiPerAccento
    }// fine del metodo

    private static String check(String nomeIn) {
        String nomeOut = ''
        ArrayList listaTagContenuto = new ArrayList()
        ArrayList listaTagIniziali = new ArrayList()
        int pos
        String tagSpazio = ' '
        boolean usaNomeSingolo = Pref.getBool(LibBio.CONFRONTA_SOLO_PRIMO_NOME_ANTROPONIMI)

        listaTagContenuto.add('<ref')
        listaTagContenuto.add('-')
        listaTagContenuto.add('"')
        listaTagContenuto.add("'")
        listaTagContenuto.add('(')

        listaTagIniziali.add('"')
        listaTagIniziali.add("'")//apice
        listaTagIniziali.add('ʿ')//apostrofo
        listaTagIniziali.add('‘')//altro tipo di apostrofo
        listaTagIniziali.add('‛')//altro tipo di apostrofo
        listaTagIniziali.add('[')
        listaTagIniziali.add('(')
        listaTagIniziali.add('.')
        listaTagIniziali.add('<!--')
        listaTagIniziali.add('{')
        listaTagIniziali.add('&')

        String tag = ''

        if (nomeIn.length() > 2 && nomeIn.length() < 100) {
            nomeOut = nomeIn

            if (usaNomeSingolo) {
                // @todo Maria e Maria Cristina sono uguali
                if (nomeOut.contains(tagSpazio)) {
                    pos = nomeOut.indexOf(tagSpazio)
                    nomeOut = nomeOut.substring(0, pos)
                    nomeOut = nomeOut.trim()
                }// fine del blocco if
            }// fine del blocco if

            listaTagContenuto?.each {
                tag = (String) it
                if (nomeOut.contains(tag)) {
                    pos = nomeOut.indexOf((String) it)
                    nomeOut = nomeOut.substring(0, pos)
                    nomeOut = nomeOut.trim()
                    def stip
                }// fine del blocco if
                def stop
            } // fine del ciclo each

            listaTagIniziali?.each {
                tag = (String) it

                if (nomeOut.startsWith(tag)) {
                    nomeOut = ''
                }// fine del blocco if
            } // fine del ciclo each

            //nomeOut = nomeOut.toLowerCase()       //@todo va in errore per GianCarlo
            nomeOut = LibTesto.primaMaiuscola(nomeOut)
        }// fine del blocco if-else

        return nomeOut
    }// fine del metodo

    //--ricostruisce i records di antroponimi
    public static void spazzolaPacchetto(ArrayList<String> listaNomiUniciDiversiPerAccento) {
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        String mess

        listaNomiUniciDiversiPerAccento?.each {
            spazzolaNome(it, soglia)
        }// fine del ciclo each

        fine = System.currentTimeMillis()
        durata = fine - inizio
        durata = durata / 1000
        mess = ' Creati nuovi records in ' + durata + ' sec.'
        println(mess)
    }// fine del metodo

    private static void spazzolaNome(String nome, int soglia) {
        int numVoci
        Antroponimo antroponimo

        if (nome) {
            numVoci = numeroVociCheUsanoNome(nome)
            if (numVoci > soglia) {
                antroponimo = Antroponimo.findByNome(nome)
                if (antroponimo == null) {
                    new Antroponimo(nome: nome, voci: numVoci, lunghezza: nome.length()).save()
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo

    private static int numeroVociCheUsanoNome(String nome) {
        int numVoci = 0
        ArrayList risultato
        String query = "select count(nome) from BioGrails where nome='${nome}'"

        risultato = BioGrails.executeQuery(query)
        if (risultato && risultato.size() == 1) {
            numVoci = (int) risultato.get(0)
        }// fine del blocco if

        return numVoci
    }// fine del metodo

    /**
     * Ricalcolo records esistenti
     * Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (antroponimo)
     */
    public void ricalcola() {
        ArrayList<Antroponimo> listaAntroponimi = Antroponimo.list()

        listaAntroponimi?.each {
            ricalcolaAntroponimo(it)
        } // fine del ciclo each

//        query = "select count(nome) from BioGrails where nome='${nome}'"
//        def risultato = BioGrails.executeQuery(query)
//        antroponimo = Antroponimo.findByNome(nome)
//
//
//        //--elimina tutto ciò che compare oltre al nome
//        listaNomiUniciDiversiPerAccento = elaboraNomiUnici(listaNomiCompleta)
//
//        //--ricostruisce i records di antroponimi
//        spazzolaPacchetto(listaNomiUniciDiversiPerAccento)
    }// fine del metodo

    private static void ricalcolaAntroponimo(Antroponimo antroponimo) {
        String nome
        int numVoci

        if (antroponimo) {
            nome = antroponimo.nome
            numVoci = numeroVociCheUsanoNome(nome)
            antroponimo.voci = numVoci
            antroponimo.save()
        }// fine del blocco if
    }// fine del metodo

    // Elabora tutte le pagine
    def elabora() {
        ArrayList<String> listaNomi

        //esegue la query
        listaNomi = getListaNomi()

        //crea le pagine dei singoli nomi (circa 600)
        if (listaNomi) {
            elabora(listaNomi)
        }// fine del blocco if

        if (listaNomi) {
            creaPagineControllo()
        }// fine del blocco if
    }// fine del metodo

    // Elabora le pagine
    def elabora(ArrayList<String> listaNomi) {
        listaNomi?.each {
            elaboraSingoloNome((String) it)
        }// fine del ciclo each
        def stop
    }// fine del metodo

    public creaPagineControllo() {
        //crea la pagina riepilogativa
        creaPaginaRiepilogativa()

        //crea le pagine di riepilogo di tutti i nomi
        elencoNomi()

        //crea la pagina di controllo didascalie
        creaPaginaDidascalie()
    }// fine del metodo

    def elencoNomi() {
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)
        String testo = ''
        String titolo = progetto + 'Liste'
        String summary = LibBio.getSummary()
        int k = 0
        def listaNomi
        Antroponimo antro
        ArrayList lista = new ArrayList()
        String nome
        int voci
        String vociTxt

        listaNomi = Antroponimo.findAllByVociGreaterThan(soglia, [sort: 'voci', order: 'desc'])
        lista.add(['#', 'Nome', 'Voci'])
        listaNomi?.each {
            vociTxt = ''
            antro = (Antroponimo) it
            nome = antro.nome
            voci = antro.voci
            if (voci > taglio) {
                nome = "'''[[Persone di nome " + nome + "|" + nome + "]]'''"
            }// fine del blocco if-else
            k++
            vociTxt = LibTesto.formatNum((String) voci)
            lista.add([k, nome, voci])
        } // fine del ciclo each

        testo += getElencoHead(k)
        testo += getElencoBody(lista)
        testo += getElencoFooter()

        new Edit(titolo, testo, summary)
    }// fine del metodo

    private static String getElencoHead(int numNomi) {
        String testoTitolo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)
        int numBio = BioGrails.count()

        testoTitolo += '<noinclude>'
        testoTitolo += "{{StatBio|data=$dataCorrente}}"
        testoTitolo += '</noinclude>'
        testoTitolo += aCapo
        testoTitolo += '==Nomi=='
        testoTitolo += aCapo
        testoTitolo += "Elenco dei '''"
        testoTitolo += LibTesto.formatNum(numNomi)
        testoTitolo += "''' nomi '''differenti''' "
        testoTitolo += "<ref>Gli apostrofi vengono rispettati. Pertanto: '''María, Marià, Maria, Mária, Marìa, Mariâ''' sono nomi diversi</ref>"
        testoTitolo += "<ref>I nomi ''doppi'' ('''Maria Cristina'''), vengono considerati nella loro completezza</ref>"
        testoTitolo += "<ref>Per motivi tecnici, non vengono riportati nomi che iniziano con '''apici''' od '''apostrofi'''</ref>"
        testoTitolo += "<ref>Non vengono riportati nomi che iniziano con '''['''</ref>"
        testoTitolo += "<ref>Non vengono riportati nomi che iniziano con '''{'''</ref>"
        testoTitolo += "<ref>Non vengono riportati nomi che iniziano con '''('''</ref>"
        testoTitolo += "<ref>Non vengono riportati nomi che iniziano con '''.'''</ref>"
        testoTitolo += "<ref>Non vengono riportati nomi che iniziano con '''&'''</ref>"
        testoTitolo += "<ref>Non vengono riportati nomi che iniziano con '''<'''</ref>"
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
    private static String getElencoBody(ArrayList listaVoci) {
        String testoTabella
        Map mappa = new HashMap()

        mappa.put('lista', listaVoci)
        mappa.put('width', '160')
        mappa.put('align', TipoAllineamento.secondaSinistra)
        testoTabella = WikiLib.creaTabellaSortable(mappa)

        return testoTabella
    }// fine del metodo

    private static String getElencoFooter() {
        String testoFooter = ''

        testoFooter += aCapo
        testoFooter += '==Note=='
        testoFooter += aCapo
        testoFooter += '<references/>'
        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '==Voci correlate=='
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Nomi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Didascalie]]'
        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '<noinclude>'
        testoFooter += '[[Categoria:Liste di persone per nome| ]]'
        testoFooter += '</noinclude>'

        return testoFooter
    }// fine del metodo

    /**
     * Elabora la pagina per un singolo nome
     */
    public void elaboraSingoloNome(String nome) {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        def nonServe

        if (debug) {
            nome = 'Andrea'
        }// fine del blocco if

        nonServe = new ListaNome(nome)

        def stop
    }// fine del metodo

    /**
     * Elabora la pagina per un singolo nome
     */
    public void elaboraSingoloNomeOld(String nome) {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String titolo
        String testo = ''
        String summary = LibBio.getSummary()
        ArrayList<BioGrails> listaBiografie

        if (debug) {
            nome = 'Andrea'
        }// fine del blocco if

        titolo = tagTitolo + nome
        listaBiografie = getListaBiografie(nome)

        //header
        testo += this.getNomeHead(nome, listaBiografie.size())

        //body
        testo += this.getNomeBody(nome, listaBiografie)

        //footer
        testo += this.getNomeFooter(nome)

        testo = testo.trim()

        //registra la pagina
        if (!debug) {
            new EditBio(titolo, testo, summary)
        }// fine del blocco if

        def stop
    }// fine del metodo

    //--costruisce una lista di nomi
    public static ArrayList<String> getListaNomi() {
        ArrayList<String> listaNomi
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)
        String query = "select nome from Antroponimo where voci>'${taglio}' order by nome asc"

        //esegue la query
        listaNomi = (ArrayList<String>) Antroponimo.executeQuery(query)

        return listaNomi
    }// fine del metodo

    //--costruisce una lista di biografie che 'usano' il nome
    //--se il flag usaNomeSingolo è vero, il nome della voce deve coincidere esattamente col parametro in ingresso
    //--se il flag usaNomeSingolo è falso, il nome della voce deve iniziare col parametro
    private static ArrayList<BioGrails> getListaBiografie(String nome) {
        ArrayList<BioGrails> listaBiografie = new ArrayList()
        BioGrails bio
        String nomeBio
        boolean confrontaSoloPrimo = Pref.getBool(LibBio.CONFRONTA_SOLO_PRIMO_NOME_ANTROPONIMI)
        ArrayList<BioGrails> listaGrezza

        //--recupera una lista 'grezza' di tutti i nomi
        if (confrontaSoloPrimo) {
            def criterio = BioGrails.createCriteria()
            listaBiografie = criterio.list() {
                or {
                    like("nome", "${nome}")
                    like("nome", "${nome} %")
                }
                order("cognome", "asc")
            }
        } else {
            def criterio = BioGrails.createCriteria()
            listaGrezza = criterio.list() {
                like("nome", "${nome}")
                order("cognome", "asc")
            }
            //--i nomi sono differenziati in base all'accento
            listaGrezza?.each {
                bio = (BioGrails) it
                nomeBio = bio.nome
                //nomeBio = nomeBio.toLowerCase()       //@todo va in errore per GianCarlo
                if (nomeBio.equalsIgnoreCase(nome)) {
                    listaBiografie.add(bio)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if-else

        return listaBiografie
    }// fine del metodo

    public String getNomeSingolo(String nomeIn) {
        String nomeOut = nomeIn
        boolean confrontaSoloPrimo = Pref.getBool(LibBio.CONFRONTA_SOLO_PRIMO_NOME_ANTROPONIMI)
        String tagSpazio = ' '
        int pos

        // per i confronti solo il primo nome viene considerato
        // @todo Maria e Maria Cristina sono uguali
        if (nomeIn && confrontaSoloPrimo) {
            if (nomeOut.contains(tagSpazio)) {
                pos = nomeOut.indexOf(tagSpazio)
                nomeOut = nomeOut.substring(0, pos)
                nomeOut = nomeOut.trim()
            }// fine del blocco if
        }// fine del blocco if

        return nomeOut
    }// fine del metodo

    public String getNomeHead(String nome, int num) {
        String testo = ''
        boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI)
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String numero = ''
        String template = templateIncipit
        String tagIndice = '__FORCETOC__'
        String tagNoIndice = '__NOTOC__'

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

        testo += "{{${template}|nome=${nome}}}"
        testo += aCapo
        testo += aCapo

        return testo
    }// fine del metodo


    public String getNomeBody(String nome, ArrayList<BioGrails> listaBiografie) {
        String testo = ''
        boolean dividePerGenere = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA, false)
        int maxVoci = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_ANTROPONIMI, 100)
        String tagMaschio = 'M'
        String tagFemmina = 'F'
        ArrayList listaVociMaschili
        ArrayList listaVociFemminili

        if (dividePerGenere) {
            listaVociMaschili = this.selezionaGenere(listaBiografie, tagMaschio)
            listaVociFemminili = this.selezionaGenere(listaBiografie, tagFemmina)

            if (listaVociMaschili && listaVociFemminili) {
                testo += '\n==Uomini==\n'
                testo += this.getNomeBodyBase(nome, listaVociMaschili, maxVoci)
                testo += '\n==Donne==\n'
                testo += this.getNomeBodyBase(nome, listaVociFemminili, maxVoci)
            } else {
                if (listaVociMaschili) {
                    testo += this.getNomeBodyBase(nome, listaVociMaschili, maxVoci)
                }// fine del blocco if
                if (listaVociFemminili) {
                    testo += this.getNomeBodyBase(nome, listaVociFemminili, maxVoci)
                }// fine del blocco if
            }// fine del blocco if-else
        } else {
            testo = this.getNomeBodyBase(nome, listaBiografie, maxVoci)
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    public String getNomeBodyBase(String nome, ArrayList<BioGrails> listaBiografie, int maxVoci) {
        String testo = ''
        Map mappa
        String chiave
        ArrayList valore
        ArrayList<String> listaDidascalie

        mappa = BioLista.getMappaAttività(listaBiografie)
        mappa?.each {
            chiave = it.key
            valore = (ArrayList) mappa.get(chiave)
            listaDidascalie = BioLista.getListaDidascalie((ArrayList<BioGrails>) valore)

            if (chiave.startsWith(tagPunti)) {
                testo += getNomeBodyBasePagina(chiave, listaDidascalie)
            } else {
                if (listaDidascalie.size() >= maxVoci) {
                    testo += getNomeBodyBaseSottoPagina(nome, chiave, valore)
                } else {
                    testo += getNomeBodyBasePagina(chiave, listaDidascalie)
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del ciclo each

        return testo
    }// fine del metodo

    public String getNomeBodyBasePagina(String chiave, ArrayList listaDidascalie) {
        String testo = ''
        String tag = '=='

        testo += tag
        testo += chiave
        testo += tag
        testo += aCapo
        testo += getParagrafoDidascalia(listaDidascalie)
        testo += aCapo
        testo += aCapo

        return testo
    }// fine del metodo

    public String getNomeHeadSottoPagina(String titoloRitorno, int num) {
        String testo = ''
        boolean usaTavolaContenuti = false
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String numero = ''
        String tagIndice = '__FORCETOC__'
        String tagNoIndice = '__NOTOC__'

        if (num) {
            numero = LibTesto.formatNum(num)
        }// fine del blocco if

        if (usaTavolaContenuti) {
            testo += tagIndice
        } else {
            testo += tagNoIndice
        }// fine del blocco if-else
        testo += aCapo

        testo += "{{torna a|${titoloRitorno}}}"
        testo += aCapo

        testo += '<noinclude>'
        testo += aCapo
        testo += "{{StatBio"
        if (numero) {
            testo += "|bio=$numero"
        }// fine del blocco if
        testo += "|data=$dataCorrente}}"
        testo += aCapo

        return testo
    }// fine del metodo

    public String getNomeBodyBaseSottoPagina(String nome, String chiave, ArrayList<BioGrails> listaBiografie) {
        String testo = ''
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        boolean usaCategoriaNellaSottopagina = Pref.getBool(LibBio.USA_CATEGORIA_SOTTOPAGINE_ANTROPONIMI, false)
        String summary = LibBio.getSummary()
        String sottoTitolo
        String sottoChiave
        String tag = '=='
        String torna
        Map mappa
        ArrayList<String> listaDidascalie
        ArrayList valore

        sottoChiave = chiave.substring(chiave.indexOf('|') + 1, chiave.indexOf(']]'))

        torna = tagTitolo + nome
        sottoTitolo = torna + '/' + sottoChiave

        //header
        testo += this.getNomeHeadSottoPagina(torna, listaBiografie.size())

        //body
        mappa = BioLista.getMappaLetteraIniziale(listaBiografie)
        mappa?.each {
            chiave = it.key
            valore = (ArrayList) mappa.get(chiave)
            listaDidascalie = BioLista.getListaDidascalie((ArrayList<BioGrails>) valore)
            testo += getNomeBodyBasePagina(chiave, listaDidascalie)
        }// fine del ciclo each

        //footer
        if (usaCategoriaNellaSottopagina) {
            testo += this.getNomeFooter(nome + '/' + sottoChiave)
        }// fine del blocco if

        testo = testo.trim()

        //registra la pagina
        if (!debug) {
            new EditBio(sottoTitolo, testo, summary)
        }// fine del blocco if

        testo = tag
        testo += chiave
        testo += tag
        testo += aCapo
        testo += "*{{Vedi anche|${sottoTitolo}}}"

        testo += aCapo

        return testo
    }// fine del metodo

    public ArrayList selezionaGenere(ArrayList<BioGrails> listaVoci, String tag) {
        ArrayList lista = null
        BioGrails bio

        if (listaVoci && listaVoci.size() > 0 && tag) {
            lista = new ArrayList()
            listaVoci?.each {
                bio = it
                if (bio.sesso.equals(tag)) {
                    lista.add(bio)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return lista
    }// fine del metodo

    // raggruppa per attività una lista di biografie
    // costruisce una mappa con:
    // una chiave per ogni attività
    // una lista di didascalie
    // inserisce solo le attività utilizzate
    public Map getMappaAttività(ArrayList<BioGrails> listaVoci) {
        LinkedHashMap<String, ArrayList<String>> mappa = null
        String didascalia = ''
        String chiaveOld = ''
        String chiave = ''
        ArrayList<String> lista
        BioGrails bio
        String attivita
        boolean usaParagrafiLettera = true

        if (listaVoci) {
            mappa = new LinkedHashMap<String, ArrayList<String>>()
            listaVoci?.each {
                bio = it
                try { // prova ad eseguire il codice
                    if (bio.didascaliaListe) {
                        didascalia = bio.didascaliaListe
                    } else {
                        didascalia = creaDidascaliaAlVolo(bio)
                    }// fine del blocco if-else
                } catch (Exception unErrore) { // intercetta l'errore
                    didascalia = creaDidascaliaAlVolo(bio)
                }// fine del blocco try-catch
                attivita = bio.attivita
                if (attivita) {
                    chiave = this.getAttivita(bio)
                    if (!chiave) {
                        chiave = tagPunti
                    }// fine del blocco if
                } else {
                    chiave = tagPunti
                }// fine del blocco if-else

                chiave = chiaveUnica(mappa, chiave)

                if (chiave.equals(chiaveOld)) {
                    lista = mappa.get(chiave)
                    lista.add(didascalia)
                } else {
                    if (mappa.get(chiave)) {
                        lista = mappa.get(chiave)
                        lista.add(didascalia)
                    } else {
                        lista = new ArrayList<String>()
                        lista.add(didascalia)
                        mappa.put(chiave, lista)
                        chiaveOld = chiave
                    }// fine del blocco if-else
                }// fine del blocco if-else
            }// fine del ciclo each
        }// fine del blocco if

        return mappa
    }// fine del metodo

    public String chiaveUnica(Map mappa, String chiaveIn) {
        String chiaveOut = chiaveIn
        String chiaveAttivita = chiaveIn.substring(chiaveIn.indexOf('|') + 1, chiaveIn.length())
        ArrayList<String> listaChiaviGiaUsate = mappa.keySet()
        String chiaveGiaUsata
        String chiaveRidotta

        listaChiaviGiaUsate?.each {
            chiaveGiaUsata = it
            chiaveRidotta = chiaveGiaUsata.substring(chiaveGiaUsata.indexOf('|') + 1, chiaveGiaUsata.length())
            if (chiaveRidotta.equals(chiaveAttivita)) {
                chiaveOut = chiaveGiaUsata
            }// fine del blocco if
        }// fine del blocco if

        return chiaveOut
    }// fine del metodo

    // se manca la didascalia, la crea al volo
    public String creaDidascaliaAlVolo(BioGrails bio) {
        String didascaliaTxt = ''
        long grailsId
        DidascaliaBio didascaliaObj

        if (bio) {
            grailsId = bio.id
            didascaliaObj = new DidascaliaBio(grailsId)
            didascaliaObj.setInizializza()
            didascaliaTxt = didascaliaObj.getTestoEstesaSimboli()
        }// fine del blocco if

        return didascaliaTxt
    }// fine del metodo

    // restituisce il nome dell'attività
    // restituisce il plurale
    // restituisce il primo carattere maiuscolo
    // aggiunge un link alla voce di riferimento
    public String getAttivita(BioGrails bio) {
        String attivitaLinkata = ''
        String singolare
        boolean link = titoloParagrafoConLink
        String attivita
        String genere
        Professione professione

        if (bio) {
            singolare = bio.attivita
            genere = bio.sesso
            if (singolare) {
                attivita = attivitaPluralePerGenere(singolare, genere)
                if (attivita) {
                    if (link) {
                        professione = Professione.findBySingolare(singolare)
                        attivitaLinkata = '[['
                        if (professione) {
                            attivitaLinkata += LibTesto.primaMaiuscola(professione.voce)
                        } else {
                            attivitaLinkata += LibTesto.primaMaiuscola(singolare)
                        }// fine del blocco if-else
                        attivitaLinkata += '|'
                        attivitaLinkata += attivita
                        attivitaLinkata += ']]'
                    } else {
                        attivitaLinkata = attivita
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return attivitaLinkata
    }// fine del metodo

    public String attivitaPluralePerGenere(String singolare, String sesso) {
        String plurale
        Genere genere = Genere.findBySingolareAndSesso(singolare, sesso)

        if (genere) {
            plurale = genere.plurale
        }// fine del blocco if

        if (plurale) {
            plurale = LibTesto.primaMaiuscola(plurale)
            plurale = plurale.trim()
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Ordina una mappa
     *
     * @param mappa non ordinata
     * @return mappa ordinata
     */
    public Map ordinaMappa(Map mappaIn) {
        // variabili e costanti locali di lavoro
        Map mappaOut = mappaIn
        ArrayList<String> listaChiavi
        String chiave
        def valore

        if (mappaIn && mappaIn.size() > 1) {
            listaChiavi = mappaIn.keySet()
            listaChiavi.remove(tagPunti) //elimino l'asterisco (per metterlo in fondo)
            listaChiavi = ordinaChiavi(listaChiavi)
            if (listaChiavi) {
                mappaOut = new LinkedHashMap()
                listaChiavi?.each {
                    chiave = it
                    valore = mappaIn.get(chiave)
                    mappaOut.put(chiave, valore)
                }// fine del blocco if

                // aggiungo (in fondo) l'asterisco. Se c'è.
                valore = mappaIn.get(tagPunti)
                if (valore) {
                    mappaOut.put(tagPunti, valore)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappaOut
    }// fine della closure

    /**
     * Ordina una mappa
     *
     * @param mappa non ordinata
     * @return mappa ordinata
     */
    public ArrayList<String> ordinaChiavi(ArrayList<String> listaChiaviIn) {
        ArrayList<String> listaChiaviOut = listaChiaviIn.sort()
        String chiaveCompleta
        String chiaveRidotta
        String chiaveDoppia
        ArrayList<String> listaChiavi
        String chiave
        String valore
        ArrayList<String> lista = new ArrayList<String>()
        String tag = 'x3x7x4x'

        if (true) {
            listaChiaviOut?.each {
                chiaveCompleta = it
                chiaveRidotta = chiaveCompleta.substring(chiaveCompleta.indexOf('|') + 1, chiaveCompleta.length())
                lista.add(chiaveRidotta + tag + chiaveCompleta)
            } // fine del ciclo each

            lista.sort()

            if (lista) {
                listaChiaviOut = new ArrayList<String>()
                lista?.each {
                    chiaveDoppia = it
                    chiaveCompleta = chiaveDoppia.substring(chiaveDoppia.indexOf(tag) + tag.length(), chiaveDoppia.length())
                    listaChiaviOut.add(chiaveCompleta)
                } // fine del ciclo each
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return listaChiaviOut
    }// fine della closure

    public String getParagrafoDidascalia(ArrayList<String> nomi) {
        String testo = ''
        String nome
        String tag = ''

        if (nomi) {
            nomi?.each {
                nome = it
                testo += '*'
                testo += nome
                testo += '\n'
            }// fine del ciclo each
        }// fine del blocco if

        return testo.trim()
    }// fine del metodo


    public String getNomeFooter(String nome) {
        String testo = ''
        String aCapo = '\n'

//        testo += '==Voci correlate=='
//        testo += aCapo
//        testo += aCapo
//        testo += '*[[Progetto:Antroponimi/Nomi]]'
//        testo += aCapo
//        testo += '*[[Progetto:Antroponimi/Didascalie]]'
//        testo += aCapo
//        testo += aCapo
        testo += '<noinclude>'
        testo += "[[Categoria:Liste di persone per nome|${nome}]]"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo

    // pagina di controllo/servizio
    public creaPaginaDidascalie() {
        String titolo = progetto + 'Didascalie'
        String testo = ''
        String summary = 'Biobot'

        testo += getDidascalieHeader()
        testo += getDidascalieBody()
        testo += getDidascalieFooter()

        //registra la pagina
        new Edit(titolo, testo, summary)
    }// fine della closure


    private static String getDidascalieHeader() {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String aCapo = '\n'
        String bot = getBotLink()

        testo += '__NOTOC__'
        testo += '<noinclude>'
        testo += "{{StatBio"
        testo += "|data=$dataCorrente}}"
        testo += aCapo

        testo += "Pagina di servizio per il '''controllo'''<ref>Attualmente il ${bot} usa il tipo '''8''' (estesa con simboli)</ref> delle didascalie utilizzate nelle ''Liste di persone di nome''..."
        testo += aCapo
        testo += 'Le didascalie possono essere di diversi tipi:'
        testo += aCapo

        return testo
    }// fine della closure

    private static String getDidascalieBody() {
        String testo = ''
        String titoloEsempio = 'Silvio Spaventa'
        WrapBio bio = new WrapBio(titoloEsempio)
        BioGrails bioGrails = BioGrails.findByTitle(titoloEsempio)
        long grailsId = 0

        if (bioGrails) {
            grailsId = bioGrails.id
        }// fine del blocco if

        if (grailsId) {
            DidascaliaTipo.values().each {
                if (it.stampaTest) {
                    testo += rigaDidascalia(it)
                    testo += rigaEsempio(it, grailsId)
                }// fine del blocco if
            }// fine di each
        }// fine del blocco if

        return testo
    }// fine della closure

    private static String rigaDidascalia(DidascaliaTipo tipo) {
        String testo = ''
        String tag = ': '
        boolean usaRef = true
        String ref;
        ref = tipo.getRef();

        testo += '#'
        // testo += "'''"
        testo += LibTesto.primaMaiuscola(tipo.getSigla())
        // testo += "'''"
        testo += tag
        testo += tipo.getDescrizione()
        if (usaRef) {
            if (ref != null) {
                if (!ref.equals("")) {
                    testo += ref
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return testo
    }// fine della closure

    private static String rigaEsempio(DidascaliaTipo tipo, long grailsId) {
        String testo = ''
        String aCapo = '\n'
        String testoDidascalia = ''
        DidascaliaBio didascalia

        didascalia = new DidascaliaBio(grailsId)
        didascalia.setInizializza()

        switch (tipo) {
            case DidascaliaTipo.base:
                testoDidascalia = didascalia.getTestoBase()
                break
            case DidascaliaTipo.crono:
                testoDidascalia = didascalia.getTestoCrono()
                break
            case DidascaliaTipo.cronoSimboli:
                testoDidascalia = didascalia.getTestoCrono()
                break
            case DidascaliaTipo.semplice:
                testoDidascalia = didascalia.getTestoSemplice()
                break
            case DidascaliaTipo.completa:
                testoDidascalia = didascalia.getTestoCompleta()
                break
            case DidascaliaTipo.completaSimboli:
                testoDidascalia = didascalia.getTestoCompletaSimboli()
                break
            case DidascaliaTipo.estesa:
                testoDidascalia = didascalia.getTestoEstesa()
                break
            case DidascaliaTipo.estesaSimboli:
                testoDidascalia = didascalia.getTestoEstesaSimboli()
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        testo += '<BR>'
        testo += "'''"
        testo += testoDidascalia
        testo += "'''"
        testo += aCapo

        return testo
    }// fine della closure

    private static String getDidascalieFooter() {
        String testoFooter = ''
        String aCapo = '\n'

        testoFooter += '==Note=='
        testoFooter += aCapo
        testoFooter += '<references/>'
        testoFooter += aCapo
        testoFooter += '==Voci correlate=='
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Nomi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Liste]]'
        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '<noinclude>'
        testoFooter += '[[Categoria:Liste di persone per nome| ]]'
        testoFooter += '</noinclude>'

        return testoFooter
    }// fine della closure

    private static String getBotLink() {
        String testo = ''

        testo += "'''"
        testo += '[[Utente:Biobot|<span style="color:green;">bot</span>]]'
        testo += "'''"

        return testo
    }// fine della closure

    /**
     * Crea la pagina riepilogativa
     */
    public creaPaginaRiepilogativa() {
        ArrayList<String> listaVoci = getListaNomi()
        String testo = ''
        String titolo = progetto + 'Nomi'
        String summary = 'Biobot'

        if (listaVoci) {
            testo += getRiepilogoHead()
            testo += getRiepilogoBody(listaVoci)
            testo += getRiepilogoFooter()

            new Edit(titolo, testo, summary)
        }// fine del blocco if
    }// fine del metodo


    public String getRiepilogoHead() {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String aCapo = '\n'

        testo += '__NOTOC__'
        testo += '<noinclude>'
        testo += "{{StatBio|data=$dataCorrente}}"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo


    public String getRiepilogoBody(ArrayList<String> listaVoci) {
        String testo = ''
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)
        LinkedHashMap mappa = null
        String chiave
        String nome
        def lista
        def ricorrenze = LibTesto.formatNum(taglio)
        String aCapo = '\n'

        testo += '==Nomi=='
        testo += aCapo
        testo += 'Elenco dei '
        testo += "''' "
        testo += LibTesto.formatNum(listaVoci.size())
        testo += "'''"
        testo += ' nomi che hanno più di '
        testo += "'''"
        testo += ricorrenze
        testo += "'''"
        testo += ' ricorrenze nelle voci biografiche'
        testo += aCapo

        testo += aCapo
        testo += '{{Div col|cols=3}}'
        if (listaVoci) {
            listaVoci.each {
                nome = it
                testo += aCapo
                testo += this.getRiga(nome)
            }// fine del ciclo each
        }// fine del blocco if
        testo += aCapo
        testo += '{{Div col end}}'
        testo += aCapo

        return testo
    }// fine del metodo

    public String getRiga(String nome) {
        String testo = ''
        String tag
        String aCapo = '\n'
        String numVoci

        if (nome) {
            tag = tagTitolo + nome
            testo += '*'
            testo += '[['
            testo += tag
            testo += '|'
            testo += nome
            testo += ']]'
            if (Pref.getBool('usaOccorrenzeAntroponimi')) {
                numVoci = numeroVociCheUsanoNome(nome)
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

    public String getRiepilogoFooter() {
        String testo = ''
        String aCapo = '\n'

        testo += aCapo
        testo += '==Voci correlate=='
        testo += aCapo
        testo += '*[[Progetto:Antroponimi]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Liste]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Didascalie]]'
        testo += aCapo
        testo += aCapo
        testo += '<noinclude>'
        testo += '[[Categoria:Liste di persone per nome| ]]'
        testo += '</noinclude>'

        return testo
    }// fine del metodo

    /**
     * Ritorna l'antroponimo dal link alla voce
     * Se non esiste, lo crea
     */
    public static Antroponimo getAntroponimo(String nomeDaControllare) {
        Antroponimo antroponimo = null
        String nome = ''
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)
        int voci = 0

        if (nomeDaControllare) {
            nome = check(nomeDaControllare)
        }// fine del blocco if

        if (nome) {
            voci = numeroVociCheUsanoNome(nome)
        }// fine del blocco if

        if (nome) {
            antroponimo = Antroponimo.findByNome(nome)
            if (!antroponimo) {
                if (voci > soglia) {
                    antroponimo = new Antroponimo(nome: nome, voci: voci)
                    antroponimo.save(flush: true)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return antroponimo
    } // fine del metodo


} // fine della service classe
