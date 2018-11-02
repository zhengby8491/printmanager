<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<meta charset="UTF-8">
<title>选择收款源</title>
<script type="text/javascript">
	var currDataType = "SALE";
	$(function()
	{
		//查询
		$("#btn_search").click(function()
		{
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//销售对账列表
		$("#sale").click(function()
		{
			$("#bootTable").bootstrapTable('showColumn', 'customerMaterialCode');
			$("#bootTable").bootstrapTable('showColumn', 'unitId');
			$("#bootTable").bootstrapTable('showColumn', 'productName');
			$("#bootTable").bootstrapTable('showColumn', 'style');
			$("#bootTable").bootstrapTable('hideColumn', 'oemProductName');
			$("#bootTable").bootstrapTable('hideColumn', 'oemStyle');
			$("#bootTable").bootstrapTable('hideColumn', 'procedureName');
			$("#customer,#oem").css("color", "");
			$(this).css("color", "red");
			var _url = Helper.basePath + "/quick/receive_list_sale";
			$("#bootTable").bootstrapTable("refreshOptions", {
				url : _url,
				pageNumber : 1
			});
		});
		//代工对账列表
		$("#oem").click(function()
		{
			$("#bootTable").bootstrapTable('hideColumn', 'customerMaterialCode');
			$("#bootTable").bootstrapTable('hideColumn', 'unitName');
			$("#bootTable").bootstrapTable('hideColumn', 'productName');
			$("#bootTable").bootstrapTable('showColumn', 'oemProductName');
			$("#bootTable").bootstrapTable('showColumn', 'procedureName');
			$("#bootTable").bootstrapTable('hideColumn', 'style');
			$("#bootTable").bootstrapTable('showColumn', 'oemStyle');
			$("#sale,#customer,#financeAdjust").css("color", "");
			$(this).css("color", "red");
			var _url = Helper.basePath + "/quick/receive_list_oem";
			$("#bootTable").bootstrapTable("refreshOptions", {
				url : _url,
				pageNumber : 1
			});
		});
		//客户期初列表
		$("#customer").click(function()
		{
			$("#bootTable").bootstrapTable('hideColumn', 'customerMaterialCode');
			$("#bootTable").bootstrapTable('hideColumn', 'unitName');
			$("#bootTable").bootstrapTable('hideColumn', 'productName');
			$("#bootTable").bootstrapTable('hideColumn', 'style');
			$("#bootTable").bootstrapTable('hideColumn', 'oemProductName');
			$("#bootTable").bootstrapTable('hideColumn', 'oemStyle');
			$("#bootTable").bootstrapTable('hideColumn', 'procedureName');
			$("#sale,#oem,#financeAdjust").css("color", "");
			$(this).css("color", "red");
			var _url = Helper.basePath + "/quick/receive_list_customerBegin";
			$("#bootTable").bootstrapTable("refreshOptions", {
				url : _url,
				pageNumber : 1
			});
		});
		
		//财务调整单列表
		$("#financeAdjust").click(function()
		{
			$("#bootTable").bootstrapTable('hideColumn', 'customerMaterialCode');
			$("#bootTable").bootstrapTable('hideColumn', 'unitName');
			$("#bootTable").bootstrapTable('hideColumn', 'productName');
			$("#bootTable").bootstrapTable('hideColumn', 'style');
			$("#bootTable").bootstrapTable('hideColumn', 'oemProductName');
			$("#bootTable").bootstrapTable('hideColumn', 'oemStyle');
			$("#bootTable").bootstrapTable('hideColumn', 'procedureName');
			$("#sale,#oem,#customer").css("color", "");
			$(this).css("color", "red");
			var _url = Helper.basePath + "/quick/receive_list_financeAdjust";
			$("#bootTable").bootstrapTable("refreshOptions", {
				url : _url,
				pageNumber : 1
			});
		});
		
		//选择确认
		$("#btn_ok").click(function()
		{
			parent.getCallInfo_receiveArray($("#bootTable").bootstrapTable('getAllSelections'));
			Helper.popup.close();
		});

		$("#btn_cancel").click(function()
		{
			Helper.popup.close();
		});
		
		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/quick/receive_list_sale",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : [ 10, 20, 50 ],
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,//参数
			responseHandler : responseHandler,

			//showColumns : true, //是否显示所有的列
			//minimumCountColumns : 2, //最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 360, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			checkbox : $("#multiple").val().toBoolean(),//开启多选
			uniqueId : "id",//定义列表唯一键
			//resizable : true, //是否启用列拖动
			columns : [ {
				field : 'state',
				checkbox : true,
				visible : $("#multiple").val().toBoolean(),
				width : 40
			}, {
				field : 'master_createTime',
				title : '单据日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.createTime)
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'master_billNo',
				title : '源单单号',
				width : 130,
				formatter : function(value, row, index)
				{
					return row.master.billNo
				}
			}, {
				field : 'master_billType',
				title : '源单类型',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.master.billTypeText;
				}
			}, {
				field : 'customerMaterialCode',
				title : '客户料号',
				width : 120
			}, {
				field : 'unitName',
				title : '单位',
				width : 60
			}, {
				field : 'oemProductName',
				title : '产品名称',
				visible : false,
				width : 140,
				formatter :function(value,row,index)
				{
					return row.productName;
				}
			}, {
				field : 'procedureName',
				title : '工序名称',
				visible : false,
				width : 140
			}, {
				field : 'productName',
				title : '成品名称',
				width : 140
			}, {
				field : 'oemStyle',
				title : '加工规格',
				visible : false,
				width : 100,
				formatter : function (value,row,index)
				{
					return row.style;
				}
			}, {
				field : 'style',
				title : '产品规格',
				width : 100
			}, {
				field : 'money',
				title : '应收款',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.billType == 'SALE_SK' || row.master.billType == 'OEM_EC')
					{
						return value;
					} else if (row.master.billType == 'FINANCE_ADJUST')
					{
						return row.adjustMoney;
					} else
					{
						return row.receiveMoney;
					}
				}
			}, {
				field : 'receiveMoney',
				title : '已收款金额',
				width : 90,
				formatter : function(value, row, index)
				{
					if (row.master.billType == 'SALE_SK' || row.master.billType == 'OEM_EC')
					{
						return toDecimal2(value);
					} else if (row.master.billType == 'FINANCE_ADJUST')
					{
						return toDecimal2(row.receiveOrPayMoney);
					} else
					{
						return toDecimal2(row.receivedMoney);
					}
				}
			}, {
				field : 'unReceiveMoney',
				title : '未收款金额',
				width : 90,
				formatter : function(value, row, index)
				{
					if (row.master.billType == 'SALE_SK' || row.master.billType == 'OEM_EC')
					{
						return toDecimal2(row.money - row.receiveMoney);
					} else if (row.master.billType == 'FINANCE_ADJUST')
					{
						return toDecimal2(row.adjustMoney - row.receiveOrPayMoney);
					} else
					{
						return toDecimal2(row.receiveMoney - row.receivedMoney);
					}
				}
			} ],
			onDblClickRow : function(row)
			{
				//双击选中事件	
				selectRow(row);
			}
		});
	});

	function selectRow(row)
	{
		if ($("#multiple").val().toBoolean())
		{//多选,则双击事件无效
			parent.getCallInfo_receiveArray([ row ]);
			return;
		} else
		{//单选，双击事件立即返回,处理返回index事件
			parent.getCallInfo_receiveSingle(row);
			Helper.popup.close();
		}
	}
	//请求参数
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['customerId'] = $("#customerId").val();
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
	//制保留2位小数，如：2，会在2后面补上00.即2.00    
	function toDecimal2(x)
	{
		var f = parseFloat(x);
		if (isNaN(f))
		{
			return false;
		}
		var f = Math.round(x * 100) / 100;
		var s = f.toString();
		var rs = s.indexOf('.');
		if (rs < 0)
		{
			rs = s.length;
			s += '.';
		}
		while (s.length <= rs + 2)
		{
			s += '0';
		}
		return s;
	}
</script>
</head>
<body>
	<input type="hidden" id="multiple" value="${multiple }">
	<input type="hidden" id="customerId" value="${customerId }">
	<div class="layer_container">
		<div class="cl layer_content">
			<!--分类选择-->
			<div class="tree">
				<ul>
					<li>
						<a href="javascript:void(0);" style="color: red" id="sale">销售对账单</a>
					</li>
					<shiro:hasPermission name="oem:reconcil:list">
					<li>
						<a href="javascript:void(0);" id="oem">代工对账单</a>
					</li>
					</shiro:hasPermission>
					<li>
						<a href="javascript:void(0);" id="customer">客户期初</a>
					</li>
					<li>
						<a href="javascript:void(0);" id="financeAdjust">财务调整单</a>
					</li>
				</ul>
			</div>
			<!--表格容器左START-->
			<div class="layer_table_container" style="width: 1010px">
				<!-- 查询栏 -->
				<div class="cl layer_top">
					<div class="row-dd" style="margin-left: 5px">
						<label class="form-label label_ui"></label>
						<span class="ui-combo-wrap" class="form_text">
							<sys:dateConfine label="单据日期" />
							<input type="text" class="input-txt input-txt_9" id="billNo" name="billNo" value="${billNo }" placeholder="源单单号" />
						</span>
						<div class="layer_btns">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查找
							</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_ok">确认</button>
							<button type="button" class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
						</div>
					</div>
				</div>
				<!-- 表格 -->
				<div class="table-wrap" style="height: 390px;">
					<table class="layer_table" id="bootTable">

					</table>
				</div>
			</div>
			<!--表格容器左END-->
		</div>
	</div>
</body>
</html>
