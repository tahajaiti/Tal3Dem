package org.kyojin.controller;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kyojin.entity.Donor;

import java.io.IOException;

public class HelloController extends HttpServlet {

    @Override
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tal3demPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Donor donor = new Donor();
        donor.setUsername("donor1");

        em.persist(donor);
        em.getTransaction().commit();
        em.close();
        emf.close();

        System.out.println("HelloController initialized");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.setContentType("text/html");
            res.getWriter().println("<h1>TEST MN CONTROLLER</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
