package it.algos.algospref

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class Preferenze {

    public static final String TYPE_STR = 'string'
    public static final String TYPE_INT = 'integer'
    public static final String TYPE_DEC = 'decimal'
    public static final String TYPE_DATA = 'date'
    public static final String TYPE_BOOL = 'boolean'

    private static final missingValue = "\b" // an impossible value signifying that no such code exists in the database
    public static tipo = [TYPE_STR, TYPE_INT, TYPE_DEC, TYPE_DATA, TYPE_BOOL]
    private static DateFormat formatter = new SimpleDateFormat('dd-MM-yyyy', Locale.ITALY)

    String code
    String type
    String value
    Date dateCreated
    Date lastUpdated
    int ordine
    String descrizione

    static constraints = {
        ordine(unique: true)
        code(blank: false, size: 1..40, unique: true)
        type(blank: false, inList: tipo)
        value(blank: false, size: 1..100, validator: { val, obj -> (decodeValue(val, obj) != null) ? true : false })
        descrizione(widget: 'textarea', nullable: true, blank: true)
    } // end of static constraints


    static mapping = {
        columns {
            code index: "pref_code_idx"
        }
        descrizione type: 'text'
    } // end of static mapping

    public Pref(String code, String stringa) {
        /* regola le variabili di istanza coi parametri */
        this.setCode(code)
        this.setType(tipo[0])
        this.setValue(stringa)
    }// fine del metodo costruttore


    public Pref(String code, int intero) {
        /* regola le variabili di istanza coi parametri */
        this.setCode(code)
        this.setType(tipo[1])
        this.setIntero(intero)
    }// fine del metodo costruttore


    public Pref(String code, Date data) {
        /* regola le variabili di istanza coi parametri */
        this.setCode(code)
        this.setType(tipo[3])
        this.setData(data)
    }// fine del metodo costruttore

    public Pref(String code, boolean booleano) {
        /* regola le variabili di istanza coi parametri */
        this.setCode(code)
        this.setType(tipo[4])
        this.regolaBool(booleano)
    }// fine del metodo costruttore

    static getValue(String code) {
        if (!code) return null

        def val

        val = findByCode(code)
        if (val) {
            val = decodeValue(val.type, val.value)
        }

        if (!val) val = missingValue

        return (val != missingValue) ? val : null
    } // end of static method

    private static decodeValue(String value, obj) {
        String type = obj.type
        if (value) {
            switch (type) {
                case "integer":
                    try {
                        return new Integer(value)
                    } catch (NumberFormatException ne) {
                    }
                    break

                case "decimal":
                    try {
                        return new BigDecimal(value)
                    } catch (NumberFormatException ne) {
                    }
                    break

                case "date":
                    try {
                        return formatter.parse(value)
                    } catch (ParseException pe) {
                    }
                    break

                case "boolean":
                    try {
                        obj.regolaBool(BoolValue.isVero(value))
                        return true
                        // return BoolValue.isVero(value)
                    } catch (ParseException pe) {
                    }
                    break

                default:  // string
                    return value
                    break
            }// fine di switch
        }// fine del blocco if

        return null
    } // end of static method

    /**
     * GORM supports the registration of events as methods that get fired
     * when certain events occurs such as deletes, inserts and updates
     * The following is a list of supported events:
     * beforeInsert - Executed before an object is initially persisted to the database
     * beforeUpdate - Executed before an object is updated
     * beforeDelete - Executed before an object is deleted
     * beforeValidate - Executed before an object is validated
     * afterInsert - Executed after an object is persisted to the database
     * afterUpdate - Executed after an object has been updated
     * afterDelete - Executed after an object has been deleted
     * onLoad - Executed when an object is loaded from the database
     * devono essere metodi e non closure (le closure non funzionano dal 13-3-14)
     */

    /**
     * metodo chiamato automaticamente da Grails
     * prima di creare un nuovo record
     * metodo e non closure (che non funziona)
     */
    def beforeInsert() {
    } // end of metodo beforeInsert

    /**
     * metodo chiamato automaticamente da Grails
     * prima di registrare un record esistente
     * metodo e non closure (che non funziona)
     */
    def beforeUpdate() {
    } // end of metodo beforeUpdate

    /**
     * metodo chiamato automaticamente da Grails
     * prima di cancellare un record
     * metodo e non closure (che non funziona)
     */
    def beforeDelete() {
    } // end of metodo beforeDelete

    /**
     * metodo chiamato automaticamente da Grails
     * prima di convalidare un record
     * metodo e non closure (che non funziona)
     */
    def beforeValidate() {
    } // end of metodo beforeDelete

    /**
     * metodo chiamato automaticamente da Grails
     * dopo aver creato un nuovo record
     * metodo e non closure (che non funziona)
     */
    def afterInsert() {
    } // end of metodo beforeInsert

    /**
     * metodo chiamato automaticamente da Grails
     * dopo aver registrato un record esistente
     * metodo e non closure (che non funziona)
     */
    def afterUpdate() {
    } // end of metodo beforeUpdate

    /**
     * metodo chiamato automaticamente da Grails
     * dopo aver cancellato un record
     * metodo e non closure (che non funziona)
     */
    def afterDelete() {
    } // end of metodo beforeDelete

    /**
     * metodo chiamato automaticamente da Grails
     * dopo che il record è stato letto dal database e
     * le proprietà dell'oggetto sono state aggiornate
     * metodo e non closure (che non funziona)
     */
    def onLoad() {
    } // end of metodo onLoad

    public setIntero(int valoreInt) {
        String testo

        if (valoreInt) {
            testo = valoreInt.toString()
            if (testo) {
                this.value = testo
            }// fine del blocco if
        }// fine del blocco if
    } // end of method

    public setData(Date valoreData) {
        String testo

        if (valoreData) {
            testo = formatter.format(valoreData)
            if (testo) {
                this.value = testo
            }// fine del blocco if
        }// fine del blocco if
    } // end of method


    public regolaBool(boolean valoreBool) {
        if (valoreBool) {
            this.value = BoolValue.vero
        } else {
            this.value = BoolValue.falso
        }// fine del blocco if-else
    } // end of method


    public String getStr() {
        return decodeValue(value, this)
    } // end of method


    public int getInt() {
        return decodeValue(value, this)
    } // end of method


    public Date getDate() {
        return decodeValue(value, this)
    } // end of method

    public boolean getBool() {
        return BoolValue.isVero(value)
    } // end of method

    public static String getStr(String code) {
        String testo = ''
        Preferenze pref = findByCode(code)
        if (pref) {
            testo = pref.value
        }// fine del blocco if

        return testo
    } // end of method


    public static int getInt(String code) {
        int numero = 0
        Preferenze pref = findByCode(code)
        if (pref) {
            numero = pref.getInt()
        }// fine del blocco if

        return numero
    } // end of method


    public static Date getDate(String code) {
        Date data = null
        Preferenze pref = findByCode(code)
        if (pref) {
            data = pref.getDate()
        }// fine del blocco if

        return data
    } // end of method

    public static boolean getBool(String code) {
        boolean booleano = false
        Preferenze pref = findByCode(code)
        if (pref) {
            booleano = pref.getBool()
        }// fine del blocco if

        return booleano
    } // end of method

} // end of Class
