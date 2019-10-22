<%@page import="com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator"%>
<%
				boolean inForm = request.getAttribute("formId") != null;

				int width = inForm ? 340 : 140;
				int height = 24;
				
				String id = RandomGenerator.getRandomStr();
				switch(f.getType()) {
				case Field.TYPE_DATE:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" class="easyui-datebox" data-options="editable:false,width:<%=width %>,height:<%=height %>">
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="frm.clearCombValue('<%=id %>')" />
			
<%
					break;
				case Field.TYPE_DATETIME:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" class="easyui-datetimebox" data-options="editable:false,width:<%=width %>,height:<%=height %>">
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="frm.clearCombValue('<%=id %>')" />
<%
					break;
				case Field.TYPE_SELECT:
%>
					<input id="<%=id %>" class="easyui-combobox" name="<%=f.getName() %>" data-options="width:<%=width %>,height:<%=height %>,url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
<%
					break;
				case Field.TYPE_RSSELECT:
%>
					<input id="<%=id %>" class="easyui-combobox" name="<%=f.getName() %>" data-options="width:<%=width %>,height:<%=height %>,mode:'remote',url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
<%
					break;
				case Field.TYPE_RMSELECT:
%>
					<input id="<%=id %>" class="easyui-combobox" name="<%=f.getName() %>" data-options="width:<%=width %>,height:<%=height %>,mode:'remote',multiple:true,url:'<%=ctxPath%>/api.do?apiKey=qd-codes&codeDomain=<%=f.getCode()%>',valueField:'code',textField:'caption'">
<%
					break;
				case Field.TYPE_TEXTAREA:
%>
					<input id="<%=id %>" class="easyui-textbox" name="<%=f.getName() %>" data-options="multiline:true,width:<%=f.getCols() * height %>,height:<%=f.getRows() * height %>" />
<%
					break;
				case Field.TYPE_TEXT:
					default:
%>
					<input id="<%=id %>" class="easyui-textbox" name="<%=f.getName() %>" type="text"  data-options="width:<%=width %>,height:<%=height %>">
<%
				}
%>