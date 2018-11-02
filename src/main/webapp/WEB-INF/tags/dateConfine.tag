<%-- 使用方法:<sys:dateConfine/>--%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="initDate" type="java.lang.Boolean" required="false" description="是否初始化日期范围"%>
<%@ attribute name="label" type="java.lang.String" required="true" description="日期范围标签"%>
<%@ attribute name="dateMin" type="java.util.Date" required="false" description="初始最小日期"%>
<%@ attribute name="dateMax" type="java.util.Date" required="false" description="初始最大日期"%>
<%@ attribute name="dateMaxId" type="java.lang.String" required="false" description="最大日期id"%>
<%@ attribute name="dateMinId" type="java.lang.String" required="false" description="最下日期id"%>
<label class="form-label label_ui">${label}：</label>
<span>
	<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'dateMax\')||\'%y-%M-%d\'}'})" class="input-txt input-txt_0 Wdate" 
	<c:if test="${not empty dateMinId }"> id="${dateMinId }" name="${dateMinId }"</c:if>
	<c:if test="${empty dateMinId }">id="dateMin" name="dateMin"</c:if> value="<fmt:formatDate value="${dateMin}" pattern="yyyy-MM-dd"/>" />
</span>

<label class="label_2 align_c">至</label>

<span>
	<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'dateMin\')}'})" class="input-txt input-txt_0 Wdate" 
	<c:if test="${not empty dateMaxId }"> id="${dateMaxId }" name="${dateMaxId }"</c:if>
	<c:if test="${empty dateMaxId }">id="dateMax" name="dateMax"</c:if> value="<fmt:formatDate value="${dateMax}" pattern="yyyy-MM-dd"/>" />
</span>

<script type="text/javascript">
if("${initDate}"=="true"||"${initDate}"=="")
{
    if("${auditflag}"==""){
      //初始化日期区间，默认1个月
        if(Helper.isEmpty($("#dateMin").val())){
        	$("#dateMin").val(new Date().add("m",-1).format('yyyy-MM-dd'));
        }
        if(Helper.isEmpty($("#dateMax").val())){
        	$("#dateMax").val(new Date().format('yyyy-MM-dd'));
        }
    }
  
}


</script>
