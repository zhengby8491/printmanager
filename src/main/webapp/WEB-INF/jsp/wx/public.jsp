<meta charset="utf-8" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<meta name="keywords" content="印刷,印管家">
<meta name="description" content="印刷">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld"%>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld"%>
<%@ taglib prefix="phtml" uri="/WEB-INF/tlds/html.tld"%>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags"%>

<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="serverName" value="<%=request.getServerName() %>" />
<c:set var="projectName" value="${fns:getConfig('SITE_PROJECT_NAME')}" />
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getConfig('SITE_BASE_PATH')}" />
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static" />
<c:set var="v" value="${fns:getConfig('SITE_RESOURCE_VERSION')}" />
<c:set var="loginUser" value="${fns:getUser()}" />

<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/zepto/zepto.min.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/zepto/touch.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/zepto/fx.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/zepto/fx_methods.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/doT/doT.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/sui/sm.min.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/plugin/sui/sm-extend.min.js' charset='utf-8'></script>
<script type='text/javascript' src='${ctxStatic}/wx/js/common/HYWX.js?v=${v }' charset='utf-8'></script>
<%-- <script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_index.js' charset='utf-8'></script> --%>
<link rel="stylesheet" href="${ctxStatic}/wx/css/sm.min.css">
<link rel="stylesheet" href="${ctxStatic}/wx/css/sm-extend.min.css">
<link rel="stylesheet" href="${ctxStatic}/wx/css/iconfont.css">
<link rel="stylesheet" href="${ctxStatic}/wx/css/wx.css?v=${v }">
<script type="text/javascript">
	HYWX.basePath = "${ctx}";
</script>

