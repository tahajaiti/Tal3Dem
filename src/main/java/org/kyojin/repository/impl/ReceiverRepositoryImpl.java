package org.kyojin.repository.impl;

import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.Receiver;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.ReceiverRepository;

@Implementation(ReceiverRepository.class)
@Injectable
public class ReceiverRepositoryImpl extends JpaRepository<Receiver, Long> implements ReceiverRepository {
    public ReceiverRepositoryImpl() {
        super(Receiver.class);
    }
}
