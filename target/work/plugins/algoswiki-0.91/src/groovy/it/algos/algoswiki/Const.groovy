package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:28
 */
class Const {
    /* Formato dati selezionato per la risposta alla Request */
    public static Format FORMAT = Format.json

    /* codifica dei caratteri */
    public static String ENC = 'UTF-8'

    /* prefisso URL base */
    public static String API_HTTP = 'https://'

    /* prefisso iniziale (prima del progetto) */
    public static String API_WIKI = '.'

    /* azione API generica */
    public static String API_ACTION = '.org/w/api.php?action='

    /* azione API login */
    public static String API_LOGIN = 'login'

    /* suffisso per il formato della risposta */
    public static String API_FORMAT = '&format=' + FORMAT.toString()

    /* azione API delle query */
    public static String API_QUERY = API_ACTION + 'query'

    /* prefisso URL per leggere la pagina in modifica in formato txt anzichè html */
    public static String WIKI_PRE = '.org/w/index.php?title='

    /* suffisso URL per leggere la pagina in modifica in formato txt anzichè html */
    public static String WIKI_POST = '&action=edit'

    /* parametro API query normale */
    public static String CONTENT = '&prop=revisions&rvprop=content'

    /* parametro API query estesa */
    // ids: Get both of these IDs: revid, parentid
    // flags: Whether the revision was a minor edit
    public static String CONTENT_ALL = '&prop=revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size'

    /* parametro API */
    public static String TITLE = '&titles='

    /* parametro API */
    public static String QUERY_ID = '&pageids='

    /* parametro API */
    public static String QUERY = 'query'

    /* tag delle mappe (obbligatori) */
    public static String TAG_TITlE = 'title'
    public static String TAG_NS = 'ns'
    public static String TAG_PAGE_ID = 'pageid'
    public static String TAG_TESTO_WIKI = '*'
    public static String TAG_TESTO = 'testo'
    public static String TAG_TYPE = 'type'
    public static String TAG_CONTINUE = 'cmcontinue'
    public static String TAG_SUCCESSO = 'Success'

    /* tag delle mappe (facoltativi) */
    public static String TAG_USER = 'user'
    public static String TAG_USER_ID = 'userid'
    public static String TAG_SIZE = 'size'
    public static String TAG_COMMENT = 'comment'
    public static String TAG_CONTENT_FORMAT = 'contentformat'
    public static String TAG_CONTENT_MODEL = 'contentmodel'
    public static String TAG_REV_ID = 'revid'
    public static String TAG_PARENT_ID = 'parentid'
    public static String TAG_MINOR = 'minor'
    public static String TAG_TIMESTAMP = 'timestamp'
    public static String TAG_PAGE_LANGUAGE = 'pagelanguage'
    public static String TAG_TOUCHED = 'touched'
    public static String TAG_LAST_REV_ID = 'lastrevid'
    public static String TAG_COUNTER = 'counter'
    public static String TAG_LENGTH = 'length'
    public static String TAG_START_TIME_STAMP = 'starttimestamp'
    public static String TAG_EDIT_TOKEN = 'edittoken'
    public static String TAG_MISSING = 'missing'
    public static String TAG_REDIRECT = '#redirect'
    public static String TAG_DISAMBIGUA = '{{Disambigua}}'
    public static String TAG_STATO_PAGINA = 'statopagina'
    public static String TAG_PAGES = 'pages'
    public static String TAG_FILES = 'files'
    public static String TAG_SUBCATS = 'subcats'
    public static String TAG_REVISIONS = 'revisions'
    public static String TAG_CATEGORYINFO = 'categoryinfo'
    public static String TAG_HIDDEN = 'hidden'

    /* caratteri finali per la stringa di edittoken da rinviare al server */
    public static String END_TOKEN = '%2B%5C'

} // fine della Enumeration
