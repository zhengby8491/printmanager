/*======流程拆分业务新增 =========*/

$(function(){
    //计算材料开数
    $(document).on("change",".material-class input[name='materialStyle']",function(){
    	var materialStyle = $(this).val();
    	var style = $(this).parents(".fold_table").find("input[name='partList.style']").val();
    	if(style && materialStyle){
    		var materialStyleArr = materialStyle.split("*");
    		var styleArr = style.split("*");
    		if(Helper.isNotEmpty(materialStyleArr) && Helper.isNotEmpty(styleArr)){
    			var msLength = Number(materialStyleArr[0]);
    			var msWidth  = Number(materialStyleArr[1]);
    			var sLength = Number(styleArr[0]);
    			var sWidth  = Number(styleArr[1]);
    			
    			var materialSplitQty1 = msLength.div(sLength).trunc().mul((msWidth.div(sWidth)).trunc());
    			var materialSplitQty2 = msLength.div(sWidth).trunc().mul((msWidth.div(sLength)).trunc());
    			var materialSplitQty =1;
    			if(materialSplitQty1 > materialSplitQty2){
    				materialSplitQty = materialSplitQty1;
    			}else{
    				materialSplitQty = materialSplitQty2
    			}
    			
    			$(this).parents(".material-class").find("input[name='materialSplitQty']").val(materialSplitQty > 1?materialSplitQty:1);
    			
    			//计算 材料用量
    			count_part_materialNum($(this).parents(".material-class"));
    		}
    	}
    });
    //计算材料开数
    $(document).on("change",".for_sel input[name='partList.style']",function(){
    	var materialStyleArrays = $(this).parents(".fold_table").find("input[name='materialStyle']");
    	var style = $(this).val();
    	if(style && Helper.isNotEmpty(materialStyleArrays)){
    		
    		$.each( materialStyleArrays, function(i, data){
        		var materialStyleArr = data.value.split("*");
        		var styleArr = style.split("*");
        		if(Helper.isNotEmpty(materialStyleArr) && Helper.isNotEmpty(styleArr)){
        			var msLength = Number(materialStyleArr[0]);
        			var msWidth  = Number(materialStyleArr[1]);
        			var sLength = Number(styleArr[0]);
        			var sWidth  = Number(styleArr[1]);
        			
        			var materialSplitQty1 = msLength.div(sLength).trunc().mul((msWidth.div(sWidth)).trunc());
        			var materialSplitQty2 = msLength.div(sWidth).trunc().mul((msWidth.div(sLength)).trunc());
        			var materialSplitQty =1;
        			if(materialSplitQty1 > materialSplitQty2){
        				materialSplitQty = materialSplitQty1;
        			}else{
        				materialSplitQty = materialSplitQty2
        			}
        			
        			$(this).parents(".material-class").find("input[name='materialSplitQty']").val(materialSplitQty > 1?materialSplitQty:1);
        			
        			//计算 材料用量
        			count_part_materialNum($(this).parents(".material-class"));
        		}  
    		});
    	}
    });
});

	//计算金额
function calcMoney(obj){
	var qty_dom=$(obj).parent().parent().find("input[name='productList.saleProduceQty']");
	var price_dom=$(obj).parent().parent().find("input[name='productList.price']");
	var money_dom=$(obj).parent().parent().find("input[name='productList.money']");
	
	money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());//金额=(单价*数量)
}
	
	

$(function(){
	//所有单价改变事件
	$("#product_table tbody").on("blur","input[name='productList.price']",function(){
		calcMoney(this);
	});
	
	//金额改变事件
	$("table tbody").on("blur","input[name='productList.money']",function(){
		var money_value=Number($(this).val()).tomoney();
		$(this).val(money_value);
		calcPrice(this);
	});
});


//计算加工单价 （输入金额）
function calcPrice(obj){
	var qty_dom=$(obj).parent().parent().find("input[name='productList.saleProduceQty']");
	var price_dom=$(obj).parent().parent().find("input[name='productList.price']");
	var money_dom=$(obj).parent().parent().find("input[name='productList.money']");
	
	if(qty_dom.val()==0||qty_dom.val().trim()==''){
	    Helper.message.warn("请先输入数量");
	    money_dom.val(0);
	    return;
	}
	var price=Number(money_dom.val()).div(qty_dom.val()).toFixed(4);
	price_dom.val(price);
}