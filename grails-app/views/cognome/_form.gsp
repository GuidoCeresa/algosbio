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

