import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Elabora la seconda metà delle nazionalità
 */
class Nazionalita2Job extends NazMail{

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita2 = '0 0 7 ? * 7#2' //--secondo sabato del mese

    static triggers = {
        cron name: 'nazionalita2', cronExpression: cronExpressionNazionalita2
    }// fine del metodo statico

    def execute() {
        super.uploadNazionalitaSecondaMeta()
    }// fine del metodo execute

} // end of Job Class
