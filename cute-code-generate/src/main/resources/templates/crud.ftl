<?xml version="1.0" encoding="UTF-8"?>
<root name="${desc!className}">
	<dataList apiKey="wb-${className?lower_case}-list">
		<buttons>
			<button id="1" text="新增" icon="el-icon-circle-plus-outline" formid="1" />
		</buttons>
		<search>
			<#list fields as field>
			<#if !field.ignoreEntity>
			<field label="${field.comment!field.name}" type="text" name="${field.name}" />
			</#if>
			</#list>
		</search>
		<table>
			<td name="id" field="id" type="hidden" />
			<#list fields as field>
			<#if !field.ignoreEntity>
			<td name="${field.comment!field.name}" field="${field.name}" type="text" />
			</#if>
			</#list>
			<td name="操作" type="btn">
				<btn id="2" name="修改" pk="id" formid="1" optype="edit" />
			</td>
		</table>
	</dataList>
	
	<form id="1" getApiKey="wb-${className?lower_case}-get" saveApiKey="wb-${className?lower_case}-save">
		<field label="id" type="hidden" name="id" />
		<#list fields as field>
		<#if !field.ignoreEntity>
		<field label="${field.comment!field.name}" type="text" name="${field.name}" />
		</#if>
		</#list>
	</form>
	
</root>