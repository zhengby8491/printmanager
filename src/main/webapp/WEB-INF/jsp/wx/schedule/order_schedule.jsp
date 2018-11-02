<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<title>工序进度</title>
</head>

<body>
	<div id="orderInfomation">
		<div class="infoContent">
			<div class="content infinite-scroll infinite-scroll-bottom" data-distance="100">
				<div class="list-block" id="ifoContent">
					<script id="ifoContenttmpl" type="text/x-dot-template">
{{ for(var i=0 ,catlen= it.length;i<catlen;i++) { }}
        <table id="ifoTable" style="border:0.05rem solid #999">
      <caption class="goodNameP" style="border:0.05rem solid #999;">{{= it[i].partName}}</caption>
     
            <thead>
                <tr style="background-color:#d4ceca">
                    <th style="width:10%">进度</th>
                    <th style="width:15%;text-align:left">工序名称</th>
                    <th style="width:20%">应产数</th>
                    <th style="width:20%">报产数</th>
                    <th style="width:35%">报产时间</th>
                </tr>
            </thead>
            <tbody>
 				{{ for(var j=0 ,goodslen=it[i].child.length;j<goodslen;j++) { }}
               <tr>		
                   <td>
						{{?it[i].child[j].reportQty/it[i].child[j].yieldQty>0}}							
							</div>
							<div style="background-image:url(${ctxStatic}\/wx\/img\/green1.png);width:100%;height:1rem; background-size: 0.5rem;
    								background-repeat: no-repeat;
    								background-position: center;" >
								{{?j==0&&j!=it[i].child.length-1}}
									<img style="position:relative;top:0.5rem;"height:25px" src="${ctxStatic}\/wx\/img\/theY.png">
								{{??j==it[i].child.length-1&&j!=0}}
									<img style="position:relative;top:-0.5rem;"height:25px" src="${ctxStatic}\/wx\/img\/theY.png">
								{{??j==0&&j==it[i].child.length-1}}

								{{?? }}
									<img style="height:25px" src="${ctxStatic}\/wx\/img\/theY.png">
								{{?}}
							</div>
						{{??}}
							<div style="background-image:url(${ctxStatic}\/wx\/img\/gay01.png);width:100%;height:1rem;    background-size: 0.5rem;
   									background-repeat: no-repeat;
  									background-position: center;" >
								{{?j==0}}
									<img style="position:relative;top:0.5rem;height:25px" src="${ctxStatic}\/wx\/img\/theY.png">
								{{?? j==it[i].child.length-1}}
									<img style="position:relative;top:-0.5rem;height:25px" src="${ctxStatic}\/wx\/img\/theY.png">
								{{??}}
									<img style="height:25px" src="${ctxStatic}\/wx\/img\/theY.png">
								{{?}}
							</div>
						{{?}}
						
                   </td>
	 				<td style="text-align:left">{{=it[i].child[j].procedureName}}</td>	
					<td>{{=it[i].child[j].yieldQty}}</td>
                    <td>{{=it[i].child[j].reportQty}}</td>
                    <td>
{{?it[i].child[j].reportQty!='0'&&getLocalTime(it[i].child[j].updateTime)!=null}}

							{{=getLocalTime(it[i].child[j].updateTime)}}
{{??}}
-
{{?}}
					</td>
               </tr>
 {{ } }}
           </tbody> 
        </table>
{{ } }}
  </script>
				</div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="schedule" name="module" />
		</jsp:include>
	</div>
	<script>
//获取orderNum
		function getQueryString(name) {
			  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
			  var r = window.location.search.substr(1).match(reg);
			  if (r != null) {
			    return unescape(r[2]);
			  }
			  return null;
			}
//获取数据
        	 $.ajax({
        	 url:'${ctx}/produce/work//ajaxDetailItemPc',
        	 data:{
        		 "billNo":getQueryString('oderNum')
        	 },
        	 success:function(result){
         		 var result = JSON.parse(result)
         		 data = result.result;
        		 if(data.length==0||!result){
        			  $.alert('暂无数据，请点击返回',function(){
        				  history.go(-1); 
        			  })
        		 }
                var interText = doT.template($("#ifoContenttmpl").text());
               $("#ifoContent").html(interText(data));  
        	 }
         })
      function getLocalTime(nS) { 
				var date =  new Date(parseInt(nS/1000)*1000);
				var   second=date.getSeconds()<=9?second=('0'+date.getSeconds()):second=date.getSeconds();  
				var   minute=date.getMinutes()<=9?minut=('0'+date.getMinutes()):minut=date.getMinutes();  
				var   hour=date.getHours()<=9?hour=('0'+date.getHours()):hour=date.getHours(); 
				var   year=date.getFullYear()<=9?year=('0'+date.getFullYear()):year=date.getFullYear();
				var   month=(date.getMonth()+1)<=9?month=('0'+(date.getMonth()+1)):month=(date.getMonth()+1);
				var   date=(date.getDate())<=9?date=('0'+date.getDate()):date=date.getDate();
				return year+"-"+month+"-"+date+"   "+hour+":"+minute+":"+second; 
				
   } 
</script>
</body>
</html>
