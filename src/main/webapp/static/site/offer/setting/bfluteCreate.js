/**
 * Author:       THINK
 * Create:       2017年11月1日 上午10:32:48
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init();// 初始化表单提交功能

	/**
	 * 初始化表单提交功能
	 * @since 1.0, 2017年11月1日 上午11:43:45, THINK
	 */
	function init()
	{
		// 判断是批量提交还是单条提交
		var URL = "";
		if ($("#isBatch").val() == "false")
		{
			URL = Helper.basePath + "/offer/setting/saveBflute";
		}
		if ($("#isBatch").val() == "true")
		{
			URL = Helper.basePath + "/offer/setting/saveBfluteByBatch";
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
						if (val.paperQuality != null && val.paperQuality != "" && val.paperQulity != 0)
						{
							var postObj = {};
							postObj['pit'] = sw.pit;
							postObj['paperQuality'] = val.paperQuality;
							postObj['price'] = val.price;
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
							parent.location.href = Helper.basePath + "/offer/setting/bflute?type=" + $("#offerType").val();
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
})
/**
 * 验证数据
 * @since 1.0, 2017年11月21日 上午9:58:37, zhengby
 */
function validate()
{
	try{
		// 坑形
		var $name = $("input[name='pit']");
		// 批量操作
		if($("#isBatch").val()=="true")
		{
			// 校验第一个纸质是否已填写
			var $paperQuality = $("input[name='batchList.paperQuality']").eq(0);
			// 校验第一个单价是否已填写
			var $price =$("input[name='batchList.price']").eq(0);
		}else{
			// 纸质
			var $paperQuality = $("input[name='paperQuality']"); 
			// 单价
			var $price = $("input[name='price']");
		}
		// 坑形
		if (!Helper.validfield.validateFieldText($name, "请填写坑形"))
		{
			throw "";
		}
		// 纸质
		if (!Helper.validfield.validateFieldText($paperQuality, "请填写纸质"))
		{
			throw "";
		}
		if (!Helper.validfield.validateFieldEqText($paperQuality, "0", "请填写纸质"))
		{
			throw "";
		}
		// 单价
		if (!Helper.validfield.validateFieldText($price, "请填写单价"))
		{
			throw "";
		}
		if (!Helper.validfield.validateFieldEqText($price, "0", "请填写单价"))
		{
			throw "";
		}
		// 特殊处理批量填写的验证方法
		$.each($("input[name='batchList.paperQuality']"),function(index,item)
		{
			if($(item).val() != null && $(item).val() != 0)
			{
				$.each($("input[name='batchList.price']"),function(index2,item2)
				{
					if(index2 == index)
					{	
						console.log(index2);
						// 吨价
						if (!Helper.validfield.validateFieldText($(item2), "请填写单价"))
						{
							throw "";
						}
						if (!Helper.validfield.validateFieldEqText($(item2), "0", "请填写单价"))
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