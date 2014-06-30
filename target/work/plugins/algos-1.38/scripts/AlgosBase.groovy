
class AlgosBase {

    public static String sourceDir
    public static String PREFIX = 'Algos'
    private static String flagOverwrite = 'flagOverwrite'
    private static String aCapo = '\n'

    public static void sovraScrive(String pathFile, String fileDstTxt) {
        File dstFile = new File(pathFile)
        boolean creato

        if (dstFile.exists()) {
            dstFile.write(fileDstTxt)
        } else {
            creato = new File(pathFile).createNewFile()
            if (creato) {
                dstFile = new File(pathFile)
                if (dstFile.exists()) {
                    dstFile.write(fileDstTxt)
                } else {
                    print('Errore - Non sono proprio riuscito a creare il file: ' + pathFile)
                }// fine del blocco if-else
            } else {
                print('Errore - Non sono riuscito a creare il file: ' + pathFile)
            }// fine del blocco if-else
        }// fine del blocco if-else
    } // fine del metodo

    public static sovraScrive(String directory, String fileName, String fileDstTxt) {
        String pathFile = directory + fileName
        File dstFile = new File(pathFile)
        boolean creato

        if (dstFile.exists()) {
            dstFile.write(fileDstTxt)
        } else {
            def ant = new AntBuilder()
            ant.mkdir(dir: directory)
            creato = new File(pathFile).createNewFile()

            if (creato) {
                dstFile = new File(pathFile)
                if (dstFile.exists()) {
                    dstFile.write(fileDstTxt)
                } else {
                    print('Errore - Non sono proprio riuscito a creare il file: ' + pathFile)
                }// fine del blocco if-else
            } else {
                print('Errore - Non sono riuscito a creare il file: ' + pathFile)
            }// fine del blocco if-else
        }// fine del blocco if-else
    } // fine del metodo

    public static void addFile(String dstDir, String dstFile, String iniText) {
        String testo
        String testoOriginario

        if (dstDir && dstFile) {
            testoOriginario = getTextFile(dstDir, dstFile)
            testoOriginario = testoOriginario.trim()
            testo = iniText + aCapo + testoOriginario
            sovraScrive(dstDir, dstFile, testo)
        }// fine del blocco if

    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // presuppone che il nome del file sorgente sia lo stesso, con prefisso 'Algos'
    // il type è una delle 3 modalità: true, false, never
    // il tag è del tipo (enumeration) AlgosTag
    // se il file non esiste, non fa nulla
    public static String creaTxtFile(String srcNomeFile, def algosType, def algosTag) {
        String testo = ''
        String testoBase
        String testoHeader
        String srcDir = sourceDir

        if (srcDir) {
            testoHeader = creaTxtHeader(algosType, algosTag)
            testoBase = getTextFile(srcDir, srcNomeFile)
            testo += testoHeader
            testo += aCapo
            testo += testoBase
        }// fine del blocco if

        return testo
    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // il type è una delle 3 modalità: true, false, never
    public static String creaTxtHeader(def algosType, def algosTag) {
        String testoHeader = ''
        String srcDir = sourceDir
        String headerFileName

        if (algosType && algosTag) {
            headerFileName = algosType.fileName
            testoHeader = getHeader(srcDir + headerFileName, algosTag)
        }// fine del blocco if

        return testoHeader
    } // fine del metodo

    public static String creaIni(def algosType, def algosTag, String iniFileName) {
        String testoIni = ''
        String srcDir = sourceDir
        String xmlNameFile = 'Algosxml'
        String testoPrimaRigaXml

        if (algosTag && iniFileName) {
            testoIni = getHeader(srcDir + iniFileName, algosTag)

            testoPrimaRigaXml = getTextFile(srcDir, xmlNameFile)
            if (algosTag.xmlPrimaRiga) {
                testoIni = testoPrimaRigaXml + testoIni
            }// fine del blocco if
        }// fine del blocco if

        //--i templates non differenziano tra creazione del file e primo inserimento
        if (algosType.fileName.equals('Templates')) {
            testoIni = ''
        }// fine del blocco if

        return testoIni
    } // fine del metodo

    public static String creaIniFile(def algosType, def algosTag) {
        return creaIni(algosType, algosTag, 'File')
    } // fine del metodo

    public static String creaIniHeader(def algosType, def algosTag) {
        return creaIni(algosType, algosTag, 'Header')
    } // fine del metodo

    public static String addIniFile(String fileDstTxt, def algosType, def algosTag) {
        return creaIniFile(algosType, algosTag) + fileDstTxt
    } // fine del metodo

    public static String addIniHeader(String fileDstTxt, def algosType, def algosTag) {
        return creaIniHeader(algosType, algosTag) + fileDstTxt
    } // fine del metodo

    public static String getTextFile(String pathFile) {
        String testo = ''
        File file

        file = new File(pathFile)
        if (file.exists()) {
            testo = file.getText()
        }// fine del blocco if

        return testo
    } // fine del metodo

    public static String getTextFile(String dir, String nameFile) {
        return getTextFile(dir + nameFile)
    } // fine del metodo

    public static String getHeader(String pathHeaderFile, String ini, String end) {
        String headerTxt = ''
        String headerBase

        headerBase = getTextFile(pathHeaderFile)

        headerBase?.eachLine {
            if (it) {
                headerTxt += ini + it + end + aCapo
            }// fine del blocco if
        } // fine del ciclo each

        return headerTxt
    } // fine del metodo

    public static String getHeader(String pathHeaderFile, def algosTag) {
        String aCapo = '\n'
        String headerTxt = ''
        String headerBase

        if (pathHeaderFile && algosTag) {
            headerBase = getTextFile(pathHeaderFile)

            headerBase?.eachLine {
                if (it) {
                    headerTxt += algosTag.ini + it + algosTag.end + aCapo
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return headerTxt
    } // fine del metodo

    // Cerca il valore del tag
    // Il valore dopo l'uguale deve essere uguale a true
    private static boolean sovraScrivibile(dstDir, dstName) {
        boolean sovraScrivibile = false
        String dstPath = dstDir + dstName
        String flag = flagOverwrite
        String riga
        String car
        File dstFile = new File(dstPath)
        String testo = ''
        int posIni
        int posEnd
        String uguale = "="
        String spazio = " "
        String endLine = "\n"
        String next
        String valore
        int posIniValore

        if (dstFile) {
            testo = dstFile.getText()
        }// fine del blocco if

        if (testo) {
            while (testo.contains(flag)) {
                posIni = testo.indexOf(flag)
                posEnd = testo.indexOf(endLine, posIni)
                if (posEnd > 0) {
                    riga = testo.subSequence(posIni, posEnd)
                    riga = riga.trim()
                    if (riga.contains(uguale)) {
                        next = testo.substring(posIni + flag.length(), posIni + flag.length() + 1)
                        if (next.equals(uguale) || next.equals(spazio)) {
                            posIniValore = testo.indexOf(uguale, posIni + 1)
                            valore = testo.substring(posIniValore + 1, posEnd)
                            valore = getValore(valore)
                            if (valore.equals("true")) {
                                sovraScrivibile = true
                            }// fine del blocco if
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine del blocco if
                testo = testo.substring(posIni + 1)
            }// fine del blocco while
        }// fine del blocco if

        return sovraScrivibile
    } // fine del metodo

    // Cerca il tag
    // Ce ne possono essere più di uno
    // Quello significativo deve avere un = prima che la linea vada a capo
    // Il tag deve essere seguito da spazio o uguale (per evitare una parola più lunga che lo contenesse)
    private static boolean esisteFlag(dstDir, dstName) {
        boolean esiste = false
        String dstPath = dstDir + dstName
        def flag = flagOverwrite
        File dstFile = new File(dstPath)
        String testo = ''
        int posIni
        int posEnd
        String uguale = '='
        String spazio = ' '
        String endLine = '\n'
        String riga
        String next

        if (dstFile) {
            testo = dstFile.getText()
        }// fine del blocco if

        if (testo) {
            while (testo.contains(flag)) {
                posIni = testo.indexOf(flag)
                posEnd = testo.indexOf(endLine, posIni)
                if (posEnd > 0) {
                    riga = testo.subSequence(posIni, posEnd)
                    riga = riga.trim()
                    if (riga.contains(uguale)) {
                        next = testo.substring(posIni + flag.length(), posIni + flag.length() + 1)
                        if (next.equals(uguale) || next.equals(spazio)) {
                            esiste = true
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine del blocco if
                testo = testo.substring(posIni + 1)
            }// fine del blocco while
        }// fine del blocco if

        return esiste
    } // fine del metodo

    // Controlla se esiste già l'header previsto
    // L'header viene costruito come quello definitivo
    private static boolean esisteHeader(String dstDir, String dstFile, def algosType, def algosTag) {
        boolean esiste = false
        String testoDest = ''
        String testoIniHeader
        String testoIniFile

        if (dstDir && dstFile && algosType && algosTag) {
            testoIniHeader = creaIniHeader(algosType, algosTag)
            testoIniFile = creaIniFile(algosType, algosTag)
            testoDest = getTextFile(dstDir, dstFile)
            if (testoIniHeader && testoIniFile && testoDest) {
                if (testoDest.startsWith(testoIniHeader) || testoDest.startsWith(testoIniFile)) {
                    esiste = true
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if
        return esiste
    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // presuppone che il nome del file sorgente sia lo stesso, con prefisso 'Algos'
    // algosType è del tipo enumeration AlgosType: true, false, never
    // algosTag è del tipo (enumeration) AlgosTag
    // se creaNuovo è true, lo crea anche se non esiste
    // se creaNuovo è false, se non esiste non fa nulla
    public static exe(String dstDir, String dstFileName, def algosType, def algosTag, String srcFile) {
        exe(dstDir, dstFileName, algosType, algosTag, true, srcFile)
    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // presuppone che il nome del file sorgente sia lo stesso, con prefisso 'Algos'
    // algosType è del tipo enumeration AlgosType: true, false, never
    // algosTag è del tipo (enumeration) AlgosTag
    // se creaNuovo è true, lo crea anche se non esiste
    // se creaNuovo è false, se non esiste non fa nulla
    public static exe(String dstDir, String dstFileName, def algosType, def algosTag, boolean creaNuovo, String srcFile) {
        String fileDstTxt
        String testoHeader
        String dstFile = dstFileName + '.' + algosTag.suffix

        if (srcFile) {
            srcFile = PREFIX + srcFile
        } else {
            srcFile = PREFIX + dstFileName
        }// fine del blocco if-else

        if (esisteFile(sourceDir, srcFile)) {
            fileDstTxt = creaTxtFile(srcFile, algosType, algosTag)
            if (esisteFile(dstDir, dstFile)) {
                if (algosType.fileName.equals('Never')) {
                    if (esisteHeader(dstDir, dstFile, algosType, algosTag)) {
                        print("Il file " + dstFile + " esisteva già, aveva l'header di controllo e non è stato modificato")
                    } else {
                        testoHeader = creaTxtHeader(algosType, algosTag)
                        testoHeader = addIniHeader(testoHeader, algosType, algosTag)
                        addFile(dstDir, dstFile, testoHeader)
                        print("Nel file " + dstFile + " non era presente l'header di controllo che è stato aggiunto in testa al file, senza modificarne il contenuto")
                    }// fine del blocco if-else
                } else {
                    fileDstTxt = addIniHeader(fileDstTxt, algosType, algosTag)
                    if (esisteFlag(dstDir, dstFile)) {
                        if (sovraScrivibile(dstDir, dstFile)) {
                            sovraScrive(dstDir, dstFile, fileDstTxt)
                            print('Nel file ' + dstFile + ' il flag di controllo è true ed il file è stato sovrascritto')
                        } else {
                            print('Nel file ' + dstFile + ' il flag di controllo è falso ed il file non è stato modificato')
                        }// fine del blocco if-else
                    } else {
                        if (fileDstTxt) {
                            sovraScrive(dstDir, dstFile, fileDstTxt)
                            print("Nel file " + dstFile + " non era presente l'header di controllo che è stato aggiunto in testa al file, ed il file è stato sovrascritto")
                        } else {
                            print('Errore - Non ho trovato il contenuto del file: ' + dstFile)
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                }// fine del blocco if-else
            } else {
                if (creaNuovo) {
                    fileDstTxt = addIniFile(fileDstTxt, algosType, algosTag)
                    if (fileDstTxt) {
                        sovraScrive(dstDir, dstFile, fileDstTxt)
                        print('Il file ' + dstFile + ' non esisteva ed è stato creato')
                    } else {
                        print('Errore - Non ho trovato il contenuto del file: ' + dstFile)
                    }// fine del blocco if-else
                } else {
                    print('Il file ' + dstFile + ' non esisteva e non è stato creato')
                }// fine del blocco if-else
            }// fine del blocco if-else
        } else {
            print('Errore - Nella cartella ' + sourceDir + ' non ho trovato il file di servizio per creare: ' + dstDir + dstFileName + '.' + algosTag.suffix)
        }// fine del blocco if-else
    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // presuppone che il nome del file sorgente sia lo stesso, con prefisso 'Algos'
    // algosType è del tipo enumeration AlgosType: true, false, never
    // algosTag è del tipo (enumeration) AlgosTag
    // se il file non esiste, lo crea
    public static exeNewTrue(String dstDir, String dstFileName, def algosType, def algosTag) {
        return exe(dstDir, dstFileName, algosType, algosTag, true, '')
    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // presuppone che il nome del file sorgente sia lo stesso, con prefisso 'Algos'
    // algosType è del tipo enumeration AlgosType: true, false, never
    // algosTag è del tipo (enumeration) AlgosTag
    // se il file non esiste, non fa nulla
    public static exeNewFalse(String dstDir, String dstFileName, def algosType, def algosTag) {
        return exe(dstDir, dstFileName, algosType, algosTag, false, '')
    } // fine del metodo

    // presuppone che la directory del file sorgente sia stata iniettata in questa classe
    // presuppone che il nome del file sorgente sia lo stesso, con prefisso 'Algos'
    // algosType è del tipo enumeration AlgosType: true, false, never
    // algosTag è del tipo (enumeration) AlgosTag
    // se il file non esiste, lo crea
    public static exe(String dstDir, String dstFileName, def algosType, def algosTag) {
        return exe(dstDir, dstFileName, algosType, algosTag, true, '')
    } // fine del metodo

    // Testing if the file already exists
    private static boolean esisteFile(dstDir, dstName) {
        boolean esiste = false
        String dstPath = dstDir + dstName
        File file = new File(dstPath)

        if (file) {
            if (file.exists()) {
                esiste = true
            }// fine del blocco if
        }// fine del blocco if

        return esiste
    } // fine del metodo

    private static addFlag(String directory, String fileName, String headerTxt) {
        String path = directory + fileName
        File dstFile = new File(path)
        String text

        if (dstFile.exists()) {
            text = dstFile.getText()
            text = headerTxt + '\n' + text
            dstFile.write(text)
        } else {
            print('Errore - Non sono riuscito a creare il file: ' + path)
        }// fine del blocco if-else
    } // fine del metodo

    private static String getValore(String valore) {
        ArrayList<String> lista = AlgosTag.getAllEnd()
        valore = valore.trim()

        lista?.each {
            if (valore.endsWith(it)) {
                valore = valore.substring(0, valore.length() - it.length())
                valore = valore.trim()
            }// fine del blocco if
        }
        valore = valore.trim()

        return valore
    } // fine del metodo

} // fine della classe script
