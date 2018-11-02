
$(function(){
	//修改金额
	$("table tbody").on("blur","input[name='detailList.money']",function(){
		var money_value=Number($(this).val()).tomoney();
		$(this).val(money_value);
		calcPrice(this);
	});
});

	



//修改金额
function calcPrice(obj){
  	var qty_dom=$(obj).parent().parent().find("input[name='detailList.qty']");
  	var price_dom=$(obj).parent().parent().find("input[name='detailList.price']");
  	var money_dom=$(obj).parent().parent().find("input[name='detailList.money']");
  	if(qty_dom.val()==0||qty_dom.val().trim()==''){
  	    Helper.message.warn("请先输入数量");
  	    money_dom.val(0);
  	    return;
  	}
  	var price=Number(money_dom.val()).div(qty_dom.val()).toFixed(4);
  	price_dom.val(price);
  	calcTaxRate(obj);
}
