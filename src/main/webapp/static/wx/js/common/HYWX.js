//印管家项目JS核心工具包
//FrameWork基本信息
//console.log(navigator.userAgent);
var HYWX = {};

HYWX.isNull = function(value)
{
    if(value === undefined || value === null) { return true; }
    return false;
};
//数字操作
HYWX.math = {

    // 加法函数,arg1加上arg2的精确结果
    add: function(arg1, arg2)
    {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length
        }catch(e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        }catch(e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        return (arg1 * m + arg2 * m) / m
    },
    // 减法函数，用来得到精确的减法结果
    subtr: function(arg1, arg2)
    {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        }catch(e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        }catch(e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        // last modify by deeka
        // 动态控制精度长度
        n = (r1 >= r2) ? r1 : r2;
        return Number(((arg1 * m - arg2 * m) / m).toFixed(n));
    },
    // 乘法函数，用来得到精确的乘法结果
    // 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
    // 调用：accMul(arg1,arg2)
    // 返回值：arg1乘以arg2的精确结果
    mul: function(arg1, arg2)
    {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        }catch(e) {
        }
        try {
            m += s2.split(".")[1].length
        }catch(e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    },
    // 除法函数，用来得到精确的除法结果
    // 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
    // 调用：accDiv(arg1,arg2)
    // 返回值：arg1除以arg2的精确结果
    div: function(arg1, arg2)
    {
        var t1 = 0, t2 = 0, r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length
        }catch(e) {
        }
        try {
            t2 = arg2.toString().split(".")[1].length
        }catch(e) {
        }
        with(Math) {
            r1 = Number(arg1.toString().replace(".", ""))
            r2 = Number(arg2.toString().replace(".", ""))
            return (r1 / r2) * pow(10, t2 - t1);
        }
    },
    // 丢弃小数部分
    trunc: function(arg)
    {
        return parseInt(arg);

    },
    // 向上取整
    ceil: function(arg)
    {
        return Math.ceil(arg);
    },
    // 向下取整
    floor: function(arg)
    {
        return Math.floor(arg);
    },
    // 四舍五入
    round: function(arg)
    {
        return Math.round(arg);
    },
    // 转换成money
    money: function(arg)
    {
        if(arg.toString().indexOf(".") > 0) {
            return Number(Number(arg).toFixed(2));
        }else {
            return Number(arg);
        }
    }
}
HYWX.message = {
		loading:function(){
			$.showIndicator();
		},
		hideLoading:function(){
			$.hideIndicator();
		},
		alert:function(value){
			$.alert(value);
		}
};
HYWX.isEmpty = function(value)
{
    if(HYWX.isNull(value)) {
        return true;
    }else if(typeof (value) == "string") {
        if(value.trim() === "" || value === "undefined" || value === "[]" || value === "{}") { return true; }
    }else if(typeof (value) === "object") {
        return value.length <= 0 ? true : false;
    }else {
        return false;
    }
};
HYWX.request = function(obj) {
    $.ajax({
        type: obj.method||"POST",
        url: obj.url,
        data:obj.data, 
        dataType: obj.dataType||"json",
        contentType: obj.contentType||'application/json;charset=utf-8', //设置请求头信息  
        async: obj.async||true,//默认异步请求
        success: (function(data){
            if(data=="turn"){
                alert("微信授权失效，我们将重新为您拉去授权");  
                window.location.href=HYWX.basePath+"/wx/menu/jumping";
                return;
            }
            obj.success(data);
        }),
        error:obj.error||(function(){
        	$.toast("操作失败!",2000,"mytoast"); 
        }),
        beforeSend:obj.beforeSend||function(){   
        	//$.showIndicator(); 
        },            
        complete:obj.complete||function(){
        	//$.hideIndicator(); 
        },
    });
};
HYWX.requestByObj = function(obj) {
      $.ajax({
          type: obj.method||"POST",
          url: obj.url,
          data:JSON.stringify(obj.data||{}),//将form序列化成JSON字符串  
          dataType: obj.dataType||"json",
          contentType: obj.contentType||'application/json;charset=utf-8', //设置请求头信息  
          async: obj.async||true,//默认异步请求
          success: (function(data){
              if(data=="turn"){
                  alert("微信授权失效，我们将重新为您拉去授权");  
                  window.location.href=HYWX.basePath+"/wx/menu/jumping";
                  return;
              }
              obj.success(data);
          }),
          error:obj.error||(function(){
        		  $.toast("操作失败!",2000,"mytoast");
          }),
          beforeSend:obj.beforeSend||function(){  
        	  $(".disable_mask").show(); 
          },            
          complete:obj.complete||function()
          {
        	  $(".disable_mask").hide();
          },
      });
};

HYWX.loadMore = {
    // 每次加载添加多少条目
     pageSize:12,
    // 从第一页开始加载
     pageNumber:1,
     addData:function(obj){
    	 $.ajax({
             type: obj.method||"POST",
             url: obj.url,
             data:JSON.stringify(obj.data||{}),//将form序列化成JSON字符串  
             dataType: obj.dataType||"json",
             contentType: obj.contentType||'application/json;charset=utf-8', //设置请求头信息  
             async: obj.async||true,//默认异步请求
             success: (function(data){
                 if(data=="turn"){
                     alert("微信授权失效，我们将重新为您拉去授权");  
                     window.location.href=HYWX.basePath+"/wx/menu/jumping";
                     return;
                 }
                 obj.success(data);
                 count = data.count;
                 if(count==0){
                	// 加载完毕，则注销无限加载事件，以防不必要的加载
  	                $.detachInfiniteScroll($('.infinite-scroll'));
  	                // 删除加载提示符
  	                $('.infinite-scroll-preloader').hide();	    
	                $("#itemlist").append("<div class='empty_tip'>目前没有任何数据</div>");
	                return;
	             }
                 if((HYWX.loadMore.pageNumber-1)*HYWX.loadMore.pageSize >= count) { 
                     // 加载完毕，则注销无限加载事件，以防不必要的加载
 	                $.detachInfiniteScroll($('.infinite-scroll'));
 	                // 删除加载提示符
 	                $('.infinite-scroll-preloader').hide();	                
 	                $("#itemlist").append("<div class='empty_tip'>已经看到最后啦</div>") 	                
 	            }                        
                 //重置刷新
                 $.refreshScroller();
             }),
             error:obj.error||(function(){
           		  $.toast("操作失败!",2000,"mytoast");
             }),
             beforeSend:function(){
            	 $(".disable_mask").show(); 
             },            
             complete:function(){
            	 $(".disable_mask").hide();
             },
         });;
     }   
};
//删除两边的空格
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
                    "M+": this.getMonth() + 1, //月份 
                    "d+": this.getDate(), //日 
                    "h+": this.getHours(), //小时 
                    "m+": this.getMinutes(), //分 
                    "s+": this.getSeconds(), //秒 
                    "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
                    "S": this.getMilliseconds() //毫秒 
                };
                if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
            }
            
$(function(){
	//滚动事件
    $(".content").scroll(function(){
   		var win_height = $("body").height();
       	var scroll_height = $(".content").scrollTop();
   		if(scroll_height>=1.5*win_height){
   			$("#to_top").show();
   		}else if(scroll_height>0&&scroll_height<1.5*win_height){
   			$("#to_top").hide();
   		}
   	})
	//点击top图标
   	$(document).on("click","#to_top",function(){
   		$(".content").scrollTop(0);
   		$("#to_top").hide();
   	}) 
   	/* 文本溢出 */
	$(document).on("click",".item-value",function(){
		$(this).toggleClass("display_all");
	})
	/*tab切换*/
	$(document).on("click",".tab-link",function(){
		$(this).addClass("active").siblings(".tab-link").removeClass("active");
	})
	/* 搜索清空按钮 */
//	$(document).on("focus","#search",function(){
//		$(".empty_btn").show();
//	})
//	$(document).on("blur","#search",function(){
//		$(".empty_btn").hide();
//	})
	
})
Number.prototype.tomoney = function()
{
    return HYWX.math.money(this);
};
