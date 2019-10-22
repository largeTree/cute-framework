<%@page import="com.qiuxs.cuteframework.core.basic.bean.Pair"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.utils.PageModelUtils"%>
<%@page import="com.qiuxs.cuteframework.core.basic.utils.JsonUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Field"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Search"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.DataList"%>
<%@page import="com.qiuxs.cuteframework.core.basic.Constants"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Td"%>
<%@page import="java.util.List"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Table"%>
<%@page import="com.qiuxs.cuteframework.view.pagemodel.Page"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据列表</title>
<%@ include file="include.jsp"%>
<%
	Page pageModel = (Page) request.getAttribute("pageModel");

	DataList dataList = pageModel.getDataList();
	Search search = dataList.getSearch();
	Table table = dataList.getTable();
	List<Td> tds = table.getTds();
%>
<script type="text/javascript">
	var apiKey = '${pageModel.dataList.apiKey }';
	var ctxPath = top.frm.getCtxPath();
	var tdBtns = <%
			String btnConfig = "[]";
			for (Td td : tds) {
				if (Td.TD_TYPE_BTN.equals(td.getType())) {
					btnConfig = JsonUtils.toJSONString(td.getBtns());
				}
			}
			out.print(btnConfig);
	%>;
	
	/** 触发搜索 */
	function doSearch(pageNo, pageSize) {
		pageNo = pageNo || 1;
		pageSize = pageSize || 20;
		var params = collectSearchParams();
		loadData(params, pageNo, pageSize);
	}

	/** 收集查询参数 */
	function collectSearchParams() {
		return frm.collectSearchParams('#searchFrom');
	}
	
	/** 加载数据 */
	function loadData(param, pageNo, pageSize) {
		frm.showLoading();
		param = param || {};
		param.pageNo = pageNo || 1;
		param.pageSize = pageSize || 20;
		$.post(ctxPath + '/api.do?apiKey=' + apiKey, param, function(data, status) {
			if ('success' === status && data.code === 0) {
				applyData(data.data.rows, data.data.total, param.pageNo, param.pageSize);
			} else {
				$.messager.alert('Error', data.msg, 'error');
			}
			frm.finishLoading();
		});
	}
	
	/** 将数据刷到页面上 */
	function applyData(dataList, total, pageNo, pageSize) {
		setPagination(total, pageNo, pageSize);
		// 填充数据
		var $dataTable = $('#data-list');
		var theads = $dataTable.find('thead td');
		var $tbody = $dataTable.children('tbody');
		$tbody.empty();
		var rowIdx = ((pageNo - 1) * pageSize) + 1;
		for (var i = 0;i < dataList.length; i++) {
			appendRow($tbody, dataList[i], theads, rowIdx++);
		}
	}
	
	// 设置分页栏
	function setPagination(total, pageNo, pageSize) {
		// 创建分页工具栏				
		$('#pp').pagination({
			total: total,
			pageSize: pageSize,
			pageNumber: pageNo,
			onSelectPage: function (pageNo, pageSize) {
				doSearch(pageNo, pageSize);
			},
			onChangePageSize: function(pageSize) {
				doSearch($(this).pagination('options').pageNumber, pageSize);
			}
		});
	}
	
	/** 追加一行 */
	function appendRow($tbody, row, theads, rowIdx) {
		var $tr = $(document.createElement('tr'));
		for (var i = 0;i < theads.length; i++) {
			var $td = $(document.createElement('td'));
			var $thead = $(theads[i]);
			var tType = $thead.attr('t-type');
			if($thead[0].hasAttribute('seq')) {
				$td.html(rowIdx);
			} else if (tType === '<%=Td.TD_TYPE_BTN %>') {
				for (var btn of tdBtns) {
					// [{"href":"from?pid=mylog&fromId=mylog&action=view","name":"查看详情","pk":"id"}]
					var $btn = $(document.createElement('a'));
					var pk = btn.pk ? row[btn.pk] : null;
					$btn.attr('onclick', "doProcess(event, '" + btn.name + "'," + (pk ? "'" + pk + "'" : null) + ", " + (btn.href ? "'" + btn.href + "'" : null) + ", " + (btn.apiKey ? "'" + btn.apiKey + "'" : null) + ", " + (btn.js ? "'" + btn.js + "'" : null) + ")");
					$btn.attr('href', 'javascript:void(0)');
					$btn.html(btn.name);
					$td.append($btn);
				}
			} else {
				var tLen = parseInt($thead.attr('t-len'));
				var tVal;
				if (tType === '<%=Td.TD_TYPE_CAPTION %>') {
					tVal = row['_caption'][$thead.attr('t-field')];
				} else {
					tVal = row[$thead.attr('t-field')];
				}
				if (tLen > 0 && tVal && tVal.length > tLen) {
					$td.attr('title', tVal);
					tVal = tVal.substring(0, tLen) + '...';
				}
				$td.html(tVal);
				$td.attr('style', $thead.attr('t-style'));
			}
			$tr.append($td);
		}
		$tbody.append($tr);
	}
	
	/** 单击行按钮时触发 */
	function doProcess(event, name, pk, href, apiKey, js) {
		if (href) {
			var id = new Date().getTime();
			top.frm.opWin(id, name, href, null, function() {
				var pageOptions = $('#pp').pagination('options');
				doSearch(pageOptions.pageNumber, pageOptions.pageSize);
			});
		} else if (apiKey) {
			
		} else if (js) {
			
		}
	}
	
	$(() => {
		doSearch();
	});
</script>
</head>
<body>
	<div style="padding: 5px; margin: 0px auto; width:99%; overflow: hidden;">
		<%
			List<Field> searchFields =  search.getFields();
			
			Pair<List<Field>, List<List<Field>>> groupedFields = PageModelUtils.groupFields(searchFields, 7);
		
			List<List<Field>> groupedSfs = groupedFields.getV2();
			List<Field> hiddenFields = groupedFields.getV1();
			
			List<Field> lastRow = groupedSfs.get(groupedSfs.size() - 1);
			
			// 添加查询按钮
			// 最后一行如果满了、则新增一行
			if (lastRow.size() == 7) {
				lastRow = new ArrayList<>();
				lastRow.add(Field.SEARCH_BTN);
				groupedSfs.add(lastRow);
			} else {
				// 没满直接添加在最后一行
				lastRow.add(Field.SEARCH_BTN);
			}
			
		%>
		<form id="searchFrom">
		<%
			for (Field f : hiddenFields) {
		%>
			<input type="hidden" name="<%=f.getName() %>" value="<%=f.getDefVal() %>" />
		<%
			}
		%>
		<table>
			<%
				for (List<Field> sfRow : groupedSfs) {
			%>
				<tr>
			<%
					for (Field f : sfRow) {
						if (!Field.TYPE_SEARCH_BTN.equals(f.getType())) {
			%>
						<td style="text-align: right;">
							<label for="<%=f.getName() %>"><%=f.getLabel() %>:</label>
						</td>
						<td>
							<%@ include file="fromField.jsp"%>
						</td>
			<%
						} else {
			%>
						<td style="text-align: right;"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()" >查询</a></td>
			<%
						}
					}
			%>
				</tr>
			<%
				}
			%>
			</table>
		</form>
	</div>
	
	<table id="data-list" class="my-table" width="99%">
		<thead>
			<tr>
				<td seq>序号</td>
				<%
					// 上方定义了tds
					for (int i = 0; i < tds.size(); i++) {
						Td td = tds.get(i);
				%>
					<td t-len="<%=td.getLength() %>" t-field="<%=td.getField() %>" t-sortable="<%=td.getOrder() %>" t-type="<%=td.getType() %>" t-style="<%
						StringBuilder hStyle = new StringBuilder();
						StringBuilder cStyle = new StringBuilder();
						switch (td.getType()) {
						case Td.TD_TYPE_HIDE:
							cStyle.append("display: none;");
							hStyle.append("display: none;");
							break;
						case Td.TD_TYPE_TEXT:
						case Td.TD_TYPE_CAPTION:
							cStyle.append("text-align: left;");
							break;
						case Td.TD_TYPE_NUM:
							cStyle.append("text-align: right;");
							break;
						case Td.TD_TYPE_DATE:
						case Td.TD_TYPE_DATETIME:
							cStyle.append("text-align: center;");
							break;
						}
						out.print(cStyle.toString());
					%>" style="<%=hStyle.toString()%>"><%=td.getName() %></td>
				<%
					}
				%>
			</tr>
		</thead>
		<tbody>
			
		</tbody>
		<tfoot>
			<tr>
				<td colspan="<%=tds.size()%>">
					<div id="pp" style="background: #efefef;"></div>
				</td>
			</tr>
		</tfoot>
	</table>
</body>
</html>