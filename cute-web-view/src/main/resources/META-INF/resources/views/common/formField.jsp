<%@page import="com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
				boolean inForm = request.getAttribute("formId") != null;

				Integer width = 110;
				Integer height = 21;

				String id = RandomGenerator.getRandomStr();
				
				switch(f.getType()) {
				case Field.TYPE_DATE:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" readonly class="my-input" >
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
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" class="my-input" >
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="$(this).prev().val('')" />
					<%-- <script>
					  jQuery(function() {  jQuery("#<%=id %>").datetimepicker({  			format: "Y-m-d H:i:s" });   });   
					</script> --%>
					<script type="text/javascript">
						mycal=new dhtmlXCalendarObject('<%=id %>');
				   	 	mycal.setDateFormat("%Y-%m-%d %H:%i:%s");
					</script>
<%
					break;
				case Field.TYPE_LIST:
%>
					<select id="<%=id %>" class="my-input" name="<%=f.getName() %>">
						<option value="" selected> - 请选择 - </option>
					</select>
					<script type="text/javascript">
						frm.setList('<%=id %>', '<%=f.getCode()%>', '<%=f.getDefval() %>');
					</script>
<%
					break;
				case Field.TYPE_ACLIST:
%>
					<input id="<%=id %>" class="my-input" name="<%=f.getName() %>" data-options="width:100,height:16,mode:'remote',url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
					<script type="text/javascript">
						frm.setAcList('<%=id %>', '<%=f.getCode()%>');
					</script>
<%
					break;
				case Field.TYPE_TEXTAREA:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" class="my-input" />
<%
					break;
				case Field.TYPE_TEXT:
					default:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" class="my-input" />
<%
				}
%>