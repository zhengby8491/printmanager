<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>财务汇总</title>
</head>

<body>
	<div class="page page-current sum_content">
		<div class="buttons-tab row no-gutter">
			<span class="tab-link active button col-50" id="receive">收入</span>
			<span class="tab-link button col-50" id="payment">支出</span>
		</div>
		<div class="content infinite-scroll infinite-scroll-bottom" data-distance="100" style="top: 2rem; margin-top: 0">
			<div class="search_div">
				<div class="date-container cl row">
					<div class="search-input col-80 row no-gutter">
						<input type="text" placeholder="开始日期" id="startDate" class="col-45 item-value date_input" readonly="">
						<div class="col-10 text-center" style="line-height: 1.6rem">至</div>
						<input type="text" placeholder="结束日期" id="endDate" class="col-45 item-value date_input" readonly="">
					</div>
				</div>
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
						<i class="icon iconfont icon-get"></i>
					</span>
					<span class="sum_all_text">收入金额</span>
					：
					<span class="color-o">
						￥
						<span id="all_money"></span>
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
            <li class="sum_item" data-index={{=it[k].index}}>
				<span class="li_index pull-left cl">{{=it[k].index}}.&nbsp;&nbsp;</span>
				<div class="pull-left cl">
                	<div class="">{{=it[k].name}}</div>		
					<div class="pull-left color-o w-100">￥{{=it[k].money}}</div>
					<div class="pull-left">折扣：<span class="color-o">￥{{=it[k].discount}}</span></div>	
				</div>						
           </li>
		{{ } }}
	</script>

	<script type="text/javascript">
    $(function()
        {    
    		//汇总支出接口
    		var payUrl = "${ctx}/wx/sum/financePaymentDetail" ;
    		//汇总收入接口
    		var receiveUrl = "${ctx}/wx/sum/financeReceiveDetail" ;
    		//汇总支出总数接口
    		var paySumUrl = "${ctx}/wx/sum/financePaymentSum" ;
    		//汇总收入总数接口
    		var receiveSumUrl = "${ctx}/wx/sum/financeReceiveSum" ;
    		//默认为收入接口
    		var financeUrl = receiveUrl;
    		var financeSumUrl = receiveSumUrl;
             // 加载flag
            var loading = false;          
            //数据总数
            var count = 0;
            //li索引      
            var index = 1;
            function addItems(){
                HYWX.loadMore.addData({
                    url:financeUrl,
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize,
          	            "dateMin":$("#startDate").val(),
          	            "dateMax":$("#endDate").val(),
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
                        if((HYWX.loadMore.pageNumber-1)*HYWX.loadMore.pageSize >= count) { 
                            // 加载完毕，则注销无限加载事件，以防不必要的加载
        	                $.detachInfiniteScroll($('.infinite-scroll'));
        	                // 删除加载提示符
        	                $('.infinite-scroll-preloader').hide();
        	            }                        
                        //重置刷新
                        $.refreshScroller();
                    }
                })
            }
            function querySum(){
            	HYWX.requestByObj({
                    url:financeSumUrl,
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "dateMin":$("#startDate").val(),
          	            "dateMax":$("#endDate").val(),
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
            		if($(this).attr("id")=="receive"){
            			financeUrl = receiveUrl; 
            		    financeSumUrl = receiveSumUrl;
            			$(".sum_all_text").text("收入金额");
            			$(".sum_all_div").find("i.iconfont").removeClass("icon-zhichu")
            		}else if($(this).attr("id")=="payment"){
            			financeUrl = payUrl;
            			financeSumUrl = paySumUrl;
            			$(".sum_all_text").text("支出金额");
            			$(".sum_all_div").find("i.iconfont").addClass("icon-zhichu")
            		}
            		$(".search_btn").triggerHandler("tap");   
            	}           
            })         
        })
    </script>
</body>
</html>
