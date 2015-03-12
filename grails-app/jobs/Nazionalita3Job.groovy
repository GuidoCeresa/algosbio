import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Elabora la seconda metà delle nazionalità
 */
class Nazionalita3Job extends  NazMail{

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita3 = '0 0 7 ? * 7#3' //--terzo sabato del mese

    static triggers = {
        cron name: 'nazionalita3', cronExpression: cronExpressionNazionalita3
    }// fine del metodo statico

    def execute() {
        super.uploadNazionalitaPrimaMeta()
    }// fine del metodo execute

} // end of Job Class
