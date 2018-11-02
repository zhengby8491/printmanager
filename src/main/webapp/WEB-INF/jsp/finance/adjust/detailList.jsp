<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>财务调整明细</title>
<script type="text/javascript">
	$(function()
	{
		/* 搜索*/
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		$("input[type='radio']").change(function()
		{
			$("#btn_search").click();
		})
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/finance/adjust/ajaxDetailList",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//resizable : true, //是否启用列拖动
			showColumns : true, //是否显示所有的列
			minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : true,//是否显示导出按钮
			//exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_finance_adjust_detail",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'isCheck',
				title : '单据状态',
				width : 70,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (row.master.isCheck == 'YES')
					{
						return '已审核';
					} else
					{
						return '未审核';
					}
				},
				cellStyle : function(value, row, index, field)
				{
					if (value == '未审核')
					{
						return {
							css : {
								"color" : "#f00"
							}
						};
					} else
					{
						return {
							css : {
								"color" : "#080"
							}
						};
					}
				}
			}, {
				field : 'adjustTime',
				title : '调整日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.adjustTime)
					{
						return new Date(row.master.adjustTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'adjustType',
				title : '调整类型',
				width : 120,
				formatter : function(value, row, index)
				{
					return row.master.adjustTypeTarget;
				}
			}, {
				field : 'billNo',
				title : '调整单号',
				width : 120,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.master.id, row.master.billNo);
				}
			}, {
				field : 'businessCode',
				title : '对象编号',
				width : 120
			}, {
				field : 'businessName',
				title : '调整对象',
				width : 120
			}, {
				field : 'adjustMoney',
				title : '调整金额',
				width : 100
			}, {
				field : 'reason',
				title : '调整事由',
				'class' : 'memoView',
				width : 200
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function()
			{
				//alert("数据加载完成");
				if ($(".glyphicon-th").next().html() == '')
				{
					$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
					// 控制筛选菜单金额选择,删除已隐藏字段的选项
					if (!Helper.basic.hasPermission('finance:adjust:export'))
					{
						$(".export.btn-group").remove();
					}
					$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
				}
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
			},
			onColumnSwitch : function(field, checked)
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for (var index = 0; index < _tds.length; index++)
				{
					if (_tds.eq(index).text() == "-")
					{
						_tds.eq(index).text('');
					}
				}
				
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			},
			onLoadError : function()
			{
				// alert("数据加载异常");
			},
			onDblClickRow : function(row)
			{
				if (row.id == null)
				{
					return;
				}
				var url = Helper.basePath + '/finance/adjust/view/' + row.master.id;
				var title = "财务调整单";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			}
		});
	});
	// 获取返回信息
	function getCallInfo_business(obj,businessType)
	{
		$("#businessName").val(obj.name);
		$("#businessType").val(businessType);
	}
	//请求参数
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['businessName'] = $("#businessName").val();
		if (''!= $("#adjustType").val())
		{
			params['adjustType'] = $("#adjustType").val();
		}
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();
		return params;
	}
	//ajax结果
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="调整日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调整类型：</label>
							<span class="ui-combo-wrap wrap-width">
								<phtml:list name="adjustType" textProperty="target" valueProperty="value" cssClass="input-txt input-txt_3 hy_select"
								 type="com.huayin.printmanager.persist.enumerate.AdjustType" defaultOption="全部"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调整对象：</label>
						<!-- 	<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="businessName"/>
								<div class="select-btn" id="business_quick_select">...</div>
							</span> -->
							<input type="text" class="input-txt input-txt_3" id="businessName"/>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调整单号：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_3s"id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未审核
						</label>
					</div>
				</div>
			</div>

			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格部分End-->

		</div>
	</div>
</body>
</html>