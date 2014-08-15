import it.algos.algosbio.LibBio
import it.algos.algospref.Pref

/**
 * Created by gac on 02/07/14.
 */
class UploadJob {
    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def bioGrailsService
    def statisticheService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
//    private static String cronExpressionUpload = "0 0 7 ? * MON-FRI"   //--tutti i giorni alle 7, sabato e domenica esclusi
    private static String cronExpressionUpload = "0 0 8 ? * MON,WED,FRI" //--alle 8 lunedì, mercoledì e venerdì

    static triggers = {
        cron name: 'upload', cronExpression: cronExpressionUpload
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_UPLOAD)) {
            if (bioGrailsService) {
                bioGrailsService.uploadAll()
            }// fine del blocco if

            if (statisticheService) {
                statisticheService.paginaSintesi()
            }// fine del blocco if
        }// fine del blocco if

    }// fine del metodo execute

} // end of Job Class

