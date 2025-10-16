package org.kyojin.repository.impl;

import jakarta.persistence.TypedQuery;
import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.Receiver;
import org.kyojin.repository.JpaRepository;
import org.kyojin.repository.ReceiverRepository;

import java.util.Collections;
import java.util.List;

@Implementation(ReceiverRepository.class)
@Injectable
public class ReceiverRepositoryImpl extends JpaRepository<Receiver, Long> implements ReceiverRepository {
    public ReceiverRepositoryImpl() {
        super(Receiver.class);
    }


    public List<Receiver> search(String keyword, int page, int size) {
        try {
            return executeInTransaction(em -> {
                var cb = em.getCriteriaBuilder();
                var cq = cb.createQuery(Receiver.class);
                var root = cq.from(Receiver.class);

                String pattern = "%" + keyword.toLowerCase() + "%";

                var predicates = cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern),
                        cb.like(cb.lower(root.get("phone")), pattern),
                        cb.like(cb.lower(root.get("bloodType")), pattern),
                        cb.like(cb.lower(root.get("state")), pattern)
                );

                cq.select(root).where(predicates);

                TypedQuery<Receiver> query = em.createQuery(cq);
                query.setFirstResult((page - 1) * size);
                query.setMaxResults(size);

                return query.getResultList();
            });

        } catch (Exception e) {
            log.error("Search failed for Receiver: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
