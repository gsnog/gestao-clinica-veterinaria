<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty csrfToken}">
	<input type="hidden" name="_csrf" value="${csrfToken}"/>
</c:if>
