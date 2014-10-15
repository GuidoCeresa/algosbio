<%@ page import="it.algos.algosbio.Anno" %>



<div class="fieldcontain ${hasErrors(bean: annoInstance, field: 'progressivoCategoria', 'error')} required">
	<label for="progressivoCategoria">
		<g:message code="anno.progressivoCategoria.label" default="Progressivo Categoria" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="progressivoCategoria" type="number" value="${annoInstance.progressivoCategoria}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: annoInstance, field: 'titolo', 'error')} required">
	<label for="titolo">
		<g:message code="anno.titolo.label" default="Titolo" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="titolo" required="" value="${annoInstance?.titolo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: annoInstance, field: 'secolo', 'error')} ">
	<label for="secolo">
		<g:message code="anno.secolo.label" default="Secolo" />
		
	</label>
	<g:textField name="secolo" value="${annoInstance?.secolo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: annoInstance, field: 'sporcoMorto', 'error')} ">
	<label for="sporcoMorto">
		<g:message code="anno.sporcoMorto.label" default="Sporco Morto" />
		
	</label>
	<g:checkBox name="sporcoMorto" value="${annoInstance?.sporcoMorto}" />

</div>

<div class="fieldcontain ${hasErrors(bean: annoInstance, field: 'sporcoNato', 'error')} ">
	<label for="sporcoNato">
		<g:message code="anno.sporcoNato.label" default="Sporco Nato" />
		
	</label>
	<g:checkBox name="sporcoNato" value="${annoInstance?.sporcoNato}" />

</div>

