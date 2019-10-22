<%@page import="com.qiuxs.cuteframework.core.context.EnvironmentContext"%>
<%
	String ctxPath = request.getContextPath();
	String theme = (String)request.getAttribute("_theme");
	if (theme == null) {
		theme = "default";
	}
	String version = EnvironmentContext.getEnvContext().getVersion();
%>

<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/easyui/themes/<%=theme %>/easyui.css?ver=<%=version %>" />
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/easyui/themes/icon.css?ver=<%=version %>" />
<script type="text/javascript" src="<%=ctxPath %>/easyui/jquery.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/easyui/jquery.easyui.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/easyui/locale/easyui-lang-zh_CN.js?ver=<%=version %>"></script>

<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/common.css?ver=<%=version %>" />

<script type="text/javascript" src="<%=ctxPath %>/js/easyui-extend.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/tpl.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/common.js?ver=<%=version %>"></script>