package it.algos.algoslogo

import java.sql.Timestamp

class LogoService {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def eventoService

    //--registra una info senza utente
    public String setInfo(def request, Evento evento) {
        return setBase(request, evento, '', '', Livello.info, '')
    }// fine del metodo

    //--registra una info senza ruolo
    public String setInfo(def request, Evento evento, String utente) {
        return setBase(request, evento, utente, '', Livello.info, '')
    }// fine del metodo

    //--registra una info senza dettaglio
    public String setInfo(def request, Evento evento, String utente, String ruolo) {
        return setBase(request, evento, utente, ruolo, Livello.info, '')
    }// fine del metodo

    //--registra una info con dettaglio
    public String setInfo(def request, Evento evento, String utente, String ruolo, String dettaglio) {
        return setBase(request, evento, utente, ruolo, Livello.info, dettaglio)
    }// fine del metodo

    //--registra un'avviso solo col dettaglio
    public String setWarn(String dettaglio) {
        return setBase(null, null, '', '', Livello.warn, dettaglio)
    }// fine del metodo

    //--registra un'avviso senza request e senza utente
    public String setWarn(Evento evento) {
        return setBase(null, evento, '', '', Livello.warn, '')
    }// fine del metodo

    //--registra un'avviso senza request e senza utente ma col dettaglio
    public String setWarn(Evento evento, String dettaglio) {
        return setBase(null, evento, '', '', Livello.warn, dettaglio)
    }// fine del metodo

    //--registra un'avviso senza utente
    public String setWarn(def request, Evento evento) {
        return setBase(request, evento, '', '', Livello.warn, '')
    }// fine del metodo

    //--registra un'avviso senza ruolo
    public String setWarn(def request, Evento evento, String utente) {
        return setBase(request, evento, utente, '', Livello.warn, '')
    }// fine del metodo

    //--registra un'avviso senza dettaglio
    public String setWarn(def request, Evento evento, String utente, String ruolo) {
        return setBase(request, evento, utente, ruolo, Livello.warn, '')
    }// fine del metodo

    //--registra un'avviso con dettaglio
    public String setWarn(def request, Evento evento, String utente, String ruolo, String dettaglio) {
        return setBase(request, evento, utente, ruolo, Livello.warn, dettaglio)
    }// fine del metodo

    //--registra un evento generico (molto generico, senza dettaglio)
    public String setBase(def request, Evento evento, String utente, String ruolo, Livello livello) {
        return setBase(request, evento, utente, ruolo, livello, '')
    }// fine del metodo

    //--registra un evento generico
    public String setBase(def request, Evento evento, String utente, String ruolo, Livello livello, String dettaglio) {
        String testoFlash = ''
        Logo logo = new Logo()
        Timestamp time

        logo.time = new Date().toTimestamp()
        if (evento) {
            logo.evento = evento
        } else {
            if (eventoService) {
                logo.evento = eventoService.getGenerico()
            } else {
                logo.evento = null
            }// fine del blocco if-else
        }// fine del blocco if-else
        logo.utente = utente
        logo.ruolo = ruolo
        logo.livello = livello
        logo.dettaglio = dettaglio
        if (request) {
            logo.ip = request.getRemoteAddr()
        }// fine del blocco if

        logo.save(flush: true)

        //       postaService.sendLogoMail(logo)

        return testoFlash
    }// fine del metodo

} // fine della service classe
