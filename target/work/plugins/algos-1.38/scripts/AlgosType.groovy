// Tipo di sostituzione
enum AlgosType {

    always('True'),
    only('False'),
    never('Never'),
    templates('Templates')

    public final String fileName

    AlgosType(String fileName) {
        this.fileName = fileName
    } // end of Constructor

    public String getPath(String dir) {
        if (dir) {
            return dir + fileName
        } else {
            return ''
        }// fine del blocco if-else
    } // fine del metodo

} // fine della Enumeration
