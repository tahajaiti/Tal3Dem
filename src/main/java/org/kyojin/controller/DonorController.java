package org.kyojin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kyojin.core.Injector;
import org.kyojin.core.annotation.Route;
import org.kyojin.service.DonorService;

import java.io.IOException;
import java.util.Map;

@Route("/donor")
public class DonorController extends Controller{
    private static final int PAGE_SIZE = 8;

    private DonorService donorService;


    @Override
    public void init() {
        this.donorService = Injector.get(DonorService.class);
    }

    @Override
    protected Map<String, String> getPages() {
        return Map.of(
                "index", "/WEB-INF/views/content/donor/index.jsp"
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = param(req, "action", "index");
        String query = param(req, "q", "");
        Integer page = param(req, "page", 1);
        Map<String, String> pages = getPages();

        String jsp = pages.get(action);
        if (jsp == null) {
            renderError(req, resp, HttpServletResponse.SC_NOT_FOUND, "Page not found: " + action);
            return;
        }

        var donors = donorService.index(page, PAGE_SIZE, query);

        setAttr(req, "content", jsp);
        setAttr(req,"donors", donors.getData());
        setAttr(req, "meta", donors.getMetadata());
        render(req, resp, "layouts/main");

    }
}
