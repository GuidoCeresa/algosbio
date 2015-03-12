import it.algos.algosbio.LibBio
import it.algos.algoslib.LibGrails
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Elabora la prima metà delle nazionalità
 */
class Nazionalita1Job {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def listaService
    def statisticheService
    def bioService
    def nazionalitaService
    def mailService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita1 = '0 0 7 ? * 7#1,7#3' //--primo e terzo sabato del mese

    static triggers = {
//        cron name: 'nazionalita1', cronExpression: cronExpressionNazionalita1
        simple startDelay: 1000 * 60, repeatInterval: 1000 * 60 * 30
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_NAZIONALITA)) {

            if (Pref.getBool(LibBio.USA_LISTE_BIO_NAZIONALITA)) {
                if (bioService) {
                    spedisceMail('Elabora la prima metà delle nazionalità')
                    nazionalitaService?.uploadNazionalitaPrimaMeta(bioService)
                }// fine del blocco if
            }// fine del blocco if

            if (statisticheService) {
                statisticheService.nazionalitaUsate()
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
