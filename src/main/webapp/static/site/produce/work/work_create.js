var productType = null;
var isBook = null;
var isRotary = null;
var curr_material_div = null;
var curr_customer_id = null;
var curr_customer_name = null;
var curr_customer_code = null;
var _sourceDetailIdTrigger = null;// 【选择销售订单】触发
var hasPermission = Helper.basic.hasPermission('produce:work:money');
$(function()
{
	productType = $("select[name='productType']").val();
	isBook = ($("select[name='productType']").val() == 'BOOK') ? true : false;
	isRotary = ($("select[name='productType']").val() == 'ROTARY') ? true : false;
	if (!hasPermission)
	{
		$("th[name='price']").hide();
		$("th[name='money']").hide();
		$("td").has("input[name='productList.price']").hide();
		$("td").has("input[name='productList.money']").hide();
	}
	// 初始化制单日期
	$("#createDate").val(new Date().format('yyyy-MM-dd'));
	// 初始化书刊表格
	if (isBook)
	{
		// 部件table 添加P数列
		$("<th width='60'>P数</th>").insertAfter("#part_template .work_table thead tr th:eq(3)");
		$("<td><input name=\"partList.pageNum\" type=\"text\" class=\"constraint_negative tab_input\" value=\"0\" readonly=\"readonly\"/></td>").insertAfter("#part_template table tbody tr td:eq(3)");

		// 部件table 添加贴数列和每贴正数列
		$("<th width='60'>贴数</th><th width='60'>每贴正数</th>").insertAfter("#part_template .work_table thead tr th:eq(10)");
		$("<td><input name=\"partList.stickersNum\" type=\"text\" class=\"constraint_negative tab_input\" value=\"0\" readonly=\"true\"/></td><td><input name=\"partList.stickersPostedNum\" type=\"text\" class=\"constraint_negative tab_input\" value=\"0\" readonly=\"true\"/></td>").insertAfter("#part_template table tbody tr td:eq(10)");

		// 部件产品table 添加P数列
		$("<th>P数</th>").insertAfter("#part_template .makeup_table thead tr th:eq(2)");
		// 拼版数改为单面P数
		$("#part_template .makeup_table thead tr th:eq(4)").html("单面P数");
		$("#part_template .work_table thead tr th:eq(5)").html("单面P数");
		$("#part_template .work_table thead tr th:eq(15)").html("单贴放损%");

		// 设置书刊翻单补单时为单贴放损%
		$("#partList_div  .work_table thead tr th:eq(15)").html("单贴放损%");

		// 增加单贴放损
		$("<th width='60'>单贴放损</th>").insertAfter("#part_template .work_table thead tr th:eq(15)");
		$("<td><input name=\"partList.stickerlossQty\" type=\"text\" class=\"constraint_negative tab_input bg_color\" value=\"0\" /></td>").insertAfter("#part_template table tbody tr td:eq(15)");

		// 禁止修改放损数
		$("#part_template table tbody tr td:eq(17) input").removeClass("bg_color");
		$("#part_template table tbody tr td:eq(17) input").attr("readonly", "readonly");
	}

	if (Helper.isNotEmpty($("#sourceWorkId").val()))
	{// 翻单或补单， 禁止修改产品类型
		$("#productType").attr("disabled", "disabled");
		$("#billType").attr("disabled", "disabled");
		// 初始化交货日期
		$("input[name='productList.deliveryTime']").each(function()
		{
			$(this).val(new Date().format('yyyy-MM-dd'));
		});
	}
	// 初始格式化材料数量
	$("input[name='materialQty']").each(function()
	{
		$(this).val(Number($(this).val()).toString());
	});
	// 初始化产品生产数量
	sum_product_produceQty();
	// 初始化批量交货日期悬浮窗
	$("#batch_edit_deliveryTime").each(function()
	{
		$(this).powerFloat({
			eventType : "click",
			targetAttr : "src",
			reverseSharp : true,
			container : $(this).siblings(".batch_box_container")
		})
	})
	// 取消
	$("#btn_cancel").click(function()
	{
		closeTabAndJump("生产工单列表");
	});
	// 保存
	$("#btn_save,#btn_save_audit").click(function()
	{
		var form_check_flag = true;
		if ($("#productList_div table tbody tr").length <= 0)
		{
			Helper.message.warn("至少选 择1款产品");
			form_check_flag = false;
		}
		if (form_check_flag)
		{// 校验颜色
			$("input[name='partList.generalColor']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					Helper.message.warn("请填写正反普色");
					$(this).focus().select();
					form_check_flag = false;
					return false;
				}
			});
		}
		if (form_check_flag)
		{// 校验颜色
			$("input[name='partList.spotColor']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					Helper.message.warn("请填写正反专色");
					$(this).focus().select();
					form_check_flag = false;
					return false;
				}
			});
		}
		if (form_check_flag)
		{// 校验销量
			$("#productList_div input[name='productList.saleProduceQty']").each(function()
			{
				if ($(this).val() == '0')
				{
					Helper.message.warn("生产数量必须大于0");
					$(this).focus().select();
					form_check_flag = false;
					return false;
				}
			});
		}

		if (form_check_flag)
		{// 校验部件名
			var partNameArray = $("#partList_div input[name='partList.partName']").map(function()
			{
				return $(this).val();
			}).get();
			$("#partList_div input[name='partList.partName']").each(function()
			{
				if (Helper.isEmpty($(this).val()))
				{
					Helper.message.warn("部件名称不能为空");
					$(this).focus().select();
					form_check_flag = false;
					return false;
				} else
				{
					if (partNameArray.containCount($(this).val()) > 1)
					{
						Helper.message.warn("部件名称重名");
						$(this).select();
						form_check_flag = false;
						return false;
					}
				}
			});
		}

		if (form_check_flag && isRotary)
		{
			// 校验齿轮数，
			$("input[name='partList.gear']").each(function()
			{
				if (0 >= $(this).val())
				{
					Helper.message.warn("齿轮数必须大于0");
					$(this).focus().select();
					form_check_flag = false;
					return false;
				}
			});
		}

		if (form_check_flag && isRotary)
		{
			// 齿间距
			$("input[name='partList.distance']").each(function()
			{
				if (0 == $(this).val())
				{
					Helper.message.warn("请填写齿间距");
					$(this).focus().select();
					form_check_flag = false;
					return false;
				}
			})
		}
		if (form_check_flag)
		{
			$("#isCheck").val($(this).attr("id") == "btn_save_audit" ? "YES" : "NO");
			order_save();
		}
	});

	$("select[name='productType']").change(function()
	{
		if ($("#billType").val() == 'PRODUCE_SUPPLEMENT' || $("#billType").val() == 'PRODUCE_TURNING')
		{
			location.href = Helper.basePath + '/produce/work/create?billType=PRODUCE_SUPPLEMENT&sourceWorkId=' + $("#sourceWorkId").val();
		} else
		{
			location.href = Helper.basePath + '/produce/work/create?productType=' + $(this).val();
		}

	});
  /* 自动分贴 */
	$("#auto_stricker").click(function()
	{
		var items = $("#product_table tbody").find("tr");
		if (items.length > 1)
    {
      return Helper.message.warn("合版不能进行自动分贴");
    }
		if (items.length == 0)
		{
			return Helper.message.warn("请先选择销售订单数据或者选择产品数据");	
		}
		Helper.popup.show('自动分贴', Helper.basePath + '/produce/work/autoStricks', '680', '315');
	});
	/* 选择产品 */
	$("#select_product").click(function()
	{
		// 先选客户
		Helper.popup.show('选择客户信息', Helper.basePath + '/quick/customer_select?multiple=false&productType=' + productType, '900', '490');
	});
	/* 选择销售源单 */
	$("#select_sale_detail").click(function()
	{
		Helper.popup.show('选择销售订单', Helper.basePath + '/sale/order/quick_select_forwork?productType=' + productType, '960', '490');
	});

	/* 机台名称 */
	$(document).on('click', '#machineNameDiv', function()
	{
		$(this).siblings('input:first').attr("id", "machineName");	
		Helper.popup.show('选择机台信息', Helper.basePath + '/quick/machine_select', '860', '520');
	})

	/* 添加部件 */
	$("#add_part").click(function()
	{
		if ($("#productList_div table tbody tr").length <= 0)
		{
			Helper.message.warn("请选择产品！");
			return false;
		}

		// 快速获取销售明细工序材料
		var partNameArray = $("#partList_div input[name='partList.partName']").map(function()
		{
			return $(this).val();
		}).get();
		// 如果是原单触发,部件为空,则查看销售订单明细的工序和材料
		if (Helper.isEmpty(partNameArray) && _sourceDetailIdTrigger === true)
		{
			_reopenAndShow();
		}
		// 默认情况下正常使用
		else
		{
			createPart();
		}

		addOnlyClass_ft()
		_sourceDetailIdTrigger = null;// 处理完毕后还原
		
		// 初始该标签下的所有table表格使其可以列拖动
		allTable_ColDrag($("#partList_div"));
	});

	// 创建part
	function createPart()
	{
		var part_clone = $("#part_template").find(".fold_table").clone(true);
		var part_product_map = getProductListMap();
		var part_num = 0;
		// 添加部件产品信息
		var part_product_table = part_clone.find(".plywood_product table");
		if (part_product_map.size() > 0)
		{
			var pageNum_td = "";
			if (isBook)
			{
				pageNum_td = "<td><input name='productList.pageNum' class='constraint_negative tab_input bg_color' type='text' value='4'/></td>";
			}
			$.each(part_product_map.values(), function(i, obj)
			{
				part_num = part_num.add(Number(obj.produceQty));
				part_product_table.append("<tr><td><i title='删除' class='fa fa-trash-o'></i></td>" 
						+ "<td><input name='productList.productId' type='hidden' value='" + obj.id + "'/>" 
						+ "<input name='productList.productName' onmouseover='this.title=this.value' class='tab_input' readonly='readonly' type='text' value='" + obj.name + "'/></td>" 
						+ "<td><input name='productList.produceQty' class='tab_input' readonly='readonly' type='text' value='" + obj.produceQty + "'/></td>" + pageNum_td
						+ "<td><input name='productList.pieceNum' class='constraint_negative tab_input bg_color' type='text' value='1'/></td></tr>");
			});
		}
		// 设置部件数量
		var _temp_part_multiple = Number(part_clone.find("input[name='partList.multiple']").val());
		part_clone.find("input[name='partList.qty']").val(Number(part_num).mul(_temp_part_multiple));
		// 设置拼版数
		count_part_pieceNum(part_clone);
		// 设置P数
		if (isBook)
		{
			count_part_pageNum(part_clone);
		}
		// 设置印张正数
		count_part_impressionNum(part_clone);
		// 添加部件信息
		$("#partList_div").append(part_clone);
	}
	
	function _reopenAndShow()
	{
		var detailId = null;
		if (_lastRows && _lastRows.length == 1)
		{
			detailId = _lastRows[0].sourceDetailId;
		}

		if (detailId == null)
		{
			createPart();
			return false;
		}

		$.ajax({
			url : Helper.basePath + '/sale/order/ajaxSaleDetail',
			data : {
				'id' : detailId
			},
			async : false,
			type : 'get',
			success : function(data)
			{
				var oldData = JSON.parse(data);
				if (oldData && oldData.partList.length > 0)
				{
					// 部件
					$(oldData.partList).each(
							function(i, part)
							{
								var new_part_clone = $("#part_template").find(".fold_table").clone(true);
								// 添加部件产品信息
								var part_product_map = getProductListMap();
								var part_num = 0;
								var part_product_table = new_part_clone.find(".plywood_product table");
								if (part_product_map.size() > 0)
								{
									var pageNum_td = "";
									if (isBook)
									{
										pageNum_td = "<td><input name='productList.pageNum' class='constraint_negative tab_input bg_color' type='text' value='4'/></td>";
									}
									$.each(part_product_map.values(), function(i, obj)
									{
										part_num = part_num.add(Number(obj.produceQty));
										part_product_table.append("<tr><td><i title='删除' class='fa fa-trash-o'></i></td>" 
												+ "<td><input name='productList.productId' type='hidden' value='" + obj.id + "'/>" 
												+ "<input name='productList.productName' onmouseover='this.title=this.value' class='tab_input' readonly='readonly' type='text' value='" + obj.name + "'/></td>" 
												+ "<td><input name='productList.produceQty' class='tab_input' readonly='readonly' type='text' value='" + obj.produceQty + "'/></td>" + pageNum_td
												+ "<td><input name='productList.pieceNum' class='constraint_negative tab_input bg_color' type='text' value='1'/></td></tr>");
									});
								}
								// 设置部件数量
								var _temp_part_multiple = Number(new_part_clone.find("input[name='partList.multiple']").val());
								new_part_clone.find("input[name='partList.qty']").val(Number(part_num).mul(_temp_part_multiple));
								// 设置拼版数
								count_part_pieceNum(new_part_clone);
								// 设置P数
								if (isBook)
								{
									count_part_pageNum(new_part_clone);
								}
								// 设置印张正数
								count_part_impressionNum(new_part_clone);
								// 材料
								curr_material_div = new_part_clone.find(".material .right_path");
								var newMaterialList = [];
								$(part.materialList).each(function(i, material)
								{
									var newMaterial = {};
									newMaterial.id = material.materialId;
									newMaterial.code = material.materialCode;
									newMaterial.name = material.materialName;
									newMaterial.style = material.style;
									newMaterial.stockUnitId = material.stockUnitId;
									newMaterial.valuationUnitId = material.valuationUnitId;
									newMaterial.weight = material.weight;
									newMaterial.isCustPaper = material.isCustPaper;
									newMaterialList.push(newMaterial)
								});
								append_material(newMaterialList);
								new_part_clone.find("input[name='partList.partName']").val(part.partName); // 部件名称
								new_part_clone.find(".requireText").html(part.memo); // 工艺备注
								_reopenAndShowProcedure(new_part_clone, part.procedureList)// 工艺路线
								// console.log(new_part_clone)
								$("#partList_div").append(new_part_clone);
							});
					// 成品
					$(oldData.pack).each(function(i, pack)
					{
						var $pack = $("#pack_div");
						// 材料
						curr_material_div = $pack.find(".material .right_path");
						if ($.trim(curr_material_div.html()) != "")
							return false;
						var newMaterialList = [];
						$(pack.materialList).each(function(i, material)
						{
							var newMaterial = {};
							newMaterial.id = material.materialId;
							newMaterial.code = material.materialCode;
							newMaterial.name = material.materialName;
							newMaterial.style = material.style;
							newMaterial.stockUnitId = material.stockUnitId;
							newMaterial.valuationUnitId = material.valuationUnitId;
							newMaterial.weight = material.weight;
							newMaterial.isCustPaper = material.isCustPaper;
							newMaterialList.push(newMaterial)
						});
						append_material(newMaterialList);
						$pack.find(".requireText").html(pack.memo); // 工艺备注
						// 工艺路线
						var procedure_finished_map = {};
						$(pack.procedureList).each(function(i, procedure_finished)
						{
							procedure_finished_map[procedure_finished["procedureId"]] = procedure_finished;
						});
						$pack.find(".procedure_finished_pro .inside_item .radio_item ").each(function(i, procedure_finished)
						{
							var _THIS = $(this);
							var procedureId = _THIS.find("input[name='procedure_id']").val();
							var oldProcedure = procedure_finished_map[procedureId];
							if (oldProcedure)
							{
								_THIS.find("input[name='check_procedure']").prop("checked", true);
								_THIS.find(".procedure_item_name").data("memo", oldProcedure.memo);
								if (oldProcedure.isOutSource == "YES")
								{
									_THIS.find("input[name='procedure_isOutSource']").val("YES");
									_THIS.append("<span class='outsource'>(发外)</span>");
								}
							}
						});
					});
				} else
				{
					var new_part_clone = $("#part_template").find(".fold_table").clone(true);
					// 添加部件产品信息
					var part_product_map = getProductListMap();
					var part_num = 0;
					var part_product_table = new_part_clone.find(".plywood_product table");
					if (part_product_map.size() > 0)
					{
						var pageNum_td = "";
						if (isBook)
						{
							pageNum_td = "<td><input name='productList.pageNum' class='constraint_negative tab_input bg_color' type='text' value='4'/></td>";
						}
						$.each(part_product_map.values(), function(i, obj)
						{
							part_num = part_num.add(Number(obj.produceQty));
							part_product_table.append("<tr><td><i title='删除' class='fa fa-trash-o'></i></td>" + "<td><input name='productList.productId' type='hidden' value='" + obj.id + "'/>" 
									+ "<input name='productList.productName' onmouseover='this.title=this.value' class='tab_input' readonly='readonly' type='text' value='" + obj.name + "'/></td>" 
									+ "<td><input name='productList.produceQty' class='tab_input' readonly='readonly' type='text' value='" + obj.produceQty + "'/></td>" + pageNum_td
									+ "<td><input name='productList.pieceNum' class='constraint_negative tab_input bg_color' type='text' value='1'/></td></tr>");
						});
					}
					// 设置部件数量
					var _temp_part_multiple = Number(new_part_clone.find("input[name='partList.multiple']").val());
					new_part_clone.find("input[name='partList.qty']").val(Number(part_num).mul(_temp_part_multiple));
					// 设置拼版数
					count_part_pieceNum(new_part_clone);
					// 设置P数
					if (isBook)
					{
						count_part_pageNum(new_part_clone);
					}
					// 设置印张正数
					count_part_impressionNum(new_part_clone);
					// 添加部件信息
					$("#partList_div").append(new_part_clone);
				}
			}
		})
	}

	// 追加工序
	function _reopenAndShowProcedure(new_part_clone, procedureList)
	{
		$(procedureList).each(
				function(i, procedure)
				{
					var isOutSource_check = "";
					var isOutSourceText = "";
					var procedure_id = procedure.procedureId;
					var procedure_code = procedure.procedureCode;
					var procedure_name = procedure.procedureName;
					var procedure_procedureType = procedure.procedureType;
					var procedure_procedureClassId = procedure.procedureClassId;
					var procedure_isOutSource = procedure.isOutSource;
					var procedure_memo = procedure.memo || "";
					if (procedure.isOutSource == "YES")
					{
						isOutSource_check = "checked";
						isOutSourceText = "<span class='outsource'>(发外)</span>";
					}
					var procedure_line = new_part_clone.find(".procedure-line-class");
					procedure_line.append("<div class='cl pri_item mar_r'><i class='arrow_r fa fa-long-arrow-right'></i>" 
							+ "<span class='show_fw'> <label class='hy_hide radio_item'><input type='checkbox' title='发外' " + isOutSource_check + " />发外</label><span class='removeRequierBt'>X</span><label class='hy_hide radio_item2'>要求</label><input type='hidden' name='procedure_id' value='" + procedure_id + "'/>"
							+ "<input type='hidden' name='procedure_code' value='" + procedure_code + "'/><input type='hidden' name='procedure_name' value='" + procedure_name + "'/>" 
							+ "<input type='hidden' name='procedure_procedureType' value='" + procedure_procedureType + "'/><input type='hidden' name='procedure_procedureClassId' value='" + procedure_procedureClassId + "'/>" 
							+ "<input type='hidden' name='procedure_isOutSource' value='" + procedure_isOutSource + "'/> <span class='ct' data-memo='" + procedure_memo + "'>" + procedure_name + "</span>" + isOutSourceText
							+ "</span> <i title='删除' class='del_ico fa fa-close'></i> </div>");
				});
	}

	function addOnlyClass_ft()
	{
		var listNum = $(".fold_table").length;
		for (var i = 0; i < listNum; i++)
		{
			$(".fold_table").eq(i).attr('onlyClass', 'onlyClass' + i)
		}
	}
	addOnlyClass_ft();
	/* 删除产品 */
	$(document).on("click", "#productList_div .fa-trash-o", function()
	{
		var _temp_productList_productId = $(this).parents("tr").find("input[name='productList.productId']").val();
		$(this).parents("tr").remove();
		// 汇总
		sum_product_produceQty();
		// 重新计算成品材料用量
		count_pack_materialNum_all();
		var part_del_flag = false;
		if (!(getProductList_ProductIdArray().contains(_temp_productList_productId)))
		{// 如果该产品是唯一的产品则遍历所有部件，删除所有包含该产品的部件
			part_del_flag = true;
		}
		$("#partList_div .fold_table").each(function()
		{
			// 包含此产品
			var _arr = $(this).find(".plywood_product table input[name='productList.productId']").map(function()
			{
				return $(this).val()
			}).get();
			if (_arr.contains(_temp_productList_productId))
			{
				if (part_del_flag)
				{// 删除部件
					$(this).remove();
				} else
				{// 更新生产数
					var part_product_map = getProductListMap();
					$(this).find(".plywood_product table input[name='productList.productId']").each(function()
					{
						$(this).parents("tr").find("input[name='productList.produceQty']").val(part_product_map.get($(this).val()).produceQty);
					});
					count_part_pieceNum($(this));
					if (isBook)
					{
						count_part_pageNum($(this));
					}
					count_part_impressionNum($(this));
				}
			}
		});
		// 如果产品为空，则删除所有成品材料信息
		if ($("#productList_div table tbody tr").length <= 0)
		{
			$("#pack_div .inside_box").eq(0).find(".right_path").empty();
		}
	});

	/* 删除部件 */
	$(document).on("click", "#partList_div .work_table .fa-trash-o", function()
	{
		$(this).parents(".fold_table").remove();
	});
	/* 删除部件中的产品 */
	$(document).on("click", "#partList_div .plywood_product .fa-trash-o", function()
	{
		var _part_obj = $(this).parents(".fold_table");
		if ($(this).parents("tbody").find("tr").size() == 1)
		{
			$(this).parents(".fold_table").remove();
		} else
		{
			$(this).parents("tr").remove();
		}
		count_part_num(_part_obj);
		count_part_pieceNum(_part_obj);// 部件拼版数计算
		if (isBook)
		{
			count_part_pageNum(_part_obj);// 部件P数计算
		}
		count_part_impressionNum(_part_obj);// 部件印张正数计算

	});
	
	/*复制部件 */
	$(document).on("click", "#partList_div .work_table .fa-copy", function()
	{
		// 复制当前的整个div[class="fold_table"]标签
		var copy_part_obj = $(this).parents(".fold_table").clone(true);
		
		// 删除复制部件input[name='partList.id']标签
		copy_part_obj.find("input[name='partList.id']").remove();
		
		// 删除复制部件里的所有产品的input[name='productList.id']标签
		var copy_produc_list = copy_part_obj.find(".plywood_product .makeup_table tbody tr");
		$.each(copy_produc_list, function(index, item)
    {
			$(item).find("input[name='productList.id']").remove();
    });
		
		// 删除复制部件里的所有材料的input[name='id']标签
		var copy_material_list = copy_part_obj.find(".w_right .inside_container .material .right_path .material-class");
		$.each(copy_material_list, function(index, item)
		{
			$(item).find("input[name='id']").remove();
		});
		
		// 复制部件选择印刷方式
		var select = $(this).parents(".fold_table").find(".work_table").find("tbody tr td").eq(6).find("select[id='partList.printType']").val();
		var copy_select = copy_part_obj.find(".work_table").find("tbody tr td").eq(6).find("select[id='partList.printType']");
		copy_select.val(select);			// 选择印刷方式
		
		// 追加复制部件
		$(this).parents("#partList_div").append(copy_part_obj);
		
		// 重新为div[class="fold_table"]标签添加onlyclass属性
		addOnlyClass_ft();
		
		// 重置复制表格列拖动效果
		var work_table = copy_part_obj.find(".work_table");							// 查找出复制的work表格
		var makeup_table = copy_part_obj.find(".makeup_table");					// 查找出复制的makeup表格
		var workId = work_table.attr("id");															// 查找出复制的work表格的id属性
		var makeupId = makeup_table.attr("id");													// 查找出复制的makeup表格的id属性
		if ((workId != null || workId != undefined) && workId.indexOf("JColResizer") != -1)						// 当workId是属于JColResizer时删除该id
		{
			work_table.removeAttr("id");
		}
		if ((makeupId != null || makeupId != undefined) && makeupId.indexOf("JColResizer") != -1)			// makeupId是属于JColResizer时删除该id
		{
			makeup_table.removeAttr("id");
		}
		work_table.prev(".JCLRgrips").remove();
		makeup_table.prev(".JCLRgrips").remove();
		table_ColDrag(work_table);									// 重置work_table列拖动效果
		table_ColDrag(makeup_table);								// 重置makeup_table列拖动效果
	});

	/* 部件详情显示/隐藏 */
	$(document).on("click", ".btn_toggle", function()
	{
		var nextTr = $(this).parents('.for_sel').next();
		if (nextTr.css('display') == 'none')
		{
			$(this).removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
			nextTr.show();
		} else if (nextTr.css('display') != 'none')
		{
			$(this).removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
			nextTr.hide();
		}
	})
	// 部件规格键盘输入控制
	$(document).on('keypress', "input[name='materialStyle'],input[name='partList.style']", function()
	{
		return (/\d|\*|[.]{1}/.test(String.fromCharCode(event.keyCode)));
	});
	// 部件规格失去焦点判断
	$(document).on('blur', "input[name='materialStyle'],input[name='partList.style']", function()
	{
		if (Helper.isNotEmpty($(this).val()))
		{
			if (!Helper.validfield.validateFieldMaterialSizeText($(this), "规格错误"))
			{
				return;
			}
			/*
			 * if
			 * (!/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/.test($(this).val())) {
			 * Helper.message.tips("规格错误", this); $(this).focus(); }
			 */
		}
	});
	/* 部分td加overflow */
	$('.fold_table tbody td ').not('.use_relative').css('overflow', 'hidden');
	/* 颜色下拉按钮进入事件 */
	$(document).on("mouseenter", ".use_relative", function()
	{
		$(this).find(".sel_ico").addClass('active');
		$(this).css("overflow","inherit");
		// $(this).children(".td_box").css("width", $(this).parent().css("width"));
		$(this).find(".td_box").show();
	});
	/* 颜色下拉按钮移出事件 */
	$(document).on("mouseleave", ".use_relative", function()
	{
		$(this).find(".sel_ico").removeClass('active');
		$(this).find(".td_box").hide();
	})
	/* 颜色下拉内容单击选中事件 */
	$(document).on("click", ".td_box_item", function()
	{
		$(this).parent().parent().parent().find("input:first").val($(this).text());
		$(this).parent().hide();

		// 印刷专色 颜色限制
		var a = $(this).parent().parent().parent().find("input:first").attr("name")
		if (a != "partList.generalColor")
		{
			count_part_plateSheetNum($(this).parents(".fold_table"));
			return;
		}
		if ($(this).parent().parent().parent().parent().find("select[name='partList.printType']").val() != "SINGLE")
		{
			count_part_plateSheetNum($(this).parents(".fold_table"));
			return;
		}
		var temp_spotColor_div = $(this).parent().parent().parent().next().find(".color_sel_item");
		temp_spotColor_div.empty();
		temp_spotColor_div.append("<div class='sel_ico active'></div>");
		temp_spotColor_div.append("<div class='hy_hide td_box' style='width:160px;'></div>");
		var spotColor_input = $(this).parent().parent().parent().next().find("input[name='partList.spotColor']");
		if (String($(this).text()).split("+")[0] == "0")
		{
			temp_spotColor_div.find(".td_box").append("<div class='td_box_item'>0+1</div><div class='td_box_item'>0+2</div><div class='td_box_item'>0+3</div><div class='td_box_item'>0+4</div><div class='td_box_item_cancel'>1+0</div><div class='td_box_item_cancel'>2+0</div><div class='td_box_item_cancel'>3+0</div><div class='td_box_item_cancel'>4+0</div>");
			if (String(spotColor_input.val()).split("+")[0] != "0")
			{
				spotColor_input.val("0+0");
			}
		} else
		{
			temp_spotColor_div.find(".td_box").append("<div class='td_box_item_cancel'>0+1</div><div class='td_box_item_cancel'>0+2</div><div class='td_box_item_cancel'>0+3</div><div class='td_box_item_cancel'>0+4</div><div class='td_box_item'>1+0</div><div class='td_box_item'>2+0</div><div class='td_box_item'>3+0</div><div class='td_box_item'>4+0</div>");
			if (String(spotColor_input.val()).split("+")[0] == "0")
			{
				spotColor_input.val("0+0");
			}
		}
		count_part_plateSheetNum($(this).parents(".fold_table"));
	})
	/* 规格下拉内容单击选中事件 */
	$(document).on("click", ".td_style_item", function()
	{
		$(this).parent().parent().parent().find("input:first").val($(this).text()).trigger('change');
		$(this).parent().hide();

	})
	$(document).on("change", "input[name='partList.generalColor'],input[name='partList.spotColor']", function()
	{
		var str = $(this).val().split("");
		if (str[2] == undefined)
		{
			Helper.message.warn("请录入正确格式 N+N到N+N");
			$(this).val("0+0");
			return;
		}
		if (str[1] != "+")
		{
			Helper.message.warn("请录入正确格式 N+N到N+N");
			$(this).val("0+0");
			return;
		}
		if (isNaN(str[0]) || isNaN(str[2]))
		{
			Helper.message.warn("请录入正确格式 N+N到N+N");
			$(this).val("0+0");
			return;
		}
		if (str.length > 3)
		{
			Helper.message.warn("请录入正确格式 N+N到N+N");
			$(this).val("0+0");
			return;
		}
		count_part_plateSheetNum($(this).parents(".fold_table"));
	});
	// 印刷方式事件
	$(document).on("change", "select[name='partList.printType']", function()
	{
		// SINGLE("单面"),DOUBLE("正反"),MYSELF("自翻"),BLANK("无");
		var _default_generalColor = "0+0";
		var _default_spotColor = "0+0";
		var temp_generalColor_div = $(this).parents("td").next().children(".color_sel_item");
		var temp_spotColor_div = $(this).parents("td").next().next().children(".color_sel_item");
		temp_generalColor_div.empty();
		temp_spotColor_div.empty();

		// 改变专色普色区间
		if ($(this).val() == "SINGLE")
		{// 单面
			temp_generalColor_div.append("<div class='sel_ico'></div>");
			temp_generalColor_div.append("<div class='hy_hide td_box' style='width:160px;'></div>");
			temp_generalColor_div.find(".td_box").append("<div class='td_box_item'>0+1</div><div class='td_box_item'>0+2</div><div class='td_box_item'>0+3</div><div class='td_box_item'>0+4</div><div class='td_box_item'>1+0</div><div class='td_box_item'>2+0</div><div class='td_box_item'>3+0</div><div class='td_box_item'>4+0</div>");
			temp_spotColor_div.append("<div class='sel_ico'></div>");
			temp_spotColor_div.append("<div class='hy_hide td_box' style='width:160px;'></div>");
			temp_spotColor_div.find(".td_box").append("<div class='td_box_item_cancel'>0+1</div><div class='td_box_item_cancel'>0+2</div><div class='td_box_item_cancel'>0+3</div><div class='td_box_item_cancel'>0+4</div><div class='td_box_item'>1+0</div><div class='td_box_item'>2+0</div><div class='td_box_item'>3+0</div><div class='td_box_item'>4+0</div>");
			_default_generalColor = "1+0";
			_default_spotColor = "0+0";
		} else if ($(this).val() == "BLANK")
		{// 白样
			_default_generalColor = "0+0";
			_default_spotColor = "0+0";
		} else
		{// 正反、自翻
			temp_generalColor_div.append("<div class='sel_ico' ></div>");
			temp_generalColor_div.append("<div class='hy_hide td_box' style='width:200px;'></div>");
			temp_generalColor_div.find(".td_box").append("<div class='td_box_item'>0+0</div><div class='td_box_item'>0+1</div><div class='td_box_item'>0+2</div><div class='td_box_item'>0+3</div><div class='td_box_item'>0+4</div>");
			temp_generalColor_div.find(".td_box").append("<div class='td_box_item'>1+0</div><div class='td_box_item'>1+1</div><div class='td_box_item'>1+2</div><div class='td_box_item'>1+3</div><div class='td_box_item'>1+4</div>");
			temp_generalColor_div.find(".td_box").append("<div class='td_box_item'>2+0</div><div class='td_box_item'>2+1</div><div class='td_box_item'>2+2</div><div class='td_box_item'>2+3</div><div class='td_box_item'>2+4</div>");
			temp_generalColor_div.find(".td_box").append("<div class='td_box_item'>3+0</div><div class='td_box_item'>3+1</div><div class='td_box_item'>3+2</div><div class='td_box_item'>3+3</div><div class='td_box_item'>3+4</div>");
			temp_generalColor_div.find(".td_box").append("<div class='td_box_item'>4+0</div><div class='td_box_item'>4+1</div><div class='td_box_item'>4+2</div><div class='td_box_item'>4+3</div><div class='td_box_item'>4+4</div>");
			temp_spotColor_div.append(temp_generalColor_div.html());
			if ($(this).val() == "DOUBLE")
			{// 正反
				_default_generalColor = "1+1";
				_default_spotColor = "0+0";
			} else if ($(this).val() == "MYSELF" || $(this).val() == "WOSELF" || $(this).val() == "ROSELF")
			{ // 自翻
				_default_generalColor = "1+0";
				_default_spotColor = "0+0";
			}
		}
		$(this).parents("td").next().children("input[name='partList.generalColor']").val(_default_generalColor);
		$(this).parents("td").next().next().children("input[name='partList.spotColor']").val(_default_spotColor);

		// 帖数
		if (isBook)
		{
			count_part_stickersNum($(this).parents(".fold_table"));
		}
		// 印版付数
		count_part_plateSuitNum($(this).parents(".fold_table"));
		// 印版张数
		count_part_plateSheetNum($(this).parents(".fold_table"));
		// 计算印张正数
		count_part_impressionNum($(this).parents(".fold_table"));
	})
	/* 添加材料 */
	$(document).on("click", ".newadd_item_btn", function()
	{
		if ($("#productList_div table tbody tr").length <= 0)
		{
			Helper.message.warn("至少选 择1款产品");
			return false;
		}
		curr_material_div = $(this).parent().next();
		Helper.popup.show('选择材料', Helper.basePath + '/quick/work_material_select?multiple=true', '1200', '550');

	})

	/* 移除材料 */
	$(document).on("click", ".remove_item_btn", function()
	{
		$(this).parents('.material-class').remove();
	})

	/* 工序分类显示 */
	$(document).on("mouseenter", ".classify_item a", function()
	{
		$(this).parent().next('.classify_content').show();
	})
	/* 工序分类隐藏 */
	$(document).on("mouseleave", ".classify_item", function()
	{
		$(this).children('.classify_content').hide();
	})
	// 选择工序事件
	$(document).on(
			"click",
			".procedure .classify_item .classify_content li",
			function()
			{
				var isOutSource_check = "";
				var isOutSourceText = "";
				var procedure_id = $(this).find("input[name='procedureList.procedureId']").val();
				var procedure_code = $(this).find("input[name='procedureList.procedureCode']").val();
				var procedure_name = $(this).find("input[name='procedureList.procedureName']").val();
				var procedure_procedureType = $(this).find("input[name='procedureList.procedureType']").val();
				var procedure_procedureClassId = $(this).find("input[name='procedureList.procedureClassId']").val();
				var procedure_isOutSource = "NO";
				var procedure_produceType = $(this).find("span").attr("_produceType");
				if (procedure_produceType == "EXTERNAL")
				{
					procedure_isOutSource = "YES";
					isOutSource_check = "checked";
					isOutSourceText = "<span class='outsource'>(发外)</span>";
				}
				var nameBool = true
				var procedure_line = $(this).parents(".procedure").next(".procedure-line").find(".procedure-line-class");
				for (var i = 0; i < procedure_line.find(".pri_item").length; i++)
				{
					if (procedure_name == procedure_line.find(".pri_item").eq(i).find("input[name='procedure_name']").val())
					{
						nameBool = false;
					}
				}
				if (nameBool)
				{
					procedure_line.append("<div class='cl pri_item mar_r'><i class='arrow_r fa fa-long-arrow-right'></i> <span class='show_fw'> <label class='hy_hide radio_item'><input type='checkbox' title='发外' " + isOutSource_check 
							+ " />发外</label><span class='removeRequierBt'>X</span><label class='hy_hide radio_item2'>要求</label><input type='hidden' name='procedure_id' value='" + procedure_id + "'/><input type='hidden' name='procedure_code' value='" + procedure_code
							+ "'/><input type='hidden' name='procedure_name' value='" + procedure_name + "'/><input type='hidden' name='procedure_procedureType' value='" + procedure_procedureType 
							+ "'/><input type='hidden' name='procedure_procedureClassId' value='" + procedure_procedureClassId + "'/> <input type='hidden' name='procedure_isOutSource' value='" + procedure_isOutSource + "'/> <span class='ct'>" + procedure_name + "</span>" + isOutSourceText
							+ "</span> <i title='删除' class='del_ico fa fa-close'></i> </div>");
					procedure_line.find(".pri_item").first().find(".fa-long-arrow-right").remove();
				}
			})
	// 清空工序要求
	$(document).on('click', '.removeRequierBt', function()
	{
		var $that = $(this);

		// 删除工序工序要求
		$that.siblings('span.ct').data("memo", "");

		// 找到所有span.ct并设置工艺备注
		var $proceduresMemo = "";
		$that.parents(".procedure-memo-only").find("span.ct").each(function(i, val)
		{
			var $procedureMemo = $(this).data("memo");
			var $procedureName = $.trim($(this).html());
			if ($procedureMemo)
			{
				$proceduresMemo += $procedureName + ": " + $procedureMemo + "；";
			}
		});
		$that.parents('.inside_container').find('.requireText').html($proceduresMemo);
	})
	/* 删除已选工序 */
	$(document).on("click", ".del_ico", function()
	{
		var $that = $(this);

		// 删除工序工序要求
		$that.parents('.pri_item').find('span.ct').data("memo", "");

		// 找到所有span.ct并设置工艺备注
		var $proceduresMemo = "";
		$that.parents(".procedure-memo-only").find("span.ct").each(function(i, val)
		{
			var $procedureMemo = $(this).data("memo");
			var $procedureName = $.trim($(this).html());
			if ($procedureMemo)
			{
				$proceduresMemo += $procedureName + ": " + $procedureMemo + "；";
			}
		});
		$that.parents('.inside_container').find('.requireText').html($proceduresMemo);

		// 删除工序相应要求（必须放到最后,否则工艺配置找不到）
		var procedure_line = $(this).parent().parent();
		$(this).parent().remove();
		procedure_line.find(".pri_item").first().find(".fa-long-arrow-right").remove();// 删除第一个节点的箭头
	})
	/* 部分td加overflow */
	$('.fold_table tbody td ').not('.use_relative').css('overflow', 'hidden');
	// 工序线路发外控制-显示
	$(document).on("mouseenter", ".procedure-line .pri_item", function()
	{
		// $(this).find('.radio_item2').show();
		// $(this).children('.radio_item').show();
	})
	// 工序线路发外控制-隐藏
	$(document).on("mouseleave", ".procedure-line .pri_item", function()
	{
		// $(this).children('.radio_item').hide();
		// $(this).find('.radio_item2').hide();
	})
	// 工序要求编辑
	$(document).on('click', '.radio_item2', function()
	{
		var $that = $(this)
		var procedureMemo = $that.siblings('span.ct').data("memo"); // 选中的工序要求
		layer.prompt({
			title : '请输入您的要求',
			value : procedureMemo,
			formType : 2
		}, function(text, index)
		{
			layer.close(index);
			text = $.trim(text);
			// 设置新的工序要求
			$that.siblings('span.ct').data("memo", text);

			// 找到所有span.ct并设置工艺备注
			var $proceduresMemo = "";

			$that.parents(".procedure-memo-only").find("span.ct").each(function(i, val)
			{
				var $procedureMemo = $(this).data("memo")
				var $procedureName = $.trim($(this).html());
				if ($procedureMemo)
				{
					$proceduresMemo += $procedureName + ': ' + $procedureMemo + "；";
				}
			});
			$that.parents('.inside_container').find('.requireText').html($proceduresMemo);

			if ($that.siblings('.check_procedure[name="check_procedure"]'))
			{
				$that.parent().children("input[type=checkbox]").prop("checked", true)
			}
		});
	})
	// 成品工序线路发外控制-显示
	$(document).on("mouseenter", ".procedure_finished_pro .radio_item", function()
	{
		$(this).children('.isOutsource').show();
		$(this).find('.radio_item2').show();
		$(this).find('.removeRequierBt').show();

	})
	// 成品工序线路发外控制-隐藏
	$(document).on("mouseleave", ".procedure_finished_pro .radio_item", function()
	{
		$(this).children('.isOutsource').hide();
		$(this).find('.radio_item2').hide();
		$(this).find('.removeRequierBt').hide()
	})
	// 工序线路发外控制
	$(document).on("change", ".procedure-line .pri_item .radio_item", function()
	{
		if ($(this).children("input").is(':checked'))
		{
			$(this).parent().append("<span class='outsource'>(发外)</span>");
			$(this).parent().find("input[name='procedure_isOutSource']").val("YES");
		} else
		{
			$(this).parent().find("input[name='procedure_isOutSource']").val("NO");
			$(this).siblings('.outsource').remove();

		}

	})
	// 成品工序线路发外控制
	$(document).on("change", ".procedure_finished_pro .isOutsource", function()
	{
		if ($(this).children("input").is(':checked'))
		{
			$(this).siblings(".check_procedure").prop("checked", true);
			$(this).parent().append("<span class='outsource'>(发外)</span>");
			$(this).siblings("input[name='procedure_isOutSource']").val("YES");
		} else
		{
			$(this).siblings("input[name='procedure_isOutSource']").val("NO");
			$(this).siblings('.outsource').remove();

		}

	})
	// 成品工序线路选择控制
	$(document).on("change", ".procedure_finished_pro .check_procedure", function()
	{
		var $that = $(this);
		if ($(this).parent().children("input").is(':checked'))
		{
			if ($(this).parent().find("input[name='procedure_isOutSource']").val() == "YES")
			{
				$(this).parent().append("<span class='outsource'>(发外)</span>");
				$(this).parent().find("input[name='procedure_isOutSource']").val("YES");
				$(this).siblings(".isOutsource").children("input[type=checkbox]").prop("checked", true);
			}

		} else
		{
			$(this).siblings('.isOutsource').children().prop("checked", false);
			$(this).parent().find("input[name='procedure_isOutSource']").val("NO");
			$(this).siblings('.outsource').remove();
			$(this).siblings('.removeRequierBt').click();
		}

	})
	// 点击工序名触发checkbox事件
	$(".procedure_item_name").click(function()
	{
		$(this).siblings(".check_procedure").trigger("click");
	})
	// 计算
	// 产品销量事件计算
	$(document).on("blur", "input[name='productList.saleProduceQty']", function()
	{
		var _temp_product_id = $(this).parents("tr").find("input[name='productList.productId']").val();
		var _temp_spareProduceQty = $(this).parents("tr").find("input[name='productList.spareProduceQty']").val();
		var _new_produceQty = Number($(this).val()).add(Number(_temp_spareProduceQty));
		$(this).parents("tr").find("input[name='productList.produceQty']").val(_new_produceQty);
		// 汇总生产总量
		sum_product_produceQty();
		// 重新计算成品材料用量
		count_pack_materialNum_all();
		// 重新计算部件数量
		count_part_all_num(_temp_product_id);

		// 计算金额
		var qty_value = Number($(this).val()).trunc();
		$(this).val(qty_value);
		if (qty_value > 0)
		{
			calcMoney(this);
		}

	});
	// 产品备品事件计算
	$(document).on("blur", "input[name='productList.spareProduceQty']", function()
	{
		var _temp_product_id = $(this).parents("tr").find("input[name='productList.productId']").val();
		var _temp_saleProduceQty = $(this).parents("tr").find("input[name='productList.saleProduceQty']").val();
		var _new_produceQty = Number($(this).val()).add(Number(_temp_saleProduceQty));
		$(this).parents("tr").find("input[name='productList.produceQty']").val(_new_produceQty);
		// 汇总生产总量
		sum_product_produceQty();
		// 重新计算成品材料用量
		count_pack_materialNum_all();
		// 重新计算部件数量
		count_part_all_num(_temp_product_id);
	});
	// 部件数量变更事件计算
	$(document).on("blur", "#partList_div input[name='partList.qty']", function()
	{
		count_part_impressionNum($(this).parents(".fold_table"));
	});
	// 翻单补单时触发事件
	if (Helper.isNotEmpty($("#order_billType").val()) && $("#order_productType").val() == "ROTARY")
	{
		$("#partList_div input[name='partList.qty']").blur();
	}
	// 部件倍率事件计算
	$(document).on("blur", "#partList_div input[name='partList.multiple']", function()
	{
		count_part_num($(this).parents(".fold_table"));// 设置部件数量
		count_part_impressionNum($(this).parents(".fold_table"));// 设置印张正数
	});

	// 部件产品拼版数事件计算
	$(document).on("blur", "#partList_div .fold_table .plywood_product table input[name='productList.pieceNum']", function()
	{
		count_part_pieceNum($(this).parents(".fold_table"));// 部件拼版数计算
		count_part_impressionNum($(this).parents(".fold_table"));// (印张正数)

	});
	// 部件产品P数事件计算
	$(document).on("blur", "#partList_div .fold_table .plywood_product table input[name='productList.pageNum']", function()
	{
		count_part_pageNum($(this).parents(".fold_table"));// 部件P数计算
		count_part_impressionNum($(this).parents(".fold_table"));// (印张正数)
	});
	// 部件防损率事件计算
	$(document).on("blur", "#partList_div input[name='partList.lossRate']", function()
	{
		if (isBook)
		{// 书刊 计算单贴放损
			count_part_stickerlossQty($(this).parents(".fold_table"));
		}
		count_part_lossQty($(this).parents(".fold_table"));
	});
	// 部件单贴防损事件计算
	$(document).on("blur", "#partList_div input[name='partList.stickerlossQty']", function()
	{
		count_part_lossRate($(this).parents(".fold_table"));
		count_part_lossQty($(this).parents(".fold_table"));
	});
	$(document).on("blur", "#partList_div input[name='partList.lossQty']", function()
	{
		count_part_lossRate($(this).parents(".fold_table"));
	});
	// 部件材料开数事件计算材料用量
	$(document).on("blur", "#partList_div .material-class input[name='materialSplitQty']", function()
	{
		if ($(this).val() == '0')
		{
			$(this).val('1');
		}
		count_part_materialNum($(this).parents(".material-class"));
	});
	// 成品材料开数事件计算材料用量
	$(document).on("blur", "#pack_div .material-class input[name='materialSplitQty']", function()
	{
		if ($(this).val() == '0')
		{
			$(this).val('1');
		}
		count_pack_materialNum($(this).parents(".material-class"));
	});

	/* 轮转工单绑定事件 */
	// 齿轮数、齿间距触发走距计算事件
	$(document).on("blur", "#partList_div input[name='partList.gear'],input[name='partList.distance']", function()
	{
		count_rotary_walkDistance($(this).parents(".work_table_new"));
		count_part_impressionNum($(this).parents(".fold_table"));// 计算标准用料
	});
	// 走距触发齿间距计算事件
	$(document).on("blur", "#partList_div input[name='partList.walkDistance']", function()
	{
		count_rotary_distance($(this).parents(".work_table_new"));
		count_part_impressionNum($(this).parents(".fold_table")); // 计算标准用料
	});
	/* 去掉成品工序中工序的最后一个箭头 */
	$(".procedure_finished_pro").find(".inside_item:last").find(".fa-long-arrow-right").remove();
	
});
// 批量修改交货日期
function batchEditDeliveryTime()
{
	$("#product_table input[name='productList.deliveryTime']").val($(".batch_deliveryTime_input").val());
}
// 计算公式--产品生产数量汇总
function sum_product_produceQty()
{
	var num = new Number(0);
	$("#productList_div input[name='productList.produceQty']").each(function()
	{
		num = num.add(Number($(this).val()))
	});

	$("#product_sum_produceQty").val(num);
	return num;
}
// 计算公式--部件数量及部件产品生产数量(产品id,修改差值)
function count_part_all_num(productId)
{
	// 更新部件产品生产数量
	var part_product_map = getProductListMap();
	// 遍历所有部件，重新计算包含该每个产品的部件数量
	$("#partList_div .fold_table").each(function()
	{
		$(this).find(".plywood_product table input[name='productList.productId']").each(function()
		{
			$(this).parents("tr").find("input[name='productList.produceQty']").val(part_product_map.get($(this).val()).produceQty);
		});
		// 包含此产品
		var _arr = $(this).find(".plywood_product table input[name='productList.productId']").map(function()
		{
			return $(this).val()
		}).get();
		if (_arr.contains(productId))
		{
			// 设置部件数量
			var _temp_part_multiple = Number($(this).find("input[name='partList.multiple']").val());
			var _temp_part_produceQty_sum = $(this).find(".plywood_product table input[name='productList.produceQty']").map(function()
			{
				return Number($(this).val())
			}).get().sum();
			$(this).find("input[name='partList.qty']").val(_temp_part_produceQty_sum.mul(_temp_part_multiple));
		}
		// 计算最大公因数(印张正数)
		count_part_impressionNum($(this));
	});
}
// 计算部件数量
function count_part_num(partObj)
{
	var _temp_part_produceQty_sum = partObj.find(".plywood_product table input[name='productList.produceQty']").map(function()
	{
		return Number($(this).val())
	}).get().sum();
	var _temp_part_multiple = partObj.find("input[name='partList.multiple']").val();
	partObj.find("input[name='partList.qty']").val(_temp_part_produceQty_sum.mul(Number(_temp_part_multiple)));
}
// 计算印张正数(最大公因数)
// 包装：(生产数量/拼版数)
// 书刊：(生产数量*P数/2/拼版数)
function count_part_impressionNum(partObj)
{

	var _part_multiple = Number(partObj.find("input[name='partList.multiple']").val());// 部件倍率
	var _part_impressionNum = 0;
	var _walkDistance = Number(partObj.find("input[name='partList.walkDistance']").val());// 走距
	var _part_pieceNum = Number(partObj.find("input[name='partList.pieceNum']").val());// 部件拼数
	var _part_produceQty = Number(partObj.find("input[name='partList.qty']").val());// 部件数量

	var _part_product_arr = new Array();
	partObj.find(".plywood_product table tbody tr").each(function()
	{
		_part_product_arr.push($(this).find("input[name='productList.productId']").val());
	});
	if (_part_product_arr.length > 1)
	{// 合版
		partObj.find(".plywood_product table tbody tr").each(function()
		{
			var _produceQty = Number($(this).find("input[name='productList.produceQty']").val()).mul(_part_multiple);
			var _pieceNum = Number($(this).find("input[name='productList.pieceNum']").val());
			var _temp_part_impressionNum = 0;
			if (isBook)
			{// 书刊
				var _pageNum = Number($(this).find("input[name='productList.pageNum']").val() || 1);
				if (partObj.find(".work_table tbody select[name='partList.printType']").val() == "SINGLE")
				{
					_temp_part_impressionNum = Number(_produceQty.mul(_pageNum).div(_pieceNum));
				} else
				{
					_temp_part_impressionNum = Number(_produceQty.mul(_pageNum).div(2).div(_pieceNum));
				}

			} else if (isRotary)
			{
				// 轮转
				// 标准用料（m）：=生产数量*走距/拼版数/1000
				_temp_part_impressionNum = _produceQty.mul(_walkDistance).div(_pieceNum).div(1000).roundFixed(2);
				// _temp_part_impressionNum =
				// ((Number(_temp_part_impressionNum)*100).round()/100).toFixed(2);
			} else
			{// 包装
				_temp_part_impressionNum = Number(_produceQty.div(_pieceNum));
			}
			if (Number(_part_impressionNum).subtr(_temp_part_impressionNum) < 0)
			{
				_part_impressionNum = _temp_part_impressionNum;
			}
		});
	} else
	{// 单版
		if (isBook)
		{// 书刊
			var _part_pageNum = Number(partObj.find("input[name='partList.pageNum']").val() || 1);
			if (partObj.find(".work_table tbody select[name='partList.printType']").val() == "SINGLE")
			{
				_part_impressionNum = Number(_part_produceQty.mul(_part_pageNum).div(_part_pieceNum));
			} else
			{
				_part_impressionNum = Number(_part_produceQty.mul(_part_pageNum).div(2).div(_part_pieceNum));
			}
		} else if (isRotary)
		{// 轮转
			// 标准用料（m）：=生产数量*走距/拼版数/1000
			_part_impressionNum = _part_produceQty.mul(_walkDistance).div(_part_pieceNum).div(1000).roundFixed(2);
			// 
			// _part_impressionNum =
			// ((Number(_part_impressionNum)*100).round()/100).toFixed(2);
		} else
		{// 包装
			_part_impressionNum = Number(_part_produceQty.div(_part_pieceNum));
		}
	}

	partObj.find(".work_table tbody input[name='partList.impressionNum']").val((isRotary ? _part_impressionNum : _part_impressionNum.ceil()));

	if (isBook)
	{
		// 贴数
		count_part_stickersNum(partObj);
		// 每贴正数
		count_part_stickersPostedNum(partObj);
		// 单贴防损数
		count_part_stickerlossQty(partObj);
	}
	// 防损数
	count_part_lossQty(partObj);
}
// 计算部件P数
function count_part_pageNum(partObj)
{
	var _temp_part_pageNum_sum = partObj.find(".plywood_product table input[name='productList.pageNum']").map(function()
	{
		return Number($(this).val())
	}).get().sum();
	partObj.find(".work_table tbody input[name='partList.pageNum']").val(_temp_part_pageNum_sum);
}
// 计算部件拼版数
function count_part_pieceNum(partObj)
{
	var _temp_part_pieceNum_sum = partObj.find(".plywood_product table input[name='productList.pieceNum']").map(function()
	{
		return Number($(this).val())
	}).get().sum();
	partObj.find(".work_table tbody input[name='partList.pieceNum']").val(_temp_part_pieceNum_sum);
}
// 计算放损数
function count_part_lossQty(partObj)
{
	// 印张正数*（1+(放损/100)）
	var _impressionNum = partObj.find(".work_table tbody input[name='partList.impressionNum']").val();
	var _lossRate = partObj.find(".work_table tbody input[name='partList.lossRate']").val();
	var _lossQty = Number(_impressionNum).mul(Number(_lossRate)).div(100).ceil();
	if (isBook)
	{// 书刊
		var _stickerlossQty = partObj.find(".work_table tbody input[name='partList.stickerlossQty']").val();
		var _stickersNum = partObj.find(".work_table tbody input[name='partList.stickersNum']").val();
		// 放损数=单贴放损*贴数
		_lossQty = Number(_stickerlossQty).mul(Number(_stickersNum));
	}
	partObj.find(".work_table tbody input[name='partList.lossQty']").val(_lossQty);
	// 总印张正数
	if (isRotary)
	{
		partObj.find(".work_table tbody input[name='partList.totalImpressionNum']").val((Number(_impressionNum).add(Number(_lossQty))).toFixed(2));
	} else
	{
		partObj.find(".work_table tbody input[name='partList.totalImpressionNum']").val(Number(_impressionNum).add(Number(_lossQty)));
	}
	// 计算材料用量
	count_part_materialNum_all(partObj);
}
// 计算单贴放损
function count_part_stickerlossQty(partObj)
{
	// 单贴放损=每贴正数*单贴放损%/100 stickersPostedNum
	var _stickersPostedNum = partObj.find(".work_table tbody input[name='partList.stickersPostedNum']").val();
	var _lossRate = partObj.find(".work_table tbody input[name='partList.lossRate']").val();
	var _stickerlossQty = Number(_stickersPostedNum).mul(Number(_lossRate)).div(100).ceil();
	partObj.find(".work_table tbody input[name='partList.stickerlossQty']").val(_stickerlossQty);
}
// 计算放损率
function count_part_lossRate(partObj)
{
	var _impressionNum = partObj.find(".work_table tbody input[name='partList.impressionNum']").val();
	var _lossQty = partObj.find(".work_table tbody input[name='partList.lossQty']").val();
	if (_impressionNum == 0)
	{
		partObj.find(".work_table tbody input[name='partList.lossRate']").val(Number(_lossQty).mul(100));
		return;
	}
	var _lossRate = Number(_lossQty).div(_impressionNum).mul(100).toFixed(2);
	if (isBook)
	{
		var _stickerlossQty = partObj.find(".work_table tbody input[name='partList.stickerlossQty']").val();
		var _stickersPostedNum = partObj.find(".work_table tbody input[name='partList.stickersPostedNum']").val();
		// 单贴放损%=单贴放损/每贴正数*100
		_lossRate = Number(_stickerlossQty).div(_stickersPostedNum).mul(100).toFixed(4);
	}
	partObj.find(".work_table tbody input[name='partList.lossRate']").val(_lossRate);
	// 总印张正数
	partObj.find(".work_table tbody input[name='partList.totalImpressionNum']").val(Number(_impressionNum).add(Number(_lossQty)));
	// 计算材料用量
	count_part_materialNum_all(partObj);
}
// 计算印版张数
function count_part_plateSheetNum(partObj)
{
	var _plateSheetNum_obj = partObj.find("input[name='partList.plateSheetNum']");
	var _temp_printType = partObj.find(".work_table select[name='partList.printType']").val();
	var _temp_generalColor = partObj.find(".work_table tbody input[name='partList.generalColor']").val();
	var _temp_spotColor = partObj.find(".work_table tbody input[name='partList.spotColor']").val();
	if (_temp_printType == "DOUBLE")
	{
		// 正反：印版张数=印刷普色+印刷专色
		_plateSheetNum_obj.val(eval(_temp_generalColor + "+" + _temp_spotColor));
	} else
	{// 其它:单面，自翻，天地翻，对滚:印版张数=印刷普色（正面的颜色数如：4+3取4）+印刷专色（正面的专色如：2+1取2）
		var _temp_generalColor_arr = _temp_generalColor.split("+");
		var _temp_spotColor_arr = _temp_spotColor.split("+");
		var _temp_generalColor_num = _temp_generalColor_arr[0] > _temp_generalColor_arr[1] ? _temp_generalColor_arr[0] : _temp_generalColor_arr[1];
		var _temp_spotColor_num = _temp_spotColor_arr[0] > _temp_spotColor_arr[1] ? _temp_spotColor_arr[0] : _temp_spotColor_arr[1];

		_plateSheetNum_obj.val(Number(_temp_generalColor_num).add(Number(_temp_spotColor_num)));
	}
	if (isBook)
	{
		_plateSheetNum_obj.val(Number(partObj.find("input[name='partList.stickersNum']").val()).mul(Number(_plateSheetNum_obj.val())));
	}
}
// 计算贴数(涉及P数与印刷方式的改变)
function count_part_stickersNum(partObj)
{
	var _temp_printType = partObj.find(".work_table select[name='partList.printType']").val();
	var _part_multiple = partObj.find("input[name='partList.multiple']").val();// 拼版数
	var _temp_pageNum = partObj.find("input[name='partList.pageNum']").val();// p数
	var _temp_pieceNum = partObj.find("input[name='partList.pieceNum']").val();// 拼版数

	// P数和拼板数取，最大公因数对应的部件产品里的P数与拼版数
	var _part_impressionNum = 0;
	partObj.find(".plywood_product table tbody tr").each(function()
	{
		var _produceQty = Number($(this).find("input[name='productList.produceQty']").val()).mul(_part_multiple);
		var _pieceNum = Number($(this).find("input[name='productList.pieceNum']").val());
		var _pageNum = Number($(this).find("input[name='productList.pageNum']").val() || 1);
		var _temp_part_impressionNum = Number(_produceQty.mul(_pageNum).div(2).div(_pieceNum));
		if (Number(_part_impressionNum).subtr(_temp_part_impressionNum) < 0)
		{
			_part_impressionNum = _temp_part_impressionNum;
			_temp_pageNum = _pageNum;// p数
			_temp_pieceNum = $(this).find("input[name='productList.pieceNum']").val();// 拼版数
		}

	});
	if (_temp_printType == "BLANK")
	{
		// 正反、单面： P数/(拼版数*2)
		partObj.find("input[name='partList.stickersNum']").val(1);
	} else if (_temp_printType == "DOUBLE" || _temp_printType == "SINGLE")
	{
		// 正反、单面： P数/(拼版数*2)
		partObj.find("input[name='partList.stickersNum']").val(Number(_temp_pageNum).div(Number(_temp_pieceNum).mul(2)).ceil());
	} else
	{// 其它:P数/拼版数
		partObj.find("input[name='partList.stickersNum']").val(Number(_temp_pageNum).div(Number(_temp_pieceNum)).ceil());
	}
	// 每贴正数
	count_part_stickersPostedNum(partObj)
	// 印版付数
	count_part_plateSuitNum(partObj);
	// 印版张数
	count_part_plateSheetNum(partObj);
}
// 计算每贴正数
function count_part_stickersPostedNum(partObj)
{// 印张正数/贴数
	var _temp_printType = partObj.find(".work_table select[name='partList.printType']").val();
	var _temp_impressionNum = partObj.find("input[name='partList.impressionNum']").val();// 印张正数
	var _temp_stickersNum = partObj.find("input[name='partList.stickersNum']").val();// 贴数
	// if (_temp_printType == "BLANK")
	// {

	// partObj.find("input[name='partList.stickersPostedNum']").val(0);
	// } else
	// {
	// }
	// 正反、单面： P数/(拼版数*2)
	partObj.find("input[name='partList.stickersPostedNum']").val(Number(_temp_impressionNum).div(Number(_temp_stickersNum)).ceil());

}
// 印版付数(涉及贴数与印刷方式的改变)
function count_part_plateSuitNum(partObj)
{
	var _temp_printType = partObj.find(".work_table select[name='partList.printType']").val();
	var _temp_stickersNum = partObj.find(".work_table input[name='partList.stickersNum']").val();
	if (_temp_printType == "BLANK")
	{// 白样
		partObj.find("input[name='partList.plateSuitNum']").val("0");
	} else if (_temp_printType == "DOUBLE")
	{// 正反
		if (isBook)
		{// 贴数*2
			partObj.find("input[name='partList.plateSuitNum']").val(Number(_temp_stickersNum).mul(2));
		} else
		{// 2
			partObj.find("input[name='partList.plateSuitNum']").val(2);
		}
	} else
	{
		if (isBook)
		{// 贴数*1
			partObj.find("input[name='partList.plateSuitNum']").val(_temp_stickersNum);
		} else
		{// 1
			partObj.find("input[name='partList.plateSuitNum']").val(1);
		}
	}
}

// 遍历计算部件材料用量（涉及材料开数和部件总印张）
function count_part_materialNum_all(partObj)
{
	var totalImpressionNum = Number(partObj.find("input[name='partList.totalImpressionNum']").val());
	if (isRotary)
	{
		partObj.find(".material-class input[name='materialQty']").each(function()
		{
			$(this).val(totalImpressionNum.toFixed(2));
		})
	} else
	{
		partObj.find(".material-class input[name='materialSplitQty']").each(function()
		{
			if ($(this).parents(".material-class").find("input[name='materialStockUnitName']").val() == "令")
			{
				var stockUnitId = $(this).parents(".material-class").find("input[name='materialStockUnitId']").val();
				var accuracy = Helper.basic.info('UNIT', stockUnitId).accuracy;
				$(this).parents(".material-class").find("input[name='materialQty']").val(totalImpressionNum.div(Number($(this).val())).div(500).roundFixed(accuracy));
			} else
			{
				$(this).parents(".material-class").find("input[name='materialQty']").val(totalImpressionNum.div(Number($(this).val())).ceil());
			}
		})
	}
}
// 计算部件材料用量（涉及材料开数和部件总印张）
function count_part_materialNum(materialObj)
{
	var totalImpressionNum = Number(materialObj.parents(".fold_table").find("input[name='partList.totalImpressionNum']").val());
	var _temp_materialObj_qty = "";
	if (isRotary)
	{
		_temp_materialObj_qty = totalImpressionNum.toFixed(2);
	} else
	{
		if (materialObj.find("input[name='materialStockUnitName']").val() == "令")
		{		
			var stockUnitId = materialObj.find("input[name='materialStockUnitId']").val();
			var accuracy = Helper.basic.info('UNIT', stockUnitId).accuracy;
			_temp_materialObj_qty = totalImpressionNum.div(Number(materialObj.find("input[name='materialSplitQty']").val())).div(500).roundFixed(accuracy);
		} else
		{
			_temp_materialObj_qty = totalImpressionNum.div(Number(materialObj.find("input[name='materialSplitQty']").val())).ceil();
		}
	}
	materialObj.find("input[name='materialQty']").val(_temp_materialObj_qty);
}

// 遍历计算成品材料用量（涉及材料开数和产品总生产数量）
function count_pack_materialNum_all()
{
	var _temp_sum_produceQty = Number($("#product_sum_produceQty").val());

	if (isRotary)
	{
		$("#pack_div .material-class input[name='materialQty']").each(function()
		{
			$(this).val(_temp_sum_produceQty);
		})
	} else
	{
		$("#pack_div .material-class input[name='materialSplitQty']").each(function()
		{
			if($(this).parents(".material-class").find("input[name='materialStockUnitName']").val() == "令")
			{
				var stockUnitId = $(this).parents(".material-class").find("input[name='materialStockUnitId']").val();
				var accuracy = Helper.basic.info('UNIT', stockUnitId).accuracy;
				$(this).parents(".material-class").find("input[name='materialQty']").val(_temp_sum_produceQty.div(Number($(this).val())).div(500).roundFixed(accuracy));
			} else
			{
				$(this).parents(".material-class").find("input[name='materialQty']").val(_temp_sum_produceQty.div(Number($(this).val())).ceil());
			}
		})
	}
}

// 计算成品材料用量（涉及材料开数和产品总生产数量）
function count_pack_materialNum(materialObj)
{
	var _temp_sum_produceQty = Number($("#product_sum_produceQty").val());
	var totalMaterialNum = 0;
	if (isRotary)
	{
		materialObj.find("input[name='materialQty']").val(_temp_sum_produceQty);

	} else
	{
		var _temp_materialObj_qty = "";
		if (materialObj.find("input[name='materialStockUnitName']").val() == "令")
		{
			var stockUnitId = materialObj.find("input[name='materialStockUnitId']").val();
			var accuracy = Helper.basic.info('UNIT', stockUnitId).accuracy;
			_temp_materialObj_qty = _temp_sum_produceQty.div(Number(materialObj.find("input[name='materialSplitQty']").val())).div(500).roundFixed(accuracy);
		} else
		{
			_temp_materialObj_qty = _temp_sum_produceQty.div(Number(materialObj.find("input[name='materialSplitQty']").val())).ceil();
		}
		materialObj.find("input[name='materialQty']").val(_temp_materialObj_qty);
	}
}

// 计算走距（轮转工单）
function count_rotary_walkDistance(materialObj)
{
	// 走距（mm）：=齿轮数*齿间距，齿间距=走距/齿轮数，根据手动输入的走距可以进行反算齿间距，必填字段，可存在小数点，（mm）指的是毫米的单位
	var gear = materialObj.find("input[name='partList.gear']").val(); // 齿轮数
	var distance = materialObj.find("input[name='partList.distance']").val();// 间距
	var walkDistance = Number(gear).mul(Number(distance)).toFixed(2); // 走距
	materialObj.find("input[name='partList.walkDistance']").val(walkDistance);
}
// 计算齿间距
function count_rotary_distance(materialObj)
{
	// 齿间距=走距/齿轮数
	var walkDistance = materialObj.find("input[name='partList.walkDistance']").val();// 走距
	var gear = materialObj.find("input[name='partList.gear']").val(); // 齿轮数
	var distance = Number(walkDistance).div(Number(gear)).toFixed(3); // 齿间距
	materialObj.find("input[name='partList.distance']").val(distance);
}
// ------------------------------计算结束------------------------------------
// 获取产品列表MAP
function getProductList_ProductIdArray()
{
	return $("#productList_div table input[name='productList.productId']").map(function()
	{
		return $(this).val()
	}).get();
}
// 获取产品列表MAP
function getProductListMap()
{
	var part_product_map = new Helper.Map();
	// 获取产品信息并加入到部件
	$("#productList_div table tbody tr").each(function()
	{
		var sel_productId = $(this).find("td input[name='productList.productId']").val();
		var sel_productName = $(this).find("td input[name='productList.productName']").val();
		var sel_product_produceQty = $(this).find("td input[name='productList.produceQty']").val();
		// 包装产品，按产品ID包装成MAP
		if (!part_product_map.hasKey(sel_productId))
		{
			part_product_map.put(sel_productId, {
				"id" : sel_productId,
				"name" : sel_productName,
				"produceQty" : sel_product_produceQty
			});
		} else
		{
			part_product_map.get(sel_productId).produceQty = Number(part_product_map.get(sel_productId).produceQty).add(Number(sel_product_produceQty));
		}
	});
	return part_product_map;
}

// 选择客户返回信息
function getCallInfo_customer(obj)
{
	curr_customer_id = obj.id;
	curr_customer_name = obj.name;
	curr_customer_code = obj.code;
	Helper.popup.show('选择产品信息', Helper.basePath + '/quick/product_select?multiple=true&productType=' + productType + '&customerId=' + obj.id + '&customerName=' + obj.name, '900', '490');
}

// 获取返回产品数组信息
var _lastRows = null;
function getCallInfo_productArray(rows)
{
	if (rows.length > 0)
	{
		_lastRows = rows;
		append_product_tr("product_table", rows);
	}
	// 汇总生产总量
	sum_product_produceQty();
	// 重新计算成品材料用量
	count_pack_materialNum_all();
	var partNameArray = $("#partList_div input[name='partList.partName']").map(function()
	{
		return $(this).val();
	}).get();
	if (Helper.isEmpty(partNameArray))
	{
		_sourceDetailIdTrigger = true;
		$("#add_part").click();
	}
	$("input[name='productList.saleProduceQty']").trigger("blur");
}

/**获取返回机台信息 */
function getCallInfo_machine(rows)
{
	if (rows)
	{
		$("#machineName").val(rows.name);
		$("#machineName").siblings("input.machineId").val(rows.id);
		$("#machineName").attr("id", "");
	}
}

// 获取返回材料数组信息
function getCallInfo_materialArray(rows)
{
	if (rows.length > 0)
	{
		append_material(rows);
	}
}
function append_product_tr(tableId, rows)
{
	$.each(rows, function()
	{
		var _THIS = this;
		var _TR = $("<tr/>");
		if (Helper.isNotEmpty(_THIS.sourceDetailId))
		{// 判断是否已存在源单ID
			var sourceDetailIdArray = $("table tbody tr td input[name='productList.sourceDetailId']").map(function()
			{
				return this.value
			}).get();
			if (Helper.isNotEmpty(sourceDetailIdArray) && sourceDetailIdArray.contains("" + _THIS.sourceDetailId))
			{
				return true;// continue;
			}
		}
		$("#" + tableId).find("thead tr th").each(function()
		{
			var name = $(this).attr("name");
			var value = eval("_THIS." + name);
			value = value == undefined ? "" : value;
			if (Helper.isNotEmpty(_THIS.customerId))
			{
				curr_customer_id = _THIS.customerId;
				curr_customer_code = _THIS.customerCode;
				curr_customer_name = _THIS.customerName;
			}
			switch (name)
			{
			case 'operator':
				_TR.append('<td class="td-manage"><input name="productList.productId" type="hidden" value="' + _THIS.id + '"/><i class="delete fa fa-trash-o"></i></td>');
				break;
			case 'code':
				_TR.append('<td style="display: none"><input name="productList.productCode" class="tab_input" type="text" readonly="readonly" value="' + (value || '') + '"/></td>');
				break;
			case 'productName':
				_TR.append('<td><input name="productList.productName" onmouseover="this.title=this.value" class="tab_input" type="text" readonly="readonly" value="' + _THIS.name + '"/></td>');
				break;
			case 'imgUrl':
				if (_THIS.imgUrl == "")
				{
					_TR.append('<td></td>');
				} else
				{
					_TR.append('<td><img class="pimg" src="' + _THIS.imgUrl + '"/></td>');
				}
				break;
			case 'customerName':
				_TR.append('<td><input name="productList.customerId" class="tab_input" type="hidden" value="' + curr_customer_id 
						+ '"/><input name="productList.customerCode" class="tab_input" type="hidden" value="' + curr_customer_code 
						+ '"/><input name="productList.customerName" class="tab_input" readonly="readonly" onmouseover="this.title=this.value" type="text" value="' + curr_customer_name + '"/></td>');
				break;
			case 'style':
				_TR.append('<td><input name="productList.style" class="tab_input" readonly="readonly" type="text" value="' + (_THIS.specifications || "") + '"/></td>');
				break;
			case 'unit':
				_TR.append('<td><input name="productList.unitId" class="tab_input" type="hidden" value="' + _THIS.unitId + '"/><input class="tab_input" readonly="readonly" type="text" value="' + Helper.basic.info("UNIT", _THIS.unitId).name + '"/></td>');
				break;
			case 'sourceQty':
				_TR.append('<td><input name="productList.sourceQty" class="constraint_negative tab_input" readonly="readonly" type="text" value="' + (_THIS.sourceQty || 0) + '"/></td>');
				break;
			case 'saleProduceQty':
				_TR.append('<td><input name="productList.saleProduceQty" class="constraint_negative tab_input bg_color" type="text" value="' + (_THIS.saleProduceQty || 0) + '"/></td>');
				break;
			case 'spareProduceQty':
				_TR.append('<td><input name="productList.spareProduceQty" class="constraint_negative tab_input bg_color" type="text" value="' + (_THIS.spareProduceQty || 0) + '"/></td>');
				break;
			case 'produceQty':
				_TR.append('<td><input name="productList.produceQty" class="constraint_negative tab_input" readonly="readonly" type="text" value="' + ((_THIS.saleProduceQty || 0) + (_THIS.spareProduceQty || 0)) + '"/></td>');
				break;
			case 'sourceBillNo':
				_TR.append('<td><input name="productList.sourceBillNo" onmouseover="this.title=this.value" class="tab_input" readonly="readonly" type="text" value="' + (_THIS.sourceBillNo || "") 
						+ '"/><input name="productList.sourceBillType" type="hidden" value="' + (_THIS.sourceBillType || "") 
						+ '" /><input name="productList.sourceId" type="hidden" value="' + (_THIS.sourceId || "") + '" /><input name="productList.sourceDetailId" type="hidden" value="' + (_THIS.sourceDetailId || "") + '" /></td>');
				break;
			case 'price':
				if (!hasPermission)
				{
					_TR.append('<td style="display:none"><input name="productList.price" class="constraint_decimal_negative tab_input bg_color"  type="text" value="' + (_THIS.salePrice || 0) + '"/></td>');
				} else
				{
					_TR.append('<td><input name="productList.price" class="constraint_decimal_negative tab_input bg_color"  type="text" value="' + (_THIS.salePrice || 0) + '"/></td>');
				}
				break;
			case 'money':
				if (!hasPermission)
				{
					_TR.append('<td style="display:none"><input name="productList.money" class="constraint_decimal_negative tab_input bg_color"  type="text" value="' + (_THIS.money || 0) + '"/></td>');
				} else
				{
					_TR.append('<td><input name="productList.money" class="constraint_decimal_negative tab_input bg_color"  type="text" value="' + (_THIS.money || 0) + '"/></td>');
				}
				break;
			case 'customerBillNo':
				_TR.append('<td><input name="productList.customerBillNo" class="tab_input" readonly="readonly" type="text" value="' + (_THIS.customerBillNo || "") + '"/></td>');
				break;
			case 'customerMaterialCode':
				_TR.append('<td><input name="productList.customerMaterialCode" class="tab_input" readonly="readonly" type="text" value="' + (_THIS.customerMaterialCode || "") + '" /></td>');
				break;
			case 'deliveryTime':
				var _tempTime = new Date().format('yyyy-MM-dd');
				if (Helper.isNotEmpty(_THIS.deliveryTime))
				{
					_tempTime = new Date(_THIS.deliveryTime).format('yyyy-MM-dd');
				}
				_TR.append('<td><input name="productList.deliveryTime" class="tab_input bg_color" type="text" readonly="true" onFocus="WdatePicker({lang:\'zh-cn\',minDate: \'%y-%M-%d\' })" value="' + _tempTime + '"/></td>');
				break;
			case 'customerRequire':
				_TR.append('<td><input name="productList.customerRequire" class="tab_input bg_color" onmouseover="this.title=this.value" type="text" value="' + (_THIS.customerRequire || "") + '"/></td>');
				break;
			case 'memo':
				_TR.append('<td><input name="productList.memo" class="tab_input bg_color memo" onmouseover="this.title=' + (_THIS.memo || "") + '" type="text" title="' + (_THIS.memo || "") + '" value="' + (_THIS.memo || "") + '"/></td>');
				break;
			}

		});
		_TR.prependTo("#" + tableId);
	});
	
	// 添加一行后，初始化表格列拖动功能
	table_ColDrag($("#" + tableId));
	// 初始化部件表格列拖动功能，解决部件表格溢出的问题
	table_ColDrag($("#partList_div").find(".fold_table").eq(0).find(".for_sel table"));
}

// 添加材料
function append_material(rows)
{
	$.each(rows, function(i, row)
	{
		var clone_Item = $("#material_template").children().clone(true);

		clone_Item.find("input[name='materialId']").val(row.id);
		clone_Item.find("input[name='materialCode']").val(row.code);
		clone_Item.find("input[name='materialName']").val(row.name);
		clone_Item.find("input[name='materialStyle']").val(row.style);
		clone_Item.find("input[name='materialStockUnitId']").val(row.stockUnitId);
		clone_Item.find("input[name='materialValuationUnitId']").val(row.valuationUnitId);
		clone_Item.find("input[name='materialStockUnitName']").val(Helper.basic.info('UNIT', row.stockUnitId).name);
		clone_Item.find("input[name='materialWeight']").val(row.weight);
		if (row.isCustPaper && row.isCustPaper == "YES")
		{
			clone_Item.find("input[name='isCustPaper']").prop("checked", true);
		}
		curr_material_div.append(clone_Item);
		$(clone_Item).find("input[name='materialStyle']").trigger('change')
	});

	// 计算材料用量
	if (curr_material_div.parents("#partList_div").size() > 0)
	{// 部件材料
		curr_material_div.find(".material-class").each(function()
		{
			count_part_materialNum($(this));
		});
	} else
	{// 成品材料
		curr_material_div.find(".material-class").each(function()
		{
			count_pack_materialNum($(this));
		});
	}

}
//自动分贴后回调方法
function createPartFromAuto(row)
{
	// 选择分贴后清空工单上部件
	$("#partList_div").empty();
	$.each(row,function(index,item)
	{
		var part_clone = $("#part_template").find(".fold_table").clone(true);
		var part_product_map = getProductListMap();
		var part_num = 0;
		// 填充分贴的部件信息
		var part_table = part_clone.find(".work_table");
		part_table.find("input[name='partList.partName']").val(item.partName); // 部件名称
		//part_table.find("input[name='partList.pageNum']").val(item.pageNum); // P数
		//part_table.find("input[name='partList.pieceNum']").val(item.pieceNum); // 单面P数
		part_table.find("select[name='partList.printType']").val(item.printType);// 印刷方式
		
		// 添加部件产品信息
		var part_product_table = part_clone.find(".plywood_product table");
		if (part_product_map.size() > 0)
		{
			var pageNum_td = "";
			if (isBook)
			{
				pageNum_td = "<td><input name='productList.pageNum' class='constraint_negative tab_input bg_color' type='text' value='"+ item.pageNum +"'/></td>";
			}
			$.each(part_product_map.values(), function(i, obj)
			{
				part_num = part_num.add(Number(obj.produceQty));
				part_product_table.append("<tr><td><i title='删除' class='fa fa-trash-o'></i></td>" 
						+ "<td><input name='productList.productId' type='hidden' value='" + obj.id + "'/>" 
						+ "<input name='productList.productName' onmouseover='this.title=this.value' class='tab_input' readonly='readonly' type='text' value='" + obj.name + "'/></td>" 
						+ "<td><input name='productList.produceQty' class='tab_input' readonly='readonly' type='text' value='" + obj.produceQty + "'/></td>" + pageNum_td
						+ "<td><input name='productList.pieceNum' class='constraint_negative tab_input bg_color' type='text' value='"+ item.pieceNum +"'/></td></tr>");
			});
		}
		// 设置部件数量
		var _temp_part_multiple = Number(part_clone.find("input[name='partList.multiple']").val());
		part_clone.find("input[name='partList.qty']").val(Number(part_num).mul(_temp_part_multiple));
		// 设置拼版数
		count_part_pieceNum(part_clone);
		// 设置P数
		if (isBook)
		{
			count_part_pageNum(part_clone);
		}
		// 设置印张正数
		count_part_impressionNum(part_clone);
		// 添加部件信息
		$("#partList_div").append(part_clone);
	
	})
}

function order_save()
{
	var isSave = false;// 是否弹出空规格提示
	var isEqual = false;// 是否有不同的单位
	$("input[name='materialStyle']").each(function()
	{
		if (Helper.isEmpty($(this).parents("#material_template")) && Helper.isEmpty($(this).val()))
		{
			var tr_dom = $(this).parent().parent().parent();
			var materialValuationUnitId = tr_dom.find("input[name='materialValuationUnitId']").val();
			var materialStockUnitId = tr_dom.find("input[name='materialStockUnitId']").val();
			if (materialValuationUnitId != materialStockUnitId)
			{
				isEqual = true;
				return;
			}
			isSave = true;
		}
	});
	if (isEqual)
	{
		Helper.message.warn("有需要单位换算的材料，请输入规格进行换算");
		return;
	}
	if (isSave)
	{
		Helper.message.confirm('存在空的材料规格，是否保存', function(index)
		{
			form_submit();
		})
	} else
	{
		form_submit();
	}
}
function form_submit()
{

	var supplementCount = 0;
	var turningCount = 0;
	var product_list = [];
	var part_list = [];
	var pack = {
		"materialList" : [],
		"procedureList" : []
	};
	// 封装产品列表信息
	$("#productList_div tbody tr").each(function(index, item)
	{
		product_list[index] = {};
		product_list[index].productId = $(item).find("input[name='productList.productId']").val();
		product_list[index].productCode = $(item).find("input[name='productList.productCode']").val();
		product_list[index].productName = $(item).find("input[name='productList.productName']").val();
		product_list[index].customerId = $(item).find("input[name='productList.customerId']").val();
		product_list[index].customerCode = $(item).find("input[name='productList.customerCode']").val();
		product_list[index].customerName = $(item).find("input[name='productList.customerName']").val();
		product_list[index].customerBillNo = $(item).find("input[name='productList.customerBillNo']").val();
		product_list[index].customerMaterialCode = $(item).find("input[name='productList.customerMaterialCode']").val();
		product_list[index].style = $(item).find("input[name='productList.style']").val();
		product_list[index].unitId = $(item).find("input[name='productList.unitId']").val();
		product_list[index].sourceQty = $(item).find("input[name='productList.sourceQty']").val();
		product_list[index].saleProduceQty = $(item).find("input[name='productList.saleProduceQty']").val();
		product_list[index].spareProduceQty = $(item).find("input[name='productList.spareProduceQty']").val();
		product_list[index].produceQty = $(item).find("input[name='productList.produceQty']").val();
		product_list[index].deliveryTime = $(item).find("input[name='productList.deliveryTime']").val();
		product_list[index].customerRequire = $(item).find("input[name='productList.customerRequire']").val();
		product_list[index].sourceId = $(item).find("input[name='productList.sourceId']").val();
		product_list[index].sourceDetailId = $(item).find("input[name='productList.sourceDetailId']").val();
		product_list[index].sourceBillNo = $(item).find("input[name='productList.sourceBillNo']").val();
		product_list[index].sourceBillType = $(item).find("input[name='productList.sourceBillType']").val() || null;
		product_list[index].memo = $(item).find("input[name='productList.memo']").val();
		product_list[index].price = $(item).find("input[name='productList.price']").val();
		product_list[index].money = $(item).find("input[name='productList.money']").val();
		// cuidi

	});
	// 封装部件列表信息
	$("#partList_div .fold_table").each(function(index, item)
	{
		var _part_table_tr = $(item).find("table tbody tr");
		part_list[index] = {};
		// 组装部件-信息
		part_list[index].partName = _part_table_tr.find("input[name='partList.partName']").val();
		part_list[index].qty = _part_table_tr.find("input[name='partList.qty']").val();
		part_list[index].multiple = _part_table_tr.find("input[name='partList.multiple']").val();
		part_list[index].pieceNum = _part_table_tr.find("input[name='partList.pieceNum']").val();
		part_list[index].pageNum = _part_table_tr.find("input[name='partList.pageNum']").val() || 0;
		part_list[index].style = _part_table_tr.find("input[name='partList.style']").val();
		part_list[index].printType = _part_table_tr.find("select[name='partList.printType']").val();
		part_list[index].generalColor = _part_table_tr.find("input[name='partList.generalColor']").val();
		part_list[index].spotColor = _part_table_tr.find("input[name='partList.spotColor']").val();
		if (isRotary)
		{
			part_list[index].gear = _part_table_tr.find("input[name='partList.gear']").val();
			part_list[index].distance = _part_table_tr.find("input[name='partList.distance']").val();
			part_list[index].walkDistance = _part_table_tr.find("input[name='partList.walkDistance']").val();
			// 轮转工单的特殊处理
			part_list[index].materialNum = _part_table_tr.find("input[name='partList.impressionNum']").val();
			part_list[index].totalMaterialNum = _part_table_tr.find("input[name='partList.totalImpressionNum']").val();
		} else
		{
			part_list[index].impressionNum = _part_table_tr.find("input[name='partList.impressionNum']").val();
			part_list[index].totalImpressionNum = _part_table_tr.find("input[name='partList.totalImpressionNum']").val();
		}
		part_list[index].stickersNum = _part_table_tr.find("input[name='partList.stickersNum']").val() || 0;
		part_list[index].stickersPostedNum = _part_table_tr.find("input[name='partList.stickersPostedNum']").val() || 0;
		part_list[index].plateSuitNum = _part_table_tr.find("input[name='partList.plateSuitNum']").val();
		part_list[index].plateSheetNum = _part_table_tr.find("input[name='partList.plateSheetNum']").val();
		part_list[index].lossRate = _part_table_tr.find("input[name='partList.lossRate']").val();
		part_list[index].stickerlossQty = _part_table_tr.find("input[name='partList.stickerlossQty']").val();
		part_list[index].lossQty = _part_table_tr.find("input[name='partList.lossQty']").val();
		part_list[index].memo = $(item).find('.requireText').html();
		part_list[index].machineName = $(item).find("input[name='partList.machineName']").val();
		part_list[index].machineId = $(item).find("input[name='partList.machineId']").val();
		// 组装部件-产品信息
		part_list[index].productList = [];
		$(item).find(".plywood_product tbody tr").each(function(product_index, product_item)
		{
			part_list[index].productList[product_index] = {};
			part_list[index].productList[product_index].productId = $(product_item).find("input[name='productList.productId']").val();
			part_list[index].productList[product_index].productName = $(product_item).find("input[name='productList.productName']").val();
			part_list[index].productList[product_index].produceQty = $(product_item).find("input[name='productList.produceQty']").val();
			part_list[index].productList[product_index].pieceNum = $(product_item).find("input[name='productList.pieceNum']").val();
			part_list[index].productList[product_index].pageNum = $(product_item).find("input[name='productList.pageNum']").val() || 0;
		})
		// 组装部件-材料信息
		part_list[index].materialList = [];
		$(item).find(".material-class").each(function(material_index, material_item)
		{

			part_list[index].materialList[material_index] = {};
			part_list[index].materialList[material_index].materialId = $(material_item).find("input[name='materialId']").val();
			part_list[index].materialList[material_index].materialCode = $(material_item).find("input[name='materialCode']").val();
			part_list[index].materialList[material_index].materialName = $(material_item).find("input[name='materialName']").val();
			part_list[index].materialList[material_index].style = $(material_item).find("input[name='materialStyle']").val();
			part_list[index].materialList[material_index].weight = $(material_item).find("input[name='materialWeight']").val();
			part_list[index].materialList[material_index].stockUnitId = $(material_item).find("input[name='materialStockUnitId']").val();
			part_list[index].materialList[material_index].valuationUnitId = $(material_item).find("input[name='materialValuationUnitId']").val();
			part_list[index].materialList[material_index].splitQty = $(material_item).find("input[name='materialSplitQty']").val();
			part_list[index].materialList[material_index].qty = $(material_item).find("input[name='materialQty']").val();

			if ($(material_item).find("input[name='isCustPaper']").is(':checked'))
			{
				part_list[index].materialList[material_index].isCustPaper = "YES";
			} else
			{
				part_list[index].materialList[material_index].isCustPaper = "NO";
			}
		})
		// 组装部件-工序信息
		part_list[index].procedureList = [];
		$(item).find(".procedure-line-class .pri_item").each(function(procedure_index, procedure_item)
		{
			part_list[index].procedureList[procedure_index] = {};
			part_list[index].procedureList[procedure_index].procedureId = $(procedure_item).find("input[name='procedure_id']").val();
			part_list[index].procedureList[procedure_index].procedureCode = $(procedure_item).find("input[name='procedure_code']").val();
			part_list[index].procedureList[procedure_index].procedureName = $(procedure_item).find("input[name='procedure_name']").val();
			part_list[index].procedureList[procedure_index].procedureType = $(procedure_item).find("input[name='procedure_procedureType']").val();
			part_list[index].procedureList[procedure_index].procedureClassId = $(procedure_item).find("input[name='procedure_procedureClassId']").val();
			part_list[index].procedureList[procedure_index].isOutSource = $(procedure_item).find("input[name='procedure_isOutSource']").val();
			var part_procedure_outputQty = 0;
			if ($(procedure_item).find("input[name='procedure_procedureType']").val() == "BEFORE")
			{// 印前工序，取印版张数
				part_procedure_outputQty = part_list[index].plateSheetNum;
			} else if ($(procedure_item).find("input[name='procedure_procedureType']").val() == "PRINT")
			{// 印刷工序，取总印张数(如果是轮转工单，取总用料)
				part_procedure_outputQty = isRotary ? part_list[index].totalMaterialNum : part_list[index].totalImpressionNum;
			} else if ($(procedure_item).find("input[name='procedure_procedureType']").val() == "AFTER")
			{// 印后工序，取印张正数(如果是轮转工单，取标准用料)
				part_procedure_outputQty = isRotary ? part_list[index].materialNum : part_list[index].impressionNum;
			}
			part_list[index].procedureList[procedure_index].outputQty = part_procedure_outputQty;
			part_list[index].procedureList[procedure_index].inputQty = part_procedure_outputQty;
			part_list[index].procedureList[procedure_index].sort = (procedure_index + 1);

			part_list[index].procedureList[procedure_index].memo = $(procedure_item).parents('.procedure-line').find('span.ct').eq(procedure_index).data("memo");

		})
	});
	// 组装成品信息
	pack.memo = $("#pack_memo").html();
	// 组装成品材料信息
	$("#pack_div .material-class").each(function(index, item)
	{
		pack.materialList[index] = {};
		pack.materialList[index].materialId = $(item).find("input[name='materialId']").val();
		pack.materialList[index].materialCode = $(item).find("input[name='materialCode']").val();
		pack.materialList[index].materialName = $(item).find("input[name='materialName']").val();
		pack.materialList[index].style = $(item).find("input[name='materialStyle']").val();
		pack.materialList[index].weight = $(item).find("input[name='materialWeight']").val();
		pack.materialList[index].stockUnitId = $(item).find("input[name='materialStockUnitId']").val();
		pack.materialList[index].valuationUnitId = $(item).find("input[name='materialValuationUnitId']").val();
		pack.materialList[index].splitQty = $(item).find("input[name='materialSplitQty']").val();
		pack.materialList[index].qty = $(item).find("input[name='materialQty']").val();

		if ($(item).find("input[name='isCustPaper']").is(':checked'))
		{
			pack.materialList[index].isCustPaper = "YES";
		} else
		{
			pack.materialList[index].isCustPaper = "NO";
		}
	});
	// 组装成品工序信息
	$("#pack_div .radio_item input[name='check_procedure']:checked").each(function(index, item)
	{
		pack.procedureList[index] = {};
		pack.procedureList[index].isOutSource = $(item).parent().find("input[name='procedure_isOutSource']").val();
		pack.procedureList[index].procedureId = $(item).parent().find("input[name='procedure_id']").val();
		pack.procedureList[index].procedureCode = $(item).parent().find("input[name='procedure_code']").val();
		pack.procedureList[index].procedureName = $(item).parent().find("input[name='procedure_name']").val();
		pack.procedureList[index].procedureType = $(item).parent().find("input[name='procedure_procedureType']").val();
		pack.procedureList[index].procedureClassId = $(item).parent().find("input[name='procedure_procedureClassId']").val();
		pack.procedureList[index].memo = $(item).siblings('span.ct').data("memo");
		// 产出数=产品生产数量总和，投入数=产出数
		pack.procedureList[index].inputQty = $("#product_sum_produceQty").val();
		pack.procedureList[index].outputQty = $("#product_sum_produceQty").val();

	});
	var request_work_json = {
		"billType" : $("#billType").val(),
		"billNo" : $("#billNo").val(),
		"sourceWorkId" : Helper.isEmpty($("#sourceWorkId").val()) ? null : $("#sourceWorkId").val(),
		"productType" : productType,
		"isOutSource" : $("#isOutSource").prop("checked") ? 'YES' : 'NO',
		"isEmergency" : $("#isEmergency").prop("checked") ? 'YES' : 'NO',
		"supplementCount" : supplementCount,
		"turningCount" : turningCount,
		"memo" : $("#memo").val(),
		"productList" : product_list,
		"partList" : part_list,
		"pack" : pack,
		"isCheck" : $("#isCheck").val()
	};
	// return false;
	$("#btn_save").attr({
		"disabled" : "disabled"
	});

	// return false;
	Helper.request({
		url : Helper.basePath + "/produce/work/save",
		data : request_work_json,
		success : function(data)
		{

			if (data.success)
			{
				Helper.message.suc('已保存!');
				location.href = Helper.basePath + '/produce/work/view/' + data.obj.id;
			} else
			{
				Helper.message.warn('保存失败!' + data.message);
				$("#btn_save").removeAttr("disabled");
			}
		},
		error : function(data)
		{
		}
	});
}