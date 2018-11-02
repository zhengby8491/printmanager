<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>状态</title>
</head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
<body>
	<div id="paySucced" class="content">
		<div class="fnContent">
			<div class="sc_text">
				<p>
					订单编号是：
					<span>${purchaseRecord.billNo }</span>
				</p>
			</div>
			<div class="sc_sectionDiv" <c:if test="${state==1 }">style="display: none"</c:if>>
				<div>
					<div class="sc_handImg">
						<div>
							<img src="${ctxStatic}/wx/img/pay/success.png">
						</div>
						<p>您的订单支付成功</p>
					</div>
					<div class="sc_fint">
						<h3>温馨提示：</h3>
						<ul>
							<li>1.如果有疑问,联系客服</li>
							<li>2.如果想查看相关的付款信息，请登录印管家的系统设置购买信息里查看相关的付款信息</li>
							<li>
								3.如果您有问题需要咨询，请联系客服:
								<p>400-800-8755</p>
							</li>
						</ul>
					</div>
				</div>
				<div <c:if test="${state==0 }">style="display: none"</c:if>>
					<div class="sc_handImg">
						<div>
							<img src="${ctxStatic}/wx/img/pay/cha.png">
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

					<table>
						<tr>
							<th>服务名称</th>
							<th>支付金额</th>
							<th>支付时间</th>
						</tr>
						<tr>
							<td>${purchaseRecord.productName }</td>
							<td>${purchaseRecord.price }</td>
							<td>
							<fmt:formatDate value="${purchaseRecord.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
						</tr>
					</table>
					<a href="${ctx}/wx/homepage/center" class="btnA goCenter">跳转印管家</a>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
		<jsp:param value="check" name="module" />
	</jsp:include>
</body>
</html>