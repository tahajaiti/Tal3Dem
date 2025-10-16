package org.kyojin.repository;

import org.kyojin.entity.Receiver;

import java.util.List;

public interface ReceiverRepository extends Repository<Receiver, Long>{

    List<Receiver> search(String keyword, int page, int size);
}
