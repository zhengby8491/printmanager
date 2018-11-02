<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<title>送/退货单转未对账单</title>
<script type="text/javascript">
	$(function()
	{
		/* 判断当前用户是否有权限查看金额 */
		var hasPermisson = Helper.basic.hasPermission('oem:reconcil:transmit:money');
		/* 更多*/
		$("#btn_more").click(function()
		{
			$("#more_div").toggle();
		});

		/* 转对账*/
		$("#btn_transmit").click(function()
		{
			var rows = getSelectedRows();
			if (Helper.isNotEmpty(rows))
			{
				var customerId = "";
				var supplierArr = new Array();
				$(rows).each(function()
				{
					if (this.master)
					{
						customerId = this.master.customerId;
						if (!supplierArr.contains(customerId))
						{
							supplierArr.push(customerId);
						}
					}
				});
				if (supplierArr.length == 1)
				{//判断是否统一供应商
					var _paramStr = "";
					$(rows).each(function(index, item)
					{
						if (item.id)
						{
							if (item.master.billType == 'OEM_ED')
							{
								_paramStr = _paramStr + "&deliverIds=" + item.id;
							} else if (item.master.billType == 'OEM_ER')
							{
								_paramStr = _paramStr + "&returnIds=" + item.id;
							}
						}
					});
					var url = Helper.basePath + '/oem/reconcil/create?1=1&customerId=' + customerId + '&' + _paramStr;
					var title = "代工对账";
					admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
				} else
				{
					Helper.message.warn("请选择同一客户");
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
			var deliverIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'OEM_ED')
					return this.id;
				else
					return null;
			}).get();
			var returnIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'OEM_ER')
					return this.id;
				else
					return null;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/oem/transmit/completeDeliver', {
					"tableType" : "DETAIL",
					"deliverIds" : deliverIds,
					"returnIds" : returnIds
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
			var deliverIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'OEM_ED')
					return this.id;
				else
					return null;
			}).get();
			var returnIds = $(rows).map(function()
			{
				if (this.master && this.master.billType == 'OEM_ER')
					return this.id;
				else
					return null;
			}).get();
			if (Helper.isNotEmpty(rows))
			{
				Helper.post(Helper.basePath + '/oem/transmit/completeDeliverCancel', {
					"tableType" : "DETAIL",
					"deliverIds" : deliverIds,
					"returnIds" : returnIds
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

		//订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/oem/transmit/toReconcilList",
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
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,//是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "print_oem_transmit_reconcil",//必须制定唯一的表格cookieID

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
			}, {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 60
			}, {
				field : 'master_createTime',
				title : '制单日期',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.master.createTime))
					{
						return new Date(row.master.createTime).format("yyyy-MM-dd");
					}
				}
			}, {
				field : 'master_billType',
				title : '单据类型',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					if (Helper.isNotEmpty(row.master.billType))
					{
						return Helper.basic.getEnumText("com.huayin.printmanager.persist.enumerate.BillType", row.master.billType, "text");
					}
				}
			}, {
				field : 'master_billNo',
				title : '源单单号',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return idTransToUrl(row.masterId, row.master.billNo);
				}
			}, {
				field : 'oemOrderBillNo',
				title : '代工单号',
				width : 120,
				formatter : function(value, row, index)
				{
					return idTransToUrl(row.oemOrderBillId, value)
				}
			}, {
				field : 'master.customerName',
				title : '客户名称',
				width : 200,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					return row.master.customerName;
				}
			}, {
				field : 'productName',
				title : '成品名称',
				width : 120
			}, {
				field : 'procedureName',
				title : '工序名称',
				width : 100
			}, {
				field : 'style',
				title : '加工规格',
				width : 100
			}, {
				field : 'qty',
				title : '送/退货数量',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return value;
					}
					if (row.master.billType == "OEM_ER")
					{
						return "-" + value;
					} else
					{
						return value;
					}
				}
			}, {
				field : 'reconcilQty',
				title : '已对账数量',
				width : 120,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return value;
					}

					if (row.master.billType == "OEM_ER" && value != 0)
					{
						return "-" + value;
					} else
					{
						return value;
					}
				}
			}, {
				field : 'qty_reconcilQty',
				title : '未对账数量',
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return Number(row.qty).subtr(Number(row.reconcilQty));
					}
					if (row.master.billType == "OEM_ER")
					{
						return "-" + Number(row.qty).subtr(Number(row.reconcilQty));
					} else
					{
						return Number(row.qty).subtr(Number(row.reconcilQty));
					}

				}
			}, {
				field : 'price',
				title : '单价',
				visible : hasPermisson,
				width : 100
			}, {
				field : 'money',
				title : '金额',
				visible : hasPermisson,
				width : 100,
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return Number(Number(row.qty).subtr(Number(row.reconcilQty))).mul(Number(row.price));
					}
					if (row.master.billType == "OEM_ER")
					{
						if (!row.price)
						{
							return 0;
						} else
						{
							return "-" + Number(Number(row.qty).subtr(Number(row.reconcilQty))).mul(Number(row.price));
						}

					} else
					{
						return Number(Number(row.qty).subtr(Number(row.reconcilQty))).mul(Number(row.price));
					}

				}
			}, {
				field : 'partName',
				title : '部件名称',
				width : 100
			}, {
				field : 'processRequire',
				title : '加工要求',
				'class' : 'memoView',
				width : 100
			}, {
				field : 'memo',
				title : '备注',
				'class' : 'memoView',
				width : 100
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
				if (!hasPermisson)
				{
					$("div[title='列'] ul[role=menu]").find("input[value=13]").parent().parent().remove();
					$("div[title='列'] ul[role=menu]").find("input[value=14]").parent().parent().remove();
				}
				if (!Helper.basic.hasPermission('oem:reconcil:transmit:export'))
				{
					$(".export.btn-group").remove();
				}
				$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
			}
		});

	});
	function queryParams(params)
	{
		params['billNo'] = $("#billNo").val().trim();
		params['productName'] = $("#productName").val().trim();
		params['procedureName'] = $("#procedureName").val().trim();
		params['customerName'] = $("#customerName").val().trim();
		params['completeFlag'] = $("input[name='completeFlag']:checked").val().trim();
		params['createTimeMin'] = $("#dateMin").val().trim();
		params['createTimeMax'] = $("#dateMax").val().trim();
		params['oemOrderBillNo'] = $("#oemOrderBillNo").val().trim();
		return params;
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="代工管理-转单功能-送/退货单转对账单"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="oem:reconcil:create">
						<button class="nav_btn table_nav_btn" id="btn_transmit">生成代工对账单</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="oem:reconcil:transmit:complete">
						<button class="nav_btn table_nav_btn" id="btn_complete">强制完工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="oem:reconcil:transmit:completecancel">
						<button class="nav_btn table_nav_btn" id="btn_complete_cancel" style="display: none;">取消强制完工</button>
					</shiro:hasPermission>

				</div>
			</div>
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						</dd>
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">源单单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap ">
								<input type="text" class="input-txt input-txt_9" id="customerName" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="procedureName" />
								<div class="select-btn" id="procedure_quick_select">...</div>
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
							<label class="form-label label_ui">代工单号：</label>
							<span class="ui-combo-wrap ">
								<input type="text" class="input-txt input-txt_13" id="oemOrderBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">成品名称：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" id="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
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