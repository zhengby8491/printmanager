<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>采购订单审核</title>
</head>

<body>
	<div class="page page-current">
		<div class="searchbar">
			<a class="button button-fill button-primary  searchbar-cancel">
				<span class="icon icon-search"></span>
			</a>
			<div class="search-input">
				<label class="icon icon-search" for="search"></label>
				<input type="search" id="search" placeholder="搜索采购单号/供应商名称">
			</div>
		</div>
		<div class="content content-padded check_content infinite-scroll infinite-scroll-bottom" data-distance="100" style="top: 2.6rem; margin-top: 0">
			<ul id="itemlist" class="list-container list-block">

			</ul>

			<!-- 加载提示符 -->
			<div class="infinite-scroll-preloader">
				<div class="preloader"></div>
			</div>
		</div>
		<nav class="bar bar-tab check_item">
			<label class="label-checkbox">
				<input id="checkall" type="checkbox" name="checkbox">
				<div id="checkall_btn" class="item-media checkall_btn">
					<i class="icon icon-form-checkbox"></i>
					<span>全选</span>
				</div>
			</label>
			<div class="pull-right">
				<span class="all_money_div">
					合计:
					<span class="all_money_text">
						￥
						<span id="all_money">0</span>
					</span>
				</span>
				<a href="#" class="button button-danger auditall_btn">
					审核(
					<span id="all_count">0</span>
					)
				</a>
			</div>
		</nav>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="check" name="module" />
		</jsp:include>
	</div>

	<!-- 主表模板 -->
	<script id="itemlisttmpl" type="text/x-dot-template">
					{{ for(var k in it) { }}
                  		<li class="check_item" data-billid="{{=it[k].id}}">	
					<label class="label-checkbox">		
						<div class="check_item_head cl">							
							<div class="item-kv pull-left">
						            <div class="item-key w-56">采购单号:</div>
						            <div class="item-value check_oreder_num">{{=it[k].billNo}}</div>
<!-- 						            <span><i class="icon iconfont icon-nz"></i></span> -->
						    </div>
						    <a href="javascript:;" class="button button-fill audit_btn">审核</a>					    
						</div>	
						<table>
							<tbody>
								<tr>
									<td colspan="3" class="check_item_content bg-w">							            	            
						        		<div style="padding: 0 0.2rem 0 1.3rem;">	
									        <div class="item-kv">
									            <div class="item-key">供应商  :</div>
									            <div class="item-value">{{=it[k].supplierName}}</div>
									        </div>
									         <div class="item-bottom" style="padding-bottom: 0.1rem;">
												<div>
									            	<div class="">联系人  :</div>
									            	<div class="w_name">{{=it[k].linkName||''}}</div>
												</div>
												<div>
									            	<div style="margin-left:.5rem">电话  :</div>
									            	<div class="">{{=it[k].mobile||''}}</div>
												</div>
									         </div>
									     </div>
									     <!--  选中按钮 -->
									     <input class="ccc" type="checkbox" name="checkbox"/>
										 <div class="item-media"><i class="icon icon-form-checkbox"></i></div>	
										 <!-- 上下拉按钮 -->									     
									     <span class="icon icon-up icon-down pull_down"></span>
							       	</td>
							    </tr>
			        			<tr class="bg-w">
				        			<td class="check_money_td">金额  :<span class="color-o">￥<span class="item_money">{{=it[k].money}}</span></span>
				        			<span class="">(税额  :<span class="color-o">￥<span>{{=it[k].tax}}</span></span>)</span></td>
				        		</tr>
				        		<tr class="check_item_tr">
				        			<td colspan="3" class="check_item_td">
				        				<div class="check_detail_preloader"><div class="preloader"></div></div>
				        			</td>
				        		</tr>
					        </tbody>
			        	</table>
				    </label>
		        </li>
			{{ } }}
	</script>

	<!-- 从表模板 -->
	<script id="detaillisttmpl" type="text/x-dot-template">
										<div class="check_detail_content">
											{{ for(var k in it) { }}
					        				<div class="check_item_detail">
					        					<!-- 审核明细排序 -->
					        					<span class="check_item_order color-o">{{=it[k].index}}.</span>				        					
					        					<div class="cl">
					        						 <div class="item-kv pull-left">
											            <div class="item-value">{{=it[k].materialName||''}}&nbsp;&nbsp;&nbsp;&nbsp;
															{{if(it[k].specifications!=null){}}
	    														{{=it[k].specifications}}
	    													{{}}}
														 </div>  
											        </div>										        
					        					</div>
					        					<div class="cl">				        						
											        <div class="row no-gutter">										            
											            <div class="item-value color-o col-30">￥{{=it[k].valuationPrice}}</div>
											            <div class="item-value col-20">X</div>	
											            <div class="item-value col-50">{{=it[k].valuationQty}}{{=it[k].valuationUnitName}}({{=it[k].qty}}{{=it[k].unitName}})</div>									            
											            
											        </div>
													<div class="row no-gutter">
														<div class="item-value col-50">小计  :<span class="color-o">￥{{=it[k].money}}</span></div>
														<div class="item-value col-50">
															交货日期  :  {{=it[k].deliveryTime}}
											        	</div>
					        						</div>
					        					</div>
											</div>
											{{ } }}
					        			</div>
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
                    url:'${ctx}/wx/check/purchCheckList',
                    data:{
          	            "searchContent":$("#search").val().trim(),
          	            "pageNumber":HYWX.loadMore.pageNumber ,
          	            "pageSize":HYWX.loadMore.pageSize          	
          	        },
                    success:function(data){
                        var result = data.result;
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
        })
          
        /* 时间监听 */
        //全选按钮
        $(document).on("click",'#checkall_btn',function(){
        	var flag = $(this).siblings("input[type=checkbox]").prop("checked");
        	if(flag==true){
        		$(".check_item_content input[type=checkbox]").prop("checked",false) 
        	}else if(flag==false){
        		$(".check_item_content input[type=checkbox]").prop("checked",true) 
        	}
        })
        //选中按钮
        $(document).on("change","input[type=checkbox]",function(){
        	recount();
        })
        //单条审核
        $(document).on("click",".audit_btn",function(){
        	var $li = $(this).closest("li");
        	var billId = $(this).closest("li").data("billid"); 
        	$.confirm('确认审核?', '',function (){
	        	 HYWX.request({
	                url:'${ctx}/wx/check/purchCheck',
	                data:{"id":billId},
	                dataType:"json",
	 		        contentType: 'application/x-www-form-urlencoded',
	                success:function(data){
	                	$li.remove();
	                	$.toast("审核成功!",2000,"mytoast"); 
	                	recount();
	                }
	            }) 	
        	})
        })
        //批量审核按钮
        $(document).on("click",".auditall_btn",function(){
        	if($(this).hasClass("active")){
        		$.confirm('确认审核?', '',
        				//确认按钮
        		        function () {
        		          var ids=[],$lis=[];
        		          $("#itemlist").children("li").forEach(function(a){
        		        	  var flag = $(a).find("input[type=checkbox]").prop("checked");
        		              if(flag){
        		            	  ids.push( $(a).data("billid"));
        		            	  $lis.push($(a));
        		              }
        		          })
        		          HYWX.requestByObj({
                              url:'${ctx}/wx/check/purchCheckAll',
                              data:{"ids":ids},
                              success:function(data){
                            	  $lis.forEach(function(a){
                            		  $(a).remove();
                            	  })
                            	  $.toast("审核成功!",2000,"mytoast"); 
                            	  recount();
                              }
                          })   
        		        },
        		        //取消按钮
        		        function () {
        		        	return;
        		        	//$.showIndicator();
        		        }
        		      );
        	}
        })
              
       	//下拉按钮禁用默认事件
       	$(document).on("click",".pull_down,.check_detail_content",function(e){
           		  e.preventDefault();
                  e.stopPropagation();
           	}) 
       	//点击下拉按钮
       	$(document).on("click",".pull_down",function(){
       			$(this).toggleClass("icon-down").parent().parent().siblings(".check_item_tr").toggle();  
       			var $li = $(this).closest("li");
       			addDetail($li);      			       			
        })
        //加载明细
        function addDetail($a){
        		var billId = $a.data("billid");
        		if($a.find(".check_item_td").children().eq(0).hasClass("check_detail_preloader")){
        			HYWX.requestByObj({
                        url:'${ctx}/wx/check/purchCheckDetail',
                        data:{"id":billId},
                        success:function(data){
                            var result = data.result,index = 1;
                            //格式化日期
                             for(var r in result){
                            	result[r].index = index;
                            	index++;
                                result[r].deliveryTime = new Date(result[r].deliveryTime).format("yyyy-MM-dd");
                            }                         
                            count = data.count;      
                            var interText = doT.template($("#detaillisttmpl").text());
                            $a.find(".check_item_td").html(interText(data.result));                         
                        }
                    })               
        		}
                
            }
		//计算合计金额和选中数
		function recount(){
        	var $selLi = $("#itemlist").children(".check_item");
        	var count = 0, sumMoney = 0;
        	$selLi.forEach(function(a){
        		if($(a).find("input[type=checkbox]").prop("checked")==true){
        			count ++ ;
        			sumMoney += parseFloat($(a).find(".item_money").text());
        		}
        	})
        	$("#all_count").text(count);
            $("#all_money").text(sumMoney.toFixed(4));
        	if(count>0){
        		$(".auditall_btn").addClass("active");        		
        	}else{
        		$(".auditall_btn").removeClass("active");
        	}          	
        }        
    </script>
</body>
</html>
