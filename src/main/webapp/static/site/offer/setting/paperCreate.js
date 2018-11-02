/**
 * Author:       THINK
 * Create:       2017年10月30日 下午2:19:48
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init();// 初始化表单提交功能

	/**
	 * 初始化表单提交功能
	 * @since 1.0, 2017年10月31日 上午11:13:21, THINK
	 */
	function init()
	{
		// 判断是批量提交还是单条提交
		var URL = "";
		if ($("#isBatch").val() == "false")
		{
			URL = Helper.basePath + "/offer/setting/savePaper";
		}
		if ($("#isBatch").val() == "true")
		{
			URL = Helper.basePath + "/offer/setting/savePaperByBatch";
		}
		
		// 表单提交
		$("#jsonForm").validate({
			submitHandler : function(form)
			{
				if(!validate())
				{
					return
				}
				var sw = $(form).formToJson();
				var postData;
				// 如果是单条保存
				if ($("#isBatch").val() == "false")
				{
					postData = sw;
				}
				// 如果是批量保存
				if ($("#isBatch").val() == "true")
				{
					// 组装新的传递对象
					postData = new Array();
					$.each(sw.batchList, function(index, val)
					{
						if (val.weight != null && val.weight != "" && val.weight != 0)
						{
							var postObj = {};
							postObj['name'] = sw.name;
							postObj['weight'] = val.weight;
							postObj['tonPrice'] = val.tonPrice;
							postObj['isPageTurn'] = "YES";
							postObj['offerType'] = $("#offerType").val();
							postData.push(postObj);
						}
					});
				}
				// 必须写在验证前面，否则无法ajax提交
				Helper.request({
					url : URL,
					data : postData,
					success : function(data)
					{
						if (data.success)
						{
							parent.location.href = Helper.basePath + "/offer/setting/paper?type=" + $("#offerType").val();
						} else
						{
							layer.alert('创建失败：' + data.message);
						}
					}
				});
			},
			onkeyup : false,
			focusCleanup : true
		});
	}
	;
})

/**
 * 验证数据
 * @since 1.0, 2017年11月21日 上午9:58:37, zhengby
 */
function validate()
{
	try{
		// 材料名称
		var $name = $("input[name='name']");
		// 批量操作
		if($("#isBatch").val()=="true")
		{
			// 校验第一个克重是否已填写
			var $weight = $("input[name='batchList.weight']").eq(0);
			// 校验第一个吨价是否已填写
			var $tonPrice =$("input[name='batchList.tonPrice']").eq(0);
		}else{
			// 克重
			var $weight = $("input[name='weight']"); 
			// 吨价
			var $tonPrice = $("input[name='tonPrice']");
		}
		// 材料名称
		if (!Helper.validfield.validateFieldText($name, "请填写材料名称"))
		{
			throw "";
		}
		// 克重
		if (!Helper.validfield.validateFieldText($weight, "请填写材料克重"))
		{
			throw "";
		}
		if (!Helper.validfield.validateFieldEqText($weight, "0", "请填写材料克重"))
		{
			throw "";
		}
		// 吨价
		if (!Helper.validfield.validateFieldText($tonPrice, "请填写材料吨价"))
		{
			throw "";
		}
		if (!Helper.validfield.validateFieldEqText($tonPrice, "0", "请填写材料吨价"))
		{
			throw "";
		}
		// 特殊处理批量填写的验证方法
		$.each($("input[name='batchList.weight']"),function(index,item)
		{
			if($(item).val() != null && $(item).val() != 0)
			{
				$.each($("input[name='batchList.tonPrice']"),function(index2,item2)
				{
					if(index2 == index)
					{	
						console.log(index2);
						// 吨价
						if (!Helper.validfield.validateFieldText($(item2), "请填写材料吨价"))
						{
							throw "";
						}
						if (!Helper.validfield.validateFieldEqText($(item2), "0", "请填写材料吨价"))
						{
							throw "";
						}
					}
				})
			}
		})
	}catch(e)
	{
		return false;
	}
	return true;
}
