$(function(){	
	$("table").on("input","input[name='detailList.qty']",function(){	
		countMoney($(this));
	});
	
	$("table").on("input","input[name='detailList.price']",function(){
		countMoney($(this));
	});
	$("table").on("input","input[name='detailList.money']",function(){
		countPrice($(this));
	});
	
});
function countMoney(this_){
	var tr_dom=this_.parent().parent();
	var qty=tr_dom.find("input[name='detailList.qty']").val();
	var price=tr_dom.find("input[name='detailList.price']").val();
	if(qty==""||price==""){
		return ;
	}
	var money=Number(qty).mul(price).tomoney();
	tr_dom.find("input[name='detailList.money']").val(money);
}

function countPrice(this_){
	var tr_dom=this_.parent().parent();
	var qty=tr_dom.find("input[name='detailList.qty']").val();
	var money=tr_dom.find("input[name='detailList.money']").val();
	if(qty==""||price==""){
		return ;
	}
	var price=Number(money).div(qty).toFixed(4);
	tr_dom.find("input[name='detailList.price']").val(price);
}

function cancelReturn(){
	history.go(-1);
}