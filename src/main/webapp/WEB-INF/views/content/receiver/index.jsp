<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="isDonor" value="${sessionScope.user != null && sessionScope.user.userType == 'Donor'}" />

<div class="min-h-screen flex flex-col items-center justify-center px-4 py-6">
    <!-- Search -->
    <div class="flex flex-col sm:flex-row justify-between items-center w-full max-w-6xl mb-6 gap-4">
        <h2 class="text-2xl font-bold text-gray-800">Receivers</h2>

        <form method="get" class="flex gap-2 w-full sm:w-auto">
            <input
                    type="text"
                    name="q"
                    placeholder="Search by name..."
                    value="${param.q}"
                    class="border border-gray-300 p-2.5 rounded-md w-full sm:w-64 focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
            <button type="submit" class="bg-red-500 text-white px-4 rounded hover:bg-red-600 transition">
                Search
            </button>
        </form>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto bg-white shadow-md rounded-lg w-full max-w-6xl">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Blood Type</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Urgency</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Phone</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Required Donors</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">State</th>
                <c:if test="${isDonor}">
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Action</th>
                </c:if>
            </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
            <c:forEach var="receiver" items="${receivers}">
                <tr class="hover:bg-red-50 transition">
                    <td class="px-6 py-4 whitespace-nowrap text-gray-800 font-medium">
                        <c:out value="${receiver.name}" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                        <c:out value="${receiver.email}" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                        <c:out value="${receiver.bloodType}" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                        <c:out value="${receiver.urgency}" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                        <c:out value="${receiver.phone}" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                        <c:out value="${receiver.requiredDonors}" />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                        <c:out value="${receiver.state}" />
                    </td>

                    <c:if test="${isDonor}">
                        <td class="px-6 py-4 whitespace-nowrap">
                            <form method="post" action="${pageContext.request.contextPath}/donate">
                                <input type="hidden" name="receiverId" value="${receiver.id}" />
                                <button type="submit" class="text-red-500 hover:underline font-medium bg-transparent border-none p-0">
                                    Donate
                                </button>
                            </form>
                        </td>
                    </c:if>

                </tr>
            </c:forEach>

            <c:if test="${empty receivers}">
                <tr>
                    <td colspan="${isDonor ? 8 : 7}" class="px-6 py-4 text-center text-gray-400">
                        No receivers found
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <!-- Pagination -->
    <div class="flex justify-center mt-6 space-x-2">
        <c:if test="${meta.currentPage > 1}">
            <a href="?page=${meta.currentPage-1}&q=${param.q}"
               class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 transition">
                &laquo; Prev
            </a>
        </c:if>

        <c:forEach begin="1" end="${meta.totalPages}" var="i">
            <a href="?page=${i}&q=${param.q}"
               class="px-3 py-1 rounded font-medium ${i == meta.currentPage ? 'bg-red-500 text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-300'} transition">
                    ${i}
            </a>
        </c:forEach>

        <c:if test="${meta.currentPage < meta.totalPages}">
            <a href="?page=${meta.currentPage+1}&q=${param.q}"
               class="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 transition">
                Next &raquo;
            </a>
        </c:if>
    </div>
</div>
