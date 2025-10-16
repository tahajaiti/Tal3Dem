package org.kyojin.repository.impl;

import jakarta.persistence.TypedQuery;
import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.Donor;
import org.kyojin.entity.Receiver;
import org.kyojin.repository.DonorRepository;
import org.kyojin.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

@Implementation(DonorRepository.class)
@Injectable
public class DonorRepositoryImpl extends JpaRepository<Donor, Long> implements DonorRepository {
    public DonorRepositoryImpl() {
        super(Donor.class);
    }

    public List<Donor> search(String keyword, int page, int size) {
        try {
            return executeInTransaction(em -> {
                var cb = em.getCriteriaBuilder();
                var cq = cb.createQuery(Donor.class);
                var root = cq.from(Donor.class);

                String pattern = "%" + keyword.toLowerCase() + "%";

                var predicates = cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern),
                        cb.like(cb.lower(root.get("phone")), pattern),
                        cb.like(cb.lower(root.get("bloodType")), pattern),
                        cb.like(cb.lower(root.get("status")), pattern)
                );

                cq.select(root).where(predicates);

                TypedQuery<Donor> query = em.createQuery(cq);
                query.setFirstResult((page - 1) * size);
                query.setMaxResults(size);

                return query.getResultList();
            });

        } catch (Exception e) {
            log.error("Search failed for Donor: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
