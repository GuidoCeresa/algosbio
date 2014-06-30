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

class FileService {

    boolean transactional = true

    //Sovrascrive un file di testo su disco locale
    //Se il file esiste, lo cancella
    public sovrascrive = { pathFile, testo ->
        boolean riuscito = false

        //controllo di congruità
        if (pathFile && testo) {
            //cancella eventualmente il file
            cancella(pathFile);

            //scrive il file
            riuscito = scrive(pathFile, testo)
        }// fine del blocco if

        //valore di ritorno
        return riuscito
    }// fine della closure

    //Scrive un file di testo su disco locale
    //se non trova la directory, la crea
    //se il file esiste, non fa nulla e ritorna false
    public scrive = { pathFile, testo ->
        boolean riuscito = false
        File file
        FileWriter uscita

        //controllo di congruità
        if (pathFile && testo) {
            //crea il file in memoria
            file = new File((String) pathFile)

            //controlla se esiste
            if (file.exists()) {
                riuscito = false
            } else {
                if (isEsisteDirMadre(pathFile)) {
                    uscita = new FileWriter(file)
                    if (uscita) {
                        uscita.write(testo)
                        uscita.flush()
                        uscita.close()
                    }// fine del blocco if

                    //controlla che esista
                    riuscito = file.isFile()
                } else {
                    creaDirMadre(pathFile)
                    riuscito = scrive(pathFile, testo)
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if

        //valore di ritorno
        return riuscito
    }// fine della closure

    //legge un file di testo dal disco locale
    public legge = { pathFile ->
        def testo = ""
        def file

        if (pathFile) {
            file = new File((String) pathFile)
            if (file.isFile()) {
                testo = file.getText()
            }// fine del blocco if
        }// fine del blocco if

        /* valore di ritorno */
        return testo
    }// fine della closure

    //legge le righe di un file di testo dal disco locale
    public leggeRighe = { pathFile ->
        def righe = []
        def file

        if (pathFile) {
            //crea un oggetto dal nome
            file = new File((String) pathFile)
            if (file.isFile()) {
                file.eachLine {
                    righe.add(it)
                }
            }// fine del blocco if
        }// fine del blocco if

        /* valore di ritorno */
        return righe
    }// fine della closure

    //cancella un file dal disco locale
    //cancella le directory SOLO se sono vuote
    public cancella = { path ->
        /* variabili e costanti locali di lavoro */
        boolean cancellato = false
        File file;

        //controllo di congruità
        if (path) {
            //crea un oggetto dal nome
            file = new File((String) path)
            if (file.isFile()) {
                cancellato = file.delete()
            }// fine del blocco if
        }// fine del blocco if

        //valore di ritorno
        return cancellato
    }// fine della closure

    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public deleteDir = { fileDir ->
        def children
        boolean success

        if (fileDir.isDirectory()) {

            children = fileDir.list()
            children.each {
                success = deleteDir(new File((File) fileDir, (String) it))
                if (!success) {
                    return false
                }
            }
        }

        // The directory is now empty so delete it
        return fileDir.delete()
    }

    //cancella una directory dal disco locale
    //cancella ANCHE il contenuto interno (files e directory) SENZA nessun preavviso
    public cancellaDir = { path ->
        /* variabili e costanti locali di lavoro */
        boolean cancellato = true
        boolean canc = true
        File file;
        def temp

        //controllo di congruità
        if (path) {
            if (isEsisteDir(path)) {
                file = new File((String) path)
                cancellato = deleteDir(file)
            }// fine del blocco if

        }// fine del blocco if

        //valore di ritorno
        return cancellato
    }// fine della closure

    //crea una directory sul disco locale
    public creaDir = { pathDir ->
        boolean creata = false
        File dir;

        //controllo di congruità
        if (pathDir) {
            //crea un oggetto directory dal nome
            dir = new File((String) pathDir)
            dir.mkdirs(); //di default crea anche la madre
            creata = dir.isDirectory()
        }// fine del blocco if

        //valore di ritorno
        return creata
    }// fine della closure

    // Determina se esiste un dato file
    public isEsisteFile = { file ->
        boolean esiste = false

        //controllo di congruità
        if (file) {
            if (file instanceof String) {
                esiste = isEsisteFile(new File((String) file))
            } else {
                if (file instanceof File) {
                    esiste = file.isFile()
                } else {
                    esiste = false
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if

        //valore di ritorno
        return esiste
    }// fine della closure

    // Restituisce la directory madre del file indicato
    public getPathMadre = { pathFile ->
        String pathDir = ""
        String strPathFile
        int pos

        //controllo di congruità
        if (pathFile && pathFile instanceof String) {
            strPathFile = (String) pathFile
            pos = strPathFile.lastIndexOf('/')
            pathDir = strPathFile.substring(0, pos)
        }// fine del blocco if

        //valore di ritorno
        return pathDir
    }// fine della closure

    //crea una directory madre del file indicato, sul disco locale
    public creaDirMadre = { pathFile ->
        boolean creata = false
        String pathDir = ""

        //controllo di congruità
        if (pathFile && pathFile instanceof String) {
            pathDir = getPathMadre(pathFile)
            creata = creaDir(pathDir)
        }// fine del blocco if

        //valore di ritorno
        return creata
    }// fine della closure

    // Determina se esiste la directory che contiene il file indicato
    public isEsisteDirMadre = { pathFile ->
        boolean esiste = false
        String pathDir

        //controllo di congruità
        if (pathFile && pathFile instanceof String) {
            pathDir = getPathMadre(pathFile)
            esiste = isEsisteDir(pathDir)
        }// fine del blocco if

        //valore di ritorno
        return esiste
    }// fine della closure

    // Determina se esiste una data directory
    public isEsisteDir = { directory ->
        boolean esiste = false

        //controllo di congruità
        if (directory) {
            if (directory instanceof String) {
                esiste = isEsisteDir(new File((String) directory))
            } else {
                if (directory instanceof File) {
                    esiste = directory.isDirectory()
                } else {
                    esiste = false
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if

        //valore di ritorno
        return esiste
    }// fine della closure

    //elenco dei files contenuti in una directory locale
    public getDir = { dir ->
        def temp = null
        def files = []
        String nome
        String pathDir = dir

        File file = new File(pathDir)

        if (file.isDirectory()) {
            temp = file.listFiles()
        }// fine del blocco if

        /* elimina quelli nascosti */
        temp.each {
            nome = it.toString()
            if (!nome.startsWith(".")) {
                files.add(nome);
            }// fine del blocco if
        }// fine del blocco if

        /* valore di ritorno */
        return files;
    }// fine della closure

    // Aggiunge i titoli delle colonne ad un file
    public addTitoli = { pathFile, titoli ->
        String testo = ''

        testo = legge(pathFile)
        if (!testo.startsWith(titoli)) {
            testo = titoli + '\n' + testo
            sovrascrive(pathFile, testo)
        }// fine del blocco if
    }// fine della closure

} // fine della service classe
