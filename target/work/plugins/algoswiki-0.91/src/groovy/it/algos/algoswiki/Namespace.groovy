package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 7-11-12
 * Time: 07:52
 */
// MediaWiki ships with 18 built-in namespaces.
public enum Namespace {
    main(0, 'Main', 'Real content, articles'),
    talk(1, 'Talk', 'Talk pages of real content'),
    user(2, 'User', 'Real content, articles'),
    userTalk(3, 'User talk', 'Real content, articles'),
    project(4, 'Project', 'Real content, articles'),
    projectTalk(5, 'Project talk', 'Real content, articles'),
    file(6, 'File', 'Real content, articles'),
    fileTalk(7, 'File talk', 'Real content, articles'),
    mediaWiki(8, 'MediaWiki', 'Real content, articles'),
    mediaWikiTalk(9, 'MediaWiki talk', 'Real content, articles'),
    template(10, 'Template', 'Real content, articles'),
    templateTalk(11, 'Template talk', 'Real content, articles'),
    help(12, 'Help', 'Real content, articles'),
    helpTalk(13, 'Help talk', 'Real content, articles'),
    category(14, 'Category', 'Real content, articles'),
    categoryTalk(15, 'Category talk', 'Real content, articles')

    private int number
    private String tag
    private String description

    /**
     * Costruttore completo con parametri.
     *
     * @param number utilizzato nelle query
     * @param tag utilizzato per chiarezza
     * @param description usato solo qui
     */
    Namespace(int number, String tag, String description) {
        this.setNumber(number)
        this.setTag(tag)
        this.setDescription(description)
    } // fine del costruttore

    int getNumber() {
        return number
    }

    void setNumber(int number) {
        this.number = number
    }

    String getTag() {
        return tag
    }

    void setTag(String tag) {
        this.tag = tag
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }
} // fine della Enumeration
