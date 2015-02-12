<%@ page import="it.algos.algosbio.Esclusi" %>



<div class="fieldcontain ${hasErrors(bean: esclusiInstance, field: 'pageid', 'error')} required">
	<label for="pageid">
		<g:message code="esclusi.pageid.label" default="Pageid" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="pageid" type="number" value="${esclusiInstance.pageid}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: esclusiInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="esclusi.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${esclusiInstance?.title}"/>

</div>

