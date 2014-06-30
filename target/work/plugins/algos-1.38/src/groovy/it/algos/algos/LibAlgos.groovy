package it.algos.algos
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 21-7-13
 * Time: 07:41
 */
class LibAlgos {


    static String getTitoloTabellaSorted(String app, String cont, String sort, String order, String title) {
        return "<th class=\"sortable\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
    }// fine del metodo

    static String getTitoloTabellaNotSorted(String app, String cont, String sort, String order, String title) {
        String testo

        if (order && order.equals('desc')) {
            testo = "<th class=\"sortable sorted desc\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
        } else {
            testo = "<th class=\"sortable sorted asc\"><a href=\"/${app}/${cont}/list?sort=${sort}&amp;order=${order}\">${title}</a></th>"
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    static ArrayList<Long> listaId(Class domain) {
        ArrayList<Long> listaId = null
        String query
        String nome

        if (domain) {
            nome = domain.getSimpleName()
            if (nome) {
                query = "Select id from ${nome} order by id"
                listaId = (ArrayList<Long>) domain.executeQuery(query)
            }// fine del blocco if
        }// fine del blocco if

        return listaId
    } // fine del metodo

    static long primo(Class domain) {
        long pos = 0
        ArrayList<Long> listaId = listaId(domain)

        if (listaId && listaId.size() > 0) {
            pos = listaId[0]
        }// fine del blocco if

        return pos
    } // fine del metodo

    static long prec(Class domain, long posOld) {
        long posNew = posOld
        int posArray
        ArrayList<Long> listaId = listaId(domain)

        if (listaId && listaId.size() > 1) {
            posArray = listaId.indexOf(posOld)
            if (posArray > 0) {
                posArray--
                posNew = listaId[posArray]
            }// fine del blocco if
        }// fine del blocco if

        return posNew
    } // fine del metodo

    static long suc(Class domain, long posOld) {
        long posNew = posOld
        int posArray
        ArrayList<Long> listaId = listaId(domain)

        if (listaId && listaId.size() > 1) {
            posArray = listaId.indexOf(posOld)
            if (posArray < listaId.size() - 1) {
                posArray++
                posNew = listaId[posArray]
            }// fine del blocco if
        }// fine del blocco if

        return posNew
    } // fine del metodo

    static long ultimo(Class domain) {
        long pos = 0
        ArrayList<Long> listaId = listaId(domain)

        if (listaId && listaId.size() > 0) {
            pos = listaId[listaId.size() - 1]
        }// fine del blocco if

        return pos
    } // fine del metodo

} // fine della classe
