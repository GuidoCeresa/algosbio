import grails.util.Holders
import it.algos.algosbio.LibBio
import it.algos.algosbio.LibPass
import it.algos.algospref.Pref
import it.algos.algoswiki.ErrLogin
import it.algos.algoswiki.Login
import it.algos.algosbio.BioWiki

class BootStrap {


    def init = { servletContext ->
        new BioWiki(pageid:27,title:"Stand").save()
        new BioWiki(pageid:28,title:"Shining").save()
    } // fine della closure

    def destroy = {
    } // fine della closure

}// fine della classe di bootstrap
