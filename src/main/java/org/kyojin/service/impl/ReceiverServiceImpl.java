package org.kyojin.service.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Inject;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.response.PaginationResponseDTO;
import org.kyojin.entity.Receiver;
import org.kyojin.repository.ReceiverRepository;
import org.kyojin.service.ReceiverService;

import java.util.List;

@Injectable
@Implementation(ReceiverService.class)
public class ReceiverServiceImpl implements ReceiverService {

    @Inject private ReceiverRepository receiverRepo;

    public PaginationResponseDTO<Receiver> index(int page, int size, String query) {
        List<Receiver> receivers;

        if (query == null || query.isBlank()) {
            receivers = receiverRepo.findAll(page, size);
        } else {
            receivers = receiverRepo.search(query, page, size);
        }

        long totalItems = receiverRepo.count();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return new PaginationResponseDTO<>(receivers, new PaginationResponseDTO.Metadata(page, totalPages, totalItems, size));
    }

}
