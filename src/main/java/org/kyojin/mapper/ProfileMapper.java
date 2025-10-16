package org.kyojin.mapper;

import jakarta.servlet.http.HttpServletRequest;
import org.kyojin.dto.request.ProfileRequestDTO;
import org.kyojin.entity.*;
import org.kyojin.enums.*;
import org.kyojin.service.SessionService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileMapper {

    public static User toEntity(HttpServletRequest req, ProfileRequestDTO dto, SessionService sessionService) {
        User user = (User) sessionService.getUser(req);
        if (user == null) return null;

        mapCommonFields(user, dto);

        if (user instanceof Donor donor) {
            mapDonorFields(donor, dto);
        } else if (user instanceof Receiver receiver) {
            mapReceiverFields(receiver, dto);
        }

        return user;
    }

    public static ProfileRequestDTO fromRequest(HttpServletRequest req) {
        ProfileRequestDTO dto = new ProfileRequestDTO();

        dto.setName(req.getParameter("name"));
        dto.setEmail(req.getParameter("email"));
        dto.setPhone(req.getParameter("phone"));
        dto.setCin(req.getParameter("cin"));
        dto.setBloodType(req.getParameter("bloodType"));

        String birthDateStr = req.getParameter("birthDate");
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            try {
                Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateStr);
                dto.setBirthDate(birthDate);
            } catch (Exception ignored) {}
        }

        String weightStr = req.getParameter("weight");
        if (weightStr != null && !weightStr.isEmpty()) {
            try {
                dto.setWeight(Double.parseDouble(weightStr));
            } catch (NumberFormatException ignored) {}
        }

        dto.setIsPregnant(parseBoolean(req.getParameter("isPregnant")));
        dto.setIsBreastfeeding(parseBoolean(req.getParameter("isBreastfeeding")));
        dto.setHasHiv(parseBoolean(req.getParameter("hasHiv")));
        dto.setHasHepatitisB(parseBoolean(req.getParameter("hasHepatitisB")));
        dto.setHasHepatitisC(parseBoolean(req.getParameter("hasHepatitisC")));
        dto.setHasDiabetes(parseBoolean(req.getParameter("hasDiabetes")));

        dto.setUrgency(req.getParameter("urgency"));

        return dto;
    }

    private static Boolean parseBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equals("1");
    }

    private static void mapCommonFields(User user, ProfileRequestDTO dto) {
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getCin() != null) user.setCin(dto.getCin());
        if (dto.getBirthDate() != null) user.setBirthDate(dto.getBirthDate());
        if (dto.getBloodType() != null) user.setBloodType(BloodType.valueOf(dto.getBloodType()));
    }

    private static void mapDonorFields(Donor donor, ProfileRequestDTO dto) {
        if (dto.getWeight() != null) donor.setWeight(dto.getWeight());
        if (donor.getMedicalProfile() == null) return;

        if (dto.getIsPregnant() != null) donor.getMedicalProfile().setIsPregnant(dto.getIsPregnant());
        if (dto.getIsBreastfeeding() != null) donor.getMedicalProfile().setIsBreastfeeding(dto.getIsBreastfeeding());
        if (dto.getHasHiv() != null) donor.getMedicalProfile().setHasHiv(dto.getHasHiv());
        if (dto.getHasHepatitisB() != null) donor.getMedicalProfile().setHasHepatitisB(dto.getHasHepatitisB());
        if (dto.getHasHepatitisC() != null) donor.getMedicalProfile().setHasHepatitisC(dto.getHasHepatitisC());
        if (dto.getHasDiabetes() != null) donor.getMedicalProfile().setHasDiabetes(dto.getHasDiabetes());
    }

    private static void mapReceiverFields(Receiver receiver, ProfileRequestDTO dto) {
        if (dto.getUrgency() != null) receiver.setUrgency(Urgency.valueOf(dto.getUrgency()));
    }
}
