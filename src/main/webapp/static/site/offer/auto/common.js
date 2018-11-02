/**
 * Author:       THINK
 * Create:       2017年11月3日 上午10:51:43
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init(); // 初始化
	var LIST = []; // 用于保存内部核价单阶梯数据报表
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
		$("#btn_add_insertP").click(function()
		{
			if ($(this).html() == "添加插页")
			{
				$(this).html("删除插页")
				$(".insert_tr").show();
			} else
			{
				$(this).html("添加插页");
				$(".insert_tr").hide();
			}
		});

		// ================ 报价保存================

		// 每次切换按钮时，删除复制的阶梯数据，防止出现重复阶梯数据
		$(".offer_btn2").click(function()
		{
			$(".tempTr:hidden").remove();
		});
		// 保存
		$("#btn_save").on("click", function()
		{
			// 先要执行对外报价
			if ($("#dy_form").is(":hidden"))
			{
				// 报价时间为空,则需要先计算报价
				var $createDateTime = $("#createDateTime");
				if (Helper.isEmpty($createDateTime.val()))
				{
					Helper.message.tips("请先计算报价", $(".btn_cal"));
					$field.focus();
					return false;
				}
			}

			// 验证
			if (!_validateSave())
			{
				return false;
			}

			// 提交
			formSubmit();
		});

		// ================ 报价计算================

		// 点击对外报价
		$(".btn_dy").on("click", function()
		{
			// 隐藏费用明细按钮
			$(".btn_fee").hide();
			// 显示报价
			$(".autoprice_div1").show();
			// 阶梯数
			$("#ladder").show();
			// 隐藏报价费用明细
			$(".autoprice_div2").hide();

			// 显示对外报价
			$("#dy_form").show();
			// 隐藏内部核价
			$("#nb_form").attr("style", "display:none;")
			// 隐藏费用明细
			$("#fee_form").hide();
			// 隐藏拼版开料图
			$("#pb_div").hide();
		});

		// 点击内部核价
		$(".btn_nb").on("click", function()
		{
			// 显示费用明细按钮
			$(".btn_fee").show();
			// 显示报价
			$(".autoprice_div1").show();
			// 阶梯数
			$("#ladder").show();
			// 隐藏报价费用明细
			$(".autoprice_div2").hide();

			// 隐藏对外报价
			$("#dy_form").hide();
			// 显示内部核价
			// $("#nb_form").show();
			$("#nb_form").attr("style", "display:block;");
			// 隐藏费用明细
			$("#fee_form").hide();
			// 隐藏拼版开料图
			$("#pb_div").hide();
		});

		// 点击费用明细
		$(".btn_fee").on("click", function()
		{
			// 隐藏费用明细按钮
			$(".btn_fee").hide();
			// 隐藏报价
			$(".autoprice_div1").hide();
			// 显示报价费用明细
			$(".autoprice_div2").show();

			// 隐藏对外报价
			$("#dy_form").hide();
			// 隐藏内部核价
			// $("#nb_form").hide();
			$("#nb_form").attr("style", "display:none;");
			// 显示费用明细
			$("#fee_form").show();
			// 隐藏拼版开料图
			$("#pb_div").hide();
		});

		// 点击费用明细关闭
		$("#btn_fee_close").on("click", function()
		{
			// 显示费用明细按钮
			$(".btn_fee").show();
			// 隐藏报价
			$(".autoprice_div1").show();
			// 阶梯数
			$("#ladder").show();
			// 显示报价费用明细
			$(".autoprice_div2").hide();

			// 隐藏对外报价
			$("#dy_form").hide();
			// 隐藏内部核价
			// $("#nb_form").show();
			$("#nb_form").attr("style", "display:block;");
			// 隐藏费用明细
			$("#fee_form").hide();
			// 隐藏拼版开料图
			$("#pb_div").hide();
		});

		// 点击拼版开料图
		$(".btn_pb").on("click", function()
		{
			// 隐藏费用明细按钮
			$(".btn_fee").hide();
			// 显示报价
			$(".autoprice_div1").show();
			// 阶梯数
			$("#ladder").hide();
			// 显示报价费用明细
			$(".autoprice_div2").hide();

			// 隐藏对外报价
			$("#dy_form").hide();
			// 隐藏内部核价
			// $("#nb_form").hide();
			$("#nb_form").attr("style", "display:none;");
			// 隐藏费用明细
			$("#fee_form").hide();
			// 隐藏拼版开料图
			$("#pb_div").show();
		});
		// 计算报价
		$(".btn_cal").on("click", function()
		{
			// 验证数据
			if (_validateQuote())
			{
				// 计算报价
				_quote();

				// 显示对外报价
				$(".btn_dy").click();
			}
		});
		// 选中的行显示高亮
		$(".changeTr").click(function()
		{
			var $this = $(this);
			// TODO
			// 每次点击时，都post计算
			// 找到机台Id
			var machineId = $this.find("input[name='machineId']").val();
			// 找到部件名称
			var machineType = $this.find("input[name='machineType']").val();
			// 切换机台
			_changeTr($this, machineId, machineType);
			// 获取选中的机台
			var choosenedList = _getChoosenedPart();
			// 重新计算
			_quote(choosenedList);

		});
		// 选择客户
		$("#customer_quick_select").click(function()
		{
			Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '500');
		});

		$("#product_quick_select").click(function()
		{
			var _customerId = $("#customerId").val();
			if (Helper.isEmpty(_customerId))
			{
				try
				{
					// 客户名称
					var $customerName = $("#customerName");
					if (!validateFieldText($customerName, "请选择客户信息"))
					{
						throw "";
					}
				} catch (e)
				{
					return false;
				}
			}
			Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false&customerId=' + _customerId, '900', '490');
		});

		// 选中克重
		$("#paperWeight,#paperWeight2,#paperWeight3").on("change", function()
		{
			var $this = $(this);
			var $paperPrice = $this.find("option:selected").data("tonprice");
			$(this).next().val($paperPrice);
		});

		// 书刊类添加插页，删除插页监听事件
		$("#addInsert").on("click", function()
		{
			$("#delInsert").parents("tr").show();
			$(this).parent().hide();
			$("#insertTr").show();
		})
		$("#delInsert").on("click", function()
		{
			$(this).parents("tr").hide();
			$("#addInsert").parent().show();
			$("#insertTr").hide();
		})

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
			if ($(this).val() == "DOUBLE" || $(this).val() == "HEADTAIL" || $(this).val() == "CHAOS")
			{
				$(this).parent().parent().find(".doubleColor").show();
			} else
			{
				$(this).parent().parent().find(".doubleColor").hide();
			}

			// 自翻印刷，反面不能选择
			if ($(this).val() == "CHAOS")
			{
				$(this).parent().parent().find(".doubleColor select").attr("disabled", true);
			} else
			{
				$(this).parent().parent().find(".doubleColor select").removeAttr("disabled");
			}
			// 当选择单面印刷，反面印刷的下拉值设成无色+零专
			if ($(this).val() == "SINGLE")
			{
				$(this).parent().parent().find("select[name='offerPartList.offerPrintColorType2']").val("NONE");
				$(this).parent().parent().find("select[name='offerPartList.offerSpotColorType2']").val("NOSPOT");
			}
		});

		// 自翻印刷，反面不能选择，并且需要同步正面选择得颜色
		$("select[name='offerPartList.offerPrintColorType']").on("change", function()
		{
			var $this = $(this);
			var $type = $this.parent().parent().find("select[name='offerPartList.offerPrintStyleType']").find("option:selected").val();

			if ($type == "CHAOS")
			{
				var $val = $this.find("option:selected").val();
				$(this).parent().parent().find(".doubleColor select[name='offerPartList.offerPrintColorType2']").val($val).trigger("change");
			}
		});

		// 自翻印刷，反面不能选择，并且需要同步正面选择得颜色
		$("select[name='offerPartList.offerSpotColorType']").on("change", function()
		{
			var $this = $(this);
			var $type = $this.parent().parent().find("select[name='offerPartList.offerPrintStyleType']").find("option:selected").val();

			if ($type == "CHAOS")
			{
				var $val = $this.find("option:selected").val();
				$(this).parent().parent().find(".doubleColor select[name='offerPartList.offerSpotColorType2']").val($val).trigger("change");
			}
		});
	}

	/**
	 * 取消和勾选 相同部件的勾选
	 * @since 1.0, 2017年12月4日 上午9:16:06, think
	 */
	function _changeTr(chooseTr, machineId, machineType)
	{
		// 取消 相同部件的勾选
		$(".copyTr_nb").each(function(idx, item)
		{
			var $item = $(item);
			var $machineType = $item.find("input[name='machineType']").val();
			if (machineType == $machineType)
			{
				$item.removeClass("choosen");
				$item.find("input[name='ckbox']").prop("checked", false);
			}
		});

		// 选中 相同部件的勾选
		chooseTr.addClass("choosen");
		chooseTr.find("input[name='ckbox']").prop("checked", true);
	}

	/**
	 * 获取选中的部件和机台
	 * @since 1.0, 2017年12月4日 上午9:16:06, think
	 */
	function _getChoosenedPart()
	{
		var choosenedList = [];
		$(".copyTr_nb.choosen").each(function(idx, item)
		{
			var $item = $(item);
			var $machineId = $item.find("input[name='machineId']").val();
			var $machineType = $item.find("input[name='machineType']").val();
			choosenedList.push($machineType + $machineId);
		});

		return choosenedList;
	}

	/**
	 * 保存
	 * @since 1.0, 2017年11月7日 下午5:16:06, zhengby
	 */
	function formSubmit()
	{
		var postData = _genPostData();
		// 内部核价单阶梯数据报表
		postData.offerOrderQuoteInnerList = LIST;
		// 选中机台
		var choosenedList = _getChoosenedPart();
		if (choosenedList && choosenedList.length > 0)
		{
			postData.chooseedMachineList = choosenedList;
		}
		postData.specification = $("#specification").val();
		// 保存时生成
		_savePost(postData).done(function(data)
		{
			if (data.obj && data.success)
			{
				Helper.message.suc("已保存！");
				// 跳转到报价列表
				var url = Helper.basePath + '/offer/list';
				var title = "报价单列表";
				closeTabAndJump(title, url);
			} else
			{
				Helper.message.warn('保存失败：' + data.message);
			}
		});
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
				$(this).parent().parent().find(".doubleColor select").removeAttr("disabled");
			}
		});

		var qf = $("#quote_form").formToJson();
		var df = $("#dy_form").formToJson();

		// 先要移植disabled元素，否则不能正常获取
		$("select[name='offerPartList.offerPrintStyleType']").each(function(idx, item)
		{
			if ($(this).val() == "CHAOS")
			{
				$(this).parent().parent().find(".doubleColor select").attr("disabled", true);
			}
		});
		var postData = $.extend(true, qf, df);
		postData.boxType = $("#boxType").val();
		postData.ladderCol = $("#rows").val();
		postData.ladderSpeed = $("#spaceQty").val();
		postData.offerType = $("#offerType").val();

		// 成品尺寸计算（默认是彩盒）
		if ($("#spec").length > 0)
		{
			var $specText = $("#spec").find("option:selected").text();
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
			var customPaperList = $("input[name='offerPartList.customPaper']");
			for (var j = 0; j < customPaperList.length; j++)
			{
				if (i == j)
				{
					if ($("input[name='offerPartList.customPaper']").eq(j).is(":checked"))
					{
						offerPart['customPaper'] = "YES";
					} else
					{
						offerPart['customPaper'] = "NO";
					}
				}
			}
			// 加坑纸
			// if (offerPart.containBflute && offerPart.containBflute == "on")
			// {
			// offerPart.containBflute = "YES";
			// }
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
			if ($("#btn_add_insertP").length > 0 && $("#btn_add_insertP").html() == "添加插页" && offerPart.partName == "插页")
			{
				continue;
			}

			offerPartList.push(offerPart);
		}
		postData.offerPartList = offerPartList;

		// 部件工序
		delete postData.partDetail;
		$("#quote_form .procedureTd:visible").each(function(idx, item)
		{
			postData.offerPartList[idx].offerPartProcedureList = [];
			$.each($(item).find(".offer-label input[name='partDetail.isChecked']:checked"), function(idx2, item2)
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
		});

		// 成品工序
		delete postData.productDetail;
		postData.productProcedure = [];
		$("#quote_form .productProcedureTd").each(function(idx, item)
		{
			$.each($(item).find(".offer-label input[name='productDetail.isChecked']:checked"), function(idx2, item2)
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
		// 对外报价单验证
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

			// 自定义尺寸-长
			var $length = $("#length:visible");
			if ($length.length > 0 && !Helper.validfield.validateFieldText($length, "请填写成品尺寸-长"))
			{
				throw "";
			}
			if ($length.length > 0 && !Helper.validfield.validateFieldEqText($length, "0", "请填写成品尺寸-长"))
			{
				throw "";
			}
			// 自定义尺寸-宽
			var $width = $("#width:visible");
			if ($width.length > 0 && !Helper.validfield.validateFieldText($width, "请填写成品尺寸-宽"))
			{
				throw "";
			}
			if ($width.length > 0 && !Helper.validfield.validateFieldEqText($width, "0", "请填写成品尺寸-宽"))
			{
				throw "";
			}

			// =========== 必填字段验证 ===========
			// 印刷数量
			var $amount = $("#amount");
			if (!Helper.validfield.validateFieldText($amount, "请填写印刷数量"))
			{
				throw "";
			}
			if (!Helper.validfield.validateFieldEqText($amount, "0", "请填写印刷数量"))
			{
				throw "";
			}
			// 页数
			var $pageType = $("#pageType");
			if ($pageType.length > 0 && !Helper.validfield.validateFieldText($pageType, "请填写页数"))
			{
				throw "";
			}
			if ($pageType.length > 0 && !Helper.validfield.validateFieldEqText($pageType, "0", "请填写页数"))
			{
				throw "";
			}
			// 设计费
			var designType = $(".design .designType").val();
			if (designType != "")
			{
				var $design = $(".design .designFee");
				if (!Helper.validfield.validateFieldText($design, "请填写设计费"))
				{
					throw "";
				}
				if (!Helper.validfield.validateFieldEqText($design, 0, "请填写设计费"))
				{
					throw "";
				}
			}
			// 印刷纸张（多条）
			$("select[name='offerPartList.paperName']:visible").each(function()
			{
				// 书刊画册特殊处理
				var display = $(this).parents(".insert_tr").css("display");
				if (display != "none")
				{
					if (!Helper.validfield.validateFieldEqText($(this), "", "请选择印刷纸张"))
					{
						throw "";
					}
				}
			});
			// 坑纸
			$("select[name='offerPartList.bflutePit']:visible").each(function()
			{
				if (!Helper.validfield.validateFieldText($(this), "请选择坑形"))
				{
					throw "";
				}
			})
			$("input[name='partDetail.isChecked']:checked").each(function()
			{
				// 指定尺寸-长
				var $length = $(this).parent().find("input[name='partDetail.length']");
				if ($length.length > 0 && !Helper.validfield.validateFieldText($length, "请填写指定尺寸-长"))
				{
					throw "";
				}
				if ($length.length > 0 && !Helper.validfield.validateFieldEqText($length, "0", "请填写指定尺寸-长"))
				{
					throw "";
				}
				// 指定尺寸的宽
				var $width = $(this).parents("label").find("input[name='partDetail.width']");
				if ($width.length > 0 && !Helper.validfield.validateFieldText($width, "请填写指定尺寸-宽"))
				{
					throw "";
				}
				if ($width.length > 0 && !Helper.validfield.validateFieldEqText($width, "0", "请填写指定尺寸-宽"))
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
			// 阶梯数量行数
			var $rows = $("#rows");
			if (!Helper.validfield.validateFieldText($rows, "请填写阶梯数量行数"))
			{
				throw "";
			}
			if (!Helper.validfield.validateFieldEqText($rows, "0", "请填写阶梯数量行数"))
			{
				throw "";
			}
			// 阶梯数量间隔
			var $spaceQty = $("#spaceQty");
			if (!Helper.validfield.validateFieldText($spaceQty, "请填写阶梯数量间隔"))
			{
				throw "";
			}
			if (!Helper.validfield.validateFieldEqText($spaceQty, "0", "请填写阶梯数量间隔"))
			{
				throw "";
			}
			// 交货时间
			var $deliveryDate = $("#deliveryDate");
			if (!validateFieldText($deliveryDate, "请填写交货时间"))
			{
				throw "";
			}
			// 客户名称
			var $customerName = $("#customerName");
			if (!validateFieldText($customerName, "请填写客户名称"))
			{
				throw "";
			}
			// 客户地址
			var $linkAddress = $("#linkAddress");
			if (!validateFieldText($linkAddress, "请填写客户地址"))
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
			// 成品名称
			var $productName = $("#productName");
			if (!validateFieldText($productName, "请填写成品名称"))
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
	 * 计算报价
	 * @since 1.0, 2017年11月6日 下午5:36:33, think
	 */
	function _quote(choosenedList)
	{
		var postData = _genPostData();

		if (choosenedList && choosenedList.length > 0)
		{
			postData.chooseedMachineList = choosenedList;
		}

		_quotePost(postData).done(function(data)
		{
			if (data.obj && data.success)
			{
				// 对外报价计算
				_quoteOuter(data);
				// 内部核价计算
				_quoteInner(data);
				// 拼版开料图
				_quoteOpening(data);
			} else
			{
				Helper.message.warn('计算失败：' + data.message);
			}
		});
	}

	/**
	 * 对外报价计算
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteOuter(data)
	{
		// ================ 对外报价 - 报价单 ================
		_quoteOuterOrder(data);
		// ================ 对外报价 - 报价列表 ================
		_quoteOuterOrderList(data);

	}

	/**
	 * 对外报价 - 报价单
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteOuterOrder(data)
	{
		// 报价时间
		$("#createDateTime").val(data.obj.createDateTimeStr);
		// 交货时间
		$("#deliveryDate").val(data.obj.deliveryDateStr);
		// 报价单号（默认为空，保存成功后再创建）
		// $("#offerNo").val("保存成功后生成");
		// 客户名称：可以手动输入，也可以弹出看选择印管家基础资料里的客户信息
		// 客户地址：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 联系人：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 联系电话：可以手动输入，也可以根据弹出框选择的客户名称携带客户地址
		// 成品名称：可以手动输入，也可以弹出框选择印管家基础资料里的产品信息，默认的为报价单的名称，如彩盒报价就是彩盒，如是书刊画册，就是书刊画册
		$("#productName").val($("#offerTypeText").val());
		// 成品尺寸：直接取上面报价的成品尺寸，不可编辑输入，只读
		$("#specification").val(data.obj.specification);
	}

	/**
	 * 对外报价 - 报价单列表
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteOuterOrderList(data)
	{
		// 删除历史的tr
		$("#dy_form .copyTr").remove();

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

			$("#dy_table").append(TR);
		}
	}

	/**
	 * 内部核价计算
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteInner(data)
	{
		// ================ 内部核价 - 机台列表 ================
		_quoteInnerMachine(data);
		// ================ 内部核价 - 报价列表 ================
		_quoteInnerOrder(data);
		// ================ 内部核价 - 费用列表 ================
		_quoteInnerFeeDetail(data);
	}

	/**
	 * 内部核价 - 机台列表
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteInnerMachine(data)
	{
		// ================ 内部核价 - 机台列表 ================
		// 删除历史的tr
		$("#nb_form .copyTr_nb").remove();
		var $len = data.obj.offerMachineList ? data.obj.offerMachineList.length : 0;

		// 如果全部机台不匹配，则需要提示
		if ($len == 0)
		{
			Helper.message.warn("找不到合适的机台");
			return false;
		}

		for (var i = 0; i < $len; i++)
		{
			var TR = $("#template_nb").find("tbody tr").clone(true);
			var machine = data.obj.offerMachineList[i];

			// 类型：取选择的名称，如果选择的是平口盒就平口盒，选择的是其他盒型就是其他盒型
			TR.find("td input[name='machineType']").val(machine.boxType);
			// 印刷机：根据上面计算的最优方案的拼版选择总金额的最少的印刷机
			TR.find("td input[name='name']").val(machine.name);
			// 上机尺寸：根据上面最优的上机拼版方式，算出上机规格
			TR.find("td input[name='style']").val(machine.style);
			// 印刷方式：根据报价时选择的是单面印刷还是双面印刷
			TR.find("td input[name='offerPrintStyleTypeText']").val(machine.offerPrintStyleTypeText);
			// 手/贴数：P数/单面拼数，进位取整，不足1的按一贴算
			TR.find("td input[name='thread']").val(machine.thread);
			// 拼版数：根据上面最优的上机方式算出拼版数
			TR.find("td input[name='sheetNum']").val(machine.sheetNum);
			// 印张正数：印刷数量/拼版数
			TR.find("td input[name='impositionNum']").val(machine.impositionNum);
			// 损耗数
			TR.find("td input[name='waste']").val(machine.waste);
			// 大纸尺寸：根据上面最优的拼版方式算出是大度纸（889*1194）还是正度纸（787*889）
			TR.find("td input[name='paperTypeText']").val(machine.paperTypeText);
			// 纸开度：根据上面最优的拼版方式算出材料开数
			TR.find("td input[name='materialOpening']").val(machine.paperTypeText + machine.materialOpening + "开");
			// 大纸张数：（印张正数+损耗数）/纸开度
			TR.find("td input[name='bigPaperNum']").val(machine.bigPaperNum);
			// 坑纸数：印张正数+损耗数
			TR.find("td input[name='bfluteNum']").val(machine.bfluteNum);
			// 最低价
			TR.find("td input[name='lowerPrice']").val(machine.lowerPrice);
			// 机台ID
			TR.find("td input[name='machineId']").val(machine.id);
			// 追加到表格
			$("#nb_table .sec_tr").before(TR);

			// 选中最低价
			if (machine.isLowerPrice == true)
			{
				TR.addClass("choosen");

				// 勾选选中
				TR.find("td input[name='ckbox']").attr("checked", true);
			}
		}
	}

	/**
	 * 内部核价 - 报价列表
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteInnerOrder(data)
	{
		// ================ 内部核价 - 报价列表 ================
		var $len = data.obj.offerMachineOrderList ? data.obj.offerMachineOrderList.length : 0;
		for (var i = 0; i < $len; i++)
		{
			var TR = $("#template_nb2").find("tbody tr").clone(true);
			LIST[i] = {}; // 用于保存内部报价阶梯数据报表
			var machineOrder = data.obj.offerMachineOrderList[i];
			TR.find("td input").eq(0).val(i + 1);
			// 数量：根据前面阶梯数量的列数的不同生成不同的列数，根据前面不同阶梯数量间隔用报价数量+阶梯数量间隔生成不同数量
			TR.find("td input").eq(1).val(machineOrder.amount);
			LIST[i].amount = machineOrder.amount;
			// 纸张费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量所有部件用到的纸张费用
			TR.find("td input").eq(2).val(Number(machineOrder.paperFee).toFixed(2));
			LIST[i].paperFee = machineOrder.paperFee;
			// 印刷费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量所有部件对应印刷机的印刷费
			TR.find("td input").eq(3).val(Number(machineOrder.printFee).toFixed(2));
			LIST[i].printFee = machineOrder.printFee;
			// 工序费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量所有部件对应的工序所有工序费用
			TR.find("td input").eq(4).val(Number(machineOrder.procedureFee).toFixed(2));
			LIST[i].procedureFee = machineOrder.procedureFee;
			// 其他费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应不同数量的其他费用
			TR.find("td input").eq(5).val(Number(machineOrder.ohterFee).toFixed(2));
			LIST[i].ohterFee = machineOrder.ohterFee;
			// 运费：根据前面的阶梯数量带入到费用明细里计算出不同数量对应的不同的运费，可以手动调整
			TR.find("td input").eq(6).val(Number(machineOrder.freightFee).toFixed(2));
			LIST[i].freightFee = machineOrder.freightFee;
			// 成本金额：纸张费+印刷费+工序费+其他费+运费
			TR.find("td input").eq(7).val(Number(machineOrder.costMoney).toFixed(2));
			LIST[i].costMoney = machineOrder.costMoney;
			// 利润：根据彩盒设置里的利润设置，计算出不同阶梯数量的利润金额
			TR.find("td input").eq(8).val(Number(machineOrder.profitFee).toFixed(2));
			LIST[i].profitFee = machineOrder.profitFee;
			// 未税金额：成本金额+利润
			TR.find("td input").eq(9).val(Number(machineOrder.untaxedFee).toFixed(2));
			LIST[i].untaxedFee = machineOrder.untaxedFee;
			// 未税单价：未税金额/数量
			TR.find("td input").eq(10).val(Number(machineOrder.untaxedPrice).toFixed(4));
			LIST[i].untaxedPrice = machineOrder.untaxedPrice;
			// 含税金额：未税金额*（1+税率）
			TR.find("td input").eq(11).val(Number(machineOrder.taxFee).toFixed(2));
			LIST[i].taxFee = machineOrder.taxFee;
			// 含税单价：含税金额/数量
			TR.find("td input").eq(12).val(Number(machineOrder.taxPrice).toFixed(4));
			LIST[i].taxPrice = machineOrder.taxPrice;

			$("#nb_table tbody").append(TR);
		}
	}

	/**
	 * 内部核价 - 费用列表
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteInnerFeeDetail(data)
	{
		// 删除历史的tr
		$("#fee_form .copyTr_nb").remove();
		var $len = data.obj.offerMachineDetailFeeList ? data.obj.offerMachineDetailFeeList.length : 0;
		for (var i = 0; i < $len; i++)
		{
			var TR = $("#template_nbfee").find("tbody tr").clone(true);
			var machineFee = data.obj.offerMachineDetailFeeList[i];
			// 纸张费：根据前面的报价逻辑计算出来的大纸张数，以及大纸尺寸，换算成吨的数量，再乘以纸张设置里对应的克重的吨价，大纸尺寸的规格长/1000*大纸尺寸规格宽/1000*克重/1000/1000*大纸张数*吨价
			TR.find("td input").eq(0).val(machineFee.paperFeeName);
			TR.find("td").eq(1).html(machineFee.paperFeeCal);
			TR.find("td input").eq(1).val(machineFee.paperFee);

			$("#fee .last").before(TR);
		}
	}

	/**
	 * 拼版开料图计算
	 * @param data
	 * @since 1.0, 2017年11月7日 上午11:29:43, think
	 */
	function _quoteOpening(data)
	{
		// 删除历史的tr
		$("#pb_div .copyTr_nb").remove();
		var $len = data.obj.offerOpeningList ? data.obj.offerOpeningList.length : 0;
		for (var i = 0; i < $len; i++)
		{
			var TR = $("#template_opening").find(".copyTr_nb").clone(true);
			var opening = data.obj.offerOpeningList[i];
			// 开料图
			TR.find("#open_name1").html(opening.name);
			TR.find("#open_opening").attr("src", Helper.staticPath + "/layout/images/opening/" + opening.opening + ".jpg");
			TR.find("#open_paperLen").html(opening.paperLen);
			TR.find("#open_paperWidth").html(opening.paperWidth);
			// TR.find("#open_name2").html(opening.name);
			// 拼版图
			// var $jlen = opening.sheetNum / 2;
			// var $tr = "";
			// for (var j = 0; j < $jlen; j++)
			// {
			// var $td = "<td>" + opening.sheetLen + "*" + opening.sheetWidth +
			// "</td>";
			// $tr = $tr + "<tr>" + $td + $td + "</tr>";
			// }
			// TR.find("#open_sheetNum tbody").append($tr);
			// TR.find("#open_openingLen").html(opening.openingLen);
			// TR.find("#open_openingWidth").html(opening.openingWidth);

			$("#pb_div").append(TR);
		}
	}

	/**
	 * post计算报价
	 * @param offerOrder
	 * @since 1.0, 2017年11月13日 下午5:36:33, think
	 */
	function _quotePost(offerOrder)
	{
		return _post(offerOrder, "/offer/auto/quote");
	}

	/**
	 * post保存
	 * @param offerOrder
	 * @since 1.0, 2017年11月13日 下午5:36:33, think
	 */
	function _savePost(offerOrder)
	{
		return _post(offerOrder, "/offer/auto/save");
	}

	/**
	 * 提交到服务器
	 * offerOrder说明
	 * {
	 *   length: 成品尺寸-长
	 *   width:  成品尺寸-宽
	 * }
	 * @param offerOrder
	 * @since 1.0, 2017年11月6日 下午5:36:33, think
	 */
	function _post(offerOrder, url)
	{
		var def = $.Deferred();
		// 报价时间
		Helper.request({
			url : Helper.basePath + url,
			data : offerOrder,
			async : true,// 默认异步请求
			success : function(data)
			{
				def.resolve(data);
			},
			error : function(data)
			{
				Helper.message.warn("请求错误")
			},
			beforeSend : function()
			{
				layer.load(1);
			},
			complete : function()
			{
				layer.closeAll('loading');
			}
		});
		return def.promise();
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

/**
 * 处理从客户列表选择返回的数据
 * @since 1.0, 2017年11月3日 下午6:19:21, zhengby
 */
function getCallInfo_customer(obj)
{
	// var old_customerId = $("#customerId").val();
	$("#customerId").val(obj.id);
	$("#customerName").val(obj.name);
	if (Helper.isNotEmpty(obj.defaultAddress))
	{
		$("#linkName").val(obj.defaultAddress.userName);
		$("#phone").val(obj.defaultAddress.mobile);
		$("#linkAddress").val(obj.defaultAddress.address);
	}
}

/**
 * 处理从产品列表返回的数据
 * @since 1.0, 2017年12月6日 上午11:17:13, zhengby
 */
function getCallInfo_product(obj)
{
	$("#productName").val(obj.name);
}
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
			url : Helper.basePath + "/offer/auto/getOfferPaperList",
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
						str += "<option value='" + val.weight + "' data-tonPrice=" + val.tonPrice + ">" + val.weight + "</option>";
					});
					$this.parent().next().find(".machine_sel").children().remove(); // 删除默认选项
					$this.parent().next().find(".machine_sel").append(str).trigger("change");
				}
			},
			error : function(data)
			{
				Helper.message.warn("请求错误");
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
	if (eqmsg == $field.val())
	{
		$(".btn_dy").trigger("click");
		Helper.message.tips(message, $field);
		$field.focus();
		return false;
	}
	return true;
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
	if (Helper.isEmpty($field.val()))
	{
		$(".btn_dy").trigger("click");
		Helper.message.tips(message, $field);
		$field.focus();
		return false;
	}
	return true;
}