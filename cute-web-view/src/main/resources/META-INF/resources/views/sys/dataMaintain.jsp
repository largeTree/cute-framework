<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>数据维护</title>
<%@ include file="../common/include.jsp"%>
<script type="text/javascript">
var ctxPath = top.frm.getCtxPath();
function exec(apiKey) {
	top.frm.showLoading();
	frm.postApi(apiKey, {}, {}).then(function(data) {
		alert(data.msg);
		top.frm.finishLoading();
	}, function(data) {
		top.frm.finishLoading();
	});
}
</script>
</head>
<body>
	<ul style="list-style: none;">
		<li>
			初始化权限数据：<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="exec('func-initFunc')">执&nbsp;&nbsp;行</a>
		</li>
	</ul>
</body>
</html>