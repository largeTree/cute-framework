<%@page import="com.qiuxs.cuteframework.core.context.EnvironmentContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆页面</title>
<%
	String ctxPath = request.getContextPath();
	String theme = (String)request.getAttribute("_theme");
	if (theme == null) {
		theme = "metro";
	}
	String version = EnvironmentContext.getVersion();
	String loginApi = request.getParameter("api");
	String redirect = request.getParameter("redirect");
%>

<script type="text/javascript" src="<%=ctxPath %>/thirdparts/jq/jquery.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/jq/jquery.cookie.js?ver=<%=version %>"></script>

<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/thirdparts/easyui/themes/<%=theme %>/easyui.css?ver=<%=version %>" />
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/thirdparts/easyui/themes/icon.css?ver=<%=version %>" />

<script type="text/javascript" src="<%=ctxPath %>/thirdparts/easyui/jquery.easyui.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/easyui/locale/easyui-lang-zh_CN.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/common.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/login.js?ver=<%=version %>"></script>

<style type="text/css">
	.my-input {
		border: #A1B7E5 solid 1px;
		width: 12em;
		height: 2em;
		border-radius: 3px;
		padding: 0px 0.5em;
	}
	.err-input {
		border: #FF3030 solid 1px;
	}
	.err-input:focus {
		outline-color: #FF3030;
	}
	#err {
		color: red;
		padding: 5px;
	}
</style>
<script type="text/javascript">

/** 登录页必须在最上层，如果不是在最上层的话，将最上层地址重新指定为当前地址 */
if (self != top) {
	top.location.href = self.location.href;
}

</script>
</head>
<body style="text-align: center;">
	<div id="dd" class="easyui-dialog" title="登陆" style="width:400px;height:200px;"
	    data-options="modal:true, shadow: false, closable: false">
	    <table width="100%" height="100%">
	    	<tr>
	    		<td style="text-align: right;width: 30%">账号：</td>
	    		<td style="width: 40%"><input id="uid" name="userName" type="text" class="my-input" onkeypress="onKeyPress(event)" onblur="unSetError(this)" placeholder="请输入账号" /></td>
	    		<td rowspan="2" id="err"></td>
	    	</tr>
	    	<tr>
	    		<td style="text-align: right;">密码：</td>
	    		<td style="width: 40%"><input id="pwd" name="pwd" type="password" class="my-input" onkeypress="onKeyPress(event)" onblur="unSetError(this)" placeholder="请输入密码" /></td>
	    	</tr>
	    	<tr>
	    		<td colspan="3" style="text-align: center;">
	    			<a id="login-btn" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="doLogin('<%=loginApi %>', '<%=redirect %>')">登陆</a>
	    		</td>
	    	</tr>
	    </table>
	</div>
</body>
</html>