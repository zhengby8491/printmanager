function initCommonReport(reportQty,qualifiedQty,unqualified,callback){
	
	initTbody(reportQty, function(){
		var id = $(this).parent().parent().data('uniqueid');
	    var reportQtyVal = $(this).val();
	    var qualifiedQtyVal = $(this).parent().parent().find("input[name='"+qualifiedQty+"']").val();
	    var unqualifiedVal	= $(this).parent().parent().find("input[name='"+unqualified+"']").val();
	    if($.isFunction(callback)){
			 callback.call(this,id, reportQtyVal, qualifiedQtyVal, unqualifiedVal);	 
		 }
	});
	
	initTbody(qualifiedQty, function(){
		var id = $(this).parent().parent().data('uniqueid');
	    var qualifiedQtyVal = $(this).val();
	    var reportQtyVal = $(this).parent().parent().find("input[name='"+reportQty+"']").val();
	    var unqualifiedVal=  $(this).parent().parent().find("input[name='"+unqualified+"']").val();
	    if($.isFunction(callback)){
			 callback.call(id+"", reportQtyVal, qualifiedQtyVal, unqualifiedVal);	 
		 }
	})
	initTbody(unqualified, function(){
		var id = $(this).parent().parent().data('uniqueid');
	    var unqualifiedVal = $(this).val();
	    var qualifiedQtyVal = $(this).parent().parent().find("input[name='"+qualifiedQty+"']").val();
	    var reportQtyVal =  $(this).parent().parent().find("input[name='"+reportQty+"']").val();
	    if($.isFunction(callback)){
			 callback.call(id+"", reportQtyVal, qualifiedQtyVal, unqualifiedVal);	 
		 }
	})
}

function initTbody(input_name, callback)
{
	$("table tbody").on("blur","input[name='"+input_name+"']",function(){
		var obj = $(this).parent().parent().find("input[name='"+input_name+"']");
		 if($.isFunction(callback)) callback.call(obj);
	});
}


/*
function initReport(reportQty, qualifiedQty, unqualified)
{
	reportQty = reportQty || "reportQty";
	qualifiedQty = qualifiedQty || "qualifiedQty";
	unqualified = unqualified || "unqualified";
	var id ;
	// 上报数
	$("table tbody").on("blur","input[name='"+reportQty+"']",function(){
	    id = $(this).parent().parent().data('uniqueid');
	    var reportQtyVal = $(this).parent().parent().find("input[name='"+reportQty+"']").val()
	  if(Number(reportQty)>Number(postDataKey[id].yieldQty)){
		  Helper.message.warn('上报数不能超过应上报数!');
	  } else{
		  $(this).parent().parent().find("input[name='"+qualifiedQty+"']").val(reportQtyVal);
		  $(this).parent().parent().find("input[name='"+unqualified+"']").val(0);
		  postDataEditable[id].reportQty= reportQtyVal;
		  postDataEditable[id].unqualified=0;
		  postDataEditable[id].qualifiedQty=Number(reportQtyVal);
	  }
	});
	
	// 合格数
	$("table tbody").on("blur","input[name='qualifiedQty']",function(){
	    console.log($(this).parent().parent().find("input[name='qualifiedQty']").val());
	    var reportQty = $(this).parent().parent().find("input[name='reportQty']").val()
	    var qualifiedQty = $(this).val();
	    if(Number(qualifiedQty) > Number(reportQty)){
			 $(this).parent().parent().find("input[name='qualifiedQty']").val(reportQty); 
			 $(this).parent().parent().find("input[name='unqualified']").val(0);
			 return ;
		 }
	    var unqualified = Number(reportQty).subtr(Number(qualifiedQty)).tomoney()
	    console.log("不合格数="+unqualified);
	    $(this).parent().parent().find("input[name='unqualified']").val(unqualified);
	    postDataEditable[id].unqualified=unqualified;
	    postDataEditable[id].qualifiedQty=Number(qualifiedQty);
	});
	
	// 不合格数
	$("table tbody").on("blur","input[name='unqualified']",function(){
		 var reportQty = $(this).parent().parent().find("input[name='reportQty']").val()
		 var unqualified=  $(this).parent().parent().find("input[name='unqualified']").val();
		 if(Number(unqualified) > Number(reportQty)){
			 $(this).parent().parent().find("input[name='unqualified']").val(reportQty); 
			 $(this).parent().parent().find("input[name='qualifiedQty']").val(0);
			 return ;
		 }
		 var qualifiedQty = Number(reportQty).subtr(Number(unqualified)).tomoney()
		  console.log("合格数="+qualifiedQty);
		 $(this).parent().parent().find("input[name='qualifiedQty']").val(qualifiedQty);
		 postDataEditable[id].unqualified=Number(unqualified);
		 postDataEditable[id].qualifiedQty=qualifiedQty;
	});
}

*/