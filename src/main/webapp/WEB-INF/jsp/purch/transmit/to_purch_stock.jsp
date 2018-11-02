<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>订单转入库</title>
<script type="text/javascript">
	$(function()
	{
		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
		});

		$("#btn_transmit").click(function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				var supplierId = "";
				var supplierArr = new Array();
				$(rows).each(function()
				{
					if (this.master)
					{
						supplierId = this.master.supplierId;
						if (!supplierArr.contains(supplierId))
						{
							supplierArr.push(supplierId);
						}
					}
				});
				if (supplierArr.length == 1)
				{//判断是否统一供应商
					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						//console.log(index,item)
						if (item.id)
						{
							if (index == 0)
							{
								_paramStr = "ids=" + item.id;
							} else
							{
								_paramStr = _paramStr + "&ids=" + item.id;
							}
						}
					});
					var url = Helper.basePath + '/purch/stock/toStock?' + _paramStr;
					var title = "采购入库";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));

				} else
				{
					Helper.message.warn("请选择同一供应商");
				}
			} else
			{
				Helper.message.warn("请选择明细");
			}
		});
		//强制完工
		$("#btn_complete").on("click", function()
		{
			var rows = getSelectedRows();
			var ids = $(rows).map(function()
			{
				return this.id;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/purch/stock/complete', {
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
				Helper.message.warn("请选择订单");
			}
		});
		//取消强制完工
		$("#btn_complete_cancel").on("click", function()
		{
			var rows = getSelectedRows();
			var ids = $(rows).map(function()
			{
				return this.id;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/purch/stock/complete_cancel', {
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
				Helper.message.warn("请选择订单");
			}
		});

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/purch/transmit/to_purch_stock_list",
			method : "post",
			contentType : 'application/json', //设置请求头信息  
			dataType : "json",
			pagination : false, // 是否显示分页（*）
			sidePagination : 'server',//设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 20,
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
			cookieIdTable : "print_purch_transmit_stock",//必须制定唯一的表格cookieID

			uniqueId : "id",//定义列表唯一键
			columns : [ {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return index + 1;
				}
			},  {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return new Date(row.master.createTime).format("yyyy-MM-dd");
				}
			}, {
				field : 'deliveryTime',
				title : '交货日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return new Date(value).format("yyyy-MM-dd");
				}
			}, {
				field : 'purch_billNo',
				title : '采购单号',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return billNoTransToUrl(row.master.billNo);
				}
			}, {
				field : 'supplierName',
				title : '供应商名称',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return row.master.supplierName;
				}
			}, {
				field : 'materialName',
				title : '材料名称',
				width : 120
			}, {
				field : 'specifications',
				title : '材料规格',
				width : 120
			}, {
				field : 'weight',
				title : '克重',
				width : 80
			}, {
				field : 'purchUnitName',
				title : '单位',
				width : 80
			}, {
				field : 'qty',
				title : '采购数量',
				width : 60
			}, {
				field : 'storageQty',
				title : '已入库数量',
				width : 80
			}, {
				field : 'notStorageQty',
				title : '未入库数量',
				width : 80,
				formatter : function(value, row, index)
				{
					return Number(row.qty).subtr(Number(row.storageQty));
				}
			}, {
				field : 'sourceBillTypeText',
				title : '源单类型',
				width : 80
			}, {
				field : 'sourceBillNo',
				title : '生产单号',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return billNoTransToUrl(value);
				}
			}, {
				field : 'productNames',
				title : '成品名称',
				width : 100
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function(data)
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
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			}
		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!Helper.basic.hasPermission('transmit:to_purch_stock:export'))
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
		params['materialName'] = $("#materialName").val().trim();
		params['supplierName'] = $("#supplierName").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();

		params['sourceBillNo'] = $("#sourceBillNo").val().trim();
		params['productStyle'] = $("#style").val().trim();
		params['deliverDateMin'] = $("#deliverDateMin").val().trim();
		params['deliverDateMax'] = $("#deliverDateMax").val().trim();
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
					<sys:nav struct="采购管理-转单功能-订单转入库"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="purch:stock:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成入库单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="purch:stock:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>

					<shiro:hasPermission name="purch:stock:complete_cancel">
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
							<sys:dateConfine label="制单日期" initDate="true" dateMax="${dateMax }" dateMin="${dateMin }" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">采购单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" name="billNo" type="text" class="input-txt input-txt_3" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="supplierName" name="supplierName" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="materialName" type="text" class="input-txt input-txt_3" name="materialName" />
								<div class="select-btn" id="material_quick_select">...</div>
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
							<label class="form-label label_ui label_1">交货日期：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deliverDateMax\')}'})" class="input-txt input-txt_0 Wdate" id="deliverDateMin" name="deliverDateMin" value="">
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_2 " style="text-align: left; margin-left: -2px; width: 18px">至</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deliverDateMin\')}'})" class="input-txt input-txt_0 Wdate" id="deliverDateMax" name="deliverDateMax" value="">
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" id="sourceBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">&nbsp;材&nbsp;料&nbsp;规&nbsp;格：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_16" id="style" />
							</span>
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