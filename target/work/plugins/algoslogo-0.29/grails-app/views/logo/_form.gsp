<%@ page import="it.algos.algoslogo.Logo" %>



<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'time', 'error')} ">
	<label for="time">
		<g:message code="logo.time.label" default="Time" />
		
	</label>
	

</div>

<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'utente', 'error')} ">
	<label for="utente">
		<g:message code="logo.utente.label" default="Utente" />
		
	</label>
	<g:textField name="utente" value="${logoInstance?.utente}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'ruolo', 'error')} ">
	<label for="ruolo">
		<g:message code="logo.ruolo.label" default="Ruolo" />
		
	</label>
	<g:textField name="ruolo" value="${logoInstance?.ruolo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'evento', 'error')} required">
	<label for="evento">
		<g:message code="logo.evento.label" default="Evento" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="evento" name="evento.id" from="${it.algos.algoslogo.Evento.list()}" optionKey="id" required="" value="${logoInstance?.evento?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'livello', 'error')} required">
	<label for="livello">
		<g:message code="logo.livello.label" default="Livello" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="livello" from="${it.algos.algoslogo.Livello?.values()}" keys="${it.algos.algoslogo.Livello.values()*.name()}" required="" value="${logoInstance?.livello?.name()}" />

</div>

<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'dettaglio', 'error')} ">
	<label for="dettaglio">
		<g:message code="logo.dettaglio.label" default="Dettaglio" />
		
	</label>
	<g:textField name="dettaglio" value="${logoInstance?.dettaglio}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: logoInstance, field: 'ip', 'error')} ">
	<label for="ip">
		<g:message code="logo.ip.label" default="Ip" />
		
	</label>
	<g:textField name="ip" value="${logoInstance?.ip}"/>

</div>

