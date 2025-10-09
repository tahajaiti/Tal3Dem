package org.kyojin.repository.impl;

import org.kyojin.entity.Donation;
import org.kyojin.repository.DonationRepository;
import org.kyojin.repository.JpaRepository;

public class DonationRepositoryImpl extends JpaRepository<Donation, Long> implements DonationRepository {
    public DonationRepositoryImpl() {
        super(Donation.class);
    }
}
