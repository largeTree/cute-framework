<%@page import="com.qiuxs.cuteframework.core.context.EnvironmentContext"%>
<%
	String ctxPath = request.getContextPath();
	String theme = (String)request.getAttribute("_theme");
	if (theme == null) {
		theme = "default";
	}
	String version = EnvironmentContext.getVersion();
%>

<!-- jquery -->
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/jq/jquery.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/jq/jquery.cookie.js?ver=<%=version %>"></script>

<!-- jquery easyui -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/thirdparts/easyui/themes/<%=theme %>/easyui.css?ver=<%=version %>" />
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/thirdparts/easyui/themes/icon.css?ver=<%=version %>" />
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/easyui/jquery.easyui.min.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/easyui/locale/easyui-lang-zh_CN.js?ver=<%=version %>"></script>

<!-- dhtmlx -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/thirdparts/dhtmlxSuite/dhtmlxCalendar/dhtmlxcalendar.css?ver=<%=version %>" />
<script type="text/javascript" src="<%=ctxPath %>/thirdparts/dhtmlxSuite/dhtmlxCalendar/dhtmlxcalendar.js?ver=<%=version %>"></script>

<!-- customer -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/common.css?ver=<%=version %>" />
<script type="text/javascript" src="<%=ctxPath %>/js/easyui-extend.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/tpl.js?ver=<%=version %>"></script>
<script type="text/javascript" src="<%=ctxPath %>/js/common.js?ver=<%=version %>"></script>