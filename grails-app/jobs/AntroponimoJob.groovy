import it.algos.algoslib.Lib
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref
import it.algos.algospref.Preferenze
import it.algos.algosbio.LibBio

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 31-1-14
 * Time: 20:45
 */
class AntroponimoJob extends BioCrono {

    //--codifica dell'orario di attivazione
    //--  1,   2,   3,   4,   5,   6,   7
    //--SUN, MON, TUE, WED, THU, FRI, SAT
    private static String antroponimiAllSettimane = "0 0 $ORA_INIZIO ? * $GIORNO_NOMI" //--tutti i martedi

    static triggers = {
        cron name: 'antroponimiAllSettimane', cronExpression: antroponimiAllSettimane
    }// fine del metodo statico

    def execute() {
        super.uploadAntroponimi()
    }// fine del metodo execute

} // end of Job Class
