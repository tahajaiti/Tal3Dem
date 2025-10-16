package org.kyojin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.kyojin.dto.request.DonationRequestDTO;
import org.kyojin.dto.response.DonationResponseDTO;

public interface DonationService {
    DonationResponseDTO makeDonation(DonationRequestDTO requestDTO, HttpServletRequest req);
}
