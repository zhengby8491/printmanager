<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>选择支付方式</title>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
</head>
<body>
	<div class="content" id="choosePay">
		<div class="cp_title">订单已经提交请尽快支付</div>
		<div class="cp_content">
			<div class="cp_orderNum">
				订单编号:
				<span>${purchaseRecord.billNo }</span>
			</div>
			<div class="cp_money">
				您需要支付：
				<strong>
					¥
					<span>${purchaseRecord.price+purchaseRecord.tax}</span>
					元
				</strong>
			</div>
			<div class="cp_payFn">
				<a class="button button-fill button-warning affirmPay" href="${ctx}/wx/pay/prepay?billNo=${purchaseRecord.billNo}&productId=${purchaseRecord.productId}&orderNo=${purchaseRecord.billNo}"> 确认支付</a>
			</div>
		</div>

	</div>
	<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
		<jsp:param value="check" name="module" />
	</jsp:include>
</body>
<script type="text/javascript">
		//禁止浏览器后退按钮
		console.log(document.RUL)
	  	 	history.pushState(null, null, document.URL);
			window.addEventListener('popstate', function () {
   				history.pushState(null, null, document.URL);
			  });
</script>
</html>