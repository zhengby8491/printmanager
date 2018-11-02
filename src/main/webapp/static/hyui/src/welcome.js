$(document).ready(function()
{
	// 初始化备忘录
	var eventData = [];
	getEventData(new Date().format("yyyy"), new Date().format("MM"));
	$(document).on("click", ".calendar-month-navigation", function()
	{
		var year = $(".zabuto_calendar td[id]").attr("id").split("_")[3].split("-")[0];
		var month = $(".zabuto_calendar td[id]").attr("id").split("_")[3].split("-")[1];
		getEventData(year, month);
	});
	// 请求广告图片和链接地址
	Helper.request({
		url : Helper.basePath + "/sys/advertisement/ajaxPublisList?advertisementType=WORK",
		success : function(data)
		{
			for (var d = 0; d < data.length; d++)
			{
				var li = '<li><a  href="http://' + data[d].linkedUrl + '" target="_blank" data-id="' + data[d].id + '" data-linkedUrl="' + data[d].linkedUrl + '"><img src="' + data[d].photoUrl + '" /></a></li>';
				$(".adv_ul").append(li);
			}
			reload_common();
		}
	});
	// 统计广告点击数
	$(document).on("click", ".adv_ul a", function()
	{
		var adId = $(this).data("id");
		var ip = $("#keleyivisitorip").html();
		var address = "";
		// 新浪ip地址查询接口
		$.getScript('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=' + ip, function()
		{
			if (remote_ip_info.ret == '1')
			{
				address = remote_ip_info.province + remote_ip_info.city;
				Helper.request({
					url : Helper.basePath + "/sys/advertisement/ajaxStatistics?id=" + adId + "&ip=" + ip + "&address=" + address,
					success : function(data)
					{
					}
				})
			}
		});
	});
	$(".add_memorandum_i").click(function()
	{
		if ($(".zabuto_calendar").find(".badge-sel").length == 0)
		{
			Helper.message.warn("请选择日期！");
			return;
		}
		var sel_day = $(".zabuto_calendar").find(".badge-sel").parent().attr("id").split("_")[3];
		var eventContent = "";
		for (var i = 0, len = eventData.length; i < len; i++)
		{
			if (eventData[i].date == sel_day)
			{
				eventContent = eventData[i].content;
			}
		}
		// index为layer索引
		var index = layer.prompt({
			title : '<div class="l">备忘录</div><div class="r" style="margin-right: -55px;">' + sel_day + '</div>',
			formType : 2,
			closeBtn : 0,
			yes : function(index, layero)
			{
				Helper.request({
					url : Helper.basePath + "/workbench/memorandum/save?date=" + sel_day + "&content=" + layero.find(".layui-layer-input").val(),
					error : function(request)
					{
						Helper.message.warn("服务器繁忙");
					},
					success : function(data)
					{
						var year = $(".zabuto_calendar td[id]").attr("id").split("_")[3].split("-")[0];
						var month = $(".zabuto_calendar td[id]").attr("id").split("_")[3].split("-")[1];
						getEventData(year, month);
					}
				});
				layer.close(index);
			}
		});
		// 备忘输入框赋值
		$('#layui-layer' + index + " .layui-layer-input").val(eventContent);
	})
	// 监听日历点击，判断是当天badge-sel 或者 有备忘badge-event
	$(document).on("click", ".zabuto_calendar td[id]", function()
	{
		// 先移除其他天badge-sel
		if ($(".zabuto_calendar").find(".badge-sel").hasClass("badge-today") || $(".zabuto_calendar").find(".badge-sel").hasClass("badge-event"))
		{
			$(".zabuto_calendar").find(".badge-sel").removeClass("badge-sel");
		} else
		{
			$(".zabuto_calendar").find(".badge-sel").parent().html($(".zabuto_calendar").find(".badge-sel").text());
		}
		// 点击 添加 badge-sel
		if ($(this).find(".badge").length == 0)
		{
			$(this).find("div").wrapInner(function()
			{
				return '<span class="badge badge-sel"></span>';
			})
		} else
		{
			$(this).find(".badge").addClass("badge-sel");
			if ($(this).find(".badge").hasClass("badge-event"))
			{
				$(".add_memorandum_i").trigger("click");
			}
		}
	})
	$("#btn_oftenMenu").click(function()
	{
		Helper.popup.show('编辑常用功能(最多可选择8个)', Helper.basePath + '/workbench/often/oftenMenuList', '800', '550');
	});
	$(document).on("click", ".offen_menu_name", function()
	{
		var url = Helper.basePath + $(this).data("url");
		var title = $(this).text();
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})

	$(".demonstration_list").on("click", function()
	{
		var type = $(this).data('type');
		window.parent.location = Helper.basePath + '/demos/' + type;
		// Helper.request({
		// url: Helper.basePath + '/demos/' + type,
		// success: function(data)
		// {
		// if(data.success) {
		// window.parent.location.reload();
		// }
		// }
		// });
	});

	// 初始化日历
	function setCalendar(data, year, month)
	{
		eventData = data;
		$(".my-calendar").empty().zabuto_calendar({
			year : year,
			month : month,
			lang : "zn",
			month_labels : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ],
			dow_labels : [ "一", "二", "三", "四", "五", "六", "日" ],
			weekstartson : 0,
			today : true,
			data : eventData,
		});
		$(".my-calendar").find(".badge-today").addClass("badge-sel");
		
	}
	// 获取备忘时间，成功则初始化日历
	function getEventData(year, month)
	{
		Helper.request({
			url : Helper.basePath + '/workbench/memorandum/findAll?year=' + year + "&month=" + month,
			success : function(data)
			{
				if (data.success)
				{
					eventData = data.obj;
					for (var i = 0; i < eventData.length; i++)
					{
						eventData[i].date = new Date(eventData[i].date).format("yyyy-MM-dd");
					}
					setCalendar(eventData, year, month);
				}
			}
		});
	}
	
	$(document).off().on("click", ".calendar-month-navigation", function (){
		reload_common();
	})

	$("#div_notStockPurch").click(function()
	{// 跳转采购入库转单
		var dd = new Date();
		dd.setDate(dd.getDate() + 3);
		var url = Helper.basePath + '/purch/transmit/to_purch_stock?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
		var title = "采购未入库";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	$("#div_notArriveOutSource").click(function()
	{// 跳转发外到货转单
		var dd = new Date();
		dd.setDate(dd.getDate() + 3);
		var url = Helper.basePath + '/outsource/transmit/to_arrive?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
		var title = "发外未到货";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	$("#div_notDeliveSale").click(function()
	{// 跳转销售送货转单
		var dd = new Date();
		dd.setDate(dd.getDate() + 3);
		var url = Helper.basePath + '/sale/transmit/to_deliver?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
		var title = "销售未送货";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	});
	$("#div_materialMinStock").click(function()
	{
		var dd = new Date();
		dd.setDate(dd.getDate() + 3);
		var url = Helper.basePath + '/stock/material/stock_warn';
		var title = "材料库存预警";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notReceiveOrder").click(function()
	{
		var dd = new Date();
		dd.setDate(dd.getDate() + 3);
		var url = Helper.basePath + '/finance/unfinished/saleReceive?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
		var title = "应收账款明细";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notPayment").click(function()
	{
		var dd = new Date();
		dd.setDate(dd.getDate() + 3);
		var url = Helper.basePath + '/finance/unfinished/purchPayment?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
		var title = "采购应付账款明细";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckSale").click(function()
	{
		var url = Helper.basePath + '/sale/order/list?auditflag=true';
		var title = "销售订单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckPurch").click(function()
	{
		var url = Helper.basePath + '/purch/order/list?auditflag=true';
		var title = "采购订单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckWork").click(function()
	{
		var url = Helper.basePath + '/produce/work/list?auditflag=true';
		var title = "生产施工单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckPayment").click(function()
	{
		var url = Helper.basePath + '/finance/payment/list?auditflag=true';
		var title = "付款单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckOutSource").click(function()
	{
		var url = Helper.basePath + '/outsource/process/list?auditflag=true';
		var title = "发外加工单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckReceive").click(function()
	{
		var url = Helper.basePath + '/finance/receive/list?auditflag=true';
		var title = "收款单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckWriteoffReceive").click(function()
	{
		var url = Helper.basePath + '/finance/writeoffReceive/list?auditflag=true';
		var title = "收款核销单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_notCheckWriteoffPayment").click(function()
	{
		var url = Helper.basePath + '/finance/writeoffPayment/list?auditflag=true';
		var title = "收款核销单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_workSaleSumQty").click(function()
	{
		var url = Helper.basePath + '/produce/work/list';
		var title = "生产工单列表";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_deliverSumQty").click(function()
	{
		var url = Helper.basePath + '/sale/transmit/to_deliver';
		var title = "销售未送货";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_purchSumQty").click(function()
	{
		var url = Helper.basePath + '/purch/transmit/to_purch_stock';
		var title = "采购未入库";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_arriveSumQty").click(function()
	{
		var url = Helper.basePath + '/outsource/transmit/to_arrive';
		var title = "发外未到货";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_receiveSumQty").click(function()
	{
		var url = Helper.basePath + '/finance/unfinished/saleReceive';
		var title = "应收账款明细";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_paymentOutSourceSumQty").click(function()
	{
		var url = Helper.basePath + '/finance/unfinished/outSourcePayment';
		var title = "发外应付账款明细";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_workPurchQty").click(function()
	{
		var url = Helper.basePath + '/purch/transmit/to_purch_order';
		var title = "工单未采购";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_workTakeQty").click(function()
	{
		var url = Helper.basePath + '/produce/transmit/to_take';
		var title = "工单未领料";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_purchReconcilQty").click(function()
	{
		var url = Helper.basePath + '/purch/transmit/to_purch_reconcil';
		var title = "入库未对账";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_saleReconcilQty").click(function()
	{
		var url = Helper.basePath + '/sale/transmit/to_reconcil';
		var title = "送货未对账";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_processReconcilQty").click(function()
	{
		var url = Helper.basePath + '/outsource/transmit/to_reconcil';
		var title = "到货未对账";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_workStockQty").click(function()
	{
		var url = Helper.basePath + '/produce/transmit/to_product_in';
		var title = "工单未入库";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	$("#div_paymentPurchSumQty").click(function()
	{
		var url = Helper.basePath + '/finance/unfinished/purchPayment';
		var title = "采购应付账款明细";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
	})
	load();

});

function load()
{
	$.ajax({
		type : "POST",
		data : {},
		url : Helper.basePath + "/query",
		dataType : "json",
		contentType : 'application/json;charset=utf-8', // 设置请求头信息
		success : function(data)
		{
			$("#productStockQty").text(data.obj.productStockQty);
			$("#productStockMoney").text(data.obj.productStockMoney);
			$("#materialStockQty").text(data.obj.materialStockQty);
			$("#materialStockMoney").text(data.obj.materialStockMoney);
			$("#materialMinStock").text(data.obj.materialMinStock);
			$("#notArriveOutSource").text(data.obj.notArriveOutSource);
			$("#notStockPurch").text(data.obj.notStockPurch);
			$("#notDeliveSale").text(data.obj.notDeliveSale);
			$("#notPayment").text(data.obj.notPayment);
			$("#notReceiveOrder").text(data.obj.notReceiveOrder);
			$("#notCheckSale").text(data.obj.notCheckSale);
			$("#notCheckPurch").text(data.obj.notCheckPurch);
			$("#notCheckWork").text(data.obj.notCheckWork);
			$("#notCheckPayment").text(data.obj.notCheckPayment);
			$("#notCheckReceive").text(data.obj.notCheckReceive);
			$("#notCheckOutSource").text(data.obj.notCheckOutSource);
			$("#notCheckWriteoffReceive").text(data.obj.notCheckWriteoffReceive);
			$("#notCheckWriteoffPayment").text(data.obj.notCheckWriteoffPayment);
			// 未清
			$("#workSaleSumQty").text(data.obj.workSaleSumQty);
			$("#deliverSumQty").text(data.obj.deliverSumQty);
			$("#purchSumQty").text(data.obj.purchSumQty);
			$("#arriveSumQty").text(data.obj.arriveSumQty);
			$("#receiveSumQty").text(data.obj.receiveSumQty);
			$("#paymentPurchSumQty").text(data.obj.paymentPurchSumQty);
			$("#paymentOutSourceSumQty").text(data.obj.paymentOutSourceSumQty);
			$("#workPurchQty").text(data.obj.workPurchQty);
			$("#workTakeQty").text(data.obj.workTakeQty);
			$("#purchReconcilQty").text(data.obj.purchReconcilQty);
			$("#saleReconcilQty").text(data.obj.saleReconcilQty);
			$("#processReconcilQty").text(data.obj.processReconcilQty);
			$("#workStockQty").text(data.obj.workStockQty);

		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}

// 重新渲染菜单
function save_success(data)
{
	var html = "";
	var count = 1;
	for ( var d in data)
	{
		html += '<div class="offen_menu_item"><span class="offen_menu_index">' + count + '</span><div class="offen_menu_name" data-url="' + data[d] + '">' + d + '</div></div>';
		count++;
	}
	$(".hy_menu_list").html(html);
}
// 重新渲染日历热门推荐布局
function reload_common()
{
	var hy_right = $(".hy_right").height();
	var my_calendar = $(".my-calendar").height();
	var adv_list_div = hy_right - my_calendar - 67;
	$(".wel").height(adv_list_div);
}