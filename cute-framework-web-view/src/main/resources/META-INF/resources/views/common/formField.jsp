<%@page import="com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator"%>
<%
				boolean inForm = request.getAttribute("formId") != null;

				Integer width = 110;
				Integer height = 21;

				String id = RandomGenerator.getRandomStr();
				
				switch(f.getType()) {
				case Field.TYPE_DATE:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" readonly>
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="frm.clearCombValue('<%=id %>')" />
					<script type="text/javascript">
						mycal=new dhtmlXCalendarObject('<%=id %>');
				   	 	mycal.setDateFormat("%Y-%m-%d");
				     	mycal.hideTime();
					</script>
<%
					break;
				case Field.TYPE_DATETIME:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" readonly>
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="frm.clearCombValue('<%=id %>')" />
					<%-- <script>
					  jQuery(function() {  jQuery("#<%=id %>").datetimepicker({  			format: "Y-m-d H:i:s" });   });   
					</script> --%>
					<script type="text/javascript">
						mycal=new dhtmlXCalendarObject('<%=id %>');
				   	 	mycal.setDateFormat("%Y-%m-%d %H:%i:%s");
					</script>
<%
					break;
				case Field.TYPE_SELECT:
%>
					<input id="<%=id %>" class="easyui-combobox" name="<%=f.getName() %>" data-options="width:<%=width %>,height:<%=height %>,url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
<%
					break;
				case Field.TYPE_RSSELECT:
%>
					<input id="<%=id %>" class="easyui-combobox" name="<%=f.getName() %>" data-options="width:100,height:16,mode:'remote',url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
<%
					break;
				case Field.TYPE_RMSELECT:
%>
					<input id="<%=id %>" class="easyui-combobox" name="<%=f.getName() %>" data-options="width:100,height:16,mode:'remote',multiple:true,url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
<%
					break;
				case Field.TYPE_TEXTAREA:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" />
<%
					break;
				case Field.TYPE_TEXT:
					default:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" />
<%
				}
%>