<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="min-h-screen bg-gray-50 pt-24 pb-12">
    <div class="max-w-3xl mx-auto px-4">
        <div class="bg-white shadow-md rounded-lg p-8">
            <div class="mb-8">
                <h2 class="text-3xl font-bold text-gray-800 mb-2">Create Account</h2>
                <p class="text-gray-600">Join our blood donation community</p>
            </div>

            <form action="${pageContext.request.contextPath}/auth?action=register" method="post" id="registerForm" class="space-y-6">

                <!-- Role Selection -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-3">Account Type *</label>
                    <div class="grid grid-cols-2 gap-4">
                        <label class="cursor-pointer border-2 border-gray-300 rounded-lg p-4 text-center transition-all hover:border-red-500 has-[:checked]:border-red-500 has-[:checked]:bg-red-50">
                            <input type="radio" name="role" value="DONOR" class="hidden role-radio" required>
                            <div class="font-semibold text-gray-800">Donor</div>
                            <div class="text-xs text-gray-600 mt-1">I want to donate blood</div>
                        </label>
                        <label class="cursor-pointer border-2 border-gray-300 rounded-lg p-4 text-center transition-all hover:border-red-500 has-[:checked]:border-red-500 has-[:checked]:bg-red-50">
                            <input type="radio" name="role" value="RECEIVER" class="hidden role-radio">
                            <div class="font-semibold text-gray-800">Receiver</div>
                            <div class="text-xs text-gray-600 mt-1">I need blood donation</div>
                        </label>
                    </div>
                </div>

                <!-- Basic Info -->
                <div class="space-y-4">
                    <h3 class="text-lg font-semibold text-gray-800 border-b pb-2">Personal Information</h3>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Username *</label>
                            <input type="text" name="username" placeholder="Enter username" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500" required>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
                            <input type="email" name="email" placeholder="Enter email" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500" required>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Password *</label>
                            <input type="password" name="password" placeholder="Enter password" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500" required>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
                            <input type="text" name="phone" placeholder="Enter phone number" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">CIN</label>
                            <input type="text" name="cin" placeholder="Enter CIN" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Birth Date</label>
                            <input type="date" name="birthDate" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Gender</label>
                            <select name="gender" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                                <option value="">Select Gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Blood Type</label>
                            <select name="bloodType" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                                <option value="">Select Blood Type</option>
                                <option value="A+">A+</option>
                                <option value="A-">A-</option>
                                <option value="B+">B+</option>
                                <option value="B-">B-</option>
                                <option value="AB+">AB+</option>
                                <option value="AB-">AB-</option>
                                <option value="O+">O+</option>
                                <option value="O-">O-</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Donor Fields -->
                <div id="donorFields" class="hidden space-y-4">
                    <h3 class="text-lg font-semibold text-gray-800 border-b pb-2">Donor Information</h3>

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Weight (kg)</label>
                        <input type="number" name="weight" placeholder="Enter weight" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                    </div>

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">Health Conditions</label>
                        <div class="grid grid-cols-2 md:grid-cols-3 gap-3">
                            <label class="flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="isPregnant" class="w-4 h-4 text-red-500 rounded focus:ring-red-500">
                                <span class="text-sm text-gray-700">Pregnant</span>
                            </label>
                            <label class="flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="isBreastfeeding" class="w-4 h-4 text-red-500 rounded focus:ring-red-500">
                                <span class="text-sm text-gray-700">Breastfeeding</span>
                            </label>
                            <label class="flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasHiv" class="w-4 h-4 text-red-500 rounded focus:ring-red-500">
                                <span class="text-sm text-gray-700">HIV</span>
                            </label>
                            <label class="flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasHepatitisB" class="w-4 h-4 text-red-500 rounded focus:ring-red-500">
                                <span class="text-sm text-gray-700">Hepatitis B</span>
                            </label>
                            <label class="flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasHepatitisC" class="w-4 h-4 text-red-500 rounded focus:ring-red-500">
                                <span class="text-sm text-gray-700">Hepatitis C</span>
                            </label>
                            <label class="flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasDiabetes" class="w-4 h-4 text-red-500 rounded focus:ring-red-500">
                                <span class="text-sm text-gray-700">Diabetes</span>
                            </label>
                        </div>
                    </div>
                </div>

                <!-- Receiver Fields -->
                <div id="receiverFields" class="hidden space-y-4">
                    <h3 class="text-lg font-semibold text-gray-800 border-b pb-2">Receiver Information</h3>

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Urgency Level</label>
                        <select name="urgency" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:border-red-500 focus:ring-1 focus:ring-red-500">
                            <option value="">Select Urgency</option>
                            <option value="CRITICAL">Critical</option>
                            <option value="URGENT">Urgent</option>
                            <option value="NORMAL">Normal</option>
                        </select>
                    </div>
                </div>

                <button type="submit" class="w-full bg-red-500 text-white font-semibold py-3 rounded-md hover:bg-red-600 transition-colors">
                    Register
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    const donorFields = document.getElementById('donorFields');
    const receiverFields = document.getElementById('receiverFields');
    const radios = document.querySelectorAll('.role-radio');

    radios.forEach(radio => {
        radio.addEventListener('change', () => {
            if (radio.value === 'DONOR') {
                donorFields.classList.remove('hidden');
                receiverFields.classList.add('hidden');
            } else {
                receiverFields.classList.remove('hidden');
                donorFields.classList.add('hidden');
            }
        });
    });
</script>