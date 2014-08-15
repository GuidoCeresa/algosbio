import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class AttivitaJob {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def listaService
    def statisticheService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionAttivita =  '0 0 8 ? * THU' //--tutti i giovedi alle 8

    static triggers = {
        cron name: 'attivita', cronExpression: cronExpressionAttivita
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_ATTIVITA)) {
            if (listaService) {
                listaService.uploadAttivita()
            }// fine del blocco if
            if (statisticheService) {
                statisticheService.attivitaUsate()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo execute

} // end of Job Class
