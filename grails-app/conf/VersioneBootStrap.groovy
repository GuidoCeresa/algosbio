import groovy.sql.Sql
import it.algos.algosbio.Anno
import it.algos.algosbio.Genere
import it.algos.algosbio.Giorno
import it.algos.algosbio.Localita
import it.algos.algoslib.Mese
import it.algos.algospref.Pref
import it.algos.algospref.Preferenze
import it.algos.algospref.Type
import it.algos.algosbio.LibBio

import javax.sql.DataSource

class VersioneBootStrap {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def versioneService

    DataSource dataSource

    //--metodo invocato direttamente da Grails
    //--tutte le aggiunte, modifiche e patch vengono inserite con una versione
    //--l'ordine di inserimento è FONDAMENTALE
    def init = { servletContext ->
        //--controllo del flusso
        log.debug 'init'

        //--prima installazione del programma
        if (versioneService && versioneService.installaVersione(1)) {
            versioneService.newVersione('Applicazione', 'Installazione iniziale')
        }// fine del blocco if

        //--connessione tra applicazione e tomcate
        if (versioneService && versioneService.installaVersione(2)) {
            versioneService.newVersione('DataSource', 'Aggiunto autoReconnect=true')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(3)) {
            versioneService.newVersione('Preferenze', 'Aggiunto cassetto e colonne')
        }// fine del blocco if

        //--registra solo se il contenuto (data esclusa) è modificato
        if (versioneService && versioneService.installaVersione(4)) {
            versioneService.newVersione('Upload', 'Aggiunto controllo differenze significative in fase di registrazione voce')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(5)) {
            versioneService.newVersione('Grafica', 'Formattato il numero di persone nel template in testa pagina')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(6)) {
            versioneService.newVersione('Upload', 'Ordinamento alfabetico nei sotogruppi di giorni e anni')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(7)) {
            versioneService.newVersione('Preferenze', 'Aggiunto taglio per antroponimi')
        }// fine del blocco if

        if (versioneService && versioneService.installaVersione(8)) {
            versioneService.newVersione('Applicazione', 'Aggiunta tavola Professione')
        }// fine del blocco if

        if (versioneService && versioneService.installaVersione(9)) {
            versioneService.newVersione('Applicazione', 'Riattivate liste di nomi')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(10)) {
            versioneService.newVersione('Preferenze', 'Aggiunto usaOccorrenzeAntroponimi')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(11)) {
            versioneService.newVersione('Preferenze', 'Aggiunto confrontaSoloPrimoNomeAntroponimi')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(12)) {
            versioneService.newVersione('Antroponimi', 'Modificato titolo delle pagine')
        }// fine del blocco if

        //--aggiunte alcuni parametri
        if (versioneService && versioneService.installaVersione(13)) {
            versioneService.newVersione('BioGrails', 'Nuovo parametro didascaliaListe')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(14)) {
            versioneService.newVersione('Preferenze', 'Aggiunto summary')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(15)) {
            versioneService.newVersione('Preferenze', 'Aggiunti valori per le statistiche')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(16)) {
            versioneService.newVersione('Preferenze', 'Aggiunti valori per i crono jobs')
        }// fine del blocco if

        //--completa il nuovo campo ordine delle preferenze
        if (versioneService && versioneService.installaVersione(17)) {
            //@todo occorre creare manualmente il campo con "alter table preferenze add ordine integer not null"
            def preferenze = Preferenze.list()
            Preferenze preferenza

            preferenze?.each {
                preferenza = it
                preferenza.ordine = preferenza.id
                preferenza.save(flush: true)
            } // fine del ciclo each

            versioneService.newVersione('Preferenze', 'Aggiunti i campi ordine e descrizione')
        }// fine del blocco if

        //--modificate le constraints di BioWiki
        if (versioneService && versioneService.installaVersione(18)) {
            //@todo occorre modificare manualmente il db con "alter table bio_wiki modify column extra_lista longtext set utf8 collate utf8 default null"
            //@todo occorre modificare manualmente il db con "alter table bio_wiki modify column errori longtext set utf8 collate utf8 default null"
            versioneService.newVersione('Database', 'Modificati con default null i campi: BioWiki.extraLista, BioWiki.errori')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(19)) {
            Pref pref = new Pref()
            pref.ordine = 3
            pref.code = LibBio.USA_TAVOLA_CONTENUTI
            pref.descrizione = 'Mostra il sommario in testa alle pagine'
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_TAVOLA_CONTENUTI di default true')
        }// fine del blocco if

        //--aggiunte alcune preferenze
        if (versioneService && versioneService.installaVersione(20)) {
            Pref pref = new Pref()
            pref.ordine = 4
            pref.code = LibBio.TAGLIO_META_ATTIVITA
            pref.descrizione = 'Lunghezza di circa metà della lista delle attività. Calibrato per il tempo impiegato ad elaborarle/caricarle e non sul numero di attività.'
            pref.type = Type.intero
            pref.intero = 250
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'Aggiunto valore di taglio per lo sdoppiamento di attività')
        }// fine del blocco if

        //--modifica nome
        if (versioneService && versioneService.installaVersione(21)) {
            versioneService.newVersione('Applicazione', 'Modificato nome in algosbio')
        }// fine del blocco if

        //--modifica jobs
        if (versioneService && versioneService.installaVersione(21)) {
            versioneService.newVersione('Jobs', 'Modificato il timing')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(22)) {
            scambiaPreferenzeBooleane(LibBio.USA_CRONO_DOWNLOAD, 'tutti i giorni a mezzanotte')
            versioneService.newVersione('Preferenze', 'Spostata USA_CRONO_DOWNLOAD')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(23)) {
            scambiaPreferenzeBooleane(LibBio.USA_CRONO_UPLOAD, 'tutti i giorni alle 8')
            versioneService.newVersione('Preferenze', 'Spostata USA_CRONO_UPLOAD')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(24)) {
            scambiaPreferenzeBooleane(LibBio.USA_CRONO_ELABORA, 'sabato e domenica ogni ora, dalle 10 alle 11 di sera')
            versioneService.newVersione('Preferenze', 'Spostata USA_CRONO_ELABORA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(25)) {
            scambiaPreferenzeBooleane(LibBio.USA_CRONO_ATTIVITA, 'tutti i giovedi alle 10')
            versioneService.newVersione('Preferenze', 'Spostata USA_CRONO_ATTIVITA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(26)) {
            scambiaPreferenzeBooleane(LibBio.USA_CRONO_NAZIONALITA, 'tutti i venerdi alle 10')
            versioneService.newVersione('Preferenze', 'Spostata USA_CRONO_NAZIONALITA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(27)) {
            scambiaPreferenzeBooleane(LibBio.USA_LIMITE_DOWNLOAD, 'usa un limite per le pagine in download')
            versioneService.newVersione('Preferenze', 'Spostata USA_LIMITE_DOWNLOAD')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(28)) {
            scambiaPreferenzeIntero(LibBio.MAX_DOWNLOAD, 'numero massimo di pagine in download')
            versioneService.newVersione('Preferenze', 'Spostata MAX_DOWNLOAD')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(29)) {
            scambiaPreferenzeBooleane(LibBio.DEBUG, 'ambiente di debug')
            versioneService.newVersione('Preferenze', 'Spostata DEBUG')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(30)) {
            scambiaPreferenzeBooleane(LibBio.USA_LIMITE_ELABORA, 'rielaborazione da BioWiki a BioGrails')
            versioneService.newVersione('Preferenze', 'Spostata USA_LIMITE_ELABORA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(31)) {
            scambiaPreferenzeIntero(LibBio.MAX_ELABORA, 'numero massimo di pagine in elaborazione')
            versioneService.newVersione('Preferenze', 'Spostata MAX_ELABORA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(32)) {
            scambiaPreferenzeBooleane(LibBio.REGISTRA_SOLO_MODIFICHE_SOSTANZIALI, 'registra solo le modifiche essenziali escludendo la sola data')
            versioneService.newVersione('Preferenze', 'Spostata REGISTRA_SOLO_MODIFICHE_SOSTANZIALI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(33)) {
            scambiaPreferenzeStringa(LibBio.ANNO_DEBUG, 'anno da utilizzare nel debug degli anni')
            versioneService.newVersione('Preferenze', 'Spostata ANNO_DEBUG')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(34)) {
            scambiaPreferenzeStringa(LibBio.CAT_DEBUG, 'categoria da utilizzare nel debug di biografie')
            versioneService.newVersione('Preferenze', 'Spostata CAT_DEBUG')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(35)) {
            scambiaPreferenzeBooleane(LibBio.USA_CRONO_ANTROPONIMI, 'tutti i lunedi alle 10')
            versioneService.newVersione('Preferenze', 'Spostata USA_CRONO_ANTROPONIMI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(36)) {
            scambiaPreferenzeBooleane(LibBio.USA_OCCORRENZE_ANTROPONIMI, '')
            versioneService.newVersione('Preferenze', 'Spostata USA_OCCORRENZE_ANTROPONIMI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(37)) {
            scambiaPreferenzeIntero(LibBio.TAGLIO_ANTROPONIMI, 'numero di voci necessario per creare la pagina del nome')
            versioneService.newVersione('Preferenze', 'Spostata TAGLIO_ANTROPONIMI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(38)) {
            scambiaPreferenzeIntero(LibBio.SOGLIA_ANTROPONIMI, 'numero di voci necessario per elencare il nome nella lista')
            versioneService.newVersione('Preferenze', 'Spostata SOGLIA_ANTROPONIMI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(39)) {
            scambiaPreferenzeStringa(LibBio.ULTIMA_SINTESI, "data dell'ultima sintesi pubblicata su wiki")
            versioneService.newVersione('Preferenze', 'Spostata ULTIMA_SINTESI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(40)) {
            scambiaPreferenzeIntero(LibBio.VOCI, 'numero di voci gestite')
            versioneService.newVersione('Preferenze', 'Spostata VOCI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(41)) {
            scambiaPreferenzeIntero(LibBio.GIORNI, 'numero di giorni gestiti')
            versioneService.newVersione('Preferenze', 'Spostata GIORNI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(42)) {
            scambiaPreferenzeIntero(LibBio.ANNI, 'numero di anni gestiti')
            versioneService.newVersione('Preferenze', 'Spostata ANNI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(43)) {
            scambiaPreferenzeIntero(LibBio.ATTIVITA, 'numero di attività gestite')
            versioneService.newVersione('Preferenze', 'Spostata ATTIVITA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(44)) {
            scambiaPreferenzeIntero(LibBio.NAZIONALITA, 'numero di nazionalità lista')
            versioneService.newVersione('Preferenze', 'Spostata NAZIONALITA')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(45)) {
            scambiaPreferenzeIntero(LibBio.ATTESA, 'numero -medio- di giorni di attesa per il controllo/riscrittura delle voci biografiche')
            versioneService.newVersione('Preferenze', 'Spostata ATTESA')
        }// fine del blocco if

        //--cancellata una preferenza da Preferenze
        if (versioneService && versioneService.installaVersione(46)) {
            Preferenze preferenza = Preferenze.findByCode(LibBio.SUMMARY)

            if (preferenza) {
                preferenza.delete(flush: true)
            }// fine del blocco if

            versioneService.newVersione('Preferenze', 'Cancellata SUMMARY da Preferenze in quanto già implementata in Pref')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(47)) {
            scambiaPreferenzeBooleane(LibBio.USA_PAGINE_SINGOLE, 'controllo modifiche per singola pagina')
            versioneService.newVersione('Preferenze', 'Spostata USA_PAGINE_SINGOLE')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(48)) {
            scambiaPreferenzeBooleane(LibBio.USA_CASSETTO, 'presenta le liste in un cassetto')
            versioneService.newVersione('Preferenze', 'Spostata USA_CASSETTO')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(49)) {
            scambiaPreferenzeBooleane(LibBio.USA_COLONNE, 'presenta le liste in colonne affiancate')
            versioneService.newVersione('Preferenze', 'Spostata USA_CASSETTO')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(50)) {
            scambiaPreferenzeBooleane(LibBio.CONFRONTA_SOLO_PRIMO_NOME_ANTROPONIMI, 'differnza tra nomi singoli e composti')
            versioneService.newVersione('Preferenze', 'Spostata CONFRONTA_SOLO_PRIMO_NOME_ANTROPONIMI')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(51)) {
            scambiaPreferenzeIntero(LibBio.MAX_RIGHE_CASSETTO, 'numero massimo di righe oltre il quale scatta il cassetto')
            versioneService.newVersione('Preferenze', 'Spostata MAX_RIGHE_CASSETTO')
        }// fine del blocco if

        //--spostate alcune preferenze da Preferenze a Pref
        if (versioneService && versioneService.installaVersione(52)) {
            scambiaPreferenzeIntero(LibBio.MAX_RIGHE_COLONNE, 'numero massimo di righe oltre il quale scattano le colonne affiancate')
            versioneService.newVersione('Preferenze', 'Spostata MAX_RIGHE_COLONNE')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(53)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.CICLO_ANTROPONIMI
            pref.descrizione = 'Numero di voci del blocco elaborazione antroponimi'
            pref.type = Type.intero
            pref.intero = 10
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'CICLO_ANTROPONIMI di default dieci')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(54)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LOG_INFO
            pref.descrizione = 'Registra sul log informazioni di debug dettagliate'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LOG_INFO di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(55)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_MAIL_INFO
            pref.descrizione = 'In caso di errore (grave), spedisce una mail'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_MAIL_INFO di default false')
        }// fine del blocco if

        //--nuovo campo sesso (M o F) in Genere - Valore iniziale per i record esistenti = x
        if (versioneService && versioneService.installaVersione(56)) {
            Genere gen
            def lista = Genere.list()
            lista?.each {
                gen = (Genere) it
                gen.sesso = 'x'
                gen.save(flush: true)
            } // fine del ciclo each
            versioneService.newVersione('Genere', 'Aggiunto campo con valore neutro (da regolare)')
        }// fine del blocco if

        //--nuovo campo sesso (M o F) in Genere - Valore iniziale per i record esistenti = x
        if (versioneService && versioneService.installaVersione(57)) {
            Genere gen
            def lista = Genere.list()
            lista?.each {
                gen = (Genere) it
                gen.delete(flush: true)
            } // fine del ciclo each
            versioneService.newVersione('Genere', 'Reset della tavola')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(58)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.TAGLIO_LOCALITA
            pref.descrizione = 'Numero di voci necessario per creare una pagina della località'
            pref.type = Type.intero
            pref.intero = 100
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'TAGLIO_LOCALITA di default 100')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(59)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_VOCI_PARAGRAFO_ANTROPONIMI
            pref.descrizione = 'Numero di voci del paragrafo di antroponimi per creare una sotto-pagina'
            pref.type = Type.intero
            pref.intero = 50
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_VOCI_PARAGRAFO_ANTROPONIMI di default 50')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(60)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_VOCI_PARAGRAFO_LOCALITA
            pref.descrizione = 'Numero di voci del paragrafo di località per creare una sotto-pagina'
            pref.type = Type.intero
            pref.intero = 50
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_VOCI_PARAGRAFO_LOCALITA di default 50')
        }// fine del blocco if

        //--sdoppiamento campi nome e titolo di Localita
        if (versioneService && versioneService.installaVersione(61)) {
            ArrayList lista = Localita.findAll()
            Localita loc
            String nome
            String tag = '('

            lista?.each {
                loc = (Localita) it
                nome = loc.nome
                if (nome.contains(tag)) {
                    nome = nome.substring(0, nome.indexOf(tag))
                    nome = nome.trim()
                    loc.nomeDiverso = true
                }// fine del blocco if
                loc.titolo = nome
                loc.save(flush: true)
            } // fine del ciclo each

            versioneService.newVersione('Localita', 'Sdoppiati campi nome e titolo')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(62)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_SUDDIVISIONE_UOMO_DONNA
            pref.descrizione = 'Suddivide in uomini e donne le liste dei nomi (antroponimi)'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_SUDDIVISIONE_UOMO_DONNA di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(63)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_CICLI_ELABORA_COGNOMI
            pref.descrizione = 'Numero di cicli massimi di elaborazione dei cognomi'
            pref.type = Type.intero
            pref.intero = 13000
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_CICLI_ELABORA_COGNOMI di default 13000')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(64)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.TAGLIO_COGNOMI
            pref.descrizione = 'Numero di voci necessario per creare una pagina dei cognomi'
            pref.type = Type.intero
            pref.intero = 50
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'TAGLIO_COGNOMI di default 50')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(65)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_CATEGORIA_SOTTOPAGINE_ANTROPONIMI
            pref.descrizione = 'Inserisce la categorizzazione nel footer delle sottopagine di antroponimi'
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_CATEGORIA_SOTTOPAGINE_ANTROPONIMI di default true')
        }// fine del blocco if

        //--nuovo campo mese in Giorno - Valori iniziali per i record esistenti = x
        if (versioneService && versioneService.installaVersione(66)) {
            ArrayList giorni = Giorno.list()
            Giorno giorno
            String nome
            String tagSpazio = ' '

            giorni?.each {
                giorno = (Giorno) it
                nome = giorno.nome

                if (nome) {
                    if (nome.contains(tagSpazio)) {
                        nome = nome.substring(nome.indexOf(tagSpazio))
                    }// fine del blocco if
                    nome = nome.trim()
                    giorno.mese = nome
                    giorno.save(failOnError: true)
                }// fine del blocco if
            } // fine del ciclo each

            versioneService.newVersione('Giorno', 'Aggiunto campo \'mese\'')
        }// fine del blocco if

        //--nuovo campo secolo in Anno - Valori iniziali per i record esistenti = x
        if (versioneService && versioneService.installaVersione(67)) {
            ArrayList anni = Anno.list()
            Anno anno
            String titolo
            int num = 0
            String secolo

            anni?.each {
                anno = (Anno) it
                titolo = anno.titolo
                secolo = ''

                try { // prova ad eseguire il codice
                    num = Integer.decode(titolo)
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch

                if (num >= 1401 && num <= 1500) {
                    secolo = 'XV'
                }// fine del blocco if

                if (num >= 1501 && num <= 1600) {
                    secolo = 'XVI'
                }// fine del blocco if

                if (num >= 1601 && num <= 1700) {
                    secolo = 'XVII'
                }// fine del blocco if

                if (num >= 1701 && num <= 1800) {
                    secolo = 'XVIII'
                }// fine del blocco if

                if (num >= 1801 && num <= 1900) {
                    secolo = 'XIX'
                }// fine del blocco if

                if (num >= 1901 && num <= 2000) {
                    secolo = 'XX'
                }// fine del blocco if

                if (num >= 2001 && num <= 2100) {
                    secolo = 'XXI'
                }// fine del blocco if

                if (secolo) {
                    secolo += ' secolo'
                    anno.secolo = secolo
                    anno.save(failOnError: true)
                }// fine del blocco if
            } // fine del ciclo each

//            versioneService.newVersione('Anno', 'Aggiunto campo \'anno\'')
        }// fine del blocco if

    }// fine della closure


    private static void creaPrefStr(String code, String valore) {
        creaPrefStr(code, valore, '')
    }// fine del metodo

    private static void creaPrefStr(String code, String valore, String descrizione) {
        Pref pref
        int ordine = getMaxOrdine()

        pref = new Pref()
        pref.ordine = ordine
        pref.type = Type.stringa
        pref.code = code
        pref.stringa = valore
        if (descrizione) {
            pref.descrizione = descrizione
        }// fine del blocco if
        pref.save(flush: true)
    }// fine del metodo

    def allungaCampo(Sql sql, String nomeCampo) {
        String query = "alter table wiki.bio_grails modify column `${nomeCampo}` varchar(765)"
        sql.execute(query)
        println('Allungato (longtext) il campo ${nomeCampo}')
    }// fine del metodo

    private static void scambiaPreferenzeBooleane(String code) {
        scambiaPreferenzeBooleane(code, '')
    }// fine del metodo

    private static void scambiaPreferenze(String code, String descrizione, Type type) {
        Preferenze preferenza
        Pref pref
        int ordine = getMaxOrdine()

        preferenza = Preferenze.findByCode(code)
        if (preferenza) {
            ordine++
            pref = new Pref()
            pref.ordine = ordine
            pref.type = type
            pref.code = preferenza.code
            if (descrizione) {
                pref.descrizione = descrizione
            } else {
                pref.descrizione = preferenza.descrizione
            }// fine del blocco if-else

            switch (type) {
                case Type.booleano:
                    pref.bool = preferenza.getBool()
                    break
                case Type.intero:
                    pref.intero = preferenza.getInt()
                    break
                case Type.stringa:
                    pref.stringa = preferenza.getStr()
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch

            pref.save(flush: true)
        }// fine del blocco if

        if (preferenza && Pref.findByCode(code)) {
            preferenza.delete(flush: true)
        }// fine del blocco if
    }// fine del metodo

    private static void scambiaPreferenzeBooleane(String code, String descrizione) {
        scambiaPreferenze(code, descrizione, Type.booleano)
    }// fine del metodo

    private static void scambiaPreferenzeIntero(String code, String descrizione) {
        scambiaPreferenze(code, descrizione, Type.intero)
    }// fine del metodo

    private static void scambiaPreferenzeStringa(String code, String descrizione) {
        scambiaPreferenze(code, descrizione, Type.stringa)
    }// fine del metodo

    private static void scambiaPreferenze(ArrayList lista) {
        lista?.each {
            scambiaPreferenze((String) it)
        } // fine del ciclo each
    }// fine del metodo

    private static int getMaxOrdine() {
        int ordine = 0
        def lista
        Pref preferenza

        lista = Pref.list([sort: 'ordine', order: 'desc'])
        if (lista) {
            preferenza = lista[0]
            if (preferenza) {
                ordine = preferenza.ordine
            }// fine del blocco if
        }// fine del blocco if

        return ordine
    }// fine del metodo

    def destroy = {
    }// fine della closure

}// fine della classe
