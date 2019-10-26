<%@page import="com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator"%>
<%
				boolean inForm = request.getAttribute("formId") != null;

				Integer width = 100;
				Integer height = 16;

				String id = RandomGenerator.getRandomStr();
				switch(f.getType()) {
				case Field.TYPE_DATE:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" >
					<img src="<%=ctxPath %>/imgs/common/clear.png" align="absmiddle" onclick="frm.clearCombValue('<%=id %>')" />
			
<%
					break;
				case Field.TYPE_DATETIME:
%>
					<input id="<%=id %>" name="<%=f.getName() %>" type="text" >
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