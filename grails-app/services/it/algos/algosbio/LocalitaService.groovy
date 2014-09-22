package it.algos.algosbio

import grails.transaction.Transactional

@Transactional
class LocalitaService {

    //--spazzola ed elabora n voci come da preferenza
    def elabora() {

    } // fine del metodo

    /**
     * Ritorna la località dal link alla voce
     * Se non esiste, la crea
     */
    public static Localita getLuogoNascita(BioWiki bioWiki) {
        return getLocalita(bioWiki.luogoNascita, bioWiki.luogoNascitaLink, NatoMorto.nato)
    } // fine del metodo

    public static Localita getLuogoMorte(BioWiki bioWiki) {
        return getLocalita(bioWiki.luogoMorte, bioWiki.luogoMorteLink, NatoMorto.morto)
    } // fine del metodo

    /**
     * Ritorna la località dal link alla voce
     * Se non esiste, la crea
     */
    private  static Localita getLocalita(String luogo, String luogoLink, NatoMorto natoMorto) {
        Localita localita = null

        if (luogo || luogoLink) {
            if (luogo) {
                luogo = luogo.trim()
            }// fine del blocco if
            if (luogoLink) {
                luogoLink = luogoLink.trim()
            } else {
                luogoLink = luogo
            }// fine del blocco if-else

            localita = Localita.findByNome(luogoLink)
            if (!localita) {
                localita = new Localita(nome: luogoLink)
            }// fine del blocco if

            if (localita) {
                switch (natoMorto) {
                    case NatoMorto.nato:
                        localita.nati = localita.nati + 1
                        break
                    case NatoMorto.morto:
                        localita.morti = localita.morti + 1
                        break
                    default: // caso non definito
                        break
                } // fine del blocco switch

                try { // prova ad eseguire il codice
                    localita.save(flush: true)
                } catch (Exception unErrore) { // intercetta l'errore
                   def a= unErrore
                }// fine del blocco try-catch
            }// fine del blocco if
        }// fine del blocco if

        return localita
    } // fine del metodo

    private static enum NatoMorto {
        nato, morto
    } // fine della Enumeration
} // fine della service classe
