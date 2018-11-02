<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建付款核销单</title>
<script type="text/javascript">
$(function() {
	 //取消
	 $("#btn_cancel").click(function(){
 		 closeTabAndJump("付款核销单列表");
	 });
	/* 选择供应商 */
	$("#selectSupplier").click(function() {
				Helper.popup.show('选择供应商', Helper.basePath
						+ '/quick/supplier_select', '900', '490');
	});
	/* 选择付款核销源 */
	$("#select_payment_source").click(function() {
		var _supplierId=$("#supplierId").val();
		if(Helper.isEmpty(_supplierId))
		{
			Helper.message.alert("请先选择供应商");
			$("#supplierName").focus();
			return false;
		}
		Helper.popup.show('选择付款核销源', Helper.basePath+ '/quick/payment_select?multiple=true&supplierId='+_supplierId, '1000', '490');
	});
	
	/* 删除付款核销源 */
	$("table tbody").on("click","a[name='btn_del']",function() {
		$(this).parent().parent().remove();
		resetSequenceNum();
		sum();
		countOrderMoney();
	});
	/* 修改总折扣 */
	$("#order_discount").on("blur",function() {
		if(Number($("#order_discount").val())>Number($("#sum_money").val()))
		{
			$("#order_discount").val($("#sum_money").val());
		}
		countOrderMoney();
	});
	/* 修改明细付款核销 */
	$("table tbody").on("blur","input[name='detailList.money']",function() {
		sum();
		countOrderMoney();
	});
	// 初始付款核销日期
	$("#billTime").val(new Date().format('yyyy-MM-dd'));
	
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));

	
	//保存
	$("#btn_save,#btn_save_audit").click(function(){
		fixEmptyValue();
		if(Helper.isEmpty($("#supplierId").val()))
		{
			Helper.message.warn("请选择供应商")
			return false;
		}
		if(Helper.isEmpty($("#order_money").val())||Number($("#order_money").val())<=0)
		{
			Helper.message.warn("付款核销金额必须大于0");
			return false;
		}
		if(Number($("#order_money").val()).add(Number($("#order_discount").val()))!=Number($("#sum_money").val()))
		{
			Helper.message.warn("金额计算有误，总付款核销不等于明细汇总");
			return false;
		}
		
		if(Number($("#sum_money").val())>Number($("#sum_sourceBalanceMoney").val()))
		{
			Helper.message.warn("付款金额不能大于源单未付费金额");
			return false;	
		}
		if(Number($("#supplier_advanceMoney_after").val())<0)
		{
			Helper.message.warn("预付款不足，请修改明细金额");
			return false;	
		}
		var paramObj=$("#form_writeoffPayment").formToJson();
    	 	paramObj.isCheck=$(this).attr("id")=="btn_save_audit"?"YES":"NO";
		$(this).attr({"disabled":"disabled"});
  		Helper.request({
	    url: Helper.basePath+"/finance/writeoffPayment/save",  
	    data: paramObj,
	    success: function(data){  
	    	if(data.success){
				Helper.message.suc('已保存!');
				location.href=Helper.basePath + '/finance/writeoffPayment/view/' + data.obj.id;
			}else
			{
				Helper.message.warn('保存失败!'+data.message);
				$(this).removeAttr("disabled");
			}
	    },  
	    error: function(data){  
	    	 //console.log(data);
	    }  
	});  
	})
});
 

//获取返回付款核销源数组信息
function getCallInfo_paymentArray(rows)
{
	if(rows.length>0)
	{
		appendTr("detailList",rows);
		resetSequenceNum();
	}
	sum();
	countOrderMoney();
	sumCurrRecBalance();
}
//绑定"修改本次付款金额"事件
function sumCurrRecBalance(){
	$("#detailList").find("input[name='detailList.money']").off().on('blur',function(){
		var currPay = Number($(this).parent().parent().find("input[name='detailList.money']").val()); //本次付款金额
		var unPay = Number($(this).parent().parent().find("input[name='detailList.sourceBalanceMoney']").val()); //未付款金额
		var currPayBalance = unPay - currPay;
		$(this).parent().parent().find("input[name='curPaymentMoneyBalance']").val(toDecimal2(currPayBalance));
		sum();
	});
}
//获取返回信息
function getCallInfo_supplier(obj) {
	var old_supplierId=$("#supplierId").val();
	$("#supplierId").val(obj.id);
	$("#supplierName").val(obj.name);
	$("#supplier_advanceMoney_before").val(obj.advanceMoney);
	$("#supplier_advanceMoney_after").val(obj.advanceMoney);
	if(Helper.isNotEmpty(old_supplierId)&&obj.id!=old_supplierId)
	{//如果改变供应商，则清空table
		//清空table tr
		$("table tbody tr").remove();
	}
	sum();
	countOrderMoney();
}

function appendTr(tableId,rows)
{
	//console.log(rows);
	$.each(rows,function(){
		var _THIS=this;
		var _TR=$("<tr/>");
		var sourceDetailIdArray=$("table tbody tr td input[name='detailList.sourceDetailId']").map(function(){return this.value}).get();
	
		//console.log(sourceDetailIdArray);
		//console.log(this.id);
		//判断是否已存在明细ID
		if(Helper.isNotEmpty(sourceDetailIdArray)&&sourceDetailIdArray.contains(""+this.id))
		{//如果已存在则跳过本次循环
			return true;//continue;	
		} 
		$("#"+tableId).find("thead tr th").each(function(){
			var name=$(this).attr("name");
			var value=eval("_THIS."+name);
			value=value==undefined?"":value;
			switch(name)
			{
				case 'seq':
					_TR.append('<td></td>');
					break;
				case 'operator':
					_TR.append('<td class="td-manage"><input name="detailList.sourceId" type="hidden" readonly="true" value="'+_THIS.master.id+'"/><input name="detailList.sourceDetailId" type="hidden" readonly="true" value="'+_THIS.id+'"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
					break;	
				case 'master.billNo':
					_TR.append('<td><input name="detailList.sourceBillNo" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
					break;
				case 'master.billType':
 					_TR.append('<td><input name="detailList.sourceBillType" type="hidden" value="'+value+'"/>'+_THIS.master.billTypeText+'</td>');
					break;
				case 'master.createTime':
					_TR.append('<td>'+new Date(value).format("yyyy-MM-dd")+'</td>');
					break;
				case 'name':
				  	if(_THIS.master.billType=="PURCH_PK"){
				  	  _TR.append('<td><input name="detailList.materialId" class="tab_input" type="hidden" value="'+(_THIS.materialId||"")+'"/><input name="detailList.materialName" class="tab_input" readonly="true" type="text" value="'+(_THIS.materialName||"")+'"/>');
				  	  _TR.append('<input name="detailList.productId" type="hidden"/><input name="detailList.productName" type="hidden"/>');
				  	  _TR.append('<input name="detailList.procedureId" type="hidden"/><input name="detailList.procedureName" type="hidden"/></td>');
				  	}else if(_THIS.master.billType=="OUTSOURCE_OC"){
					   	if(_THIS.type=="PRODUCT"){
					   	 _TR.append('<td><input name="detailList.productId" class="tab_input" type="hidden" value="'+(_THIS.productId||"")+'"/><input name="detailList.productName" class="tab_input" readonly="true" type="text" value="'+(_THIS.productName||"")+'"/>');
					   	 _TR.append('<input name="detailList.materialId" type="hidden"/><input name="detailList.materialName" type="hidden"/>');
					   	 _TR.append('<input name="detailList.procedureId" type="hidden"/><input name="detailList.procedureName" type="hidden"/></td>');
					   	}else if(_THIS.type=="PROCESS"){
					   	 _TR.append('<td><input name="detailList.procedureId" class="tab_input" type="hidden" value="'+(_THIS.procedureId||"")+'"/><input name="detailList.procedureName" class="tab_input" readonly="true" type="text" value="'+(_THIS.procedureName||"") +'"/>');
					   	 _TR.append('<input name="detailList.materialId" type="hidden"/><input name="detailList.materialName" type="hidden"/>');
					   	 _TR.append('<input name="detailList.productId" type="hidden"/><input name="detailList.productName" type="hidden"/></td>');
					   	}
					 }else{
					   _TR.append('<td><input name="detailList.materialId" type="hidden"/><input name="detailList.materialName" type="hidden"/>');
					   _TR.append('<input name="detailList.procedureId" type="hidden"/><input name="detailList.procedureName" type="hidden"/>');
					   _TR.append('<input name="detailList.productId" type="hidden"/><input name="detailList.productName" type="hidden"/></td>');
					 }
				  	break;
				case 'style':
					_TR.append('<td><input name="detailList.style" readonly="true" class="tab_input" type="text" value="'+(_THIS.style||_THIS.specifications||"")+'"/></td>');
					break;
				case 'money':
				    if(_THIS.master.billType=="BEGIN_SUPPLIER"){
				  	  _TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.paymentMoney+'"/></td>');
				  	}else if(_THIS.master.billType == "FINANCE_ADJUST")
						{
							_TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="readonly" value="'+_THIS.adjustMoney+'"/></td>');
						}else{
				  	  _TR.append('<td><input name="detailList.sourceMoney" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
				  	}
					break;
				case 'paymentMoney':
				    if(_THIS.master.billType=="BEGIN_SUPPLIER"){
				  	  _TR.append('<td><input name="detailList.sourcePaymentMoney" class="tab_input" type="text" readonly="true" value="'+_THIS.paymentedMoney+'"/></td>');
				  	}else if (_THIS.master.billType == "FINANCE_ADJUST")
						{
							_TR.append('<td><input name="detailList.sourcePaymentMoney" class="tab_input" type="text" readonly="readonly" value="'+_THIS.receiveOrPayMoney+'"/></td>');
						}else{
				  	  _TR.append('<td><input name="detailList.sourcePaymentMoney" class="tab_input" type="text" readonly="true" value="'+value+'"/></td>');
				  	}
					break;
				case 'unPaymentMoney':
				    if(_THIS.master.billType=="BEGIN_SUPPLIER"){
				  	  _TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="'+(Number(_THIS.paymentMoney).subtr(Number(_THIS.paymentedMoney)))+'"/></td>');
				  	}else if (_THIS.master.billType == "FINANCE_ADJUST")
						{
							_TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="readonly" value="' + toDecimal2(Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
						}else{
				  	  _TR.append('<td><input name="detailList.sourceBalanceMoney" class="tab_input" type="text" readonly="true" value="'+(Number(_THIS.money).subtr(Number(_THIS.paymentMoney)))+'"/></td>');
				  	}
					break;
				case 'currentPaymentMoney':
				    if(_THIS.master.billType=="BEGIN_SUPPLIER"){
					   var readonly=(_THIS.paymentMoney-_THIS.paymentedMoney)<0?"readonly":"";
					  _TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" '+readonly+' value="'+(Number(_THIS.paymentMoney).subtr(Number(_THIS.paymentedMoney)))+'"/></td>');
				  	}else if (_THIS.master.billType == "FINANCE_ADJUST")
						{
							var readonly = (_THIS.adjustMoney - _THIS.receiveOrPayMoney) < 0 ? "readonly" : "";
							_TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" ' + readonly + ' value="' + (Number(_THIS.adjustMoney).subtr(Number(_THIS.receiveOrPayMoney))) + '"/></td>');
						}else{
				  	  var readonly=(_THIS.money-_THIS.paymentMoney)<0?"readonly":"";
					  _TR.append('<td><input name="detailList.money" class="constraint_decimal tab_input" type="text" '+readonly+' value="'+(Number(_THIS.money).subtr(Number(_THIS.paymentMoney)))+'"/></td>');
				  	}
					break;
				case 'curPaymentMoneyBalance':
					if(_THIS.master.billType=="BEGIN_SUPPLIER"){
					  _TR.append('<td><input name="curPaymentMoneyBalance" class="tab_input" type="text" readonly="true" value="'+0.00+'"/></td>');
				  	}else{
					  _TR.append('<td><input name="curPaymentMoneyBalance" class="tab_input" type="text" readonly="true" value="'+0.00+'"/></td>');
				  	}
					break;
			}
			
		});
		if(_THIS.money-_THIS.paymentMoney<0)
		{
			_TR.prependTo("#"+tableId);
		}else
		{
			_TR.appendTo("#"+tableId);
		}
		
	});
}
//重新设置序号  
function resetSequenceNum(){  
    $("table tbody tr").each(function(index){  
        $(this).find("td").first().html(++index);  
    });  
}
function sum()
{
	var sum_sourceMoney=0;
	var sum_sourcePaymentMoney=0;
	var sum_sourceBalanceMoney=0;
	var sum_sourceCurBalanceMoney=0; //本次付款余额
	var sum_money=0;
	$("table tbody tr").each(function(){
		sum_sourceMoney=(Number(sum_sourceMoney)).add(Number($(this).find("td input[name='detailList.sourceMoney']").val()));
		sum_sourcePaymentMoney=(Number(sum_sourcePaymentMoney)).add(Number($(this).find("td input[name='detailList.sourcePaymentMoney']").val()));
		sum_sourceBalanceMoney=(Number(sum_sourceBalanceMoney)).add(Number($(this).find("td input[name='detailList.sourceBalanceMoney']").val()));
		sum_sourceCurBalanceMoney=(Number(sum_sourceCurBalanceMoney)).add(Number($(this).find("td input[name='curPaymentMoneyBalance']").val()));
		sum_money=(Number(sum_money)).add(Number($(this).find("td input[name='detailList.money']").val()));
	});
	$("#sum_sourceMoney").val(toDecimal2(sum_sourceMoney));
	$("#sum_sourcePaymentMoney").val(toDecimal2(sum_sourcePaymentMoney));
	$("#sum_sourceBalanceMoney").val(toDecimal2(sum_sourceBalanceMoney));
	$("#sum_sourceCurBalanceMoney").val(toDecimal2(sum_sourceCurBalanceMoney));
	$("#sum_money").val(toDecimal2(sum_money));
}
function countOrderMoney()
{
	$("#order_money").val(Number($("#sum_money").val()).subtr(Number($("#order_discount").val())));
	$("#supplier_advanceMoney_after").val(Number($("#supplier_advanceMoney_before").val()).subtr(Number($("#order_money").val())));
	
	if(Number($("#supplier_advanceMoney_after"))<0)
	{
		$("#supplier_advanceMoney_after").css({"color":"red"});
	}else
	{
		$("#supplier_advanceMoney_after").attr({"color":""});	
	}
}
//制保留2位小数，如：2，会在2后面补上00.即2.00    
function toDecimal2(x) {    
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return false;    
    }    
    var f = Math.round(x*100)/100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
} 
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--导航按钮-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="财务管理-付款核销单-创建"></sys:nav>
				</div>
				<div class="top_nav">
					<shiro:hasPermission name="finance:writeoffPayment:create">
						<button class="nav_btn table_nav_btn" id="btn_save">保存</button>
					</shiro:hasPermission>
					<shiro:hasManyPermissions name="finance:writeoffPayment:create,finance:writeoffPayment:audit">
						<button class="nav_btn table_nav_btn" id="btn_save_audit">保存并审核</button>
					</shiro:hasManyPermissions>
					<button class="nav_btn table_nav_btn" id="btn_cancel">取消</button>
				</div>
			</div>
			<form id="form_writeoffPayment">
				<!--主表-订单表单-->
				<div class="form-container">
					<div class="form-wrap">
						<div class="cl form_content">
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_3">供应商名称：</label>
									<span class="ui-combo-wrap wrap-width">
										<input type="text" readonly class="input-txt input-txt_7" name="supplierName" id="supplierName" />
										<div class="select-btn" id="selectSupplier">...</div>
										<input type="hidden" readonly name="supplierId" id="supplierId" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">核销日期：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1 Wdate" onFocus="WdatePicker({lang:'zh-cn'})" name="billTime" id="billTime" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">核销人员：</label>
									<span class="ui-combo-wrap">
										<phtml:list items="${fns:basicList2('EMPLOYEE')}" valueProperty="id" textProperty="name" name="employeeId" cssClass="input-txt input-txt_1 hy_select2" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_3">使用前预付款：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_16" readonly="readonly" id="supplier_advanceMoney_before" value="0" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">使用后预付款：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" id="supplier_advanceMoney_after" value="0" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_3">本次核销金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="input-txt input-txt_1" readonly="readonly" name="money" id="order_money" value="0" />
									</span>
								</dd>
								<dd class="row-dd">
									<label class="form-label label_ui label_1">折扣金额：</label>
									<span class="ui-combo-wrap">
										<input type="text" class="constraint_decimal_negative input-txt input-txt_0" name="discount" id="order_discount" value="0" />
									</span>
								</dd>
							</dl>
							<dl class="cl row-dl">
								<dd class="row-dd">
									<label class="form-label label_ui label_3">备注：</label>
									<span class="form_textarea">
										<textarea class="noborder" name="memo" style="width: 774px" placeholder="说点什么...100个字符以内" onKeyUp="Helper.doms.textarealength(this,100)"></textarea>
										<p class="textarea-numberbar">
											<em>0</em>
											/100
										</p>
									</span>
								</dd>
							</dl>
						</div>
					</div>
				</div>

				<!-- 按钮栏Start -->
				<div class="btn-billbar" id="toolbar">
					<a id="select_payment_source" href="javascript:;" class="nav_btn table_nav_btn">
						<i class="fa fa-plus-square"></i>
						选择源单
					</a>
				</div>
				<!--按钮栏End-->


				<!--从表-订单表格-->
				<div>
					<div class="table-container">
						<table class="border-table" style="width: 1250px" id="detailList">
							<thead>
								<tr>
									<th width="40" name="seq">序号</th>
									<th width="40" name="operator">操作</th>
									<th width="120" name="master.billNo">源单编号</th>
									<th width="100" name="master.billType">源单类型</th>
									<th width="80" name="master.createTime">制单日期</th>
									<th width="100" name="name">产品/材料/工序</th>
									<th width="100" name="style">规格</th>
									<th width="100" name="money">单据金额</th>
									<th width="120" name="paymentMoney">已付款金额</th>
									<th width="120" name="unPaymentMoney">未付款金额</th>
									<th width="120" name="curPaymentMoneyBalance">本次付款余额</th>
									<th width="120" name="currentPaymentMoney">本次付款金额</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<td>合计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>
										<input id="sum_sourceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourcePaymentMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceBalanceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_sourceCurBalanceMoney" class="tab_input" type="text" readonly="readonly">
									</td>
									<td>
										<input id="sum_money" class="tab_input" type="text" readonly="readonly">
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="cl form_content">
					<dl class="cl row-dl-foot">

						<dd class="row-dd">
							<label class="form-label label_ui label_3">制 单 人：</label>
							<span class="ui-combo-wrap">
								<input name="createName" type="text" class="input-txt input-txt_1" readonly value="${loginUser.realName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">制单日期：</label>
							<span class="ui-combo-wrap">
								<input name="createTime" type="text" class="input-txt input-txt_1" readonly="readonly" value="<fmt:formatDate value="${order.createTime }" type="date" pattern="yyyy-MM-dd" />" id="createDate" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审 核 人：</label>
							<span class="ui-combo-wrap">
								<input name="checkUserName" type="text" class="input-txt input-txt_1" readonly value="${order.checkUserName}" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">审核日期：</label>
							<span class="ui-combo-wrap">
								<input name="checkTime" type="text" class="input-txt input-txt_1" readonly value="<fmt:formatDate value="${order.checkTime }" type="date" pattern="yyyy-MM-dd" />" />
							</span>
						</dd>

					</dl>
				</div>
			</form>
		</div>
	</div>
</body>
</html>