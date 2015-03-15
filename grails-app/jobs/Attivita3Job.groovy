import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class Attivita3Job extends AttNazUpload {

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionAttivita3 = '0 0 7 ? * 5#3' //--terzo giovedi del mese alle 7

    static triggers = {
        cron name: 'attivita', cronExpression: cronExpressionAttivita3
    }// fine del metodo statico

    def execute() {
        super.uploadAttivitaPrimaMeta()
    }// fine del metodo execute

} // end of Job Class
