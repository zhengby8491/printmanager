<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>

<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_schedule.js' charset='utf-8'></script>
<title>进度查询</title>
</head>

<body>
	<div class="page page-current sum_content index_content">
		<ul id="itemlist" class="list-container list-block">
			<a href="${ctx}/wx/schedule/view/saleSchedule">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jihuadaichuli"></use>
	                    </svg>
					<span class="item_title">销售订单进度</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/schedule/view/workSchedule">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jihuachulizhong"></use>
	                    </svg>
					<span class="item_title">生产工单进度</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/schedule/view/purchSchedule">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jihuajindu"></use>
	                    </svg>
					<span class="item_title">采购订单进度</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
			<a href="${ctx}/wx/schedule/view/outSourceSchedule">
				<li class="sum_item">
					<svg class="icon" aria-hidden="true">
	                        <use xlink:href="#icon-jihuayichuli"></use>
	                    </svg>
					<span class="item_title">发外加工进度</span>
					<span class="icon icon-right cl"></span>
				</li>
			</a>
		</ul>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="schedule" name="module" />
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
