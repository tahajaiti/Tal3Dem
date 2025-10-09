package org.kyojin.provider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.kyojin.core.annotation.Injectable;

@Injectable
public class EntityManagerProvider {

    private final EntityManagerFactory emf;

    public EntityManagerProvider() {
        this.emf = Persistence.createEntityManagerFactory("tal3demPU");
    }

    public EntityManager get() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

}
