<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>购买信息</title>
</head>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/wx/css/pay-common.css">
<style type="text/css">
</style>
<body>
	<div class="page page-current" id="pay_message2">
		<div class="searchbar">
			<a class="button button-fill button-primary  searchbar-cancel">
				<span class="icon icon-search"></span>
			</a>
			<div class="search-input">
				<label class="icon icon-search" for="search"></label>
				<input type="search" id="search" placeholder="搜索订单编号/服务名称">
			</div>
		</div>
		<div class="content infinite-scroll infinite-scroll-bottom" data-distance="100" style="top: 2.6rem; margin-top: 0">
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
	<li class="mar-btm6 bg-w" data-index="{{= it[k].index}}">
		<div class="pm2_top cleaerFloat">
			<div class="item-content" style="background-color:#F0F0F0">
				<div class="item-date">
					<div class="item-media color-g w-66">
						<i class="icon iconfont icon-nz"></i>订单编号:								
					</div>
					<div class="item-inner deliverDate">{{= it[k].billNo}}</div>
				</div>
			</div>
			<div class="item-kv">
				<div class="item-key w-66">公司名称 :</div>
				<div class="item-value w-60p">{{= it[k].companyName }}</div>
			</div>
			<div class="item-kv">
				<div class="item-key w-66">服务名称 :</div>
				<div class="item-value color-o w-60p">{{= it[k].productName||'' }}</div>
			</div>
			{{? it[k].orderState==1}}
			<a style="position: absolute;right: 0;top: 0;" class="goPay btnA">立即支付</a>
			<a href="##" style="position: absolute;right: 0;top: 1.9rem;" class="cancelPay btnA" data-cancel="{{= it[k].id}}">取消订单</a>
			{{?}}
		</div>
		<div class="pm2_item">  
			<div class="item-kv">
				<div class="item-key w-66 ">购买时间 :</div>
				<div class="item-value color-o ">{{= it[k].createTime||'' }}</div>
			</div>

			<div class="item-kv">
				<div class="item-key w-66">用户名称 :</div>
				<div class="item-value ">{{= it[k].userName||'' }}</div>
			</div>
		</div>
		<div class="pm2_item" style="    position: relative;left: 0.9rem;">  
			<div class="item-kv">
				<div class="item-key w-56 ">联系人 :</div>
				<div class="item-value color-o w-90">{{= it[k].linkMan||'' }}</div>
			</div>
			<div class="item-kv">
				<div class="item-key w-56">联系电话 :</div>
				<div class="item-value color-o w-90">{{= it[k].telephone||'' }}</div>
			</div>

		</div>
		<div class="pm2_table">
			<table>
				<tr>
					<td width=50%><span class="item-key w-66 ">订单金额 :</span><div class="item-value">{{= it[k].price+it[k].tax }}元</div></td>

					<td width=50%><span class="item-key w-74 ">支付金额 :</span>
						{{? it[k].payPrice==null}}
						<div class="item-value"></div>
						{{??}}	
						<div class="item-value">{{= it[k].payPrice}}元</div>
						{{?}}
					</td>
				</tr>
				<tr></span>
					<td ><span class="item-key w-66 ">发票信息 :</span>
						{{? it[k].invoiceInfor == 0}}
						<div class="item-value">不需要发票</div>
						{{?? it[k].invoiceInfor == 1}}
						<div class="item-value">增值税发票(6%)</div>
						{{?? it[k].invoiceInfor == 2}}
						<div class="item-value">增值税发票(17%)</div>
						{{?}}
					</td>
					<td><span class="item-key w-74 ">邀请人姓名 :</span><div class="item-value">{{= it[k].inviter = it[k].inviter=='null'?'':it[k].inviter}}</div></td>
				</tr>
				<tr>
					{{? it[k].orderState==0}}
					<td><span class="item-key w-66 ">订单状态 :</span><div class="item-value">已取消</div></td>
					{{?? it[k].orderState==1}}
					<td><span class="item-key w-66 ">订单状态 :</span><div class="item-value">待支付</div></td>
					{{?? it[k].orderState==2}}
					<td><span class="item-key w-66 ">订单状态 :</span><div class="item-value">已完成</div></td>
					{{?}}
					<td><span class="item-key w-74 ">邀请人电话 :</span><div class="item-value">{{= it[k].inviterPhone }}</div></td>
				</tr>
			</table>
		</div>
	</li>
	{{ } }}
</script>
	<script type="text/javascript">
	//立即支付
	$(document).on('click','.goPay',function(){
		var orderNmber = $(this).siblings('.item-content').find('.deliverDate').html();
		window.location =  HYWX.basePath+ "/wx/pay/view/step3/choose/pay/"+orderNmber;
	});
	//取消訂單
	$(document).on('click','.cancelPay',function(){
		$.ajax({
			url: HYWX.basePath + '/wx/pay/cancelOrder/'+ $(this).attr('data-cancel'),
			type:'post',
			success:function(data){
				location.reload();
			}
		})
	})


	$(function(){                   
         // 加载flag
        var loading = false;          
        //数据总数
        var count = 0;
        function addItems(){
        	HYWX.loadMore.addData({
        		url:HYWX.basePath+"/wx/pay/ajaxBuyList",
        		data:{
        			"searchContent":$("#search").val().trim(),
        			"pageNumber":HYWX.loadMore.pageNumber ,
        			"pageSize":HYWX.loadMore.pageSize          	
        		},
        		loadType:"more",
        		success:function(data){
        			var result = data.result;
                    //格式化日期
                    console.log(result)
                    for(var r in result){
                    	result[r].createTime = new Date(result[r].createTime).format("yyyy-MM-dd");
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
        $(document).on('infinite', '.infinite-scroll-bottom', function() {
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