import grails.test.mixin.Mock
import it.algos.algospref.Pref
import it.algos.algospref.Preferenze
import it.algos.algosbio.LibBio
import it.algos.algospref.Type
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 31-1-14
 * Time: 20:45
 */
@Mock(Pref)
class AttivitaJob {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def listaService
    def statisticheService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionAttivita =  '0 0 8 ? * THU' //--tutti i giovedi alle 8

    static triggers = {
        cron name: 'attivita', cronExpression: cronExpressionAttivita
    }// fine del metodo statico

    def execute() {
        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_ATTIVITA)) {
            if (listaService) {
                listaService.uploadAttivita()
            }// fine del blocco if
            if (statisticheService) {
                statisticheService.attivitaUsate()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo execute

} // end of Job Class
