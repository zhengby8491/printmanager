<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>${projectName}-初始化基础资料</title>
<link rel="stylesheet" type="text/css" href="${ctxHYUI}/src/login.css?v=${v }" />
<meta name="keywords" content="印管家,印管家V2.0,印刷,印刷行业系统,后台管理,企业后台管理系统,ERP">
<meta name="description" content="印管家...">
<script type="text/javascript">
	$(function()
	{
		//所有选择
		$("#select_div input[name='all']").click(function()
		{

			if ($(this).prop("checked"))
			{//选择
				$(this).parents("div.m_all").parent().find("input[type='checkbox']").prop("checked", true);
			} else
			{//不选
				$(this).parents("div.m_all").parent().find("input[type='checkbox']").prop("checked", false);
			}
			refreshResult();
		});
		//所有类别选择
		$("#select_div input[name='classId']").click(function()
		{
			if ($(this).prop("checked"))
			{//选择
				$(this).parents("tr").find("input[type='checkbox']").prop("checked", true);
				$(this).parents("div.m_class").parent().find("input[name='all']").prop("checked", true);
			} else
			{//不选
				$(this).parents("tr").find("input[type='checkbox']").prop("checked", false);
			}
			refreshResult();
		});
		//单个选择
		$("#select_div input[name='itemId']").click(function()
		{
			if ($(this).prop("checked"))
			{//选择
				$(this).parents("tr").find("input[name='classId']").prop("checked", true);
				$(this).parents("div.m_class").parent().find("input[name='all']").prop("checked", true);
			} else
			{//不选
				//不作任何操作
			}
			refreshResult();
		});
		$("#btn_ok").click(function()
		{
			Helper.message.confirm("请确认是否选择完整", function()
			{
				layer.closeAll('dialog');
				form_submit();
			}, function()
			{

			});

		})
	});

	/**
	刷新
	*/
	function refreshResult()
	{
		$("#result_div .m_right_material tbody").empty();
		$("#select_div .m_left_material input[name='classId']:checked").each(function()
		{
			var classTr = $(this);
			var _TR = "<tr>";
			_TR = _TR + '<td class="m_classTitle"><span id="' + $(this).val() + '">' + $(this).next().text() + '</span></td>';
			var material_td = '<td class="m_classItem">';
			classTr.parents("tr").find("input[name='itemId']:checked").each(function(index, item)
			{
				material_td = material_td + '<span id="' + $(item).val() + '">' + $(item).next().text() + '</span> ';
			});
			_TR = _TR + material_td + "</td></tr>";
			$("#result_div .m_right_material tbody").append(_TR);
		});

		$("#result_div .m_right_product tbody").empty();
		$("#select_div .m_left_product input[name='classId']:checked").each(function()
		{
			var classTr = $(this);
			var _TR = "<tr>";
			_TR = _TR + '<td class="m_classTitle"><span id="' + $(this).val() + '">' + $(this).next().text() + '</span></td>';
			var product_td = '<td class="m_classItem">';
			classTr.parents("tr").find("input[name='itemId']:checked").each(function(index, item)
			{
				product_td = product_td + '<span id="' + $(item).val() + '">' + $(item).next().text() + '</span> ';
			});
			_TR = _TR + product_td + "</td></tr>";
			$("#result_div .m_right_product tbody").append(_TR);
		});

		$("#result_div .m_right_procedure tbody").empty();
		$("#select_div .m_left_procedure input[name='classId']:checked").each(function()
		{
			var classTr = $(this);
			var _TR = "<tr>";
			_TR = _TR + '<td class="m_classTitle"><span id="' + $(this).val() + '">' + $(this).next().text() + '</span></td>';
			var procedure_td = '<td class="m_classItem">';
			classTr.parents("tr").find("input[name='itemId']:checked").each(function(index, item)
			{
				procedure_td = procedure_td + '<span id="' + $(item).val() + '">' + $(item).next().text() + '</span> ';
			});
			_TR = _TR + procedure_td + "</td></tr>";
			$("#result_div .m_right_procedure tbody").append(_TR);
		});
	}
	function form_submit()
	{
		var materialVoList = [];
		var productClassIdList = [];
		var procedureVoList = []
		//封装材料信息
		$("#result_div .m_right_material tbody tr ").each(function(index, item)
		{
			materialVoList[index] = {};
			materialVoList[index].materialClassId = $(item).find(".m_classTitle span").attr("id");
			materialVoList[index].materialList = [];
			$(item).find(".m_classItem span").each(function(sub_index, sub_item)
			{
				materialVoList[index].materialList[sub_index] = {};
				materialVoList[index].materialList[sub_index].id = $(sub_item).attr("id");
			});
		});
		//封装产品分类
		$("#result_div .m_right_product tbody tr .m_classItem span").each(function(index, item)
		{
			productClassIdList[index] = $(item).attr("id");
		});
		//封装工序信息
		$("#result_div .m_right_procedure tbody tr").each(function(index, item)
		{
			procedureVoList[index] = {};
			procedureVoList[index].procedureClassId = $(item).find(".m_classTitle span").attr("id");
			procedureVoList[index].procedureList = [];
			$(item).find(".m_classItem span").each(function(sub_index, sub_item)
			{
				procedureVoList[index].procedureList[sub_index] = {};
				procedureVoList[index].procedureList[sub_index].id = $(sub_item).attr("id");
			});
		});
		var cversion = $('input:radio[name="cversion"]:checked').val();
		if (!cversion)
		{
			Helper.message.alert('请选择版本!');
			return;
		}

		var request_json = {
			"materialVoList" : materialVoList,
			"productClassIdList" : productClassIdList,
			"procedureVoList" : procedureVoList,
			"version" : cversion,
		};
		//console.log(request_json);
		$("#btn_ok").attr({
			"disabled" : "disabled"
		});
		Helper.request({
			url : Helper.basePath + "/sys/company/initBasic",
			data : request_json,
			success : function(data)
			{
				if (data.success)
				{
					Helper.message.suc('初始化成功!');
					initUnitConvert();
					location.href = Helper.basePath;
				} else
				{
					Helper.message.warn('初始化失败!' + data.message);
					$("#btn_ok").removeAttr("disabled");
				}
			},
			error : function(data)
			{
				Helper.message.warn('初始化失败!' + data.message);
			}

		});
	}
	// 初始化换算单位信息
	function initUnitConvert()
	{
		Helper.request({
			url : Helper.basePath + "/sys/company/initUnitConvert",
			success : function(data)
			{
				if (data.success)
				{

				} else
				{
					Helper.message.warn('初始化换算单位失败!' + data.message);
				}
			},
			error : function()
			{
				console.log('初始化换算单位失败!代码编号：unitConvert falied');
			}
		})
	}
</script>
</head>
<body>
	<div class="hy_count">
		<!--头部Start-->
		<div class="header">
			<div class="header_wrap">
				<div class="logo">
					<a href="#" style="text-decoration: none;">
						<span class="icon icon-ygj" style="font-size: 85px; color: #02A578;"></span>
					</a>
				</div>
				<div class="info">
					<span class="info_1">欢迎来到印管家！</span>
					<span class="info_2">
						当前在线用户：
						<b>${fns:sessionCount()}</b>
						人
					</span>
				</div>
			</div>
		</div>
		<!--头部End-->
		<!--主要部分Start-->
		<div class="main">
			<div class="main_wrap">
				<!--内容Start-->
				<div class="m_content">
					<div class="cl">
						<!--左start-->
						<div class="m_left" id="select_div">
							<!-- 选择版本 -->
							<div class="cl m_left_material">
								<div class="m_all">
									<label>
										<input type="radio" name="all" checked="checked">
										<span>版本</span>
									</label>
								</div>
								<div class="m_class">
									<table class="m_table">
										<tbody>
											<tr>
												<td class="m_classTitle">
													<label>
														<input type="radio" name="cversion" value="1">
														<span>简易版</span>
													</label>
												</td>
												<td class="m_classItem">
													<label> 生产管理 送货管理 对账管理 收款管理</label>
												</td>
											</tr>
											<tr>
												<td class="m_classTitle">
													<label>
														<input type="radio" name="cversion" value="2" checked="checked">
														<span>标准版</span>
													</label>
												</td>
												<td class="m_classItem">
													<label> 销售管理 生产管理 采购管理 发外管理 库存管理 财务管理</label>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- 选择版本 -->

							<!--左-材料Start-->
							<div class="cl m_left_material">
								<div class="m_all">
									<label>
										<input type="checkbox" name="all" />
										<span>材料</span>
									</label>
								</div>
								<div class="m_class">
									<table class="m_table">
										<tbody>
											<c:forEach items="${fns:publicBasicList('MATERIALCLASS')}" var="item">
												<tr>
													<td class="m_classTitle">
														<label>
															<input type="checkbox" name="classId" value="${item.id }" />
															<span>${item.name }</span>
														</label>
													</td>
													<td class="m_classItem">
														<c:forEach items="${fns:publicBasicListParam('MATERIAL','materialClassId',item.id)}" var="sum_item">
															<label>
																<input type="checkbox" name="itemId" value="${sum_item.id }" />
																<span>${sum_item.weight }克</span>
															</label>
														</c:forEach>
													</td>
												</tr>
											</c:forEach>

										</tbody>
									</table>
								</div>
							</div>
							<!--左-材料End-->
							<!--左-产品Start-->
							<div class="cl m_left_product">
								<div class="m_all">
									<label>
										<input type="checkbox" name="all" />
										<span>产品</span>
									</label>
								</div>
								<div class="m_class">
									<table class="m_table">
										<tbody>
											<tr>
												<td class="m_classTitle">
													<label>
														<input type="checkbox" name="classId" />
														<span>包装</span>
													</label>
												</td>
												<td class="m_classItem">
													<c:forEach items="${fns:publicBasicListParam('PRODUCTCLASS','productType','PACKE')}" var="item">
														<label>
															<input type="checkbox" name="itemId" value="${item.id }" />
															<span>${item.name }</span>
														</label>
													</c:forEach>
												</td>
											</tr>
											<tr>
												<td class="m_classTitle">
													<label>
														<input type="checkbox" name="classId" />
														<span>书刊</span>
													</label>
												</td>
												<td class="m_classItem">
													<c:forEach items="${fns:publicBasicListParam('PRODUCTCLASS','productType','BOOK')}" var="item">
														<label>
															<input type="checkbox" name="itemId" value="${item.id }" />
															<span>${item.name }</span>
														</label>
													</c:forEach>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!--左-产品End-->
							<!--左-工序Start-->
							<div class="cl m_left_procedure">
								<div class="m_all">
									<label>
										<input type="checkbox" name="all" />
										<span>工序</span>
									</label>
								</div>
								<div class="m_class">
									<table class="m_table">
										<tbody>
											<c:forEach items="${fns:publicBasicList('PROCEDURECLASS')}" var="item">
												<tr>
													<td class="m_classTitle">
														<label>
															<input type="checkbox" name="classId" value="${item.id }" />
															<span>${item.name }</span>
														</label>
													</td>
													<td class="m_classItem">
														<c:forEach items="${fns:publicBasicListParam('PROCEDURE','procedureClassId',item.id)}" var="sum_item">
															<label>
																<input type="checkbox" name="itemId" value="${sum_item.id }" />
																<span>${sum_item.name }</span>
															</label>
														</c:forEach>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<!--左-工序End-->
						</div>
						<!--左end-->
						<!--右start-->
						<div class="m_right" id="result_div">
							<!--右-材料Start-->
							<div class="cl m_right_material">
								<h4>材料</h4>
								<div class="m_class">
									<table class="m_table">
										<tbody>


										</tbody>
									</table>
								</div>
							</div>
							<!--右-材料End-->
							<!--右-产品Start-->
							<div class="cl m_right_product">
								<h4>产品</h4>
								<div class="m_class">
									<table class="m_table">
										<tbody>

										</tbody>
									</table>
								</div>
							</div>
							<!--右-产品End-->
							<!--右-工序Start-->
							<div class="cl m_right_procedure">
								<h4>工序</h4>
								<div class="m_class">
									<table class="m_table">
										<tbody>

										</tbody>
									</table>
								</div>
							</div>
							<!--右-工序End-->

						</div>
						<!--右end-->
					</div>
					<div class="m_footer">
						<a class="m_submit m_active loca_btn" href="javascript:void(0)" id="btn_ok">确&nbsp;&nbsp;定 </a>
					</div>
				</div>
				<!--内容End-->
			</div>
		</div>
		<!--主要部分End-->
	</div>
</body>
</html>
