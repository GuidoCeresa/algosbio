<%@ page import="it.algos.algosvers.Versione" %>



<div class="fieldcontain ${hasErrors(bean: versioneInstance, field: 'numero', 'error')} required">
	<label for="numero">
		<g:message code="versione.numero.label" default="Numero" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="numero" type="number" value="${versioneInstance.numero}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: versioneInstance, field: 'giorno', 'error')} ">
	<label for="giorno">
		<g:message code="versione.giorno.label" default="Giorno" />
		
	</label>
	<g:datePicker name="giorno" precision="day"  value="${versioneInstance?.giorno}" default="none" noSelection="['': '']" />

</div>

<div class="fieldcontain ${hasErrors(bean: versioneInstance, field: 'titolo', 'error')} required">
	<label for="titolo">
		<g:message code="versione.titolo.label" default="Titolo" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="titolo" required="" value="${versioneInstance?.titolo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: versioneInstance, field: 'descrizione', 'error')} ">
	<label for="descrizione">
		<g:message code="versione.descrizione.label" default="Descrizione" />
		
	</label>
	<g:textArea name="descrizione" cols="40" rows="5" value="${versioneInstance?.descrizione}"/>

</div>

