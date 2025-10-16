package org.kyojin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kyojin.core.Injector;
import org.kyojin.core.annotation.Route;
import org.kyojin.dto.request.ProfileRequestDTO;
import org.kyojin.dto.response.ProfileResponseDTO;
import org.kyojin.enums.BloodType;
import org.kyojin.mapper.ProfileMapper;
import org.kyojin.service.ProfileService;

import java.io.IOException;
import java.util.Map;

@Route("/profile")
public class ProfileController extends Controller {

    private ProfileService profileService;

    @Override
    public void init() {
        this.profileService = Injector.get(ProfileService.class);
    }

    @Override
    protected Map<String, String> getPages() {
        return Map.of(
                "index", "/WEB-INF/views/content/profile/index.jsp"
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAttr(req, "bloodTypes", BloodType.values());
        forward("index", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = param(req, "action");
        if (action == null || action.isEmpty()) {
            renderError(req, resp, 400, "Bad Request: Missing action parameter");
            return;
        }

        try {
            switch (action) {
                case "update" -> handleUpdate(req, resp);
                case "delete" -> handleDelete(req, resp);
                default -> renderError(req, resp, 400, "Invalid action: " + action);
            }
        } catch (Exception e) {
            renderError(req, resp, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        ProfileRequestDTO dto = ProfileMapper.fromRequest(req);
        ProfileResponseDTO result = profileService.updateProfile(dto, req);

        if (result == null) {
            setError(req, "Something went wrong. Please try again.");
            forward("index", req, resp);
            return;
        }

        setSuccess(req, result.message());
        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        ProfileResponseDTO result = profileService.deleteProfile(req);

        if (result == null) {
            setError(req, "Unable to delete account. Please try again.");
            forward("index", req, resp);
            return;
        }

        setSuccess(req, result.message());
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
