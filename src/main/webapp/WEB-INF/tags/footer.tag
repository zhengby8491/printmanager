<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="footer">
	Copyright &copy; ${fns:getConfig('SITE_PROJECT_YEAR')}
	<a href="${pageContext.request.contextPath}${fns:getConfig('SITE_BASE_PATH')}">${fns:getConfig('SITE_PROJECT_NAME')}</a>
	- ${fns:getConfig('SITE_PROJECT_VERSION')}
</div>
