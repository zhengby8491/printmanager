/**
 * Author:       THINK
 * Create:       2017年11月3日 上午10:51:43
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
(function($)
{
	var LIST = []; // 用于保存内部核价单阶梯数据报表
	var CHOOSENLIST = [];
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
})(Zepto);

$(function()
{
	init(); // 初始化

	/**
	 * 初始化功能、按钮事件
	 * @since 1.0, 2017年11月03日 上午10:51:43, think
	 */
	function init()
	{
		initBtn();
	}

	/**
	 * 初始化按钮功能
	 * @since 1.0, 2017年11月3日 上午11:07:41, zhengby
	 */
	function initBtn()
	{
		// ================ 画册书刊特殊处理================
		$(".btn_add_insertP").click(function()
		{
			if ($(this).val() == "添加插页")
			{
				$(this).val("删除插页")
				$(".offer_page_div").eq(2).show();
				$(".offer_color_div").eq(2).show();
				$(".offer_material_div").eq(2).show();
				$(".insert_tr").show();
			} else
			{
				$(this).val("添加插页");
				$(".offer_page_div").eq(2).hide();
				$(".offer_color_div").eq(5).children(".doubleColor").hide();
				$(".offer_color_div").eq(4).find("select[name='offerPartList.offerPrintStyleType']").val("SINGLE");
				$(".offer_material_div").eq(2).hide();
				$(".insert_tr").hide();
			}
		});

		// ================ 报价保存================
		// 计算报价
		$("#btn_cal").on("click", function()
		{
			// 验证数据
			if (_validateQuote())
			{
				// 计算报价
				_quote();
			}
		});

		// 选中克重
		$("#quote_form").on("change","select[name='offerPartList.paperWeight']",function(){
			var $this = $(this);
			var $paperPrice = $this.find("option:checked").attr("tonPrice");
			$(this).next().val($paperPrice);
		});
		
		// 成品尺寸自定义监听事件
		$("#spec").on("change", function()
		{
			if ($(this).val() == "CUSTOM")
			{
				$(".hiddenSpan").show();
			} else
			{
				$(".hiddenSpan").hide();
			}
		});

		// 单面和双面印刷
		$("select[name='offerPartList.offerPrintStyleType']").on("change", function()
		{
			var $doubleColor = $(this).parents("div.offer_color_div").next().find(".doubleColor");
			var $marginLeft = "margin-left: " + ($("#offerPrintStyleType").width() + 10) + "px;";
			if ($(this).val() == "DOUBLE" || $(this).val() == "HEADTAIL" || $(this).val() == "CHAOS")
			{
				$doubleColor.show();
				$doubleColor.find("span").eq(0).attr("style", $marginLeft);
			} else
			{
				$(this).parents("div.offer_color_div").next().find(".doubleColor").hide();
			}

			// 自翻印刷，反面不能选择
			if ($(this).val() == "CHAOS")
			{
				$(this).parents("div.offer_color_div").next().find(".doubleColor select").attr("disabled", true);
			} else
			{
				$(this).parents("div.offer_color_div").next().find(".doubleColor select").removeAttr("disabled");
			}
			
			// 当选择单面印刷，反面印刷的下拉值设成无色+零专
			if ($(this).val() == "SINGLE")
			{
				$(this).parents("div.offer_color_div").next().find("select[name='offerPartList.offerPrintColorType2']").val("NONE");
				$(this).parents("div.offer_color_div").next().find("select[name='offerPartList.offerSpotColorType2']").val("NOSPOT");
			}
		});

		// 自翻印刷，反面不能选择，并且需要同步正面选择得颜色(普色)
		$("select[name='offerPartList.offerPrintColorType']").on("change", function()
		{
			var $this = $(this);
			var $type = $this.parents("div.offer_color_div").find("select[name='offerPartList.offerPrintStyleType']").val();
			if ($type == "CHAOS")
			{
				var $val = $this.val();
				$(this).parents("div.offer_color_div").next().find(".doubleColor select[name='offerPartList.offerPrintColorType2']").val($val).trigger("change");
			}
		});

		// 自翻印刷，反面不能选择，并且需要同步正面选择得颜色(专色)
		$("select[name='offerPartList.offerSpotColorType']").on("change", function()
		{
			var $this = $(this);
			var $type = $this.parents("div.offer_color_div").find("select[name='offerPartList.offerPrintStyleType']").val();

			if ($type == "CHAOS")
			{
				var $val = $this.val();
				$(this).parents("div.offer_color_div").next().find(".doubleColor select[name='offerPartList.offerSpotColorType2']").val($val).trigger("change");
			}
		});
		// 自定义页数填写框的显示
		$("select[name='offerPartList.pages']").on("change", function()
		{
			var $this = $(this);
			if ($this.find("option:checked").val() == "-1")
			{
				$this.parents("div.offer_page_div").find("span.customInp").show();
			} else
			{
				$this.parents("div.offer_page_div").find("span.customInp").hide();
			}
		})
		
	}

	/**
	 * 保存
	 * @since 1.0, 2017年11月7日 下午5:16:06, zhengby
	 */
	function formSubmit()
	{
		var postData = _genPostData();
		// 找到选中的机台Id
    if (CHOOSENLIST && CHOOSENLIST.length > 0)
    {
      postData.chooseedMachineList = CHOOSENLIST;
    }
		HYWX.requestByObj({
			url : HYWX.basePath + "/wx/offer/save",
			data : postData,
			async : true,// 默认异步请求
			success : function(data)
			{
				if (data.obj && data.success)
				{
					$.closeModal();
					HYWX.message.alert("已保存！");
				} else
				{
					HYWX.message.alert('保存失败：' + data.message);
				}
			},
			error : function(data)
			{
				HYWX.message.alert("请求错误");
			},
			beforeSend : function()
			{
				HYWX.message.loading();
			},
			complete : function()
			{
				HYWX.message.hideLoading();
			}
		});
	}

	/**
	 * 计算报价
	 * @since 1.0, 2017年11月6日 下午5:36:33, think
	 */
	function _quote(machineIdList)
	{
		var postData = _genPostData();
		if (machineIdList)
		{
			postData.chooseedMachineList = machineIdList;
      CHOOSENLIST = machineIdList;
    }

		HYWX.requestByObj({
			url : HYWX.basePath + "/wx/offer/quote",
			data : postData,
			async : true,// 默认异步请求
			success : function(data)
			{
				if (data.obj && data.success)
				{
					// 填充对外报价单（隐藏域）
					_quoteOut(data);
					// 填充对内报价单（隐藏域）
					_quoteInner(data);
					// 点击报价时先跳到选择机台的页面
					if (!machineIdList)
					{
						_selectMachine(data);
						_onSelectMachine();
					}
					// 在选择机台点击确定后显示报价单
					if (machineIdList)
					{
						_showOrder(data);
					}
				} else
				{
					HYWX.message.alert('保存失败：' + data.message);
				}
			},
			error : function(data)
			{
				HYWX.message.alert("请求错误");
			},
			beforeSend : function()
			{
				HYWX.message.loading();
			},
			complete : function()
			{
				HYWX.message.hideLoading();
			}
		});
	}
	
	/**
	 * 选择机台
	 * @since 1.0, 2018年04月10日 下午2:31:33, zhengby
	 */
	function _selectMachine(data)
	{
		// 构造结果
		var interText = doT.template($("#selectMachine").text());
		var obj = data.obj.offerMachineList;
		// 计算出最优的机台并展示在页面
		$.modal({
			title : "请选择机台",
			text : interText(obj),
			extraClass : "selectmachine",
			buttons : [ {
				text : '关闭',
				onClick : function()
				{
				}
			}, {
				// 源码sm.min.js 1299
				text : '确定',
				onClick : function(a, b)
				{
					var machineIdList = [];
					$.each($("table.info-table input[type='checkbox']"), function(){
						if ($(this).prop("checked"))
						{
							var machineId = $(this).parents("tr").find("input[name='machineId']").val();
							var boxType = $(this).parents("tr").find("input[name='boxType']").val();
							machineIdList.push(boxType + machineId);
						}
					});
					_quote(machineIdList);
				}
			} ]
		});
	}
	
	/**
	 * 选择机台时的绑定事件
	 * @since 1.0, 2018年04月10日 下午17:36:21, zhengby
	 */
	function _onSelectMachine()
	{
		$("table.info-table").on("click","td", function(){
			var $this = $(this);
			$.each($("table.info-table").find("input[name='boxType']"), function(index,item)
			{
				if ($(item).val() == $this.parent().find("input[name='boxType']").val())
				{
					$(item).parent().parent().find("input[type='checkbox']").prop("checked", false);
					$(item).parents("tr").removeClass("choosenTr");
				}
			});
			$this.parent().find("input[type='checkbox']").prop("checked", true);
			$this.parent().addClass("choosenTr");
		});
	}
	
	/**
	 * 显示报价单
	 * @since 1.0, 2017年11月9日 下午5:36:33, think
	 */
	function _showOrder(data)
	{
		// 构造结果
		var interText = doT.template($("#offerResult").text());
		var obj = _genOrderResult(data);
		// 计算出最优的机台并展示在页面
		$.modal({
			title : $("#boxType").val(),
			text : interText(obj),
			extraClass : "offer_result",
			buttons : [ {
				text : '关闭',
				onClick : function()
				{
				}
			}, {
				// 源码sm.min.js 1299
				text : '保存报价单',
				onClick : function(a, b)
				{
					$("#dy_form").show();

					// 验证
					if (!_validateSave())
					{
						return false;
					}
					// 提交
					formSubmit();
				}
			} ]
		});

		// 初始化日期插件
		$("#deliveryDate").calendar({
			value : new Date()
		});
	}
	
	/**
	 * 构造报价单结果
	 * @since 1.0, 2017年11月9日 下午5:36:33, think
	 */
	function _genOrderResult(data)
	{
		var obj = {};
		
		// 成品尺寸
		obj.spec = data.obj.specification;
		// 多部件列表
		var offerPartList = data.obj.offerPartList;
		var $len = offerPartList.length;
		for (var i = 0; i < $len; i++)
		{
			var offerPart = offerPartList[i];
			// 成品数量
			obj.amount = offerPart.amount;
			// 成品单价
			obj.price = offerPart.taxPrice;
			// 印刷纸张
			obj.printName = offerPart.printName;
			// 印刷颜色
			obj.printColor = offerPart.printColor;
			// 默认第一条
			break;
		}

		// 费用
		var $len = data.obj.offerMachineOrderList ? data.obj.offerMachineOrderList.length : 0;
		for (var i = 0; i < $len; i++)
		{
			var machineOrder = data.obj.offerMachineOrderList[i];
			// 纸张费
			obj.paperFee = machineOrder.paperFee;
			// 印刷费
			obj.printFee = machineOrder.printFee;
			// 工序费
			obj.procedureFee = machineOrder.procedureFee;
			// 其他费
			obj.ohterFee = machineOrder.ohterFee;
			// 运费
			obj.freightFee = machineOrder.freightFee;
			// 成本金额：纸张费+印刷费+工序费+其他费+运费
			obj.costMoney = machineOrder.costMoney;
			// 总费用 ： 含税金额
			obj.taxFee = machineOrder.taxFee;
			break;
		}

		// 机台
		var $len = data.obj.offerMachineList ? data.obj.offerMachineList.length : 0;
		// 如果全部机台不匹配，则需要提示
		if ($len == 0)
		{
			Helper.message.alert("找不到合适的机台");
			return false;
		}

		// 报价时间
		obj.createDateTime = data.obj.createDateTimeStr;
		// 成品名称
		obj.productName = $("#boxType").val();
		// 交货时间
		obj.deliveryDateStr = data.obj.deliveryDateStr;

		return obj;
	}

	/**
	 * 构造postdata
	 * @since 1.0, 2017年11月9日 下午5:36:33, think
	 */
	function _genPostData()
	{
		// 先要移植disabled元素，否则不能正常获取
		$("select[name='offerPartList.offerPrintStyleType']").each(function(idx, item)
		{
			if ($(this).val() == "CHAOS")
			{
				$(this).parents("div.offer_color_div").next().find(".doubleColor select").removeAttr("disabled");
			}
		});

		var qf = $("#quote_form").formToJson();
console.log(qf)
		// 先要移植disabled元素，否则不能正常获取
		$("select[name='offerPartList.offerPrintStyleType']").each(function(idx, item)
		{
			if ($(this).val() == "CHAOS")
			{
				$(this).parents("div.offer_color_div").next().find(".doubleColor select").attr("disabled", true);
			}
		});

		var df = {};
		if ($("#dy_form").length > 0)
		{
			df.createDateTime = $("#createDateTime").val();
			df.productName = $("#productName").val();
			df.customerName = $("#customerName").val();
			df.linkName = $("#linkName").val();
			df.phone = $("#phone").val();
			df.linkAddress = $("#linkAddress").val();
			df.deliveryDate = $("#deliveryDate").val();
		}
		console.log($("#machineId").val())
		var postData = $.extend(true, qf, df);
		postData.boxType = $("#boxType").val();
		postData.machineId = $("#machineId").val();
		postData.ladderCol = 5;
		postData.ladderSpeed = 1000;
		postData.offerType = $("#offerType").val();
		
		// 成品尺寸计算（默认是彩盒）
		if ($("#spec").length > 0)
		{
			var $specText = $("#spec").find("option:checked").text();
			if ($specText.indexOf("正") != -1)
			{
				// 纸张类型 - 正度
				postData.paperType = 'ARE_DEGREES_PAPER';
			} else if ($specText.indexOf("大") != -1)
			{
				// 纸张类型 - 大度
				postData.paperType = 'MAGNANIMOUS_PAPER';
			}
			// 当成品尺寸选择自定义尺寸时
			if (postData.spec == "CUSTOM")
			{
				var customLength = $("#length").val();
				var customWidth = $("#width").val();
				// 自定义规格
				postData.spec = customLength + "*" + customWidth;
				// 自定义成品尺寸-长
				postData.styleLength = customLength;
				// 自定义成品尺寸-宽
				postData.styleWidth = customWidth;
			} else
			{
				// 成品尺寸
				var $spec = $("#spec").val();
				var $specSplit = $spec.split("*");
				// 成品尺寸-展长
				postData.styleLength = $specSplit[0];
				// 成品尺寸-展宽
				postData.styleWidth = $specSplit[1];
				// 封套类的展长展宽特殊计算
				if (postData.offerType == 'ENVELOPETYPE')
				{
					// 封套展长：长*2+10
					postData.styleLength = $specSplit[0] * 2 + 10;
					// 封套展宽：宽+兜底
					postData.styleWidth = $specSplit[1] * 1 + postData.revealType * 1;
				}
				// 信封类的展长展宽特殊计算
				if (postData.offerType == 'MAILERTYPE')
				{
					// 信封展开长：长+40
					postData.styleLength = $specSplit[0] * 1 + 40;
					// 信封展开宽：宽*2+20
					postData.styleWidth = $specSplit[1] * 2 + 20;
				}
			}
		} else
		{
			// 卡片类 不干胶类
			postData.spec = postData.styleLength + "*" + postData.styleWidth;
		}
		
		// 用于展示的规格
		postData.specification = postData.spec;
		// 特殊处理的规格(彩盒类)
		if (postData.offerType == "CARTONBOX")
		{
			postData.specification = postData.length + "*" + postData.width + "*" + postData.height;
		}
		
		// 客来纸、加坑纸
		var offerPartList = [];
		for (var i = 0; i < postData.offerPartList.length; i++)
		{
			var offerPart = postData.offerPartList[i];
			// 客来纸
			if (offerPart.customPaper && offerPart.customPaper == "on")
			{
				offerPart.customPaper = "YES";
			}
			// 加坑纸
//			if (offerPart.containBflute && offerPart.containBflute == "on")
//			{
//				offerPart.containBflute = "YES";
//			}
			var bfluteList = $("input[name='offerPartList.containBflute']");
			for (var j = 0; j < bfluteList.length; j++)
			{
				if (i == j)
				{
					if (bfluteList.eq(j).is(":checked"))
					{
						offerPart['containBflute'] = "YES";
					} else
					{
						offerPart['containBflute'] = "NO";
					}
				}
			}
			// 删除隐藏插页（书刊画册特殊处理）
			if ($("#btn_add_insertP").length > 0 && $("#btn_add_insertP").val() == "添加插页" && offerPart.partName == "插页")
			{
				continue;
			}
			// 书刊类自定义页数
			if (postData.offerType == "ALBUMBOOK")
			{
				if (offerPart.pages == "-1")
				{
					offerPart.pages = offerPart.customPages;
				}
			}
			offerPartList.push(offerPart);
		}
		postData.offerPartList = offerPartList;

		// 厚道工序
		delete postData.partDetail;
		$("#quote_form .procedureTr").each(function(idx, item)
		{
			if ($(item).css("display") != "none")
			{
				postData.offerPartList[idx].offerPartProcedureList = [];
				$.each($(item).find(".label-checkbox input[name='partDetail.isChecked']:checked"), function(idx2, item2)
				{
					postData.offerPartList[idx].offerPartProcedureList[idx2] = {};
					postData.offerPartList[idx].offerPartProcedureList[idx2].offerType = $(item2).parent().find("input[name='partDetail.offerType']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].procedureType = $(item2).parent().find("input[name='partDetail.procedureType']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].procedureClass = $(item2).parent().find("input[name='partDetail.procedureClass']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].procedureId = $(item2).parent().find("input[name='partDetail.procedureId']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].length = $(item2).parent().find("input[name='partDetail.length']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].width = $(item2).parent().find("input[name='partDetail.width']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].price = $(item2).parent().find("input[name='partDetail.price']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].procedureName = $(item2).parent().find("input[name='partDetail.procedureName']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].procedureUnit = $(item2).parent().find("input[name='partDetail.procedureUnit']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].lowestPrice = $(item2).parent().find("input[name='partDetail.lowestPrice']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].startPrice = $(item2).parent().find("input[name='partDetail.startPrice']").val();
					postData.offerPartList[idx].offerPartProcedureList[idx2].offerProcedureFormulaType = $(item2).parent().find("input[name='partDetail.offerProcedureFormulaType']").val();
				})
			}
		});

		// 成品工序
		delete postData.productDetail;
		postData.productProcedure = [];
		$("#quote_form .productProcedureTd").each(function(idx, item)
		{
			$.each($(item).find(".label-checkbox input[name='productDetail.isChecked']:checked"), function(idx2, item2)
			{
				postData.productProcedure[idx2] = {};
				postData.productProcedure[idx2].offerType = $(item2).parent().find("input[name='productDetail.offerType']").val();
				postData.productProcedure[idx2].procedureType = $(item2).parent().find("input[name='productDetail.procedureType']").val();
				postData.productProcedure[idx2].procedureClass = $(item2).parent().find("input[name='productDetail.procedureClass']").val();
				postData.productProcedure[idx2].procedureId = $(item2).parent().find("input[name='productDetail.procedureId']").val();
				postData.productProcedure[idx2].length = $(item2).parent().find("input[name='productDetail.length']").val();
				postData.productProcedure[idx2].width = $(item2).parent().find("input[name='productDetail.width']").val();
				postData.productProcedure[idx2].price = $(item2).parent().find("input[name='productDetail.price']").val();
				postData.productProcedure[idx2].procedureName = $(item2).parent().find("input[name='productDetail.procedureName']").val();
				postData.productProcedure[idx2].procedureUnit = $(item2).parent().find("input[name='productDetail.procedureUnit']").val();
				postData.productProcedure[idx2].lowestPrice = $(item2).parent().find("input[name='productDetail.lowestPrice']").val();
				postData.productProcedure[idx2].startPrice = $(item2).parent().find("input[name='productDetail.startPrice']").val();
				postData.productProcedure[idx2].offerProcedureFormulaType = $(item2).parent().find("input[name='productDetail.offerProcedureFormulaType']").val();
			})
		});
		// 设计样式和设计费用
		postData.designType = $(".design .designType").val();
		postData.designFee = $(".design .designFee").val();
		return postData;
	}

	/**
	 * 计算报价验证
	 * @since 1.0, 2017年11月13日 下午5:36:33, think
	 */
	function _validateQuote()
	{
		// 基础数据验证
		return _validateBase();
	}

	/**
	 * 保存报价验证
	 * @since 1.0, 2017年11月13日 下午5:36:33, think
	 */
	function _validateSave()
	{
		// 报价验证 - 基础数据验证
		if (!_validateBase())
		{
			return false;
		}

		if (!_validateOuterOrder())
		{
			return false;
		}

		return true;
	}

	/**
	 * 报价验证 - 基础数据
	 * @since 1.0, 2017年11月13日 下午5:36:33, think
	 */
	function _validateBase()
	{
		try
		{
			// =========== 可选字段验证 ===========
			// 自定义尺寸-长（用于：吊牌卡片）
			var $length = $("#length");
			var _isShow = $length.parents(".hiddenSpan").css("display") != 'none';
			if ($length.length > 0 && _isShow && !validateFieldText($length, "请填写成品尺寸-长"))
			{
				throw "";
			}
			if ($length.length > 0 && _isShow && !validateFieldEqText($length, "0", "请填写大于0的成品尺寸-长"))
			{
				throw "";
			}
			// 自定义尺寸-宽（用于：吊牌卡片）
			var $width = $("#width");
			if ($width.length > 0 && _isShow && !validateFieldText($width, "请填写成品尺寸-宽"))
			{
				throw "";
			}
			if ($width.length > 0 && _isShow && !validateFieldEqText($width, "0", "请填写大于0的成品尺寸-宽"))
			{
				throw "";
			}
			// =========== 必填字段验证 ===========
			// 印刷数量
			var $amount = $("#amount");
			if (!validateFieldText($amount, "请填写印刷数量"))
			{
				throw "";
			}
			if (!validateFieldEqText($amount, "0", "请填写大于0的印刷数量"))
			{
				throw "";
			}
			$("input[name='offerPartList.customPages']").each(function()
			{

				if ($(this).parents(".customInp").css("display") != "none" && !validateFieldText($(this), "请填写自定义页数"))
				{
					throw "";
				}
			});
			// 页数
			var $pageType = $("#pageType");
			if ($pageType.length > 0 && !validateFieldText($pageType, "请填写页数"))
			{
				throw "";
			}
			if ($pageType.length > 0 && !validateFieldEqText($pageType, "0", "请填写大于0的页数"))
			{
				throw "";
			}
			// 设计费
			var designType = $(".design .designType").val();
			if (designType != "")
			{
				var $design = $(".design .designFee");
				if (!validateFieldText($design, "请填写设计费"))
				{
					throw "";
				}
				if (!validateFieldEqText($design, 0, "请填写设计费"))
				{
					throw "";
				}
			}
			// 印刷纸张（多条）
			$("select[name='offerPartList.paperName']").each(function()
			{
				// 书刊画册特殊处理
				var display = $(this).parents(".insert_tr").css("display");
				if (display != "none")
				{
					if (!validateFieldEqText($(this), "", "请选择印刷纸张"))
					{
						throw "";
					}
				}

			});
			// 坑纸
			$("select[name='offerPartList.bflutePit']").each(function()
			{
				var visible = $(this).parents("tr.bfluteTr").css("display");
				if (visible != "none" && !validateFieldText($(this), "请选择坑形"))
				{
					throw "";
				}
			})
			$("input[name='partDetail.isChecked']:checked").each(function()
			{
				// 指定尺寸-长
				var $length = $(this).parent().find("input[name='partDetail.length']");
				if ($length.length > 0 && !validateFieldText($length, "请填写指定尺寸-长"))
				{
					throw "";
				}
				if ($length.length > 0 && !validateFieldEqText($length, "0", "请填写大于0的指定尺寸-长"))
				{
					throw "";
				}
				// 指定尺寸的宽
				var $width = $(this).parents("label").find("input[name='partDetail.width']");
				if ($width.length > 0 && !validateFieldText($width, "请填写指定尺寸-宽"))
				{
					throw "";
				}
				if ($width.length > 0 && !validateFieldEqText($width, "0", "请填写大于0的指定尺寸-宽"))
				{
					throw "";
				}
			})
		} catch (e)
		{
			return false;
		}

		return true;
	}

	/**
	 * 报价验证 - 报价单
	 * @since 1.0, 2017年11月13日 下午5:36:33, think
	 */
	function _validateOuterOrder()
	{
		try
		{
			// 成品名称
			var $productName = $("#productName");
			if (!validateFieldText($productName, "请填写成品名称"))
			{
				throw "";
			}
			// 客户名称
			var $customerName = $("#customerName");
			if (!validateFieldText($customerName, "请填写客户名称"))
			{
				throw "";
			}
			// 联系人
			var $linkName = $("#linkName");
			if (!validateFieldText($linkName, "请填写联系人"))
			{
				throw "";
			}
			// 联系电话
			var $phone = $("#phone");
			if (!validateFieldText($phone, "请填写联系电话"))
			{
				throw "";
			}
			// 客户地址
			var $linkAddress = $("#linkAddress");
			if (!validateFieldText($linkAddress, "请填写客户地址"))
			{
				throw "";
			}
			// 交货时间
			var $deliveryDate = $("#deliveryDate");
			if (!validateFieldText($deliveryDate, "请填写交货时间"))
			{
				throw "";
			}
		} catch (e)
		{
			return false;
		}

		return true;
	}

	/**
	 * 选择设计类型
	 * @since 1.0, 2018年10月30日 下午2:10:30, zhengxchn@163.com
	 */
	$(".design .designType").on("change", function()
	{
		var designType = $(this).val();
		if (designType == "")
		{
			$(".design .designFee").val("");
			$(".design .designFee").attr("readonly", "readonly");
			$(".design .designFee").css("background-color", "#f1f1f1");
		}
		else
		{
			$(".design .designFee").removeAttr("readonly");
			$(".design .designFee").css("background-color", "#ffffff");
		}
  });
});

// ================ 页面公共功能 ================

/**
 * 选择纸张类型后联动纸张克重下拉框
 * @since 1.0, 2017年11月8日 下午6:54:03, zhengby
 */
function selectPaperType(obj)
{
	var $this = $(obj);
	var name = $this.val();
	var type = $("#offerType").val();

	if (name != null && name != "")
	{
		$.ajax({
			type : "POST",
			url : HYWX.basePath + "/wx/offer/getOfferPaperList",
			data : {
				"name" : name,
				"offerType" : type
			},
			dataType : "json",
			async : false,// 同步请求
			success : function(data)
			{
				var str = "";
				if (data.length != 0)
				{
					// 设置克重 和 吨价
					$.each(data, function(idx, val)
					{
						str += "<option value='" + val.weight + "'tonPrice=" + val.tonPrice + ">" + val.weight + "</option>";
					});
					$this.parent().next().find(".machine_sel").children().remove(); // 删除默认选项
					$this.parent().next().find(".machine_sel").append(str).trigger("change");
				}
			},
			error : function(data)
			{
				HYWX.message.err("请求错误");
			}
		});
	} else
	{
		// 当选择了“请选择”选项时，初始化第二个下拉框
		var str = "<option value='0'>请选择</option>";
		$this.parent().next().find(".machine_sel").children().remove();
		$this.parent().next().find(".machine_sel").append(str);
	}

}
/**
 * 对外报价阶梯数据
 * @since 1.0, 2017年12月19日 上午9:09:22, zhengby
 */
function _quoteOut(data)
{
//	// 对外报价单仅第一个阶梯数据
//	for(var i = 0 ; i<data.obj.offerPartList.length; i++)
//	{
//		var $TR = $("#quoteListDiv").clone(true);
//	}
//	var offerPart = data.obj.offerPartList[0];
//	$("#_printName").val(offerPart.printName);
//	$("#_printColor").val(offerPart.printColor);
//	$("#_printProcedure").val(offerPart.printProcedure);
//	$("#_amount").val(offerPart.amount);
//	$("#_price").val(Number(offerPart.price).toFixed(2));
//	$("#_fee").val(Number(offerPart.fee).toFixed(2));
//	$("#_taxPrice").val(Number(offerPart.taxPrice).toFixed(2));
//	$("#_taxFee").val(Number(offerPart.taxFee).toFixed(2));
	//删除历史的tr
	$("#quote_form .copyTr").remove();

	// 多部件列表
	var offerPartList = data.obj.offerPartList;
	var $len = offerPartList.length;

	for (var i = 0; i < $len; i++)
	{
		var TR = $("#template").find("tbody tr").clone(true);
		var offerPart = offerPartList[i];
		// 印刷纸张：根据前面选择的材料名称和克重
		TR.find("td input").eq(0).val(offerPart.printName);
		// 颜色：根据前面选择的印刷颜色，如四色+四专，四色+四专/四色+四专
		TR.find("td input").eq(1).val(offerPart.printColor);
		// 加工工序
		TR.find("td input").eq(2).val(offerPart.printProcedure);
		// 数量
		TR.find("td input").eq(3).val(offerPart.amount);
		// 单价：金额/数量
		TR.find("td input").eq(4).val(Number(offerPart.price).toFixed(4));
		// 金额：内部核价的不含税金额
		TR.find("td input").eq(5).val(Number(offerPart.fee).toFixed(2));
		// 含税单价：含税金额/数量
		TR.find("td input").eq(6).val(Number(offerPart.taxPrice).toFixed(4));
		// 含税金额：内部核价的含税金额
		TR.find("td input").eq(7).val(Number(offerPart.taxFee).toFixed(2));

		$("table.offer_table").append(TR);
	}
}

/**
 * 对内报价阶梯数据
 * @since 1.0, 2017年12月19日 上午9:09:13, zhengby
 */
function _quoteInner(data){
	var $len = data.obj.offerMachineOrderList ? data.obj.offerMachineOrderList.length : 0;
	for (var i = 0; i < $len; i++)
	{
		var TR = $("#template_nb2").find("tbody tr").clone(true);
//		LIST[i] = {}; // 用于保存内部报价阶梯数据报表
		var machineOrder = data.obj.offerMachineOrderList[i];
		TR.find("td input").eq(0).val(i + 1);
		// 数量：根据前面阶梯数量的列数的不同生成不同的列数，根据前面不同阶梯数量间隔用报价数量+阶梯数量间隔生成不同数量
		TR.find("td input").eq(1).val(machineOrder.amount);
//		LIST[i].amount = machineOrder.amount;
		// 纸张费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量所有部件用到的纸张费用
		TR.find("td input").eq(2).val(Number(machineOrder.paperFee).toFixed(2));
//		LIST[i].paperFee = machineOrder.paperFee;
		// 印刷费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量所有部件对应印刷机的印刷费
		TR.find("td input").eq(3).val(Number(machineOrder.printFee).toFixed(2));
//		LIST[i].printFee = machineOrder.printFee;
		// 工序费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量所有部件对应的工序所有工序费用
		TR.find("td input").eq(4).val(Number(machineOrder.procedureFee).toFixed(2));
//		LIST[i].procedureFee = machineOrder.procedureFee;
		// 其他费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量的其他费用
		TR.find("td input").eq(5).val(Number(machineOrder.ohterFee).toFixed(2));
//		LIST[i].ohterFee = machineOrder.ohterFee;
		// 运费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应的不同的运费，可以手动调整
		TR.find("td input").eq(6).val(Number(machineOrder.freightFee).toFixed(2));
//		LIST[i].freightFee = machineOrder.freightFee;
		// 成本金额：纸张费+印刷费+工序费+其他费+运费
		TR.find("td input").eq(7).val(Number(machineOrder.costMoney).toFixed(2));
//		LIST[i].costMoney = machineOrder.costMoney;
		// 利润：根据彩盒设置里的利润设置，计算出不同阶梯数量的利润金额
		TR.find("td input").eq(8).val(Number(machineOrder.profitFee).toFixed(2));
//		LIST[i].profitFee = machineOrder.profitFee;
		// 未税金额：成本金额+利润
		TR.find("td input").eq(9).val(Number(machineOrder.untaxedFee).toFixed(2));
//		LIST[i].untaxedFee = machineOrder.untaxedFee;
		// 未税单价：未税金额/数量
		TR.find("td input").eq(10).val(Number(machineOrder.untaxedPrice).toFixed(4));
//		LIST[i].untaxedPrice = machineOrder.untaxedPrice;
		// 含税金额：未税金额*（1+税率）
		TR.find("td input").eq(11).val(Number(machineOrder.taxFee).toFixed(2));
//		LIST[i].taxFee = machineOrder.taxFee;
		// 含税单价：含税金额/数量
		TR.find("td input").eq(12).val(Number(machineOrder.taxPrice).toFixed(4));
//		LIST[i].taxPrice = machineOrder.taxPrice;
		$("table.offer_table").append(TR);
	}
}
/**
 * 是否为NULL
 * @param value
 * @returns {Boolean}
 * @since 1.0, 2017年11月14日 下午5:15:30, think
 */
function isNull(value)
{
	if (value === undefined || value === null)
	{
		return true;
	}
	return false;
}

/**
 * 是否为空
 * @param value
 * @returns
 * @since 1.0, 2017年11月14日 下午5:15:39, think
 */
function isEmpty(value)
{
	if (isNull(value))
	{
		return true;
	} else if (typeof (value) == "string")
	{
		if (value.trim() === "" || value === "undefined" || value === "[]" || value === "{}")
		{
			return true;
		}
	} else if (typeof (value) === "object")
	{
		return value.length <= 0 ? true : false;
	} else
	{
		return false;
	}
}

/**
 * 
 * 表单验证字段并提示信息
 * @param $field
 * @param message
 * @returns {Boolean}
 * @since 1.0, 2017年10月26日 下午3:19:55, think
 */
function validateFieldText($field, message)
{
	if (isEmpty($field.val()))
	{
		$.toast(message, 1000, "mytoast");
		$field.focus();
		return false;
	}
	return true;
}
/**
 * 
 * 表单验证字段并提示信息
 * @param $field
 * @param eqmsg
 * @param message
 * @returns {Boolean}
 * @since 1.0, 2017年10月26日 下午3:19:55, think
 */
function validateFieldEqText($field, eqmsg, message)
{
	if (!(eqmsg < $field.val()))
	{
		$.toast(message, 1000, "mytoast");
		$field.focus();
		return false;
	}
	return true;
}