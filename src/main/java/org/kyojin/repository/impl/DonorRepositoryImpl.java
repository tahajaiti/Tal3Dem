package org.kyojin.repository.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.Donor;
import org.kyojin.repository.DonorRepository;
import org.kyojin.repository.JpaRepository;

@Implementation(DonorRepository.class)
@Injectable
public class DonorRepositoryImpl extends JpaRepository<Donor, Long> implements DonorRepository {
    public DonorRepositoryImpl() {
        super(Donor.class);
    }
}
