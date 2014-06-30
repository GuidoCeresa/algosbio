/* Created by Algos s.r.l. */
/* Date: mag 2013 */
/* Il plugin Algos ha creato o sovrascritto il templates che ha creato questo file. */
/* L'header del templates serve per controllare le successive release */
/* (tramite il flag di controllo aggiunto) */
/* Tipicamente VERRA sovrascritto (il template, non il file) ad ogni nuova release */
/* del plugin per rimanere aggiornato. */
/* Se vuoi che le prossime release del plugin NON sovrascrivano il template che */
/* genera questo file, perdendo tutte le modifiche precedentemente effettuate, */
/* regola a false il flag di controllo flagOverwrite© del template stesso. */
/* (non quello del singolo file) */
/* flagOverwrite = true */

package it.algos.algos

import it.algos.algoslib.*
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

import java.sql.Timestamp

class AlgosTagLib {

    static namespace = 'algos'
    def grailsApplication

    private static String aCapo = '\n'

    //--disegna i menu extra aggiuntivi a quelli standard dei templates
    //--stringa solo azione e di default controller=questo; classe e titolo vengono uguali
    //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
    def menuExtra = { args ->
        String testoOut = ''
        ArrayList lista = null
        def elementoLista
        String app = LibGrails.getAppName(grailsApplication)
        String contQuesto
        String cont
        String action
        String icon
        String title

        if (args.menuExtra) {
            lista = args.menuExtra
        }// fine del blocco if
        if (params.controller) {
            contQuesto = params.controller
        }// fine del blocco if

        lista?.each {
            elementoLista = it
            action = ''
            if (elementoLista instanceof String) {
                if (elementoLista) {
                    cont = contQuesto
                    action = elementoLista
                    icon = action
                    title = action
                    title = LibTesto.primaMaiuscola(title)
                }// fine del blocco if
            } else {
                if (elementoLista instanceof Map && elementoLista.action) {
                    cont = contQuesto
                    if (elementoLista.cont) {
                        cont = elementoLista.cont
                    }// fine del blocco if
                    action = ''
                    if (elementoLista.action) {
                        action = elementoLista.action
                    }// fine del blocco if
                    icon = action
                    if (elementoLista.icon) {
                        icon = elementoLista.icon
                    }// fine del blocco if
                    title = action
                    title = LibTesto.primaMaiuscola(title)
                    if (elementoLista.title) {
                        title = elementoLista.title
                    }// fine del blocco if
                } else { // emergency error
                    cont = contQuesto
                    action = 'list'
                    icon = action
                    title = 'error'
                }// fine del blocco if-else
            }// fine del blocco if-else
            if (action) {
                testoOut += LibHtml.getRigaMenu(app, cont, action, icon, title)
            }// fine del blocco if
        } // fine del ciclo each

        out << testoOut
    }// fine della closure

    def listaControllers = {
    }// fine della closure

    //--disegna i titoli delle colonne della tavola/lista
    def titoliLista = { args ->
        String testoOut = ''
        String appName = ''
        Class clazz
        ArrayList lista = null
        def campi
        String cont
        String oldSort = 'id'
        String sort = ''
        String order = 'asc'
        String title = 'void'
        String campo
        def elementoLista

        if (args.campiLista) {
            lista = args.campiLista
        }// fine del blocco if
        if (params.controller) {
            cont = params.controller
        }// fine del blocco if
        if (params.sort) {
            oldSort = params.sort
        }// fine del blocco if
        if (params.order) {
            order = params.order
        }// fine del blocco if

        if (cont) {
            appName = LibGrails.getAppName(grailsApplication)
            if (lista) {
                lista?.each {
                    elementoLista = it
                    if (elementoLista instanceof String) {
                        campo = it
                        sort = campo
                        title = message(code: "${cont}.${campo}.labellist", default: "${campo}")
                        title = LibTesto.primaMaiuscola(title)
                    } else {
                        if (elementoLista instanceof Map && elementoLista.title && elementoLista.campo) {
                            campo = elementoLista.campo
                            if (elementoLista.sort) {
                                sort = elementoLista.sort
                            } else {
                                sort = campo
                            }// fine del blocco if-else
                            title = elementoLista.title
                        } else { // emergency error
                            campo = 'it'
                            sort = campo
                            title = elementoLista.campo
                            title = LibTesto.primaMaiuscola(title)
                        }// fine del blocco if-else
                    }// fine del blocco if-else

                    if (sort.equals(oldSort)) {
                        testoOut += Lib.Html.getTitoloTabellaNotSorted(appName, cont, sort, order, title)
                    } else {
                        testoOut += Lib.Html.getTitoloTabellaSorted(appName, cont, sort, order, title)
                    }// fine del blocco if-else
                } // fine del ciclo each
            } else {
                clazz = LibGrails.getDomainClazz(grailsApplication, cont)
                if (clazz) {
                    campi = new DefaultGrailsDomainClass(clazz).persistentProperties*.name
                    campi?.each {
                        campo = it
                        sort = campo
                        campo = LibTesto.primaMaiuscola(campo)
                        title = message(code: "${cont}.${campo}.labellist", default: "${campo}")
                        if (sort.equals(oldSort)) {
                            testoOut += Lib.Html.getTitoloTabellaNotSorted(appName, cont, sort, order, title)
                        } else {
                            testoOut += Lib.Html.getTitoloTabellaSorted(appName, cont, sort, order, title)
                        }// fine del blocco if-else
                    } // fine del ciclo each
                }// fine del blocco if
            }// fine del blocco if-else
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    //--disegna tutti i campi di una riga della tavola/lista
    //--disegna eventuali campi extra per le funzioni dei militi di una croce
    def rigaLista = { args ->
        String testoOut = ''
        String appName = ''
        String contPath
        Class clazz
        ArrayList lista = null
        ArrayList listaExtra = null
        def bean = null
        def campi
        String cont
        String campo = ''
        def rec = null
        long id = 0.0
        def value = null
        def elementoLista

        if (args.campiLista) {
            lista = args.campiLista
        }// fine del blocco if
        if (params.controller) {
            cont = params.controller
        }// fine del blocco if
        if (args.rec) {
            rec = args.rec
        }// fine del blocco if
        try { // prova ad eseguire il codice
            if (rec && rec.id) {
                id = rec.id
            }// fine del blocco if
        } catch (Exception unErrore) { // intercetta l'errore
            log.error unErrore
        }// fine del blocco try-catch
        if (args.campiExtra) {
            listaExtra = args.campiExtra
        }// fine del blocco if

        if (cont && rec) {
            appName = LibGrails.getAppName(grailsApplication)
            if (lista) {
                lista?.each {
                    elementoLista = it
                    if (elementoLista instanceof String) {
                        campo = it
                    } else {
                        if (elementoLista instanceof Map && elementoLista.campo) {
                            campo = elementoLista.campo
                        }// fine del blocco if
                    }// fine del blocco if-else
                    try { // prova ad eseguire il codice
                        if (rec."${campo}") {
                            value = rec."${campo}"
                        } else {
                            if (rec."${campo}" instanceof Boolean) {
                                value = rec."${campo}"
                            } else {
                                value = null
                            }// fine del blocco if-else
                        }// fine del blocco if-else
                    } catch (Exception unErrore) { // intercetta l'errore
                        value = null
                    }// fine del blocco try-catch
                    testoOut += getCampoTabella(appName, cont, id, value)
                } // fine del ciclo each
            } else {
                contPath = appName + '.' + LibTesto.primaMaiuscola(cont)
                bean = applicationContext.getBean(contPath)
                if (bean) {
                    campi = bean.properties.keySet()
                    campi?.each {
                        campo = it
                        if (rec."${campo}") {
                            value = rec."${campo}"
                        } else {
                            value = null
                        }// fine del blocco if-else
                        testoOut += getCampoTabella(appName, cont, id, value)
                    } // fine del ciclo each
                }// fine del blocco if
            }// fine del blocco if-else
        }// fine del blocco if

        if (cont && rec && listaExtra) {
            value = false
            listaExtra?.each {
                campo = it
                testoOut += getCampoTabella(appName, cont, id, false)
            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    static String getCampoTabella(String app, String cont, long id, def value) {
        return getCampoTabella(app, cont, id, value, 'show')
    }// fine del metodo

    static String getCampoTabella(String app, String cont, long id, def value, String action) {
        String testo = ''
        String ref = "<a href=\"/${app}/${cont}/${action}/${id}\">"

        if (value instanceof Long || value instanceof Boolean || value instanceof String || value instanceof Date) {
            if (value instanceof Long) {
                testo = getCampoTabellaLong(ref, value)
            }// fine del blocco if

            if (value instanceof Boolean) {
                testo = getCampoTabellaBooleano(value)
            }// fine del blocco if

            if (value instanceof String) {
                if (value.startsWith('http')) {
                    testo = getCampoTabellaUrl(value)
                } else {
                    testo = getCampoTabellaStringa(ref, value)
                }// fine del blocco if-else
            }// fine del blocco if

            if (value instanceof Date) {
                if (value instanceof Timestamp) {
                    testo = getCampoTabellaTime(app, cont, id, value)
                } else {
                    testo = getCampoTabellaData(app, cont, id, value)
                }// fine del blocco if-else
            }// fine del blocco if
        } else {
            if (value) {
                testo = getCampoTabellaStringa(ref, value)
            } else {
                testo = getCampoTabellaStringa(ref, '')
            }// fine del blocco if-else
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    static String getCampoTabellaLong(String ref, def value) {
        String testo = ''

        testo += ref
        testo += "${value}"
        testo += "</a>"

        return tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaBooleano(boolean value) {
        String testo = ''

        if (value) {
            testo = "<input type=\"checkbox\" checked>"
        } else {
            testo = "<input type=\"checkbox\" disabled>"
        }// fine del blocco if-else

        return tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaStringa(String ref, def value) {
        String testo = ''
        String valore = "${value}"

        testo += ref
        testo += "${value}"
        testo += "</a>"

        return tagCella(testo)
    }// fine del metodo

    static String getCampoTabellaUrl(String value) {
        int pos = value.lastIndexOf('/')
        String valore = value.substring(pos + 1)
        String testo = "<a href=\"${value}\">${valore}</a>"
        return tagCella(testo)
    }// fine del metodo


    static String getCampoTabellaTime(String app, String cont, long id, Timestamp value) {
        String testo
        String timeTxt = value.toString()
//        timeTxt = timeTxt.substring(0, 16)

        if (LibTime.isData(timeTxt)) {
            testo = getCampoTabellaData(app, cont, id, value)
        } else {
            timeTxt = timeTxt.substring(0, 16)
            testo = "<a href=\"/${app}/${cont}/show/${id}\">${timeTxt}</a>"
            testo = tagCella(testo)
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    static String getCampoTabellaData(String app, String cont, long id, def value) {
        String testo
        String dataTxt = LibTime.getGioMeseAnno(value)

        testo = "<a href=\"/${app}/${cont}/show/${id}\">${dataTxt}</a>"
        return tagCella(testo)
    }// fine del metodo

    //--Inserisce il testo nel tag della cella (normale)
    public static tagCella(String testoIn) {
        return tag('td', testoIn, 'row', 1)
    } // fine del metodo statico

    //--Inserisce il testo nel tag di un elemento HTML
    public static tag(String tag, String testoIn, String prefixSpan, int span) {
        String testoOut = ''

        testoOut += "<${tag}"
        if (span && span > 1) {
            testoOut += " ${prefixSpan}span=\""
            testoOut += span
            testoOut += '"'
        }// fine del blocco if
        testoOut += '>'

        testoOut += testoIn
        testoOut += "</${tag}>"
        testoOut += aCapo

        return testoOut
    } // fine del metodo statico

} // fine della tag library

