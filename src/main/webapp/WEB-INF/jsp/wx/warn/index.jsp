<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_index.js' charset='utf-8'></script>
<title>未清预警</title>
</head>

<body>
	<div class="page page-current sum_content index_content">
		<ul id="itemlist" class="list-container list-block">
			<a href="${ctx}/wx/warn/view/saleNotProduction">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-wodedingdanicon"></use>
	                    </svg>
					<span id="workSaleSumQty" class="badge"></span>
					<span class="item_title">未生产订单</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/warn/view/saleNotDelivery">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-delivery"></use>
	                    </svg>
					<span id="saleDeliverSumQty" class="badge"></span>
					<span class="item_title">未送货订单</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/warn/view/purchNotStock">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-15"></use>
	                    </svg>
					<span id="purchStockSumQty" class="badge"></span>
					<span class="item_title">未入库采购</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/warn/view/outSourceNotArrive">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-gongwenbao"></use>
	                    </svg>
					<span id="arriveSumQty" class="badge"></span>
					<span class="item_title">未到货发外</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/warn/view/saleNotReceive">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-biaoqian"></use>
	                    </svg>
					<span id="receiveSumQty" class="badge"></span>
					<span class="item_title">未收款销售</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/warn/view/purchNotPayment">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-tubiao"></use>
	                    </svg>
					<span id="paymentPurchSumQty" class="badge"></span>
					<span class="item_title">未付款采购</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/warn/view/outSourceNotPayment">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-calculator"></use>
	                    </svg>
					<span id="paymentOutSourceSumQty" class="badge"></span>
					<span class="item_title">未付款发外</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
		</ul>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="warn" name="module" />
		</jsp:include>
	</div>


	<script type="text/javascript">
	 $(function() {
     	HYWX.request({
     		url:'${ctx}/wx/homepage/warn',
             data:{},
             success:function(data){
             	for(var i in data){
             		$("#"+i).text(data[i]);
             		if(data[i]>0){
             			$("#"+i).show();
             		}
             	}
             }
     	})
     });
    </script>
</body>
</html>
