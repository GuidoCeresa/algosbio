import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class AttivitaPrimaJob extends BioCrono {

    //--codifica dell'orario di attivazione
    //--  1,   2,   3,   4,   5,   6,   7
    //--SUN, MON, TUE, WED, THU, FRI, SAT
    private static String attivitaPrimaMetaPrimaSettimana = "0 0 $ORA_INIZIO ? * $GIORNO_ATT#1" //--primo martedi del mese
    private static String attivitaPrimaMetaTerzaSettimana = "0 0 $ORA_INIZIO ? * $GIORNO_ATT#3" //--terzo martedi del mese

    static triggers = {
        cron name: 'attivitaPrimaMetaPrimaSettimana', cronExpression: attivitaPrimaMetaPrimaSettimana
        cron name: 'attivitaPrimaMetaTerzaSettimana', cronExpression: attivitaPrimaMetaTerzaSettimana
    }// fine del metodo statico

    def execute() {
        super.uploadAttivitaPrimaMeta()
    }// fine del metodo execute

} // end of Job Class
