package it.algos.algoswiki

import it.algos.algoslib.LibHtml
import it.algos.algoslib.LibTime

//--scrive messaggi di log sul server wiki
//--log.error in colore rosso (inizio riga)
//--log.warn in colore blu (inizio riga)
//--log.info in colore verde (inizio riga)
class LogWikiService {

    private static String aCapo = '\n'
    private static String space = '&nbsp;'
    private static String iniRiga = '*'
    private static String PAGINA_LOG = 'Utente:Biobot/log'

    public void info(String testo) {
        scrive(testo, LogTipo.info)
    } // fine del metodo

    public void warn(String testo) {
        scrive(testo, LogTipo.warn)
    } // fine del metodo

    public void error(String testo) {
        scrive(testo, LogTipo.error)
    } // fine del metodo


    private static void scrive(String testo, LogTipo logTipo) {
        scriveLogin(null, testo, logTipo)
    } // fine del metodo

    //--for testing purpose only
    public static void scriveLogin(Login login, String testo, LogTipo logTipo) {
        String riga = ''
        String titolo
        String sepA = ' '
        String sepB = ' - '
        String summary = 'operation log'

        titolo = logTipo.toString()

        switch (logTipo) {
            case LogTipo.info:
                titolo += space + space // per allineare con la larghezza del testo di blu e rosso
                titolo = LibHtml.setVerdeBold(titolo)
                break
            case LogTipo.warn:
                titolo = LibHtml.setBluBold(titolo)
                break
            case LogTipo.error:
                titolo = LibHtml.setRossoBold(titolo)
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        riga += iniRiga
        riga += titolo
        riga += sepA
        riga += LibTime.getGioMeseAnnoTime()
        riga += sepB
        riga += testo

        if (login) {
            new EditAdd(login, PAGINA_LOG, riga, summary)
        } else {
            new EditAdd(PAGINA_LOG, riga, summary)
        }// fine del blocco if-else
    } // fine del metodo


} // fine della service classe
