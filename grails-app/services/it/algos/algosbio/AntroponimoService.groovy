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

import grails.transaction.Transactional
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algoslib.LibMat
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit
import it.algos.algoswiki.QueryVoce
import it.algos.algoswiki.TipoAllineamento
import it.algos.algoswiki.WikiLib
import it.algos.algoswiki.WikiService

import java.text.Normalizer

/**
 * Gestione dei nomi (antroponimi)
 *
 * 1° fase da fare una tantum o ogni 6-12 mesi
 * Costruisce
 * Annullamento del link tra BioGrails e gli Antroponimi
 * Creazione dei records di antroponimi leggendo i records BioGrails
 * Controllo della pagina Progetto:Antroponimi/Nomi doppi
 * Ricalcolo delle voci per ricostruire in ogni record di BioGrails il link verso il corretto record di Antroponimo
 *
 * 2° fase da fare una tantum
 * Aggiunge
 * Aggiunta dei records di antroponimi leggendo i records BioGrails
 * Controllo della pagina Progetto:Antroponimi/Nomi doppi
 * Ricalcolo delle voci per ricostruire in ogni record di BioGrails il link verso il corretto record di Antroponimo
 *
 * 3° fase da fare ogni settimana
 * Ricalcola
 * Upload
 * Controllo della pagina Progetto:Antroponimi/Nomi doppi
 * Spazzolamento di tutti i records di Antroponimi per aggiornare il numero di voci linkate
 * Creazione della pagina/lista per ogni record di Antroponimi che supera la soglia
 *
 * Note:
 * A- Se USA_ACCENTI_NORMALIZZATI è true, devo vedere sempre il nome Aaròn. Se è false lo vedo solo se supera SOGLIA_ANTROPONIMI
 * B- Crea records di Antroponimo al di sopra di SOGLIA_ANTROPONIMI
 * C- Se USA_SOLO_PRIMO_NOME_ANTROPONIMI è true, considera solo il primo nome che trova per creare un antroponimo
 *    e trovo solo Aaron. Se è false, trovo anche Aaron Michael (se supera la SOGLIA_ANTROPONIMI)
 * D- Se USA_LISTA_NOMI_DOPPI è true, aggiunge i records letti da Progetto:Antroponimi/Nomi doppi
 *
 * nomi distinti: 93.219
 * 2382 antroponimi
 * Jean-Jacques deve rimanere, Jean Baptiste no
 */
@Transactional(readOnly = false)
class AntroponimoService {

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    WikiService wikiService = new WikiService()

    private static String aCapo = '\n'
    private static String TITOLO_LISTA_NOMI_DOPPI = 'Progetto:Antroponimi/Nomi doppi'

    private tagTitolo = 'Persone di nome '
    private tagPunti = 'Altre...'
    private boolean titoloParagrafoConLink = true
    private String progetto = 'Progetto:Antroponimi/'
    private String templateIncipit = 'incipit lista nomi'

    /**
     * azzera i link tra BioGrails e Antroponimo
     * cancella i records di antroponimi
     */
    public static void cancellaTutto() {
        cancellaLink()
        cancellaAntroponimi()
    }// fine del metodo

    /**
     * azzera i link tra BioGrails e Antroponimo
     */
    public static void cancellaLink() {
        String query = "update BioGrails set nomeLink=null"
        BioGrails.executeUpdate(query)

        query = "update Antroponimo set voceRiferimento=null"
        Antroponimo.executeUpdate(query)
    }// fine del metodo

    /**
     * cancella i records di antroponimi
     */
    public static void cancellaAntroponimi() {
        def recs = Antroponimo.list()

        recs?.each {
            it.delete(flush: true)
        } // fine del ciclo each
    }// fine del metodo

    /**
     * costruisce i records
     */
    public void costruisce() {
        cancellaTutto()
        aggiunge()

        log.info 'Fine costruzione antroponimi'
    }// fine del metodo

    /**
     * Aggiunta nuovi records
     * Vengono creati nuovi records per i nomi presenti nelle voci (bioGrails) che superano la soglia minima
     */
    public void aggiunge() {
        ArrayList<String> listaNomiCompleta
        ArrayList<String> listaNomiUnici

        listaNomiDoppi()

        //--recupera una lista 'grezza' di tutti i nomi
        listaNomiCompleta = creaListaNomiCompleta()

        //--elimina tutto ciò che compare oltre al nome
        listaNomiUnici = elaboraNomiUnici(listaNomiCompleta)

        //--(ri)costruisce i records di antroponimi
        spazzolaPacchetto(listaNomiUnici)

        //--aggiunge i riferimenti alla voce principale di ogni record
        elaboraVocePrincipale()
    }// fine del metodo

    /**
     * Recupera una lista 'grezza' di tutti i nomi
     */
    private static ArrayList<String> creaListaNomiCompleta() {
        String query = "select distinct nome from BioGrails where nome <>'' order by nome asc"
        return (ArrayList<String>) BioGrails.executeQuery(query)
    }// fine del metodo

    /**
     * Elabora tutti i nomi
     * Costruisce una lista di nomi ''validi' e 'unici'
     */
    public static ArrayList<String> elaboraNomiUnici(ArrayList<String> listaNomiCompleta) {
        ArrayList<String> listaNomiUnici = new ArrayList<String>()
        String nomeDaControllare
        String nomeValido = ' '

        //--costruisce una lista di nomi 'unici'
        listaNomiCompleta?.each {
            nomeDaControllare = (String) it
            nomeValido = check(nomeDaControllare)
            if (nomeValido) {
                if (!listaNomiUnici.contains(nomeValido)) {
                    listaNomiUnici.add(nomeValido)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del ciclo each

        return listaNomiUnici
    }// fine del metodo

    /**
     * Elabora il singolo nome
     * Usa (secondo preferenze) i nomi singoli: Maria e Maria Cristina sono uguali
     * Elimina caratteri 'anomali' dal nome
     * Gian, Lady, Sir, Maestro, Abd, 'Abd, Abu, Abū, Ibn, DJ, e J.
     */
    private static String check(String nomeIn) {
        String nomeOut = ''
        ArrayList listaTagContenuto = new ArrayList()
//        ArrayList listaTagIniziali = new ArrayList()
        int pos
        String tagSpazio = ' '
        boolean usaNomeSingolo = Pref.getBool(LibBio.USA_SOLO_PRIMO_NOME_ANTROPONIMI)

        listaTagContenuto.add('(')

        String tag = ''

        if (nomeIn && nomeIn.length() > 2 && nomeIn.length() < 100) {
            nomeOut = nomeIn.trim()

            // @todo Prende solo il primo
            if (usaNomeSingolo) {
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

            if (!LibBio.checkNome(nomeOut)) {
                nomeOut = ''
            }// fine del blocco if

            nomeOut = LibTesto.primaMaiuscola(nomeOut)

            if (Pref.getBool(LibBio.USA_ACCENTI_NORMALIZZATI, false)) {
                nomeOut = Normalizer.normalize(nomeOut, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
            }// fine del blocco if

            if (nomeOut.length() < 2) {
                nomeOut = ''
            }// fine del blocco if
        }// fine del blocco if

        return nomeOut
    }// fine del metodo

    /**
     * Spazzola la lista di nomi
     */
    public void spazzolaPacchetto(ArrayList<String> listaNomiUnici) {
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info

        listaNomiUnici?.each {
            spazzolaNome(it, soglia)
            k++
            if (LibMat.avanzamento(k, 1000)) {
                fine = System.currentTimeMillis()
                durata = fine - inizio
                durata = durata / 1000

                info = 'Spazzolati '
                info += LibTesto.formatNum(k)
                info += ' nomi su '
                info += LibTesto.formatNum(listaNomiUnici.size())
                info += ' in '
                info += LibTesto.formatNum(durata)
                info += ' sec. totali'
                log.info info
            }// fine del blocco if
        } // fine del ciclo each
        log.info 'Spazzolati tutti'
    }// fine del metodo

    /**
     * Controlla il singolo nome
     * Controlla la soglia minima
     * Crea un record per ogni nome non ancora esistente (se supera la soglia)
     * Registra anche i nomi accentati ma col riferimento al record del nome normalizzato (senza accenti)
     */
    private void spazzolaNome(String nomeConEventualeAccento, int soglia) {
        int numVoci

        if (nomeConEventualeAccento) {
            numVoci = numeroVociCheUsanoNome(nomeConEventualeAccento)
            if (nomeSenzaAccento(nomeConEventualeAccento)) {
                if (numVoci > soglia) {
                    registraSingoloNome(nomeConEventualeAccento, numVoci, true)
                }// fine del blocco if
            } else {
                registraSingoloNome(nomeConEventualeAccento, numVoci, false)
            }// fine del blocco if-else
        }// fine del blocco if
    }// fine del metodo

    /**
     * Registra il singolo record
     */
    private static Antroponimo registraSingoloNome(String nome, int numVoci, boolean vocePrincipale) {
        return registraSingoloNome(nome, numVoci, vocePrincipale, null)
    }// fine del metodo

    /**
     * Registra il singolo record
     */
    private
    static Antroponimo registraSingoloNome(String nome, int numVoci, boolean vocePrincipale, Antroponimo voceRiferimento) {
        Antroponimo antroponimo = Antroponimo.findByNome(nome)

        if (antroponimo == null) {
            antroponimo = new Antroponimo()
            antroponimo.nome = nome
            antroponimo.voci = numVoci
            antroponimo.lunghezza = nome.length()
            antroponimo.voceRiferimento = voceRiferimento
            antroponimo.isVocePrincipale = vocePrincipale
            antroponimo.save(flush: true)
        }// fine del blocco if

        return antroponimo
    }// fine del metodo


    private static int numeroVociCheUsanoNome(String nome) {
        return numeroVociCheUsanoNome(nome, null)
    }// fine del metodo

    private static numeroVociCheUsanoNome(String nome, Antroponimo antroponimo) {
        int numVoci = 0
        String query = ''
        String sep = "'"
        String tagSpazio = ' '
        String tagWillCard = '%'
        String nomeWillCard
        ArrayList<Antroponimo> lista

        if (nome.contains(sep)) {
            nome = nome.replace(sep, sep + sep)
        }// fine del blocco if
        nomeWillCard = nome + tagSpazio + tagWillCard

        if (antroponimo) {
            lista = Antroponimo.findAllByVoceRiferimento(antroponimo)
            lista?.each {
                numVoci += numeroVociCheUsanoAntroponimo(it.nome, antroponimo)
            } // fine del ciclo each
        } else {
            numVoci = BioGrails.countByNomeLikeOrNomeLike(nome, nomeWillCard)
        }// fine del blocco if-else

        return numVoci
    }// fine del metodo

    private static numeroVociCheUsanoAntroponimo(String nome, Antroponimo antroponimo) {
        int numVoci = 0
        String query = ''
        String sep = "'"
        String tagSpazio = ' '
        String tagWillCard = '%'
        String nomeWillCard = nome + tagSpazio + tagWillCard

        query += "update BioGrails set nomeLink="
        if (antroponimo) {
            query += antroponimo.id
        } else {
            query += " null"
        }// fine del blocco if-else
        query += " where nome="
        query += sep + nome + sep
        query += " or nome like "
        query += sep + nomeWillCard + sep
        try { // prova ad eseguire il codice
            numVoci = BioGrails.executeUpdate(query)
        } catch (Exception unErrore) { // intercetta l'errore
//            log.warn 'Errore numeroVociCheUsanoNome = ' + nome
        }// fine del blocco try-catch

        return numVoci
    }// fine del metodo

    /**
     * Aggiunge i riferimenti alla voce principale di ogni record
     */
    private static elaboraVocePrincipale() {
        ArrayList<Antroponimo> lista = Antroponimo.list()
        Antroponimo antroponimo
        Antroponimo antroponimoPrincipale
        String nomeNormalizzato
        String nomeConAccento

        lista?.each {
            antroponimo = it
            if (antroponimo.isVocePrincipale) {
                antroponimo.voceRiferimento = antroponimo
            } else {
                if (Pref.getBool(LibBio.USA_ACCENTI_NORMALIZZATI, false)) {
                    nomeConAccento = antroponimo.nome
                    nomeNormalizzato = normalizza(nomeConAccento)
                    antroponimoPrincipale = Antroponimo.findByNome(nomeNormalizzato)
                    antroponimo.voceRiferimento = antroponimoPrincipale
                }// fine del blocco if
            }// fine del blocco if-else
            antroponimo.save()
        } // fine del ciclo each

    }// fine del metodo

    //--riempimento del campo wikiUrl di Antroponimi
    private static void regolaWikilink() {
        Antroponimo antroponimo
        Antroponimo antroponimoRiferimento
        ArrayList<Antroponimo> lista = Antroponimo.list()
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)
        String url

        lista?.each {
            antroponimo = it
            url = 'https://it.wikipedia.org/wiki/Persone di nome ' + antroponimo.nome
            if (antroponimo.isVocePrincipale) {
                if (antroponimo.wikiUrl) {
                } else {
                    if (antroponimo.voci > taglio) {
                        antroponimo.wikiUrl = url
                    } else {
                        antroponimo.wikiUrl = ''
                    }// fine del blocco if-else
                    antroponimo.save(flush: true)
                }// fine del blocco if-else
            } else {
                antroponimoRiferimento = antroponimo.voceRiferimento
                if (antroponimoRiferimento) {
                    antroponimo.wikiUrl = antroponimoRiferimento.wikiUrl
                } else {
                    antroponimo.wikiUrl = ''
                }// fine del blocco if-else
                antroponimo.save(flush: true)
            }// fine del blocco if-else
        } // fine del ciclo each
    }// fine del metodo

    /**
     * Ricalcolo records esistenti
     * Controllo della pagina Progetto:Antroponimi/Nomi doppi
     * Ricalcola il numero delle voci (bioGrails) che utilizzano ogni record (antroponimo)
     */
    public void ricalcola() {
        ArrayList<Antroponimo> listaAntroponimi
        int numVociBlocco = Pref.getInt(LibBio.NUM_VOCI_INFO_NOMI_RICALCOLA)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info

        listaNomiDoppi()

        listaAntroponimi = Antroponimo.list(sort: 'nome')
        listaAntroponimi?.each {
            ricalcolaAntroponimo(it)
            k++
            if (LibMat.avanzamento(k, numVociBlocco)) {
                fine = System.currentTimeMillis()
                durata = fine - inizio
                durata = durata / 1000

                info = 'Ricalcolate '
                info += LibTesto.formatNum(k)
                info += ' voci di antroponimi su '
                info += LibTesto.formatNum(listaAntroponimi.size())
                info += ' in '
                info += LibTesto.formatNum(durata)
                info += ' sec. totali'
                log.info info
            }// fine del blocco if
        } // fine del ciclo each
        log.info 'Ricalcolati tutti gli antroponimi'

        //--riempimento del campo wikiUrl di Antroponimi
        regolaWikilink()
        log.info "Regolato il campo wikiUrl di tutti i records che non ce l' avevano"
    }// fine del metodo

    /**
     * Controllo della pagina Progetto:Antroponimi/Nomi doppi
     */
    public void listaNomiDoppi() {
        String titolo = TITOLO_LISTA_NOMI_DOPPI
        String tagInizio = '*'
        String tagRiga = '\\*'
        ArrayList<String> righe = null
        String testoPagina = QueryVoce.getPagina(titolo).getTesto()
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)

        if (testoPagina) {
            testoPagina = testoPagina.substring(testoPagina.indexOf(tagInizio))
            righe = testoPagina.split(tagRiga)
        }// fine del blocco if

        righe?.each {
            elaboraRigaNomiDoppi(it.trim(), soglia)
        } // fine del ciclo each

    }// fine del metodo

    /**
     * Controllo della pagina Progetto:Antroponimi/Nomi doppi
     */
    public void elaboraRigaNomiDoppi(String riga, int soglia) {
        String tagNome = ','
        ArrayList<String> nomi = null
        Antroponimo antroponimo = null

        if (riga) {
            nomi = riga.split(tagNome)
        }// fine del blocco if

        if (nomi) {
            if (nomi.size() > 0) {
                antroponimo = elaboraNomeDoppio(nomi[0].trim(), soglia, true, null)
                if (antroponimo) {
                    antroponimo.voceRiferimento = antroponimo
                    antroponimo.save()
                }// fine del blocco if
                if (nomi.size() > 1) {
                    for (int k = 1; k < nomi.size(); k++) {
                        elaboraNomeDoppio(nomi[k].trim(), soglia, false, antroponimo)
                    } // fine del ciclo for
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo

    /**
     * Controllo della pagina Progetto:Antroponimi/Nomi doppi
     */
    public Antroponimo elaboraNomeDoppio(String nome, int soglia, boolean vocePrincipale, Antroponimo antroponimo) {
        int numVoci = numeroVociCheUsanoNome(nome)

        if (vocePrincipale) {
            if (numVoci > soglia) {
                antroponimo = registraSingoloNome(nome.trim(), numVoci, vocePrincipale)
            }// fine del blocco if
        } else {
            antroponimo = registraSingoloNome(nome.trim(), numVoci, vocePrincipale, antroponimo)
        }// fine del blocco if-else

        return antroponimo
    }// fine del metodo

    /**
     * Check del nome
     * Se valido, regola i link di tutte le biografie
     * Se non valido, annulla i link di tutte le biografie e cancella il record
     */
    private void ricalcolaAntroponimo(Antroponimo antroponimo) {
        String nome
        int numVoci
        boolean nomeValido

        if (antroponimo) {
            nome = antroponimo.nome
            nomeValido = LibBio.checkNome(nome)

            if (nomeValido) {
                numVoci = numeroVociCheUsanoNome(nome, antroponimo)
                antroponimo.voci = numVoci
                antroponimo.save()
            } else {
                //@todo non riesco a far funzionare la pulizia dei links e successiva cancellazione del record
                numeroVociCheUsanoAntroponimo(nome, null)
                antroponimo.voceRiferimento = null
                antroponimo.save()
                antroponimo.delete()
            }// fine del blocco if-else
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

    /**
     * crea le pagine dei singoli nomi a blocchi
     */
    public void upload() {
        def nonServe
        ArrayList<Antroponimo> listaVoci = getListaAntroponimi()
        int numVociBlocco = Pref.getInt(LibBio.NUM_VOCI_INFO_NOMI_UPLOAD)
        long inizio = System.currentTimeMillis()
        long fine
        long durata
        int k = 0
        String info
        Antroponimo antroponimo

        listaVoci?.each {
            antroponimo = (Antroponimo) it

            if (antroponimo.isVocePrincipale) {
                nonServe = new ListaNome(antroponimo)
                k++
            }// fine del blocco if

            if (LibMat.avanzamento(k, numVociBlocco)) {
                fine = System.currentTimeMillis()
                durata = fine - inizio
                durata = durata / 1000

                info = 'Caricate sul server '
                info += LibTesto.formatNum(k)
                info += ' voci di antroponimi su '
                info += LibTesto.formatNum(listaVoci.size())
                info += ' in '
                info += LibTesto.formatNum(durata)
                info += ' sec. totali'
                log.info info
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo


    public creaPagineControllo() {
        //crea la pagina riepilogativa
        paginaNomi()

        //crea le pagine di riepilogo di tutti i nomi
        paginaListe()

        //crea la pagina di controllo didascalie
        paginaDidascalie()
    }// fine del metodo

    def paginaListe() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)
        int soglia = Pref.getInt(LibBio.SOGLIA_ANTROPONIMI)
        String testo = ''
        String titolo = progetto + 'Liste nomi'
        String summary = LibBio.getSummary()
        int k = 0
        def listaNomi
        Antroponimo antro
        ArrayList lista = new ArrayList()
        String nome
//        String vociTxt
        def nonServe
        int numVoci

        listaNomi = Antroponimo.findAllByVociGreaterThan(soglia, [sort: 'voci', order: 'desc'])
        listaNomi?.each {
//            vociTxt = ''
            numVoci = 0
            antro = (Antroponimo) it
            nome = antro.nome
            numVoci = ListaNome.getNumVoci(antro)
            if (numVoci > taglio) {
                nome = "'''[[Persone di nome " + nome + "|" + nome + "]]'''"
            }// fine del blocco if-else
            k++
//            vociTxt = LibTesto.formatNum(numVoci)
            lista.add([nome, numVoci])
        } // fine del ciclo each

        testo += getListeHead(k)
        testo += getListeBody(lista)
        testo += getListeFooter()

        //registra la pagina
        if (testo) {
            testo = testo.trim()
            if (debug) {
                nonServe = new Edit('Utente:Biobot/2', testo, summary)
            } else {
                nonServe = new Edit(titolo, testo, summary)
            }// fine del blocco if-else
        }// fine del blocco if
        def stop
    }// fine del metodo

    private static String getListeHead(int numNomi) {
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
        titoli.add(LibWiki.setBold('Nome'))
        titoli.add(LibWiki.setBold('Voci'))

        mappa.put(WikiLib.MAPPA_TITOLI, titoli)
        mappa.put(WikiLib.MAPPA_LISTA, listaVoci)
        testoTabella = WikiLib.creaTable(mappa)

        return testoTabella
    }// fine del metodo

    private String getListeFooter() {
        String testoFooter = ''

        testoFooter += getCriteri()

        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '==Voci correlate=='
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Nomi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Nomi doppi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Didascalie]]'
        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '<noinclude>'
        testoFooter += '[[Categoria:Liste di persone per nome| ]]'
        testoFooter += aCapo
        testoFooter += '[[Categoria:Progetto Antroponimi|Liste nomi]]'
        testoFooter += '</noinclude>'

        return testoFooter
    }// fine del metodo

    /**
     * Elabora la pagina per un singolo nome
     * @deprecated
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

    //--costruisce una lista di nomi
    public static ArrayList<Antroponimo> getListaAntroponimi() {
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)

        return Antroponimo.findAllByVociGreaterThan(taglio, [sort: 'nome', order: 'asc'])
    }// fine del metodo

    //--costruisce una lista di biografie che 'usano' il nome
    //--se il flag usaNomeSingolo è vero, il nome della voce deve coincidere esattamente col parametro in ingresso
    //--se il flag usaNomeSingolo è falso, il nome della voce deve iniziare col parametro
    private static ArrayList<BioGrails> getListaBiografie(String nome) {
        ArrayList<BioGrails> listaBiografie = new ArrayList()
        BioGrails bio
        String nomeBio
        boolean confrontaSoloPrimo = Pref.getBool(LibBio.USA_SOLO_PRIMO_NOME_ANTROPONIMI)
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
        boolean confrontaSoloPrimo = Pref.getBool(LibBio.USA_SOLO_PRIMO_NOME_ANTROPONIMI)
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

        testo += '<noinclude>'
        testo += "[[Categoria:Liste di persone per nome|${nome}]]"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo

    // pagina di controllo/servizio
    public paginaDidascalie() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String titolo = progetto + 'Didascalie'
        String testo = ''
        String summary = 'Biobot'
        def nonServe

        testo += getDidascalieHeader()
        testo += getDidascalieBody()
        testo += getDidascalieFooter()

        //registra la pagina
        if (testo) {
            testo = testo.trim()
            if (debug) {
                nonServe = new Edit('Utente:Biobot/2', testo, summary)
            } else {
                nonServe = new Edit(titolo, testo, summary)
            }// fine del blocco if-else
        }// fine del blocco if
        def stop
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
        testoFooter += '*[[Progetto:Antroponimi/Nomi doppi]]'
        testoFooter += aCapo
        testoFooter += '*[[Progetto:Antroponimi/Liste]]'
        testoFooter += aCapo
        testoFooter += aCapo
        testoFooter += '<noinclude>'
        testoFooter += '[[Categoria:Liste di persone per nome| ]]'
        testoFooter += aCapo
        testoFooter += '[[Categoria:Progetto Antroponimi|Didascalie]]'
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
    public paginaNomi() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        ArrayList<Antroponimo> listaVoci = getListaAntroponimi()
        String testo = ''
        String titolo = progetto + 'Nomi'
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
                nonServe = new Edit('Utente:Biobot/2', testo, summary)
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


    public String getNomiBody(ArrayList<Antroponimo> listaVoci) {
        String testo = ''
        int taglio = Pref.getInt(LibBio.TAGLIO_ANTROPONIMI)
        Antroponimo antro
        def ricorrenze = LibTesto.formatNum(taglio)

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
        testo += '{{Div col|cols=4}}'
        if (listaVoci) {
            listaVoci.each {
                antro = it
                testo += aCapo
                testo += this.getRiga(antro)
            }// fine del ciclo each
        }// fine del blocco if
        testo += aCapo
        testo += '{{Div col end}}'
        testo += aCapo

        return testo
    }// fine del metodo

    public String getRiga(Antroponimo antro) {
        String testo = ''
        String nome = antro.nome
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
                numVoci = ListaNome.getNumVoci(antro)
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

        testo += getCriteri()

        testo += aCapo
        testo += '==Voci correlate=='
        testo += aCapo
        testo += '*[[Progetto:Antroponimi]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Nomi doppi]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Liste nomi]]'
        testo += aCapo
        testo += '*[[Progetto:Antroponimi/Didascalie]]'
        testo += aCapo
        testo += aCapo
        testo += '<noinclude>'
        testo += '[[Categoria:Liste di persone per nome| ]]'
        testo += aCapo
        testo += '[[Categoria:Progetto Antroponimi|Nomi]]'
        testo += '</noinclude>'

        return testo
    }// fine del metodo


    public String getCriteri() {
        String testo = ''
        String tag = aCapo + '*'

        testo += aCapo
        testo += '==Criteri=='
        testo += tag + "I nomi vengono estratti dal parametro ''nome'' del [[Template:Bio|template:Bio]] di ogni voce biografica"
        testo += tag + "Vengono considerati solo i caratteri alfabetici (UTF8) più il '''trattino''' e '''apice''' iniziale (per i nomi arabi)"
        testo += tag + "Non vengono riportati nomi che iniziano con caratteri numerici '''1, 2, 3, 4, 5, 6, 7, 8, 9, 0'''"
        testo += tag + "Non vengono riportati nomi che iniziano con i caratteri '''[ { ( . , & < ? ! * % -'''"
        testo += tag + "Non vengono riportati nomi che iniziano con le varianti tipografiche di apice '''‘ ‛ ʿ'''"
        testo += tag + "Non vengono riportati titoli o prefissi tipo '''Lady, Sir, Maestro'''"
        testo += tag + "Non vengono riportati titoli o prefissi arabi tipo '''Abd, Abu, Abū, Ibn'''"
        testo += tag + "Non vengono riportati nomi considerati diminutivi impropri di altro nome, tipo '''Gian'''"
        testo += tag + "Non vengono riportati nomi di un solo carattere"
        testo += tag + "Viene considerato solo il primo nome presente nel template della voce"
        testo += tag + "Gli apostrofi vengono rispettati. Pertanto: '''María, Marià, Maria, Mária, Marìa, Mariâ''' sono nomi diversi"
        testo += tag + "I nomi composti formati da una sola parola (tipo '''Gianpaolo''') sono compresi"
        testo += tag + "I nomi composti/doppi formati da più parole (tipo '''Maria Teresa''') sono compresi '''solo''' se sono presenti nell' '''[[Progetto:Antroponimi/Nomi doppi|apposita lista]]'''"
        testo += tag + "I nomi composti con trattino (tipo '''Jean-Baptiste''') sono previsti"
        testo += tag + "Per ogni nome viene creata una pagina con la lista di voci biografiche che riportano il nome stesso come primo nome, anche se poi seguito da altri nomi"
        testo += tag + "Il numero di occorrenze di voci biografiche richiesto per avere una pagina è stato raggiunto per [[Discussioni progetto:Antroponimi|consenso]]"
        testo += tag + "Le pagine sono suddivise per attività principale, come risulta dal parametro ''attività'' del [[Template:Bio|template:Bio]] di ogni voce biografica"
        testo += tag + "Le persone sono riportate col titolo della voce, indipendentemente dal ''nome'' e ''cognome'' utilizzati"
        testo += tag + "Le persone sono ordinate alfabeticamente per cognome, come risulta dal parametro ''cognome'' del [[Template:Bio|template:Bio]] di ogni voce biografica"
        testo += tag + "La didascalia viene presentata come previsto in [[Progetto:Antroponimi/Didascalie]]"
        testo += tag + "Se l'attività principale non risulta dalla voce, le persone vengono raggruppate in un paragrafo finale"
        testo += tag + "I titolo dei paragrafi rispecchiano il genere (maschile o femminile) delle persone che elencano"
        testo += tag + "Se nella stessa pagina ci sono persone di sesso diverso, anche con lo stesso nome (ad esempio ''Andrea''), la pagina stessa viene suddivisa"
        testo += tag + "Se le voci in un paragrafo superano il numero di 50, viene creata una sottopagina"
        testo += tag + "Nella sottopagina le voci sono suddivise per lettera alfabetica"
        testo += tag + "L'aggiornamento delle pagine è ogni una-due settimane"

        return testo
    }// fine del metodo

    /**
     * Ritorna l'antroponimo dal link alla voce
     * Se non esiste, lo crea
     */
    public Antroponimo getAntroponimo(String nomeDaControllare) {
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

    /**
     * Controlla eventuali accenti del nome
     */
    private static boolean nomeSenzaAccento(String nomeConEventualeAccento) {
        boolean senzaAccento = false
        String nomeNormalizzato

        nomeNormalizzato = normalizza(nomeConEventualeAccento)
        if (nomeNormalizzato.equals(nomeConEventualeAccento)) {
            senzaAccento = true
        }// fine del blocco if

        return senzaAccento
    }// fine del metodo

    /**
     * Elimina eventuali accenti dal nome
     */
    private static String normalizza(String nomeConEventualeAccento) {
        String nomeNormalizzato = nomeConEventualeAccento

        if (nomeConEventualeAccento) {
            if (Pref.getBool(LibBio.USA_ACCENTI_NORMALIZZATI, false)) {
                nomeNormalizzato = Normalizer.normalize(nomeConEventualeAccento, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
            }// fine del blocco if
        }// fine del blocco if

        return nomeNormalizzato
    }// fine del metodo

    public boolean isValido(Antroponimo antro) {
        boolean valido = false

        if (antro) {
            valido = LibBio.checkNome(antro.nome)
        }// fine del blocco if

        return valido
    }// fine del metodo


    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea tutti i nomi
     */
    def int uploadAllNomi(BioService bioService) {
        int nomiModificati = 0
        ArrayList<Antroponimo> listaVoci = getListaAntroponimi()
        Antroponimo antroponimo

        listaVoci?.each {
            antroponimo = (Antroponimo) it

            if (antroponimo.isVocePrincipale) {
                if (isValido(antroponimo)) {
                    if (ListaNome.uploadNome(it, bioService)) {
                        nomiModificati++
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if

        } // fine del ciclo each

        return nomiModificati
    } // fine del metodo

} // fine della service classe
