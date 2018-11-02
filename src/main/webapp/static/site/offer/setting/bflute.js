/**
 * Author:       zhengby
 * Create:       2017年11月1日 上午9:58:14
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{
	init(); // 初始化功能、按钮事件

	/**
	 * 初始化bootStrap表格
	 * @since 1.0, 2017年11月1日 上午10:05:20, zhengby
	 */
	function init()
	{
		// 订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/offer/setting/ajaxBfluteList",
			method : "post",
			contentType : 'application/json', // 设置请求头信息
			dataType : "json",
			pagination : true, // 是否显示分页（*）
			sidePagination : 'server',// 设置为服务器端分页
			pageList : Helper.bootPageList,
			queryParamsType : "",
			pageSize : 10,
			pageNumber : 1,
			queryParams : queryParams,// 参数
			responseHandler : responseHandler,

			// resizable : true, //是否启用列拖动
			showColumns : true, // 是否显示所有的列
			minimumCountColumns : 2, // 最少允许的列数
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : false, // 是否启用排序
			clickToSelect : true, // 是否启用点击选中行
			height : 430, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			cardView : false, // 是否显示详细视图

			// showExport : true,//是否显示导出按钮
			// exportDataType: 'all',
			// exportTypes : [ 'csv', 'txt', 'excel', 'doc' ],
			uniqueId : "id",// 定义列表唯一键
			columns : [ {
				field : 'state',
				checkbox : true,
				visible : true,
				width : 40
			}, {
				field : 'index',
				title : '序号',
				width : 40,
				formatter : function(value, row, index)
				{
					return index + 1;
				}
			}, {
				field : 'pit',
				title : '坑形',
				width : 300
			}, {
				field : 'paperQuality',
				title : '纸质',
				width : 200
			}, {
				field : 'price',
				title : '单价（元/千平方英寸）',
				width : 200
			}, {
				field : 'operator',
				title : '操作',
				formatter : function(value, row, index)
				{
					if (row.id == null)
					{
						return;
					}
					var operator = "";

					if (Helper.basic.hasOfferPermission($("#offerType").val(), "bflute:edit"))
					{
						operator += "<a herf='#' title='编辑' onclick='bfluteEdit(" + row.id + ")'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;";
					}

					if (Helper.basic.hasOfferPermission($("#offerType").val(), "bflute:del"))
					{
						operator += "<a herf='#' title='删除' onclick='bfluteDel(" + row.id + ")'><i class='delete fa fa-trash-o'></i></a>";
					}
					return operator;
				}
			} ],
			onLoadSuccess : function(data)
			{
				if ($(".glyphicon-th").next().html() == '')
				{
					$(".glyphicon-th").after("<span class='glyphicon_font'>筛选</span>");
				}
			},
			onClickRow : function(row, $element)
			{
				$element.addClass("tr_active").siblings().removeClass("tr_active");
			},
			onColumnSwitch : function(field, checked)
			{	
				// 在使用筛选增加或减少列时重置表格列拖动效果
				bootstrapTable_ColDrag($("#bootTable"));
			}
		});
	}
	/**
	 * 查询条件
	 * @since 1.0, 2017年10月30日 下午4:33:49, THINK
	 */
	function queryParams(params)
	{
		params['offerType'] = $("#offerType").val();
		return params;
	}
	/**
	 * 响应处理（默认写上即可）
	 * @since 1.0, 2017年10月30日 下午4:16:22, think
	 */
	function responseHandler(res)
	{
		return {
			rows : res.result,
			total : res.count
		};
	}
});

/**
 * 手动添加材料
 * @since 1.0, 2017年11月01日 下午4:16:22, zhengby
 */
function bfluteCreate(type)
{
	Helper.popup.show('手动添加材料', Helper.basePath + '/offer/setting/bfluteCreate/false?type=' + type, '400', '230');
}

/**
 * 手动批量添加材料
 * @since 1.0, 2017年11月01日 下午4:16:22, zhengby
 */
function bfluteCreateByBatch(type)
{
	Helper.popup.show('手动批量添加材料', Helper.basePath + '/offer/setting/bfluteCreate/true?type=' + type, '650', '220');
}

/**
 * 从材料列表选择添加
 * @since 1.0, 2017年11月01日 下午4:16:22, zhengby
 */
function bfluteCreateByMaterial()
{
	Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=true', '900', '490');
}

/**
 * 编辑
 * @since 1.0, 2017年11月01日 下午4:16:22, zhengby
 */
function bfluteEdit(id)
{
	Helper.popup.show('坑纸设置编辑', Helper.basePath + '/offer/setting/bfluteEdit/' + id, '400', '230');
}

/**
 * 删除
 * @since 1.0, 2017年11月01日 下午4:17:19, zhengby
 */
function bfluteDel(id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		Helper.post(Helper.basePath + '/offer/setting/deleteBflute/' + id, function(data)
		{
			if (data.success)
			{
				$("#bootTable").bootstrapTable("removeByUniqueId", id);
				$("#bootTable").bootstrapTable("refresh");
				Helper.message.suc('已删除!');
			} else
			{
				Helper.message.warn('删除失败：' + data.message);
			}
		});
	});
}

/**
 * 批量删除
 * @since 1.0, 2017年11月01日 下午4:16:22, zhengby
 */
function bfluteDelByBatch()
{
	var rows = $("#bootTable").bootstrapTable('getSelections');
	var ids = $(rows).map(function()
	{
		return this.id;
	}).get();
	if (Helper.isNotEmpty(rows))
	{
		Helper.message.confirm('确认要删除吗？', function(index)
		{
			Helper.post(Helper.basePath + '/offer/setting/deleteBfluteByBatch', {
				"ids[]" : ids
			}, function(data)
			{
				if (data.success)
				{
					$("#bootTable").bootstrapTable("refresh");
					Helper.message.suc('已删除!');
				} else
				{
					Helper.message.warn('删除失败：' + data.message);
				}
			});
		});
	} else
	{
		return Helper.message.warn('至少选择1项');
	}
}

/**
 * 从列表选择材料后返回执行的方法
 * @since 1.0, 2017年11月01日 下午4:19:12, zhengby
 */
function getCallInfo_materialArray(rows)
{
	// 组装新的传递对象
	var postData = new Array();
	$.each(rows, function(index, val)
	{
		var postObj = {};
		postObj['pit'] = val.materialClassName;
		postObj['price'] = (Helper.isNull(val.lastPurchPrice) ? 0 : val.lastPurchPrice);
		postObj['paperQuality'] = val.name;
		postObj['companyId'] = val.companyId;
		postObj['offerType'] = $("#offerType").val();
		postObj['createName'] = val.createName;
		postObj['createTime'] = val.createTime;
		postObj['updateName'] = val.updateName;
		postObj['updateTime'] = val.updateTime;
		postData.push(postObj);
	});
	// 保存选择的材料
	Helper.request({
		url : Helper.basePath + "/offer/setting/saveBfluteByMaterial",
		data : postData,
		success : function(data)
		{
			if (data.success)
			{
				$("#bootTable").bootstrapTable("refreshOptions", {
					pageNumber : 1
				});
			} else
			{
				layer.alert('保存失败：' + data.message);
			}
		}
	})
}
