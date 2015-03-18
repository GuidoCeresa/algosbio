import it.algos.algosbio.LibBio
import it.algos.algosbio.StatisticheAttivita
import it.algos.algosbio.StatisticheNazionalita
import it.algos.algoslib.Lib
import it.algos.algoslib.LibGrails
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Elabora metà voci di nazionalità ed attività
 * Spedisce mail di conferma
 */
class BioCrono {

    //--codifica dell'orario di attivazione
    //--  1,   2,   3,   4,   5,   6,   7
    //--SUN, MON, TUE, WED, THU, FRI, SAT
    protected static String GIORNO_NOMI = 'TUE' //martedì
    protected static String GIORNO_ATT = 'THU' //giovedì
    protected static String GIORNO_NAZ = 'SAT' //sabato

    // orario di inizio delle elaborazioni mattutine
    protected static String ORA_INIZIO = '7'

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def attivitaService
    def nazionalitaService
    def bioService
    def mailService
    def professioneService
    def genereService
    def antroponimoService

    protected uploadAttivitaPrimaMeta() {
        uploadaMetaAttivita(true)
    }// fine del metodo execute

    protected uploadAttivitaSecondaMeta() {
        uploadaMetaAttivita(false)
    }// fine del metodo execute

    protected uploadNazionalitaPrimaMeta() {
        uploadaMetaNazionalita(true)
    }// fine del metodo execute

    protected uploadNazionalitaSecondaMeta() {
        uploadaMetaNazionalita(false)
    }// fine del metodo execute

    private uploadaMetaAttivita(boolean primaMeta) {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_NAZIONALITA)) {

            if (Pref.getBool(LibBio.USA_LISTE_BIO_ATTIVITA) && attivitaService) {
                if (bioService) {
                    if (primaMeta) {
                        spedisceMail('Inizio elaborazione della prima metà delle attività')
                        attivitaService.uploadAttivitaPrimaMeta(bioService)
                    } else {
                        spedisceMail('Inizio elaborazione della seconda metà delle attività')
                        attivitaService.uploadAttivitaSecondaMeta(bioService)
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if

            def nonServe = new StatisticheAttivita()

        }// fine del blocco if
    }// fine del metodo execute

    private uploadaMetaNazionalita(boolean primaMeta) {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_NAZIONALITA)) {

            if (Pref.getBool(LibBio.USA_LISTE_BIO_NAZIONALITA) && nazionalitaService) {
                if (bioService) {
                    if (primaMeta) {
                        spedisceMail('Inizio elaborazione della prima metà delle nazionalità')
                        nazionalitaService.uploadNazionalitaPrimaMeta(bioService)
                    } else {
                        spedisceMail('Inizio elaborazione della seconda metà delle nazionalità')
                        nazionalitaService.uploadNazionalitaSecondaMeta(bioService)
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if

            def nonServe = new StatisticheNazionalita()

        }// fine del blocco if
    }// fine del metodo execute

    protected uploadAntroponimi() {
        boolean usaRicalcoloNomi = Pref.getBool(LibBio.USA_RICALCOLO_NOMI, false)

        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_ANTROPONIMI)) {
            //--ricontrolla la lista delle professioni
            if (professioneService) {
                professioneService.download()
            }// fine del blocco if

            //--ricontrolla la lista dei plurali per genere
            if (genereService) {
                genereService.download()
            }// fine del blocco if

            if (usaRicalcoloNomi) {
                if (antroponimoService) {
                    antroponimoService.ricalcola()
                }// fine del blocco if
            }// fine del blocco if

            if (Pref.getBool(LibBio.USA_LISTE_BIO_NOMI)) {
                if (antroponimoService && bioService) {
                    spedisceMail('Inizio elaborazione dei nomi (antroponimi)')
                    antroponimoService.uploadAllNomi(bioService)
                }// fine del blocco if
            } else {
                //--costruisce una lista di nomi (circa 1.200)
                if (antroponimoService) {
                    antroponimoService.upload()
                }// fine del blocco if
            }// fine del blocco if-else

            if (antroponimoService) {
                antroponimoService.creaPagineControllo()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo execute

    private spedisceMail(String testo) {
        boolean status = Pref.getBool(LibBio.USA_MAIL_INFO)
        String oggetto
        String adesso = new Date().toString()
        String mailTo = 'gac@algos.it'
        String time = 'Report eseguito alle ' + adesso + '\n'
        testo = time + testo
        oggetto = 'Nazionalità'

        if (mailService && status) {
            mailService.sendMail {
                to mailTo
                subject oggetto
                body testo
            }// fine della closure
        }// fine del blocco if

    }// fine del metodo

} // end of Job Class
