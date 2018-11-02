$(function()
{
	try
	{
		// 监听打开代工平台
		var $ss = $("#isOem").select2({
			language : Helper.locale,
			minimumResultsForSearch : 8
		});
		$ss.on("select2:selecting", function(e)
		{
			e.preventDefault();
			var _choose = $(this).val();
			if (_choose === "NO")
			{
				Helper.message.confirm("启用代工平台将会把公司作为代加工商展示给其它印管家客户，是否启用？", function(index)
				{
					$ss.val("YES").trigger("change");
					layer.close(index);
				}, function(index)
				{
					e.preventDefault();
				});
			} else if (_choose === "YES")
			{
				Helper.message.confirm("关闭代工平台将会关闭其它印管家客户外发至本公司的加工单，是否关闭？", function(index)
				{
					$ss.val("NO").trigger("change");
					layer.close(index);
				}, function(index)
				{
					e.preventDefault();
				});
			}
		});
	} catch (e)
	{
		// blank
	}

	$("#form-company").validate({
		submitHandler : function(form)
		{
			// 必须写在验证前面，否则无法ajax提交
			Helper.request({// 验证新增是否成功
				url : Helper.basePath + "/sys/company/update",
				data : $(form).formToJson(),
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/sys/company/list";
					} else
					{
						Helper.message.warn('操作失败：' + data.message);
					}
				},
				error : function(data)
				{
					Helper.message.warn('操作失败：' + data);
				}
			});
		},
		rules : {
			name : {
				required : true,
				remote : {
					type : "POST",
					url : Helper.basePath + '/sys/company/existForEdit',
					dataType : "json",
					data : {
						userName : function()
						{
							return $("#name").val();
						}
					}
				}
			},
			tel : {
				required : true
			},
			linkName : {
				required : true
			}
		},
		messages : {
			name : {
				required : "请输入公司名",
				remote : "公司名已经存在"
			},
			tel : {
				required : "请输入公司电话"
			},
			linkName : {
				required : "请输入联系人"
			}
		}
	/*
	 * , errorPlacement : function(error, element) {// 自定义提示错误位置
	 * layer.msg(error.html()); }
	 */
	});
});
