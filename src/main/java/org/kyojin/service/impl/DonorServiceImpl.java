package org.kyojin.service.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Inject;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.response.PaginationResponseDTO;
import org.kyojin.entity.Donor;
import org.kyojin.repository.DonorRepository;
import org.kyojin.service.DonorService;

import java.util.List;

@Injectable
@Implementation(DonorService.class)
public class DonorServiceImpl implements DonorService {
    @Inject
    private DonorRepository donorRepo;

    public PaginationResponseDTO<Donor> index(int page, int size, String query) {
        List<Donor> donors;

        if (query == null || query.isBlank()) {
            donors = donorRepo.findAll(page, size);
        } else {
            donors = donorRepo.search(query, page, size);
        }

        long totalItems = donorRepo.count();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return new PaginationResponseDTO<>(donors, new PaginationResponseDTO.Metadata(page, totalPages, totalItems, size));
    }
}
