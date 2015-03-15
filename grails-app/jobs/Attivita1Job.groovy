import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class Attivita1Job extends AttNazUpload {

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionAttivita1 = '0 0 7 ? * 5#1' //--primo giovedi del mese alle 7

    static triggers = {
        cron name: 'attivita', cronExpression: cronExpressionAttivita1
    }// fine del metodo statico

    def execute() {
        super.uploadAttivitaPrimaMeta()
    }// fine del metodo execute

} // end of Job Class
