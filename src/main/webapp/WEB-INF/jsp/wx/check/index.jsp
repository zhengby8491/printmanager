<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_check.js' charset='utf-8'></script>
<title>订单审核</title>
</head>

<body>
	<div class="page page-current sum_content index_content">
		<ul id="itemlist" class="list-container list-block">
			<a href="${ctx}/wx/check/view/saleCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-9"></use>
	                    </svg>
					<span id="saleCheckQty" class="badge"></span>
					<span class="item_title">销售订单审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/check/view/purchCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-1"></use>
	                    </svg>
					<span id="purchCheckQty" class="badge"></span>
					<span class="item_title">采购订单审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/check/view/outSourceCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-5"></use>
	                    </svg>
					<span id="outsourceCheckQty" class="badge"></span>
					<span class="item_title">发外加工审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/check/view/receiveCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-8"></use>
	                    </svg>
					<span id="receiveCheckQty" class="badge"></span>
					<span class="item_title">收款单审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/check/view/paymentCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-"></use>
	                    </svg>
					<span id="paymentCheckQty" class="badge"></span>
					<span class="item_title">付款单审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/check/view/writeoffReceiveCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-6"></use>
	                    </svg>
					<span id="receiveWriteCheckQty" class="badge"></span>
					<span class="item_title">收款核销单审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/check/view/writeoffPaymentCheckList">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-2"></use>
	                    </svg>
					<span id="paymentWriteCheckQty" class="badge"></span>
					<span class="item_title">付款核销单审核</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
		</ul>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="check" name="module" />
		</jsp:include>
	</div>


	<script type="text/javascript">
    $(function()
    {       		
            HYWX.request({
                url:"${ctx}/wx/homepage/check",
                data:{},
                success:function(data){
                    for(var d in data){
                    	$("#"+d).text(data[d]);
                    	if(parseInt($("#"+d).text())>0){
                    		$("#"+d).show();
                    	}
                    }
                }
            })
    })
    </script>
</body>
</html>
