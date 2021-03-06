<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js"></script>
<title>发外应付账款明细</title>
<script type="text/javascript">
	$(function()
	{
		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/finance/unfinished/outSourcePaymentData",
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
			cookieIdTable : "print_finance_payment_outSourceShouldPayment",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [{
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index) {
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
				field : 'workBillNo',
				title : '生产单号',
				width : 140,
				formatter : function(value, row, index)
				{
					return billNoTransToUrl(value);
				}
			}, {
				field : 'outSourceBillNo',
				title : '加工单号',
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
				title : '加工商名称',
				width : 200,
				formatter : function(value, row, index)
				{
					return row.master.supplierName;
				}

			}, {
				field : 'productName',
				title : '工序名称',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.type == 'PRODUCT')
					{
						return row.productName;
					} else if (row.type == 'PROCESS')
					{
						return row.procedureName;
					}
				}
			}, {
				field : 'style',
				title : '加工规格',
				width : 120
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
		if (!Helper.basic.hasPermission('finance:unfinishedoutsourcepayment:export'))
		{
			$(".export.btn-group").remove();
		}
		$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		});
		/* 转到付款单 */
		$("#btn_transmit").click(function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				var supplierId = "";
				var supplierArr = new Array();
				var billType = new Array();
				if (rows[rows.length - 1].master.billNo == null)
				{
					rows.splice(rows.length - 1, 1)//删除合计行数据
				}
				$(rows).each(function()
				{
					supplierId = this.master.supplierId;
					if (!supplierArr.contains(supplierId))
					{
						supplierArr.push(supplierId);
					}
					if (this.id)
					{

					} else
					{
						var type = (this.master.billNo).substring(0, 1);
					}
					if (!billType.contains(type))
					{
						billType.push(type);
					}
				});

				if (billType.length > 1)
				{
					Helper.message.warn("请选择同一种源单类型");
					return;
				}
				if (supplierArr.length == 1)
				{//判断是否统一供应商
					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						if (index == 0)
						{
							_paramStr = "ids=" + item.id;
						} else
						{
							_paramStr = _paramStr + "&ids=" + item.id;
						}
					});
					var url = Helper.basePath + '/finance/payment/create?billType=OUTSOURCE_OC&' + _paramStr;
					var title = "付款单";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));

				} else
				{
					Helper.message.warn("请选择同一供应商");
				}
			} else
			{
				Helper.message.warn("至少选择1项");
			}
		});
		//强制完工
		$("#btn_complete").on("click", function()
		{
			var rows = getSelectedRows();
			//          var ids=$(rows).map(function(){return this.id;}).get();
			var ids = new Array();
			for (var i = 0; i < rows.length; i++)
			{
				ids.push(rows[i].id);
			}

			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/finance/unfinished/outsource_complete', {
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
			//          var ids=$(rows).map(function(){return this.id;}).get();
			var ids = new Array();
			for (var i = 0; i < rows.length; i++)
			{
				ids.push(rows[i].id);
			}

			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/finance/unfinished/outsource_complete_cancel', {
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

		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!Helper.basic.hasPermission('trandsmit:deliver:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});
	});

	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val().trim();
		params['dateMax'] = $("#dateMax").val().trim();
		params['billNo'] = $("#billNo").val().trim();
		params['supplierName'] = $("#supplierName").val().trim();
		params['procedureName'] = $("#procedureName").val().trim();
		params['productName'] = $("#productName").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
		params['workBillNo'] = $("#workBillNo").val().trim();
		params['outSourceBillNo'] = $("#outSourceBillNo").val().trim();
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
					<c:if test="${fns:hasCompanyPermission('finance:unfinished:outsource_payment')}">
						<sys:nav struct="财务管理-转单功能-发外应付账款明细转付款单"></sys:nav>
					</c:if>

					<c:if test="${!fns:hasCompanyPermission('finance:unfinished:outsource_payment')}">
						<sys:nav struct="财务管理-转单功能-发外应付账款明细转付款单"></sys:nav>
					</c:if>

				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:unfinished:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成付款单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="finance:unfinished:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="finance:unfinished:complete_cancel">
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
							<label class="form-label label_ui label_6">加工商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="supplierName" name="supplierName" type="text" class="input-txt input-txt_8" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt" id="procedureName" name="procedureName" style="width: 60px" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">产品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_8" id="productName" name="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>

					<dl class="cl row-dl" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" id="workBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">加工单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_1" id="outSourceBillNo" />
							</span>
						</dd>
					</dl>
				</div>
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