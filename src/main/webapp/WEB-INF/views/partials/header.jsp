<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="w-full bg-red-500 text-white px-6 py-4 shadow-md fixed top-0 left-0 z-50">
    <div class="max-w-7xl mx-auto flex items-center justify-between">
        <a href="${pageContext.request.contextPath}/" class="text-2xl font-extrabold tracking-wide hover:text-gray-200 transition">
            Tal3Dem
        </a>

        <!-- Desktop  -->
        <ul class="hidden md:flex space-x-8 text-lg font-medium">
            <li><a href="${pageContext.request.contextPath}/receiver" class="hover:text-gray-200 transition">Receivers</a></li>
            <li><a href="${pageContext.request.contextPath}/donor" class="hover:text-gray-200 transition">Donors</a></li>
        </ul>

        <div class="hidden md:flex items-center space-x-4">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span class="text-sm font-medium">Welcome, <c:out value="${sessionScope.user.name}" /></span>
                    <a href="${pageContext.request.contextPath}/profile"
                       class="px-4 py-2 rounded-lg bg-gray-700 text-white font-semibold hover:bg-red-800 transition">
                        Profile
                    </a>
                    <form action="${pageContext.request.contextPath}/auth?action=logout" method="post" class="inline">
                        <button type="submit"
                                class="px-4 py-2 rounded-lg bg-red-700 text-white font-semibold hover:bg-red-800 transition">
                            Logout
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/auth?action=login"
                       class="px-4 py-2 rounded-lg bg-white text-red-500 font-semibold hover:bg-gray-100 transition">
                        Login
                    </a>
                    <a href="${pageContext.request.contextPath}/auth?action=register"
                       class="px-4 py-2 rounded-lg bg-yellow-400 text-red-900 font-semibold hover:bg-yellow-300 transition">
                        Sign Up
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Mobile Menu -->
        <button id="menu-btn" class="md:hidden flex items-center text-white focus:outline-none">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2"
                 viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
        </button>
    </div>

    <!-- Mobile Menu -->
    <div id="mobile-menu" class="hidden md:hidden mt-2 space-y-2 text-center">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <span class="block text-sm font-medium pb-2 border-b border-red-400">Welcome, <c:out value="${sessionScope.user.name}" /></span>
                <a href="${pageContext.request.contextPath}/receiver" class="block hover:text-gray-200 transition">Receivers</a>
                <a href="${pageContext.request.contextPath}/donor" class="block hover:text-gray-200 transition">Donors</a>
                <a href="${pageContext.request.contextPath}/profile" class="block hover:text-gray-200 transition">Profile</a>
                <form action="${pageContext.request.contextPath}/auth?action=logout" method="post" class="mt-2">
                    <button type="submit"
                            class="w-full px-4 py-2 rounded-lg bg-red-700 text-white font-semibold hover:bg-red-800 transition">
                        Logout
                    </button>
                </form>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/receiver" class="block hover:text-gray-200 transition">Receivers</a>
                <a href="${pageContext.request.contextPath}/donor" class="block hover:text-gray-200 transition">Donors</a>
                <a href="${pageContext.request.contextPath}/auth?action=login"
                   class="block mx-auto w-32 px-4 py-2 mt-2 bg-white text-red-500 rounded-lg hover:bg-gray-100 transition">
                    Login
                </a>
                <a href="${pageContext.request.contextPath}/auth?action=register"
                   class="block mx-auto w-32 px-4 py-2 mt-2 bg-yellow-400 text-red-900 rounded-lg hover:bg-yellow-300 transition">
                    Sign Up
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<script>
    document.getElementById('menu-btn').addEventListener('click', function() {
        document.getElementById('mobile-menu').classList.toggle('hidden');
    });
</script>
