<%@page import="com.qiuxs.cuteframework.view.pagemodel.FormSection"%>
<%@page import="com.qiuxs.cuteframework.core.basic.Constants"%>
<%@page import="com.qiuxs.cuteframework.core.basic.bean.Pair"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.utils.PageModelUtils"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Field"%>
<%@page import="java.util.List"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.FormModel"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Page"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表单</title>
<%@ include file="include.jsp"%>
<%
	int rowFields = 4;
	
	Page pageModel = (Page) request.getAttribute("pageModel");
	String formId = (String) request.getAttribute("formId");
	String action = request.getParameter("action");
	
	FormModel form = pageModel.getForm(formId);
	String pk = request.getParameter("pk");
%>

<style type="text/css">
#bottom-bar {
	bottom: 0px;
	text-align: center;
	margin-top: 2em;
}
</style>

<script type="text/javascript">
top.frm.showLoading();
</script>
</head>
<body>
	<%
			List<Field> fields =  form.getFields();
			Pair<List<Field>, List<List<Field>>> pair = PageModelUtils.groupFields(fields, rowFields);
			List<Field> hiddenFields = pair.getV1();
			List<List<Field>> groupedFields = pair.getV2();
	%>
	<form id="form" >
		<table width="100%" class="form-section" id="root">
			<tr style="display: none;">
		<%
			for (Field f : hiddenFields) {
		%>
				<td><input type="hidden" class="my-input" id="root_<%=f.getName() %>" name="<%=f.getName() %>" value="<% if (f.getDefval() != null) { out.print(f.getDefval()); }%>" /></td>
		<%
			}
			if (groupedFields.size() > 0) {
		%>
			</tr>
		<%
				for (int i = 0; i < groupedFields.size(); i++) {
					List<Field> fs = groupedFields.get(i);
		%>
				<tr>
		<%
					for (int j = 0; j < fs.size(); j++) {
						Field f = fs.get(j);
		%>
					<td style="text-align: right;">
						<label for="<%=f.getName() %>"><%=f.getLabel() %>:</label>
					</td>
					<td <% if (f.getSingleRow() == Constants.TRUE) { out.print("colspan=\"7\""); } %>>
						<%@ include file="formField.jsp" %>
					</td>
		<%
					}
		%>
				</tr>
		<%
				}
			}
		%>
		</table>
		<!-- sections -->
		<%
			List<FormSection> sections = form.getSections();
			for (int i = 0; i < sections.size(); i++) {
				FormSection section = sections.get(i);
				request.setAttribute("formSection", section);
				pair = PageModelUtils.groupFields(section.getFields(), rowFields);
				hiddenFields = pair.getV1();
				groupedFields = pair.getV2();
		%>
				<p><%=section.getTitle() %></p>
				<table width="100%" class="form-section" id="<%=section.getKey() %>">
					<tr style="display: none;">
		<%
				for (int j = 0; j < hiddenFields.size(); j++) {
					Field f = hiddenFields.get(j);
		%>
					<input type="hidden" class="my-input" id="<%=section.getKey() + "_" + f.getName() %>" name="<%=f.getName() %>" value="<% if (f.getDefval() != null) { out.print(f.getDefval()); }%>" />
		<%
				}
		%>									
					</tr>
		<%
				for (int j = 0; j < groupedFields.size(); j++) {
					List<Field> fs = groupedFields.get(j);
		%>
					<tr>
		<%
					for (Field f : fs) {
		%>
						<td style="text-align: right;">
							<label for="<%=f.getName() %>"><%=f.getLabel() %>:</label>
						</td>
						<td <% if (f.getSingleRow() == Constants.TRUE) { out.print("colspan=\"7\""); } %>>
							<%@ include file="formField.jsp" %>
						</td>
		<%
					}
		%>
					</tr>
		<%
				}
		%>
				</table>
		<%
			}
		%>
	</form>
	<div id="bottom-bar">
		<a href="javascript:void(0)" onclick="doSave()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
	</div>
</body>
<script type="text/javascript">
	
	var formPage = {
			getApi: '<%=form.getGetApiKey() %>',
			saveApiKey: '<%=form.getSaveApiKey() %>',
			action: '<%=action %>',
			pk: '<%=pk %>'
	}
	
	function doSave() {
		top.frm.showLoading();
		
		var jsonParams = collectJsonParams();
		var params = {
				pk: formPage.pk
		}
		frm.postApi(formPage.saveApiKey, params, jsonParams).then(function(data) {
			alert(data.msg);
			top.frm.finishLoading();
		}, function (err) {
			var msg;
			if (err.data) {
				msg = err.data.msg;
			} else {
				msg = err.msg;
			}
			alert(msg);
			top.frm.finishLoading();
		});
	}
	
	function collectJsonParams() {
		var $formSections = $('.form-section');
		var params = {};
		for (var i = 0; i < $formSections.length; i++) {
			var $section = $($formSections[i]);
			var id = $section.attr('id');
			
			var node = {};
			if (id === 'root') {
				// 根节点
				node = params;
			} else {
				// 非根节点			
				params[id] = node;
			}
			
			var $fields = $('#' + id + ' .my-input');
			
			for (var j = 0; j < $fields.length; j++) {
				var $field = $($fields[j]);
				node[$field.attr('name')] = $field.val();
			}
			
		}
		return params;
	}

	if ('edit' === formPage.action) {
		frm.postApi(formPage.getApi, {id: formPage.pk}, {}).then(function(data) {
			var data = data.data;
			applyData(data);
			top.frm.finishLoading();
		}, function(err) {
			top.frm.finishLoading();
			console.error(err);
		});
	} else {
		top.frm.finishLoading();
	}
	
	function applyData(data) {
		var $formSections = $('.form-section');
		for (var i = 0; i < $formSections.length; i++) {
			var $section = $($formSections[i]);
			var id = $section.attr('id');
			var node;
			if (id === 'root') {
				node = data;
			} else {
				node = data[id];
			}
			if (!node || node == null) {
				node = {};
			}
			var $fields = $('#' + id + ' .my-input');
			
			for (var j = 0; j < $fields.length; j++) {
				var $field = $($fields[j]);
				var fieldName = $field.attr('name');
				$field.val(node[fieldName]);
			}
		}
	}
</script>
</html>
