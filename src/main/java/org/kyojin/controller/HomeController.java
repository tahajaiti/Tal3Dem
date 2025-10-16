package org.kyojin.controller;

import org.kyojin.core.annotation.Route;

import java.util.Map;

@Route("/")
public class HomeController extends Controller{

    @Override
    protected Map<String, String> getPages() {
        return Map.of(
                "index", "/WEB-INF/views/content/home.jsp"
        );
    }
}
