package org.kyojin.repository.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.Donation;
import org.kyojin.repository.DonationRepository;
import org.kyojin.repository.JpaRepository;

@Implementation(DonationRepository.class)
@Injectable
public class DonationRepositoryImpl extends JpaRepository<Donation, Long> implements DonationRepository {
    public DonationRepositoryImpl() {
        super(Donation.class);
    }
}
