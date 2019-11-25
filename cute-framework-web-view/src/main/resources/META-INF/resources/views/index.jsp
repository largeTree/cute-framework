<%@page import="java.util.ArrayList"%>
<%@page import="com.qiuxs.cuteframework.web.common.biz.func.service.IFuncService"%>
<%@page import="java.util.List"%>
<%@page import="com.qiuxs.cuteframework.core.basic.code.tree.TreeItem"%>
<%@page import="com.qiuxs.cuteframework.core.context.ApplicationContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="font-size: calc(100vw / 7.5)">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${appName}</title>

<%@ include file="common/include.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/index.css" />

<style type="text/css">
body {
	font-family: "Microsoft YaHei";
}
</style>
<script type="text/javascript">
	$(()=>{
		$(".left-menu").hover((_this) => {
			$(_this.currentTarget).children(".menu-panel").show();
		}, (_this) => {
			$(_this.currentTarget).children(".menu-panel").hide();
		});
		
	});
</script>

</head>
<body class="easyui-layout">
	<div id="my-mask">
		<img src="<%=ctxPath %>/imgs/common/loading.gif" />
	</div>
	<div data-options="region:'north'" style="height:12%;border-top: 0px;"></div>
	<!-- <div data-options="region:'south'" style="height:15%;"></div> -->
	<div data-options="region:'west',style:{overflow:'visible'},title:'菜单'" style="width:10%;text-align:right;border-right:0px;">
		<%
			IFuncService funcService = ApplicationContextHolder.getBean(IFuncService.class);
			List<TreeItem> funcTree = funcService.funcTree(null, true);
			for (TreeItem funcItem : funcTree) {
		%>
			<div class="left-menu"><%=funcItem.getName() %>
				<div class="menu-panel" style="padding-right: 20px;">
					<%
						List<TreeItem> children = funcItem.getChildren();
						for (TreeItem child : children) {
							List<TreeItem> menus = child.getChildren();
							List<List<TreeItem>> menuCols = new ArrayList<>();
							List<TreeItem> oneCol = new ArrayList<>();
							for (TreeItem ti : menus) {
								oneCol.add(ti);
								if (oneCol.size() == 30) {
									menuCols.add(oneCol);
									oneCol = new ArrayList<>();
								}
							}
							if (oneCol.size() > 0) {
								menuCols.add(oneCol);
							}
							for (int i = 0;i < menuCols.size();i++) {
								List<TreeItem> menuCol = menuCols.get(i);
					%>
						<div style="float: left;padding: 0px;">
							<p class="menu-title">
								<%
									if (i == 0) {
								%>
									<%=child.getName() %>
								<%
									} else {
								%>
										&nbsp;
								<%		
									}
								%>
							</p>
							<ul style="list-style: none;margin-top: 5px;padding-left: 15px;">
								<%
									for (TreeItem ti : menuCol) {
								%>
										<li class="menu-item" ><a href="javascript:void(0)" onclick="frm.opTab(null, '<%=ti.getName() %>', '<%=ti.getAttr().getString("page") %>')"><%=ti.getName() %></a></li>
								<%											
									}
								%>
							</ul>
						</div>
					<%
							}
						}
					%>
				</div>
			</div>
		<%
			}
		%>
	</div>
	<!-- <div data-options="region:'east'" ></div> -->
	<div id="mainTabs" class="easyui-tabs" data-options="region:'center'" style="border-top:0px;border-bottom:0px;border-right:0px;">
		 <div title="我的桌面" data-options="closable:false" >
		 	<iframe src="view/mydesktop" style="border: 0px;width: 100%;height: 100%"/>
	    </div>
	</div>
</body>
</html>