import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 02/07/14.
 */
class UploadJob {
    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def statisticheService
    def bioService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionUpload = "0 0 7 ? * MON,WED,FRI" //--alle 7 lunedì, mercoledì e venerdì

    static triggers = {
        cron name: 'upload', cronExpression: cronExpressionUpload
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_UPLOAD)) {
            if (statisticheService) {
                statisticheService.uploadAll()
                statisticheService.paginaSintesi()
            }// fine del blocco if
        }// fine del blocco if

    }// fine del metodo execute

} // end of Job Class

