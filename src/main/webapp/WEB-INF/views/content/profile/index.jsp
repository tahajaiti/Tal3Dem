<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="isDonor" value="${sessionScope.user != null && sessionScope.user.userType == 'Donor'}" />
<c:set var="isReceiver" value="${sessionScope.user != null && sessionScope.user.userType == 'Receiver'}" />

<div class="min-h-screen flex flex-col items-center justify-center px-4 py-6">

    <div class="w-full max-w-4xl mb-6">
        <h2 class="text-2xl font-bold text-gray-800">Edit Profile</h2>
    </div>

    <div class="bg-white shadow-md rounded-lg w-full max-w-4xl p-6">

        <form action="${pageContext.request.contextPath}/profile?action=update" method="post" class="space-y-6">
            <div>
                <h3 class="text-lg font-semibold text-gray-800 mb-4 pb-2 border-b">Basic Information</h3>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Name</label>
                        <input type="text" name="name" value="${sessionScope.user.name}"
                               class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500" required>
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Email</label>
                        <input type="email" name="email" value="${sessionScope.user.email}"
                               class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500" required>
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Phone</label>
                        <input type="text" name="phone" value="${sessionScope.user.phone}"
                               class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-gray-500 uppercase mb-1">CIN</label>
                        <input type="text" name="cin" value="${sessionScope.user.cin}"
                               class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Birth Date</label>
                        <input type="date" name="birthDate" value="${sessionScope.user.birthDate}"
                               class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Blood Type</label>
                        <select name="bloodType" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                            <option value="">Select Blood Type</option>
                            <option value="A_POS" ${sessionScope.user.bloodType == 'A_POS' ? 'selected' : ''}>A+</option>
                            <option value="A_NEG" ${sessionScope.user.bloodType == 'A_NEG' ? 'selected' : ''}>A-</option>
                            <option value="B_POS" ${sessionScope.user.bloodType == 'B_POS' ? 'selected' : ''}>B+</option>
                            <option value="B_NEG" ${sessionScope.user.bloodType == 'B_NEG' ? 'selected' : ''}>B-</option>
                            <option value="AB_POS" ${sessionScope.user.bloodType == 'AB_POS' ? 'selected' : ''}>AB+</option>
                            <option value="AB_NEG" ${sessionScope.user.bloodType == 'AB_NEG' ? 'selected' : ''}>AB-</option>
                            <option value="O_POS" ${sessionScope.user.bloodType == 'O_POS' ? 'selected' : ''}>O+</option>
                            <option value="O_NEG" ${sessionScope.user.bloodType == 'O_NEG' ? 'selected' : ''}>O-</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Donor Fields -->
            <c:if test="${isDonor}">
                <div>
                    <h3 class="text-lg font-semibold text-gray-800 mb-4 pb-2 border-b">Donor Information</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                        <div>
                            <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Weight (kg)</label>
                            <input type="number" step="0.1" name="weight" value="${sessionScope.user.weight}"
                                   class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                        </div>

                        <div>
                            <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Last Donation Date</label>
                            <input type="date" name="lastDonationDate" value="${sessionScope.user.lastDonationDate}"
                                   class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                        </div>
                    </div>

                    <div class="mt-6">
                        <h4 class="text-md font-semibold text-gray-700 mb-3">Medical Profile</h4>
                        <div class="grid grid-cols-2 sm:grid-cols-3 gap-4">
                            <label class="inline-flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="isPregnant"
                                       class="w-4 h-4 text-red-500 border-gray-300 rounded focus:ring-red-500"
                                    ${sessionScope.user.medicalProfile.isPregnant ? 'checked' : ''}>
                                <span class="text-sm text-gray-700">Pregnant</span>
                            </label>
                            <label class="inline-flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="isBreastfeeding"
                                       class="w-4 h-4 text-red-500 border-gray-300 rounded focus:ring-red-500"
                                    ${sessionScope.user.medicalProfile.isBreastfeeding ? 'checked' : ''}>
                                <span class="text-sm text-gray-700">Breastfeeding</span>
                            </label>
                            <label class="inline-flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasHiv"
                                       class="w-4 h-4 text-red-500 border-gray-300 rounded focus:ring-red-500"
                                    ${sessionScope.user.medicalProfile.hasHiv ? 'checked' : ''}>
                                <span class="text-sm text-gray-700">HIV</span>
                            </label>
                            <label class="inline-flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasHepatitisB"
                                       class="w-4 h-4 text-red-500 border-gray-300 rounded focus:ring-red-500"
                                    ${sessionScope.user.medicalProfile.hasHepatitisB ? 'checked' : ''}>
                                <span class="text-sm text-gray-700">Hepatitis B</span>
                            </label>
                            <label class="inline-flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasHepatitisC"
                                       class="w-4 h-4 text-red-500 border-gray-300 rounded focus:ring-red-500"
                                    ${sessionScope.user.medicalProfile.hasHepatitisC ? 'checked' : ''}>
                                <span class="text-sm text-gray-700">Hepatitis C</span>
                            </label>
                            <label class="inline-flex items-center gap-2 cursor-pointer">
                                <input type="checkbox" name="hasDiabetes"
                                       class="w-4 h-4 text-red-500 border-gray-300 rounded focus:ring-red-500"
                                    ${sessionScope.user.medicalProfile.hasDiabetes ? 'checked' : ''}>
                                <span class="text-sm text-gray-700">Diabetes</span>
                            </label>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Receiver Fields -->
            <c:if test="${isReceiver}">
                <div>
                    <h3 class="text-lg font-semibold text-gray-800 mb-4 pb-2 border-b">Receiver Information</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-xs font-medium text-gray-500 uppercase mb-1">Urgency</label>
                            <select name="urgency" class="w-full border border-gray-300 p-2.5 rounded-md focus:outline-none focus:ring-1 focus:ring-red-500 focus:border-red-500">
                                <option value="">Select Urgency</option>
                                <option value="CRITICAL" ${sessionScope.user.urgency == 'CRITICAL' ? 'selected' : ''}>Critical</option>
                                <option value="URGENT" ${sessionScope.user.urgency == 'URGENT' ? 'selected' : ''}>Urgent</option>
                                <option value="NORMAL" ${sessionScope.user.urgency == 'NORMAL' ? 'selected' : ''}>Normal</option>
                            </select>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="flex gap-3 pt-4">
                <button type="submit"
                        class="flex-1 bg-red-500 hover:bg-red-600 text-white px-4 py-3 rounded-md font-semibold transition">
                    Save Changes
                </button>
            </div>
        </form>

        <form method="post" action="${pageContext.request.contextPath}/profile?action=delete" class="mt-3">
            <button type="submit"
                    onclick="return confirm('Are you sure you want to delete your account? This cannot be undone!')"
                    class="w-full bg-white hover:bg-gray-50 text-red-600 px-4 py-3 rounded-md font-semibold border-2 border-red-500 transition">
                Delete Account
            </button>
        </form>

    </div>
</div>
