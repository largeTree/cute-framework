<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.qiuxs.cuteframework.core.context.EnvironmentContext"%>
<%
	String ctxPath = request.getContextPath();
	String theme = (String)request.getAttribute("_theme");
	if (theme == null) {
		theme = "default";
	}
	String version = EnvironmentContext.getVersion();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/jq/jquery.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/common.js?ver=<%=version %>"></script>

<style type="text/css">
	#log-table {
		border-bottom: 1px solid #CCC;
		font-size: 12px;
	} 
	#log-table tr td {
		border-left: 1px solid #CCC;
		border-top: 1px solid #CCC;
	}
	#log-table tr td:last-child {
		border-right: 1px solid #CCC;
	}
	td {
		padding: 5px 10px;
	}
	.td-label {
		text-align: right;
		width: 10%;
		font-weight: 600;
	}
	pre { 
		white-space:pre-wrap;/*css-3*/ 
		white-space:-moz-pre-wrap;/*Mozilla,since1999*/ 
		white-space:-pre-wrap;/*Opera4-6*/ 
		white-space:-o-pre-wrap;/*Opera7*/ 
		word-wrap:break-word;/*InternetExplorer5.5+*/　　
		background-color:#CCCCCC;
	} 
</style>
</head>
<body style="padding: 0px;">
	<table id="log-table" width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="td-label">客户端IP</td>
			<td id="ip"></td>
			<td class="td-label">用户ID</td>
			<td id="userId"></td>
			<td class="td-label">服务器ID</td>
			<td id="serverId"></td>
			<td class="td-label">日志级别</td>
			<td id="level"></td>
		</tr>
		<tr>
			<td class="td-label">线程ID</td>
			<td id="threadId" colspan="2"></td>
			<td class="td-label">全局流水号</td>
			<td id="globalId" colspan="2"></td>
			<td class="td-label">错误代码</td>
			<td id="errorCode"></td>
		</tr>
		<tr>
			<td class="td-label">类名</td>
			<td id="className" colspan="2"></td>
			<td class="td-label">方法</td>
			<td id="method"></td>
			<td class="td-label">时间</td>
			<td id="logTime" colspan="2"></td>
		</tr>
		<tr>
			<td class="td-label">内容</td>
			<td colspan="7"><pre id="msg" ></pre></td>
		</tr>
		<tr>
			<td class="td-label">Throwable</td>
			<td colspan="7"><pre id="throwable"></pre></td>
		</tr>
		<tr>
			<td class="td-label">StackTrace</td>
			<td colspan="7"><pre id="stacktrace"></pre></td>
		</tr>
	</table>
</body>
<script type="text/javascript">
	$(()=>{
		top.frm.postApi('mylog-get', { id: '<%=request.getParameter("pk") %>' }).then((data) => {
			data = data.data;
			for (var key in data) {
				var val = data[key];
				$('#' + key).html(val);
			}
		}, (ext) => {
			console.log(data);
		});
	});
</script>
</html>