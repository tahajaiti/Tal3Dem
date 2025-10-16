package org.kyojin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kyojin.core.Injector;
import org.kyojin.core.annotation.Route;
import org.kyojin.dto.response.PaginationResponseDTO;
import org.kyojin.entity.Receiver;
import org.kyojin.service.ReceiverService;

import java.io.IOException;
import java.util.Map;

@Route("/receiver")
public class ReceiverController extends Controller{

    private static final int PAGE_SIZE = 8;

    private ReceiverService receiverService;


    @Override
    public void init() {
        this.receiverService = Injector.get(ReceiverService.class);
    }

    @Override
    protected Map<String, String> getPages() {
        return Map.of(
                "index", "/WEB-INF/views/content/receiver/index.jsp"
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

        var receivers = receiverService.index(page, PAGE_SIZE, query);

        setAttr(req, "content", jsp);
        setAttr(req,"receivers", receivers.getData());
        setAttr(req, "meta", receivers.getMetadata());
        render(req, resp, "layouts/main");

    }

}
