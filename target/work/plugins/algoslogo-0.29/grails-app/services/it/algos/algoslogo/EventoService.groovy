package it.algos.algoslogo

class EventoService {

    //--codifica degli eventi
    public static String GENERICO = 'Generico'
    public static String NUOVO = 'New'
    public static String MODIFICA = 'Edit'
    public static String CANCELLA = 'Delete'
    public static String SETUP = 'SetupIniziale'

    //--recupera l'evento gwenerico
    public Evento getGenerico() {
        return Evento.findByNome(GENERICO)
    } // fine del metodo

    //--recupera l'evento nuovo
    public Evento getNuovo() {
        return Evento.findByNome(NUOVO)
    } // fine del metodo

    //--recupera l'evento modifica
    public Evento getModifica() {
        return Evento.findByNome(MODIFICA)
    } // fine del metodo

    //--recupera l'evento cancellazione
    public Evento getCancella() {
        return Evento.findByNome(CANCELLA)
    } // fine del metodo

} // fine della service classe
