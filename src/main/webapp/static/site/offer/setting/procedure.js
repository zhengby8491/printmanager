/**
 * Author:		   think
 * Create:	 	   2017年10月30日 上午9:50:49
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */

$(function()
{
	init(); // 初始化

	/**
	 * 
	 * 初始化功能、按钮事件
	 * 
	 * @since 1.0, 2017年10月26日 上午11:40:02, think
	 */
	function init()
	{
		initBtn(); // 初始化初始化功能、按钮事件
		initProcedure(); // 初始化工序
	}

	/**
	 * 
	 * 初始化初始化功能、按钮事件
	 * @since 1.0, 2017年10月17日 下午6:53:43, think
	 */
	function initBtn()
	{
		// 保存
		$("#btnSave").click(function()
		{
			formSubmit();
		});
		// 取消
		$("#btnCancel").click(function()
		{
			closeTabAndJump("")
		});
		// 批量删除
		$("#batchDel").on("click", function()
		{
			$.each($("#bootTable input[type='checkbox']"), function(index, obj)
			{
				var $this = $(this);
				if ($this.is(":checked"))
				{
					$this.parents("tr").remove();
				}
				;
			})
		})
	}

	/**
	 * 
	 * 初始化工序
	 * @since 1.0, 2017年10月26日 上午11:43:43, think
	 */
	function initProcedure()
	{
		// 表格初始化
		$("#bootTable").bootstrapTable({
			method : "post",
			contentType : 'application/json', // 设置请求头信息
			dataType : "local",
			queryParamsType : "",

			// resizable : true, //是否启用列拖动
			showColumns : true, // 是否显示所有的列
			minimumCountColumns : 2, // 最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表

			showExport : true,// 是否显示导出按钮
			// exportDataType: 'all',
			exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],

			cookie : true,// 是否启用COOKIE
			cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
			cookieIdTable : "procedure",// 必须制定唯一的表格cookieID

			uniqueId : "id",// 定义列表唯一键
			onColumnSwitch : function(field, checked)
			{	
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			}
		});

		// 表格工具栏
		if ($(".glyphicon-th").next().html() == '')
		{
			$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
			$(".glyphicon-export").after("<span class='glyphicon_font'>导出</span>");
		}

		// 单价/元
		$("input[name='list.price']").on("change", function()
		{
			// 展示公式
			_changeFormula($(this));
		});

		// 控制每张最低单价的输入
		$("select[name='list.procedureUnit']").on("change", function()
		{
			if ($(this).val() == "UNIT2" || $(this).val() == "UNIT3")
			{
				$(this).parents("tr").find("input[name='list.lowestPrice']").removeAttr("readonly").addClass("bg_color constraint_decimal");
			} else
			{
				$(this).parents("tr").find("input[name='list.lowestPrice']").attr("readonly", "readonly").removeClass("bg_color constraint_decimal");

			}
			$(this).parents("tr").find("input[name='list.lowestPrice']").val("");
			// 展示公式
			_changeFormula($(this));
		});

		// 普通和自定义公式
		$("select[name='list.offerProcedureFormulaType']").on("change", function()
		{
			var $this = $(this);
			var $val = $this.val();
			var $formula = $this.parents("tr").find("input[name='list.formulaText']");
			var $customFormula = $this.parents("tr").find("input[name='list.customFormulaText']");
			if ($val == "NORMAL")
			{
				$formula.removeClass("bg_color").addClass("bg_color");
				$customFormula.attr("readonly", "readonly").removeClass("bg_color");
			} else
			{
				$formula.removeClass("bg_color");
				$customFormula.removeAttr("readonly").addClass("bg_color");
			}
		});

		// 自定义公式
		$("input[name='list.customFormulaText']").on("click", function()
		{
			// 设置当前选中的tr
			if ($(this).hasClass("bg_color"))
			{
				$currentCustom = $(this);
				Helper.popup.show('自定义公式', Helper.basePath + '/offer/setting/procedureFormula?type=' + $("#offerType").val(), '1100', '500');
			} else
			{
				Helper.message.view($(this).val())
			}
		});

		// 单位选中UNIT2单位特殊处理文字的显示
		$("table tr").on("blur", "td input[name='list.lowestPrice']", function()
		{
			var $TR = $(this).parents("tr");
			var unit = $TR.find("select[name='list.procedureUnit']").val();
			if (unit == "UNIT2")
			{
				var price = $TR.find("input[name='list.price']").val();
				var lowestPrice = $TR.find("input[name='list.lowestPrice']").val();
				var str = "印张正数*上机长/1000*上机宽/1000*单价（如果印张正数*（如果上机长/1000*上机宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是上机长/1000*上机宽/1000*单价）<起步价，那么起步价，否则是（如果上机长/1000*上机宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是上机长/1000*上机宽/1000*单价）*印张正数）";
				str = str.replace(new RegExp("单1价", "g"), lowestPrice).replace(new RegExp("单价", "g"), price);
				$TR.find("input[name='list.formulaText']").val(str);
				// 隐藏公式
				$TR.find("input[name='list.formula']").val("印张正数*上机长/1000*上机宽/1000*单价");
			} else if (unit == "UNIT3")
			{
				var price = $TR.find("input[name='list.price']").val();
				var lowestPrice = $TR.find("input[name='list.lowestPrice']").val();
				var str = "印张正数*指定长/1000*指定宽/1000*单价（如果印张正数*（如果指定长/1000*指定宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是指定长/1000*指定宽/1000*单价）<起步价，那么起步价，否则是（如果指定长/1000*指定宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是指定长/1000*指定宽/1000*单价）*印张正数）";
				str = str.replace(new RegExp("单1价", "g"), lowestPrice).replace(new RegExp("单价", "g"), price);
				$TR.find("input[name='list.formulaText']").val(str);
				// 隐藏公式
				$TR.find("input[name='list.formula']").val("印张正数*指定长/1000*指定宽/1000*单价");
			}
		})
	}

	/**
	 * 
	 * 公式改变（根据单价 和 单位）
	 * @since 1.0, 2017年10月26日 上午11:43:43, think
	 */
	function _changeFormula($this)
	{
		var $TR = $this.parents("tr");
		var unit = $TR.find("select[name='list.procedureUnit']").val();
		var price = $TR.find("input[name='list.price']").val();
		var lowestPrice = $TR.find("input[name='list.lowestPrice']").val();
		switch (unit)
		{
		case "UNIT1":
			// 展示公式
			var str = "印张正数*单价（如果印张正数*单价<起步价，那么就是起步价，否则是印张正数*单价）";
			$TR.find("input[name='list.formulaText']").val(str.replace(new RegExp("单价", "g"), price));
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印张正数*单价");
			break;
		case "UNIT2":
			// 展示公式
			var str = "印张正数*上机长/1000*上机宽/1000*单价（如果印张正数*（如果上机长/1000*上机宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是上机长/1000*上机宽/1000*单价）<起步价，那么起步价，否则是（如果上机长/1000*上机宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是上机长/1000*上机宽/1000*单价）*印张正数）";
			str = str.replace(new RegExp("单1价", "g"), lowestPrice).replace(new RegExp("单价", "g"), price);
			$TR.find("input[name='list.formulaText']").val(str);
			$TR.find("input[name='list.lowestPrice']").val("0.00");
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印张正数*上机长/1000*上机宽/1000*单价");
			break;
		case "UNIT3":
			// 展示公式
			var str = "印张正数*指定长/1000*指定宽/1000*单价（如果印张正数*（如果指定长/1000*指定宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是指定长/1000*指定宽/1000*单价）<起步价，那么起步价，否则是（如果指定长/1000*指定宽/1000*单价<每张最低单1价，那么每张最低单1价，否则是指定长/1000*指定宽/1000*单价）*印张正数）";
			str = str.replace(new RegExp("单1价", "g"), lowestPrice).replace(new RegExp("单价", "g"), price);
			$TR.find("input[name='list.formulaText']").val(str);
			$TR.find("input[name='list.lowestPrice']").val("0.00");
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印张正数*指定长/1000*指定宽/1000*单价");
			break;
		case "UNIT4":
			// 展示公式
			var str = "印刷数量*单价（如果印刷数量*单价<起步价，那么就是起步价，否则是印刷数量*单价）";
			$TR.find("input[name='list.formulaText']").val(str.replace(new RegExp("单价", "g"), price));
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印刷数量*单价");
			break;
		case "UNIT5":
			// 展示公式
			var str = "印张正数*总贴数*单价（如果印张正数*总贴数*单价<起步价，那么就是起步价，否则是印张正数量*总贴数*单价）/*封面和内页相加的贴数*/";
			$TR.find("input[name='list.formulaText']").val(str.replace(new RegExp("单价", "g"), price));
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印张正数*总贴数*单价");
			break;
		case "UNIT6":
			// 展示公式
			var str = "印张正数*P数*单价（如果印张正数*P数*单价<起步价，那么起步价，否则就是印张正数*P数*单价）";
			$TR.find("input[name='list.formulaText']").val(str.replace(new RegExp("单价", "g"), price));
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印张正数*P数*单价");
			break;
		case "UNIT7":
			// 展示公式
			var str = "印刷数量*单价（如果印刷数量*单价<起步价，那么就是起步价，否则是印刷数量*单价）/*这里的印刷数量是书刊报价和联单报价里的本数的数量*/";
			$TR.find("input[name='list.formulaText']").val(str.replace(new RegExp("单价", "g"), price));
			// 隐藏公式
			$TR.find("input[name='list.formula']").val("印刷数量*单价");
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * 表单提交
	 * @since 1.0, 2017年10月30日 上午15:49:45, think
	 */
	function formSubmit()
	{
		/**
		 * 1. 验证表单数据
		 * 2. 构造提交数据
		 * 3. 提交到服务器
		 */

		// 1. 验证表单数据
		if (!formSubmitValidate())
		{
			return false;
		}

		// 2. 构造提交数据
		var postData = [];
		$("#bootTable tbody tr").each(function(index, item)
		{
			postData[index] = {};
			postData[index].id = $(item).find("input[name='list.id']").val();
			postData[index].offerType = $("#offerType").val();
			postData[index].procedureType = $(item).find("select[name='list.procedureType']").val();
			postData[index].procedureClass = $(item).find("input[name='list.procedureClass']").val();
			postData[index].name = $(item).find("input[name='list.name']").val();
			postData[index].price = $(item).find("input[name='list.price']").val();
			postData[index].procedureUnit = $(item).find("select[name='list.procedureUnit']").val();
			postData[index].lowestPrice = $(item).find("input[name='list.lowestPrice']").val();
			postData[index].startPrice = $(item).find("input[name='list.startPrice']").val();
			postData[index].offerProcedureFormulaType = $(item).find("select[name='list.offerProcedureFormulaType']").val();
			postData[index].formulaText = $(item).find("input[name='list.formulaText']").val();
			postData[index].formula = $(item).find("input[name='list.formula']").val();
			postData[index].formulaParam = $(item).find("input[name='list.formulaParam']").val();
			postData[index].customFormulaText = $(item).find("input[name='list.customFormulaText']").val();
			postData[index].customFormula = $(item).find("input[name='list.customFormula']").val();
			postData[index].customFormulaParam = $(item).find("input[name='list.customFormulaParam']").val();
			postData[index].switchStatus = $(item).find("select[name='list.switchStatus']").val();
			postData[index].sort = index;
		});

		// 3. 提交到服务器
		$("#btnSave").attr({
			"disabled" : "disabled"
		});
		// 当全部工序删除时，特殊处理
		if (postData.length == 0)
		{
			postData[0] = {};
			postData[0].offerType = $("#offerType").val();
			postData[0].isDeleteAll = true;
		}
		_postData(postData);
	}

	/**
	 * 
	 * 提交到服务器
	 * @since 1.0, 2017年10月17日 下午7:18:29, think
	 */
	function _postData(postData)
	{
		Helper.request({
			url : Helper.basePath + "/offer/setting/saveProcedure",
			data : postData,
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc("已保存!");
					location.reload();
				} else
				{
					Helper.message.warn('保存失败! ' + data.message);
				}
				$("#btnSave").removeAttr("disabled");
			},
			error : function(data)
			{
				Helper.message.warn('保存失败! ' + data.message);
				$("#btnSave").removeAttr("disabled");
			}
		});
	}

	/**
	 * 
	 * 表单验证
	 * @since 1.0, 2017年10月30日 上午15:49:45, think
	 */
	function formSubmitValidate()
	{
		try
		{
			// 工序分类
			$("#bootTable input[name='list.procedureClass']").each(function()
			{
				if (!Helper.validfield.validateFieldText($(this), "请填写工序分类"))
				{
					throw "";
				}
			});
			// 工序名称
			$("#bootTable input[name='list.name']").each(function()
			{
				if (!Helper.validfield.validateFieldText($(this), "请填写工序名称"))
				{
					throw "";
				}
			});
			// 单价/元
			$("#bootTable input[name='list.price']").each(function()
			{
				if (!Helper.validfield.validateFieldText($(this), "请填写单价/元"))
				{
					throw "";
				}
			});
			// 每张最低单价/元
			$("#bootTable input[name='list.lowestPrice']").each(function()
			{
				var $unit = $(this).parents("tr").find("select[name='list.procedureUnit']").val();
				// 只有单位选择了，元/m2或者元/指定m2时，才可以编辑输入，否则为只读
				if ($unit == "UNIT2" || $unit == "UNIT3")
				{
					if (!Helper.validfield.validateFieldText($(this), "请填写每张最低单价/元"))
					{
						throw "";
					}
				}
			});
			// 起步价/元
			$("#bootTable input[name='list.startPrice']").each(function()
			{
				if (!Helper.validfield.validateFieldText($(this), "请填写起步价/元"))
				{
					throw "";
				}
			});
			// 自定义公式
			$("#bootTable input[name='list.customFormulaText']").each(function()
			{
				var $this = $(this);
				var $offerProcedureFormulaType = $this.parents("tr").find("select[name='list.offerProcedureFormulaType']");
				if ($offerProcedureFormulaType.val() == "CUSTOM")
				{
					if (!Helper.validfield.validateFieldText($this, "请填写自定义公式"))
					{
						throw "";
					}
				}
			});

		} catch (e)
		{
			return false;
		}

		return true;
	}

	/**
	 * 响应处理（默认写上即可）
	 * @since 1.0, 2017年10月30日 上午9:16:22, think
	 */
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}

	/**
	 * 查询条件
	 * @since 1.0, 2017年10月30日 上午15:16:22, think
	 */
	function queryParams(params)
	{
		params['offerType'] = $("#offerType").val();
		return params;
	}
	
	// 表格列拖动效果
	bootstrapTable_ColDrag($("#bootTable"));
});

/**
 * 
 * 工序新增
 * @since 1.0, 2017年10月30日 上午9:55:23, think
 */
function procedureCreate(choose)
{
	var _TR = $("#template").find("tbody tr").clone(true);
	
	if(choose)
	{
		_TR.find("select[name='list.procedureType']").val(choose.procedureType);
		_TR.find("input[name='list.procedureClass']").val(choose.procedureClassName);
		_TR.find("input[name='list.name']").val(choose.name);
	}

	$("#bootTable tbody").append(_TR);
	// 当第一次增加工序时需删除提示语行“没有记录”
	if ($("#bootTable tbody").find("tr").hasClass("no-records-found"))
	{
		$("#bootTable tbody").find("tr.no-records-found").remove();
	}
	
	// 在工序新增后重置表格列拖动效果
	bootstrapTable_ColDrag($("#bootTable"));
};

/**
 * 同步工序
 * @since 1.0, 2017年12月25日 下午1:34:53, think
 */
function procedureSyn()
{
	Helper.popup.show('选择工序', Helper.basePath + '/quick/procedure_select_filter?multiple=true&procedureTypeArray=AFTER,FINISHED', '900', '490');
}

/**
 * 工序删除
 * @param obj
 * @since 1.0, 2017年10月30日 下午5:29:27, think
 */
function procedureDel(obj)
{
	if (obj != "" && obj != null)
	{
		// 删除行
		obj.parents("tr").remove();
	}
};

/**
 * 工序复制
 * @param obj
 * @since 1.0, 2017年10月30日 下午5:33:48, think
 */
function procedureCopy(obj)
{
	var _TR = obj.parent().parent().clone(true);
	// 去除编号和id
	_TR.find("td").first().html("");
	_TR.find("input[name='list.id']").val(null);

	$("#bootTable tbody").append(_TR);
};

/**
 * 编辑自定义公式
 * @since 1.0, 2017年10月31日 下午3:01:24, think
 */
var $currentCustom;
function procedureCustom(formulaText, formula)
{
	if ($currentCustom)
	{
		$currentCustom.parents("tr").find("input[name='list.customFormulaText']").val(formulaText);
		$customFormula = $currentCustom.parents("tr").find("input[name='list.customFormula']").val(formula);
	}
}

/**
 * 获取自定义公式值
 * @since 1.0, 2017年10月31日 下午3:19:29, think
 */
function getCustom()
{
	var ret = {
		formulaText : $currentCustom.parents("tr").find("input[name='list.customFormulaText']").val(),
		formula : $currentCustom.parents("tr").find("input[name='list.customFormula']").val()
	};

	return ret;
}

/**
 * 获取工序信息
 * @param obj
 * @since 1.0, 2017年12月25日 下午1:51:53, think
 */
function getCallInfo_procedureArray(objs)
{
	$.each(objs, function(i, o){
		procedureCreate(o);
	});
}
