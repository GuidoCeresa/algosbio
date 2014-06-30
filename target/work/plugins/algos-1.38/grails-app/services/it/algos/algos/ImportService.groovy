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

package it.algos.algos

class ImportService {

//    /* indirizzo dell'applicazione di servizio sul server */
//    /* proprietà statica per essere visibile anche ai test */
//    public static String server = 'http://192.168.0.254:8080/webdata/'
//
//    /* massimo numero di records per ogni pagina importata */
//    /* proprietà statica per essere visibile anche ai test */
//    public static int limiteImport = 5
//
//    // nome della directory per i dati locali da recuperare
//    // proprietà statica per essere visibile anche ai test
//    public static String prefix = './grails-app/conf/resources/'
//
//    // service sotto transazione - attiva di default
//    boolean transactional = true
//
//    // Regola i records
//    // Aggiunge e/o modifica e/o cancella i records importando i dati
//    // I dati possono essere locali o sul server
//    // modulo (nome base)
//    // tipoImport; come gestire i nuovi records e quelli esistenti:
//    // --> ricostruisce
//    // tipoFile; formato dei dati del file di testo da leggere:
//    // --> tab
//    // locazione; path iniziale del file:
//    // --> localeGrails, localeRete, servereRemoto
//    // pathFile; indirizzo globale del file
//    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
//    // (non serve se tipoImport = ricostruisce)
//    public boolean regola(modulo, String pathFile) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regola(modulo, null, pathFile)
//    }// fine del metodo
//
//    // Regola i records
//    // Aggiunge e/o modifica e/o cancella i records importando i dati
//    // I dati possono essere locali o sul server
//    // modulo (nome base)
//    // service a cui (eventualmente) rinviare il metodo di creazione del singolo record
//    // tipoImport; come gestire i nuovi records e quelli esistenti:
//    // --> ricostruisce
//    // tipoFile; formato dei dati del file di testo da leggere:
//    // --> tab
//    // locazione; path iniziale del file:
//    // --> localeGrails, localeRete, servereRemoto
//    // pathFile; indirizzo globale del file
//    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
//    // (non serve se tipoImport = ricostruisce)
//    public boolean regola(modulo, service, String pathFile) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regola(modulo, service, TipoImport.ricostruisce, TipoFile.tab, pathFile, '')
//    }// fine del metodo
//
//    // Regola i records
//    // Aggiunge e/o modifica e/o cancella i records importando i dati
//    // I dati possono essere locali o sul server
//    // modulo (nome base)
//    // tipoImport; come gestire i nuovi records e quelli esistenti:
//    // --> ricostruisce, aggiorna, aggiunge, modifica
//    // tipoFile; formato dei dati del file di testo da leggere:
//    // --> tab
//    // locazione; path iniziale del file:
//    // --> localeGrails, localeRete, servereRemoto
//    // pathFile; indirizzo globale del file
//    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
//    // (non serve se tipoImport = ricostruisce)
//    public boolean regola(modulo, TipoImport tipoImport, String pathFile, String campoChiave) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regola(modulo, null, tipoImport, pathFile, campoChiave)
//    }// fine del metodo
//
//    // Regola i records
//    // Aggiunge e/o modifica e/o cancella i records importando i dati
//    // I dati possono essere locali o sul server
//    // modulo (nome base)
//    // service a cui (eventualmente) rinviare il metodo di creazione del singolo record
//    // tipoImport; come gestire i nuovi records e quelli esistenti:
//    // --> ricostruisce, aggiorna, aggiunge, modifica
//    // tipoFile; formato dei dati del file di testo da leggere:
//    // --> tab
//    // locazione; path iniziale del file:
//    // --> localeGrails, localeRete, servereRemoto
//    // pathFile; indirizzo globale del file
//    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
//    // (non serve se tipoImport = ricostruisce)
//    public boolean regola(modulo, service, TipoImport tipoImport, String pathFile, String campoChiave) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regola(modulo, service, tipoImport, TipoFile.tab, pathFile, campoChiave)
//    }// fine del metodo
//
//    // Regola i records
//    // Aggiunge e/o modifica e/o cancella i records importando i dati
//    // I dati possono essere locali o sul server
//    // modulo (nome base)
//    // service a cui (eventualmente) rinviare il metodo di creazione del singolo record
//    // tipoImport; come gestire i nuovi records e quelli esistenti:
//    // --> ricostruisce, aggiorna, aggiunge, modifica
//    // tipoFile; formato dei dati del file di testo da leggere:
//    // --> tab
//    // locazione; path iniziale del file:
//    // --> localeGrails, localeRete, servereRemoto
//    // pathFile; indirizzo globale del file
//    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
//    // (non serve se tipoImport = ricostruisce)
//    public boolean regola(modulo, TipoImport tipoImport, TipoFile tipoFile, String pathFile, String campoChiave) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regola(modulo, null, tipoImport, tipoFile, pathFile, campoChiave)
//    }// fine del metodo
//
//    // Regola i records
//    // Aggiunge e/o modifica e/o cancella i records importando i dati
//    // I dati possono essere locali o sul server
//    // modulo (nome base)
//    // service a cui (eventualmente) rinviare il metodo di creazione del singolo record
//    // tipoImport; come gestire i nuovi records e quelli esistenti:
//    // --> ricostruisce, aggiorna, aggiunge, modifica
//    // tipoFile; formato dei dati del file di testo da leggere:
//    // --> tab, csvit, csven, silk, xml, json
//    // locazione; path iniziale del file:
//    // --> localeGrails, localeRete, servereRemoto
//    // pathFile; indirizzo globale del file
//    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
//    // (non serve se tipoImport = ricostruisce)
//    public boolean regola(modulo, service, TipoImport tipoImport, TipoFile tipoFile, String pathFile, String campoChiave) {
//        def riuscito = false
//        def righe
//
//        // se è ricostruzione cancella prima tutti i records
//        if (tipoImport == TipoImport.ricostruisce) {
//            cancella(modulo);
//        }// fine del blocco if
//
//        // recupera la lista di tutte le righe (ogni riga una mappa)
//        righe = getRighe(pathFile, tipoFile)
//
//        // regola i records
//        if (righe) {
//            riuscito = regolaRecords(modulo, service, tipoImport, righe, campoChiave)
//        }// fine del blocco if
//
//        // valore di ritorno
//        return riuscito
//    }// fine del metodo
//
//    // Cancella tutti i records
//    public cancella = {modulo ->
//        if (modulo) {
//            def nome = modulo.getName()
//            modulo.executeUpdate("delete from " + nome)
//            def a
//            //def lista = modulo.list()
//            //lista.each {
//            //    it.delete(flush: true)
//            //}
//        }// fine del blocco if
//    }// fine della closure
//
//    // Controlla che il totale dei records da un webservices non superi il limite stabilito
//    public importaTuttiInsieme = {modulo ->
//        return (totRecords(modulo) < limiteImport)
//    }// fine della closure
//
//    // Recupera il totale dei records da un webservices
//    public totRecords = {modulo ->
//        int records = null
//        String nomeModulo
//        def oggetto = null
//        String strRecords
//
//        if (modulo) {
//            nomeModulo = modulo.name.toLowerCase()
//            oggetto = importaBase(server + nomeModulo + "/totale")
//        }// fine del blocco if
//
//        /* converte */
//        strRecords = oggetto.toString()
//        records = Integer.decode(strRecords)
//
//        /* valore di ritorno */
//        return records
//    }// fine della closure
//
//    // I dati vengono letti dal webservices webdata sul server Algos.
//    public importaSuper = {service, modulo ->
//        def importato = false
//        def records
//        def quantiRecords
//        def delta = ImportService.limiteImport
//        String nome
//
//        /* estrae il nome (minuscolo) del service */
//        nome = getNomeService(service)
//
//        /* invoco il metodo delegato del service che recupera i dati */
//        if (importaTuttiInsieme(modulo)) {
//            records = importaAll(modulo)
//            importato = elaboraPacchetto(service, nome, records)
//        } else {
//            quantiRecords = totRecords(modulo)
//
//            for (int k = 1; k < quantiRecords; k = k + delta) {
//                records = importa(modulo, k, k + delta)
//                if (!elaboraPacchetto(service, nome, records)) {
//                    importato = false
//                }// fine del blocco if
//            } // fine del ciclo for
//
//        }// fine del blocco if-else
//
//        /* valore di ritorno */
//        return importato;
//    }// fine della closure
//
//    // I dati vengono letti dal webservices webdata sul server Algos.
//    // legge solo le province italiane
//    // todo non funziona
//    public importaItaliaSuper = {service, modulo ->
//        def importato
//        def records
//        def quantiRecords
//        String nome
//
//        /* estrae il nome (minuscolo) del service */
//        nome = getNomeService(service)
//
//        /* invoco il metodo delegato del service che recupera i dati */
//        records = importaAll(modulo)
//        importato = elaboraPacchetto(service, nome, records)
//
//        /* valore di ritorno */
//        return importato;
//    }// fine della closure
//
//    // elabora il pacchetto di records
//    public elaboraPacchetto = {service, nome, records ->
//        def importato = false
//
//        /* elabora i dati */
//        if (records != null) {
//            importato = true
//            records."$nome".each {
//                /* crea il singolo record */
//                if (service.creaRecordImport(it) == null) {
//                    importato = false
//                }// fine del blocco if
//            }
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return importato;
//    }// fine della closure
//
//    // Recupera un oggetto da un webservices
//    public importaBase = {indirizzo ->
//        def oggetto = null
//        URL url
//        URLConnection conn
//
//        if (indirizzo instanceof String) {
//            url = new URL((String) indirizzo)
//            conn = url.openConnection()
//            conn.addRequestProperty("accept", "application/xml")
//
//            oggetto = new XmlSlurper().parse(conn.content)
//
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return oggetto
//    }// fine della closure
//
//    // Recupera una lista di records da un webservices
//    public importa = {modulo, primo, ultimo ->
//        def records = null
//        String nomeModulo
//
//        if (modulo) {
//            nomeModulo = modulo.name.toLowerCase()
//            records = importaBase(server + nomeModulo + "/download?primo=" + primo + "&ultimo=" + ultimo)
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return records
//    }// fine della closure
//
//    // Recupera una lista di records da un webservices
//    public importaAll = {modulo ->
//        def records = null
//        String nomeModulo
//
//        if (modulo) {
//            nomeModulo = modulo.name.toLowerCase()
//            records = importaBase(server + nomeModulo + "/download")
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return records
//    }// fine della closure
//
//    // Recupera un singolo NodeChild da un webservices
//    public importaNodo = {modulo, id ->
//        def nodo = null
//        String nomeModulo
//
//        if (modulo) {
//            if (id > 0) {
//                nomeModulo = modulo.name.toLowerCase()
//                nodo = importaBase(server + nomeModulo + "/download?id=" + id)
//            }// fine del blocco if
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return nodo
//    }// fine della closure
//
//    // Recupera un singolo campo da un webservices
//    public importaCampo = {modulo, id, nomeCampo ->
//        String valCampo = ""
//        String nomeModulo
//        def nodo = null
//
//        if (modulo) {
//            nomeModulo = modulo.name.toLowerCase()
//            nodo = importaBase(server + nomeModulo + "/download?id=" + id)
//            if (nomeCampo != null) {
//                if (nodo != null) {
//                    valCampo = nodo."$nomeCampo".text();
//                }// fine del blocco if
//            }// fine del blocco if
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return valCampo
//    }// fine della closure
//
//    // nome del service chiamante
//    public getNomeService = {service ->
//        String nome = ""
//
//        if (service != null) {
//            nome = service.toString()
//            if (nome != null) {
//                if (nome.contains('Service')) {
//                    nome = nome.substring(0, nome.indexOf('Service'));
//                    nome = nome.toLowerCase()
//                }// fine del blocco if
//            }// fine del blocco if
//        }// fine del blocco if
//
//        /* valore di ritorno */
//        return nome;
//    }// fine della closure
//
//    // Vengono creati i records
//    public creaRecords(modulo, righe) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return creaRecords(modulo, null, righe)
//    }// fine del metodo
//
//    // Vengono creati i records
//    public creaRecords(modulo, service, righe) {
//        cancella(modulo);
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regolaRecords(modulo, service, TipoImport.ricostruisce, righe, '')
//    }// fine del metodo
//
//    // Vengono regolati i records
//    public regolaRecords(modulo, tipoImport, righe, nomeCampo) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return regolaRecords(modulo, null, tipoImport, righe, nomeCampo)
//    }// fine del metodo
//
//    // Vengono regolati i records
//    public regolaRecords(modulo, service, tipoImport, righe, nomeCampo) {
//        def riuscito = false
//
//        switch (tipoImport) {
//            case TipoImport.ricostruisce:
//                if (righe in List) {
//                    riuscito = true
//                    righe.each {
//                        if (!creaRecord(modulo, service, it)) {
//                            riuscito = false
//                        }// fine del blocco if
//                    }
//                }// fine del blocco if
//                break;
//            case TipoImport.aggiorna:
//                if (righe in List) {
//                    riuscito = true
//                    righe.each {
//                        def valore = it.get(nomeCampo)
//                        def numRec = getId(modulo, nomeCampo, valore)
//                        if (numRec) {
//                            modificaRecord(modulo, service, it, numRec)
//                        } else {
//                            if (!creaRecord(modulo, service, it)) {
//                                riuscito = false
//                            }// fine del blocco if
//                        }// fine del blocco if-else
//                    }
//                }// fine del blocco if
//                break;
//            case TipoImport.aggiunge:
//                if (righe in List) {
//                    riuscito = true
//                    righe.each {
//                        def valore = it.get(nomeCampo)
//                        def numRec = getId(modulo, nomeCampo, valore)
//                        if (numRec) {
//                        } else {
//                            if (!creaRecord(modulo, service, it)) {
//                                riuscito = false
//                            }// fine del blocco if
//                        }// fine del blocco if-else
//                    }
//                }// fine del blocco if
//                break;
//            case TipoImport.modifica:
//                if (righe in List) {
//                    riuscito = true
//                    righe.each {
//                        def valore = it.get(nomeCampo)
//                        def numRec = getId(modulo, nomeCampo, valore)
//                        if (numRec) {
//                            modificaRecord(modulo, service, it, numRec)
//                        } else {
//                        }// fine del blocco if-else
//                    }
//                }// fine del blocco if
//                break;
//            default: // caso non definito
//                break;
//        } // fine del blocco switch
//
//        // valore di ritorno
//        return riuscito
//    }// fine del metodo
//
//    // Restituisce l'ID del record
//    // Zero o nullo se non trovato
//    public getId = {modulo, nomeCampo, valChiave ->
//        def recNum = 0
//        def criteria
//        def record
//
//        if (modulo) {
//            //todo patch perché il metodo criteria non funziona nei tests
//            switch (nomeCampo) {
//                case 'taNome':
//                    def tot = Prova.count()
//                    def a = Prova.list()
//
//                    record = modulo.findByTaNome(valChiave)
//                    break;
//                case 'taCognome':
//                    record = modulo.findByTaCognome(valChiave)
//                    break;
//                case 'taIndirizzo':
//                    record = modulo.findByTaIndirizzo(valChiave)
//                    break;
//                default: // caso non definito
//                    criteria = modulo.createCriteria()
//                    record = criteria.get {eq(nomeCampo, valChiave)}
//                    break;
//            } // fine del blocco switch
//            recNum = record?.id
//        }// fine del blocco if
//
//        // valore di ritorno
//        return recNum
//    }// fine della closure
//
//    // Viene creato il singolo record
//    // metodo sovrascritto nel service (se esiste)
//    // return istanza (record) creata della domainClass interessata
//    public Object creaRecord(modulo, service, mappa) {
//        if (service) {
//            try { // prova ad eseguire il codice;
//                return service.creaRecord(modulo, mappa)
//            } catch (Exception unErrore) { // intercetta l'errore
//                return creaRecord(modulo, mappa)
//            }// fine del blocco try-catch
//        } else {
//            return creaRecord(modulo, mappa)
//        }// fine del blocco if-else
//    }// fine del metodo
//
//    // Viene creato il singolo record
//    // return istanza (record) creata della domainClass interessata
//    public Object creaRecord(modulo, mappa) {
//        Object record = null  // istanza
//        def campi
//
//        // controllo di congruità
//        if (mappa in Map) {
//            record = modulo.newInstance()
//            campi = record.properties
//            mappa.each {
//                String chiave = it.key
//                chiave = LibTesto.primaMinuscola(chiave)
//                def valore = it.value
//                if (campi.keySet().contains(chiave)) {
//                    record."${chiave}" = valore
//                }// fine del blocco if
//            }
//
//            //todo manca la gestione dei link
//
//            // registra l'istanza
//            try { // prova ad eseguire il codice;
//                record = record.save(flush: true)
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//            //if (record.id &&record.version) {
//            //    record = record.save(flush: true)
//            //}// fine del blocco if
//
//        }// fine del blocco if
//
//        // valore di ritorno
//        return record
//    }// fine del metodo
//
//    // Viene modificato il singolo record recuperato col  campo chiave
//    // metodo sovrascritto nel service (se esiste)
//    // return istanza (record) creata della domainClass interessata
//    public Object modificaRecord(modulo, service, mappa, id) {
//        if (service) {
//            try { // prova ad eseguire il codice;
//                return service.modificaRecord(modulo, mappa, id)
//            } catch (Exception unErrore) { // intercetta l'errore
//                return modificaRecord(modulo, mappa, id)
//            }// fine del blocco try-catch
//        } else {
//            return modificaRecord(modulo, mappa, id)
//        }// fine del blocco if-else
//    }// fine del metodo
//
//    // Viene modificato il singolo record recuperato col  campo chiave
//    // return istanza (record) creata della domainClass interessata
//    public Object modificaRecord(modulo, mappa, id) {
//        Object record = null  // istanza
//
//        // controllo di congruità
//        if (mappa in Map) {
//            record = modulo.get(id)
//            mappa.each {
//                String chiave = it.key
//                chiave = LibTesto.primaMinuscola(chiave)
//                def valore = it.value
//                record."${chiave}" = valore
//            }
//
//            //todo manca la gestione dei link
//
//            // registra l'istanza
//            record = record.save(flush: true)
//        }// fine del blocco if
//
//        // valore di ritorno
//        return record
//    }// fine del metodo
//
//    // Lettura dei dati
//    // I dati possono essere locali o sul server
//    // Presume che il file sia locale e si trovi al path indicato
//    // Presume che i dati siano separati dal separatore stabilito
//    // Restituisce una lista di mappe (titolo, valore) per ogni riga
//    public List getRighe(String pathFile) {
//        // invoca il metodo sovrascritto con alcuni parametri di default
//        return getRighe(pathFile, TipoFile.tab)
//    }// fine del metodo
//
//    // Lettura dei dati
//    // I dati possono essere locali o sul server
//    // Presume che il file sia locale e si trovi al path indicato
//    // Presume che i dati siano separati dal separatore stabilito
//    // Restituisce una lista di mappe (titolo, valore) per ogni riga
//    public List getRighe(String pathFile, TipoFile tipoFile) {
//        // variabili e costanti locali di lavoro
//        List righe = [] // lista di righe
//
//        switch (tipoFile) {
//            case TipoFile.tab:
//                righe = getTab(pathFile)
//                break
//            case 2:
//
//                break
//            default: // caso non definito
//                break
//        } // fine del blocco switch
//
//        // valore di ritorno
//        return righe
//    }// fine del metodo
//
//    // Lettura dei dati da un file locale in formato tab
//    // Presume che il file sia locale e si trovi al path indicato
//    // Presume che i dati siano separati dal separatore stabilito
//    // Presume che la prima riga sia costituita dai titoli dei campi
//    // Restituisce una lista di mappe (titolo, valore) per ogni riga
//    public List getTab(String pathFile) {
//        // variabili e costanti locali di lavoro
//        List righe = [] // lista di righe
//        def file
//        def sep = TipoFile.tab.getSeparatore()
//        def campi
//        def titoli
//        String titolo
//        Map map // una per ogni riga del file
//
//        file = new File((String) pathFile)
//
//        if (file.exists()) {
//            // crea la mappa coi titoli dei campi
//            file.eachLine {it, firstLine ->
//                if (firstLine == 1) {
//                    titoli = it.split(sep)
//                } else {
//                    campi = it.split(sep)
//                    map = [:]
//                    campi.eachWithIndex {campo, index ->
//                        if (index < titoli.size()) {
//                            titolo = titoli[index]
//                            titolo = LibTesto.primaMinuscola(titolo)
//                            map.put(titolo, campo)
//                        }// fine del blocco if
//                    }
//                    righe.add(map)
//                }// fine del blocco if-else
//            }
//        }// fine del blocco if
//
//        // valore di ritorno
//        return righe
//    }// fine del metodo
//
//    // Costruisce i titoli delle colonne
//    // Restituisce sempre una riga di titoli separati dal tabulatore
//    // I titoli hanno l'iniziale minuscola
//    // In ingresso può esserci una stringa di nomi separati da virgola oppure una lista di nomi
//    public creaTitoli = {valore ->
//        def titoli = ''
//        def lista = null
//        def tab = '\t'
//        def fine = '\n'
//
//        if (valore in String) {
//            lista = valore.split(',')
//        }// fine del blocco if
//
//        if (valore in List) {
//            lista = valore
//        }// fine del blocco if
//
//        if (lista) {
//            lista.each {campo ->
//                campo = LibTesto.primaMinuscola(campo.trim())
//                titoli += campo + tab
//            }
//            titoli = titoli.trim()
//        }// fine del blocco if
//
//        // valore di ritorno
//        return titoli
//    }// fine del metodo

} // fine della service classe
