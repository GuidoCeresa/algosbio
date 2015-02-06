package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 15-11-13
 * Time: 08:22
 */
class EditBio extends Edit {

    private boolean paginaDaRegistrare

    //--usa il titolo della pagina
    public EditBio(String titolo, String testoNew, String summary) {
        super(titolo, testoNew, summary)
    }// fine del metodo costruttore

    //--recupera il testo ricevuto dalla prima Request
    //--controlla che esistano modifiche sostanziali (non solo la data)
    protected void regolaTesto() {
        paginaDaRegistrare = true
        boolean registraSoloModificheSostanziali = Pref.getBool(LibBio.REGISTRA_SOLO_MODIFICHE_SOSTANZIALI)
        String testoOld = super.getTestoPrimaRequest()
        String testoNew = super.getTestoNew()
        String testoOldSignificativo = ''
        String testoNewSignificativo
        String tagIniA = 'ListaBio'
        String tagIniB = 'StatBio'
        String tagEnd = '}}'

        if (registraSoloModificheSostanziali) {
            if (testoOld) {
                testoOldSignificativo = LibTesto.testoSuccessivo(testoOld, tagEnd, tagIniA, tagIniB)
            }// fine del blocco if
            if (testoNew) {
                testoNewSignificativo = LibTesto.testoSuccessivo(testoNew, tagEnd, tagIniA, tagIniB)
            }// fine del blocco if
            if (testoOldSignificativo && testoNewSignificativo) {
                if (testoNewSignificativo.equals(testoOldSignificativo)) {
                    paginaDaRegistrare = false
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    //--controllo prima di eseguire la seconda request
    protected boolean eseguiSecondaRequest() {
        return paginaDaRegistrare
    } // fine del metodo

} // fine della classe