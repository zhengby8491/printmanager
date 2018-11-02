<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>库存汇总</title>
</head>

<body>
	<div class="page page-current sum_content stock_content">
		<div class="buttons-tab row no-gutter">
			<span class="tab-link active button col-50" id="PRODUCT">成品库存</span>
			<span class="tab-link button col-50" id="MATERIAL">材料库存</span>
		</div>
		<div class="content infinite-scroll infinite-scroll-bottom" data-distance="100" style="top: 2rem; margin-top: 0">
			<div class="search_div">
				<div class="searchbar row">
					<div class="search-input col-80">
						<label class="icon icon-search" for="search"></label>
						<input type="search" id="search" placeholder="输入关键字...">
					</div>
					<a class="button button-fill button-primary col-20 search_btn">搜索</a>
				</div>
			</div>
			<div class="sum_content_head">
				<span class="search_div_icon">
					<i class="icon icon-search"></i>
				</span>
				<span class="close_icon">
					<i class="icon iconfont icon-guanbi1"></i>
				</span>
				<div class="sum_all_div">
					<div class="">
						<i class="icon iconfont icon-kucunguanli"></i>
						<span class="sum_all_text1">成品库存</span>
						：
						<span class="sum_all_qty color-o">0</span>
					</div>
					<div class="">
						<i class="icon iconfont icon-jine"></i>
						<span class="sum_all_text2">成品金额</span>
						：
						<span class="color-o">
							￥
							<span class="sum_all_money">0</span>
						</span>
					</div>
				</div>
			</div>
			<ul id="itemlist" class="list-container list-block">

			</ul>
			<!-- 加载提示符 -->
			<div class="infinite-scroll-preloader">
				<div class="preloader"></div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="sum" name="module" />
		</jsp:include>
	</div>
	<script id="itemlisttmpl" type="text/x-dot-template">
		{{ for(var k in it) { }}
            <li class="sum_item cl" data-index={{=it[k].index}}>
					<span class="li_index item-key">{{=it[k].index}}.&nbsp;&nbsp;</span>
					<div class="item-kv w-90p">
						<div class="">
							<div class="item-kv w-75p">
								{{?it[k].productName}}
									{{=it[k].productName}}
								{{??}}
									{{=it[k].materialName}}
								{{?}}
								({{=it[k].style||''}})
							</div>							
						</div>
						<div class="row no no-gutter item_detail">	
							<div class="color-o col-20">￥{{=it[k].price}}</div>
							<div class="col-10">x</div>
							<div class="col-33">{{=it[k].qty}}{{=it[k].unitName||''}}</div>
							<div class="color-o col-33">￥{{=it[k].money}}</div>	
						</div>
					</div>
					{{? it[k].weight}}
						<div class="pull-right weight">克重：{{=it[k].weight}}</div>
					{{?}}
           </li>
		{{ } }}
	</script>

	<script type="text/javascript">
    $(function()
        {    
    		//材料库存明细接口
    		var materialUrl = "${ctx}/wx/sum/stockMaterialDetail" ;
    		//成品库存明细接口
    		var productUrl = "${ctx}/wx/sum/stockProductDetail" ;
    		//材料库存总数接口
    		var materialSumUrl = "${ctx}/wx/sum/materialStockSum" ;
    		//成品库存总数接口
    		var productSumUrl = "${ctx}/wx/sum/productStockSum" ;
    		//默认显示成品库存
    		var stockUrl = productUrl;
    		//默认显示成品库存总数
    		var stockSumUrl = productSumUrl;
             // 加载flag
            var loading = false;          
            //数据总数
            var count = 0;
            //li索引      
            var index = 1;
            function addItems(){
                HYWX.loadMore.addData({
                    url:stockUrl,
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize,          	           
          	        },
                    success:function(data){
                        var result = data.result;                                          
                        for(var r in result){
                        	result[r].index = index;
                        	index++;
                        }
                        $("#all_money").text(data.money);
                        count = data.count; 
                     	// 重置加载flag
                        loading = false; 
                     	// 更新页码
                        HYWX.loadMore.pageNumber++;
                        //添加新数据
                        if((HYWX.loadMore.pageNumber-1)==1){
                            //页码等于1：节点内容替换
                            var interText = doT.template($("#itemlisttmpl").text());
                            $("#itemlist").html(interText(result));  
                        }else if((HYWX.loadMore.pageNumber-1)!=1){
                          //页码不等于1：节点内容增加
                            var interText = doT.template($("#itemlisttmpl").text());
                            $("#itemlist").append(interText(result));  
                        }
                    }
                })
            }
            function querySum(){
            	HYWX.requestByObj({
                    url:stockSumUrl,
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	        },
					success:function(data){
						$(".sum_all_qty").text(data.qty);
            			$(".sum_all_money").text(data.money);
					}
          	    })
            }
            //查询总数 
            querySum();
            //预先加载8条
            addItems();          
            // 注册'infinite'事件处理函数
            $(document).on('infinite', '.infinite-scroll-bottom', function()
              	            {
              	                // 如果正在加载，则退出
              	                if(loading) return;
              	                // 设置flag
              	                loading = true;
              	                addItems();
              	            }); 
	            
            $(".search_btn").on("tap",function(){
                $.refreshScroller();
                $.attachInfiniteScroll($('.infinite-scroll'));               
                $('.infinite-scroll-preloader').show();
                HYWX.loadMore.pageNumber = 1;
                index = 1;
                addItems();
                querySum();
                var scroller = $('.infinite-scroll-bottom');
                scroller.scrollTop(0);
            })                      
            $.init(); 
            
            /* 初始化日期插件*/
            $("#startDate,#endDate").calendar({
                value: new Date()
            });
             /* 搜索区关闭按钮 */
            $(document).on("click",".close_icon",function(){
            	$(".search_div,.close_icon").hide();
            	//$("#startDate,#endDate,#search").val("");
            	$(".search_div_icon").show();
            })
            /* 搜索区打开按钮 */
            $(document).on("click",".search_div_icon",function(){
            	$(".search_div,.close_icon").fadeIn();
            	$(".search_div_icon").hide();
            })
            /* tab切换,公用js已经加active类 */
            $(".tab-link").on("singleTap",function(){
            	 if($(this).hasClass("active")){
            		if($(this).attr("id")=="MATERIAL"){
            			stockUrl = materialUrl;
            			stockSumUrl = materialSumUrl
            			$(".sum_all_text1").text("材料库存");
            			$(".sum_all_text2").text("材料金额");
            		}else if($(this).attr("id")=="PRODUCT"){
            			stockUrl = productUrl;
            			stockSumUrl = productSumUrl;
            			$(".sum_all_text1").text("成品库存");
            			$(".sum_all_text2").text("成品金额");
            		}
            		$(".search_btn").triggerHandler("tap");  
            	}           
            })         
        })
    </script>
</body>
</html>

