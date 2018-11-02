/*点击页签切换表格*/
$(function()
{
	$('.info_item').eq(0).show();// 显示第一个表格
	$('.tab_page_list li').click(function()
	{
		var ind = $(this).index();// 获取li位置
		$('.tab_page_list li').removeClass('active').eq(ind).addClass('active');
		$('.info_item').hide().eq(ind).show();
	})

	/* 选择客户 */
	$("#selectCustomer").click(function()
	{
		Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=true', '800', '480');
	});

	/* 删除 */
	$("table tbody").on("click", "a[name='btn_del']", function()
	{
		$(this).parent().parent().remove();
		resetSequenceNum();
	});

	// 保存
	$("#btn_save").click(function()
	{

		var reg = /^(0|[1-9][0-9]{0,9})(\.[0-9]{1,4})?$/;
		var value = $("#salePrice").val();

		if (Helper.isEmpty($("#name").val()))
		{
			Helper.message.warn("成品名称不能为空")
			return false;
		}
		if (Helper.isNotEmpty(value))
		{
			if (!reg.test(value))
			{
				Helper.message.warn("产品单价只能支持4位小数");
				return false;
			}
		}
		form_submit();
	})
	$(document).on("change", "#upload", function(e)
	{
		var imgBox = e.target;
		var file = imgBox.files[0];
		if (!/image\/\w+/.test(file.type))
		{
			Helper.message.warn("请选择图片文件");
			return false;
		} else if (file.size >= 600 * 1024)
		{
			Helper.message.warn("图片文件不能大于600KB");
			$("#upload").remove();
			$("#delBtn").before('<input type="file" id="upload" name="upfile" style="display: none;">');
			return false;
		} else
		{
			uploadImg($('#show_img'), imgBox)
		}
	});

	// 上传图片按钮
	$("#addBtn").on('click', function()
	{
		$("#upload").click();
	});
	showDelBtn();
	// 删除图片按钮
	$("#delBtn").click(function()
	{
		var fileName = $("#fileName").val();
		var id = $("#id").val();
		Helper.post(Helper.basePath + '/basic/product/delImg?fileName=' + encodeURI(fileName) + '&id=' + id, function(data)
		{
			if (data.success)
			{
				$("#show_img").find("img").remove();
				$("#fileName").val("");
				showDelBtn();
			} else
			{
				Helper.message.warn('删除失败：' + data.message);
			}
			$("#upload").val("");
		})
	})
})

function ajaxUpload()
{
	$("#openWindow").hide();
	var fileM = document.querySelector("#upload");
	var formData = new FormData();
	var fileObj = fileM.files[0];
	formData.append('upfile', fileObj);
	$.ajax({
		type : "post",
		url : Helper.basePath + '/basic/product/upload',
		data : formData,
		cache : false,
		processData : false,
		contentType : false,
		success : function(data)
		{
			var jsonobj = eval("(" + data + ")");
			if (jsonobj.state == "SUCCESS")
			{
				$("#fileName").val(jsonobj.original);// 存文件名
				$("#addBtn").hide();
			} else
			{
				$("#show_img").find("img").remove();
				$("#upload").val("");
				Helper.message.warn('上传失败!' + jsonobj.state);
			}
			showDelBtn();
		}
	})
}

function uploadImg(element, tag)
{
	var file = tag.files[0];
	var imgSrc;
	var reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function()
	{
		imgSrc = this.result;
		var imgs = document.createElement("img");
		$(imgs).attr("src", imgSrc);
		element.append(imgs);
	};
	ajaxUpload();
}

function showDelBtn()
{
	var lenth = $("#show_img").children("img").length;
	var va = $("#show_img").children("img").attr("src");
	if ((va == "" || typeof (va) == "undefined") && lenth <= 1)
	{
		$("#containerDiv").hide();
		$("#addBtn").show();
	} else
	{
		$("#containerDiv").show().on("mouseenter", function()
		{
			$("#delBtn").show();
		}).on("mouseleave", function()
		{
			$("#delBtn").hide();
		})
	}

}

function form_submit()
{
	$("#btn_save").attr({
		"disabled" : "disabled"
	});
	Helper.request({
		url : Helper.basePath + "/basic/product/update",
		data : $("#jsonForm").formToJson(),// 将form序列化成JSON字符串
		success : function(data)
		{
			if (data.success)
			{
				Helper.message.suc('已保存!');
				parent.location.href = Helper.basePath + '/basic/product/list';
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
// 控制单价输入框
// 获取返回产品数组信息
function getCallInfo_customerArray(rows)
{
	if (rows.length > 0)
	{
		appendTr("customerList", rows);
		resetSequenceNum();
	}
}

function appendTr(tableId, rows)
{
	// console.log(rows);
	$.each(rows, function()
	{
		var _THIS = this;
		var _TR = $("<tr/>");
		var idArray = $("table tbody tr td input[name='customerList.id']").map(function()
		{
			return this.value
		}).get();

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
			case 'seq':
				_TR.append('<td></td>');
				break;
			case 'code':
				_TR.append('<td><input name="customerList.code" class="tab_input" type="text" readonly="true" value="' + value + '"/></td>');
				break;
			case 'name':
				_TR.append('<td><input name="customerList.name" class="tab_input" readonly="true" type="text" value="' + value + '"/></td>');
				break;
			case 'operator':
				_TR.append('<td class="td-manage"><input name="customerList.id" type="hidden" readonly="true" value="' + _THIS.id + '"/><a title="删除行" href="javascript:void(0)" name="btn_del"><i class="delete fa fa-trash-o"></i></a></td>');
				break;
			}

		});
		_TR.appendTo("#" + tableId);

	});
}
// 重新设置序号
function resetSequenceNum()
{
	$("table tbody tr").each(function(index)
	{
		$(this).find("td").first().html(++index);
	});
}