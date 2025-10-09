package org.kyojin.repository.impl;

import org.kyojin.entity.MedicalProfile;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.MedicalProfileRepository;

public class MedicalProfileRepositoryImpl extends JpaRepository<MedicalProfile, Long> implements MedicalProfileRepository {
    public MedicalProfileRepositoryImpl() {
        super(MedicalProfile.class);
    }
}
