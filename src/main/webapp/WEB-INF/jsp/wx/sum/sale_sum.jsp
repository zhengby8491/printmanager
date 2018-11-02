<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>销售汇总</title>
</head>

<body>
	<div class="page page-current sum_content">
		<div class="buttons-tab row no-gutter">
			<span class="tab-link active button col-20" id=DAY>日</span>
			<span class="tab-link button col-20" id="MONTH">月</span>
			<span class="tab-link button col-30" id="SHOULD">应收款</span>
			<span class="tab-link button col-30" id="EXPIRE">到期应收</span>
			<input id="queryType" type="hidden" value="DAY" />
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
					<span>
						<i class="icon iconfont icon-jine"></i>
					</span>
					<span class="sum_all_text">当日下单金额</span>
					：
					<span class="color-o">
						￥
						<span id="all_money">0</span>
					</span>
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
            <li class="sum_item" data-index={{=it[k].index}} data-id={{=it[k].id}}>
				<span class="li_index pull-left cl">{{=it[k].index}}.&nbsp;&nbsp;</span>
				<div class="pull-left cl">
                	<div class="">{{=it[k].name||''}}</div>		
					<div class="color-o">￥{{=it[k].money}}</div>	
				</div>	
				<span class="icon icon-right cl"></span>			
           </li>
		{{ } }}
	</script>

	<script type="text/javascript">
    $(function()
        {       		
            // 加载flag
            var loading = false;          
            //数据总数
            var count = 0;
            //li索引      
            var index = 1;
            //定义不同状态总金额
            var sumResult ;
            function addItems(){
                HYWX.loadMore.addData({
                    url:"${ctx}/wx/sum/saleCustomerSum",
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize,    
          	          	"wXSumQueryType":$("#queryType").val()
          	        },
                    success:function(data){
                        var result = data.result;                                          
                        for(var r in result){
                        	result[r].index = index;
                        	index++;
                        }
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
                    url:"${ctx}/wx/sum/saleSum",
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	          	"wXSumQueryType":$("#queryType").val()
          	        },
					success:function(data){
                        $("#all_money").text(data);
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
            		$("#queryType").val($(this).attr("id"));
            		$(".search_btn").triggerHandler("tap");  
            		if($(this).attr("id")=="DAY"){            			   			
            			$(".sum_all_text").text("当日下单金额");
            		}else if($(this).attr("id")=="MONTH"){
            			$(".sum_all_text").text("当月下单金额");
            		}else if($(this).attr("id")=="SHOULD"){
            			$(".sum_all_text").text("应收款金额");
            		}else if($(this).attr("id")=="EXPIRE"){
            			$(".sum_all_text").text("到期应收金额");
            		}
            	}        
            })
            //点击进入明细
            $(document).on("click",".sum_item",function(){
            	var customerId = $(this).data("id");
            	var type = $("#queryType").val();
          	    window.location.href="${ctx}/wx/sum/view/saleSumDetail?customerId="+customerId+"&type="+type;
            })
        })
    </script>
</body>
</html>
