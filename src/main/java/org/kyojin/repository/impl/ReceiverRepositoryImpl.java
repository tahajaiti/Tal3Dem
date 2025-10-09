package org.kyojin.repository.impl;

import org.kyojin.entity.Receiver;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.ReceiverRepository;

public class ReceiverRepositoryImpl extends JpaRepository<Receiver, Long> implements ReceiverRepository {
    public ReceiverRepositoryImpl() {
        super(Receiver.class);
    }
}
