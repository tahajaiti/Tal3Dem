package org.kyojin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.kyojin.dto.request.ProfileRequestDTO;
import org.kyojin.dto.response.ProfileResponseDTO;

public interface ProfileService {

    ProfileResponseDTO updateProfile(ProfileRequestDTO dto, HttpServletRequest req);
    ProfileResponseDTO deleteProfile(HttpServletRequest req);
}
