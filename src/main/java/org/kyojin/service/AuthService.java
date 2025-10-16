package org.kyojin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.kyojin.dto.request.AuthRequestDTO;
import org.kyojin.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(AuthRequestDTO dto);
    AuthResponseDTO login(AuthRequestDTO dto, HttpServletRequest req);
    void logout(HttpServletRequest req);

}
