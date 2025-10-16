package org.kyojin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kyojin.core.Injector;
import org.kyojin.core.annotation.Route;
import org.kyojin.dto.request.AuthRequestDTO;
import org.kyojin.mapper.AuthMapper;
import org.kyojin.service.AuthService;

import java.io.IOException;
import java.util.Map;

@Route("/auth")
public class AuthController extends Controller {

    private AuthService authService;

    @Override
    public void init() {
        authService = Injector.get(AuthService.class);
    }

    @Override
    protected Map<String, String> getPages() {
        return Map.of(
                "login", "/WEB-INF/views/content/auth/login.jsp",
                "register", "/WEB-INF/views/content/auth/register.jsp",
                "index", "/WEB-INF/views/content/auth/login.jsp"
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = param(req, "action");

        if (action == null || action.isEmpty()) {
            renderError(req, resp, 400, "Bad Request: Missing action parameter");
            return;
        }

        switch (action) {
            case "login" -> handleLogin(req, resp);
            case "register" -> handleRegister(req, resp);
            case "logout" -> handleLogout(req, resp);
            default -> renderError(req, resp, 404, "Not Found");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            AuthRequestDTO requestDTO = AuthMapper.toRequest(req);
            var result = authService.login(requestDTO, req);

            if (result != null && result.user() != null) {
                setSuccess(req, "Login successful! Welcome back.");
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                setError(req, "Invalid username or password. Please try again.");
                forward("login", req, resp);
            }
        } catch (Exception e) {
            setError(req, "An error occurred during login. Please try again.");
            forward("login", req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            AuthRequestDTO requestDTO = AuthMapper.toRequest(req);
            var result = authService.register(requestDTO);

            if (result != null && result.user() != null) {
                setSuccess(req, "Registration successful! Please login to continue.");
                resp.sendRedirect(req.getContextPath() + "/auth?action=login");
            } else {
                String errorMessage = result != null && result.message() != null
                        ? result.message()
                        : "Registration failed. Please check your information and try again.";
                setError(req, errorMessage);
                forward("register", req, resp);
            }
        } catch (Exception e) {
            setError(req, "An error occurred during registration. Please try again.");
            forward("register", req, resp);
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        authService.logout(req);
        redirect(resp, req.getContextPath() + "/auth?action=login");
    }
}