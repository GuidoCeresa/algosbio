import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 31-1-14
 * Time: 20:45
 */
class NazionalitaJob {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def listaService
    def statisticheService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita =  '0 0 8 ? * SAT' //--tutti i sabati alle 8

    static triggers = {
        cron name: 'nazionalita', cronExpression: cronExpressionNazionalita
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_NAZIONALITA)) {
            if (listaService) {
                listaService.uploadNazionalita()
            }// fine del blocco if
            if (statisticheService) {
                statisticheService.nazionalitaUsate()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo execute

} // end of Job Class
