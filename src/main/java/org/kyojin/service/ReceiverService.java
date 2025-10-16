package org.kyojin.service;

import org.kyojin.dto.response.PaginationResponseDTO;
import org.kyojin.entity.Receiver;

public interface ReceiverService {
    PaginationResponseDTO<Receiver> index(int page, int size, String query);
}
