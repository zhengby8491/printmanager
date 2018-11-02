<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<script type="text/javascript" src="${ctxStatic}/site/sys/permissions.js?v=${v }"></script>
<title>公司权限</title>
<script type="text/javascript">
	$(function()
	{
		$("#btn_save").click(function()
		{
			$("#btn_save").attr("disabled", "disabled");
			var menuIdList = $("table input[type='checkbox']:checked").map(function()
			{
				return $(this).val();
			}).get();
			Helper.Remote.request({
				url : Helper.basePath + "/sys/company/permissions_save",
				data : {
					id : $("#companyId").val(),
					menuIdList : menuIdList
				},
				success : function(data)
				{
					if (data.success)
					{
						parent.location.href = Helper.basePath + "/sys/company/list";
					} else
					{
						Helper.message.warn('操作失败：' + data.message);
						$("#btn_save").removeAttr("disabled");
					}
				},
				error : function(data)
				{
					Helper.message.warn('操作失败：' + data);
					$("#btn_save").removeAttr("disabled");
				}
			});
		});
	});
</script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<input type="hidden" id="companyId" value="${companyId }" />
				<div class="row-div">
					<label class="form-label label_ui label_2"></label>
					<input class="nav_btn table_nav_btn" type="button" id="btn_save" value="提交&nbsp;&nbsp;">
				</div>
			</div>

			<div id="node" class="table_cont">
				<input type="checkbox" id="checkbox_all" />
				<label for="checkbox_all" class="c-red">全选</label>
				<c:forEach items="${allMenuList}" var="node1">
					<table class="border-table first_table" coldrag="false" style="margin-bottom: 20px">
						<tr>
							<td rowspan="${fn:length(node1.childrens)+1}" width="100" class="node1">
								<span>
									<input type="checkbox" value="${node1.id }" id="${ node1.id }" <c:if test="${not empty hasMenuIdMap[node1.id]}"> checked</c:if> />
									<label for="${ node1.id }">${node1.name }</label>
								</span>
							</td>
						</tr>
						<c:forEach items="${node1.childrens}" var="node2">
							<tr>
								<td>
									<table class="second_table" coldrag="false" rules="all">
										<tr>
											<td rowspan="${fn:length(node2.childrens)+1}" width="120" class="node2">
												<span>
													<input type="checkbox" value="${node2.id }" id="${ node2.id }" <c:if test="${not empty hasMenuIdMap[node2.id]}"> checked</c:if> />
													<label for="${ node2.id }">${node2.name }</label>
												</span>
											</td>
										</tr>
										<c:forEach items="${node2.childrens}" var="node3">
											<tr>
												<td width="160" align="left" class="node3">
													<span>
														<input type="checkbox" value="${node3.id }" id="${ node3.id }" <c:if test="${not empty hasMenuIdMap[node3.id]}"> checked</c:if> />
														<label for="${node3.id }">${node3.name }</label>
													</span>
												</td>
												<td align="left" class="node4">
													<c:forEach items="${node3.childrens}" var="node4">
														<!-- 如果还有childrens，则需要新开table并设置rowspan -->
														<c:if test="${not empty node4.childrens }">
															<table class="third_table" coldrag="false" rules="all" style="border: 1px solid #ddd;">
																<tr>
																	<td rowspan="2" width="120" class="node4">
																		<span>
																			<input type="checkbox" value="${node4.id }" id="${ node4.id }" <c:if test="${not empty hasMenuIdMap[node4.id]}"> checked</c:if> />
																			<label for="${ node4.id }">${node4.name }</label>
																		</span>
																	</td>
																</tr>
																<c:if test="${not empty node4.childrens }">
																	<tr>
																		<td align="left" class="node5">
																			<c:forEach items="${node4.childrens}" var="node5">
																				<span>
																					<input type="checkbox" value="${node5.id }" id="${ node5.id }" <c:if test="${not empty hasMenuIdMap[node5.id]}"> checked</c:if> />
																					<label for="${node5.id }">${node5.name }</label>
																				</span>
																			</c:forEach>
																		</td>
																	</tr>
																</c:if>
															</table>
														</c:if>
														<!-- 不在有子节点直接显示 -->
														<c:if test="${empty node4.childrens }">
															<span>
																<input type="checkbox" value="${node4.id }" id="${ node4.id }" <c:if test="${not empty hasMenuIdMap[node4.id]}"> checked</c:if> />
																<label for="${ node4.id }">${node4.name }</label>
															</span>
														</c:if>
													</c:forEach>
												</td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:forEach>

			</div>
		</div>
	</div>
</body>
</html>