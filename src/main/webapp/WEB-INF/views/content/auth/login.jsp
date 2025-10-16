<div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4">
    <div class="max-w-md w-full bg-white shadow-md rounded-xl p-8">
        <div class="mb-6 text-center">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">Welcome Back</h2>
            <p class="text-gray-600">Log in to access your blood donation account</p>
        </div>

        <!-- Login -->
        <form action="${pageContext.request.contextPath}/auth?action=login" method="post" class="space-y-5">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1" for="email">Email</label>
                <input
                        type="email"
                        name="email"
                        id="email"
                        placeholder="Enter your email"
                        class="w-full border border-gray-300 p-3 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500"
                        required>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1" for="password">Password</label>
                <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="Enter your password"
                        class="w-full border border-gray-300 p-3 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500"
                        required>
            </div>

            <button type="submit" class="w-full bg-red-500 text-white font-semibold py-3 rounded-md hover:bg-red-600 transition-colors">
                Login
            </button>
        </form>

        <p class="text-center mt-6 text-gray-500 text-sm">
            Don’t have an account?
            <a href="${pageContext.request.contextPath}/auth?action=register" class="text-red-500 font-semibold hover:underline">
                Sign Up
            </a>
        </p>
    </div>
</div>
