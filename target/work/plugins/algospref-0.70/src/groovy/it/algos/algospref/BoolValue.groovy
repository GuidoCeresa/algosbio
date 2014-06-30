package it.algos.algospref

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 23-9-12
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public enum BoolValue {

    vero1(vero, true),
    vero2('Vero', true),
    vero3('true', true),
    vero4('True', true),
    vero5('ok', true),
    vero6('OK', true),
    vero7('Ok', true),
    vero8('YES', true),
    vero9('yes', true),
    vero10('Yes', true),
    vero11('SI', true),
    vero12('si', true),
    vero13('Si', true),
    falso1('falso', false),
    falso2('Falso', false),
    falso3('false', false),
    falso4('False', false),
    falso5('NO', false),
    falso6('no', false),
    falso7('No', false),
    falso8('NON', false),
    falso9('non', false),
    falso10('Non', false),
    fals11('', false),
    fals12(' ', false)

    String tag
    boolean booleano

    public final static String vero = 'vero'
    public final static String falso = 'falso'


    BoolValue(String tag, boolean booleano) {
        /* regola le variabili di istanza coi parametri */
        this.setTag(tag)
        this.setBooleano(booleano)
    }// fine del costruttore

    public static boolean isVero(String value) {
        boolean booleano = false

        values().each {
            if (it.tag.equals(value)) {
                booleano = it.booleano
            }// fine del blocco if
        } // fine del ciclo each

        return booleano
    } // fine del metodo

    public static boolean isFalso(String value) {
        return !isVero(value)
    } // fine del metodo

    String getTag() {
        return tag
    } // fine del metodo


    void setTag(String tag) {
        this.tag = tag
    } // fine del metodo


    boolean getBooleano() {
        return booleano
    } // fine del metodo


    void setBooleano(boolean booleano) {
        this.booleano = booleano
    } // fine del metodo

}// fine della classe Enumeration
