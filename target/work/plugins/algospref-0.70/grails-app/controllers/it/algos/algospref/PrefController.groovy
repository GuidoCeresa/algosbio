package it.algos.algospref

import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

class PrefController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 20, 100)
        respond Pref.list(params), model: [prefInstanceCount: Pref.count()]
    } // fine del metodo

    def show(Pref prefInstance) {
        respond prefInstance
    } // fine del metodo

    def create() {
        params.ordine = LibPref.getNewOrdine()
        respond new Pref(params)
    } // fine del metodo

    def save(Pref prefInstance) {
        if (prefInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (prefInstance.hasErrors()) {
            respond prefInstance.errors, view: 'create'
            return
        }// fine del blocco if

        //--controllo specifico di questo controller
        if (nuovaInstanzaVuota(prefInstance)) {
            redirect action: 'index', method: "GET"
            return
        }// fine del blocco if

        prefInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pluto.label', default: 'Pref'), prefInstance.id])
                redirect prefInstance
            }
            '*' { respond prefInstance, [status: CREATED] }
        }// fine del blocco request
    } // fine del metodo


    def edit(Pref prefInstance) {
        respond prefInstance
    } // fine del metodo

    def update(Pref prefInstance) {
        if (prefInstance == null) {
            notFound()
            return
        }// fine del blocco if

        if (prefInstance.hasErrors()) {
            respond prefInstance.errors, view: 'edit'
            return
        }// fine del blocco if

        //--controllo specifico di questo controller
        if (modificaInstanzaVuota(prefInstance)) {
            redirect action: 'index', method: "GET"
            return
        }// fine del blocco if

        prefInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'pluto.label', default: 'Pref'), prefInstance.id])
                redirect prefInstance
            }
            '*' { respond prefInstance, [status: CREATED] }
        }// fine del blocco request
    } // fine del metodo

    def delete(Pref prefInstance) {
        if (prefInstance == null) {
            notFound()
            return
        }// fine del blocco if

        prefInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Pluto.label', default: 'Pref'), prefInstance.id])
                redirect action: 'index', method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }// fine del blocco request
    } // fine del metodo

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'pluto.label', default: 'Pluto'), params.id])
                redirect action: 'index', method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    } // fine del metodo

    def nuovaInstanzaVuota(Pref prefInstance) {
        boolean vuota = true
        boolean vuotaTypo
        boolean vuotaAll = true
        String erroreType = 'La preferenza non è stata creata perché mancava il valore corrispondente al tipo selezionato'
        String erroreAll = 'La preferenza non è stata creata perché non aveva nessun valore'

        // prima controlla se esiste il valore previsto
        vuotaTypo = valorePrevistoVuoto(prefInstance)

        // adesso controlla se esistono altri valori
        if (vuotaTypo) {
            vuotaAll = tuttiValoriVuoti(prefInstance)
        } else {
            vuota = false
        }// fine del blocco if-else

        if (vuota) {
            if (vuotaAll) {
                flash.error = erroreAll
            } else {
                if (vuotaTypo) {
                    flash.error = erroreType
                } else {
                    flash.error = 'Mi sono incasinato'
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if

        return vuota
    } // fine del metodo

    def modificaInstanzaVuota(Pref prefInstance) {
        boolean vuotaTypo = true
        String erroreType = 'La preferenza non è stata modificata perché mancava il valore corrispondente al tipo selezionato'

        // prima controlla se esiste il valore previsto
        vuotaTypo = valorePrevistoVuoto(prefInstance)

        // adesso controlla se esistono altri valori
        if (vuotaTypo) {
            flash.error = erroreType
        }// fine del blocco if

        return vuotaTypo
    } // fine del metodo

    // controlla se esiste il valore previsto
    def valorePrevistoVuoto(Pref prefInstance) {
        boolean vuotaTypo = true
        Type type = null

        if (prefInstance) {
            type = prefInstance.type
        }// fine del blocco if

        if (type) {
            switch (type) {
                case Type.stringa:
                    if (prefInstance.stringa) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.testo:
                    if (prefInstance.testo) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.booleano:
                    vuotaTypo = false
                    break
                case Type.intero:
                    if (prefInstance.intero instanceof Integer) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.lungo:
                    if (prefInstance.lungo instanceof Long) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.reale:
                    if (prefInstance.reale instanceof Float) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.doppio:
                    if (prefInstance.doppio instanceof Double) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.decimale:
                    if (prefInstance.decimale instanceof BigDecimal) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                case Type.data:
                    if (prefInstance.data) {
                        vuotaTypo = false
                    }// fine del blocco if
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch
        }// fine del blocco if

        return vuotaTypo
    } // fine del metodo

    // controlla se esistono altri valori
    def tuttiValoriVuoti(Pref prefInstance) {
        boolean vuotaAll = true

        if (prefInstance) {
            if (prefInstance.stringa) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.testo) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.bool) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.intero) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.lungo) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.reale) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.doppio) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.decimale) {
                vuotaAll = false
            }// fine del blocco if
            if (prefInstance.data) {
                vuotaAll = false
            }// fine del blocco if
        }// fine del blocco if

        return vuotaAll
    } // fine del metodo

} // fine della controller classe
