<%@page import="com.qiuxs.cuteframework.core.basic.Constants"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.FormSection"%>
<%@page import="com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter" %>
<%@page import="com.qiuxs.cuteframework.core.basic.code.provider.DirectCodeHouse" %>
<%@page import="java.util.List" %>
<%@page import="com.qiuxs.cuteframework.core.basic.code.provider.CodeOption" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
				boolean inForm = request.getAttribute("formId") != null;
				FormSection _section = (FormSection) request.getAttribute("formSection");

				Integer width = 110;
				Integer height = 21;

				String id;
				if (_section != null) {
					id = _section.getKey() + "_" + f.getName();
				} else {
					id = f.getName();
				}
				
				switch(f.getType()) {
				case Field.TYPE_DATE:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" <% if (f.getRequired() == Constants.TRUE) {out.print("data-req=\"1\"");} %> type="text" placeholder="<%=f.getPlaceholder() %>" readonly class="my-input" >
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="$(this).prev().val('')" />
					<script type="text/javascript">
						mycal=new dhtmlXCalendarObject('<%=id %>');
				   	 	mycal.setDateFormat("%Y-%m-%d");
				     	mycal.hideTime();
					</script>
<%
					break;
				case Field.TYPE_DATETIME:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" <% if (f.getRequired() == Constants.TRUE) {out.print("data-req=\"1\"");} %> type="text" placeholder="<%=f.getPlaceholder() %>" class="my-input" >
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="$(this).prev().val('')" />
					<script type="text/javascript">
						mycal=new dhtmlXCalendarObject('<%=id %>');
				   	 	mycal.setDateFormat("%Y-%m-%d %H:%i:%s");
					</script>
<%
					break;
				case Field.TYPE_LIST:
					DirectCodeHouse<Object> codeHouse = DirectCodeCenter.getDirectCodeHouse(f.getCode());
					List<CodeOption<?>> options = codeHouse.getOptions();
%>
					<select id="<%=id %>" class="my-input" name="<%=f.getName() %>" <% if (f.getRequired() == Constants.TRUE) {out.print("data-req=\"1\"");} %>>
						<option value="" selected> - 请选择 - </option>
<%
						for (int _i = 0; _i < options.size(); _i++) {
							CodeOption<?> co = options.get(_i);
%>
							<option value="<%=co.getCode() %>"><%=co.getCaption() %></option>
<%
						}
%>
					</select>
<%
					break;
				case Field.TYPE_ACLIST:
%>
					<input id="<%=id %>" class="my-input" name="<%=f.getName() %>" <% if (f.getRequired() == Constants.TRUE) {out.print("data-req=\"1\"");} %> data-options="width:100,height:16,mode:'remote',url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
					<script type="text/javascript">
						frm.setAcList('<%=id %>', '<%=f.getCode()%>');
					</script>
<%
					break;
				case Field.TYPE_TEXTAREA:
%>
					<area id="<%=id %>" name="<%=f.getName() %>" <% if (f.getRequired() == Constants.TRUE) {out.print("data-req=\"1\"");} %> class="my-input" placeholder="<%=f.getPlaceholder() %>" />
<%
					break;
				case Field.TYPE_TEXT:
					default:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" <% if (f.getRequired() == Constants.TRUE) {out.print("data-req=\"1\"");} %> type="text" placeholder="<%=f.getPlaceholder() %>" class="my-input" />
<%
				}
%>