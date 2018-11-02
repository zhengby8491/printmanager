/* 判断当前用户是否有权限查看金额 */
var hasPermission = Helper.basic.hasPermission('stock:materialSplit:money');

$(function()
{
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("材料分切列表");
	});
	// 保存
	$(document).on("click", "#btn_save,#btn_save_audit", function()
	{
		fixEmptyValue();
		var this_ = $(this);
		var validate = true;
		if ($("#warehouseId").val() < 0)
		{
			Helper.message.warn("请选择仓库");
			validate = false;
			return;
		}
		if ($("table input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请录入明细");
			validate = false;
			return;
		}
		$("table input[name='detailList.qty']").each(function()
		{
			// console.log("qty="+$(this).val());
			if (Helper.isEmpty($(this).val()) || Number($(this).val()) <= 0)
			{
				Helper.message.warn("数量必须大于0");
				validate = false;
				return;
			}
		});
		if (validate)
		{// 保存
			var isSave = false;// 是否弹出空规格提示
			var isEqual = false;// 是否有不同的单位
			$("input[name='detailList.specifications']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					var tr_dom = $(this).parent().parent();
					var valuationUnitName = tr_dom.find("input[name='detailList.valuationUnitName']").val();
					var stockUnitName = tr_dom.find("input[name='detailList.stockUnitName']").val();
					if (valuationUnitName != stockUnitName)
					{
						isEqual = true;
					}
					isSave = true;
				}
			});
			if (isSave)
			{
				Helper.message.confirm('存在空的材料规格，是否保存', function(index)
				{
					if (isEqual == true)
					{
						Helper.message.warn("有需要单位换算的材料，请输入规格进行换算");
						return;
					} else
					{
						$("#isCheck").val(this_.attr("id") == "btn_save_audit" ? "YES" : "NO");
						form_submit();
					}
				})
			} else
			{
				$("#isCheck").val(this_.attr("id") == "btn_save_audit" ? "YES" : "NO");
				form_submit();
			}
		}
	});

	// 日期初始化
	$("#splitTime").val(new Date().format('yyyy-MM-dd'));
	// 初始化批量修改仓库、税收、交货日期悬浮窗
	$("#batch_edit_wareHouse,#batch_edit_taxRate,#batch_edit_deliveryTime").each(function()
	{
		$(this).powerFloat({
			eventType : "click",
			targetAttr : "src",
			reverseSharp : true,
			container : $(this).siblings(".batch_box_container")
		})
	})
	$("#selectMaterial").click(function()
	{
		Helper.popup.show('选择材料库存', Helper.basePath + '/stock/material/quick_select?warehouseId=' + $("#warehouseId").val() + '&multiple=false', '1030', '490');
	});
	// 删除明细记录
	$("table").on("click", ".delete_row", function()
	{
		$(this).parent().parent().remove();
		setRowIndex();
	});

	// 删除明细记录
	$("#add_row").click(function()
	{
		if (Helper.isNotEmpty($("#materialName").val()))
		{
			addRow();
			setRowIndex();
		} else
		{
			Helper.message.warn("请先选择分切材料");
		}
	})

	$(document).on("input", "input[name='detailList.qty']", function()
	{
		var valuationPrice = $("#valuationPrice").val();
		var money = $(this).parent().parent().find("input[name='detailList.money']");
		countValuationQty($(this));
		var valuationQty = $(this).parent().parent().find("input[name='detailList.valuationQty']").val();
		money.val(Number(valuationQty).mul(Number(valuationPrice)).tomoney())
		var moneyVal = Number(money.val());
		var qtyVal = Number($(this).val());
		var price = moneyVal.div(qtyVal).toFixed(4);
		if (price == "NaN")
		{
			price = 0;
		}
		$(this).parent().parent().find("input[name='detailList.price']").val(price);
	});

	$(document).on("change", "input[name='detailList.qty']", function()
	{
		var stockUnitAccuracy = $(this).parent().parent().find("input[name='stockUnitAccuracy']").val();
		if ($(this).val().split(".")[1] != undefined && $(this).val().split(".")[1].length > Number(stockUnitAccuracy))
		{
			Helper.message.tips("精度为" + stockUnitAccuracy + "位小数", this);
			$(this).val($(this).val().split(".")[0]);
		}
		$(this).trigger("input");
	})
	$(document).on("input", "#qty", function()
	{
		var valuationPrice = $("#valuationPrice").val();
		var money = $("#money");
		countMValuationQty();
		var valuationQty = $("#valuationQty");
		money.val(Number(valuationQty.val()).mul(valuationPrice))
		$("#price").val((Number(money.val()).div($(this).val()).toFixed(4)) || 0);
	});

	setRowIndex();
	setValuationQty();
	$("input[name='detailList.qty']").trigger("input");
})
// 批量修改仓库
function batchEditWareHouse()
{
	$("table select[name='detailList.warehouseId']").val($(".batch_wareHouse_select").val());
}
// 序号重新排列
function setRowIndex()
{
	var rowList = $("td[name='rowIndex']")
	for (var i = 0; i < rowList.length; i++)
	{
		rowList.eq(i).text(i + 1);
	}
}
function setValuationQty()
{
	for (var i = 0; i < $("detailList.qty").length; i++)
	{
		countValuationQty($("detailList.qty").eq(i))
	}
}

function form_submit()
{
	$("#forceCheck").val("NO");

	Helper.request({
		url : Helper.basePath + "/stockmaterial/split/save",
		data : $("#form_split").formToJson(),
		success : function(data)
		{
			if (data.isSuccess)
			{
				Helper.message.suc('已保存!');
				if (data.returnObject != null && data.returnObject.length > 0)
				{
					var str = '审核失败,以下材料库存数量不足\n';
					for (var i = 0; i < data.returnObject.length; i++)
					{
						str = str + '名称:' + data.returnObject[i].material.name + (data.returnObject[i].specifications || '') + '   目前库存数量' + data.returnObject[i].qty + '\n';
					}
					Helper.message.confirm(str, function(index)
					{
						Helper.message.confirm("确认操作会引起负库存，是否允许负库存?", function(index)
						{
							forceCheck(data.returnValue.id);
						}, function(index)
						{
							location.href = Helper.basePath + '/stockmaterial/split/view/' + data.returnValue.id;
						});
					}, function(index)
					{
						location.href = Helper.basePath + '/stockmaterial/split/view/' + data.returnValue.id;
					});
					$("#btn_save").removeAttr("disabled");
					$("#btn_save_audit").removeAttr("disabled");

				} else
				{
					location.href = Helper.basePath + '/stockmaterial/split/view/' + data.returnValue.id;
				}

			} else
			{
				Helper.message.warn('保存失败!' + data.message);
				$("#btn_save").removeAttr("disabled");
				$("#btn_save_audit").removeAttr("disabled");
			}
		},
		error : function(data)
		{
			// console.log(data);
			$("#btn_save").removeAttr("disabled");
			$("#btn_save_audit").removeAttr("disabled");
		}
	});
}

// 多选获取返回信息
function getCallInfo_material_stock(obj)
{
	$("#materialName").val(obj.material.name);
	$("#materialId").val(obj.material.id);
	$("#materialClassId").val(obj.material.materialClassId);
	$("#stockMaterialId").val(obj.id);
	$("#specifications").val(obj.specifications);
	$("#stockUnitName").val(obj.material.stockUnitName);
	$("#stockUnitId").val(obj.material.stockUnitId);
	$("#price").val(obj.price);
	$("#qty").val(obj.qty);
	$("#valuationUnitName").val(obj.material.valuationUnitName);
	$("#valuationUnitId").val(obj.material.valuationUnitId);
	$("#valuationQty").val(obj.valuationQty);
	$("#valuationPrice").val(obj.valuationPrice);
	$("#code").val(obj.material.code);
	$("#weight").val(obj.material.weight);
	$("#warehouseName").val(obj.warehouseName);
	$("#money").val(obj.money);
	$("#valuationUnitAccuracy").val(Helper.basic.info('UNIT', obj.material.valuationUnitId).accuracy);
	$("#tbody").empty();
	addRow();
	setRowIndex();
}

function addRow()
{
	var warehouseId = $("#warehouseId").val();
	var str = '<tr><td name="rowIndex"></td>' + '<td class="td-manage">' + '<i class="delete_row fa fa-trash-o"></i>' + '<td><input name="detailList.code" class="tab_input" type="text" readonly="readonly" value="' + $("#code").val() + '"/></td>' + '<td><input name="detailList.materialName" class="tab_input" type="text" readonly="readonly" value="' + $("#materialName").val() + '"/>' + '<input name="detailList.materialId" type="hidden" value="' + $("#materialId").val() + '"/><input name="detailList.materialClassId" type="hidden" value="' + $("#materialClassId").val() + '"/></td>' + '<td><input name="detailList.specifications" class="tab_input bg_color" type="text" value=""/></td>'
			+ '<td><input name="detailList.weight" class="tab_input" type="text" readonly="readonly" value="' + $("#weight").val() + '"/></td>' + '<td><input name="detailList.stockUnitName" class="tab_input" type="text" readonly="readonly" value="' + $("#stockUnitName").val() + '"/><input name="detailList.stockUnitId" type="hidden" value="' + $("#stockUnitId").val() + '"/></td>' + '<td><input name="detailList.qty" class="tab_input constraint_decimal bg_color" type="text" value="0"/></td>' + '<td><input name="detailList.valuationUnitName" class="tab_input" type="text" readonly="readonly" value="' + $("#valuationUnitName").val() + '"/></td>'
			+ '<td><input name="detailList.valuationQty" class="tab_input" type="text" readonly="readonly" value="0"/><input name="valuationUnitAccuracy" type="hidden" value="' + Helper.basic.info('UNIT', $("#valuationUnitId").val()).accuracy + '"/><input name="detailList.valuationUnitId" type="hidden" value="' + $("#valuationUnitId").val() + '"/></td>' + '<td>'+$("#phtml").clone(true).html()+'</td>' + (hasPermission ? '<td>' : '<td style="display:none;">')
			+ '<input name="detailList.price" class="tab_input" type="text" readonly="readonly" value="0"/><input name="detailList.valuationPrice" type="hidden" value="' + $("#valuationPrice").val() + '"/></td>' + (hasPermission ? '<td>' : '<td style="display:none;">') + '<input name="detailList.money" class="tab_input" type="text" readonly="readonly" value="0"/></td>' + '<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value=""/></td>' + '</tr>';
	$("#tbody").append(str);
	$("select[name='detailList.warehouseId']").last().val(warehouseId);
}

// 规格键盘输入控制
$(document).on('keypress', "input[name='detailList.specifications']", function()
{
	return (/\d|\*|[.]{1}/.test(String.fromCharCode(event.keyCode)));
});
// 规格失去焦点判断
$(document).on('blur', "input[name='detailList.specifications']", function()
{
	if (!Helper.validata.isMaterialSize($(this).val()))
	{
		Helper.message.warn("请录入正确的规格格式xxx或xxx*xxx或xxx*xxx*xxx (小数最多4位)");

		$(this).val("");
	}
	/*
	 * if
	 * (!/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/.test($(this).val())) {
	 * Helper.message.warn("请录入正确的规格格式 xxxx*xxxx"); $(this).val(""); }
	 */
	validataSpec($(this));
	$("input[name='detailList.qty']").trigger("input");
});
function cancelReturn()
{
	history.go(-1);
}
// 效验从表规格
function validataSpec(spec)
{
	var width = String($("#specifications").val()).split("*")[1];
	var length = String($("#specifications").val()).split("*")[0];
	var widthThis = String(spec.val()).split("*")[1];
	var lengthThis = String(spec.val()).split("*")[0];
	if (Number(widthThis) > Number(width) || Number(lengthThis) > Number(length))
	{
		Helper.message.warn("规格不能大于分切材料规格");
		spec.val($("#specifications").val())
	}
}

/**
 * 强制审核
 */
function forceCheck(id)
{
	$.ajax({
		cache : true,
		type : "POST",
		dataType : "json",
		url : Helper.basePath + '/stockmaterial/split/forceCheck/' + id,
		async : false,
		error : function(request)
		{
			layer.alert("Connection error");
		},
		success : function(data)
		{
			if (data.success)
			{
				location.href = Helper.basePath + '/stockmaterial/split/view/' + id;
			} else
			{
				layer.alert("审核失败")
			}
		}
	});
}
// 此方法仅为材料分切的主表使用
function countMValuationQty()
{
	var specifications = $("#specifications").val();
	var _splitArray = specifications.split("*");
	var weight = $("#weight").val();
	var qty = $("#qty").val();
	var length, width, height, valuationQty;

	if (specifications == "") // 当规格为空时，计价数量 = 采购量
	{
		valuationQty = qty;
		$("#valuationQty").val(valuationQty);
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
	var valuationUnitName = $.trim($("#valuationUnitName").val());
	var stockUnitName = $("#stockUnitName").val();
	// 计价单位 == 库存单位时， 计价数量 = 材料数量
	if (valuationUnitName == stockUnitName)
	{
		valuationQty = qty;
		$("#valuationQty").val(valuationQty);
		return;
	}
	if (stockUnitName == undefined || stockUnitName == null)
	{// 解决库存模块和采购模块单位命名不一致问题
		stockUnitName = $("#purchUnitName").val();
	}
	// 单位换算模块
	var unitId = $("#unitId").val();
	if (unitId == undefined)
	{// 解决库存模块和采购模块单位命名不一致问题
		unitId = $("#stockUnitId").val();
	}
	var valuationUnitId = $("#valuationUnitId").val();
	if (unitId == valuationUnitId)
	{
		$("#valuationQty").val(qty);
		return;
	}

	// 计价单位精度
	var valuationUnitAccuracy = $("#valuationUnitAccuracy").val();
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
		} else
		{
			valuationQty = Number(eval('' + formula + '')).toFixed(valuationUnitAccuracy);
		}

	} else
	{
		Helper.message.warn("无此单位换算公式，请录入公式");
		valuationQty = qty;
	}

	$("#valuationQty").val(valuationQty);
}