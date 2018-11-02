<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/layout/css/pay/pay_style.css?${v }" />
<title>支付状态</title>
</head>
<body>
	<div id="paySucced">
		<%@include file="/WEB-INF/jsp/pay/common_header.jsp"%>
		<div class="fnContent">
			<div class="common_stepImgNav">
				<img src="${ctxStatic }/layout/images/pay/stepSuccess.png">
			</div>
			<div class="sc_text">
				<p>
					订单编号是：
					<span class="sc_orderNo">20162341234123413</span>
				</p>
			</div>
			<div class="sc_sectionDiv">
				<div class="sc_statContent1">
					<div class="sc_handImg">
						<div>
							<img src="${ctxStatic }/layout/images/pay/success.png">
						</div>
						<p>您的订单支付成功</p>
					</div>
					<div class="sc_fint">
						<h3>温馨提示：</h3>
						<ul>
							<li>
								1.如果有疑问请联系客服
								<a href="http://wpa.qq.com/msgrd?v=3&uin=2880157226&site=qq&menu=yes" class="btnA">联系客服</a>
							</li>
							<li>2.如果想查看相关的付款信息，请登录印管家的系统设置购买信息里查看相关的付款信息</li>
							<li>
								3.如果您有问题需要咨询，请联系客服:
								<span>400-800-8755</span>
							</li>
						</ul>
					</div>
				</div>
				<div class="sc_statContent2" style="display: none">
					<div class="sc_handImg">
						<div>
							<img src="">
						</div>
						<p>您的订单尚未支付完成</p>
					</div>
					<div class="sc_fint">
						<h3>温馨提示：</h3>
						<ul>
							<li>
								1.如果您的网购尚未成功，请
								<a href="" class="btnA">再尝试一次</a>
							</li>
							<li>2.如果您已经支付成功但是看到该页面，请不要着急，有可能是支付平台于我们的通讯有延迟，请过5分钟后再登陆印管家系统购买信息里查看订单状态。</li>
							<li>
								3.如果付款一段时间后，您的订单状态仍为未支付状态，请联系客服:
								<span>400-800-8755</span>
							</li>
						</ul>
					</div>
				</div>
				
				<div class="sc_productTb">
					<p>
						您可以登录印管家在系统设置购买信息里查看该订单信息
						<span></span>
						<a href="${ctx}" class="btnA">跳转印管家</a>

					</p>
					<table>
						<tr>
							<th>订单编号</th>
							<th>服务名称</th>
							<th>支付金额</th>
							<th>支付时间</th>
						</tr>

					</table>
				</div>
			</div>
		</div>
		<%@include file="/WEB-INF/jsp/pay/common_footer.jsp"%>
	</div>
</body>
<script type="text/javascript">
	$(function(){

	//禁止浏览器后退按钮
  	history.pushState(null, null, document.URL);
		 window.addEventListener('popstate', function () {
		 history.pushState(null, null, document.URL);
	});
	// url 参数解析
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = decodeURI(window.location.search).substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	};

	var orderId = getQueryString('orderNo')==null? '${orderId}':getQueryString('orderNo');

	$.ajax({
				url:'${ctx}/pay/findOrdrInfo',
				type:'get',
				data:{'orderId':orderId},
				success:function(data){
					if(typeof(data)=='string'){
						data = JSON.parse(data);
					}
					var dataObj = data.obj;
					console.log(dataObj)
					$('.sc_orderNo').text(dataObj.billNo);
					var trS = '<tr>\
							<td>'+dataObj.billNo+'</td>\
							<td>'+dataObj.productName+'</td>\
							<td>'+dataObj.price+'</td>\
							<td>'+getTime(dataObj.createTime)+'</td>\
						</tr>'
						function getTime (date) {
						return new Date(date).format();
					}
				if(dataObj.isPay=='YES'||'${status}'){
					$('.sc_statContent1').show();
					$('.sc_statContent2').hide();
				}else{
					$(".sc_statContent1").hide();
					$(".sc_statContent2").show()
				}
					$('.sc_productTb table').append(trS)
				}

			})
	})
</script>
</html>