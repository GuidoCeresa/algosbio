import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Elabora la seconda metà delle nazionalità
 */
class Nazionalita2Job {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def listaService
    def statisticheService
    def bioService
    def nazionalitaService
    def mailService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita2 = '0 0 7 ? * 7#2,7#4' //--secondo e quarto sabato del mese

    static triggers = {
        cron name: 'nazionalita2', cronExpression: cronExpressionNazionalita2
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_NAZIONALITA)) {

            if (Pref.getBool(LibBio.USA_LISTE_BIO_NAZIONALITA)) {
                if (bioService) {
                    spedisceMail('Elabora la seconda metà delle nazionalità')
                    nazionalitaService?.uploadNazionalitaSecondaMeta(bioService)
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
