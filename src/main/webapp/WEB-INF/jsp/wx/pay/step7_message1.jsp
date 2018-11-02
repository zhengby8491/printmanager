<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>购买信息编辑</title>
</head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
<body>
	<div class="content" id="pay_message1">
		<table>
			<tr>
				<td width="27%">购买时间</td>
				<td>
					<div class="pm_inputContent" style="width: 100%">
						<div class="dateItem startDate">
							<input type="text" id="my-input1" />
							<span>
								<i class="iconfont">&#xe633;</i>
							</span>
						</div>
						至
						<div class="dateItem endDate">
							<input type="text" id="my-input1" />
							<span>
								<i class="iconfont">&#xe633;</i>
							</span>
						</div>
					</div>
					<script type="text/javascript">
						$("#my-input1,#my-input2").calendar({});
					</script>
				</td>
			</tr>
			<tr>
				<td>联系电话</td>
				<td>
					<div class="btAfter">
						<input type="" name="">
					</div>

				</td>
			</tr>
			<tr>
				<td>是否支付</td>
				<td>
					<div class="btAfter">
						<input type="" name="">
					</div>
				</td>
			</tr>
			<tr>
				<td>开票信息</td>
				<td>
					<div class="btAfter">
						<input type="" name="">
					</div>


				</td>
			</tr>
			<tr>
				<td>公司名称</td>
				<td>
					<div class="btAfter">
						<input type="" name="">
					</div>

				</td>
			</tr>
			<tr>
				<td>联系人</td>
				<td>
					<div class="pm_inputContent">
						<input type="" name="">
					</div>
				</td>
			</tr>
			<tr>
				<td>邀请人</td>
				<td>
					<div class="pm_inputContent">
						<input type="" name="">
					</div>
				</td>
			</tr>
			<tr>
				<td>邀请电话</td>
				<td>
					<div class="pm_inputContent">
						<input type="" name="">
					</div>
				</td>
			</tr>

		</table>
		<div class="pm_btstyle">
			<a href="${ctx}/wx/pay/step8_message2" class="ensure">确定</a>
			<a href="" class="ensure">查询</a>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
		<jsp:param value="check" name="module" />
	</jsp:include>
</body>
</html>