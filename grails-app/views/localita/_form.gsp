<%@ page import="it.algos.algosbio.Localita" %>



<div class="fieldcontain ${hasErrors(bean: localitaInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="localita.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" required="" value="${localitaInstance?.nome}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: localitaInstance, field: 'link', 'error')} required">
	<label for="link">
		<g:message code="localita.link.label" default="Link" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="link" required="" value="${localitaInstance?.link}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: localitaInstance, field: 'voci', 'error')} required">
	<label for="voci">
		<g:message code="localita.voci.label" default="Voci" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="voci" type="number" value="${localitaInstance.voci}" required=""/>

</div>

