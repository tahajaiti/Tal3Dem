<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
    <div class="max-w-md w-full bg-white shadow-lg rounded-lg p-8 text-center">
        <div class="mb-6">
            <div class="inline-flex items-center justify-center w-16 h-16 bg-red-100 rounded-full mb-4">
                <svg class="w-8 h-8 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                </svg>
            </div>

            <h1 class="text-3xl font-bold text-gray-800 mb-2">
                <c:out value="${title}" />
            </h1>

            <p class="text-gray-600 mb-6">
                <c:out value="${errorMessage}" />
            </p>
        </div>

        <div class="space-y-3">
            <a href="${pageContext.request.contextPath}/"
               class="block w-full bg-red-500 text-white px-4 py-2.5 rounded-md font-medium hover:bg-red-600 transition">
                Go to Homepage
            </a>

            <button onclick="history.back()"
                    class="block w-full bg-gray-100 text-gray-700 px-4 py-2.5 rounded-md font-medium hover:bg-gray-200 transition">
                Go Back
            </button>
        </div>
    </div>
</div>