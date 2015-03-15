import it.algos.algosbio.LibBio
import it.algos.algosbio.StatisticheAttivita
import it.algos.algosbio.StatisticheNazionalita
import it.algos.algoslib.LibGrails
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Elabora metà voci di nazionalità ed attività
 * Spedisce mail di conferma
 */
class AttNazUpload {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def attivitaService
    def nazionalitaService
    def bioService
    def mailService

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
                        spedisceMail('Elabora la prima metà delle attività')
                        attivitaService.uploadAttivitaPrimaMeta(bioService)
                    } else {
                        spedisceMail('Elabora la seconda metà delle attività')
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
                        spedisceMail('Elabora la prima metà delle nazionalità')
                        nazionalitaService.uploadNazionalitaPrimaMeta(bioService)
                    } else {
                        spedisceMail('Elabora la seconda metà delle nazionalità')
                        nazionalitaService.uploadNazionalitaSecondaMeta(bioService)
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if

            def nonServe = new StatisticheNazionalita()

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
