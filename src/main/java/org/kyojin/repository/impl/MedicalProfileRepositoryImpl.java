package org.kyojin.repository.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.MedicalProfile;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.MedicalProfileRepository;

@Implementation(MedicalProfileRepository.class)
@Injectable
public class MedicalProfileRepositoryImpl extends JpaRepository<MedicalProfile, Long> implements MedicalProfileRepository {
    public MedicalProfileRepositoryImpl() {
        super(MedicalProfile.class);
    }
}
