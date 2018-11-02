<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>代工对账单明细</title>
<script type="text/javascript">
	$(function()
	{
		/* 判断当前用户是否有权限查看金额 */
		var hasPermission = Helper.basic.hasPermission('oem:reconcil:detail:money');
		/* 选择客户 */
		$("#customer_quick_select").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '500');
		});
		/* 选择工序 */
		$("#procedure_quick_select").click(function()
		{
			Helper.popup.show('选择工序', Helper.basePath + '/quick/procedure_select?multiple=false', '900', '490');
		});
		/* 选择产品 */
		$("#product_quick_select").click(function()
		{
			Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false', '900', '490');
		});

		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
			$("#more_div2").toggle();
		});

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
			url : Helper.basePath + "/oem/reconcil/ajaxDetailList",
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

			//resizable: true, //是否启用列拖动
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
			cookieIdTable : "print_oem_reconcil_detail",//必须制定唯一的表格cookieID

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
				field : 'createTime',
				title : '制单日期',
				width : 80,
				formatter : function(value, row, index)
				{
					if (row.master.createTime)
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'deliveryTime',
				title : '送/退货日期',
				width : 100,
				formatter : function (value, row, index)
				{
					if (value)
					{
						return new Date(value).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'billNo',
				title : '对账单号',
				width : 140,
				formatter: function(value, row, index)
        {
          return idTransToUrl(row.master.id, row.master.billNo);
        }
			}, {
				field : 'oemOrderBillNo',
				title : '代工单号',
				width : 120,
				formatter : function(value, row, index)
				{
				  return idTransToUrl(row.oemOrderBillId, value);
				}
			}, {
				field : 'sourceBillNo',
				title : '源单单号',
				width : 120,
				formatter : function(value,row,index)
				{
					return idTransToUrl(row.sourceId,value);
				}
			}, {
				field : 'customerName',
				title : '客户名称',
				width : 180,
				formatter : function(value, row, index)
				{
					return row.master.customerName;
				}
			}, {
				field : 'sourceBillTypeText',
				title : '源单类型',
				width : 80
			}, {
				field : 'employeeName',
				title : '销售人员',
				width : 80,
				formatter : function(value, row, index)
				{
					return row.master.employeeName;
				}
			}, {
				field : 'currencyTypeText',
				title : '币别',
				width : 60,
				formatter : function(value, row, index)
				{
					return row.master.currencyTypeText;
				}
			}, {
				field : 'productName',
				title : '成品名称',
				width : 120
			}, {
				field : 'procedureName',
				title : '工序名称',
				width : 120
			}, {
				field : 'style',
				title : '加工规格',
				width : 100
			}, {
				field : 'qty',
				title : '对账数量',
				width : 80
			}, {
				field : 'price',
				title : '单价',
				visible : hasPermission,
				width : 60
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermission,
				width : 100
			}, {
				field : 'receiveMoney',
				title : '已收款金额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'receiveMoney2',
				title : '未收款金额',
				visible : hasPermission,
				width : 80,
				formatter : function(value, row, index)
				{
					return (row.money - row.receiveMoney).toFixed(2);
				}
			}, {
				field : 'tax',
				title : '税额',
				visible : hasPermission,
				width : 80
			}, {
				field : 'noTaxMoney',
				title : '不含税金额',
				visible : hasPermission,
				width : 100
			}, {
				field : 'partName',
				title : '部件名称',
				width : 80
			}, {
				field : 'processRequire',
				title : '加工要求',
				'class' : 'memoView',
				width : 200
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 200
			} ],
			onLoadSuccess : function(data)
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
			onDblClickRow : function(row)
			{
				if (row.id == null)
				{
					return;
				}
				var url = Helper.basePath + '/oem/reconcil/view/' + row.master.id;
				var title = "代工对账";
				admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			}
		});
		$("#bootTable").on('load-success.bs.table column-switch.bs.table page-change.bs.table search.bs.table', function()
		{
			/* 表格工具栏 */
			if ($(".glyphicon-th").next().html() == '')
			{
				$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				if (!hasPermission)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=15]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=16]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=17]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=18]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=19]").parent().parent().remove();
				}

				if (!Helper.basic.hasPermission('oem:reconcil:detail:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});

	});

	// 获取返回信息
	function getCallInfo_customer(obj)
	{
		$("#customerName").val(obj.name);
	}
	// 获取返回信息
	function getCallInfo_procedure(obj)
	{
		$("#procedureName").val(obj.name);
	}
	//获取返回信息
	function getCallInfo_product(obj)
	{
		$("#productName").val(obj.name);
	}

	//请求参数
	function queryParams(params)
	{
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['billNo'] = $("#billNo").val();
		params['customerName'] = $("#customerName").val().trim();
		params['procedureName'] = $("#procedureName").val().trim();
		params['productName'] = $("#productName").val().trim();
		params['auditFlag'] = $("input[name='auditFlag']:checked").val() == "-1" ? null : $("input[name='auditFlag']:checked").val();

		params['sourceBillNo'] = $("#sourceBillNo").val().trim();
		params['oemOrderBillNo'] = $("#oemOrderBillNo").val().trim();
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
			<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
				<div id="innerdiv" style="position: absolute;">
					<img id="bigimg" style="border: 5px solid #fff;" src="" />
				</div>
			</div>
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">对账单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="customerName" name="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3s" id="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div2">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">源单单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_13" name="sourceBillNo" id="sourceBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">代工单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="oemOrderBillNo" id="oemOrderBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="productName" name="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
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