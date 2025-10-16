package org.kyojin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponseDTO<T> {
    private List<T> data;
    private Metadata metadata;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Metadata {
        private int currentPage;
        private int totalPages;
        private long totalItems;
        private int pageSize;
    }
}
