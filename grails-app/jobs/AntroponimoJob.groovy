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
class AntroponimoJob {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def professioneService
    def genereService
    def antroponimoService
    def bioService

    //--codifica dell'orario di attivazione
    //--MON, TUE, WED, THU, FRI, SAT, SUN
    private static String cronExpressionAntroponimo = "0 0 7 ? * TUE" //--tutti i martedi alle 7

    static triggers = {
        cron name: 'antroponimo', cronExpression: cronExpressionAntroponimo
    }// fine del metodo statico

    def execute() {
        ArrayList<String> listaNomi = null
        int dimBlocco = Pref.getInt(LibBio.CICLO_ANTROPONIMI, 10)
        ArrayList<String> listaBlocchiNomi
        int numVoci
        String numVociTxt
        long inizioInizio = System.currentTimeMillis()
        long inizio = 0
        long fine = 0
        long durata = 0
        long durataTotale = 0
        String tempoTxt
        String tempoTotaleTxt
        int cont = 0
        String vociCreateTxt = ''
        int vociCreate = 0

        //--flag di attivazione
        if (Pref.getBool(LibBio.USA_CRONO_ANTROPONIMI)) {
            //--ricontrolla la lista delle professioni
            if (professioneService) {
                professioneService.download()
            }// fine del blocco if

            //--ricontrolla la lista dei plurali per genere
            if (genereService) {
                genereService.download()
            }// fine del blocco if

            if (Pref.getBool(LibBio.USA_LISTE_BIO_NOMI)) {
                if (antroponimoService && bioService) {
                    antroponimoService.uploadAllNomi(bioService)
                }// fine del blocco if
            } else {
                //--costruisce una lista di nomi (circa 1.200)
                if (antroponimoService) {
                    listaNomi = antroponimoService.getListaNomi()
                }// fine del blocco if

                //--crea le pagine dei singoli nomi a blocchi
                if (listaNomi) {
                    inizio = inizioInizio
                    numVoci = listaNomi.size()
                    numVociTxt = LibTesto.formatNum(numVoci)
                    log.info "Inizio del metodo di creazione di ${numVociTxt} voci di Antroponimi"
                    listaBlocchiNomi = Lib.Array.splitArray(listaNomi, dimBlocco)
                    listaBlocchiNomi?.each {
                        cont++
                        antroponimoService.elabora((ArrayList) it)
                        fine = System.currentTimeMillis()
                        durataTotale = fine - inizioInizio
                        durata = fine - inizio
                        durataTotale = durataTotale / 1000
                        durata = durata / 1000
                        tempoTotaleTxt = LibTesto.formatNum(durataTotale)
                        tempoTxt = LibTesto.formatNum(durata)
                        vociCreate = cont * dimBlocco
                        vociCreateTxt = LibTesto.formatNum(vociCreate)
                        log.info "Aggiornate ${vociCreateTxt}/${numVociTxt} voci di Antroponimi in ${tempoTxt}/${tempoTotaleTxt} secondi"
                        inizio = fine
                    }// fine del ciclo each
                }// fine del blocco if
            }// fine del blocco if-else

            if (antroponimoService && listaNomi) {
                antroponimoService.creaPagineControllo()
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo execute

} // end of Job Class
