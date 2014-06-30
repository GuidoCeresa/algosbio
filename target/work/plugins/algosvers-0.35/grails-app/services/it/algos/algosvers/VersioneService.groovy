package it.algos.algosvers

class VersioneService {

    //--controlla la versione installata
    public boolean installaVersione(int numeroVersioneDaInstallare) {
        boolean installa = false
        int numeroVersioneEsistente = getVersione()

        if (numeroVersioneDaInstallare > numeroVersioneEsistente) {
            installa = true
        }// fine del blocco if

        return installa
    }// fine del metodo

    private static int getVersione() {
        int numero = 0
        def lista

        lista = Versione.findAll("from Versione order by numero desc")
        if (lista && lista.size() > 0) {
            numero = lista[0].numero
        }// fine del blocco if

        return numero
    }// fine del metodo

    //--crea una versione
    //--la crea SOLO se non esiste già
    public void newVersione(String titolo, String descrizione) {
        Versione versione
        int numero = getVersione() + 1
        Date giorno = new Date()

        versione = new Versione()
        versione.numero = numero
        versione.giorno = giorno
        versione.titolo = titolo
        versione.descrizione = descrizione
        versione.save(flush: true)
    }// fine del metodo

} // fine della service classe
