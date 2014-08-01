<%@ page import="it.algos.algosbio.Localita" %>



<div class="fieldcontain ${hasErrors(bean: localitaInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="localita.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" required="" value="${localitaInstance?.nome}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: localitaInstance, field: 'nati', 'error')} required">
	<label for="nati">
		<g:message code="localita.nati.label" default="Nati" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="nati" type="number" value="${localitaInstance.nati}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: localitaInstance, field: 'morti', 'error')} required">
	<label for="morti">
		<g:message code="localita.morti.label" default="Morti" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="morti" type="number" value="${localitaInstance.morti}" required=""/>

</div>

