import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class Attivita2Job extends AttNazUpload {

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionAttivita2 = '0 0 7 ? * 5#2' //--secondo giovedi del mese alle 7

    static triggers = {
        cron name: 'attivita', cronExpression: cronExpressionAttivita2
    }// fine del metodo statico

    def execute() {
        super.uploadAttivitaSecondaMeta()
    }// fine del metodo execute

} // end of Job Class
