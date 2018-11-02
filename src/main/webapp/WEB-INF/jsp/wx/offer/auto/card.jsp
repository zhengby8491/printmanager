<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/offer/common.js?v=${v}' charset='utf-8'></script>
<title>吊牌卡片</title>
</head>
<body>
	<div class="page page-current">
		<div class="content content-padded offer_content">
			<form action="" id="quote_form" method="post">
				<table class="offer_table">
					<tr>
						<td>成品尺寸</td>
						<td>
							<input type="number" class="constraint_negative custom_height" id="length" name="styleLength" placeholder="长 ">
							<label>X</label>
							<input type="number" class="constraint_negative custom_width" id="width" name="styleWidth" placeholder="宽  ">
							mm
						</td>
					</tr>
					<tr>
						<td>印刷数量</td>
						<td>
							<input type="number" id="amount" class="constraint_negative amount" name="amount" >
							<!-- 设计费 -->
							<span class="design">
								<select class="designType input-txt input-txt_3 hy_select">
									<option value="SIMPLE">板材设计</option>
									<option value="NORMAL">来样设计</option>
									<option value="COMPLEX">创意设计 </option>
									<option value="" selected>无需设计</option>
								</select>
								<input type="number" class="constraint_negative designFee" style="background-color: rgb(241, 241, 241);" readonly="readonly" value=""><span>元/款</span>
							</span>
						</td>
					</tr>
					<!---------------------------------------- 公共 > 基础 --------------------------------------------->
					<%@include file="/WEB-INF/jsp/wx/offer/auto/_commonBase.jsp"%>
					<!---------------------------------------- 公共 > POSTDATA --------------------------------------------->
					<%@include file="/WEB-INF/jsp/wx/offer/auto/_commonPost.jsp"%>
				</table>
			</form>
			<div class="offer_bottom">
				<input id="btn_cal" class="button button-fill button-success btn_offer" type="button" value="点击报价">
			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="offer" name="module" />
		</jsp:include>
	</div>
</body>