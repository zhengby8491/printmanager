<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>采购应付账款明细</title>
<script type="text/javascript">
	$(function()
	{

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/finance/unfinished/purchPaymentData",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			// pagination: true, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			// pageSize: 20,
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
			cookieIdTable : "print_finance_payment_purchShouldPayment",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [{
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'type',
				title : '源单类型',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.master.billTypeText;
				}
			}, {
				field : 'billNo',
				title : '源单单号',
				width : 100,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.master.id,row.master.billNo);
				}
			}, {
				field : 'orderBillNo',
				title : '采购单号',
				width : 140,
				formatter : function(value, row, index)
				{
					return billNoTransToUrl(value);
				}
			}, {
				field : 'reconcilTime',
				title : '对账日期',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return new Date(row.master.reconcilTime).format("yyyy-MM-dd");
				}
			}, {
				field : 'supplierName',
				title : '供应商名称',
				width : 200,
				formatter : function(value, row, index)
				{
					return row.master.supplierName;
				}
			}, {
				field : 'materialName',
				title : '材料名称',
				width : 100
			}, {
				field : 'specifications',
				title : '材料规格',
				width : 140
			}, {
				field : 'unitName',
				title : '单位',
				width : 80
			}, {
				field : 'qty',
				title : '数量',
				width : 60
			}, {
				field : 'price',
				title : '单价',
				width : 80
			}, {
				field : 'money',
				title : '金额',
				width : 80
			}, {
				field : 'paymentMoney',
				title : '已付款',
				width : 80
			}, {
				field : 'noPaymentMoney',
				title : '未付款',
				width : 80,
				formatter : function(value, row, index)
				{
					return Number(Number(row.money).subtr(Number(row.paymentMoney))).tomoney();
				}
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function()
			{
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				$("#bootTable tbody").find("tr:last").find("td:first").next().children("input[type='checkbox']").remove();
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
		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function () {
		/* 表格工具栏 */
		$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		if (!Helper.basic.hasPermission('finance:unfinishedpurchpayment:export'))
		{
			$(".export.btn-group").remove();
		}
		$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		});
		//生产付款单
		$("#btn_transmit").click(function()
		{
			var ids = "";
			var supplier = "";
			var flag = true;
			$.each($("#bootTable").bootstrapTable('getAllSelections'), function(i, value)
			{
				if (value.master.supplierId)
				{
					if (supplier && value.master.supplierId != supplier)
					{
						flag = false;
						return;
					}
					if (!supplier)
					{
						supplier = value.master.supplierId;
					}
					ids += "&ids=" + value.id;
				}
			});
			if (Helper.isNotEmpty(ids))
			{
				if (!flag)
				{
					Helper.message.warn("请选择同一供应商");
					return;
				}

				var url = Helper.basePath + '/finance/payment/create?1=1&billType=PURCH_PK&' + ids;
				var title = "付款单";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});

		//强制完工
		$("#btn_complete").on("click", function()
		{
			var rows = getSelectedRows();
			var ids = new Array();
			for (var i = 0; i < rows.length; i++)
			{
				ids.push(rows[i].id);
			}

			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/finance/unfinished/complete', {
					"tableType" : "DETAIL",
					"ids[]" : ids
				}, function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
					} else
					{
						Helper.message.warn(data.message);
					}
					$("#bootTable").bootstrapTable("refreshOptions", {
						pageNumber : 1
					});
				});
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});

		//取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var rows = getSelectedRows();
			var ids = new Array();
			for (var i = 0; i < rows.length; i++)
			{
				ids.push(rows[i].id);
			}

			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/finance/unfinished/complete_cancel', {
					"tableType" : "DETAIL",
					"ids[]" : ids
				}, function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
					} else
					{
						Helper.message.warn(data.message);
					}
					$("#bootTable").bootstrapTable("refreshOptions", {
						pageNumber : 1
					});
				});
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});
	});

	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val().trim();
		params['dateMax'] = $("#dateMax").val().trim();
		params['billNo'] = $("#billNo").val().trim();
		params['supplierName'] = $("#supplierName").val().trim();
		params['materialName'] = $("#materialName").val().trim();
		params['orderBillNo'] = $("#orderBillNo").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
		return params;
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-采购应付账明细"></sys:nav>
				</div>

				<div class="top_nav">
					<shiro:hasPermission name="finance:unfinished:purch_create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成付款单</button>
					</shiro:hasPermission>

					<shiro:hasPermission name="finance:unfinished:purch_complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>

					<shiro:hasPermission name="finance:unfinished:purch_cancel_complete">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">

						<dd class="row-dd">
							<sys:dateConfine label="对账日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">源单单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" name="billNo" type="text" class="input-txt input-txt_1" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">采购单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="orderBillNo" name="orderBillNo" type="text" class="input-txt input-txt_1" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="supplierName" name="supplierName" type="text" class="input-txt input-txt_8" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_8" id="materialName" name="materialName" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>

				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="YES" name="completeFlag" />
							已强制完工
						</label>
						<label>
							<input type="radio" value="NO" name="completeFlag" checked="checked" />
							未强制完工
						</label>
					</div>
				</div>
			</div>
			</form>
			<!--查询表单End-->
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>