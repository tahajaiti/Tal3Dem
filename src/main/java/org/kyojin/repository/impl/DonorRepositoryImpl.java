package org.kyojin.repository.impl;

import org.kyojin.entity.Donor;
import org.kyojin.repository.DonorRepository;
import org.kyojin.repository.JpaRepository;

public class DonorRepositoryImpl extends JpaRepository<Donor, Long> implements DonorRepository {
    public DonorRepositoryImpl() {
        super(Donor.class);
    }
}
