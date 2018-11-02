<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>采购明细</title>
</head>

<body>
	<input type="hidden" id="supplierId" value="${supplierId}">
	<input type="hidden" id="type" value="${type}">
	<div class="page page-current">
		<div class="content content-padded sum_detail_content infinite-scroll infinite-scroll-bottom" data-distance="100" style="margin-top: 0">
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
                    <li class="mar-btm6" data-index="{{= it[k].index}}">                      
				      <div class="item">  
				          <div class="item-kv">
				            <div class="item-key">采购单号 :</div>
				            <div class="item-value w-60p">{{= it[k].billNo||'' }}</div>
				          </div>
				          <div class="item-kv">
				            <div class="item-key">材料名称 :</div>
				            <div class="item-value w-60p">{{= it[k].materialName||'' }}{{= it[k].specifications||'' }}(克重:{{= it[k].weight||'' }})</div>
				          </div>
				          <div class="row no no-gutter item_detail">	
							<div class="color-o col-20">￥{{=it[k].valuationPrice}}</div>
							<div class="col-10">x</div>
							<div class="col-33">{{=it[k].valuationQty}}{{=it[k].valuationUnitName}}({{=it[k].qty}}{{=it[k].unitName}})</div>
							<div class="color-o col-33">￥{{=it[k].money}}</div>	
						</div>						
				       </div>
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
                    url:'${ctx}/wx/sum/purchSupplierSumDetail',
                    data:{"supplierId":$("#supplierId").val(),"wXSumQueryType":$("#type").val()},
                    loadType:"more",
                    success:function(data){
                        console.log(data)
                        var result = data.result;   
                        console.log(JSON.stringify(result))
                        count = data.count; 
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
            $.init(); 
        })
    </script>
</body>
</html>
