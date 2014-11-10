package it.algos.algosbio

/**
 * Created by gac on 03/11/14.
 */
class LibPass {


    public static String codifica(String testoInChiaro) {
        String testoCodificato = ''

        for (int i = 0; i < testoInChiaro.length(); i++) {
            char c = testoInChiaro.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;

            testoCodificato += c
        }// fine del blocco for

        return testoCodificato
    }// fine del metodo

}// fine della classe
