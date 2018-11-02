<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>印管家工作台</title>
<c:if test="${mode == 'dev' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/welcome.css?v=${v }" />
</c:if>
<c:if test="${mode == 'pro' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.welcome.css?v=${v }" />
</c:if>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts-3d.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/modules/exporting.js"></script>
<script type="text/javascript">
	$(function()
	{
		$('.queryHead').find('li').click(function()
		{
			$("#timeType").val($(this).find("input").val());
			queryByTime();
			$(this).addClass("active").siblings().removeClass('active');
		});
		$("#div_notStockPurch").click(function()
		{//跳转采购入库转单
			var dd = new Date();
			dd.setDate(dd.getDate() + 3);
			var url = Helper.basePath + '/purch/transmit/to_purch_stock?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
			var title = "采购未入库";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		$("#div_notArriveOutSource").click(function()
		{//跳转发外到货转单
			var dd = new Date();
			dd.setDate(dd.getDate() + 3);
			var url = Helper.basePath + '/outsource/transmit/to_arrive?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
			var title = "发外未到货";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		$("#div_notDeliveSale").click(function()
		{//跳转销售送货转单
			var dd = new Date();
			dd.setDate(dd.getDate() + 3);
			var url = Helper.basePath + '/sale/transmit/to_deliver?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
			var title = "销售未送货";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		});
		$("#div_materialMinStock").click(function()
		{
			var dd = new Date();
			dd.setDate(dd.getDate() + 3);
			var url = Helper.basePath + '/stock/material/stock_warn';
			var title = "材料库存预警";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notReceiveOrder").click(function()
		{
			var dd = new Date();
			dd.setDate(dd.getDate() + 3);
			var url = Helper.basePath + '/finance/unfinished/saleReceive?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
			var title = "应收账款明细";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notPayment").click(function()
		{
			var dd = new Date();
			dd.setDate(dd.getDate() + 3);
			var url = Helper.basePath + '/finance/unfinished/purchPayment?dateMin=' + new Date().format("yyyy-MM-dd") + '&dateMax=' + new Date(dd).format("yyyy-MM-dd");
			var title = "采购应付账款明细";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notCheckSale").click(function()
		{
			var url = Helper.basePath + '/sale/order/list?auditflag=true';
			var title = "销售订单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notCheckPurch").click(function()
		{
			var url = Helper.basePath + '/purch/order/list?auditflag=true';
			var title = "采购订单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notCheckWork").click(function()
		{
			var url = Helper.basePath + '/produce/work/list?auditflag=true';
			var title = "生产施工单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notCheckPayment").click(function()
		{
			var url = Helper.basePath + '/finance/payment/list?auditflag=true';
			var title = "付款单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notCheckOutSource").click(function()
		{
			var url = Helper.basePath + '/outsource/process/list?auditflag=true';
			var title = "发外加工单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		$("#div_notCheckReceive").click(function()
		{
			var url = Helper.basePath + '/finance/receive/list?auditflag=true';
			var title = "收款单列表";
			admin_tab($("<a _href='"+url+"' data-title='"+title+"' />"));
		})
		load();
		queryByTime();
	})
	function load()
	{
		$.ajax({
			type : "POST",
			data : {},
			url : Helper.basePath + "/manager/query",
			dataType : "json",
			contentType : 'application/json;charset=utf-8', //设置请求头信息  
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
			},
			error : function(data)
			{
				//console.log(data);
			}
		});
	}
	function queryByTime()
	{
		$.ajax({
			type : "POST",
			url : Helper.basePath + "/manager/queryByTime/" + $("#timeType").val(),
			dataType : "json",
			contentType : 'application/json;charset=utf-8', //设置请求头信息  
			success : function(data)
			{

				$("#saleTotalQty").text(data.obj.saleTotalQty);
				$("#saleTotalMoney").text(data.obj.saleTotalMoney);
				$("#purchTotalQty").text(data.obj.purchTotalQty);
				$("#purchTotalMoney").text(data.obj.purchTotalMoney);
				$("#customerDebt").text(data.obj.customerDebt);
				$("#supplierDebt").text(data.obj.supplierDebt);
			},
			error : function(data)
			{
				//console.log(data);
			}
		});
	}
</script>
<script type="text/javascript">
	$(function()
	{
		/*时间段样式*/
		$(".dateClass li").click(function()
		{
			$(this).addClass('active').siblings().removeClass("active");
		})

		/*添加title*/
		$(".d_content b,.item_content").attr("onmouseover", "this.title=this.innerText");

		/* 显示数据模块左右按钮 */
		showBtn();
		$(window).resize(function()
		{
			showBtn();
		});
		$(".slide_btn_right").click(function()
		{
			$(".dataItems").animate({
				marginLeft : ($(window).width() - 1285) + "px"
			})
		})
		$(".slide_btn_left").click(function()
		{
			$(".dataItems").animate({
				marginLeft : "0"
			})
		})
		function showBtn()
		{
			if ($(window).width() < 1260)
			{
				$(".slide_btn").show();
			} else
			{
				$(".slide_btn").hide();
				$(".dataItems").animate({
					marginLeft : "0"
				})
			}
		}

		/*背景显示隐藏*/
		function hoverDiv()
		{
			$(".data_item").mouseover(function()
			{
				$(this).find(".d_bg,.d_line").show();
				$(this).siblings().find(".d_bg,.d_line").hide();
				$(this).find(".d_icon img").attr("src", "${ctxStatic}/layout/images/navigation/wd_h" + ($(this).index() + 1) + ".png");

			})
			$(".data_item").mouseout(function()
			{
				$(".d_bg,.d_line").hide();
				$(this).find(".d_icon img").attr("src", "${ctxStatic}/layout/images/navigation/wd_" + ($(this).index() + 1) + ".png")
			})
		}
		hoverDiv();

		$('.chartload').find('li').click(function()
		{
			loadChart($(this).attr('id'));
			$(this).addClass("active").siblings().removeClass('active');
		});
		loadChart('ThisMonth');
		function loadChart(timeType)
		{
			$.ajax({
				type : "POST",
				url : Helper.basePath + "/manager/loadChart/" + timeType,
				dataType : "json",
				contentType : 'application/json;charset=utf-8', //设置请求头信息  
				success : function(data)
				{
					// 			              option.series[0].data=[];
					// 			              option.series[1].data=[];
					// 			              option.series[2].data=[];
					// 			              option.series[3].data=[];
					// 			              $.each(data.obj.totalSaleMoneyByDay,function(key,val){
					// 			                  option.xAxis.data.push(key);
					// 			                  option.series[0].data.push(val);
					// 			              });

					// 			              $.each(data.obj.totalMaterialMoneyByDay,function(key,val){
					// 			                  option.series[1].data.push(val);
					// 			              });

					// 			              $.each(data.obj.totalOutSourceMoneyByDay,function(key,val){
					// 			                  option.series[2].data.push(val);
					// 			              });

					// 			              $.each(option.series[0].data,function (index,val){
					// 			                  //毛利=销售-材料-外发
					// 			                  option.series[3].data.push(val-option.series[1].data[index]-option.series[2].data[index]);
					// 			              });

					// 			              myChart.setOption(option);
				},
				error : function(data)
				{
					//console.log(data);
				}
			});
		}
		//定义chart高度--分辨率适应
		var chartHeight = parseInt($(window).height() - $("#hchart_left").offset().top - 20);
		$("#hchart_left").css("height", chartHeight + "px");
		// Load the fonts
		Highcharts.createElement('link', {
			href : 'http://fonts.googleapis.com/css?family=Signika:400,700',
			rel : 'stylesheet',
			type : 'text/css'
		}, null, document.getElementsByTagName('head')[0]);

		// 添加背景
		Highcharts.wrap(Highcharts.Chart.prototype, 'getContainer', function(proceed)
		{
			proceed.call(this);
			this.container.style.background = 'none';
		});

		Highcharts.theme = {
			colors : [ "#90ee90", "#B0C4DE", "#ffb256", "#20B2AA", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee" ],
			chart : {
				backgroundColor : null,
				style : {
					fontFamily : "Signika, serif"
				}
			},
			title : {
				style : {
					color : 'black',
					fontSize : '16px',
					fontWeight : 'bold'
				}
			},
			subtitle : {
				style : {
					color : 'black'
				}
			},
			tooltip : {
				borderWidth : 0
			},
			legend : {
				itemStyle : {
					fontWeight : 'bold',
					fontSize : '13px'
				}
			},
			xAxis : {
				labels : {
					style : {
						color : '#6e6e70'
					}
				}
			},
			yAxis : {
				labels : {
					style : {
						color : '#6e6e70'
					}
				}
			},
			plotOptions : {
				series : {
					shadow : true
				},
				candlestick : {
					lineColor : '#404048'
				},
				map : {
					shadow : false
				}
			},

			// Highstock specific
			navigator : {
				xAxis : {
					gridLineColor : '#D0D0D8'
				}
			},
			rangeSelector : {
				buttonTheme : {
					fill : 'white',
					stroke : '#C0C0C8',
					'stroke-width' : 1,
					states : {
						select : {
							fill : '#D0D0D8'
						}
					}
				}
			},
			scrollbar : {
				trackBorderColor : '#C0C0C8'
			},

			// General
			background2 : '#E0E0E8'

		};

		// 折线图全局配置
		Highcharts.setOptions(Highcharts.theme);

		Highcharts.setOptions({
			lang : {
				contextButtonTitle : "图表导出菜单",
				decimalPoint : ".",
				downloadJPEG : "导出JPEG图片",
				downloadPDF : "导出PDF文件",
				downloadPNG : "导出PNG文件",
				downloadSVG : "导出SVG文件",
				drillUpText : "返回 {series.name}",
				loading : "加载中",
				months : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ],
				noData : "没有数据",
				numericSymbols : [ "千", "兆", "G", "T", "P", "E" ],
				printChart : "打印图表",
				resetZoom : "恢复缩放",
				resetZoomTitle : "恢复图表",
				shortMonths : [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ],
				thousandsSep : ",",
				weekdays : [ "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" ]
			}
		});

		/* 折线图 */
		var option = {
			chart : {
				type : 'line',
				zoomType : 'x'
			},
			title : {
				text : '本月销售毛利润',
				style : {
					color : "#5e5e5e",
					fontWeight : "normal",
					fontFamily : "微软雅黑"
				}
			},
			xAxis : {
				categories : []
			},
			yAxis : {
				title : {
					text : '金额（单位：元）'
				}
			},
			tooltip : {
				formatter : function()
				{
					return '<b>' + this.series.name + '</b><br/>' + this.x + ': ' + this.y + '元';
				},
				style : {
					color : "#5e5e5e",
					fontWeight : "normal"
				}
			},
			plotOptions : {
				line : {
					dataLabels : {
						enabled : true,
						style : {
							color : "#5e5e5e",
							fontWeight : "normal"
						}
					},
					enableMouseTracking : true
				}
			},
			series : [ {
				name : '销售',
				stack : '总量',
				data : []
			}, {
				name : '材料',
				stack : '总量',
				data : []
			}, {
				name : '发外',
				stack : '总量',
				data : [],
				visible : false
			}, {
				name : '毛利',
				stack : '总量',
				data : [],
				visible : false
			} ],
			credits : {
				enabled : false
			},
			exporting : {
				enabled : false
			},
			legend : {
				itemStyle : {
					color : "#5e5e5e"
				}
			}

		};

		// 使用刚指定的配置项和数据显示图表。
		$('.chartload').find('li').click(function()
		{
			loadChart($(this).attr('id'));
			$(this).addClass("active").siblings().removeClass('active');
			if ($(this).index() == 0)
			{
				option.title.text = "本月销售毛利润";
			} else if ($(this).index() == 1)
			{
				option.title.text = "本年销售毛利润";
			}
		});
		loadChart('ThisMonth');
		function loadChart(timeType)
		{
			var dd = new Date();
			var len = 0;
			if (timeType == 'ThisMonth')
			{
				len = dd.getDate();
			} else if (timeType == 'Year')
			{
				len = dd.getMonth() + 1;
			}
			$.ajax({
				type : "POST",
				url : Helper.basePath + "/manager/loadChart/" + timeType,
				dataType : "json",
				contentType : 'application/json;charset=utf-8', //设置请求头信息  
				success : function(data)
				{
					option.xAxis.categories = [];
					option.series[0].data = [];
					option.series[1].data = [];
					option.series[2].data = [];
					option.series[3].data = [];
					$.each(data.obj.totalSaleMoneyByDay, function(key, val)
					{
						option.xAxis.categories.push(key);
						option.series[0].data.push(val);
						option.series[0].data.splice(len);

					});

					$.each(data.obj.totalMaterialMoneyByDay, function(key, val)
					{
						option.series[1].data.push(val);
						option.series[1].data.splice(len);
					});

					$.each(data.obj.totalOutSourceMoneyByDay, function(key, val)
					{
						option.series[2].data.push(val);
						option.series[2].data.splice(len);
					});

					$.each(option.series[0].data, function(index, val)
					{
						//毛利=销售-材料-外发
						option.series[3].data.push(val - option.series[1].data[index] - option.series[2].data[index]);
						option.series[3].data.splice(len);
					});

					$('#hchart_left').highcharts(option);
				},
				error : function(data)
				{
					//console.log(data);
				}
			});
		}
	})
</script>
</head>
<body>
	<div class="hy_workdesk hy_wd">
		<div class="hy_left" style="right: 10px">
			<div class="hy_data">
				<ul class="cl dateClass queryHead">
					<li>
						<a href="javascript:;">今日</a>
						<span>|</span>
						<input type="hidden" value="Today">
					</li>
					<li>
						<a href="javascript:;">昨天</a>
						<span>|</span>
						<input type="hidden" value="Yesterday">
					</li>
					<li class="active">
						<a href="javascript:;">本月</a>
						<span>|</span>
						<input type="hidden" value="ThisMonth">
					</li>
					<li>
						<a href="javascript:;">上月</a>
						<span>|</span>
						<input type="hidden" value="BeforeMonth">
					</li>
					<li>
						<a href="javascript:;">本季</a>
						<span>|</span>
						<input type="hidden" value="Quarter">
					</li>
					<li>
						<a href="javascript:;">本年</a>
						<input type="hidden" value="Year">
					</li>
					<input id="timeType" type="hidden" value="ThisMonth">
				</ul>
				<div class="cl dataItems_wrap">
					<div class="dataItems clearfix">
						<div class="data_item">
							<div class="d_line"></div>
							<div class="d_icon">
								<img alt="" src="${ctxStatic}/layout/images/navigation/wd_1.png" />
							</div>
							<div class="d_content">
								<p>
									<span>销售总量</span>
									<b id="saleTotalQty"></b>
								</p>
								<p>
									<span>销售总额</span>
									<b id="saleTotalMoney"></b>
								</p>
							</div>
							<div class="d_bg"></div>
						</div>
						<div class="data_item">
							<div class="d_line"></div>
							<div class="d_icon">
								<img alt="" src="${ctxStatic}/layout/images/navigation/wd_2.png" />
							</div>
							<div class="d_content">
								<p>
									<span>采购总量</span>
									<b id="purchTotalQty"></b>
								</p>
								<p>
									<span> 采购总额</span>
									<b id="purchTotalMoney"></b>
								</p>
							</div>
							<div class="d_bg"></div>
						</div>
						<div class="data_item">
							<div class="d_line"></div>
							<div class="d_icon">
								<img alt="" src="${ctxStatic}/layout/images/navigation/wd_3.png" />
							</div>
							<div class="d_content">
								<p>
									<span>客户欠款(元) &nbsp; </span>
									<b id="customerDebt"></b>
								</p>
								<p>
									<span>供应商欠款(元)</span>
									<b id="supplierDebt"></b>
								</p>
							</div>
							<div class="d_bg"></div>
						</div>
						<div class="data_item">
							<div class="d_line"></div>
							<div class="d_icon">
								<img alt="" src="${ctxStatic}/layout/images/navigation/wd_4.png" />
							</div>
							<div class="d_content">
								<p>
									<span>成品库存总量</span>
									<b id="productStockQty"></b>
								</p>
								<p>
									<span>成品库存成本</span>
									<b id="productStockMoney"></b>
								</p>
							</div>
							<div class="d_bg"></div>
						</div>
						<div class="data_item">
							<div class="d_line"></div>
							<div class="d_icon">
								<img alt="" src="${ctxStatic}/layout/images/navigation/wd_5.png" />
							</div>
							<div class="d_content">
								<p>
									<span>材料库存总量</span>
									<b id="materialStockQty"></b>
								</p>
								<p>
									<span>材料库存成本</span>
									<b id="materialStockMoney"></b>
								</p>
							</div>
							<div class="d_bg"></div>
						</div>
					</div>
					<div class="slide_btn slide_btn_left"></div>
					<div class="slide_btn slide_btn_right"></div>
				</div>
			</div>
			<div class="hy_chart">
				<ul class="cl dateClass chartload">
					<li class="active" id="ThisMonth">
						<a href="javascript:;">本月</a>
						<span>|</span>
					</li>
					<li id="Year">
						<a href="javascript:;">本年</a>
					</li>
				</ul>
				<div class="cl chart_container">
					<div id="hchart_left" style="width: 100%; min-height: 260px; max-height: 500px"></div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
