/**
 * Author:       THINK
 * Create:       2017年10月30日 上午10:03:24
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
$(function()
{

	init(); // 初始化功能、按钮事件

	/**
	 * 初始化bootStrap表格
	 * @since 1.0, 2017年10月30日 上午10:11:51, THINK
	 */
	function init()
	{
		// 订单详情table
		$("#bootTable").bootstrapTable({
			url : Helper.basePath + "/offer/setting/ajaxPaperList",
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
			detailView : false, // 是否显示父子表

			// cookie : true,// 是否启用COOKIE
			// cookiesEnabled : [ 'bs.table.columns' ],// 默认会记住很多属性，这里控制只记住列选择属性
			// cookieIdTable : "paper_setting",// 必须制定唯一的表格cookieID

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
				field : 'name',
				title : '材料分类',
				width : 300
			}, {
				field : 'weight',
				title : '克重（g）',
				width : 200
			}, {
				field : 'tonPrice',
				title : '吨价（元）',
				width : 200
			}, {
				field : 'isPageTurn',
				title : '是否自翻版',
				width : 200,
				formatter : function(value, row, index)
				{
					if (value == "YES")
					{
						return "是";
					}
					if (value = "NO")
					{
						return "否";
					}
				}
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

					if (Helper.basic.hasOfferPermission($("#offerType").val(), "paper:edit"))
					{
						operator += "<a herf='#' title='编辑' onclick='paperEdit(" + row.id + ")'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;";
					}
					
					if (Helper.basic.hasOfferPermission($("#offerType").val(), "paper:del"))
					{
						operator += "<a herf='#' title='删除' onclick='paperDel(" + row.id + ")'><i class='delete fa fa-trash-o'></i></a>";
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
})

/**
 * 手动添加材料
 * @since 1.0, 2017年11月1日 上午11:39:58, THINK
 */
function paperCreate(type)
{
	Helper.popup.show('手动添加材料', Helper.basePath + '/offer/setting/paperCreate/false?type=' + type, '400', '250');
}

/**
 * 手动批量添加材料
 * @since 1.0, 2017年10月30日 上午11:40:31, THINK
 */
function paperCreateByBatch(type)
{
	Helper.popup.show('手动批量添加材料', Helper.basePath + '/offer/setting/paperCreate/true?type=' + type, '650', '250');
}

/**
 * 从材料列表选择材料
 * @since 1.0, 2017年10月30日 上午11:40:43, THINK
 */
function paperCreateByMaterial()
{
	Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=true', '900', '490');
}

/**
 * 纸张设置编辑
 * @since 1.0, 2017年10月30日 下午2:02:09, THINK
 */
function paperEdit(id)
{
	Helper.popup.show('纸张设置编辑', Helper.basePath + '/offer/setting/paperEdit/' + id, '400', '250');
}

/**
 * 纸张设置删除
 * @since 1.0, 2017年10月30日 下午2:02:24, THINK
 */
function paperDel(id)
{
	Helper.message.confirm('确认要删除吗？', function(index)
	{
		Helper.post(Helper.basePath + '/offer/setting/deletePaper/' + id, function(data)
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
 * @since 1.0, 2017年11月2日 上午11:41:44, THINK
 */
function paperDelByBatch()
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
			Helper.post(Helper.basePath + '/offer/setting/deletePaperByBatch', {
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
	}else
	{
		return Helper.message.warn('至少选择1项');
	}
}


/**
 * 从列表选择材料后返回执行的方法
 * @since 1.0, 2017年10月30日 下午2:03:20, THINK
 */
function getCallInfo_materialArray(rows)
{
	// 组装新的传递对象
	var postData = new Array();
	$.each(rows, function(index, val)
	{
		var postObj = {};
		postObj['name'] = val.materialClassName;
		postObj['weight'] = val.weight;
		postObj['isPageTurn'] = "YES";
		postObj['companyId'] = val.companyId;
		postObj['offerType'] = $("#offerType").val();
		postObj['tonPrice'] = (Helper.isNull(val.lastPurchPrice)? 0 : val.lastPurchPrice);
		postObj['createName'] = val.createName;
		postObj['createTime'] = val.createTime;
		postObj['updateName'] = val.updateName;
		postObj['updateTime'] = val.updateTime;
		postData.push(postObj);
	});
	// 保存选择的材料
	Helper.request({
		url : Helper.basePath + "/offer/setting/savePaperByMaterial",
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