import groovy.sql.Sql
import it.algos.algosbio.Anno
import it.algos.algosbio.Antroponimo
import it.algos.algosbio.Genere
import it.algos.algosbio.Giorno
import it.algos.algosbio.LibPass
import it.algos.algosbio.Localita
import it.algos.algosbio.Secolo
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
            scambiaPreferenzeBooleane(LibBio.USA_SOLO_PRIMO_NOME_ANTROPONIMI, 'differnza tra nomi singoli e composti')
            versioneService.newVersione('Preferenze', 'Spostata USA_SOLO_PRIMO_NOME_ANTROPONIMI')
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
            String secolo

            anni?.each {
                anno = (Anno) it
                titolo = anno.titolo
                secolo = Secolo.getSecolo(titolo)
                if (secolo) {
                    anno.secolo = secolo
                    anno.save(failOnError: true)
                }// fine del blocco if
            } // fine del ciclo each

            versioneService.newVersione('Anno', 'Aggiunto campo \'anno\'')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(68)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.REGISTRA_ALL_GIORNI_ANNI
            pref.descrizione = "Registra tutti i giorni e tutti gli anni anche se sono 'puliti'"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'REGISTRA_ALL_GIORNI_ANNI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(69)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.BOT_NAME
            pref.descrizione = "Nickname del bot usato"
            pref.type = Type.stringa
            pref.stringa = 'Biobot'
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'BOT_NAME di default Biobot')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(70)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.BOT_PASSWORD
            pref.descrizione = "Password (criptata) del bot usato"
            pref.type = Type.stringa
            pref.stringa = LibPass.codifica('criptata')
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'BOT_PASSWORD di default ...')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(71)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_ACCENTI_NORMALIZZATI
            pref.descrizione = "Sostituisce le lettere accentate con le corrispondenti 'neutre', nelle ricerche degli antroponimi. Trova uguali Aaron e Aaròn."
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_ACCENTI_NORMALIZZATI di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(72)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LISTA_NOMI_DOPPI
            pref.descrizione = "Legge la pagina Progetto:Antroponimi/Nomi doppi e aggiunge i records di Antroponimo"
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LISTA_NOMI_DOPPI di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(73)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.NUM_VOCI_INFO_NOMI_RICALCOLA
            pref.descrizione = 'Numero di voci per il blocco di stampa info nel ricalcolo degli antroponimi'
            pref.type = Type.intero
            pref.intero = 100
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'NUM_VOCI_INFO_NOMI_RICALCOLA di default 100')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(74)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.NUM_VOCI_INFO_NOMI_UPLOAD
            pref.descrizione = "Numero di voci per il blocco di stampa info nell'upload degli antroponimi"
            pref.type = Type.intero
            pref.intero = 20
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'NUM_VOCI_INFO_NOMI_UPLOAD di default 20')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(75)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_NOME_COGNOME_PER_TITOLO
            pref.descrizione = "Legge la pagina Progetto:Antroponimi/Nomi doppi e aggiunge i records di Antroponimo"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_NOME_COGNOME_PER_TITOLO di default false')
        }// fine del blocco if

        //--intervento una tantum
        if (versioneService && versioneService.installaVersione(76)) {
            versioneService.newVersione('Antroponimi', 'Riempimento del campo wikiUrl per chi supera le 50 voci')
        }// fine del blocco if

        //--modifica codice preferenza
        if (versioneService && versioneService.installaVersione(77)) {
            Pref pref = Pref.findByCode(LibBio.TAGLIO_COGNOMI_DEPRECATO)
            pref.code = LibBio.TAGLIO_COGNOMI
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'Corretta codifica TAGLIO_COGNOMI, precedentemente scritta errata')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(78)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.SOGLIA_COGNOMI
            pref.descrizione = "numero di voci necessario per elencare il cognome nella lista"
            pref.type = Type.intero
            pref.intero = 50
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'SOGLIA_COGNOMI di default 50')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(79)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_RICALCOLA_COGNOMI
            pref.descrizione = "numero di records di cognomi che vengono ricalcolati per controllare il numero di voci e cancellare quelli al di sotto della soglia"
            pref.type = Type.intero
            pref.intero = 1000
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_RICALCOLA_COGNOMI di default 1000')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(80)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LISTE_BIO_GIORNI
            pref.descrizione = "usa le nuove liste"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LISTE_BIO_GIORNI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(81)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LISTE_BIO_ANNI
            pref.descrizione = "usa le nuove liste"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LISTE_BIO_ANNI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(82)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_SUDDIVISIONE_PARAGRAFI_GIORNI
            pref.descrizione = "Suddivisione in paragrafi (secoli) delle liste per anno di nati nel giorno"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_SUDDIVISIONE_PARAGRAFI_GIORNI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(83)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_SUDDIVISIONE_PARAGRAFI_ANNI
            pref.descrizione = "Suddivisione in paragrafi (mesi) delle liste per giorno di nati nel anno"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_SUDDIVISIONE_PARAGRAFI_ANNI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(84)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LISTE_BIO_NAZIONALITA
            pref.descrizione = "usa le nuove liste"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LISTE_BIO_NAZIONALITA di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(85)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LISTE_BIO_ATTIVITA
            pref.descrizione = "usa le nuove liste"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LISTE_BIO_ATTIVITA di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(86)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_LISTE_BIO_NOMI
            pref.descrizione = "usa le nuove liste"
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_LISTE_BIO_NOMI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(87)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_SUDDIVISIONE_UOMO_DONNA_NAZ
            pref.descrizione = 'Suddivide in uomini e donne le liste di nazionalità'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_SUDDIVISIONE_UOMO_DONNA_NAZ di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(88)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_VOCI_PARAGRAFO_NAZIONALITA
            pref.descrizione = 'Numero di voci del paragrafo nelle liste di nazionalità per creare una sotto-pagina'
            pref.type = Type.intero
            pref.intero = 100
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_VOCI_PARAGRAFO_NAZIONALITA di default 100')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(89)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_VOCI_PARAGRAFO_ATTIVITA
            pref.descrizione = 'Numero di voci del paragrafo nelle liste di attività per creare una sotto-pagina'
            pref.type = Type.intero
            pref.intero = 100
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_VOCI_PARAGRAFO_ATTIVITA di default 100')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(90)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.NOME_TEMPLATE_AVVISO_LISTE_GIORNI_ANNI
            pref.descrizione = "Nome template di avviso in testa alle liste di giorni ed anni"
            pref.type = Type.stringa
            pref.stringa = 'ListaBio'
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'NOME_TEMPLATE_AVVISO_LISTE_GIORNI_ANNI di default ListaBio')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(91)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.NOME_TEMPLATE_AVVISO_LISTE_NAZ_ATT
            pref.descrizione = "Nome template di avviso in testa alle liste di nazionalità ed attività"
            pref.type = Type.stringa
            pref.stringa = 'ListaBio'
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'NOME_TEMPLATE_AVVISO_LISTE_NAZ_ATT di default ListaBio')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(92)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.NOME_TEMPLATE_AVVISO_LISTE_NOMI_COGNOMI
            pref.descrizione = "Nome template di avviso in testa alle liste di nomi e cognomi"
            pref.type = Type.stringa
            pref.stringa = 'StatBio'
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'NOME_TEMPLATE_AVVISO_LISTE_NOMI_COGNOMI di default StatBio')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(93)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_TITOLO_PARAGRAFO_NAZ_ATT_LINK_PROGETTO
            pref.descrizione = 'Usa il link alle pagine di progetto nei titoli dei paragrafi di nazionalità ed attività'
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_TITOLO_PARAGRAFO_NAZ_ATT_LINK_PROGETTO di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(94)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_ATTIVITA_MULTIPLE
            pref.descrizione = 'Usa tutte le 3 attività nelle liste di Nazionalità. Le persone appaiono in più di un paragrafo.'
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_ATTIVITA_MULTIPLE di default true')
        }// fine del blocco if

        //--modifica transactional
        if (versioneService && versioneService.installaVersione(95)) {
            versioneService.newVersione('Transactional', 'Eliminate le transazioni in Giorno ed Anno')
        }// fine del blocco if

        //--regolata mail
        if (versioneService && versioneService.installaVersione(96)) {
            versioneService.newVersione('Mail', 'Regolati i parametri in config.groovy')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(97)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_PARAGRAFO_PUNTI_GIORNI_ANNI
            pref.descrizione = "Usa i puntini come titolo del paragrafo di giorni ed anni mancante di specificazione. In alternativa usa 'senza mese' e 'senza giorno'."
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_PARAGRAFO_PUNTI_GIORNI_ANNI di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(98)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_VOCI_PAGINA_NAZIONALITA
            pref.descrizione = 'Numero di voci di nazionalità per attivare la creazione per pagine separate'
            pref.type = Type.intero
            pref.intero = 10000
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_VOCI_PAGINA_NAZIONALITA di default 10.000')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(99)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_DUE_COLONNE_STATISTICHE_NAZIONALITA
            pref.descrizione = "Nelle statistiche di nazionalità, usa due colonne: liste e categorie"
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_DUE_COLONNE_STATISTICHE_NAZIONALITA di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(100)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_GIORNI_ANNI_RAGGRUPPATI
            pref.descrizione = "Nelle liste di giorni ed anni, raggruppa le voci per giorno/anno"
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_GIORNI_ANNI_RAGGRUPPATI di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(101)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_DUE_COLONNE_STATISTICHE_ATTIVITA
            pref.descrizione = "Nelle statistiche di attività, usa sei colonne: liste e categorie (oltre ai numeri)"
            pref.type = Type.booleano
            pref.bool = true
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_DUE_COLONNE_STATISTICHE_ATTIVITA di default true')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(102)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_SUDDIVISIONE_UOMO_DONNA_ATT
            pref.descrizione = 'Suddivide in uomini e donne le liste di attività'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_SUDDIVISIONE_UOMO_DONNA_ATT di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(103)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.MAX_VOCI_PAGINA_ATTIVITA
            pref.descrizione = 'Numero di voci di attività per attivare la creazione per pagine separate'
            pref.type = Type.intero
            pref.intero = 10000
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'MAX_VOCI_PAGINA_ATTIVITA di default 10.000')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(104)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_SOTTOPAGINA_ALTRI
            pref.descrizione = 'Sottopagina per il paragrafo ... altri...'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_SOTTOPAGINA_ALTRI di default false')
        }// fine del blocco if

        //--creata una nuova preferenza
        if (versioneService && versioneService.installaVersione(105)) {
            Pref pref = new Pref()
            pref.ordine = Pref.list().size() + 1
            pref.code = LibBio.USA_RICALCOLO_NOMI
            pref.descrizione = 'Ricalcola la dimensione delle occorrenze singole di nomi (antroponimi)'
            pref.type = Type.booleano
            pref.bool = false
            pref.save(flush: true)
            versioneService.newVersione('Preferenze', 'USA_RICALCOLO_NOMI di default false')
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

}// fine della classe di bootstrap
