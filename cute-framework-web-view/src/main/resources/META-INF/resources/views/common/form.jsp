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
	Page pageModel = (Page) request.getAttribute("pageModel");
	String formId = (String) request.getAttribute("formId");
	FormModel form = pageModel.getForm(formId);
%>
</head>
<body>
	<%
			List<Field> fields =  form.getFields();
			Pair<List<Field>, List<List<Field>>> pair = PageModelUtils.groupFields(fields, 3);
			List<Field> hiddenFields = pair.getV1();
			List<List<Field>> groupedFields = pair.getV2();
	%>
	<form id="form" >
		<%
			for (Field f : hiddenFields) {
		%>
				<input type="hidden" name="<%=f.getName() %>" value="<% if (f.getDefVal() != null) { out.print(f.getDefVal()); }%>" 
		<%
			}
			if (groupedFields.size() > 0) {
		%>
		<table width="100%" >
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
					<td <% if (f.getSingleRow() == Constants.TRUE) { out.print("colspan=\"5\""); } %>>
						<%@ include file="formField.jsp"%>
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
	</form>
	
</body>
</html>
