package org.kyojin.controller;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HelloController extends HttpServlet {

    @Override
    public void init() {
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
