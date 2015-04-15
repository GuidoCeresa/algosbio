import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 15/08/14.
 */
class NazionalitaJob extends BioCrono {

    //--codifica dell'orario di attivazione
    //--  1,   2,   3,   4,   5,   6,   7
    //--SUN, MON, TUE, WED, THU, FRI, SAT
    private static String nazionalitaAllSettimane = "0 0 $ORA_INIZIO ? * $GIORNO_NAZ" //--ogni giovedi del mese

    static triggers = {
        cron name: 'nazionalitaAllSettimane', cronExpression: nazionalitaAllSettimane
    }// fine del metodo statico

    def execute() {
        super.uploadNazionalita()
    }// fine del metodo execute

} // end of Job Class
