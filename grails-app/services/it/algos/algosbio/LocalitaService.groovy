package it.algos.algosbio

import grails.transaction.Transactional
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiService

@Transactional
class LocalitaService {

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    AntroponimoService antroponimoService = new AntroponimoService()

    private static tagTitoloNati = 'Persone nate a '
    private static tagTitoloMorti = 'Persone morte a '
    public static String PATH = 'Progetto:Biografie/'
    private static String aCapo = '\n'

    // Elabora tutte le pagine
    def elabora() {
        Localita localita
        String nome
        ArrayList<Localita> listaLocalita = getListaLocalita()

        listaLocalita?.each {
            localita = (Localita) it
            nome = localita.nome
            if (LibBio.checkNome(nome)) {
                elaboraSingolaLocalita(it)
            }// fine del blocco if
        }// fine del ciclo each

//            creaPaginaControllo()
        def stop
    }// fine del metodo

    /**
     * Elabora la pagina per una singola localita
     */
    public void elaboraSingolaLocalita(Localita localita) {
        int taglio = Pref.getInt(LibBio.TAGLIO_LOCALITA, 100)
        ArrayList<BioGrails> listaBiografieNati
        ArrayList<BioGrails> listaBiografieMorti
        int vociNati = 0
        int vociMorti = 0
        boolean esistePaginaNati = false
        boolean esistePaginaMorti = false

        listaBiografieNati = getListaBiografie(localita, NatoMorto.nato)
        listaBiografieMorti = getListaBiografie(localita, NatoMorto.morto)

        if (listaBiografieNati) {
            vociNati = listaBiografieNati.size()
        }// fine del blocco if
        if (listaBiografieMorti) {
            vociMorti = listaBiografieMorti.size()
        }// fine del blocco if

        if (vociNati >= taglio) {
            esistePaginaNati = true
        }// fine del blocco if
        if (vociMorti >= taglio) {
            esistePaginaMorti = true
        }// fine del blocco if

        if (esistePaginaNati || esistePaginaMorti) {
            elaboraSingolaLocalita(localita, NatoMorto.nato, listaBiografieNati)
            elaboraSingolaLocalita(localita, NatoMorto.morto, listaBiografieMorti)
        }// fine del blocco if

    }// fine del metodo

    /**
     * Elabora la pagina per una singola localita
     */
    public void elaboraSingolaLocalita(Localita localita, NatoMorto tagNatoMorto, ArrayList<BioGrails> listaBiografie) {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String titolo = ''
        String testo = ''
        String summary = LibBio.getSummary()
        int taglio = Pref.getInt(LibBio.TAGLIO_LOCALITA, 100)
        String nomeLocalita = localita.nome

        switch (tagNatoMorto) {
            case NatoMorto.nato:
                titolo = tagTitoloNati + nomeLocalita
                break
            case NatoMorto.morto:
                titolo = tagTitoloMorti + nomeLocalita
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        //header
        testo += this.getHead(localita, listaBiografie.size())

        //body
        testo += this.getBody(listaBiografie, localita, true, tagNatoMorto)

        //footer
        testo += this.getFooter(localita, tagNatoMorto)

        testo = testo.trim()

        //registra la pagina
        if (!debug) {
            new EditBio(titolo, testo, summary)
        }// fine del blocco if

    }// fine del metodo

    public String getHead(Localita localita, int num) {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String numero = ''
        boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI)
        String titoloVoceWiki
        String nome
        String incipit
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

        if (localita) {
            titoloVoceWiki = localita.titolo
            testo += "{{torna a|${titoloVoceWiki}}}"
            testo += aCapo
        }// fine del blocco if

        testo += '<noinclude>'
        testo += aCapo
        testo += "{{StatBio"
        if (numero) {
            testo += "|bio=$numero"
        }// fine del blocco if
        testo += "|data=$dataCorrente}}"
        testo += aCapo

        if (localita) {
            nome = localita.nome
            incipit = "Questa è una lista di persone presenti nell'enciclopedia che sono nate a '''${nome}''', suddivise per attività principale."
            testo += incipit
            testo += aCapo
        }// fine del blocco if

        testo += aCapo

        return testo
    }// fine del metodo

    public String getBody(ArrayList listaVoci, Localita localita, boolean usaSottopagine, NatoMorto tagNatoMorto) {
        String testo = ''
        Map mappa
        String chiave
        ArrayList<String> listaDidascalie
        int num = 0
        int maxVoci = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_LOCALITA, 100)

//        maxVoci = 5 //@TODO provvisorio

        mappa = antroponimoService.getMappaAttività(listaVoci)
        mappa = antroponimoService.ordinaMappa(mappa)
        if (mappa) {
            mappa?.each {
                chiave = it.key
                listaDidascalie = (ArrayList<String>) mappa.get(chiave)
                num = listaDidascalie.size()
                if (usaSottopagine && num >= maxVoci) {
                    testo += getBodySottoPagina(chiave, listaDidascalie, localita, tagNatoMorto)
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

    public String getHeadSottoPagina(String titoloRitorno, int num) {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())
        String numero = ''
        boolean usaTavolaContenuti = Pref.getBool(LibBio.USA_TAVOLA_CONTENUTI)
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
        testo += aCapo

        return testo
    }// fine del metodo

    public String getBodySottoPagina(String chiave, ArrayList listaDidascalie, Localita localita, NatoMorto tagNatoMorto) {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String summary = LibBio.getSummary()
        String testo = ''
        String sottoTitolo
        String sottoChiave
        String tag = '=='
        boolean usaCategoriaNellaSottopagina = false
        String nome = localita.nome
        String torna = ''

        sottoChiave = chiave.substring(chiave.indexOf('|') + 1, chiave.indexOf(']]'))
        switch (tagNatoMorto) {
            case NatoMorto.nato:
                torna = tagTitoloNati + nome
                break
            case NatoMorto.morto:
                torna = tagTitoloMorti + nome
                break
            default: // caso non definito
                break
        } // fine del blocco switch
        sottoTitolo = torna + '/' + sottoChiave

        //header
        testo += this.getHeadSottoPagina(torna, listaDidascalie.size())

        //body
        testo += aCapo
        testo += antroponimoService.getParagrafoDidascalia(listaDidascalie)
        testo += aCapo
        testo += aCapo

        //footer
        if (usaCategoriaNellaSottopagina) {
            testo += this.getFooter(localita, tagNatoMorto)
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

    public String getFooter(Localita localita, NatoMorto tagNatoMorto) {
        String testo = ''
        String aCapo = '\n'
        String nome = localita.nome
        String loc = ''

        switch (tagNatoMorto) {
            case NatoMorto.nato:
                loc = 'nascita'
                break
            case NatoMorto.morto:
                loc = 'morte'
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        testo += '<noinclude>'
        testo += "[[Categoria:Liste di persone per località di ${loc}|${nome}]]"
        testo += '</noinclude>'
        testo += aCapo

        return testo
    }// fine del metodo

    /**
     * Ritorna la località dal link alla voce
     * Se non esiste, la crea
     */
    public  static Localita getLuogoNascita(BioWiki bioWiki) {
        return getLocalita(bioWiki.luogoNascita, bioWiki.luogoNascitaLink, NatoMorto.nato)
    } // fine del metodo

    public  static Localita getLuogoMorte(BioWiki bioWiki) {
        return getLocalita(bioWiki.luogoMorte, bioWiki.luogoMorteLink, NatoMorto.morto)
    } // fine del metodo

    /**
     * Ritorna la località dal link alla voce
     * Se non esiste, la crea
     */
    private  static Localita getLocalita(String luogo, String luogoLink, NatoMorto natoMorto) {
        Localita localita = null

        if (luogo || luogoLink) {
            if (luogo) {
                luogo = luogo.trim()
            }// fine del blocco if
            if (luogoLink) {
                luogoLink = luogoLink.trim()
            } else {
                luogoLink = luogo
            }// fine del blocco if-else

            try { // prova ad eseguire il codice
                localita = Localita.findByNome(luogoLink)
            } catch (Exception unErrore) { // intercetta l'errore
//                log.info('getLocalita errata - luogo: ' + luogo + ' e luogoLink: ' + luogoLink)
            }// fine del blocco try-catch
            if (!localita) {
                localita = new Localita(nome: luogoLink)
            }// fine del blocco if

            if (localita) {
                switch (natoMorto) {
                    case NatoMorto.nato:
                        localita.nati = localita.nati + 1
                        break
                    case NatoMorto.morto:
                        localita.morti = localita.morti + 1
                        break
                    default: // caso non definito
                        break
                } // fine del blocco switch

                try { // prova ad eseguire il codice
                    localita.save(flush: true)
                } catch (Exception unErrore) { // intercetta l'errore
                    def a = unErrore
                }// fine del blocco try-catch
            }// fine del blocco if
        }// fine del blocco if

        return localita
    } // fine del metodo

    //--costruisce una lista di località
    public static ArrayList<Localita> getListaLocalita() {
        int taglio = Pref.getInt(LibBio.TAGLIO_LOCALITA, 100)
//        return Localita.findAllByNatiGreaterThanEqualsOrMortiGreaterThanEquals(taglio, taglio, [sort: 'nome'])

        return Localita.list([sort: 'nome'])
    }// fine del metodo

    //--costruisce una lista di biografie che 'usano' la località
    private static ArrayList<BioGrails> getListaBiografie(Localita localita, NatoMorto tagNatoMorto) {
        ArrayList<BioGrails> listaBiografie = new ArrayList()

        switch (tagNatoMorto) {
            case NatoMorto.nato:
                listaBiografie = BioGrails.findAllByLuogoNatoLink(localita, [sort: 'forzaOrdinamento'])
                break
            case NatoMorto.morto:
                listaBiografie = BioGrails.findAllByLuogoMortoLink(localita, [sort: 'forzaOrdinamento'])
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        return listaBiografie
    }// fine del metodo


    private static enum NatoMorto {
        nato, morto
    } // fine della Enumeration

} // fine della service classe
