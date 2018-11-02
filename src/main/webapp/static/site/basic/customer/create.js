$(function()
{
	/*点击页签切换表格*/
	$('.info_item').eq(0).show();//显示第一个表格
  $('.tab_page_list li').click(function() {
    var ind = $(this).index();//获取li位置
    $('.tab_page_list li').removeClass('active').eq(ind).addClass('active');
    $('.info_item').hide().eq(ind).show();
  })
	
	$(document).on('keypress', "input[name='addressList.qq']", function()
	{
		return (/\d/.test(String.fromCharCode(event.keyCode)));
	});
	$(document).on('blur', "input[name='addressList.qq']", function()
	{
		if (!(/^\d*$/.test($(this).val())))
		{
			$(this).val("");
		}
	});
	$(document).on("click", "tr td a[name='btn_del']", function()
	{
		$(this).parents("tr").remove();
		// 判断是删除的联系人信息 还是 客户收款单位
		if ($(this).parent().parent().find("input[name*='.isDefault']").attr("name") == "addressList.isDefault")
		{
			$("input[name='address_isDefault']").eq(0).trigger("click");
			resetSequenceNum($("#customer_address"));
		} else
		{
			$("input[name='payer_isDefault']").eq(0).trigger("click");
			resetSequenceNum($("#customer_payer"));
		}
	});

	// 添加联系人信息
	$(document).on("click", "#addtr_address", function()
	{
		linkTr().appendTo("#customer_address");
		resetSequenceNum($("#customer_address"));
		var isDefault = false;
		$.each($("input[name='addressList.isDefault']"), function()
		{
			if ($(this).val() == "YES")
			{
				isDefault = true;
			}
		})
		if (isDefault == false)
		{
			$("input[name='address_isDefault']").eq(0).trigger("click");
		}

	});
	// 客户收款单位信息
	$(document).on("click", "#addtr_payer", function()
	{
		payerTr().appendTo("#customer_payer");
		resetSequenceNum($("#customer_payer"));
		var isDefault = false;
		$.each($("input[name='payerList.isDefault']"), function()
		{
			if ($(this).val() == "YES")
			{
				isDefault = true;
			}
		})
		if (isDefault == false)
		{
			$("input[name='payer_isDefault']").eq(0).trigger("click");
		}

	});
	// 选择默认_联系人
	$(document).on("click", "input[name='address_isDefault']", function()
	{
		$(this).parents().parents().find("input[name='addressList.isDefault']").val("NO");
		$(this).prev().val("YES");
	});
	// 选择默认_客户收款单位
	$(document).on("click", "input[name='payer_isDefault']", function()
	{
		$(this).parents().parents().find("input[name='payerList.isDefault']").val("NO");
		$(this).prev().val("YES");
	});
});

function checkData()
{
	fixEmptyValue();
	var flag = true;
	$("input[name='addressList.userName']").each(function()
	{
		if ($(this).val() == "")
		{
			Helper.message.warn('联系人不能为空！')
			flag = false;
			return false;
		}
	});
	$("input[name='addressList.address']").each(function()
	{
		if ($(this).val() == "")
		{
			Helper.message.warn('地址不能为空！')
			flag = false;
			return false;
		}
	});
	$("input[name='payerList.name']").each(function()
	{
		if ($(this).val() == "")
		{
			Helper.message.warn('收款单位不能为空！')
			flag = false;
			return false;
		}
	});
	if (Helper.isEmpty($("#name").val()))
	{
		Helper.message.warn("客户名称不能为空");
		flag = false;
		return false;
	}
	if (flag)
	{
		form_submit();
	}
}

function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	Helper.request({
		url : Helper.basePath + "/basic/customer/save",
		data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				parent.location.href = Helper.basePath + '/basic/customer/list/';
			} else
			{
				Helper.message.warn('保存失败!' + data.message);
				$("#btn_save").removeAttr("disabled");
			}
		},
		error : function(data)
		{
			Helper.message.warn('保存失败!' + data.message);
		}
	});
}

// 重新设置序号
function resetSequenceNum(obj)
{
	$(obj).find("tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}
// 联系人信息 行
function linkTr()
{
	var _TR = $("<tr><td></td>");
	_TR.append("<td><input type='hidden' name='addressList.isDefault' value='NO'/><input type='radio'  name='address_isDefault'/></td>");
	_TR.append("<td><input class='tab_input' name='addressList.userName' type='text' /></td>");
	_TR.append("<td><input class='tab_input' name='addressList.address' type='text' /></td>");
	_TR.append("<td><input class='tab_input' name='addressList.mobile' type='text' /></td>");
	_TR.append("<td><input class='constraint_email tab_input' name='addressList.email' type='text' /></td>");
	_TR.append("<td><input class='tab_input' name='addressList.qq' type='text' /></td>");
	_TR.append("<td><a title='删除' href='javascript:;' name='btn_del'\"><i class='fa fa-trash-o'></i></a></td></tr>");
	return _TR;
}
// 客户收款单位 行
function payerTr()
{
	var _TR = $("<tr><td></td>");
	_TR.append("<td><input type='hidden' name='payerList.isDefault' value='NO'/><input type='radio' name='payer_isDefault'/></td>");
	_TR.append("<td><input type='text' name='payerList.name' class='tab_input' /></td>");
	_TR.append("<td><a title='删除' href='javascript:;' name='btn_del'><i class='fa fa-trash-o'></i></a></td></tr>");
	return _TR;
}