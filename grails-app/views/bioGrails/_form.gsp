












<%@ page import="it.algos.algosbio.BioGrails" %>



<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'pageid', 'error')} required">
	<label for="pageid">
		<g:message code="bioGrails.pageid.label" default="Pageid" />
		<span class="required-indicator">*</span>
	</label>
	










<g:field name="pageid" type="number" value="${bioGrailsInstance.pageid}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="bioGrails.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	










<g:textField name="title" required="" value="${bioGrailsInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'nome', 'error')} ">
	<label for="nome">
		<g:message code="bioGrails.nome.label" default="Nome" />
		
	</label>
	










<g:textField name="nome" value="${bioGrailsInstance?.nome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'cognome', 'error')} ">
	<label for="cognome">
		<g:message code="bioGrails.cognome.label" default="Cognome" />
		
	</label>
	










<g:textField name="cognome" value="${bioGrailsInstance?.cognome}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'forzaOrdinamento', 'error')} ">
	<label for="forzaOrdinamento">
		<g:message code="bioGrails.forzaOrdinamento.label" default="Forza Ordinamento" />
		
	</label>
	










<g:textField name="forzaOrdinamento" value="${bioGrailsInstance?.forzaOrdinamento}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'sesso', 'error')} ">
	<label for="sesso">
		<g:message code="bioGrails.sesso.label" default="Sesso" />
		
	</label>
	










<g:textField name="sesso" value="${bioGrailsInstance?.sesso}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'attivita', 'error')} ">
	<label for="attivita">
		<g:message code="bioGrails.attivita.label" default="Attivita" />
		
	</label>
	










<g:textField name="attivita" value="${bioGrailsInstance?.attivita}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'attivita2', 'error')} ">
	<label for="attivita2">
		<g:message code="bioGrails.attivita2.label" default="Attivita2" />
		
	</label>
	










<g:textField name="attivita2" value="${bioGrailsInstance?.attivita2}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'attivita3', 'error')} ">
	<label for="attivita3">
		<g:message code="bioGrails.attivita3.label" default="Attivita3" />
		
	</label>
	










<g:textField name="attivita3" value="${bioGrailsInstance?.attivita3}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'nazionalita', 'error')} ">
	<label for="nazionalita">
		<g:message code="bioGrails.nazionalita.label" default="Nazionalita" />
		
	</label>
	










<g:textField name="nazionalita" value="${bioGrailsInstance?.nazionalita}"/>
</div>




<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'luogoNatoLink', 'error')} ">
    <label for="luogoNatoLink">
        <g:message code="bioGrails.luogoNatoLink.label" default="Località Link" />
    </label>
    <g:select id="luogoNatoLink" name="luogoNatoLink.id" from="${it.algos.algosbio.Localita.list()}" optionKey="id" value="${bioGrailsInstance?.luogoNatoLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'luogoMortoLink', 'error')} ">
    <label for="luogoMortoLink">
        <g:message code="bioGrails.luogoMortoLink.label" default="Località Link" />
    </label>
    <g:select id="luogoMortoLink" name="luogoMortoLink.id" from="${it.algos.algosbio.Localita.list()}" optionKey="id" value="${bioGrailsInstance?.luogoMortoLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>






<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'didascaliaBase', 'error')} ">
	<label for="didascaliaBase">
		<g:message code="bioGrails.didascaliaBase.label" default="Didascalia Base" />
		
	</label>
	










<g:textField name="didascaliaBase" value="${bioGrailsInstance?.didascaliaBase}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'didascaliaGiornoNato', 'error')} ">
	<label for="didascaliaGiornoNato">
		<g:message code="bioGrails.didascaliaGiornoNato.label" default="Didascalia Giorno Nato" />
		
	</label>
	










<g:textField name="didascaliaGiornoNato" value="${bioGrailsInstance?.didascaliaGiornoNato}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'didascaliaGiornoMorto', 'error')} ">
	<label for="didascaliaGiornoMorto">
		<g:message code="bioGrails.didascaliaGiornoMorto.label" default="Didascalia Giorno Morto" />
		
	</label>
	










<g:textField name="didascaliaGiornoMorto" value="${bioGrailsInstance?.didascaliaGiornoMorto}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'didascaliaAnnoNato', 'error')} ">
	<label for="didascaliaAnnoNato">
		<g:message code="bioGrails.didascaliaAnnoNato.label" default="Didascalia Anno Nato" />
		
	</label>
	










<g:textField name="didascaliaAnnoNato" value="${bioGrailsInstance?.didascaliaAnnoNato}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'didascaliaAnnoMorto', 'error')} ">
	<label for="didascaliaAnnoMorto">
		<g:message code="bioGrails.didascaliaAnnoMorto.label" default="Didascalia Anno Morto" />
		
	</label>
	










<g:textField name="didascaliaAnnoMorto" value="${bioGrailsInstance?.didascaliaAnnoMorto}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'didascaliaListe', 'error')} ">
	<label for="didascaliaListe">
		<g:message code="bioGrails.didascaliaListe.label" default="Didascalia Liste" />
		
	</label>
	










<g:textField name="didascaliaListe" value="${bioGrailsInstance?.didascaliaListe}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'giornoMeseNascitaLink', 'error')} ">
	<label for="giornoMeseNascitaLink">
		<g:message code="bioGrails.giornoMeseNascitaLink.label" default="Giorno Mese Nascita Link" />
		
	</label>
	










<g:select id="giornoMeseNascitaLink" name="giornoMeseNascitaLink.id" from="${it.algos.algosbio.Giorno.list()}" optionKey="id" value="${bioGrailsInstance?.giornoMeseNascitaLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'giornoMeseMorteLink', 'error')} ">
	<label for="giornoMeseMorteLink">
		<g:message code="bioGrails.giornoMeseMorteLink.label" default="Giorno Mese Morte Link" />
		
	</label>
	










<g:select id="giornoMeseMorteLink" name="giornoMeseMorteLink.id" from="${it.algos.algosbio.Giorno.list()}" optionKey="id" value="${bioGrailsInstance?.giornoMeseMorteLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'annoNascitaLink', 'error')} ">
	<label for="annoNascitaLink">
		<g:message code="bioGrails.annoNascitaLink.label" default="Anno Nascita Link" />
		
	</label>
	










<g:select id="annoNascitaLink" name="annoNascitaLink.id" from="${it.algos.algosbio.Anno.list()}" optionKey="id" value="${bioGrailsInstance?.annoNascitaLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'annoMorteLink', 'error')} ">
	<label for="annoMorteLink">
		<g:message code="bioGrails.annoMorteLink.label" default="Anno Morte Link" />
		
	</label>
	










<g:select id="annoMorteLink" name="annoMorteLink.id" from="${it.algos.algosbio.Anno.list()}" optionKey="id" value="${bioGrailsInstance?.annoMorteLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'attivitaLink', 'error')} ">
	<label for="attivitaLink">
		<g:message code="bioGrails.attivitaLink.label" default="Attivita Link" />
		
	</label>
	










<g:select id="attivitaLink" name="attivitaLink.id" from="${it.algos.algosbio.Attivita.list()}" optionKey="id" value="${bioGrailsInstance?.attivitaLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'attivita2Link', 'error')} ">
	<label for="attivita2Link">
		<g:message code="bioGrails.attivita2Link.label" default="Attivita2 Link" />
		
	</label>
	










<g:select id="attivita2Link" name="attivita2Link.id" from="${it.algos.algosbio.Attivita.list()}" optionKey="id" value="${bioGrailsInstance?.attivita2Link?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>



<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'attivita3Link', 'error')} ">
	<label for="attivita3Link">
		<g:message code="bioGrails.attivita3Link.label" default="Attivita3 Link" />
	</label>
<g:select id="attivita3Link" name="attivita3Link.id" from="${it.algos.algosbio.Attivita.list()}" optionKey="id" value="${bioGrailsInstance?.attivita3Link?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>



<div class="fieldcontain ${hasErrors(bean: bioGrailsInstance, field: 'nomeLink', 'error')} ">
	<label for="nomeLink">
		<g:message code="bioGrails.nomeLink.label" default="Nome Link" />
	</label>
<g:select id="nomeLink" name="nomeLink.id" from="${it.algos.algosbio.Antroponimo.list()}" optionKey="id" value="${bioGrailsInstance?.nomeLink?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

