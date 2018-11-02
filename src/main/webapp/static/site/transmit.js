$(function()
{
	/* 选择供应商 */
	$("#supplier_quick_select").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplier_select?multiple=false', '900', '500');
	});
	/* 选择代工平台供应商 */
	$("#supplier_quick_oem").click(function()
	{
		Helper.popup.show('选择供应商', Helper.basePath + '/quick/supplierOem?multiple=false', '900', '500');
	});
	/* 选择客户 */
	$("#customer_quick_select").click(function()
	{
		Helper.popup.show('选择客户', Helper.basePath + '/quick/customer_select?multiple=false', '900', '500');
	});
	/* 选择代工平台客户 */
	$("#customer_quick_oem").click(function()
	{
		Helper.popup.show('选择客户', Helper.basePath + '/quick/customerOem?multiple=false', '900', '500');
	});
	/* 选择产品 */
	$("#product_quick_select").click(function()
	{
		Helper.popup.show('选择产品', Helper.basePath + '/quick/product_select?multiple=false', '900', '500');
	});
	/* 选择工序 */
	$("#procedure_quick_select").click(function()
	{
		Helper.popup.show('选择工序', Helper.basePath + '/quick/procedure_select?multiple=false', '900', '500');
	});
	/* 选择材料 */
	$("#material_quick_select").click(function()
	{
		Helper.popup.show('选择材料', Helper.basePath + '/quick/material_select?multiple=false', '900', '500');
	});

	/* 查询强制完工状态 */
	$("input[name='completeFlag']").change(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
		if ($(this).val() == 'YES')
		{
			$("#btn_complete_cancel").show();
			$("#btn_complete").hide();
			$("#btn_transmit").hide();
			$("#btn_transmit_rotary").hide();
		} else
		{
			$("#btn_complete_cancel").hide();
			$("#btn_complete").show();
			$("#btn_transmit").show();
			$("#btn_transmit_rotary").show();
		}
	});
	// 查询，刷新table
	$("#btn_search").click(function()
	{
		$("#bootTable").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
	});
});

// 供应商返回信息
function getCallInfo_supplier(obj)
{
	$("#supplierName").val(obj.name);
}
// 代工平台供应商返回信息
function getCallInfo_supplierOem(obj)
{
	$("#supplierName").val(obj.name);
}
// 客户返回信息
function getCallInfo_customer(obj)
{
	$("#customerName").val(obj.name);
	$("#customerName").attr('cunstomerId', obj.id)
}
// 代工平台客返回信息
function getCallInfo_customerOem(obj)
{
	$("#customerName").val(obj.name);
	$("#customerName").attr('cunstomerId', obj.id)
}
// 获取产品信息
function getCallInfo_product(obj)
{
	$("#productName").val(obj.name);
}
// 获取工序信息
function getCallInfo_procedure(obj)
{
	$("#procedureName").val(obj.name);
}
// 获取返回信息
function getCallInfo_material(obj)
{
	$("#materialName").val(obj.name);
}

// ----------bootstrap table-----------------
function responseHandler(res)
{
	return {
		rows : res.result,
		total : res.count
	};
}
// 获取选择项
function getSelectedRows()
{
	return $("#bootTable").bootstrapTable('getAllSelections');
}
