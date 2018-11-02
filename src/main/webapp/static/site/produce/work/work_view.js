var PURCH_ARR = new Array();
var STOCK_ARR = new Array();
var DELIVER_ARR = new Array();
var PRODUCT_IN_ARR = new Array();
$(function()
{
	// 格式化材料用量
	$("input[name='material.qty']").each(function()
	{
		$(this).val(Number($(this).val()).toString());
	});
	/* 部件详情显示/隐藏 */
	$(document).on("click", ".btn_toggle", function()
	{
		var nextTr = $(this).parents('.for_sel').next();
		if (nextTr.css('display') == 'none')
		{
			$(this).removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
			nextTr.show();
		} else if (nextTr.css('display') != 'none')
		{
			$(this).removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
			nextTr.hide();
		}
	})
	/* 返回显示列表 */
	$("#btn_back").on("click", function()
	{
		var url = Helper.basePath + '/produce/work/list';
		var title = "生产工单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	});

	/* 编辑 */
	$("#btn_edit").on("click", function()
	{
		location.href = Helper.basePath + '/produce/work/edit/' + $("#order_id").val();
	});
	/* 打印 */
	var url = '/produce/work/printAjax/'+ $("#order_id").val();
	$("#btn_print").loadTemplate('PRODUCE_MO', url);
	// 审核
	$("#btn_audit").on("click", function()
	{
		var order_id = $("#order_id").val();
		var order_billType = $("#order_billType").val();
		Helper.post(Helper.basePath + '/produce/work/audit/' + order_id, {
			"billType" : order_billType
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/produce/work/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	// 反审核
	$("#btn_audit_cancel").on("click", function()
	{
		var order_id = $("#order_id").val();
		var order_billType = $("#order_billType").val();
		Helper.post(Helper.basePath + '/produce/work/audit_cancel/' + order_id, {
			"billType" : order_billType
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/produce/work/view/" + order_id;
			} else
			{
				if (data.obj)
				{
					var msg = "";
					var index = 1;
					$.each(data.obj, function(i, n)
					{
						msg += (index++) + ".反审核失败,已被下游单据引用：<br/>"
						$.each(n, function(k, j)
						{

							if (i == 'PO')
							{
								msg += '&emsp;&emsp;采购订单<a href="javascript:;" onclick="order_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'IV')
							{
								msg += '&emsp;&emsp;销售送货单<a href="javascript:;" onclick="deliver_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'IS')
							{
								msg += '&emsp;&emsp;成品入库单<a href="javascript:;" onclick="in_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'MR')
							{
								msg += '&emsp;&emsp;生产领料单<a href="javascript:;" onclick="take_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == 'OP')
							{
								msg += '&emsp;&emsp;发外加工单<a href="javascript:;" onclick="process_view(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == "DY")
							{
								msg += '&emsp;&emsp;产量上报单<a href="javascript:;" onclick="work_report(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == "RM")
							{
								msg += '&emsp;&emsp;生产退料单<a href="javascript:;" onclick="stockmaterial_return(' + j.id + ')">' + j.billNo + '</a><br/>';
							}
							if (i == "SM")
							{
								msg += '&emsp;&emsp;生产补料单<a href="javascript:;" onclick="stockmaterial_supplement(' + j.id + ')">' + j.billNo + '</a><br/>';
							}

						});
					});
					Helper.message.view(msg);
				} else
				{
					Helper.message.warn(data.message);
				}
			}
		});
	});

	// 强制完工
	$("#btn_complete").on("click", function()
	{
		var order_id = $("#order_id").val();
		Helper.post(Helper.basePath + '/produce/work/complete', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/produce/work/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	// 取消强制完工
	$("#btn_complete_cancel").on("click", function()
	{
		var order_id = $("#order_id").val();
		Helper.post(Helper.basePath + '/produce/work/complete_cancel', {
			"tableType" : "MASTER",
			"ids" : [ order_id ]
		}, function(data)
		{
			if (data.success)
			{
				Helper.message.suc(data.message);
				location.href = Helper.basePath + "/produce/work/view/" + order_id;
			} else
			{
				Helper.message.warn(data.message);
			}
		});
	});
	/* 删除 */
	$("#btn_del").on("click", function()
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			Helper.post(Helper.basePath + '/produce/work/del/' + $("#order_id").val(), function(data)
			{
				if (data.success)
				{
					Helper.message.suc(data.message);
					closeTabAndJump("生产工单列表");
					/* location.href=Helper.basePath+"/produce/work/list"; */
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});
	});
	/* 翻单 */
	$("#btn_turning").on("click", function()
	{
		var url = Helper.basePath + '/produce/work/create?billType=PRODUCE_TURNING&sourceWorkId=' + $("#order_id").val();
		var title = "生产工单";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	/* 补单 */
	$("#btn_supplement").on("click", function()
	{
		var url = Helper.basePath + '/produce/work/create?billType=PRODUCE_SUPPLEMENT&sourceWorkId=' + $("#order_id").val();
		var title = "生产工单";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	/* 去掉成品工序-工序最后一个元素的箭头 */
	$(".cp_procedure").find("span:last-child").find(".fa-long-arrow-right").remove();
	var shadeBl = true
	var state = '';
	// 单独编辑材料，编辑工序功能
	$('.mp_change ul li').click(function()
	{
		state = $(this).attr('data-state');

		location.href = Helper.basePath + '/produce/work/edit/' + $("#order_id").val() + '?state=' + state;

	})
	// 判断生成采购订单按钮的显示
	purchBtn();
	// 判断生成领料按钮的显示
	takeMaterialBtn();
	// 判断生成送货按钮的显示
	deliverBtn();
	// 判断生成成品入库按钮的显示
	productiInBtn();

	// 生成采购订单
	$("#transmitToPuch").on("click", function()
	{
		transmitToPuch();

	});
	// 生成领料单
	$("#transmitToTakeMaterial").on("click", function()
	{
		transmitToTake();
	});
	// 生成销售送货
	$("#transmitToDeliver").on("click", function()
	{
		transmitToDeliver();
	});
	// 生产成品入库
	$("#transmitToProductIn").on("click", function()
	{
		transmitToProductIn();
	});

});
/* 判断生成采购订单按钮的显示 */
function purchBtn()
{
	$.each($(".unshadeMaterial .material-class"), function(i, val)
	{
		var $this = $(this);
		var purchQty = Number($this.find("input[name='materialPurchQty']").val());
		var qty = Number($this.find("input[name='material.qty']").val());
		var id = $this.find("input[name='materId']").val();
		var isCustPaper = ($this.find("input[name='isCustPaper']").attr("checked") == "checked") ? true : false;

		if (purchQty < qty && !isCustPaper)
		{ // 已采购数量小于材料用量，则添加进生成采购菜单里
			PURCH_ARR.push(id);
		}
	});

	if (PURCH_ARR.length == 0)
	{
		$("#transmitToPuch").hide();
	}
}
/* 判断生成领料按钮的显示 */
function takeMaterialBtn()
{
	$.each($(".unshadeMaterial .material-class"), function(i, val)
	{
		var $this = $(this);
		var qty = Number($this.find("input[name='material.qty']").val());
		var takeQty = Number($this.find("input[name='materialTakeQty']").val());
		var isCustPaper = ($this.find("input[name='isCustPaper']").attr("checked") == "checked") ? true : false;
		var id = $this.find("input[name='materId']").val();
		// 首先是非来纸材料，其次是未领料
		if (takeQty < qty && !isCustPaper)
		{
			STOCK_ARR.push(id);
		}
	});
	if (STOCK_ARR.length == 0)
	{
		$("#transmitToTakeMaterial").hide();
	}
}
/* 判断生成送货单按钮的显示 */
function deliverBtn()
{
	var isSoList = new Array();
	$.each($("#product_table tbody tr"), function(i, val)
	{
		var $this = $(this);
		var id = $this.find("input[name='productId']").val();
		var deliverQty = Number($this.find("input[name='deliverQty']").val()); // 已送货量
		var saleProduceQty = Number($this.find("input[name='saleProduceQty']").val());
		var sourceBillNo = $this.find("input[name='sourceBillNo']").val() || "";
		
		if (sourceBillNo != "")
		{
			isSoList.push(sourceBillNo);
		}
		if (saleProduceQty > deliverQty)
		{
			DELIVER_ARR.push(id);
		}
	});
	if (DELIVER_ARR.length == 0)
	{
		$("#transmitToDeliver").hide();
	}
	// 有销售订单则隐藏按钮
	if (isSoList.length != 0)
	{
		$("#transmitToDeliver").hide();
	}

}
/* 判断生成成品入库按钮的显示 */
function productiInBtn()
{
	if ($("#order_isOutSource").val() == "YES")
	{
		$("#transmitToProductIn").hide();
	}
	$.each($("#product_table tbody tr"), function(i, val)
	{
		var $this = $(this);
		var id = $this.find("input[name='productId']").val();
		var inStockQty = Number($this.find("input[name='inStockQty']").val()); // 已入库数量
		var saleProduceQty = Number($this.find("input[name='saleProduceQty']").val()); // 生产数量
		if (inStockQty < saleProduceQty)
		{
			PRODUCT_IN_ARR.push(id);
		}
	});
	if (PRODUCT_IN_ARR.length == 0)
	{
		$("#transmitToProductIn").hide();
	}
	// 最后判断下是否全部按钮都隐藏
	var booleanList = new Array();
	$.each($("#generateTransmit ul li"), function(i, val)
	{
		var $this = $(this);
		var n = ($this.find("a").attr("style") == "display: none;"); // 隐藏 =true
		booleanList.push(n);
	});
	if (!booleanList.contains(false))
	{
		$("#generateTransmit").hide();
	}
}

function order_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/purch/order/view/' + id;
	var title = "采购订单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function deliver_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/sale/deliver/view/' + id;
	var title = "销售送货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function in_view(id)
{
	var url = Helper.basePath + '/stockproduct/in/view/' + id;
	var title = "成品入库";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function take_view(id)
{
	var url = Helper.basePath + '/stockmaterial/take/view/' + id;
	var title = "生产领料";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 查看 */
function process_view(id)
{
	if (!Helper.isNotEmpty(id))
	{
		return;
	}
	var url = Helper.basePath + '/outsource/process/view/' + id;
	var title = "发外加工单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function work_report(id)
{
	var url = Helper.basePath + '/produce/report/view/' + id;
	var title = "产量上报";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function stockmaterial_return(id)
{
	var url = Helper.basePath + '/stockmaterial/return/view/' + id;
	var title = "生产退料";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
// 查看
function stockmaterial_supplement(id)
{
	var url = Helper.basePath + '/stockmaterial/supplement/view/' + id;
	var title = "生产补料";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 生成采购订单 */
function transmitToPuch()
{
	var ids = "";
	$.each(PURCH_ARR, function(i, val)
	{
		ids = ids + "&ids=" + val;
	});
	var url = Helper.basePath + '/purch/order/toPurch/?1=1' + ids;
	var title = "采购订单";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 生成领料单 */
function transmitToTake()
{
	var ids = "";
	$.each(STOCK_ARR, function(i, val)
	{
		ids = ids + "&ids=" + val;
	});
	var title = "生产领料";
	var url = Helper.basePath + '/stockmaterial/take/toTake/?1=1' + ids;
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 生成送货单 */
function transmitToDeliver()
{
	// 判断是否存在源单为销售订单的生产工单
	var isSoList = new Array();
	var customerList = new Array();
	$.each($("#product_table tbody tr"), function(i, val)
	{
		var $this = $(this);
		var sourceBillNo = $this.find("input[name='sourceBillNo']").val() || "";
		var customerId = $this.find("input[name='customerId']").val();
		if (sourceBillNo != "")
		{
			isSoList.push(sourceBillNo);
		}
		if (!customerList.contains(customerId))
		{
			customerList.push(customerId);
		}
	});
	if (customerList.length != 1)
	{
		Helper.message.warn("存在不同的客户");
		return;
	}

	_paramStr = "";
	var billNo = $("#billNo").val();
	$.each(DELIVER_ARR, function(i, val)
	{
		_paramStr += "&idsBillNos=" + billNo + "_" + val;

	});
	var url = Helper.basePath + '/sale/deliver/create?1=1' + _paramStr;
	var title = "销售送货";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}
/* 生成成品入库 */
function transmitToProductIn()
{
	var ids = "";
	$.each(PRODUCT_IN_ARR, function(i, val)
	{
		ids = ids + "&ids=" + val;
	});
	var url = Helper.basePath + '/stockproduct/in/toProductIn?1=1' + ids;
	var title = "成品入库";
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));

}