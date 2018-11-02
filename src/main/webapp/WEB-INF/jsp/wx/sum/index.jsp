<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_sum.js' charset='utf-8'></script>
<title>数据分析</title>
</head>

<body>
	<div class="page page-current sum_content index_content">
		<ul id="itemlist" class="list-container list-block">
			<a href="${ctx}/wx/sum/view/saleSum">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-5"></use>
	                    </svg>
					<span class="item_title">销售汇总</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/sum/view/purchSum">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-3"></use>
	                    </svg>
					<span class="item_title">采购汇总</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/sum/view/outSourceSum">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-2"></use>
	                    </svg>
					<span class="item_title">发外汇总</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/sum/view/financeSum">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-"></use>
	                    </svg>
					<span class="item_title">财务汇总</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/sum/view/stockSum">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jinrongleiicontubiao-1"></use>
	                    </svg>
					<span class="item_title">库存汇总</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
		</ul>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="sum" name="module" />
		</jsp:include>
	</div>


	<script type="text/javascript">
    $(function()
    {       		
//             HYWX.loadMore.addData({
//                 url:financeUrl,
//                 data:{
//       	            "searchContent":$("#search").val().trim(),
//       	            "pageNumber":HYWX.loadMore.pageNumber ,
//       	            "pageSize":HYWX.loadMore.pageSize,
//       	            "dateMin":$("#startDate").val(),
//       	            "dateMax":$("#endDate").val(),
//       	        },
//                 success:function(data){
                    
//                 }
//             })
    })
    </script>
</body>
</html>
