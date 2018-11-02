<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>推荐有礼</title>
</head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
<body>
	<div id="pay_recommend" class="content">
		<div class="pr_award">
			<div class="pr_inform">
				<i></i>
				<span>推荐好友购买印管家，每家最高反馈1000元现金</span>
			</div>
			<div class="pr_table">
				<table>
					<thead>
						<tr>
							<th>好友购买</th>
							<th>金额</th>
							<th>您的奖励</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>印管家报价</td>
							<td>3000</td>
							<td>200</td>
						</tr>
						<tr>
							<td>印管家标准版</td>
							<td>8800</td>
							<td>400</td>
						</tr>
						<tr>
							<td>印管家标准版+微信端</td>
							<td>10800</td>
							<td>600</td>
						</tr>
						<tr>
							<td>印管家标准版+报价</td>
							<td>11800</td>
							<td>800</td>
						</tr>
						<tr>
							<td>印管家标准版+微信端+报价</td>
							<td>13800</td>
							<td>1000</td>
						</tr>
					</tbody>
				</table>
				<div class="pr_shearBt">
					<a href="${ctx}/wx/pay/view/share2" class="btn btPoster">生成邀请海报</a>
				</div>
			</div>
		</div>
		<div class="pr_short">
			<div class="pr_title">
				<i></i>
				<span>推荐排行榜</span>
			</div>
			<div class="pr_shortContent">
				<ul>
					<li>
						<div class="pr_kempImg">
							<img src="${ctxStatic}/wx/img/pay/guanjun.png">
						</div>
						<div class="pr_shotName">张**</div>
						<div class="pr_money">
							共获得
							<strong>8000元</strong>
							推荐返现
						</div>
					</li>
					<li>
						<div class="pr_kempImg">
							<img src="${ctxStatic}/wx/img/pay/guanjun.png">
						</div>
						<div class="pr_shotName">李**</div>
						<div class="pr_money">
							共获得
							<strong>5600元</strong>
							推荐返现
						</div>
					</li>
					<li>
						<div class="pr_kempImg">
							<img src="${ctxStatic}/wx/img/pay/guanjun.png">
						</div>
						<div class="pr_shotName">吴**</div>
						<div class="pr_money">
							共获得
							<strong>5000元</strong>
							推荐返现
						</div>
					</li>
					<li>
						<div class="pr_kempImg">
							<img src="${ctxStatic}/wx/img/pay/guanjun.png">
						</div>
						<div class="pr_shotName">冯**</div>
						<div class="pr_money">
							共获得
							<strong>3600元</strong>
							推荐返现
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
		<jsp:param value="check" name="module" />
	</jsp:include>
</body>
<script type="text/javascript">
	$(function(){
				// url 参数解析
		function getQueryString(name) {
		    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
		    var r = decodeURI(window.location.search).substr(1).match(reg);
		    if (r != null) {
		        return unescape(r[2]);
		    }
		    return null;
		};
		// $('.btPoster').attr('href','${ctx}/wx/pay/view/share2?linkName='getQueryString('linkName'))
		$('.btPoster').attr('href','${ctx}/wx/pay/view/share2?linkName='+getQueryString('linkName'))
	})
</script>
</html>