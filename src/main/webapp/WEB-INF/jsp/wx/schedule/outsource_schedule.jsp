<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>发外加工进度</title>
</head>

<body>
	<div class="page page-current">
		<div class="searchbar">
			<a class="button button-fill button-primary  searchbar-cancel">
				<span class="icon icon-search"></span>
			</a>
			<div class="search-input">
				<label class="icon icon-search" for="search"></label>
				<input type="search" id="search" placeholder="搜索发外单号/成品名称/工序/供应商名称">
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
					  <input type="hidden" value='{{= it[k].json}}' />
                      <div class="item-content">
                      	  <div class="item-date">
					          <div class="item-media color-g w-69">
									<i class="icon iconfont icon-nz"></i>交货日期 :									
							   </div>
					           <div class="item-inner deliverDate">{{= it[k].deliveryTime}}</div>
					      </div>
				          <div class="pull-left sechdule_container">
				             <div class="graph">
								<span class="graph_text">{{=it[k].scheduleState}}{{=it[k].schedulePercent}}%</span>
								<span class="green" style="width:{{=it[k].schedulePercent}}%"></span>
							</div>
				          </div>
				      </div>
				      <div class="item">  
						  <span class="more"><i class="icon iconfont icon-more"></i></span>
				          <div class="item-kv">
				            <div class="item-key w-69 ">成品名称 :</div>
				            <div class="item-value color-o w-60p">
								{{? it[k].name}}
									{{for(var m in it[k].name) { }}
										{{= it[k].name[m]}}&nbsp&nbsp;
									{{ } }}
								{{??}}
									{{=it[k].productName}}
									{{?it[k].style}}
										<span class="color-b">({{= it[k].style }})</span>
									{{?}}
								{{?}}
							</div>
				          </div>
				          <div class="item-kv">
				            <div class="item-key w-69">供应商 :</div>
				            <div class="item-value color-o w-60p">{{= it[k].supplierName }}</div>
				          </div>
				          <div class="item-kv">
				            	<div class="item-key w-69">加工数量 :</div>
				            	<div class="item-value w-60p">{{= it[k].qty }}{{= it[k].unitName||'张'}}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">发外单号 :</div>
				            	<div class="item-value w-60p order_num">{{= it[k].billNo }}</div>
						  </div>
				       </div>
                    </li>
					{{ } }}
	</script>
	<script id="moretmpl" type="text/x-dot-template">                    
				      <div class="item">  
				          <div class="item-kv">
				            <div class="item-key w-69">成品名称  :</div>
				            <div class="item-value w-70p">
								{{? it.name}}
									{{for(var m in it.name) { }}
										{{= it.name[m]}}</br>
									{{ } }}
								{{??}}
									{{=it.productName}}									
								{{?}}
							</div>
				          </div>
						  {{? !it.name}}
						  	<div class="item-kv">
				            	<div class="item-key w-69">产品规格  :</div>
				            	<div class="item-value">{{= it.style||'' }}</div>
				          	</div>
						  {{?}}
						 
                          {{? it.procedureName}}
						  <div class="item-kv">
				            <div class="item-key w-69">工序名称  :</div>
				            <div class="item-value">{{= it.procedureName||'' }}</div>
				          </div>
						  {{?}}
				          <div class="item-kv">
				            <div class="item-key w-69">供应商 :</div>
				            <div class="item-value w-70p">{{= it.supplierName }}</div>
				          </div>
				          <div class="item-kv">
				            	<div class="item-key w-69">加工数量 :</div>
				            	<div class="item-value">{{= it.qty }}{{= it.unitName||'张' }}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">加工金额 :</div>
				            	<div class="item-value">￥{{= it.money }}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">到货数量  :</div>
				            	<div class="item-value">{{= it.arriveQty }}{{= it.unitName||'张' }}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">对账数量  :</div>
				            	<div class="item-value">{{= it.reconcilQty }}{{= it.unitName||'张' }}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">付款金额  :</div>
				            	<div class="item-value">￥{{= it.paymentMoney }}</div>
						  </div>
						  <div class="item-kv">
				            	<div class="item-key w-69">生产单号  :</div>
				            	<div class="item-value">{{= it.workBillNo }}</div>
						  </div>

				      </div>
	</script>
	<script type="text/javascript">
        $(function()
        {                            
            // 加载flag
            var loading = false;          
            //数据总数          
            var count = 0;
            //索引
            var index = 0;
            function addItems(){
                HYWX.loadMore.addData({
                    url:'${ctx}/wx/schedule/outSourceSchedule',
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize          	
          	        },
                    loadType:"more",
                    success:function(data){
                        var result = data.result;
                        console.log(result)
                        //格式化日期
                        for(var r in result){
                        	result[r].index = index;
                        	index++;
                            result[r].deliveryTime = new Date(result[r].deliveryTime).format("yyyy-MM-dd");
                            //加入隐藏对象，弹出框信息
                            result[r].json = JSON.stringify(result[r]);
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
                            $("#itemlist").html(interText(data.result));  
                        }else if((HYWX.loadMore.pageNumber-1)!=1){
                          //页码不等于1：节点内容增加
                         
                            var interText = doT.template($("#itemlisttmpl").text());
                            $("#itemlist").append(interText(data.result));  
                        }
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
            
            //弹出框信息
            $(document).on('click','.more', function () {
            	var moreObj = $.parseJSON($(this).closest("li").find("input[type=hidden]").val());
            	var orderNum = $(this).closest("li").find(".order_num").text();
            	var interText = doT.template($("#moretmpl").text());                    	
                $.modal({
                  title: orderNum ,
                  text: interText(moreObj),
                  buttons: [
                    {
                      text: '<i class="icon iconfont icon-guanbi"></i>',
                      onClick: function() {}
                    }
                  ]
                }) 
            });
        })
    </script>
</body>
</html>
