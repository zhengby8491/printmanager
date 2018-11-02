var hasPermission = Helper.basic.hasPermission('sale:order:money');
var _currentSelectProduct = null;

// 来自复制
var _isCopy = Boolean($("#isCopy").val());
// 来自报价
var _isOffer = Boolean($("#isOffer").val());
// 存放所有工序材料信息（当保存时使用）
var _allProduces = {};
// 解析报价的产品信息
console.info(_isOffer);
var _offerOrderIdList = $("#offerOrderIdList").val();
console.info(_offerOrderIdList);
if (_isOffer == true)
{
	var product = {};

	// 报价订单id
	if (_offerOrderIdList != '')
	{
		_offerOrderIdList = JSON.parse(_offerOrderIdList);
	}

	// 产品id
	var _productId = $("#offerDetail").val();
	if (_productId != '')
	{
		_productId = parseInt(_productId);
	}
	// 部件信息
	var _offerPartList = $("#offerPartList").val();
	if (_offerPartList != '')
	{
		_offerPartList = JSON.parse(_offerPartList);
		// 这里需要重新过滤，如果直接使用，很多属性在保存时会失败。 因为属性太多直接过滤效果好
		product.partList = [];
		for (var j = 0; j < _offerPartList.length; j++)
		{
			var _offerPart = _offerPartList[j];

			// 重新过滤工序
			var _procedureList = [];
			for (var i = 0; i < _offerPart.procedureList.length; i++)
			{
				var _procedure = _offerPart.procedureList[i];
				_procedureList.push({
					procedureId : _procedure.procedureId,
					procedureCode : _procedure.procedureCode,
					procedureName : _procedure.procedureName,
					procedureType : _procedure.procedureType,
					procedureClassId : _procedure.procedureClassId,
					isOutSource : _procedure.isOutSource
				});
			}
			// 重新过滤材料
			var _materialList = [];
			for (var i = 0; i < _offerPart.materialList.length; i++)
			{
				var _material = _offerPart.materialList[i];
				_materialList.push({
					id : _material.id,
					materialId : _material.materialId,
					materialCode : _material.materialCode,
					materialName : _material.materialName,
					style : _material.style,
					stockUnitId : _material.stockUnitId,
					valuationUnitId : _material.valuationUnitId,
					weight : _material.weight
				});
			}
			var _productList = [];
			for (var i = 0; i < _offerPart.productList.length; i++)
			{
				var _product = _offerPart.productList[i];
				_productList.push({
					productId : _product.productId,
					productName : _product.productName
				});
			}
			product.partList.push({
				partName : _offerPart.partName,
				procedureList : _procedureList,
				productList : _productList,
				materialList : _materialList
			});
		}
	}
	var _offerPack = $("#offerPack").val();
	if (_offerPack != '')
	{
		_offerPack = JSON.parse(_offerPack);
		// 重新过滤成品工序
		var _procedureList = [];
		for (var i = 0; i < _offerPack.procedureList.length; i++)
		{
			var _procedure = _offerPack.procedureList[i];
			_procedureList.push({
				procedureId : _procedure.procedureId,
				isOutSource : _procedure.isOutSource
			});
		}
		product.pack = {
			procedureList : _procedureList
		};
	}

	// 存放到对象中
	_allProduces[_productId] = product;
}

$(function()
{
	if (!hasPermission)
	{
		$("#detailList").find("th[name='salePrice']").hide();
		$("#detailList").find("th[name='money']").hide();
		$("#detailList").find("th[name='tax']").hide();
		$("#detailList tbody").find("input[name='detailList.price']").parent().hide();
		$("#detailList tbody").find("input[name='detailList.money']").parent().hide();
		$("#detailList tbody").find("input[name='detailList.tax']").parent().hide();
		$("dd").has("input[id=totalMoney]").hide();
		$("dd").has("input[id=noTaxTotalMoney]").hide();
		$("dd").has("input[id=totalTax]").hide();
	}
	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("销售订单列表");
	});
	// 选择客户
	$("#selectCustomer").click(function()
	{
		Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '500');
	});
	// 选择产品
	$("#select_product").click(function()
	{
		var _customerId = $("#customerId").val();
		if (Helper.isEmpty(_customerId))
		{
			Helper.message.alert("请先选择客户信息");
			return false;
		}
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=true&customerId=' + _customerId, '900', '490');
	});
	// 删除
	$("table tbody").on("click", "a[name='btn_del']", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
		sum();
	});
	// 选中行特效
	selectTr();
	// 查看历史单价
	$("#historyPrice").on("click", function()
	{
		var productId = $("#detailList input[type='radio']:checked").parents("tr").find("input[name='detailList.productId']").val();
		if (Helper.isEmpty(productId))
		{
			Helper.message.warn("请先选择一个产品");
			return;
		}
		Helper.popup.show('查看历史单价', Helper.basePath + '/sale/order/historyPrice/' + productId, '660', '500');
	});
	// 初始付款日期
	$("#deliveryTime").val(new Date().format('yyyy-MM-dd'));
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));
	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		fixEmptyValue();
		if (Helper.isEmpty($("#customerId").val()))
		{
			Helper.message.warn("请选择客户信息")
			return false;
		}
		if ($("input[name='detailList.qty']").size() <= 0)
		{
			Helper.message.warn("请选择产品");
			validate = false;
			return;
		}
		var flg = true;
		$("table input[name='detailList.qty']").each(function()
		{
			if (Number($(this).val()) <= 0)
			{
				Helper.message.warn("产品数量必须大于0");
				flg = false;
				return false;
			}
		});
		$("table select[name='detailList.taxRateId']").each(function()
		{
			if (Number($(this).val()) <= 0)
			{
				Helper.message.warn("请选择税收");
				flg = false;
				return false;
			}
		});
		/*
		 * if(Number($("#totalMoney").val())<=0) { Helper.message.warn("金额必须大于0");
		 * return false; }
		 */
		if (flg)
		{
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			form_submit();
		}
	})
	$("table tbody").on("blur", "input[name='detailList.money']", function()
	{
		var money_value = Number($(this).val()).tomoney();
		$(this).val(money_value);
		calcPrice(this);
	});
	// 所有单价改变事件
	$("table tbody").on("blur", "input[name='detailList.price']", function()
	{
		calcMoney(this);
	});
	// 所有数量改变事件
	$("table tbody").on("keyup blur", "input[name='detailList.qty']", function()
	{
		var qty_value = Number($(this).val()).trunc();
		$(this).val(qty_value);
		if (qty_value > 0)
		{
			calcMoney(this);
		}
	});
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

	// 初始化复制得工序材料信息
	function _initProcedureMaterials()
	{
		var products = $("#form_order").formToJson();
		if (products && products.detailList)
		{
			for ( var p in products.detailList)
			{
				var product = products.detailList[p];
				var typeofProduct = typeof product;
				if (typeofProduct == 'object')
				{
					product.master = products;
					product.id = product.id || 0;
					_currentSelectProduct = convertSaleDetailToProduct(product);

					if (_isCopy == true && !_allProduces[product.productId])
					{
						ajaxSaleDetail(product.id, product.productId);
					}
				}
			}
		}
	}
	if (_isCopy == true)
	{
		_initProcedureMaterials();
	}

	/* 工序材料信息 */
	$("table tbody").on("click", "a[name='btn_procedure']", function()
	{
		// 找到当前选中行对象
		var _productId = $(this).parent().parent().find("input[name='detailList.productId']").val();
		var _productType = $(this).parent().parent().find("input[name='detailList.productType']").val();
		var products = $("#form_order").formToJson();
		if (products && products.detailList)
		{
			for ( var p in products.detailList)
			{
				var product = products.detailList[p];
				var typeofProduct = typeof product;
				if (typeofProduct == 'object' && product.productId == _productId)
				{
					product.master = products;
					product.id = product.id || 0;
					_currentSelectProduct = convertSaleDetailToProduct(product);
					// if(_isCopy == true && !_allProduces[product.productId])
					// {
					// ajaxSaleDetail(product.id, product.productId);
					// }
					break;
				}
			}
		}
		Helper.popup.show('销售工序材料详情', Helper.basePath + '/sale/order/quick_procedure?productType=' + _productType, '1200', '550');
	});

	// AJAX查询销售详情得工序材料
	function ajaxSaleDetail(detailId, productId)
	{
		$.ajax({
			url : Helper.basePath + '/sale/order/ajaxSaleDetail',
			data : {
				'id' : detailId
			},
			async : false,
			type : 'get',
			success : function(data)
			{
				if (data)
				{
					var oldData = JSON.parse(data);
					var product = {};
					product.pack = oldData.pack;
					product.partList = oldData.partList;

					_allProduces[productId] = product;
				}
			}
		});
	}

	function convertSaleDetailToProduct(row)
	{
		var _product = {};
		_product.id = row.productId;
		_product.name = row.productName;
		_product.specifications = row.style;
		_product.unitId = row.unitId;
		_product.sourceQty = row.qty;
		_product.saleProduceQty = row.qty - row.produceedQty;
		_product.spareProduceQty = row.spareQty - row.produceSpareedQty;
		_product.sourceId = row.master.id;
		_product.sourceDetailId = row.id;
		_product.sourceBillType = row.master.billType;
		_product.sourceBillNo = row.master.billNo;
		_product.customerBillNo = row.master.customerBillNo;
		_product.customerId = row.master.customerId;
		_product.customerCode = row.master.customerCode;
		_product.customerName = row.master.customerName;
		_product.customerMaterialCode = row.customerMaterialCode;
		_product.deliveryTime = row.deliveryTime;
		_product.customerRequire = row.custRequire;
		_product.memo = row.memo;
		_product.salePrice = row.price;
		// 判断下produceedQty（已生产数量）是否为0，为0的话money取销售订单最初填写的金额;
		_product.money = 0;
		// if (row.produceedQty == 0)
		// {
		// _product.money = row.money;
		// } else
		// {
		// _product.money = ((row.qty - row.produceedQty) * row.price).toFixed(2);
		// }
		_product.code = row.productCode;
		return _product;
	}
	// 渲染完后计算下
	$("input[name='detailList.qty']").trigger("blur");
});
// 批量修改税收
function batchEditTaxRate()
{
	var taxRateId = $(".batch_taxRate_select").val();
	if (taxRateId == -1)
	{
		shotCutWindow('TAXRATE', true, $(".batch_taxRate_select"), '', "TAXRATE");
	} else
	{
		$("table select[name='detailList.taxRateId']").val(taxRateId);
		if (taxRateId > 0)
		{
			$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
		} else
		{
			$("input[name='detailList.percent']").val(0);
		}
		$("table select[name='detailList.taxRateId']").trigger("change");
	}
}
// 批量修改交货日期
function batchEditDeliveryTime()
{
	$("table input[name='detailList.deliveryTime']").val($(".batch_deliveryTime_input").val());
}
// 默认值
function defaultPercent()
{
	var taxRateId = $("#rateId").val();
	$("select[name='detailList.taxRateId']").val(taxRateId);
	if (taxRateId > 0) // 避免选中自定义或者空选项时js报错
	{
		$("input[name='detailList.percent']").val(Helper.basic.info("TAXRATE", taxRateId).percent);
	} else
	{
		$("input[name='detailList.percent']").val(0);
	}
	$("input[name='detailList.deliveryTime']").val($("#deliveryTime").val());
}
// 改变单个税率
function taxSelectNum()
{
	$("select[name='detailList.taxRateId']").off('change').on('change', function()
	{
		if ($(this).val() == -1)
		{
			shotCutWindow('TAXRATE', true, $(this));
		} else
		{
			var flg = true;
			var taxRateId = $(this).val();
			if (taxRateId > 0)
			{
				$(this).parent().next().children().val(Helper.basic.info("TAXRATE", taxRateId).percent);
			}
			calcTaxRate(this);
		}
	});
}
// 表单提交
function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	$("#btn_save_audit").attr({
		"disabled" : "disabled"
	});

	// 封装工序材料
	var products = $("#form_order").formToJson();
	if (products && products.detailList)
	{
		for ( var p in products.detailList)
		{
			var product = products.detailList[p];
			var typeofProduct = typeof product;
			if (typeofProduct == 'object')
			{
				var procedureMateria = _allProduces[product.productId];
				if (procedureMateria)
				{
					product.pack = procedureMateria.pack;
					product.partList = procedureMateria.partList;
				}
			}
		}
	}
	// 报价订单id
	if (_offerOrderIdList != '')
	{
		products.offerOrderIdList = _offerOrderIdList;
	}

	// 提交到服务器保存
	Helper.request({
		url : Helper.basePath + "/sale/order/save",
		data : products,
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				// if (data.obj.stockProductList != null)
				// {
				// var str = '审核失败,以下产品库存数量不足\n';
				// for (var i = 0; i < data.obj.length; i++)
				// {
				// str = str + '名称：' + data.obj[i].product.name + ' 目前库存数量' +
				// data.obj[i].qty + '\n';
				// }
				// layer.alert(str)
				// }
				location.href = Helper.basePath + '/sale/order/view/' + data.obj.id;
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
// 获取返回产品数组信息
function getCallInfo_productArray(rows)
{
	if (rows.length > 0)
	{
		appendTr("detailList", rows);
		resetSequenceNum();
	}
	sum();
	defaultPercent();
	taxSelectNum();
}
// 获取返回客户信息
function getCallInfo_customer(obj)
{
	// console.log(obj)
	var old_customerId = $("#customerId").val();
	$("#customerName").val(obj.name);
	$("#customerId").val(obj.id);
	if (Helper.isNotEmpty(obj.defaultAddress))
	{
		$("#linkName").val(obj.defaultAddress.userName);
		$("#mobile").val(obj.defaultAddress.mobile);
		$("#deliveryAddress").val(obj.defaultAddress.address);
	}
	$("#deliveryClassId").val(obj.deliveryClassId).trigger("change");
	$("#paymentClassId").val(obj.paymentClassId).trigger("change");
	// $("#paymentClassId").trigger("change").trigger("change");
	if (Helper.isNotEmpty(obj.taxRateId))
	{
		$("#rateId").val(Helper.basic.info('TAXRATE', obj.taxRateId).id);
	}
	$("#rateId").trigger("change");
	$("#currencyTypeText").val(Helper.basic.getEnumText('com.huayin.printmanager.persist.enumerate.CurrencyType', obj.currencyType, 'text'));
	$("#currencyType").val(obj.currencyType);
	if (Helper.isNotEmpty(old_customerId) && obj.id != old_customerId)
	{// 如果改变供应商，则清空table
		// 清空table tr
		$("table tbody tr").remove();
	}
	if (Helper.isNotEmpty(obj.employeeId) && obj.employeeId > 0)// 避免选中自定义或者空选项时js报错
	{
		$("#employeeName").val(Helper.basic.info('EMPLOYEE', obj.employeeId).name);
	} else
	{
		$("#employeeName").val("");
	}
	$("#employeeId").val(obj.employeeId);
	sum();
}

function appendTr(tableId, rows)
{
	// //console.log(rows);
	$.each(rows, function()
	{
		var _THIS = this;
		// //console.log(_THIS);
		var _TR = $("<tr/>");
		var idArray = $("table tbody tr td input[name='detailList.productId']").map(function()
		{
			return this.value
		}).get();
		// //console.log("-----------")
		// //console.log(idArray);
		// //console.log(this.id);
		// 判断是否已存在客户ID
		if (Helper.isNotEmpty(idArray) && idArray.contains("" + this.id))
		{// 如果已存在则跳过本次循环
			return true;// continue;
		}
		$("#" + tableId).find("thead tr th").each(function()
		{
			var name = $(this).attr("name");
			var value = eval("_THIS." + name);
			value = value == undefined ? "" : value;
			switch (name)
			{
			case 'radio':
				_TR.append('<td><input type="radio" name="radio"> </td>');
				break;
			case 'seq':
				_TR.append('<td></td>');
				break;
			case 'operator':
				_TR.append('<td class="td-manage"><input name="detailList.productId" type="hidden" readonly="readonly" value="' + (_THIS.id || '') + '"/><input name="detailList.productType" type="hidden" readonly="readonly" value="' + (_THIS.productClass.productType || '') + '"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
				break;
			case 'code':
				_TR.append('<td style="display: none"><input name="detailList.productCode" class="tab_input" type="text" readonly="readonly" value="' + (value || '') + '"/></td>');
				break;
			case 'name':
				_TR.append('<td><input name="detailList.productName" class="tab_input" readonly="readonly" type="text" value="' + (value || '') + '"/></td>');
				break;
			case 'imgUrl':
				if (_THIS.imgUrl != '')
				{
					_TR.append('<td><img class="pimg" src="' + value + '"/></td>');
				} else
				{
					_TR.append('<td></td>');
				}
				break;
			case 'specifications':
				_TR.append('<td><input name="detailList.style" class="tab_input" readonly="readonly" type="text" value="' + (value || '') + '"/></td>');
				break;
			case 'customerMaterialCode':
				_TR.append('<td><input name="detailList.customerMaterialCode" class="tab_input" readonly="readonly" type="text" value="' + (value || '') + '"/></td>');
				break;
			case 'unitId':
				_TR.append('<td><input name="detailList.unitId" class="tab_input" readonly="readonly" type="hidden" value="' + value + '"/><input name="detailList.unitName" class="tab_input" readonly="readonly" type="text" value="' + Helper.basic.info("UNIT", value).name + '"/></td>');
				break;
			case 'qty':
				_TR.append('<td><input name="detailList.qty" class="tab_input bg_color constraint_negative" type="text" value="' + (value || '0') + '"/></td>');
				break;
			case 'spareQty':
				_TR.append('<td><input name="detailList.spareQty" class="tab_input bg_color constraint_negative" type="text" value="' + (value || "0") + '"/></td>');
				break;
			case 'deliverSpareedQty':
				_TR.append('<td style="display: none"><input name="detailList.deliverSpareedQty" class="tab_input" readonly="readonly" type="text" value="' + (value || "0") + '"/></td>');
				break;
			case 'salePrice':
				if (hasPermission)
				{
					_TR.append('<td><input name="detailList.price" class="tab_input bg_color constraint_decimal" type="text" value="' + (value || "0") + '"/></td>');
				} else
				{
					_TR.append('<td style="display: none"><input name="detailList.price" class="tab_input bg_color constraint_decimal" type="text" value="' + (value || "0") + '"/></td>');
				}
				break;
			case 'money':
				if (hasPermission)
				{
					_TR.append('<td><input name="detailList.money" class="tab_input bg_color constraint_decimal" type="text" value="' + (value || "0") + '"/></td>');
				} else
				{
					_TR.append('<td style="display: none"><input name="detailList.money" class="tab_input bg_color constraint_decimal" type="text" value="' + (value || "0") + '"/></td>');
				}
				break;
			case 'taxRateId':
				var list =$("#phtml").clone(true).html();
				_TR.append('<td>'+list+"</td>");
				break;
			case 'percent':
				_TR.append('<td style="display: none"><input name="detailList.percent" class="tab_input" readonly="readonly" type="text" value="' + (value || 0) + '"/></td>');
				break;
			case 'tax':
				if (hasPermission)
				{
					_TR.append('<td><input name="detailList.tax" class="tab_input" readonly="readonly" type="text" value="' + (value || '') + '"/></td>');
				} else
				{
					_TR.append('<td style="display: none"><input name="detailList.tax" class="tab_input" readonly="readonly" type="text" value="' + (value || '') + '"/></td>');
				}
				break;
			case 'deliveryTime':
				_TR.append('<td><input type="text" readonly="true" class="tab_input bg_color" name="detailList.deliveryTime" onFocus="WdatePicker({lang:\'zh-cn\',minDate: \'%y-%M-%d\' })" value="" /></td>');
				break;
			case 'deliverQty':
				_TR.append('<td style="display: none"><input name="detailList.deliverQty" class="tab_input" readonly="readonly" type="text" value="' + (value || "0") + '"/></td>');
				break;
			case 'noTaxPrice':
				_TR.append('<td style="display: none"><input name="detailList.noTaxPrice" class="tab_input" readonly="readonly" type="text" value="' + (value || '0') + '"/></td>');
				break;
			case 'noTaxMoney':
				_TR.append('<td style="display: none"><input name="detailList.noTaxMoney" class="tab_input" readonly="readonly" type="text" value="' + (value || '0') + '"/></td>');
				break;
			case 'memo':
				_TR.append('<td><input name="detailList.memo" class="tab_input bg_color memo" onmouseover="this.title=this.value" type="text" value="' + (value || '') + '"/></td>');
				break;
			case 'procedureMaterial':
				_TR.append('<td class="td-manage" style="background-color: #fff;"><a title="工序材料详情" href="javascript:void(0)" name="btn_procedure"><i class="delete fa fa-info-circle"></i></a></td>');
				break;
			case 'offerId':
				_TR.append('<td style="display: none"><input name="detailList.offerId" class="tab_input" readonly="readonly" type="text" value=""/></td>');
				break;
			case 'offerNo':
				_TR.append('<td><input name="detailList.offerNo" class="tab_input" readonly="readonly" type="text" value=""/></td>');
				break;
			}
		});
		_TR.appendTo("#" + tableId);
	});
}
// 选中行特效
function selectTr()
{
	$("#detailList").on("click", "tr", function()
	{
		$("#detailList tr.trActive").removeClass("trActive");
		$(this).addClass("trActive");
		$(this).find("input[type='radio']").prop("checked", true);
	});

}
// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").eq(1).html(++index);
	});
}
// 计算加工单价 （输入金额）
function calcPrice(obj)
{
	var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");
	if (qty_dom.val() == 0 || qty_dom.val().trim() == '')
	{
		Helper.message.warn("请先输入数量");
		money_dom.val(0);
		return;
	}
	var price = Number(money_dom.val()).div(qty_dom.val()).toFixed(4);
	price_dom.val(price);
	calcTaxRate(obj);
}
// 计算金额
function calcMoney(obj)
{
	var qty_dom = $(obj).parent().parent().find("input[name='detailList.qty']");
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

	money_dom.val(Number(price_dom.val()).mul(Number(qty_dom.val())).tomoney());// 金额=(单价*数量)

	calcTaxRate(obj);
}
// 计算税额
function calcTaxRate(obj)
{
	// 获取金额对象并格式化
	var money_dom = $(obj).parent().parent().find("input[name='detailList.money']");

	var money_value = Number(money_dom.val()).tomoney();// 金额

	var taxRatePercent = Number($(obj).parent().parent().find("input[name='detailList.percent']").val());// 税率值
	// 不含税金额计算
	var noTaxMoney_dom = $(obj).parent().parent().find("input[name='detailList.noTaxMoney']");
	noTaxMoney_dom.val(Number(money_value).div(Number(1 + (taxRatePercent / 100))).tomoney());// 不含税金额=（金额/(1+税率值/100)）

	// 税额计算
	var tax_dom = $(obj).parent().parent().find("input[name='detailList.tax']");
	tax_dom.val(money_value.subtr(Number(noTaxMoney_dom.val())).tomoney());// 税额=(金额-不含税金额)

	var qty_value = Number($(obj).parent().parent().find("input[name='detailList.qty']").val());// 数量
	// 单价计算
	var price_dom = $(obj).parent().parent().find("input[name='detailList.price']");

	// 不含税单价计算
	var noTaxPrice_dom = $(obj).parent().parent().find("input[name='detailList.noTaxPrice']");
	noTaxPrice_dom.val(Number(price_dom.val()).div(Number(1 + (taxRatePercent / 100))).toFixed(4));// 不含税单价=(单价/(1+税率值/100))

	// 汇总
	sum();
}
// 汇总
function sum()
{
	var sum_qty = 0;
	var sum_tax = 0;
	var sum_noTaxMoney = 0;
	var sum_money = 0;
	$("table tbody tr").each(function()
	{
		sum_qty = Number(sum_qty).add(Number($(this).find("td input[name='detailList.qty']").val()));
		sum_tax = Number(sum_tax).add(Number($(this).find("td input[name='detailList.tax']").val()));
		sum_noTaxMoney = Number(sum_noTaxMoney).add(Number($(this).find("td input[name='detailList.noTaxMoney']").val()));
		sum_money = Number(sum_money).add(Number($(this).find("td input[name='detailList.money']").val()));
	});
	$("#qty").val(sum_qty);
	$("#tax").val(sum_tax.tomoney());
	$("#noTaxMoney").val(sum_noTaxMoney.tomoney());
	$("#money").val(sum_money.tomoney());
	$("#totalMoney").val(sum_money.tomoney());
	$("#noTaxTotalMoney").val(sum_noTaxMoney.tomoney());
	$("#totalTax").val(sum_tax.tomoney());
}

// 保存工序材料信息
function updateProcedure(productId, procedures)
{
	_allProduces[productId] = procedures;
}