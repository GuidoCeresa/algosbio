import it.algos.algospref.Pref
import it.algos.algosbio.LibBio

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 18-11-13
 * Time: 17:14
 */
class ElaboraJob {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def bioService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    //--sabato e domenica dalle 10 alle 11 di sera
    private static String cronExpressionElabora = "0 0 10-23 ? * SAT-SUN"

    static triggers = {
        cron name: 'elabora', cronExpression: cronExpressionElabora
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_ELABORA)) {
            if (bioService) {
                bioService.elabora()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo execute

} // end of Job Class
