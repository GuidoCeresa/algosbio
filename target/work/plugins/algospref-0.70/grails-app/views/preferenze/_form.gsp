<%@ page import="it.algos.algospref.Preferenze" %>



<div class="fieldcontain ${hasErrors(bean: preferenzeInstance, field: 'ordine', 'error')} required">
	<label for="ordine">
		<g:message code="preferenze.ordine.label" default="Ordine" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ordine" type="number" value="${preferenzeInstance.ordine}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: preferenzeInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="preferenze.code.label" default="Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="code" maxlength="40" required="" value="${preferenzeInstance?.code}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: preferenzeInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="preferenze.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="type" from="${preferenzeInstance.constraints.type.inList}" required="" value="${preferenzeInstance?.type}" valueMessagePrefix="preferenze.type"/>
</div>

<div class="fieldcontain ${hasErrors(bean: preferenzeInstance, field: 'value', 'error')} required">
	<label for="value">
		<g:message code="preferenze.value.label" default="Value" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="value" maxlength="100" required="" value="${preferenzeInstance?.value}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: preferenzeInstance, field: 'descrizione', 'error')} ">
	<label for="descrizione">
		<g:message code="preferenze.descrizione.label" default="Descrizione" />
		
	</label>
	<g:textArea name="descrizione" cols="40" rows="5" value="${preferenzeInstance?.descrizione}"/>
</div>

