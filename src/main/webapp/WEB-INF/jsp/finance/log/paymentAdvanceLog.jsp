<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>预付款日志列表</title>
<script type="text/javascript">
	
	$(function(){
	  //选择供应商
	  $("#supplier_quick_select").click(function() {
			Helper.popup.show('选择供应商', Helper.basePath
					+ '/quick/supplier_select?multiple=false', '900', '490');
		});
	  
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
									+ "/finance/log/ajaxPaymentAdvanceLogList",
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
										field : 'createTime',
										title : '创建时间',
										width: 80,
						                formatter: function(value, row, index)
						                {
						                    if(value) { return new Date(value).format("yyyy-MM-dd"); }
						                }
									},
									{
										field : 'billTypeText',
										title : '单据类型',
										width : 80
									},
									{
										field : 'supplierName',
										title : '供应商名称',
										width : 120
									},
									{
										field : 'employeeName',
										title : '付款人',
										width : 80
									},
									{
										field : 'money',
										title : '预付款',
										width: 80
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
//								$("#bootTable tbody").find("tr:last").find("td:first").text("合计");
//								var _tds = $("#bootTable tbody").find("tr:last").find("td");
//								for (var index = 0; index < _tds.length; index++) {
//									if (_tds.eq(index).text() == "-") {
//										_tds.eq(index).text('');
//									}
//								}
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
	
	//获取返回信息
	function getCallInfo_supplier(obj) {
		$("#supplierName").val(obj.name);
	}
	
	//请求参数
	function queryParams(params) {
		params['dateMin'] = $("#dateMin").val();
		params['dateMax'] = $("#dateMax").val();
		params['supplierName'] = $("#supplierName").val();
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
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">供应商名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_7" id="supplierName" name="supplierName" value="${supplierName}" />
								<div class="select-btn" id="supplier_quick_select">...</div>
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
</body>
</html>