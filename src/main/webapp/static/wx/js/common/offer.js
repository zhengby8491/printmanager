$(function()
{
	// 过滤工序为空的工序分类
	$(".popup_procedure").find(".procedure_ul").each(function()
	{
		$(this).each(function()
		{
			if ($(this).children("li").length == 0)
			{
				$(this).parent().remove();
			}
		})
	})
	// 关闭工序弹出框
	$(document).on("click", ".popup-overlay,.icon-guanbi", function()
	{
		$.closeModal();
	})
	// 删除工序
	$(document).on("click", ".btn_delete_procedure", function()
	{
		$(this).parent().remove();
	})
	// 数字的键盘输入控制
	$(document).on('keypress', ".constraint_number,.constraint_negative,#phone", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});
	// 数字、负号键盘输入控制
	$(document).on('keypress', ".constraint_positive", function()
	{
		return (/\d|\-/.test(String.fromCharCode(event.keyCode)));
	});
	// 数字、小数控制
	$(document).on('blur', ".constraint_number,.constraint_decimal", function()
	{
		$(this).val($(this).val().trim());
		if (/^\-?\d+(\.\d+)?$/.test($(this).val()))
		{
			if (Number($(this).val()) == 0)
			{
				$(this).val(0);
			}
		} else
		{
			$(this).val(0);
		}
		$(this).val(Number($(this).val()));
	});
	// 正数控制
	$(document).on('blur', ".constraint_negative,#phone", function()
	{
		$(this).val($(this).val().trim());
		if (/^\d+$/.test($(this).val()))
		{
			$(this).val(Number($(this).val()));
		} else
		{
			$(this).val("");
		}
	});
	// 正数控制
	$(document).on('focus', ".constraint_negative,#phone", function()
	{
		if ($(this).val() == 0)
		{
			$(this).val("");
		}
	});
	// 正数、正小数控制
	$(document).on('blur', ".constraint_decimal_negative", function()
	{
		$(this).val($(this).val().trim());
		if (/^\d+(\.\d+)?$/.test($(this).val()))
		{
			$(this).val(Number($(this).val()));
		} else
		{
			$(this).val("");
		}
	});

	// 输入法弹出触发
	$(window).resize(function()
	{
		var modalHeight = $(".modal").height() * (-0.5);
		$(".modal").css("margin-top", modalHeight + "px");
		var innerHeight = $(document).find(".offer_result").find(".modal-inner").height();
		$(document).find(".offer_result").scrollTop(innerHeight);
	})
})
// 处理报价结果
function formatResult(obj)
{
	replaceColor(obj);
	// TODO 名称要换如：纸张费用、印刷费用等
//	obj.procedureName = obj.procedureNames.replace(/#/g, "￥");
//	obj.materialCost = Number(obj.materialCost).tomoney();
//	obj.printColorCost = Number(obj.printColorCost).tomoney();
//	obj.procedureCost = Number(obj.procedureCost).tomoney();
//	obj.logisticsCost = Number(obj.logisticsCost).tomoney();
//	obj.unitPrice = Number(obj.unitPrice).toFixed(4);
//	obj.totalCost = Number(obj.totalCost).tomoney();
//	obj.alternativeCost = Number(obj.alternativeCost).tomoney();
	obj.printColor = obj.offerOrderQuoteOutList[0].printColor;
	obj.printName = obj.offerOrderQuoteOutList[0].printName;
	obj.printProcedure = obj.offerOrderQuoteOutList[0].printProcedure;
	obj.taxFee = obj.offerOrderQuoteOutList[0].taxFee;
	obj.taxPrice = obj.offerOrderQuoteOutList[0].taxPrice;
	obj.price = obj.offerOrderQuoteInnerList[0].price;
	obj.fee = obj.offerOrderQuoteOutList[0].fee;
	obj.profit = obj.offerOrderQuoteInnerList[0].profitFee;
	obj.untaxedFee = obj.offerOrderQuoteInnerList[0].untaxedFee;
	obj.untaxedPrice = obj.offerOrderQuoteInnerList[0].untaxedPrice;
	obj.createDateTime = new Date(obj.createDateTime).format("yyyy-MM-dd");
	obj.deliveryDate = new Date(obj.deliveryDate).format("yyyy-MM-dd");
	return obj;
}
// 转换颜色
function replaceColor(obj)
{
	if (obj.color != 0)
	{
		obj.color = getColorText(obj.color);
		obj.spotColor = getSpotColorText(obj.spotColor);
	} else
	{
		obj.innerColor = getColorText(obj.innerColor);
		obj.coverColor = getColorText(obj.coverColor);
		obj.insertColor = getColorText(obj.insertColor);
		obj.innerSpotColor = getSpotColorText(obj.innerSpotColor);
		obj.coverSpotColor = getSpotColorText(obj.coverSpotColor);
		obj.insertSpotColor = getSpotColorText(obj.insertSpotColor);
	}
	return obj;
	function getColorText(num)
	{
		var colorText = "";
		switch (num)
		{
		case 1:
			colorText = "单色";
			break;
		case 2:
			colorText = "双色";
			break;
		case 4:
			colorText = "四色";
			break;
		case 6:
			colorText = "六色";
			break;
		case 8:
			colorText = "八色";
			break;
		case 10:
			colorText = "十色";
			break;
		case 12:
			colorText = "十二色";
			break;
		}
		return colorText;
	}
	function getSpotColorText(num)
	{
		var spotColorText = "";
		switch (num)
		{
		case 1:
			spotColorText = "一专";
			break;
		case 2:
			spotColorText = "二专";
			break;
		case 3:
			spotColorText = "三专";
			break;
		case 4:
			spotColorText = "四专";
			break;
		default:
			spotColorText = "无专";
		}
		return spotColorText;
	}
};
(function($)
{
	$.fn.formToJson = function()
	{
		var jsonData1 = {};
		var serializeArray = this.serializeArray();
		// 先转换成{"id": ["12","14"], "name": ["aaa","bbb"],
		// "pwd":["pwd1","pwd2"]}这种形式
		$(serializeArray).each(function()
		{
			if (jsonData1[this.name] !== undefined)
			{
				if ($.isArray(jsonData1[this.name]))
				{
					jsonData1[this.name].push(HYWX.isEmpty(this.value) ? null : this.value);
				} else
				{
					jsonData1[this.name] = [ jsonData1[this.name], this.value ];
				}
			} else
			{
				jsonData1[this.name] = HYWX.isEmpty(this.value) ? null : this.value;
			}
		});

		// console.log(jsonData1);
		// console.log(JSON.stringify(jsonData1));
		// 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name":
		// "bb", "pwd":"pwd2"}]的形式
		var vCount = 0;
		// 计算json内部的数组最大长度
		for ( var item in jsonData1)
		{
			var temp = jsonData1[item];
			if ($.isArray(temp))
			{// 数组拆分
				var itemNameArray = item.split(".");
				var subName = itemNameArray[0];
				var subProperty = itemNameArray[1];
				if (!jsonData1[subName])
				{
					jsonData1[subName] = [];
				}
				for (var i = 0; i < temp.length; i++)
				{
					if (!jsonData1[subName][i])
					{
						jsonData1[subName][i] = {};
					}
					jsonData1[subName][i][subProperty] = HYWX.isEmpty(temp[i]) ? null : temp[i];
				}
				delete jsonData1[item];// 移除原有属性
			} else
			{
				if (item.split(".").length > 1)
				{
					var _itemNameArray = item.split(".");
					var _subName = _itemNameArray[0];
					var _subProperty = _itemNameArray[1];
					if (!jsonData1[_subName])
					{
						jsonData1[_subName] = [];
						jsonData1[_subName][0] = {};
					}
					jsonData1[_subName][0][_subProperty] = HYWX.isEmpty(temp) ? null : temp;
					delete jsonData1[item];// 移除原有属性
				}
			}
		}
		return jsonData1;
	};
})(Zepto)
