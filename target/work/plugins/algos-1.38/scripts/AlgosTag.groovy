// Tag per commentare le righe di header aggiunte ai file
enum AlgosTag {

    groovy('groovy', '/* ', ' */', false),
    css('css', '/* ', ' */', false),
    gsp('gsp', '<%--', '--%>', false),
    scaffolding('', '<!--', '-->', false),
    xml('xml', '<!--', '-->', true),
    template('template', '<%/* ', ' */%>', false),
    gsptemplate('gsp', '<%/* ', ' */%>', false),
    virgola('', '', ';', false),
    freccia('', '', '>', false),
    vuoto('', '', '', false)

    public final String suffix
    public final String ini
    private final String end
    private final boolean xmlPrimaRiga

    AlgosTag(String suffix, String ini, String end, boolean xmlPrimaRiga) {
        this.suffix = suffix
        this.ini = ini
        this.end = end
        this.xmlPrimaRiga = xmlPrimaRiga
    } // end of Constructor


    public static ArrayList<String> getAllEnd() {
        ArrayList<String> lista = new ArrayList<String>()

        AlgosTag.each {
            lista.add(it.end)
        } // fine del ciclo each

        return lista
    } // fine del metodo

    public String addTag(String testoIn) {
        String testoOut = testoIn

        if (testoIn) {
            testoOut = ini + testoIn + end
        }// fine del blocco if

        return testoOut
    } // fine del metodo

    public String addTagAllLine(String testoIn) {
        String testoOut = testoIn
        String tagRiga = '\n'
        String[] righe

        if (testoIn) {
            testoOut = ''
            righe = testoIn.split(tagRiga)
            righe?.each {
                testoOut += ini + it + end + tagRiga
            } // fine del ciclo each
        }// fine del blocco if

        return testoOut
    } // fine del metodo

} // fine della Enumeration
