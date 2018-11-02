<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>未到货发外</title>
</head>

<body>
	<div class="page page-current">
		<div class="searchbar">
			<a class="button button-fill button-primary  searchbar-cancel">
				<span class="icon icon-search"></span>
			</a>
			<div class="search-input">
				<label class="icon icon-search" for="search"></label>
				<input type="text" id="search" placeholder="搜索生产单号/ 发外单号/产品/工序/供应商">
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
			<jsp:param value="warn" name="module" />
		</jsp:include>
	</div>
	<script id="itemlisttmpl" type="text/x-dot-template">
					{{ for(var k in it) { }}
                    <li class="mar-btm6" data-index="{{= it[k].index}}">
                      <div class="item-content">
                      	  <div class="item-date">
					          <div class="item-media color-g w-69">
									<i class="icon iconfont icon-nz"></i>交货日期  :									
							   </div>
					           <div class="item-inner deliverDate">{{= it[k].deliveryTime}}</div>
					      </div>
						  <div class="outsource_type">	
						  	{{? it[k].type==='PROCESS'}}		
								<div>工序发外</div>	
						  	{{?}}  
						  	{{? it[k].type==='PRODUCT'}}		
								<div>整单发外</div>	
						  	{{?}}  
						  </div>      
				      </div>
				      <div class="item">  
				          <div class="item-kv">
				            <div class="item-key w-69">成品名称  :</div>
				            <div class="item-value color-o w-55p">
								{{? it[k].name}}
									{{for(var m in it[k].name) { }}
										{{= it[k].name[m]}}&nbsp;
									{{ } }}
								{{??}}
									{{=it[k].productName}}
									<span class="color-b">({{= it[k].style||'' }})</span>
								{{?}}
							</div>
				          </div>
 						  <div class="item-kv">
							{{?it[k].procedureName}}
				            <div class="item-key w-69">工序名称 :</div>
				            <div class="item-value color-o">{{= it[k].procedureName }}</div>
							{{?}}
				          </div>
				          <div class="item-kv">
				            <div class="item-key w-69">供应商  :</div>
				            <div class="item-value color-o w-55p">{{= it[k].supplierName }}</div>
				          </div>
				          <div class="item-bottom">
							<div style="">
				            	<div class="w-69">加工数量  :</div>
				            	<div class="w_num_unit">{{= it[k].qty }}{{= it[k].unitName||'张' }}</div>
							</div>
							<div>
				            	<div style="margin-left:.5rem">已到货  :</div>
				            	<div class="">{{= it[k].arriveQty }}{{= it[k].unitName||'张' }}</div>
							</div>
				          </div>
						  <div class="item-bottom">
							<div style="min-width:5.5rem">
				            	<div class="w-69">生产单号  :</div>
				            	<div class="">{{= it[k].workBillNo}}</div>
							</div>
							<div>
				            	<div style="margin-left:.5rem">发外单号 :</div>
				            	<div class="">{{= it[k].billNo }}</div>
							</div>
				          </div>
							<div class="circle" style="bottom: 0.9rem;margin-top: 0;top: initial;" data-pct="{{= it[k].deliveryPercent }}">
    							<div class="pie_left"><div class="left"></div></div>
    							<div class="pie_right"><div class="right"></div></div>
    							<div class="mask">
									<div>交货天数</div>
									<span class="deliverDay">{{=it[k].deliveryDay}}</span><span class="color-b">天</span>
								</div>
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
            //存储新增变量
            var $new = [];
            function addItems(){
                HYWX.loadMore.addData({
                    url:'${ctx}/wx/warn/outSourceNotArrive',
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize          	
          	        },
                    loadType:"more",
                    success:function(data){
                        if(data=="turn"){
                            window.location.href="${ctx}/wx/menu/jumping"
                        }
                        var result = data.result;
                        //格式化日期
                        for(var r in result){
                        	result[r].index = index;
                        	index++;
                            result[r].deliveryTime = new Date(result[r].deliveryTime).format("yyyy-MM-dd");
                        }                        
                        count = data.count; 
                    	// 重置加载$new
                        $new = [];
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
                        var startIndex = HYWX.loadMore.pageSize * (HYWX.loadMore.pageNumber-2) ;
                        var endIndex = startIndex + result.length ;
                        for(var i=startIndex;i<endIndex;i++){
                          	$new.push($("#itemlist li").eq(i).find(".circle"));
                        }
                        $new.forEach(function(a){
                        	var pct = parseInt($(a).data('pct'));
                        	if(pct>=0&&pct<100){
             		        	$(a).css({"background":"green"});
             		        	$(a).find(".mask").css({"color":"green"});
             		        	var num = pct * 3.6;
                 		        if (num<=180) {
                 		          $(a).find('.right').css('transform', "rotate(" + num + "deg)");
                 		        } else {
                 		          $(a).find('.right').css('transform', "rotate(180deg)");
                 		          $(a).find('.left').css('transform', "rotate(" + (num - 180) + "deg)");
                 		        };
             		        }else{
             		        	$(a).find(".left,.right").css("background","#ff7200");
             		        	$(a).find(".mask").css("color","#ff7200");
             		        }                        	             		                   		        
                        })
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
