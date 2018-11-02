<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>资金帐户流水信息列表</title>
<script type="text/javascript">
	$(function() {

		/* 搜索*/
		$("#btn_search").click(function() {
			$("#bootTable").bootstrapTable("refreshOptions", {
				pageNumber : 1
			});
		});
		//订单详情table
		$("#bootTable")
				.bootstrapTable(
						{
							url : Helper.basePath
									+ "/finance/log/ajaxAccountLogList",
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
							//exportDataType: 'all',
							exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

							cookie : true,//是否启用COOKIE
							cookiesEnabled : [ 'bs.table.columns' ],//默认会记住很多属性，这里控制只记住列选择属性
							cookieIdTable : "print_finance_receive_detail",//必须制定唯一的表格cookieID

							uniqueId : "id",//定义列表唯一键
							columns : [
									{
										field : 'index',
										title : '序号',
										width : 40,
										formatter : function(value, row, index) {
											return index + 1;
										}
									},
									{
										field : 'account.bankNo',
										title : '交易账户',
										width : 80,
										formatter: function(value, row, index)
						                {
						                    return row.account.bankNo;
						                }
									},
									{
										field : 'referenceId',
										title : '交易外部流水号',
										width : 80
									},
									{
										field : 'transTypeText',
										title : '交易类型',
										width : 120
									},
									{
										field : 'transMoney',
										title : '交易金额',
										width : 80
									},
									{
										field : 'transTime',
										title : '交易时间',
										width: 80,
						                formatter: function(value, row, index)
						                {
						                    if(value) { return new Date(value).format("yyyy-MM-dd"); }
						                }
									},
									{
										field : 'remnantMoney',
										title : '交易后金额',
										width : 200
									},
									{
										field : 'tradeModeText',
										title : '交易模式',
										width : 120
									}],
							onLoadSuccess : function() {
								//alert("数据加载完成");
								if ($(".glyphicon-th").next().html() == '') {
									$(".glyphicon-th")
											.after(
													"<span class='glyphicon_font'>筛选</span>");
									if (!Helper.basic
											.hasPermission('finance:receive_detail:export')) {
										$(".export.btn-group").remove();
									}
									$(".glyphicon-export")
											.after(
													"<span class='glyphicon_font'>导出</span>");
								}
// 								$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
// 								var _tds = $("#bootTable tbody").find("tr:last").find("td");
// 								for (var index = 0; index < _tds.length; index++) {
// 									if (_tds.eq(index).text() == "-") {
// 										_tds.eq(index).text('');
// 									}
// 								}
							},
							onLoadError : function() {
								// alert("数据加载异常");
							},
							onClickRow : function(row, $element) {
								$element.addClass("tr_active").siblings().removeClass("tr_active");
							},
							onColumnSwitch : function(field, checked)
							{	
								// 在使用筛选增加或减少列时重置表格列拖动效果
								bootstrapTable_ColDrag($("#bootTable"));
							}
						});

		$("input[type='radio']").change(function() {
			$("#btn_search").click();
		})
	});

	//请求参数
	function queryParams(params) {
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['bankNo'] = $("#bankNo").val();
		params['transType'] = $("#transType").val() == "-1" ? null : $("#transType").val();
		params['tradeMode'] = $("#tradeMode").val() == "-1" ? null : $("#tradeMode").val();
		return params;
	}
	//ajax结果
	function responseHandler(res) {
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
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="交易日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">交易账户：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3" name="bankNo" id="bankNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">交易类型：</label>
							<span class="ui-combo-wrap form_text">
								<phtml:list name="transType" defaultValue="-1" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.AccountTransType"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">交易模式：</label>
							<span class="ui-combo-wrap form_text">
								<phtml:list name="tradeMode" defaultValue="-1" textProperty="text" cssClass="input-txt input-txt_3 hy_select2" type="com.huayin.printmanager.persist.enumerate.FinanceTradeMode"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="submit" class="nav_btn table_nav_btn" id="btn_search" name="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>

						</dd>
					</dl>
				</div>
			</div>
			<!--查询表单END-->

			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格部分End-->
		</div>
	</div>
</body>
</html>