<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.morea {
	position: absolute;
	display: block;
	width: 150px;
	height: 150px;
	border-radius: 150px;
}

.morediv {
	position: absolute;
	top: -134px;
	background-color: #f7f7f8;
	display: none;
	left: -40%;
	height: 6.4rem;
	padding: 0.2rem 0;
	border: 1px solid #ddd;
	border-radius: 5px;
}

.morediv::after {
	width: 20px;
	height: 20px;
	background-color: red;
}

.morediv ul li {
	border-bottom: 1px solid #ddd;
	padding: 0 4px;
	width: 3.5rem;
}

.morediv ul li:nth-child(4) {
	border: none;
}

.menuContent {
	width: 160px;
	height: 80px;
	border: 3px solid #d5e0d7;
	position: absolute;
	top: -79px;
	left: -47px;
	display: none;
	border-radius: 80px 80px 0 0;
}

.inversion2 {
	position: relative;
	top: 16px;
}

.morea ul li .table_itmeHeight {
	height: 1.5rem;
}
</style>
<script type="text/javascript">

function purchaseBtnJudge(){
	var type=true;
	$.ajax({
		url:HYWX.basePath+"/sys/buy/isPay",
		type:'get',
		async:false,
		success:function(data){
			data = JSON.parse(data)
			if(!data.obj){
		      $.alert('您有未支付订单，请前往购买信息继续支付',function(){
					return false;
				});
		      type=false;
			}
		}
	}) 
	return type;
}

//$(function(){
/* 	$("#purchaseBtn").click(function(){
		$.ajax({
			url:HYWX.basePath+"/sys/buy/isPay",
			type:'get',
			async:false,
			success:function(data){
				data = JSON.parse(data)
				alert(data.obj);
				if(data.obj){
			    $.alert('您有未支付订单，请前往购买信息继续支付',function(){
						return false;
					});
				}else{
					window.location.href="${ctx}/wx/pay/view/step1/choose";
				}
			}
		}) */
/* 			$.alert("您有未支付订单，请前往购买信息继续支付",function(){
		HYWX.request({
    		url:HYWX.basePath+"/sys/buy/isPay",
            data:{},
            success:function(data){
            	if(data.success){
            	    window.location.href=HYWX.basePath+"/wx/homepage/center";
            	}else{
            	    $.toast(""+data.message,2000,"mytoast");
            	}
            },
            error:function(data){
            	$.toast(""+data.message,2000,"mytoast");
            }
   		})
	}) */        
		
	//})
//})
</script>
</head>
<body>
	<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getConfig('SITE_BASE_PATH')}" />

	<span id="to_top" class="to_top">
		<i class="icon iconfont icon-top"></i>
	</span>
	<nav class="bar bar-tab">
		<div>
			<a class="tab-item external <c:if test="${param.module eq 'offer' }">active</c:if>" href="${ctx}/wx/offer/view/index">
				<span class="icon iconfont icon-baojia"></span>
				<span class="tab-label">在线报价</span>
			</a>


			<a class="tab-item external table_itmeHeight <c:if test="${param.module eq 'check' }">active</c:if>" href="${ctx}/wx/check/view/index">
				<span class="icon iconfont icon-daishenhedingdan"></span>
				<span class="tab-label">订单审核</span>
			</a>
			<a class="tab-item inversion4 external table_itmeHeight <c:if test="${param.module eq 'schedule' }">active</c:if>" href="${ctx}/wx/schedule/view/index">
				<span class="icon iconfont icon-7shenqiajindu"></span>
				<span class="tab-label">进度查询</span>
			</a>
			<a class="tab-item inversion1 external table_itmeHeight <c:if test="${param.module eq 'warn' }">active</c:if>" href="${ctx}/wx/warn/view/index">
				<span class="icon iconfont icon-yujingshezhi"></span>
				<span class="tab-label">未清预警</span>
			</a>
			<a class="tab-item inversion3 external table_itmeHeight <c:if test="${param.module eq 'sum' }">active</c:if>" href="${ctx}/wx/sum/view/index">
				<span class="icon iconfont icon-rectangle12"></span>
				<span class="tab-label">数据分析</span>
			</a>
			<div class="tab-item external morea">
				<span class="icon iconfont">&#xe74f;</span>
				<span class="tab-label">更多</span>
				<div class="morediv">

					<ul>
						<li class="we inversion">
							<a class="tab-item inversion1 external table_itmeHeight  <c:if test="${param.module eq 'center' }">active</c:if>" " href="${ctx}/wx/homepage/center">
								<!-- <span
			class="icon iconfont icon-gerenzhongxin"></span> -->
								<span class="">个人中心</span>
							</a>

						</li>
						<li class="inversion">

							<a class="tab-item inversion1 external table_itmeHeight  <c:if test="${param.module eq 'pay' }">active</c:if>" " onclick="return purchaseBtnJudge()" href="${ctx}/wx/pay/view/step1/choose">
								<!-- <span class="icon iconfont">&#xe608;</span> -->
								<span class="" id="purchaseBtn">立即购买</span>
							</a>

						</li>
						<li class="inversion">

							<a class="tab-item inversion1 external tiaomashangbao table_itmeHeight  <c:if test="${param.module eq 'codereport'}">active</c:if>">
								<!-- <span class="icon iconfont ">&#xe613;</span> -->
								<span class="">条码上报</span>
							</a>

						</li>
						<li class="inversion">
							<a class="tab-item inversion1 external table_itmeHeight  <c:if test="${param.module eq 'pay' }">active</c:if>" " href="${ctx}/wx/pay/view/buy/list">
								<!-- <span class="icon iconfont">&#xe608;</span> -->
								<span class="">购买信息</span>
							</a>

						</li>

					</ul>
				</div>
			</div>
	</nav>
	</div>
<body>
	<script src="https://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
	<script>
<%-- 		var uri = '<%=request.getRequestURI()%>'; --%>
		$('.morea').click(function(){
			if($('.morediv').css('display')=='none'){
				$('.morediv').show()
			}else{
				$('.morediv').hide()
			}
		})

		function initWXScan(callback) {
			$.ajax({
				url : '${ctx}/wx/codereport/getConfigInfo',
				type : 'get',
				success : function(data) {
				data = JSON.parse(data).obj;
					// 扫一扫配置
					console.log( data.signature)
					wx.config({
						debug :false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
						appId : data.appid, // 必填，公众号的唯一标识  
						timestamp : data.timestamp, // 必填，生成签名的时间戳  
						nonceStr : data.nonceStr, // 必填，生成签名的随机串  
						signature : data.signature,// 必填，签名，见附录1  
						jsApiList : [ 'checkJsApi', 'chooseImage', 'previewImage', 'uploadImage', 'downloadImage', 'getNetworkType',//网络状态接口
						'openLocation',//使用微信内置地图查看地理位置接口
						'getLocation', //获取地理位置接口
						'hideOptionMenu',//界面操作接口1
						'showOptionMenu',//界面操作接口2
						'closeWindow', ////界面操作接口3
						'hideMenuItems',////界面操作接口4
						'showMenuItems',////界面操作接口5
						'hideAllNonBaseMenuItem',////界面操作接口6
						'showAllNonBaseMenuItem',////界面操作接口7
						'scanQRCode'// 微信扫一扫接口
						]
					// 必填，需要使用的JS接口列表，所有JS接口列表见附录2  
					});
					if($.isFunction(callback)) callback.call(this);
				}
			})
		}
		//var uri = '${ctx}${param.uri }';
		$(document).on('click', '.tiaomashangbao', function() {
			$.showIndicator();
			setTimeout(function () {
		          $.hideIndicator();
		          return false;
		      }, 30000);
			initWXScan(function(){
				 $.hideIndicator()
				wx.ready(function() {
					wx.scanQRCode({
						needResult : 1,
						desc : 'scanQRCode desc',
						success : function(res) {
							var attrBillNo = (res.resultStr.split(','))[1];
							if(/^MO/g.test(attrBillNo)==true){
								return $.alert('微信上报只支持工序上报')
							}else{
								window.location.href='${ctx}/wx/codereport/scan/index?billno='+attrBillNo;	
							}
						}
					});
					//初始化jsapi接口 状态
					wx.error(function(res) {
						//alert("调用微信jsapi返回的状态:"+res.errMsg);
					});
				});
			});
		})
	</script>
	<!-- 模板 -->
	<table id="template"style="display:none;" >
		<tbody>
			<tr class="copyTr">
				<td>
					<!-- 印刷纸张 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.printName" readonly="readonly">
				</td>
				<td>
					<!-- 颜色 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.printColor" readonly="readonly">
				</td>
				<td>
					<!-- 加工工序 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.printProcedure" readonly="readonly">
				</td>
				<td>
					<!-- 数量 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.amount" readonly="readonly">
				</td>
				<td>
					<!-- 单价 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.price" readonly="readonly">
				</td>
				<td>
					<!-- 金额 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.fee" readonly="readonly">
				</td>
				<td>
					<!-- 含税单价 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.taxPrice" readonly="readonly">
				</td>
				<td>
					<!-- 含税金额 -->
					<input type="text" class="tab_input " name="offerOrderQuoteOutList.taxFee" readonly="readonly">
				</td>
			</tr>
		</tbody>
	</table>
	<!-- 模板 -->
	<table id="template_nb2" style="display:none;" >
		<tbody>
			<tr class="copyTr">
				<td>
					<input type="text" class="tab_input " readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.amount" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.paperFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.printFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.procedureFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.ohterFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.freightFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.costMoney" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.profitFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.untaxedFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.untaxedPrice" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.taxFee" readonly="readonly">
				</td>
				<td>
					<input type="text" class="tab_input " name="offerOrderQuoteInnerList.taxPrice" readonly="readonly">
				</td>
			</tr>
		</tbody>
	</table>
</html>
