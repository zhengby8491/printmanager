<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts-3d.js"></script>
<title>发外应付账款汇总表</title>
<script type="text/javascript">
    $(function()
    {
        setQueryTime();
        //订单详情table
        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/finance/sum/outSourcePaymentList",
            method: "post",
            contentType: 'application/json', //设置请求头信息  
            dataType: "json",
            pagination: true, // 是否显示分页（*）
            sidePagination: 'server',//设置为服务器端分页
            pageList: Helper.bootPageList,
            queryParamsType: "",
            pageSize: 20,
            pageNumber: 1,
            queryParams: queryParams,//参数
            responseHandler: responseHandler,
            onLoadSuccess: function(data)
            {

                if(data.rows.length > 0) {
                    optionbin.series[0].data = [];
                    optionzhu.xAxis.categories = [];
                    optionzhu.series[0].data = [];
                    $.each(data.rows, function(intex, item)
                    {
                    	if(item.name !=null){
	                        var money = Number(Number(item.beginMoney).add(Number(item.shouldMoney)).subtr(Number(item.money)).subtr(Number(item.surAdvance))).tomoney();
	                        optionbin.series[0].data.push([item.name, money]);
	                        optionzhu.xAxis.categories.push(item.name);
	                        optionzhu.series[0].data.push(money);
                    	}
                    });
                    $('#pie_chart').html('');
                    $('#pie_chart').highcharts(optionbin);
                    $('#column_chart').highcharts(optionzhu);
                }else {
                    $('#pie_chart').empty().append('<div>暂无数据</div>');
                }
                
                $("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for(var index = 0;index < _tds.length;index++){
					if(_tds.eq(index).text() == "-"){
						_tds.eq(index).text('');
					}
				}
				if($(".glyphicon-th").next().html() == ''){
				/* 表格工具栏 */
		        $(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
		        if(!Helper.basic.hasPermission('finance:outsourcepayment:export')){
		         	 $(".export.btn-group").remove();
		          }
		        $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
				}
            },
            //resizable : true, //是否启用列拖动
            showColumns: true, //是否显示所有的列
            minimumCountColumns: 2, //最少允许的列数
            striped: true, // 是否显示行间隔色
            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false, // 是否启用排序
            clickToSelect: true, // 是否启用点击选中行
            height: 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            cardView: false, // 是否显示详细视图
            detailView: false, // 是否显示父子表

            showExport: true,//是否显示导出按钮
            //exportDataType: 'all',
            exportTypes: ['csv', 'txt', 'excel', 'doc'],

            cookie: true,//是否启用COOKIE
            cookiesEnabled: ['bs.table.columns'],//默认会记住很多属性，这里控制只记住列选择属性
            cookieIdTable: "print_finance_payment_outSourceShouldPayment_sum",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [{
                field: 'name',
                title: '供应商名称',
                width: 100
            }, {
                field: 'beginMoney',
                title: '期初应付',
                width: 100
            }, {
                field: 'shouldMoney',
                title: '本期应付',
                width: 100
            }, {
                field: 'advance',
                title: '本期预付',
                width: 100
            }, {
                field: 'money',
                title: '本期实付',
                width: 100
            }, {
                field: 'discount',
                title: '本期折扣 ',
                width: 100
            }, {
                field: 'sunShouldPaymentMoney',
                title: '期末应付',
                width: 100,
                formatter: function(value, row, index)
                {
                    return Number(Number(row.beginMoney).add(Number(row.shouldMoney)).subtr(Number(row.money))).tomoney();
                }
            }, {
                field: 'surAdvance',
                title: '预付余额',
                width: 60
            }, {
                field: 'realityShouldPayment',
                title: '实际应付',
                width: 80,
                formatter: function(value, row, index)
                {
                    return Number(Number(row.beginMoney).add(Number(row.shouldMoney)).subtr(Number(row.money)).subtr(Number(row.surAdvance))).tomoney();
                }
            }],
            onColumnSwitch : function(field,checked) {
				$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
				var _tds = $("#bootTable tbody").find("tr:last").find("td");
				for(var index = 0;index < _tds.length;index++){
					if(_tds.eq(index).text() == "-"){
						_tds.eq(index).text('');
					}
				}
		},
        });

        /*3D饼状图*/
        var optionbin = {
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0
                }
            },
            colors: ['#7CB5EC', '#44b5e6', '#38bbbf', '#5acb8b', '#95cd5c', '#c3cd5c', '#e7b520', '#e78623', '#b1704a', '#6181a1'],
            title: {
                text: '饼图'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    depth: 35,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}:{point.y}'
                    }
                }
            },
            series: [{
                type: 'pie',
                name: '实际应付',
                data: []
            }],
            exporting: {
                enabled: false
            },
            credits: {
                enabled: false
            }
        };
        /*3D柱形图*/
        var optionzhu = {
            chart: {
                type: 'column',
                margin: 75,
                options3d: {
                    enabled: true,
                    alpha: 10,
                    beta: 5,
                    depth: 70
                }
            },
            colors: ['#7CB5EC', '#44b5e6', '#38bbbf', '#5acb8b', '#95cd5c', '#c3cd5c', '#e7b520', '#e78623', '#b1704a', '#6181a1'],
            title: {
                text: '柱形图'
            },
            plotOptions: {
                column: {
                    depth: 25
                }
            },
            xAxis: {
                categories: []
            },
            yAxis: {
                title: {
                    text: null
                }
            },
            series: [{
                name: '实际应付',
                data: []
            }],
            exporting: {
                enabled: false
            },
            credits: {
                enabled: false
            }
        };

        var num = 0;
        $(".switch_btn").click(function()
        {
            if(num == 0) {
                $(".boot-mar").hide();
                $(".chart").show();
                $(this).html('<i class="fa fa-th-large"></i> 汇总表');
                num = 1;
            }else if(num == 1) {
                $(".boot-mar").show();
                $(".chart").hide();
                $(this).html('<i class="fa fa-bar-chart"></i> 甘特图');
                num = 0;
            }
        });
    });

    function queryParams(params)
    {
        params['dateMin'] = $("#dateMin").val().trim();
        params['dateMax'] = $("#dateMax").val().trim();
        params['supplierName'] = $("#supplierName").val().trim();
        return params;
    }
    //设置单据时间查询默认时间
    function setQueryTime()
    {
    	 var dateMax = $("#dateMax").val();
         var myDate = new Date();
         var month = myDate.getMonth();
         month = month + 1 < 10 ? ("0" + (month + 1)) : month + 1;
         var year = myDate.getFullYear();
         var dateMin = year+"-"+ month + "-01";
         $("#dateMin").val(dateMin);
    }
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-发外应付账款汇总表"></sys:nav>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<!-- <span><input type="text"
								onfocus="WdatePicker({skin:'default',dateFmt:'yyyy-MM',maxDate:'#F{$dp.$D(\'dateMax\')||\'%y-%M\'}'})"
								class="input-txt input-txt_0 Wdate" id="dateMin" name="dateMin" />
							</span> <label class="label_2 align_c">至</label> <span> <input
								type="text"
								onfocus="WdatePicker({skin:'default',dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'dateMin\')}'})"
								class="input-txt input-txt_0 Wdate" id="dateMax" name="dateMax" />
							</span> -->
							<sys:dateConfine label="时间" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_6">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="supplierName" name="supplierName" type="text" class="input-txt input-txt_9" />
								<div class="select-btn" id="supplier_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn switch_btn">
								<i class="fa fa-bar-chart"></i>
								甘特图
							</button>
						</dd>
					</dl>
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
			<!-- 甘特图Start -->
			<div class="cl chart">
				<div id="pie_chart" style="float: left; width: 50%; height: 400px"></div>
				<div id="column_chart" style="float: left; width: 49%; height: 400px"></div>
			</div>
			<!-- 甘特图End -->
		</div>
	</div>
</body>
</html>