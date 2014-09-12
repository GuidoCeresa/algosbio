<%@ page import="it.algos.algosbio.Genere" %>



<div class="fieldcontain ${hasErrors(bean: genereInstance, field: 'singolare', 'error')} required">
	<label for="singolare">
		<g:message code="genere.singolare.label" default="Singolare" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="singolare" required="" value="${genereInstance?.singolare}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: genereInstance, field: 'plurale', 'error')} required">
	<label for="plurale">
		<g:message code="genere.plurale.label" default="Plurale" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="plurale" required="" value="${genereInstance?.plurale}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: genereInstance, field: 'sesso', 'error')} required">
	<label for="sesso">
		<g:message code="genere.sesso.label" default="Sesso" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="sesso" required="" value="${genereInstance?.sesso}"/>

</div>

