<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${title}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/output.css">
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-50 min-h-screen">

<%-- Header --%>
<jsp:include page="/WEB-INF/views/partials/header.jsp" />

<%-- Messages --%>
<c:if test="${not empty message or not empty error or not empty success}">
    <c:set var="flashType" value="${not empty message ? 'blue' : not empty error ? 'red' : 'green'}" />
    <c:set var="flashText" value="${not empty message ? message : not empty error ? error : success}" />

    <div class="flash-message fixed top-20 right-4 z-50 w-80">
        <div class="bg-${flashType}-50 border border-${flashType}-200 rounded p-3 flex items-center justify-between">
            <p class="text-sm text-${flashType}-800"><c:out value="${flashText}" /></p>
            <button onclick="this.closest('.flash-message').remove()" class="text-${flashType}-600 hover:text-${flashType}-800">×</button>
        </div>
    </div>

    <script>
        const msg = document.querySelector('.flash-message');
        setTimeout(() => msg?.remove(), 5000);
    </script>
</c:if>


<%-- Main --%>
<main>
    <jsp:include page="${content}" />
</main>

</body>
</html>