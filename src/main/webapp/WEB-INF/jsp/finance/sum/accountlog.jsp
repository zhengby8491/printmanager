<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js"></script>
<title>账户资金流水汇总</title>
<script type="text/javascript">
    $(function()
    {

        //订单详情table
        $("#bootTable").bootstrapTable({
            url: Helper.basePath + "/finance/sum/accountlogList",
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
            cookieIdTable: "print_finance_accountlog_sum",//必须制定唯一的表格cookieID

            uniqueId: "id",//定义列表唯一键
            columns: [
            {
                field: 'bankNo',
                title: '账户号',
                width: 140
            },{
                field: 'beginMoney',
                title: '期初金额',
                width: 120
            }, {
                field: 'inTransMoney',
                title: '账户收入',
                width: 120
            },{
                field: 'outTransMoney',
                title: '账户支出',
                width: 120
            }, {
                field: 'remnantMoney',
                title: '账户余额',
                width: 120,
                formatter: function(value, row, index){
                    return Number(Number(row.inTransMoney).subtr(Number(row.outTransMoney))).tomoney();
                }
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
				        if(!Helper.basic.hasPermission('finance:accountlog:export')){
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

    function queryParams(params)
    {
        params['dateMin'] = $("#dateMin").val().trim();
        params['dateMax'] = $("#dateMax").val().trim();
        params['bankNo'] = $("#bankNo").val().trim();
        return params;
    }
    function responseHandler(res)
    {
        return {
            rows: res.result,
            total: res.count
        };
    }
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-账户资金流水汇总"></sys:nav>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">

						<dd class="row-dd">
							<sys:dateConfine label="流水日期" initDate="false" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">账户号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="bankNo" name="bankNo" type="text" class="input-txt input-txt_13" />
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