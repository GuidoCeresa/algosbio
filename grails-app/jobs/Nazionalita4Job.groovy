import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Elabora la seconda metà delle nazionalità
 */
class Nazionalita4Job extends NazMail {

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita4 = '0 0 7 ? * 7#4' //--quarto sabato del mese

    static triggers = {
        cron name: 'nazionalita4', cronExpression: cronExpressionNazionalita4
    }// fine del metodo statico

    def execute() {
        super.uploadNazionalitaSecondaMeta()
    }// fine del metodo execute

} // end of Job Class
