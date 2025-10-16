package org.kyojin.repository;

import org.kyojin.entity.Donor;

import java.util.List;

public interface DonorRepository extends Repository<Donor, Long> {
    List<Donor> search(String keyword, int page, int size);

}
