package it.algos.algospref

class Pref {

    int ordine
    Type type
    String code
    String descrizione
    Date dateCreated
    Date lastUpdated

    String stringa      // VARCHAR(255)
    Boolean bool        // BIT(1)
    Integer intero      // INTEGER
    Long lungo          // BIGINT(20)
    Float reale         // FLOAT
    Double doppio       // DOUBLE
    BigDecimal decimale // DECIMAL(19,2)
    Date data           // DATETIME
    String testo        // LONGTEXT

//    char valCarattere       // CHAR(1)   //@todo da sviluppare

//    Time valtempo            //@todo da sviluppare
//    Timestamp valTimestamp   //@todo da sviluppare

//    enum enumerazione {        //@todo da sviluppare
//        a, b
//    }


    static constraints = {
        ordine(unique: true)
        type(nullable: false)
        code(blank: false, size: 1..40, unique: true)
        descrizione(widget: 'textarea', nullable: true, blank: true)
        stringa(nullable: true, blank: true)
        bool(nullable: true)
        intero(nullable: true)
        lungo(nullable: true)
        reale(nullable: true)
        doppio(nullable: true)
        decimale(nullable: true)
        data(nullable: true)
        testo(widget: 'textarea', nullable: true, blank: true)
    } // end of static constraints

    /**
     * Grails support two tipe of Inheritance:
     * 1) table-per-hierarchy mapping (default)
     * 2) table-per-subclasses
     * By default GORM classes use table-per-hierarchy inheritance mapping.
     * This has the disadvantage that columns cannot have a NOT-NULL constraint applied to them at the database level.
     * If you would prefer to use a table-per-subclass inheritance strategy you can do so as follows:
     *         tablePerHierarchy false
     * The mapping of the root Xyz class specifies that it will not be using table-per-hierarchy mapping for all child classes.
     */
    // nomi dei campi sul database, di default usa il nome interno del campo
    // la superclasse di ogni domainClass inserisce i campi dateCreated e lastUpdated
    // che vengono aggiornati automaticamente da GORM
    // per disabilitare l'automatismo, mettere a false la proprietà autoTimestamp nella classe specifica
    // Grails inserisce automaticamente la proprietà/campo 'versione' per l'optimistic locking
    // per disabilitare l'automatismo, mettere a false la proprietà version nella classe specifica
    static mapping = {
        // stringa di lunghezza variabile
        ordine generator: 'native'
        descrizione type: 'text'
        testo type: 'text'
    } // end of static mapping

    public Pref() {
    }// fine del metodo costruttore

    public Pref(String code, String stringa) {
        this(code, stringa, '')
    }// fine del metodo costruttore

    public Pref(String code, String stringa, String descrizione) {
        this(code, stringa, Type.stringa, descrizione)
    }// fine del metodo costruttore

    public Pref(String code, String stringa, Type type) {
        this(code, stringa, type, '')
    }// fine del metodo costruttore

    public Pref(String code, String stringa, Type type, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(type)
        switch (type) {
            case Type.stringa:
                this.setStringa(stringa)
                break
            case Type.testo:
                this.setTesto(stringa)
                break
            default: // caso non definito
                break
        } // fine del blocco switch
    }// fine del metodo costruttore

    public Pref(String code, boolean bool) {
        this(code, bool, '')
    }// fine del metodo costruttore

    public Pref(String code, boolean bool, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.booleano)
        this.setBool(bool)
    }// fine del metodo costruttore

    public Pref(String code, int intero) {
        this(code, intero, '')
    }// fine del metodo costruttore

    public Pref(String code, int intero, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.intero)
        this.setIntero(intero)
    }// fine del metodo costruttore

    public Pref(String code, long lungo) {
        this(code, lungo, '')
    }// fine del metodo costruttore

    public Pref(String code, long lungo, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.lungo)
        this.setLungo(lungo)
    }// fine del metodo costruttore

    public Pref(String code, float reale) {
        this(code, reale, '')
    }// fine del metodo costruttore

    public Pref(String code, float reale, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.reale)
        this.setReale(reale)
    }// fine del metodo costruttore

    public Pref(String code, double doppio) {
        this(code, doppio, '')
    }// fine del metodo costruttore

    public Pref(String code, double doppio, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.doppio)
        this.setDoppio(doppio)
    }// fine del metodo costruttore

    public Pref(String code, BigDecimal decimale) {
        this(code, decimale, '')
    }// fine del metodo costruttore

    public Pref(String code, BigDecimal decimale, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.decimale)
        this.setDecimale(decimale)
    }// fine del metodo costruttore

    public Pref(String code, Date data) {
        this(code, data, '')
    }// fine del metodo costruttore

    public Pref(String code, Date data, String descrizione) {
        this.setNew(code, descrizione)
        this.setType(Type.data)
        this.setData(data)
    }// fine del metodo costruttore

    private setNew(String code, String descrizione) {
        this.setOrdine(LibPref.getNewOrdine())
        this.setCode(code)
        if (descrizione) {
            this.setDescrizione(descrizione)
        }// fine del blocco if
    }// fine del metodo

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
        regola()
    } // end of metodo beforeInsert

    /**
     * metodo chiamato automaticamente da Grails
     * prima di registrare un record esistente
     * metodo e non closure (che non funziona)
     */
    def beforeUpdate() {
        regola()
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
    } // end of metodo beforeValidate

    /**
     * metodo chiamato automaticamente da Grails
     * dopo aver creato un nuovo record
     * metodo e non closure (che non funziona)
     */
    def afterInsert() {
    } // end of metodo afterInsert

    /**
     * metodo chiamato automaticamente da Grails
     * dopo aver registrato un record esistente
     * metodo e non closure (che non funziona)
     */
    def afterUpdate() {
    } // end of metodo afterUpdate

    /**
     * metodo chiamato automaticamente da Grails
     * dopo aver cancellato un record
     * metodo e non closure (che non funziona)
     */
    def afterDelete() {
    } // end of metodo afterDelete

    /**
     * metodo chiamato automaticamente da Grails
     * dopo che il record è stato letto dal database e
     * le proprietà dell'oggetto sono state aggiornate
     * metodo e non closure (che non funziona)
     */
    def onLoad() {
    } // end of metodo onLoad

    def regola() {
        if (type != Type.stringa) {
            stringa = null
        }// fine del blocco if
        if (type != Type.testo) {
            testo = null
        }// fine del blocco if
        if (type != Type.booleano) {
            bool = null
        }// fine del blocco if
        if (type != Type.intero) {
            intero = null
        }// fine del blocco if
        if (type != Type.lungo) {
            lungo = null
        }// fine del blocco if
        if (type != Type.reale) {
            reale = null
        }// fine del blocco if
        if (type != Type.doppio) {
            doppio = null
        }// fine del blocco if
        if (type != Type.decimale) {
            decimale = null
        }// fine del blocco if
        if (type != Type.data) {
            data = null
        }// fine del blocco if
    } // fine del metodo


    public String getStr() {
        if (type == Type.stringa) {
            return stringa
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static String getStr(String code) {
        return findByCode(code)?.getStr()
    } // end of method

    public static String getStr(String code, String suggerito) {
        Pref pref = findByCode(code)

        if (pref && pref.type == Type.stringa) {
            return pref.getStr()
        } else {
            return suggerito
        }// fine del blocco if-else
    } // end of method

    public String getTxt() {
        if (type == Type.testo) {
            return testo
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static String getTxt(String code) {
        return findByCode(code)?.getTxt()
    } // end of method

    public Boolean getBool() {
        if (type == Type.booleano) {
            return bool
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static Boolean getBool(String code) {
        return findByCode(code)?.getBool()
    } // end of method

    public static Boolean getBool(String code, boolean suggerito) {
        Pref pref = findByCode(code)

        if (pref && pref.type == Type.booleano) {
            return pref.getBool()
        } else {
            return suggerito
        }// fine del blocco if-else
    } // end of method

    public Integer getInt() {
        if (type == Type.intero) {
            return intero
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static Integer getInt(String code) {
        return findByCode(code)?.getInt()
    } // end of method

    public static Integer getInt(String code, int suggerito) {
        Pref pref = findByCode(code)

        if (pref && pref.type == Type.intero) {
            return pref.getInt()
        } else {
            return suggerito
        }// fine del blocco if-else
    } // end of method


    public Long getLong() {
        if (type == Type.lungo) {
            return lungo
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static Long getLong(String code) {
        return findByCode(code)?.getLong()
    } // end of method

    public Float getReal() {
        if (type == Type.reale) {
            return reale
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static Float getReal(String code) {
        return findByCode(code)?.getReal()
    } // end of method

    public static Float getFloat(String code) {
        return getReal(code)
    } // end of method

    public Double getDouble() {
        if (type == Type.doppio) {
            return doppio
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static Double getDouble(String code) {
        return findByCode(code)?.getDouble()
    } // end of method

    public BigDecimal getDec() {
        if (type == Type.decimale) {
            return decimale
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static BigDecimal getDec(String code) {
        return findByCode(code)?.getDec()
    } // end of method

    public Date getDate() {
        if (type == Type.data) {
            return data
        } else {
            return null
        }// fine del blocco if-else
    } // end of method

    public static Date getDate(String code) {
        return findByCode(code)?.getDate()
    } // end of method

} // fine della domain classe
