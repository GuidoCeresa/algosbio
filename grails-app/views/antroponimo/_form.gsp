<%@ page import="it.algos.algosbio.Antroponimo" %>



<div class="fieldcontain ${hasErrors(bean: antroponimoInstance, field: 'nome', 'error')} required">
	<label for="nome">
		<g:message code="antroponimo.nome.label" default="Nome" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nome" required="" value="${antroponimoInstance?.nome}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: antroponimoInstance, field: 'voci', 'error')} required">
	<label for="voci">
		<g:message code="antroponimo.voci.label" default="Voci" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="voci" type="number" value="${antroponimoInstance.voci}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: antroponimoInstance, field: 'lunghezza', 'error')} required">
	<label for="lunghezza">
		<g:message code="antroponimo.lunghezza.label" default="Lunghezza" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="lunghezza" type="number" value="${antroponimoInstance.lunghezza}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: antroponimoInstance, field: 'isVocePrincipale', 'error')} ">
	<label for="isVocePrincipale">
		<g:message code="antroponimo.isVocePrincipale.label" default="Is Voce Principale" />
		
	</label>
	<g:checkBox name="isVocePrincipale" value="${antroponimoInstance?.isVocePrincipale}" />

</div>

<div class="fieldcontain ${hasErrors(bean: antroponimoInstance, field: 'voceRiferimento', 'error')} ">
	<label for="voceRiferimento">
		<g:message code="antroponimo.voceRiferimento.label" default="Voce Riferimento" />
		
	</label>
	<g:select id="voceRiferimento" name="voceRiferimento.id" from="${it.algos.algosbio.Antroponimo.list()}" optionKey="id" value="${antroponimoInstance?.voceRiferimento?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

