<%@ page contentType="text/html;charset=UTF-8" %>

<div class="min-h-screen bg-gray-50 flex flex-col">
    <!-- Hero -->
    <header class="flex-1 flex flex-col justify-center items-center text-center px-4">
        <h1 class="text-4xl md:text-5xl font-bold text-gray-800 mb-4">Join the Life-Saving Community</h1>
        <p class="text-gray-600 mb-8 max-w-xl">Donate or receive blood safely and efficiently. Together, we save lives.</p>
        <div class="flex flex-col sm:flex-row gap-4">
            <a href="${pageContext.request.contextPath}/auth?action=register" class="bg-red-500 text-white px-6 py-3 rounded-md font-semibold hover:bg-red-600 transition">Get Started</a>
            <a href="${pageContext.request.contextPath}/about" class="text-red-500 font-semibold hover:underline">Learn More</a>
        </div>
    </header>

    <!-- Features -->
    <section class="bg-white py-12">
        <div class="max-w-5xl mx-auto px-4 grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="text-center">
                <div class="text-red-500 text-4xl mb-3">🩸</div>
                <h3 class="font-semibold text-lg mb-2">Donate Blood</h3>
                <p class="text-gray-600 text-sm">Easily register as a donor and make a life-saving impact in your community.</p>
            </div>
            <div class="text-center">
                <div class="text-red-500 text-4xl mb-3">⚡</div>
                <h3 class="font-semibold text-lg mb-2">Receive Blood</h3>
                <p class="text-gray-600 text-sm">Request blood quickly and get notified when donors are available.</p>
            </div>
            <div class="text-center">
                <div class="text-red-500 text-4xl mb-3">🤝</div>
                <h3 class="font-semibold text-lg mb-2">Community</h3>
                <p class="text-gray-600 text-sm">Connect with donors and receivers, track your contributions, and save lives together.</p>
            </div>
        </div>
    </section>
</div>
