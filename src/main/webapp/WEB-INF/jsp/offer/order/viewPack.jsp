<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>报价单</title>
<script type="text/javascript">
	(function($)
	{
		var printAreaCount = 0;
		$.fn.printArea = function()
		{
			var ele = $(this);
			var idPrefix = "printArea_";
			removePrintArea(idPrefix + printAreaCount);
			printAreaCount++;
			var iframeId = idPrefix + printAreaCount;
			var iframeStyle = 'position:absolute;width:0px;height:0px;left:-500px;top:-500px;';
			iframe = document.createElement('IFRAME');
			$(iframe).attr({
				style : iframeStyle,
				id : iframeId
			});
			document.body.appendChild(iframe);
			var doc = iframe.contentWindow.document;
			$(document).find("link").filter(function()
			{
				return $(this).attr("rel").toLowerCase() == "stylesheet";
			}).each(function()
			{
				doc.write('<link type="text/css" rel="stylesheet" href="' + $(this).attr("href") + '" >');
			});
			doc.write('<div class="' + $(ele).attr("class") + '">' + $(ele).html() + '</div>');
			doc.close();
			var frameWindow = iframe.contentWindow;
			frameWindow.close();
			frameWindow.focus();
			setTimeout(function()
			{
				frameWindow.print();
			}, 500);
			/* frameWindow.onload = function()
			{
				frameWindow.print();
			} */
		}
		var removePrintArea = function(id)
		{
			$("iframe#" + id).remove();
		};
	})(jQuery);
	$(function()
	{
		$(".tofix").each(function(index, item)
		{
			var $this = $(this);
			var $val = $this.html();
			$this.html(Number($val).toFixed(2));
		});

		$(".tofix4").each(function(index, item)
		{
			var $this = $(this);
			var $val = $this.html();
			$this.html(Number($val).toFixed(4));
		});

		$(".offer_procedure_name").each(function()
		{
			$(this).text($(this).text().replace(/#/g, " ￥"));
		});
		// 返回列表
		$("#btn_back").on("click", function()
		{
			var url = Helper.basePath + '/offer/list';
			var title = "报价单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		// 审核
		$("#btn_audit").on("click", function()
		{
			var order_id = $("#id").val();
			Helper.post(Helper.basePath + '/offer/check/' + order_id, function(data)
			{
				if (data.success)
				{
					location.reload();
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});

		// 反审核
		$("#btn_audit_cancel").on("click", function()
		{
			var ids = [];
			ids.push($("#id").val());
			Helper.post(Helper.basePath + '/sale/order/createFromOfferCheck', {
				"from" : "OFFER",
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					var str = '';
					if (data.obj.ERROR)
					{
						var arr = data.obj.ERROR;
						str += "反审核失败,已被下游单据引用：<br/>";
						for (var i = 0; i < arr.length; i++)
						{
							var _tips = arr[i].split(",");
							str += '&emsp;&emsp;销售订单<a href="javascript:;" onclick="saleView(' + _tips[1] + ')">' + _tips[0] + '</a><br/>';
						}
						Helper.message.view(str);
					} else
					{
						var order_id = $("#id").val();
						Helper.post(Helper.basePath + '/offer/checkBack/' + order_id, function(data)
						{
							if (data.success)
							{
								location.reload();
							} else
							{
								Helper.message.warn(data.message);
							}
						});
					}
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});

		// 生成销售订单
		$("#btn_saleOrder_create").on("click", function()
		{
			var ids = [];
			ids.push($("#id").val());
			Helper.post(Helper.basePath + '/sale/order/createFromOfferCheck', {
				"from" : "OFFER",
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					var str = '';
					if (data.obj.ERROR)
					{
						var arr = data.obj.ERROR;
						str += "生成销售订单失败,已被下游单据引用：<br/>";
						for (var i = 0; i < arr.length; i++)
						{
							var _tips = arr[i].split(",");
							str += '&emsp;&emsp;销售订单<a href="javascript:;" onclick="saleView(' + _tips[1] + ')">' + _tips[0] + '</a><br/>';
						}
						Helper.message.view(str);
					} else if (data.obj.SUCCESS && data.obj.SUCCESS.length > 0)
					{
						var arr = data.obj.SUCCESS;
						for (var i = 0; i < arr.length; i++)
						{
							str += '<div>' + arr[i] + '</div>';
						}
						str += '<div>如果基础资料不存在，将不能生成销售订单</div>';
						str += '<div>请问是否自动新增</div>';
						str += '<div>您也可以点击<span style="color: red; font-size: 16px;">"取消"</span>先去基础资料模块里手动增加所有基础资料后再进行转单生成销售订单</div>';
						Helper.message.confirm(str, function(index)
						{
							createFromOffer(ids);
							layer.close(index);
						});
					} else
					{
						createFromOffer(ids);
					}
				} else
				{
					Helper.message.warn(data.message);
				}
			});
		});

		// 删除
		$("#btn_del").on("click", function()
		{
			Helper.message.confirm('确认要删除吗？', function(index)
			{
				Helper.post(Helper.basePath + '/offer/deleteOfferBean/' + $("#id").val(), function(data)
				{
					if (data.success)
					{
						Helper.message.suc(data.message);
						closeTabAndJump("报价单列表");
						/* location.href=Helper.basePath+"/sale/order/list"; */
					} else
					{
						Helper.message.warn(data.message);
					}
				});
			});
		});

		//$("#print_content").html($("#offer_detail_content").html());
		$("#print_content").find(".offer_detail_table").css("width", "100%");
		$("#print").click(function()
		{
			$("#print_content").printArea();
		});
		$("#printDw").click(function()
		{
			$("#print_content_dw").printArea();
		});

		$("#excel").click(function()
		{
			location.href = Helper.basePath + '/offer/viewEXCEL/${offerBean.id }';
		});
		$("#pdf").click(function()
		{
			location.href = Helper.basePath + '/offer/viewPDF/${offerBean.id }';
		});

	});

	/**
	 * 报价转销售订单
	 * @param params
	 * @returns {___anonymous5070_5075}
	 * @since 1.0, 2018年2月9日 上午11:15:13, think
	 */
	function createFromOffer(ids)
	{
		var param = "1=1";
		$(ids).each(function(i, val)
		{
			param += "&ids[]=" + val;
		});
		var url = Helper.basePath + '/sale/order/createFromOffer?' + param;
		var title = "销售订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}

	/**
	 * 查看销售订单
	 * @param params
	 * @returns {___anonymous5070_5075}
	 * @since 1.0, 2018年2月9日 上午11:15:13, think
	 */
	function saleView(id)
	{
		if (!Helper.isNotEmpty(id))
		{
			return;
		}
		var url = Helper.basePath + '/sale/order/view/' + id;
		var title = "销售订单";
		admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
	}
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!-- 隐藏打印区域 -->
			<%@include file="/WEB-INF/jsp/offer/order/_commonPrint.jsp"%>
			<div class="top_nav hidden-print">
				<input type="hidden" id="id" value="${offerBean.id }">
				<button class="nav_btn table_nav_btn" id="btn_back">返回</button>
				<div class="btn-group" id="btn_transmit">
					<span id="isCheckNO" style="<c:if test="${offerBean.isCheck=='YES' }">display: none;</c:if>">
						<shiro:hasPermission name="offer:order:audit">
							<button id="btn_audit" class="nav_btn table_nav_btn " type="button">审核</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="offer:order:del">
							<button class="nav_btn table_nav_btn" id="btn_del">删除</button>
						</shiro:hasPermission>
					</span>
					<span id="isCheckYES" style="<c:if test="${offerBean.isCheck=='NO' }">display: none;</c:if>">
						<c:if test="${empty offerBean.billNo }">
							<shiro:hasPermission name="sale:order:create">
								<button id="btn_saleOrder_create" class="nav_btn table_nav_btn " type="button">生成销售订单</button>
							</shiro:hasPermission>
						</c:if>
						<shiro:hasPermission name="offer:order:audit:cancel">
							<button id="btn_audit_cancel" class="nav_btn table_nav_btn" type="button">反审核</button>
						</shiro:hasPermission>
					</span>
					<shiro:hasPermission name="offer:order:list:print">
						<button id="print" class="nav_btn table_nav_btn " type="button">打印内部核价</button>
						<button id="printDw" class="nav_btn table_nav_btn " type="button">打印对外报价</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="offer:order:list:export">
						<button id="pdf" class="nav_btn table_nav_btn " type="button">导出PDF</button>
						<button id="excel" class="nav_btn table_nav_btn " type="button">导出EXCEL</button>
					</shiro:hasPermission>
				</div>
			</div>
			<div id="offer_detail_content">
				<table id="show_table" border="0" class="border-table offer_detail_table">
					<thead>
						<td colspan="12" style="background: #e2e9ef;">
							<span>${offerBean.productName }</span>
							- 报价单
						</td>
					</thead>
					<tbody>
						<tr>
							<td rowspan="2" width="82px;">成品信息</td>
							<td>客户名称</td>
							<td>客户地址</td>
							<td>联系电话</td>
							<td colspan="2">成品名称</td>
							<td>成品规格</td>
							<td>报价数量</td>
							<td>报价单号</td>
							<td>交货日期</td>
							<td>报价人</td>
							<td>报价日期</td>
						</tr>
						<tr class="whiteBg">
							<td>${offerBean.customerName }</td>
							<td>${offerBean.linkAddress}</td>
							<td>${offerBean.linkName}${offerBean.phone}</td>
							<td colspan="2">${offerBean.productName}</td>
							<td>${offerBean.specification}</td>
							<td>${offerBean.amount}</td>
							<td>${offerBean.offerNo}</td>
							<td>${offerBean.deliveryDateStr}</td>
							<td>${offerBean.createName}</td>
							<td>${offerBean.createDateTimeStr}</td>
						</tr>
						<c:forEach items="${offerBean.offerPartList }" var="partList">
							<tr>
								<td <c:choose>
									<c:when test="${offerBean.offerType != 'CARTONBOX'}">rowspan="5"</c:when>
									<c:otherwise>rowspan="6"</c:otherwise>
								</c:choose>>${partList.partName}</td>
								<td>印刷机</td>
								<td>上机规格</td>
								<td>印刷方式</td>
								<td style="width: 50px">正反普色</td>
								<td style="width: 50px">正反专色</td>
								<td>印版付数</td>
								<td>印版张数</td>
								<td>拼版数</td>
								<td>印张正数</td>
								<td>放损数</td>
								<td>总印张</td>
							</tr>
							<tr class="whiteBg">
								<td>${partList.machineName}</td>
								<td>${partList.machineSpec}</td>
								<td>${partList.offerPrintStyleType.text}</td>
								<td>${partList.prosConsColor}</td>
								<td>${partList.prosConsSpot}</td>
								<td>${partList.offerPrintStyleType.value}</td>
								<td>${partList.sheetZQ}</td>
								<td>${partList.sheetNum}</td>
								<td>${partList.impositionNum}</td>
								<td>${partList.waste}</td>
								<td>${partList.paperTotal}</td>
							</tr>
							<tr>
								<td>材料分类</td>
								<td>材料名称</td>
								<td>材料规格</td>
								<td colspan="2">克重</td>
								<td>上机数量</td>
								<td>材料开数</td>
								<td>材料数量</td>
								<td>计价数量</td>
								<td>材料单价</td>
								<td>材料金额</td>
							</tr>
							<tr class="whiteBg">
								<td>${partList.paperName}</td>
								<td>${partList.paperName}</td>
								<td>${partList.paperType.style}</td>
								<td colspan="2">${partList.paperWeight}</td>
								<td>${partList.paperTotal}</td>
								<td>${partList.materialOpening}</td>
								<td>${partList.materialAmount}</td>
								<td>${partList.calNum}</td>
								<td class="tofix">${partList.paperTonPrice}</td>
								<td class="tofix">${partList.paperTonPrice * partList.calNum}</td>
							</tr>
							<c:if test="${partList.containBflute == 'YES'}">
								<tr class="whiteBg">
									<td>${partList.bflutePit}</td>
									<td>${partList.bflutePaperQuality}</td>
									<td>${partList.machineSpec}</td>
									<td colspan="2"></td>
									<td>${partList.bfluteNum}</td>
									<td>1</td>
									<td>${partList.bfluteNum}</td>
									<td>${partList.bfluteCalNum}</td>
									<td class="tofix">${partList.bflutePrice}</td>
									<td class="tofix">${partList.bflutePrice * partList.bfluteCalNum}</td>
								</tr>
							</c:if>
							<c:if test="${partList.containBflute != 'YES' && offerBean.offerType == 'CARTONBOX' }">
								<tr class="whiteBg">
									<td></td>
									<td></td>
									<td></td>
									<td colspan="2"></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
							</c:if>
							</tr>
							<tr class="whiteBg">
								<td>后道工序</td>
								<td colspan="10" style="text-align: left; word-wrap: break-word; white-space: normal;">${partList.partProcedureStr}</td>
							</tr>
						</c:forEach>
						<c:if test="${offerBean.offerType == 'CARTONBOX' }">
							<tr class="whiteBg">
								<td style="background: #F1F1F1">成品工序</td>
								<td>成品工序</td>
								<td colspan="10" style="text-align: left; word-wrap: break-word; white-space: normal;">${offerBean.productProcedureStr }</td>
							</tr>
						</c:if>
					</tbody>
				</table>

				<table border="0" class="border-table offer_detail_table" style="margin-top: 0px;">
					<tbody>
						<tr>
							<td rowspan="${fn:length(offerBean.offerOrderQuoteInnerList)+1 }" width="82px;">阶梯数据</td>
							<td>数量</td>
							<td>纸张费</td>
							<td>印刷费</td>
							<td>工序费</td>
							<td>其他费</td>
							<td>运费</td>
							<td>成本金额</td>
							<td>利润</td>
							<td>未税金额</td>
							<td>未税单价</td>
							<td>含税金额</td>
							<td>含税单价</td>
						</tr>
						<c:forEach items="${offerBean.offerOrderQuoteInnerList }" var="quoteList">
							<tr class="whiteBg">
								<td>${quoteList.amount }</td>
								<td class="tofix">${quoteList.paperFee }</td>
								<td class="tofix">${quoteList.printFee }</td>
								<td class="tofix">${quoteList.procedureFee }</td>
								<td class="tofix">${quoteList.ohterFee }</td>
								<td class="tofix">${quoteList.freightFee }</td>
								<td class="tofix">${quoteList.costMoney }</td>
								<td class="tofix">${quoteList.profitFee }</td>
								<td class="tofix">${quoteList.untaxedFee }</td>
								<td class="tofix4">${quoteList.untaxedPrice }</td>
								<td class="tofix">${quoteList.taxFee }</td>
								<td class="tofix4">${quoteList.taxPrice }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>