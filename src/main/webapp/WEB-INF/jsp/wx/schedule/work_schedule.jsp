<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>生产工单进度</title>
</head>

<body>
	<div class="page page-current">
		<div class="searchbar">
			<a class="button button-fill button-primary  searchbar-cancel">
				<span class="icon icon-search"></span>
			</a>
			<div class="search-input">
				<label class="icon icon-search" for="search"></label>
				<input type="search" id="search" placeholder="搜索生产单号/成品名称/客户名称">
			</div>
		</div>
		<div class="content content-padded infinite-scroll infinite-scroll-bottom" data-distance="100" style="top: 2.6rem; margin-top: 0">
			<ul id="itemlist" class="list-container list-block">

			</ul>
			<!-- 加载提示符 -->
			<div class="infinite-scroll-preloader">
				<div class="preloader"></div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="schedule" name="module" />
		</jsp:include>
	</div>
	<script id="itemlisttmpl" type="text/x-dot-template">
					{{ for(var k in it) { }}
                    <li class="mar-btm6" data-index="{{= it[k].index}}">
					  <input type="hidden" value='{{= it[k].json}}'/>
                      <div class="item-content">
                      	  <div class="item-date">
					          <div class="item-media color-g w-69">
									<i class="icon iconfont icon-nz"></i>交货日期 :									
							   </div>
					           <div class="item-inner deliverDate">{{= it[k].deliveryTime}}</div>
					      </div>
				          <div class="pull-left sechdule_container">
				             <div class="graph">
								<span class="graph_text">完成{{=it[k].schedulePercent}}%</span>
								<span class="green" style="width:{{=it[k].schedulePercent}}%"></span>
							</div>
				          </div>
				      </div>
				      <div class="item">  
						
									<button type="button" class="infomation" >
											<a href="${ctx}/wx/schedule/view/orderSchedule?oderNum={{= it[k].workBillNo }}">
										工序信息
											</a>
									</button>
						
						 	<button type="button" class="more">
								更多详情
							 </button>
							
				          <div class="item-kv">
				            <div class="item-key w-69 ">成品名称 :</div>
				            <div class="item-value color-o w-60p">{{= it[k].productName||'' }}</div>
				          </div>
				          <div class="item-kv">
				            <div class="item-key w-69">客户名称 :</div>
				            <div class="item-value color-o w-60p">{{= it[k].customerName||'' }}</div>
				          </div>
				          <div class="item-kv">
				            	<div class="item-key w-69">生产数量 :</div>
				            	<div class="item-value w-60p">{{= it[k].workQty }}{{= it[k].unitName||'张' }}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">生产单号 :</div>
				            	<div class="item-value w-60p order_num">{{= it[k].workBillNo }}</div>
						  </div>
				       </div>
                    </li>
					{{ } }}
	</script>
	<script id="moretmpl" type="text/x-dot-template">                    
				      <div class="item">  
				          <div class="item-kv">
				            <div class="item-key w-69">成品名称 :</div>
				            <div class="item-value w-70p text-left">{{= it.productName }}</div>
				          </div>
                          <div class="item-kv">
				            <div class="item-key w-69">产品规格  :</div>
				            <div class="item-value w-70p text-left">{{= it.style }}</div>
				          </div>
				          <div class="item-kv">
				            <div class="item-key w-69">客户名称 :</div>
				            <div class="item-value w-70p text-left">{{= it.customerName }}</div>
				          </div>
				          <div class="item-kv">
				            	<div class="item-key w-69">生产数量 :</div>
				            	<div class="item-value">{{= it.workQty }}{{= it.unitName ||'张'}}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">入库数量  :</div>
				            	<div class="item-value">{{= it.inStockQty }}{{= it.unitName ||'张'}}</div>
						  </div>
						  {{? it.saleBillNo!=''}}
						  		<div class="item-kv">
				            		<div class="item-key w-69">销售单号  :</div>
				            		<div class="item-value">{{= it.saleBillNo }}</div>
						  		</div>
						  {{?}}
				      </div>
	</script>
	<script type="text/javascript">
		$(function() {
			
			// 加载flag
			var loading = false;
			//数据总数
			var count = 0;
			function addItems() {
				HYWX.loadMore.addData({
					url : '${ctx}/wx/schedule/workSchedule',
					data : {
						"searchContent" : $("#search").val().trim(),
						"pageNumber" : HYWX.loadMore.pageNumber,
						"pageSize" : HYWX.loadMore.pageSize
					},
					loadType : "more",
					success : function(data) {
						var result = data.result;
						//格式化日期
					
						for ( var r in result) {
							result[r].deliveryTime = new Date(
									result[r].deliveryTime)
									.format("yyyy-MM-dd");
							//加入隐藏对象，弹出框信息
							result[r].json = JSON.stringify(result[r]);
						}
						count = data.count;
						// 重置加载flag
						loading = false;
						// 更新页码
						HYWX.loadMore.pageNumber++;
						//添加新数据
						if ((HYWX.loadMore.pageNumber - 1) == 1) {
							//页码等于1：节点内容替换
							var interText = doT.template($("#itemlisttmpl")
									.text());
							$("#itemlist").html(interText(data.result));
						} else if ((HYWX.loadMore.pageNumber - 1) != 1) {
							//页码不等于1：节点内容增加
							var interText = doT.template($("#itemlisttmpl")
									.text());
							$("#itemlist").append(interText(data.result));
						}
					}
				})
			}

			//预先加载8条
			addItems();
			// 注册'infinite'事件处理函数
			$(document).on('infinite', '.infinite-scroll-bottom', function() {
				// 如果正在加载，则退出
				if (loading)
					return;
				// 设置flag
				loading = true;
				addItems();
			});

			$(document).on('click', '.searchbar-cancel', function() {
				$.refreshScroller();
				$.attachInfiniteScroll($('.infinite-scroll'));
				$('.infinite-scroll-preloader').show();
				HYWX.loadMore.pageNumber = 1;
				index = 1;
				addItems();
				var scroller = $('.infinite-scroll-bottom');
				scroller.scrollTop(0);
			})
			$.init();

			//弹出框信息
			$(document)
					.on(
							'click',
							'.more',
							function() {
								var moreObj = $.parseJSON($(this).closest("li")
										.find("input[type=hidden]").val());
								var orderNum = $(this).closest("li").find(
										".order_num").text();
								var interText = doT.template($("#moretmpl")
										.text());
								$
										.modal({
											title : orderNum,
											text : interText(moreObj),
											buttons : [ {
												text : '<i class="icon iconfont icon-guanbi"></i>',
												onClick : function() {
												}
											} ]
										})
							});
		});
		$(document)
		.on(
				'click',
				'.infomation',
				function() {
					
// 					var moreObj = $.parseJSON($(this).closest("li")
// 							.find("input[type=hidden]").val());
// 					var orderNum = $(this).closest("li").find(
// 							".order_num").text();
// 					var interText = doT.template($("#moretmpl")
// 							.text());
// 					$
// 							.modal({
// 								title : orderNum,
// 								text : interText(moreObj),
// 								buttons : [ {
// 									text : '<i class="icon iconfont icon-guanbi"></i>',
// 									onClick : function() {
// 									}
// 								} ]
// 							})
 				//});
})
	</script>
</body>
</html>
