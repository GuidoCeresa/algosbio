package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 19-9-13
 * Time: 12:49
 */
public enum StatoPagina {

    indeterminata('pagina non elaborata', 'Pagina non ancora controllata'),
    maiEsistita('pagina mai esistita', 'Pagina che non è mai stata creata'),
    cancellata('pagina cancellata', 'Pagina esistente ma successivamente cancellata'),
    vuota('pagina vuota', 'Pagina senza nessun contenuto'),
    redirect('pagina di redirect', 'Pagina con redirect ad altra pagina'),
    disambigua('pagina di disambigua', 'Pagina con disambigua iniziale'),
    illeggibile('pagina illeggibile', 'Pagina senza contenuto di testo leggibile con la API'),
    normale('pagina normale', 'Pagina con testo normale')

    String tag = ''
    String description = ''

    /**
     * Costruttore completo con parametri.
     *
     * @param tag utilizzato per chiarezza
     * @param description usato solo qui
     */
    StatoPagina(String tag, String description) {
        this.setTag(tag)
        this.setDescription(description)
    } // fine del costruttore

    public String getTag() {
        return tag
    }

    private void setTag(String tag) {
        this.tag = tag
    }

    private String getDescription() {
        return description
    }

    private void setDescription(String description) {
        this.description = description
    }

} // fine della Enumeration
