<%@page import="com.qiuxs.cuteframework.view.datamodel.DataSourceInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<%@ include file="../common/include.jsp"%>
<style type="text/css">
.warn-line {
	background-color: #FF9933;
}
</style>
<script type="text/javascript">
</script>
</head>
<body>
	<table width="98%" class="my-table">
		<thead>
			<tr>
				<td colspan="10" style="text-align: left;padding-left: 2%;">数据库状态</td>
			</tr>
			<tr>
				<td>链接ID</td>
				<td>关闭状态</td>
				<td>最大空闲数</td>
				<td>最大连接数</td>
				<td>最大空等待时间</td>
				<td>自动提交</td>
				<td>当前活动链接</td>
				<td>当前空闲链接</td>
				<td>驱动类名</td>
				<td>验证语句</td>
			</tr>
		</thead>
		<tbody>
			<%
				List<DataSourceInfo> dsInfos = (List<DataSourceInfo>)request.getAttribute("dsInfos");
				for (DataSourceInfo dsInfo : dsInfos) {
			%>
				<!-- 活动链接超过最大链接的90%时显示报警色 -->
				<tr class="<% if((dsInfo.getNumActive() / dsInfo.getMaxTotal()) > 0.9) {out.print("warn-line");} %>">
					<td><%=dsInfo.getDsId() %></td>
					<td><%=String.valueOf(dsInfo.getClosed()).toUpperCase() %></td>
					<td><%=dsInfo.getMaxIdle() %></td>
					<td><%=dsInfo.getMaxTotal() %></td>
					<td><%=dsInfo.getMaxWaitMillis() %>ms</td>
					<td><%=String.valueOf(dsInfo.getDefaultAutoCommit()).toUpperCase() %></td>
					<td><%=dsInfo.getNumActive() %></td>
					<td><%=dsInfo.getNumIdle() %></td>
					<td><%=dsInfo.getDriverClassName() %></td>
					<td><%=dsInfo.getValidationQuery() %></td>
				</tr>
			<%
				}
			%>
		</tbody>
	</table>
</body>
</html>