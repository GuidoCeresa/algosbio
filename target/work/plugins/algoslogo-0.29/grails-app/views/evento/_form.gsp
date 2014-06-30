<%@ page import="it.algos.algoslogo.Evento" %>



<div class="fieldcontain ${hasErrors(bean: eventoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="evento.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" required="" value="${eventoInstance?.nome}"/>

</div>

