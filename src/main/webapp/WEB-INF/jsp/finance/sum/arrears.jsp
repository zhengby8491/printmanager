<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js"></script>
<title>往来单位欠款表</title>
<script type="text/javascript">
    $(function()
    {

        //订单详情table
        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/finance/sum/arrearsList",
            method: "post",
            contentType: 'application/json', //设置请求头信息  
            dataType: "json",
            sidePagination: 'server',//设置为服务器端分页
            queryParamsType: "",
            queryParams: queryParams,//参数
            responseHandler: responseHandler,

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
            cookieIdTable: "print_finance_company_arrears",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [
            {
                field: 'name',
                title: '名称',
                width: 140
            }, {
                field: 'type',
                title: '属性',
                width: 120,
                formatter: function(value, row, index)
                {
                    if(value=="MATERIAL"){
                        return '材料商';
                    }else if(value=="PROCESS"){
                        return '加工商';
                    }else if(value=="MATERIAL_AND_PROCESS"){
                        return '综合供应商';
                    }else{
                        return value;
                    }
                    
                }
            }, {
                field: 'receiveMoney',
                title: '应收款余额',
                width: 100,
                formatter: function(value, row, index){
                    if(value==0){
                        return "-";
                    }else{
                        return value;
                    }
                }
            }, {
                field: 'paymentMoney',
                title: '应付款余额',
                width: 100,
                formatter: function(value, row, index){
                    if(value==0){
                        return "-";
                    }else{
                        return value;
                    }
                }
            }, {
                field: 'processMoney',
                title: '应付加工余额',
                width: 100,
                formatter: function(value, row, index){
                    if(value==0){
                        return "-";
                    }else{
                        return value;
                    }
                }
            }, {
                field: 'balanceMoney',
                title: '结余',
                width: 100
            }],
            onLoadSuccess : function() {
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
			        if(!Helper.basic.hasPermission('finance:arrears:export')){
			         	 $(".export.btn-group").remove();
			          }
			        $(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
					}
			},
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
    });
    function responseHandler(res)
    {
        return {
            rows: res.result,
            total: res.count
        };
    }
    function queryParams(params)
    {      
        params['supplierName'] = $("#name").val().trim();
        params['customerName'] = $("#name").val().trim();
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
					<sys:nav struct="财务管理-往来单位欠款表"></sys:nav>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<label class="form-label label_ui">名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_0" id="name" />
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
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
		</div>
	</div>
</body>
</html>