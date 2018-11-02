<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>库存管理流程图</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border page-nav-wrap">
			<!--导航页面-->
			<div class="page-nav">
				<!--导航图标-->
				<div class="nav_icon_container">
					<h4>库存管理流程图</h4>
					<div class="n_line"></div>
					<div class="nav_icons stock-nav">
						<!--图标-->
						<a class="nav_icon_b1" href="javascript:redirect('采购入库列表','/purch/stock/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_14.png" />
							</span>
							<p>采购入库</p>
						</a>
						<a class="nav_icon_b2" href="javascript:redirect('材料其它入库列表','/stockmaterial/otherin/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_12.png" />
							</span>
							<p>材料其它入库</p>
						</a>
						<a class="nav_icon_b3" href="javascript:redirect('材料库存查询','/stock/material/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_15.png" />
							</span>
							<p>材料库存</p>
						</a>
						<a class="nav_icon_b4" href="javascript:redirect('生产领料列表','/stockmaterial/take/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_21.png" />
							</span>
							<p>生产领料</p>
						</a>
						<a class="nav_icon_b5" href="javascript:redirect('生产退料列表','/stockmaterial/return/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_17.png" />
							</span>
							<p>生产退料</p>
						</a>
						<a class="nav_icon_b6" href="javascript:redirect('材料库存盘点列表','/stockmaterial/inventory/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_18.png" />
							</span>
							<p>材料库存盘点</p>
						</a>
						<a class="nav_icon_b7" href="javascript:redirect('生产补料列表','/stockmaterial/supplement/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_19.png" />
							</span>
							<p>生产补料</p>
						</a>
						<a class="nav_icon_b8" href="javascript:redirect('材料其它出库','/stockmaterial/otherout/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_2.png" />
							</span>
							<p>材料其它出库</p>
						</a>
						<a class="nav_icon_b9" href="javascript:redirect('材料库存调拨列表','/stockmaterial/transfer/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_5.png" />
							</span>
							<p>材料库存调拨</p>
						</a>
						<a class="nav_icon_b10" href="javascript:redirect('材料库存调整列表','/stockmaterial/adjust/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_20.png" />
							</span>
							<p>材料库存调整</p>
						</a>
						<a class="nav_icon_b11" href="javascript:redirect('成品入库列表','/stockproduct/in/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_16.png" />
							</span>
							<p>成品入库</p>
						</a>
						<a class="nav_icon_b12" href="javascript:redirect('成品其它入库','/stockproduct/otherin/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_12.png" />
							</span>
							<p>成品其它入库</p>
						</a>
						<a class="nav_icon_b13" href="javascript:redirect('成品库存查询','/stock/product/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_15.png" />
							</span>
							<p>成品库存</p>
						</a>
						<a class="nav_icon_b14" href="javascript:redirect('销售送货列表','/sale/deliver/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_4.png" />
							</span>
							<p>送货单</p>
						</a>
						<a class="nav_icon_b15" href="javascript:redirect('销售退货列表','/sale/return/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_17.png" />
							</span>
							<p>退货单</p>
						</a>
						<a class="nav_icon_b16" href="javascript:redirect('成品库存盘点列表','/stockproduct/inventory/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_18.png" />
							</span>
							<p>成品库存盘点</p>
						</a>
						<a class="nav_icon_b17" href="javascript:redirect('成品其它出库列表','/stockproduct/otherout/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_2.png" />
							</span>
							<p>成品其它出库</p>
						</a>
						<a class="nav_icon_b18" href="javascript:redirect('成品库存调拨列表','/stockproduct/transfer/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_5.png" />
							</span>
							<p>成品库存调拨</p>
						</a>
						<a class="nav_icon_b19" href="javascript:redirect('成品库存调整列表','/stockproduct/adjust/list')">
							<span>
								<img alt="" src="${ctxStatic }/layout/images/navigation/nav_icon_20.png" />
							</span>
							<p>成品库存调整</p>
						</a>
						<!--箭头 -->
						<span class="nav_arrow_b1">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_10.png" />
						</span>
						<span class="nav_arrow_b2">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_4.png" />
						</span>
						<span class="nav_arrow_b3">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_10.png" />
						</span>
						<span class="nav_arrow_b4">
							<img alt="" src="${ctxStatic }/layout/images/navigation/line_4.png" />
						</span>
					</div>
				</div>
				<!--导航列表-->
				<div class="nav_list_container">
					<div class="nav_list_item nav_list_1">
						<h4>材料库存明细报表</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item" style="width: 48%">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料库存查询','/stock/material/list')">材料库存查询</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产领料明细','/stockmaterial/take/detailList')">生产领料明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产补料明细','/stockmaterial/supplement/detailList')">生产补料明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('生产退料明细','/stockmaterial/return/detailList')">生产退料明细表</a>
								</li>
							</ul>
							<ul class="list_item" style="width: 52%">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料其它入库明细','/stockmaterial/otherin/detailList')">材料其它入库明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料其它出库明细','/stockmaterial/otherout/detailList')">材料其它出库明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料库存调拨明细','/stockmaterial/transfer/detailList')">材料库存调拨明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料库存调整明细','/stockmaterial/adjust/detailList')">材料库存调整明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料库存盘点明细','/stockmaterial/inventory/detailList')">材料库存盘点明细表</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_2" style="padding: 0 5px 15px 5px;">
						<h4>成品库存明细报表</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品库存查询','/stock/product/list')">成品库存查询</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品入库明细','/stockproduct/in/detailList')">成品入库明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品其他入库明细','/stockproduct/otherin/detailList')">成品其他入库明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品其他出库明细','/stockproduct/otherout/detailList')">成品其他出库明细表</a>
								</li>

							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品库存调拨明细','/stockproduct/transfer/detailList')">成品库存调拨明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品库存调整明细','/stockproduct/adjust/detailList')">成品库存调整明细表</a>
								</li>
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品库存盘点明细','/stockproduct/inventory/detailList')">成品库存盘点明细表</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="nav_list_item nav_list_3" style="min-height: 104px">
						<h4>汇总分析报告</h4>
						<div class="n_line"></div>
						<div class="cl nav_list_content">
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('材料出入汇总','/stock/material/logSumList')">材料出入汇总</a>
								</li>
							</ul>
							<ul class="list_item">
								<li>
									<i class="n_circle"></i>
									<a href="javascript:redirect('成品出入汇总','/stock/product/logSumList')">成品出入汇总</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
			$(".nav_icons a").mouseover(function(){
				var idx = $(this).index();
				var imgPath = $(this).children("span").children("img").attr("src");
				var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
				var nameArr = imgName.split("_");
				var newImg = nameArr[0]+"_"+nameArr[1]+"_h"+nameArr[2];
				$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
				$(this).children("p").css("color","#FFB257");				
			})
			$(".nav_icons a").mouseout(function(){
			    var idx = $(this).index();
					var imgPath = $(this).children("span").children("img").attr("src");
					var imgName =  imgPath.substr(imgPath.lastIndexOf("/")+1);
					var nameArr = imgName.split("_");
					var newImg = nameArr[0]+"_"+nameArr[1]+"_"+nameArr[2].substr(nameArr[2].indexOf("h")+1);
					$(this).children("span").children("img").attr("src","${ctxStatic }/layout/images/navigation/"+newImg);
					$(this).children("p").css("color","#15a67f");					
			})
	</script>
</body>
</html>
