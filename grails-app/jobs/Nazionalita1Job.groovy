import it.algos.algosbio.LibBio
import it.algos.algoslib.LibGrails
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Elabora la prima metà delle nazionalità
 */
class Nazionalita1Job extends NazMail{

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionNazionalita1 = '0 0 7 ? * 7#1' //--primo sabato del mese

    static triggers = {
        cron name: 'nazionalita1', cronExpression: cronExpressionNazionalita1
    }// fine del metodo statico

    def execute() {
        super.uploadNazionalitaPrimaMeta()
    }// fine del metodo execute

} // end of Job Class
