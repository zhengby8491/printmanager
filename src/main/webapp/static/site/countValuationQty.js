//计算计价数量
function countValuationQty(this_)
{
	var tr_dom = this_.parent().parent();
	var specifications = tr_dom.find("input[name='detailList.specifications']").val();
	var _splitArray = specifications.split("*");
	var weight = tr_dom.find("input[name='detailList.weight']").val();
	var qty = tr_dom.find("input[name='detailList.qty']").val();
	var length, width, height,valuationQty;
	
	if (specifications == "") // 当规格为空时，计价数量 = 采购量
	{
		valuationQty = qty;
		tr_dom.find("input[name='detailList.valuationQty']").val(valuationQty);
		return;
	}
	
	if (_splitArray.length == 1)
	{
		length = _splitArray[0];
		width = _splitArray[0];
		height = _splitArray[0];
	} else if (_splitArray.length == 2)
	{
		length = _splitArray[0];
		width = _splitArray[1];
		height = 0;
	} else if (_splitArray.length == 3)
	{
		length = _splitArray[0];
		width = _splitArray[1];
		height = _splitArray[2];
	}
	
	if (qty == "")
	{
		qty = 0;
	}
	var valuationUnitName = $.trim(tr_dom.find("input[name='detailList.valuationUnitName']").val());
	var stockUnitName = tr_dom.find("input[name='detailList.stockUnitName']").val();
	// 计价单位  == 库存单位时，  计价数量 = 材料数量
	if (valuationUnitName == stockUnitName)
	{
		valuationQty = qty;
		tr_dom.find("input[name='detailList.valuationQty']").val(valuationQty);
		return;
	}
	if (stockUnitName == undefined || stockUnitName == null)
	{// 解决库存模块和采购模块单位命名不一致问题
		stockUnitName = tr_dom.find("input[name='detailList.purchUnitName']").val();
	}
	// if(stockUnitName!="张"&&stockUnitName!="米"){
	// valuationQty=qty;
	// tr_dom.find("input[name='detailList.valuationQty']").val(valuationQty);
	// return;
	// }
	// 单位换算模块 先不引用
	var unitId = tr_dom.find("input[name='detailList.unitId']").val();
	if (unitId == undefined)
	{// 解决库存模块和采购模块单位命名不一致问题
		unitId = tr_dom.find("input[name='detailList.stockUnitId']").val();
	}
	var valuationUnitId = tr_dom.find("input[name='detailList.valuationUnitId']").val();
	if (unitId == valuationUnitId)
	{
		tr_dom.find("input[name='detailList.valuationQty']").val(qty);
		return;
	}

	// 计价单位精度
	var valuationUnitAccuracy = tr_dom.find("input[name='valuationUnitAccuracy']").val();
	var obj = Helper.Remote.getJson(Helper.basePath + "/basic/unitConvert/getByUnit", {
		'sourceUnitId' : unitId,
		'conversionUnitId' : valuationUnitId
	});

	if (obj != null)
	{
		var formula = obj.formula;
		formula = formula.replace("length", length);
		formula = formula.replace("weight", weight);
		formula = formula.replace("height", height);
		formula = formula.replace("width", width);
		formula = formula.replace("qty", qty);
		if ("千平方英寸" == valuationUnitName)
		{
			// 张转换千平方英寸的单位换算公式，进行修改，材料宽/25.4(材料宽/25.4*2取最接近的基数（29，31，33......55）/2)*材料长/25.4（保留两位小数）*采购数量
			// 比如 760*530的材料规格：530/25.4*2=41.73228，那么就是取43，43/2*760/25.4*采购数量，
			var baseQty = Number(length).div(25.4);
			if (baseQty <= 14.5)
			{
				baseQty = 14.5;
			} else if (baseQty > 14.5 && baseQty <= 27.5)
			{
				baseQty = baseQty.mul(2);
				baseQty = ((baseQty.ceil()) % 2 == 0) ? (baseQty.ceil() + 1).div(2) : baseQty.ceil().div(2);
			} else if (baseQty > 27.5 && baseQty <= 55)
			{
				baseQty = ((baseQty.ceil()) % 2 == 0) ? (baseQty.ceil() + 1) : baseQty.ceil();
			}
			valuationQty = baseQty.mul(width).div(25.4).mul(qty).div(1000).toFixed(valuationUnitAccuracy);
			// valuationQty =
			// Number(eval(''+formula+'')).mul(baseQty).toFixed(valuationUnitAccuracy);
		} else
		{
			valuationQty = Number(eval('' + formula + '')).toFixed(valuationUnitAccuracy);
		}

	} else
	{
		Helper.message.warn("无此单位换算公式，请录入公式");
		valuationQty = qty;
	}
	/*
	 * if(stockUnitName=="张"){ if(valuationUnitName=="吨"){
	 * valuationQty=Number(length).mul(width).mul(weight).mul(qty).div(1000000000000).toFixed(valuationUnitAccuracy);
	 * 
	 * }else if(valuationUnitName=="千克"){
	 * valuationQty=Number(length).mul(width).mul(weight).mul(qty).div(1000000000).toFixed(valuationUnitAccuracy);
	 * 
	 * }else if(valuationUnitName=="KG"){
	 * valuationQty=Number(length).mul(width).mul(weight).mul(qty).div(1000000000).toFixed(valuationUnitAccuracy);
	 * 
	 * }else if(valuationUnitName=="令"){ valuationQty=qty/500; }else
	 * if(valuationUnitName=="张"){ valuationQty=qty; }else
	 * if(valuationUnitName=="平方英寸"){
	 * valuationQty=Number(length).div(25.4).mul(width).div(25.4).mul(qty).toFixed(valuationUnitAccuracy);
	 * }else if(valuationUnitName=="千平方英寸"){
	 * //张转换千平方英寸的单位换算公式，进行修改，材料宽/25.4(材料宽/25.4*2取最接近的基数（29，31，33......55）/2)*材料长/25.4（保留两位小数）*采购数量
	 * //比如 760*530的材料规格：530/25.4*2=41.73228，那么就是取43，43/2*760/25.4*采购数量，
	 * 
	 * var baseQty=Number(length).div(25.4); if(baseQty<=14.5) { baseQty=14.5;
	 * }else if(baseQty>14.5&&baseQty<=27.5) { baseQty=baseQty.mul(2);
	 * baseQty=((baseQty.ceil())%2
	 * ==0)?(baseQty.ceil()+1).div(2):baseQty.ceil().div(2); }else
	 * if(baseQty>27.5&&baseQty<=55) { baseQty=((baseQty.ceil())%2
	 * ==0)?(baseQty.ceil()+1):baseQty.ceil(); }
	 * valuationQty=baseQty.mul(width).div(25.4).mul(qty).div(1000).toFixed(valuationUnitAccuracy); //
	 * valuationQty=Number(length).div(25.4).mul(width).div(25.4).mul(qty).div(1000).toFixed(valuationUnitAccuracy);
	 * }else if(valuationUnitName=="平方米"){
	 * valuationQty=Number(length).mul(width).mul(qty).div(1000000).toFixed(valuationUnitAccuracy);
	 * }else{ valuationQty=qty; } }else if(stockUnitName=="米"){
	 * if(valuationUnitName=="吨"){
	 * valuationQty=Number(width).mul(weight).mul(qty).div(1000000000).toFixed(valuationUnitAccuracy);
	 * }else if(valuationUnitName=="KG"){
	 * valuationQty=Number(width).mul(weight).mul(qty).div(1000000).toFixed(valuationUnitAccuracy);
	 * }else if(valuationUnitName=="千克"){
	 * valuationQty=Number(width).mul(weight).mul(qty).div(1000000).toFixed(valuationUnitAccuracy);
	 * }else if(valuationUnitName=="kg"){
	 * valuationQty=Number(width).mul(weight).mul(qty).div(1000000).toFixed(valuationUnitAccuracy);
	 * }else if(valuationUnitName=="平方米"){
	 * valuationQty=Number(width).mul(qty).div(1000).toFixed(valuationUnitAccuracy);
	 * }else{ valuationQty=qty; } }
	 */

	tr_dom.find("input[name='detailList.valuationQty']").val(valuationQty);
}
// 计算赠送计价数量
function countValuationQty2(this_)
{
	var tr_dom = this_.parent().parent();
	var specifications = tr_dom.find("input[name='detailList.specifications']").val();
	var _splitArray = specifications.split("*");
	var weight = tr_dom.find("input[name='detailList.weight']").val();
	var qty = tr_dom.find("input[name='detailList.freeQty']").val();
	var length, width, height,valuationQty;
	
	if (specifications == "") // 当规格为空时，计价数量 = 采购量
	{
		valuationQty = qty;
		tr_dom.find("input[name='detailList.freeValuationQty']").val(valuationQty);
		return;
	}
	
	if (_splitArray.length == 1)
	{
		length = _splitArray[0];
		width = _splitArray[0];
		height = _splitArray[0];
	} else if (_splitArray.length == 2)
	{
		length = _splitArray[0];
		width = _splitArray[1];
		height = 0;
	} else if (_splitArray.length == 3)
	{
		length = _splitArray[0];
		width = _splitArray[1];
		height = _splitArray[2];
	}
	
	if (qty == "")
	{
		qty = 0;
	}
	var valuationUnitName = $.trim(tr_dom.find("input[name='detailList.valuationUnitName']").val());
	var stockUnitName = tr_dom.find("input[name='detailList.stockUnitName']").val();
	// 计价单位  == 库存单位时，  计价数量 = 材料数量
	if (valuationUnitName == stockUnitName)
	{
		valuationQty = qty;
		tr_dom.find("input[name='detailList.freeValuationQty']").val(valuationQty);
		return;
	}
	if (stockUnitName == undefined || stockUnitName == null)
	{// 解决库存模块和采购模块单位命名不一致问题
		stockUnitName = tr_dom.find("input[name='detailList.purchUnitName']").val();
	}
	// if(stockUnitName!="张"&&stockUnitName!="米"){
	// valuationQty=qty;
	// tr_dom.find("input[name='detailList.valuationQty']").val(valuationQty);
	// return;
	// }
	// 单位换算模块 先不引用
	var unitId = tr_dom.find("input[name='detailList.unitId']").val();
	if (unitId == undefined)
	{// 解决库存模块和采购模块单位命名不一致问题
		unitId = tr_dom.find("input[name='detailList.stockUnitId']").val();
	}
	var valuationUnitId = tr_dom.find("input[name='detailList.valuationUnitId']").val();
	if (unitId == valuationUnitId)
	{
		tr_dom.find("input[name='detailList.freeValuationQty']").val(qty);
		return;
	}

	// 计价单位精度
	var valuationUnitAccuracy = tr_dom.find("input[name='valuationUnitAccuracy']").val();
	var obj = Helper.Remote.getJson(Helper.basePath + "/basic/unitConvert/getByUnit", {
		'sourceUnitId' : unitId,
		'conversionUnitId' : valuationUnitId
	});

	if (obj != null)
	{
		var formula = obj.formula;
		formula = formula.replace("length", length);
		formula = formula.replace("weight", weight);
		formula = formula.replace("height", height);
		formula = formula.replace("width", width);
		formula = formula.replace("qty", qty);
		if ("千平方英寸" == valuationUnitName)
		{
			// 张转换千平方英寸的单位换算公式，进行修改，材料宽/25.4(材料宽/25.4*2取最接近的基数（29，31，33......55）/2)*材料长/25.4（保留两位小数）*采购数量
			// 比如 760*530的材料规格：530/25.4*2=41.73228，那么就是取43，43/2*760/25.4*采购数量，
			var baseQty = Number(length).div(25.4);
			if (baseQty <= 14.5)
			{
				baseQty = 14.5;
			} else if (baseQty > 14.5 && baseQty <= 27.5)
			{
				baseQty = baseQty.mul(2);
				baseQty = ((baseQty.ceil()) % 2 == 0) ? (baseQty.ceil() + 1).div(2) : baseQty.ceil().div(2);
			} else if (baseQty > 27.5 && baseQty <= 55)
			{
				baseQty = ((baseQty.ceil()) % 2 == 0) ? (baseQty.ceil() + 1) : baseQty.ceil();
			}
			valuationQty = baseQty.mul(width).div(25.4).mul(qty).div(1000).toFixed(valuationUnitAccuracy);
			// valuationQty =
			// Number(eval(''+formula+'')).mul(baseQty).toFixed(valuationUnitAccuracy);
		} else
		{
			valuationQty = Number(eval('' + formula + '')).toFixed(valuationUnitAccuracy);
		}

	} else
	{
		Helper.message.warn("无此单位换算公式，请录入公式");
		valuationQty = qty;
	}

	tr_dom.find("input[name='detailList.freeValuationQty']").val(valuationQty);
}