<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_offer.js' charset='utf-8'></script>
<title>在线报价</title>
</head>

<body>
	<div class="page page-current sum_content index_content">
		<div class="content" style="overflow: auto">
			<ul id="itemlist" class="list-container list-block">
				<c:if test="${fns:hasPermission('offer:auto:single') }">
					<a href="${ctx}/wx/offer/view/single">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-single"></use>
	                    </svg>
							<span class="item_title">单张类</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:book') }">
					<a href="${ctx}/wx/offer/view/book">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-book"></use>
	                    </svg>
							<span class="item_title">画册书刊</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:carton') }">
					<a href="${ctx}/wx/offer/view/carton">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-caihe"></use>
	                    </svg>
							<span class="item_title">彩盒纸箱</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:note') }">
					<a href="${ctx}/wx/offer/view/note">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-bianqian"></use>
	                    </svg>
							<span class="item_title">便签信纸表格</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>
				<c:if test="${fns:hasPermission('offer:auto:card') }">
					<a href="${ctx}/wx/offer/view/card">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-tag"></use>
	                    </svg>
							<span class="item_title">吊牌卡片</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:sheath') }">
					<a href="${ctx}/wx/offer/view/sheath">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-fengtao"></use>
	                    </svg>
							<span class="item_title">封套类</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:glue') }">
					<a href="${ctx}/wx/offer/view/glue">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-glue"></use>
	                    </svg>
							<span class="item_title">不干胶</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:bill') }">
					<a href="${ctx}/wx/offer/view/bill">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-liandan"></use>
	                    </svg>
							<span class="item_title">联单类</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:envelope') }">
					<a href="${ctx}/wx/offer/view/envelope">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-email"></use>
	                    </svg>
							<span class="item_title">信封类</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>

				<c:if test="${fns:hasPermission('offer:auto:papercup') }">
					<a href="${ctx}/wx/offer/view/paperCup">
						<li class="sum_item">
							<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-cup"></use>
	                    </svg>
							<span class="item_title">纸杯类</span>
							<span class="icon icon-right cl"></span>
						</li>
					</a>
				</c:if>
			</ul>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="offer" name="module" />
		</jsp:include>
	</div>
</body>
</html>
