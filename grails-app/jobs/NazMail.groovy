import it.algos.algosbio.LibBio
import it.algos.algoslib.LibGrails
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Elabora la prima metà delle nazionalità
 */
class NazMail {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def statisticheService
    def bioService
    def nazionalitaService
    def mailService

    protected uploadNazionalitaPrimaMeta() {
        uploadaMeta(true)
    }// fine del metodo execute

    protected uploadNazionalitaSecondaMeta() {
        uploadaMeta(false)
    }// fine del metodo execute

    private uploadaMeta(boolean primaMeta) {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_NAZIONALITA)) {

            if (Pref.getBool(LibBio.USA_LISTE_BIO_NAZIONALITA)) {
                if (bioService) {
                    if (primaMeta) {
                        spedisceMail('Elabora la prima metà delle nazionalità')
                        nazionalitaService?.uploadNazionalitaPrimaMeta(bioService)
                    } else {
                        spedisceMail('Elabora la seconda metà delle nazionalità')
                        nazionalitaService?.uploadNazionalitaSecondaMeta(bioService)
                    }// fine del blocco if-else
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
