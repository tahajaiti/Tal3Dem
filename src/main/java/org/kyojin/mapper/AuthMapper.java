package org.kyojin.mapper;

import jakarta.servlet.http.HttpServletRequest;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.request.AuthRequestDTO;
import org.kyojin.enums.DonorStatus;

import static org.kyojin.util.ParseUtil.*;

@Injectable
public class AuthMapper {

    public static AuthRequestDTO toRequest(HttpServletRequest req) {
        AuthRequestDTO dto = new AuthRequestDTO();

        dto.setUsername(req.getParameter("username"));
        dto.setEmail(req.getParameter("email"));
        dto.setPassword(req.getParameter("password"));
        dto.setPhone(req.getParameter("phone"));
        dto.setCin(req.getParameter("cin"));
        dto.setBirthDate(req.getParameter("birthDate"));
        dto.setBloodType(parseBloodType(req.getParameter("bloodType")));
        dto.setRole(req.getParameter("role"));
        dto.setGender(parseGender(req.getParameter("gender")));

        if ("DONOR".equalsIgnoreCase(dto.getRole())) {
            dto.setWeight(parseDouble(req.getParameter("weight")));
            dto.setIsPregnant(parseBool(req.getParameter("isPregnant")));
            dto.setIsBreastfeeding(parseBool(req.getParameter("isBreastfeeding")));
            dto.setHasHiv(parseBool(req.getParameter("hasHiv")));
            dto.setHasHepatitisB(parseBool(req.getParameter("hasHepatitisB")));
            dto.setHasHepatitisC(parseBool(req.getParameter("hasHepatitisC")));
            dto.setHasDiabetes(parseBool(req.getParameter("hasDiabetes")));
            dto.setStatus(DonorStatus.AVAILABLE);
        } else if ("RECEIVER".equalsIgnoreCase(dto.getRole())) {
            dto.setUrgency(parseUrgency(req.getParameter("urgency")));
        }

        return dto;
    }




}
