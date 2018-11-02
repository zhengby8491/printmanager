<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>报价单列表</title>
</head>

<body>
	<div class="page page-current">
		<div class="searchbar">
			<a class="button button-fill button-primary  searchbar-cancel">
				<span class="icon icon-search"></span>
			</a>
			<div class="search-input">
				<label class="icon icon-search" for="search"></label>
				<input type="search" id="search" placeholder="搜索报价单号/成品名称">
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
			<jsp:param value="offer" name="module" />
		</jsp:include>
	</div>

	<!-- 主表模板 -->
	<script id="itemlisttmpl" type="text/x-dot-template">
			{{ for(var k in it) { }}
                <li class="offer_item bg-w" data-id="{{=it[k].id}}">	
					 <div class="offer_item_head"></div>
					 <div class="item-kv">
				            	<div class="item-key w-69">成品名称 :</div>
				            	<div class="item-value">{{= it[k].productName }}</div>
				     </div>
					 <div class="item-kv">
				            	<div class="item-key w-69">报价类型 :</div>
				            	<div class="item-value">{{= it[k].offerTypeText }}</div>
				     </div>
					 <div class="item-kv">
				            	<div class="item-key w-69">报价单号 :</div>
				            	<div class="item-value">{{= it[k].offerNo }}</div>
				     </div>
					 <span class="icon icon-right"></span>
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
            function addItems(){
                HYWX.loadMore.addData({
                    url:'${ctx}/wx/offer/ajaxList',
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize          	
          	        },
                    success:function(data){
                     	// 重置加载flag
                        loading = false; 
                     	// 更新页码
                        HYWX.loadMore.pageNumber++;
                        //添加新数据
                        if((HYWX.loadMore.pageNumber-1)==1){
                            //页码等于1：节点内容替换
                            var interText = doT.template($("#itemlisttmpl").text());
                            $("#itemlist").html(interText(data.result));  
                        }else if((HYWX.loadMore.pageNumber-1)!=1){
                          //页码不等于1：节点内容增加
                            var interText = doT.template($("#itemlisttmpl").text());
                            $("#itemlist").append(interText(data.result));  
                        }
                    }
                })               
            }
            $.init();                        
          
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
	            
            $(document).on('click','.searchbar-cancel',function(){
                $.refreshScroller();
                $.attachInfiniteScroll($('.infinite-scroll'));               
                $('.infinite-scroll-preloader').show();
                HYWX.loadMore.pageNumber = 1;
                index = 1;
                addItems();
                var scroller = $('.infinite-scroll-bottom');
                scroller.scrollTop(0);
            })       
            //点击进入明细
            $(document).on("click",".offer_item",function(){
            	var id = $(this).data("id");
          	    window.location.href="${ctx}/wx/offer/view/detail?id="+id;
            })
        })
        
    </script>
</body>
</html>
