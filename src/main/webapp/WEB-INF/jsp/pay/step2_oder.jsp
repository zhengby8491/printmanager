<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/layout/css/pay/pay_style.css?${v }" />
<title>订单</title>
</head>
<body>
	<div id="payOder">
		<%@include file="/WEB-INF/jsp/pay/common_header.jsp"%>
		<form id="payOderForm">
			<input type="hidden" name="productId" value="${product.id}" />
			<div class="oderContent">
				<div class="common_stepImgNav">
					<img src="${ctxStatic}/layout/images/pay/stepOrder.png">
				</div>
				<div class="oc_contact cleaerFloat">
					<div class="oc_contactLeft oc_left_div">
						<h3>
							<span>联系信息</span>
						</h3>
					</div>
					<div class="oc_contactRight oc_right_div">
						<div class="oc_contactRightDiv cleaerFloat">
							<div class="oc_username stare">
								<span class="label_9">单位名称：</span>
								<input class="input-txt_20 input_readonly" type="text" name="companyName" value="${company.name}" readonly="true">
							</div>
							<div class=" ">
								<span class="label_9">邀请人：</span>
								<input type="text" name="inviter">
							</div>
							<div class=" stare">
								<span class="label_9">真实姓名：</span>
								<input class="input-txt_20 input_readonly" type="" name="linkMan" value="${company.linkName}" readonly="true">
							</div>
							<div class="">
								<span class="label_9">邀请人电话：</span>
								<input type="" name="inviterPhone">
							</div>
							<div class="stare">
								<span class="label_9">联系电话：</span>
								<input class="input-txt_20 input_readonly" type="" name="telephone" readonly="true" value="${company.tel}">
							</div>
						</div>
					</div>
				</div>
				<div class="oc_invoice cleaerFloat">
					<div class="oc_invoiceLeft oc_left_div ">
						<p>发票信息</p>
					</div>
					<div class="oc_invoiceRight oc_right_div">
						<ul class="cleaerFloat">
							<li>
								<input type="radio" name="invoiceInfor" id="inviceId1" value="0" checked="checked">
								<label for="inviceId1">
									不需要发票
									</lable>
							</li>
							<li>
								<input type="radio" name="invoiceInfor" value="1" id="inviceId2" data-tax="0.06">

								<label for="inviceId2">
									增值税发票(6%)
									</lable>
							</li>
							<li>
								<input type="radio" name="invoiceInfor" value="2" id="inviceId3" data-tax="0.1">

								<label for="inviceId3">
									增值税发票(17%)
									</lable>
							</li>
						</ul>
						<p>如需开发票，请于一个月内联系客服申请</p>
					</div>
				</div>
				<div class="oc_serveInventory cleaerFloat">
					<div class="oc_serveInventoryLeft oc_left_div">
						<p>产品清单</p>
					</div>
					<div class="oc_serveInventoryRight oc_right_div ">
						<table>
							<tbody>
								<tr>
									<th>服务名称</th>
									<th>原价</th>
									<th>折扣</th>
									<th>优惠价</th>
								</tr>
								<tr>
									<td style="height: 40px;">${product.name}</td>
									<td>${product.originalPrice}元</td>
									<td>${product.originalPrice - product.price}元</td>
									<td class="colorDanger">${product.price}元</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="oc_close cleaerFloat ">
					<div class="oc_closeLeft oc_left_div">
						<p>订单结算</p>
					</div>
					<div class="oc_closeRight oc_right_div">
						<div class="oc_totalPrice">
							<ul>
								<li>
									<span class="w_400">商品总金额：</span>
									<strong style="float: left">¥${product.originalPrice}元</strong>
								</li>
								<li>
									<span class="w_400">优惠总金额：</span>
									<strong style="float: left">¥${product.originalPrice - product.price}元</strong>
								</li>
								<li>
									<span class="w_400">税前应付金额：</span>
									<strong style="float: left">¥${product.price}元</strong>
								</li>
								<li>
									<span class="w_400">税额：</span>
									<strong class="oc_taxNm" style="float: left">¥0元</strong>
								</li>
								<li>
									<span class="w_400">你需要支付：</span>
									<strong class="colorDanger oc_endManey" style="float: left; font-weight: bold">¥${product.price}元</strong>
								</li>
							</ul>

							<div>

								<a class="payBtn bgcolor_danger contracter oc_greePack" href="http://pan.baidu.com/s/1geFhoyR " target="_block">下载合同</a>
								<input class="payBtn bgcolor_danger oc_greePack " type="submit" value="提交订单" />
							</div>
						</div>
						<div class="oc_contract">
							<div style="padding: 20px;">
								<h3 style="text-align: center;">软件购销合同(样板)</h3>
								<p style="text-align: center">
									合同编号
									<span style="text-decoration: underline;">LSD20170711580</span>
								</p>
								<ul style="width: 100%" class="cleaerFloat">
									<li style="width: 50%">
										甲方：
										<input class="textinput" readonly="readonly" type="text" class="textinput" name="">
									</li>
									<li style="width: 50%">
										地址：
										<input type="text" readonly="readonly" class="textinput" name="">
									</li>
									<li style="width: 50%">
										联系人：
										<input type="text" readonly="readonly" class="textinput" name="">
									</li>
									<li style="width: 50%">
										电话：
										<input type="text" readonly="readonly" class="textinput" name="">
									</li>
								</ul>
								<ul style="width: 100%" class="cleaerFloat">
									<li style="width: 50%">
										乙方：
										<input class="textinput" type="text" value="上海印智软件股份有限公司" readonly="readonly" name="">
									</li>
									<li style="width: 50%">
										地址：
										<input class="textinput" type="text" value="上海浦东张江高科祖冲之路2277弄世和大厦1号楼511室" readonly="readonly" name="">
									</li>
									<li style="width: 50%">
										联系人：
										<input class="textinput" type="text" value="颜金平" readonly="readonly" name="">
									</li>
									<li style="width: 50%">
										电话：
										<input class="textinput" type="text" value="18025381551" readonly="readonly" name="">
									</li>
									<li style="width: 50%">
										户名：
										<input class="textinput" type="text" value="何艳琴" readonly="readonly">
									</li>
									<li style="width: 50%">
										开户行：
										<input class="textinput" type="text" value="平安银行上海虹口支行   账号：6225,3800,5933,8824" readonly="readonly" name="">
									</li>
								</ul>
								<div>
									<p>甲乙双方本着友好合作、互相促进的原则，甲方向乙方购买印管家ERP管理软件一套。双方申明，双方都已理解并认可了本合同的所有内容，同意承担各自应承担的权利和义务，忠实地履行本合同。</p>
									<h5>一、双方责任</h5>
									<p>1、甲方责任</p>
									<p>a)甲方应向乙方提供必要的资料并派专人负责与乙方联络、协调。</p>
									<p>b)甲方必须按照合同的付款方式支付相应款项, 不能按时支付合同费用,视甲方违约,承担违约责任。</p>
									<p>c)甲方允许乙方的广告语，以便宣传</p>
									<p>2、乙方责任</p>
									<p>a)乙方承诺在履行合同时不进行有损甲方形象、声誉等行为，并保守在履行本合同过程中获知的对方商业秘密 。</p>
									<p>b)乙方所提供的软件含ERP管理软件，不含其它相关软件。</p>
									<p>c)乙方如需实施，实施费用另计：远程实施1000元/天，上门实施为二天起， 1500元/天。</p>
									<h5>二、合同金额及付款方式：</h5>
									<p>1、本产品原价为人民币: ￥8800元 （不含税），推广价格为￥6800元 （不含税）大写：人民币陆仟捌佰元整。本合同签订后当天付全款，甲方向乙方支付合同全款，否则将不享受优惠价格。</p>
									<p>2．付款方式：本合同签定后，签订当天内，甲方向乙方支付合同金额款即6800 元人民币。</p>
									<p>3.售后服务费用 ￥2000 元/年，软件安装的第一年免收服务费。服务费付款日期为签订合同日期的下一年度，如到期未付，乙方将无条件停止软件的售后服务。</p>
									<p>附：一式两份，本合同签署时，请您加盖双方骑缝章</p>
									<p>本合同传真件盖章具有同等法律效力，</p>
								</div>
								<ul style="width: 100%" class="cleaerFloat">
									<li style="width: 50%">甲方或授权代表签字盖章</li>
									<li style="width: 50%">乙方或授权代表签字盖章</li>
								</ul>

							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<%@include file="/WEB-INF/jsp/pay/common_footer.jsp"%>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		var taxNm = 0
		$('.oc_invoiceRight input[type="radio"]').each(function(index,obj){
			//发票选择
			$('.oc_invoiceRight input[type="radio"]').change(function(){
				//当需要开发票时，切换合同版本，计算相应的税额及总价
				if($(obj).is(':checked')&& $(obj).attr('data-tax')!=null){
					taxNm = $(obj).attr('data-tax');
					$('.contracter').attr('href','http://pan.baidu.com/s/1hsCD5x6')
					//税额，向上取证
					var taxNmMoney = (taxNm*"${product.price}").toFixed(2);
					$('.oc_taxNm').text('¥'+taxNmMoney+'元');
					//支付金额
					var lastPrice = Number(${product.price}+Number(taxNmMoney));
					$('.oc_endManey').text('¥'+lastPrice.toFixed(2)+'元');
				};
				//当不需要开发票时，切换合同版本，计算相应的税额及总价
				if($(obj).attr('data-tax')==null){
					$('.oc_endManey').text('¥'+(${product.price}).toFixed(2)+'元');
					$('.oc_taxNm').text('0'+'元');
					$('.contracter').attr('href','http://pan.baidu.com/s/1geFhoyR')
				}
			})
		});
		$("#payOderForm").validate(
		{
			submitHandler : function(form) { 
				//判断是否有已下单，但未支付的订单
				$.ajax({
					url:Helper.basePath+"/sys/buy/isPay",
					type:'get',
					async:false,
					success:function(data){
						data = JSON.parse(data)
						if(!data.obj)
						//有待支付的订单
						layer.alert('您有未支付订单，请到购买信息里面查看继续购买');
						return false;
					}
				})	
				Helper.Remote.fromSubmit(form, {
					type : "post",
					dataType : "json",
					url:Helper.basePath+"/pay/savaOrder",
					async:false,
					success : function(data) {
						if (data.success) {
							debugger;
							location.href=Helper.basePath+"/pay/step3/"+data.obj.billNo;
							Helper.popup.close();
							return false;
						} else {
							layer.alert('创建失败：'+data.message);
						}
					}
				});  
			},
			rules : {
				code : {
					required : true,
				},
				name : {
					required : true,
				}
			},
			onkeyup : false,
			focusCleanup : true
	// 如果该属性设置为True,
	// 那么控件获得焦点时，移除出错的class定义，隐藏错误信息
});
});

</script>
</html>