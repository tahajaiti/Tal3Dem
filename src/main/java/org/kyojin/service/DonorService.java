package org.kyojin.service;

import org.kyojin.dto.response.PaginationResponseDTO;
import org.kyojin.entity.Donor;


public interface DonorService {
    PaginationResponseDTO<Donor> index(int page, int size, String query);
}
