<%@ page import="it.algos.algospref.Pref" %>



<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'ordine', 'error')} required">
	<label for="ordine">
		<g:message code="pref.ordine.label" default="Ordine" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ordine" type="number" value="${prefInstance.ordine}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="pref.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="type" from="${it.algos.algospref.Type?.values()}" keys="${it.algos.algospref.Type.values()*.name()}" required="" value="${prefInstance?.type?.name()}" />

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'code', 'error')} required">
	<label for="code">
		<g:message code="pref.code.label" default="Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="code" maxlength="40" required="" value="${prefInstance?.code}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'descrizione', 'error')} ">
	<label for="descrizione">
		<g:message code="pref.descrizione.label" default="Descrizione" />
		
	</label>
	<g:textArea name="descrizione" cols="40" rows="5" value="${prefInstance?.descrizione}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'stringa', 'error')} ">
	<label for="stringa">
		<g:message code="pref.stringa.label" default="Stringa" />
		
	</label>
	<g:textField name="stringa" value="${prefInstance?.stringa}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'bool', 'error')} ">
	<label for="bool">
		<g:message code="pref.bool.label" default="Bool" />
		
	</label>
	<g:checkBox name="bool" value="${prefInstance?.bool}" />

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'intero', 'error')} ">
	<label for="intero">
		<g:message code="pref.intero.label" default="Intero" />
		
	</label>
	<g:field name="intero" type="number" value="${prefInstance.intero}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'lungo', 'error')} ">
	<label for="lungo">
		<g:message code="pref.lungo.label" default="Lungo" />
		
	</label>
	<g:field name="lungo" type="number" value="${prefInstance.lungo}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'reale', 'error')} ">
	<label for="reale">
		<g:message code="pref.reale.label" default="Reale" />
		
	</label>
	<g:field name="reale" value="${fieldValue(bean: prefInstance, field: 'reale')}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'doppio', 'error')} ">
	<label for="doppio">
		<g:message code="pref.doppio.label" default="Doppio" />
		
	</label>
	<g:field name="doppio" value="${fieldValue(bean: prefInstance, field: 'doppio')}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'decimale', 'error')} ">
	<label for="decimale">
		<g:message code="pref.decimale.label" default="Decimale" />
		
	</label>
	<g:field name="decimale" value="${fieldValue(bean: prefInstance, field: 'decimale')}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'data', 'error')} ">
	<label for="data">
		<g:message code="pref.data.label" default="Data" />
		
	</label>
	<g:datePicker name="data" precision="day"  value="${prefInstance?.data}" default="none" noSelection="['': '']" />

</div>

<div class="fieldcontain ${hasErrors(bean: prefInstance, field: 'testo', 'error')} ">
	<label for="testo">
		<g:message code="pref.testo.label" default="Testo" />
		
	</label>
	<g:textArea name="testo" cols="40" rows="5" value="${prefInstance?.testo}"/>

</div>

