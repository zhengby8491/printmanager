<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta name="keywords" content="印管家,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<meta name="description" content="印管家,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<title>印管家云平台</title>
<link rel="shortcut icon" href="${ctxHYUI }/images/favicon.icon" type="image/x-icon">
<c:if test="${mode == 'dev' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/welcome.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/zabuto-calendar/style.css?v=${v }" />
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/plugins/zabuto-calendar/zabuto_calendar.min.css?v=${v }" />
</c:if>
<c:if test="${mode == 'pro' }">
	<link rel="stylesheet" type="text/css" href="${ctxHYUI}/css/hyui.welcome.css?v=${v }" />
</c:if>
<!-- 皮肤样式  -->
<link id="topskin" rel="stylesheet" type="text/css" href="" />
<style type="text/css">
.step_chart a {
	text-decoration: none;
	color: rgb(21, 166, 127);
	display: inline-block;
}

.step_chart>span {
	position: relative;
	top: -47px;
}

.step_chart p {
	font-size: 16px;
}

.hy_left_report_item {
	height: 100%;
	width: 25%;
	float: left;
	box-sizing: border-box;
}

.hy_left_report_item_padding {
	height: 100%;
	margin: 0 5px;
	border: 1px solid #ddd;
}

.hy_left_report_item .hy_left_report_title {
	background: -webkit-linear-gradient(top, #f0f8ff, #9bc2d8);
	height: 30px;
	line-height: 30px;
	font-size: 14px;
	color: #000;
	text-align: center;
}

.hy_left_report_item .cl>div {
	width: 94%;
}

.hy_workdesk .hy_left_bottom {
	position: absolute;
	top: 307px;
	bottom: 0;
	left: 0;
	width: 100%;
}

.item_list .item_content>span {
	width: 30px;
	display: inline-block;
	text-align: center;
}
</style>
</head>
<body>
	<span id="keleyivisitorip" style="display: none"></span>
	<div class="hy_workdesk hy_wd">
		<div class="hy_left">
			<div id="" class="" style="height: 300px; width: 100%; border: 1px solid #ddd;">
				<div class="memorandum_head">工作流程图</div>
				<div class="step_chart" style="margin-top: 90px; text-align: center;">
					<a href="javascript:redirect('生产工单','/produce/work/create')"> <img src="${ctxHYUI}/images/navigation/nav_icon_3.png">
						<p>生产工单</p>
					</a> <span class="step_chart_line0"> <img src="${ctxHYUI}/images//navigation/line_7.png">
					</span> <a href="javascript:redirect('销售未送货','/sale/transmit/to_deliver')"> <img src="${ctxHYUI}/images//navigation/nav_icon_4.png">
						<p>销售送货</p>
					</a> <span class="step_chart_line2"> <img src="${ctxHYUI}/images//navigation/line_7.png">
					</span> <a href="javascript:redirect('送货未对账','/sale/transmit/to_reconcil')"> <img src="${ctxHYUI}/images//navigation/nav_icon_6.png">
						<p>销售对账</p>
					</a> <span class="step_chart_line4"> <img src="${ctxHYUI}/images//navigation/line_7.png">
					</span> <a href="javascript:redirect('收款单','/finance/receive/create')"> <img src="${ctxHYUI}/images//navigation/nav_icon_7.png">
						<p>收款</p>
					</a>
				</div>
			</div>
			<div class="step_chart" style="margin-top: 120px; text-align: center;"></div>
			<div class="hy_left_bottom cl">
				<div class="hy_left_report_item">
					<div class="hy_left_report_item_padding" style="margin-left: 0;">
						<div class="hy_left_report_title">生产报表</div>
						<div class="cl">
							<div class="offen_menu_item">
								<span class="offen_menu_index">1.</span>
								<div class="offen_menu_name" data-url="/produce/work/detailList">生产工单明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">2.</span>
								<div class="offen_menu_name" data-url="/produce/work/work_detail_procedure_list">生产工单工序明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">3.</span>
								<div class="offen_menu_name" data-url="/produce/work/work_schedule">生产工单进度</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">4.</span>
								<div class="offen_menu_name" data-url="/produce/work/work_detail_material_list">生产工单用料分析</div>
							</div>
						</div>
					</div>
				</div>
				<div class="hy_left_report_item">
					<div class="hy_left_report_item_padding">
						<div class="hy_left_report_title">销售报表</div>
						<div class="cl">
							<div class="offen_menu_item">
								<span class="offen_menu_index">1.</span>
								<div class="offen_menu_name" data-url="/sale/deliver/detailList">销售送货明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">2.</span>
								<div class="offen_menu_name" data-url="/sale/return/detailList">销售退货明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">3.</span>
								<div class="offen_menu_name" data-url="/sale/reconcil/detailList">销售对账明细</div>
							</div>
						</div>
					</div>
				</div>
				<div class="hy_left_report_item">
					<div class="hy_left_report_item_padding">
						<div class="hy_left_report_title">财务明细报表</div>
						<div class="cl">
							<div class="offen_menu_item">
								<span class="offen_menu_index">1.</span>
								<div class="offen_menu_name" data-url="/finance/receive/detailList">收款单明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">2.</span>
								<div class="offen_menu_name" data-url="/finance/writeoffReceive/detailList">收款核销明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">3.</span>
								<div class="offen_menu_name" data-url="/finance/log/receiveAdvanceLog">预收款日志</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">4.</span>
								<div class="offen_menu_name" data-url="/finance/log/accountLog">资金账号流水</div>
							</div>
						</div>
					</div>
				</div>
				<div class="hy_left_report_item">
					<div class="hy_left_report_item_padding" style="margin-right: 0;">
						<div class="hy_left_report_title">财务汇总表</div>
						<div class="cl">
							<div class="offen_menu_item">
								<span class="offen_menu_index">1.</span>
								<div class="offen_menu_name" data-url="/finance/unfinished/saleReceive">应收账款明细</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">2.</span>
								<div class="offen_menu_name" data-url="/finance/sum/receive">应收帐款汇总</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">3.</span>
								<div class="offen_menu_name" data-url="/finance/sum/arrears">往来单位欠款</div>
							</div>
							<div class="offen_menu_item">
								<span class="offen_menu_index">4.</span>
								<div class="offen_menu_name" data-url="/finance/sum/accountlog">账号资金流水汇总</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="hy_right">
			<!-- <div class="memorandum_head"><i class="fa fa-plus-circle add_memorandum_i"></i></div> -->
			<div id="" class="" style="height: 130px; width: 100%; border: 1px solid #ddd; overflow: auto;">
				<div class="memorandum_head">待办任务</div>
				<ul class="item_list">
					<!-- 								<li id="div_workSaleSumQty"> -->
					<!-- 									<div class="item_icon"></div> -->
					<!-- 									<a class="item_content" href="#"><span id="workSaleSumQty"></span>条 -- 未生产订单</a><a class="view">查看</a> -->
					<!-- 								</li> -->
					<li id="div_deliverSumQty">
						<div class="item_icon"></div> <a class="item_content" href="#"> <span id="deliverSumQty"></span> 条 -- 待送货订单
					</a> <a class="view">查看</a>
					</li>
					<li id="div_saleReconcilQty">
						<div class="item_icon"></div> <a class="item_content" href="#"> <span id="saleReconcilQty"></span> 条 -- 待销售对账
					</a> <a class="view">查看</a>
					</li>
					<li id="div_receiveSumQty">
						<div class="item_icon"></div> <a class="item_content" href="#"> <span id="receiveSumQty"></span> 条 -- 待收款销售
					</a> <a class="view">查看</a>
					</li>
				</ul>
			</div>
			<div class="adv_con" style="top: 135px;">
				<div class="memorandum_head">热门推荐</div>
				<div class="adv_list_div">
					<ul class="adv_ul">
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
<!--[if lt IE 9]>
<script type="text/javascript" src="${ctxHYUI}/plugins/html5.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/respond.min.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/PIE_IE678.js"></script>
<![endif]-->
<c:if test="${mode == 'dev' }">
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery/1.9.1/jquery.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/jquery.cookie/jquery.cookie.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/plugins/zabuto-calendar/zabuto_calendar.min.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/public.ext.js?v=${v }"></script>
	<script type="text/javascript" src="${ctxHYUI}/src/welcome.js?v=${v }"></script>
</c:if>
<c:if test="${mode == 'pro' }">
	<script type="text/javascript" src="${ctxHYUI}/js/hyui.welcome.js?v=${v }"></script>
</c:if>
<script type="text/javascript" src="${ctxHYUI}/plugins/layer/layer.js"></script>
<script type="text/javascript">
	//$.noConflict(true);  // <- this：Uncaught TypeError: Cannot read property 'addMethod' of undefined
	Helper.locale = "${locale}";
	Helper.basePath = "${ctx}";
	Helper.staticPath = "${ctxStatic}";
	Helper.v = "${v}";
	Helper.ctxHYUI = "${ctxHYUI}";
	/* 初始化首页皮肤 */
	function initTopSkin()
	{
		var cssType = $.cookie('ygj_skin');
		if (cssType == "" || cssType == null || cssType == "default")
		{
			$("#topskin").attr("href", Helper.ctxHYUI + '/themes/default/style.css?v=' + Helper.v);
		} else if (cssType == "brown")
		{
			$("#topskin").attr("href", Helper.ctxHYUI + '/themes/brown/style.css?v=' + Helper.v);
		} else if (cssType == "blue")
		{
			$("#topskin").attr("href", Helper.ctxHYUI + '/themes/blue/style.css?v=' + Helper.v);
		}
	}
	initTopSkin();
	/* 处理键盘事件 */
	function doKey(e)
	{
		var ev = e || window.event; //获取event对象   
		var obj = ev.target || ev.srcElement; //获取事件源   
		var t = obj.type || obj.getAttribute('type'); //获取事件源类型   
		if (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
		{
			return false;
		}
	}
	// 禁止后退键 作用于Firefox、Opera   
	document.onkeypress = doKey;
	// 禁止后退键  作用于IE、Chrome   
	document.onkeydown = doKey;
</script>
<script type="application/javascript">
	$(function()
	{
		$(".step_chart a").mouseover(function()
		{
			var idx = $(this).index();
			var imgPath = $(this).children("img").attr("src");
			var imgName = imgPath.substr(imgPath.lastIndexOf("/") + 1);
			var nameArr = imgName.split("_");
			var newImg = nameArr[0] + "_" + nameArr[1] + "_h" + nameArr[2];
			$(this).children("img").attr("src", "${ctxHYUI}/images//navigation/" + newImg);
			$(this).children("p").css("color", "#FFB257");

			$(".step_chart_line" + idx + " img").attr('src', '${ctxHYUI}/images//navigation/line_h7.png')
		})
		$(".step_chart a").mouseout(function()
		{
			var idx = $(this).index();
			var imgPath = $(this).children("img").attr("src");
			var imgName = imgPath.substr(imgPath.lastIndexOf("/") + 1);
			var nameArr = imgName.split("_");
			var newImg = nameArr[0] + "_" + nameArr[1] + "_" + nameArr[2].substr(nameArr[2].indexOf("h") + 1);
			$(this).children("img").attr("src", "${ctxHYUI}/images//navigation/" + newImg);
			$(this).children("p").css("color", "#15a67f");
			$(".step_chart_line" + idx + " img").attr("src", "${ctxHYUI}/images//navigation/line_7.png")
		});
	});
	
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
      	$(".adv_ul").html("");
        for (var d = 0; d < data.length; d++)
        {
          var li = '<li><a  href="http://' + data[d].linkedUrl + '" target="_blank" data-id="' + data[d].id + '" data-linkedUrl="' + data[d].linkedUrl + '"><img src="' + data[d].photoUrl + '" /></a></li>';
          $(".adv_ul").append(li);
        }
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
    
  });
  
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

  function redirect(title, url)
	{
		admin_tab($("<a _href='"+Helper.basePath+url+"' data-title='"+title+"' />"));
	}
</script>
</html>
