<%@ page import="it.algos.algosbio.Cognome" %>



<div class="fieldcontain ${hasErrors(bean: cognomeInstance, field: 'testo', 'error')} required">
	<label for="testo">
		<g:message code="cognome.testo.label" default="Testo" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="testo" required="" value="${cognomeInstance?.testo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: cognomeInstance, field: 'voci', 'error')} required">
	<label for="voci">
		<g:message code="cognome.voci.label" default="Voci" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="voci" type="number" value="${cognomeInstance.voci}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: cognomeInstance, field: 'lunghezza', 'error')} required">
	<label for="lunghezza">
		<g:message code="cognome.lunghezza.label" default="Lunghezza" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="lunghezza" type="number" value="${cognomeInstance.lunghezza}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: cognomeInstance, field: 'isVocePrincipale', 'error')} ">
	<label for="isVocePrincipale">
		<g:message code="cognome.isVocePrincipale.label" default="Is Voce Principale" />
		
	</label>
	<g:checkBox name="isVocePrincipale" value="${cognomeInstance?.isVocePrincipale}" />

</div>

<div class="fieldcontain ${hasErrors(bean: cognomeInstance, field: 'voceRiferimento', 'error')} ">
	<label for="voceRiferimento">
		<g:message code="cognome.voceRiferimento.label" default="Voce Riferimento" />
		
	</label>
	<g:select id="voceRiferimento" name="voceRiferimento.id" from="${it.algos.algosbio.Cognome.list()}" optionKey="id" value="${cognomeInstance?.voceRiferimento?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: cognomeInstance, field: 'wikiUrl', 'error')} ">
	<label for="wikiUrl">
		<g:message code="cognome.wikiUrl.label" default="Wiki Url" />
		
	</label>
	<g:textField name="wikiUrl" value="${cognomeInstance?.wikiUrl}"/>

</div>

