import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class AttivitaSecondaJob extends BioCrono {

    //--codifica dell'orario di attivazione
    //--  1,   2,   3,   4,   5,   6,   7
    //--SUN, MON, TUE, WED, THU, FRI, SAT
    private static String attivitaSecondaMetaSecondaSettimana = "0 0 $ORA_INIZIO ? * $GIORNO_ATT#2" //--secondo martedi del mese
    private static String attivitaSecondaMetaQuartaSettimana = "0 0 $ORA_INIZIO ? * $GIORNO_ATT#4" //--quarto martedi del mese

    static triggers = {
        cron name: 'attivitaSecondaMetaSecondaSettimana', cronExpression: attivitaSecondaMetaSecondaSettimana
        cron name: 'attivitaSecondaMetaQuartaSettimana', cronExpression: attivitaSecondaMetaQuartaSettimana
    }// fine del metodo statico

    def execute() {
        super.uploadAttivitaSecondaMeta()
    }// fine del metodo execute

} // end of Job Class
