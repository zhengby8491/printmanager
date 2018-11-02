<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/layout/css/pay/pay_style.css?${v }" />
<title>购买信息</title>
<style type="text/css">
#payMessage .order-info {
	width: 1100px;
	margin: 0 auto;
}

#payMessage .border-table {
	width: 1200px;
	margin: 0 auto;
	font-size: 14px;
}
</style>
</head>
<body>
	<div id="payMessage">
		<%@include file="/WEB-INF/jsp/pay/common_header.jsp"%>
		<div class="cl order-info">
			<dl class="cl row-dl">
				<dd class="row-dd">
					<label class="form-label label_ui label_1">购买日期：</label>
					<span class="ui-combo-wrap form_text">
						<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deliverDateMax\')}'})" class="input-txt input-txt_21 Wdate" id="deliverDateMin" name="deliverDateMin" value="<fmt:formatDate value="${deliverDateMin }" type="date" pattern="yyyy-MM-dd"/>" />
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_1 " style="text-align: center; margin-left: -2px; width: 63px">至</label>
					<span class="ui-combo-wrap form_text">
						<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deliverDateMin\')}'})" class="input-txt input-txt_21 Wdate" id="deliverDateMax" name="deliverDateMax" value="<fmt:formatDate value="${deliverDateMax }" type="date" pattern="yyyy-MM-dd"/>" />
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui">联系电话：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="customerName" name="customerName" />
						<div class="select-btn" id="customer_quick_select">...</div>
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_1">邀请人：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="productName" name="productName" />
						<div class="select-btn" id="product_quick_select">...</div>
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_6">邀请人电话：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="productName" name="productName" />
						<div class="select-btn" id="product_quick_select">...</div>
					</span>
				</dd>
			</dl>
			<dl class="cl hide_container" id="more_div">
				<dd class="row-dd">
					<label class="form-label label_ui label_1">是否支付：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="productName" name="productName" />
						<div class="select-btn" id="product_quick_select">...</div>
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_1">开票信息：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="productName" name="productName" />
						<div class="select-btn" id="product_quick_select">...</div>
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_1">服务名称：</label>
					<span class="ui-combo-wrap form_text">
						<input type="text" class="input-txt input-txt_21" name="saleBillNo" id="saleBillNo" />
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_1">公司名称：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="productName" name="productName" />
						<div class="select-btn" id="product_quick_select">...</div>
					</span>
				</dd>
				<dd class="row-dd">
					<label class="form-label label_ui label_6">联系人：</label>
					<span class="ui-combo-wrap">
						<input type="text" class="input-txt input-txt_21" id="style" name="style" />
					</span>
				</dd>
			</dl>

		</div>
		<div class="pay_messageTable">
			<table class="border-table" id="pm_bootTable">
			</table>
		</div>
		<%@include file="/WEB-INF/jsp/pay/common_footer.jsp"%>
	</div>
	<script type="text/javascript">
	$('#pm_bootTable').bootstrapTable({
	  url: Helper.basePath + "/produce/work/ajaxDetailList",
	  method: "post",
      contentType: 'application/json', //设置请求头信息  
      dataType: "json",
      pagination: true, // 是否显示分页（*）
      sidePagination: 'server',//设置为服务器端分页
      pageList: Helper.bootPageList,
      queryParamsType: "",
      pageSize: 10,
      pageNumber: 1,
      // queryParams: queryParams,//参数
      // responseHandler: responseHandler,
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
      cookieIdTable: "print_produce_work_master",//必须制定唯一的表格cookieID

      uniqueId: "id",//定义列表唯一键
      columns: [{
      	field : 'index',
      	title : '序号',
      	width : 40,
      	formatter : function(value, row, index) {
      		return index + 1;
      	}
      },
      {
      	field : 'byTime',
      	title : '购买时间',
      	width : 70,
      	formatter : function(value, row, index) {
      		if (value) { return new Date(value).format("yyyy-MM-dd"); 
      	}
      }
	  },
	  {
	  	field: 'orderNum',
	  	title: '订单号',
	  	width:100,
	  	formatter: function(value, row, index) {
	  		return row.master.billTypeText;
	  	}
	  },{
	  	field: 'serveName',
	  	title: '服务名称',
	  	width : 100,
	  	formatter : function(value, row, index) {
	  		return new Date(row.master.createTime).format("yyyy-MM-dd");
	  	}
	  },{
	  	field: 'money',
	  	width : 50,
	  	title: '金额',
	  	formatter : function(value, row, index) {
	  		return row.master.billNo;
	  	}
	  },{
	  	field: 'deliveryTime',
	  	title: '税额',
	  	width:80,
	  	formatter: function(value, row, index) {
	  		if (value) { return new Date(value).format("yyyy-MM-dd"); }
	  	}
	  },
	  {
	  	field: 'userName',
	  	width : 60,
	  	title: '用户名'

	  }, {
	  	field: 'customerBillNo',
	  	title: '公司名称',
	  	width: 120,

	  },{
	  	field: 'contactName',
	  	width : 60,
	  	title: '联系人'
	  }, {
	  	field: ' inviteName',
	  	title: '邀请人',
	  	width:60
	  }, {
	  	field: ' InvitePhoneNob',
	  	width : 120,
	  	title: '邀请人电话'
	  },{
	  	field: ' bonus',
	  	title: '奖金金额',
	  	width: 120
	  },{
	  	field: 'style',
	  	title: '发票信息',
	  	width:100
	  }, {
	  	field: 'unitName',
	  	title: '支付',
	  	width:60
	  }, 
	  {
  	field: 'operator',
  	title: '操作',
  	width:120,
  	formatter: function(value, row, index) {
  		var operator = "";
  		if(Helper.basic.hasPermission('produce:work:list')){
  			operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="work_view('
  			+ row.master.id
  			+ ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a></span>';}
  			if (Helper.isNotEmpty(row.master.isCheck)
  				&& row.master.isCheck == 'NO') {
  				operator = "";
  			if(Helper.basic.hasPermission('produce:work:list')){
  				operator += '<span class="table_operator"><a title="查看" href="javascript:;" onclick="work_view('
  				+ row.master.id
  				+ ')" style="padding: 0 8px; color: green"><i class="fa fa-search"></i></a>';}
  				if(Helper.basic.hasPermission('produce:work:edit')){
  					operator += '<a title="编辑" href="javascript:;" onclick="work_edit('
  					+ row.master.id
  					+ ')" style="padding: 0 8px; color: green"><i class="fa fa-pencil"></i></a>';}
  					if(Helper.basic.hasPermission('produce:work:del')){
  						operator += '<a title="删除" href="javascript:;" onclick="work_del(this,'
  						+ row.master.id
  						+ ')" style="padding: 0 8px; color: red"><i class="fa fa-trash-o"></i></a></span>';}
  					}
  					return operator;
  				}
  			}],
  			onDblClickRow: function(row) {
  				work_view(row.masterId);
  			},
  			onClickRow : function(row,$element){
  				$element.addClass("tr_active").siblings().removeClass("tr_active");
  			}
  		});
  	</script>
</body>
</html>