package org.kyojin.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kyojin.util.DebugUtil;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Controller extends HttpServlet {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    /**
     * A map of action names to JSP paths.
     * Override this method in subclasses to define custom pages.
     *
     * @return A map of action names to JSP paths.
     */
    protected Map<String, String> getPages() {
        return Map.of();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = param(req, "action", "index");
        Map<String, String> pages = getPages();

        try {
            String jsp = pages.get(action);
            if (jsp == null) {
                renderError(req, resp, HttpServletResponse.SC_NOT_FOUND, "Page not found: " + action);
                return;
            }

            setAttr(req, "content", jsp);
            setAttr(req, "title", capitalize(action));
            render(req, resp, "layouts/main");

        } catch (Exception e) {
            logger.severe("Error in doGet: " + e.getMessage());
            renderError(req, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }

    /**
     * Get a trimmed parameter value from the request.
     */
    protected String param(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value != null && !value.trim().isEmpty() ? value.trim() : null;
    }

    /**
     * Get a parameter with a default value.
     */
    protected String param(HttpServletRequest request, String name, String defaultValue) {
        String value = param(request, name);
        return value != null ? value : defaultValue;
    }

    /**
     * Get an integer parameter with a default value.
     */
    protected Integer param(HttpServletRequest request, String name, Integer defaultValue) {
        String value = param(request, name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.warning("Invalid integer parameter '" + name + "': " + value);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Set a request attribute.
     */
    protected void setAttr(HttpServletRequest request, String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
     * Get a request attribute.
     */
    protected Object getAttr(HttpServletRequest request, String name) {
        return request.getAttribute(name);
    }

    /**
     * Set an error message in the session.
     */
    protected void setError(HttpServletRequest req, String message) {
        HttpSession session = req.getSession(true);
        session.setAttribute("error", message);
    }

    /**
     * Set a success message in the session.
     */
    protected void setSuccess(HttpServletRequest req, String message) {
        HttpSession session = req.getSession(true);
        session.setAttribute("success", message);
    }

    /**
     * Set a general info message in the session.
     */
    protected void setMessage(HttpServletRequest req, String message) {
        HttpSession session = req.getSession(true);
        session.setAttribute("message", message);
    }

    /**
     * Retrieve and consume flash messages from session.
     * This method moves session messages to request attributes and removes them from session.
     */
    protected void loadMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String error = (String) session.getAttribute("error");
            if (error != null) {
                setAttr(request, "error", error);
                session.removeAttribute("error");
            }

            String success = (String) session.getAttribute("success");
            if (success != null) {
                setAttr(request, "success", success);
                session.removeAttribute("success");
            }

            String message = (String) session.getAttribute("message");
            if (message != null) {
                setAttr(request, "message", message);
                session.removeAttribute("message");
            }
        }
    }

    /**
     * Send a redirect response.
     */
    protected void redirect(HttpServletResponse resp, String url) throws IOException {
        resp.sendRedirect(url);
    }

    /**
     * Render a JSP view with the main layout.
     */
    protected void render(HttpServletRequest req, HttpServletResponse resp, String jspPath)
            throws ServletException, IOException {

        // Load flash messages before rendering
        loadMessages(req);

        String fullPath = "/WEB-INF/views/" + jspPath + ".jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(fullPath);

        if (dispatcher == null) {
            throw new ServletException("Unable to find view: " + fullPath);
        }

        dispatcher.forward(req, resp);
    }

    /**
     * Render an error page.
     */
    protected void renderError(HttpServletRequest req, HttpServletResponse resp, int status, String message)
            throws ServletException, IOException {
        resp.setStatus(status);
        setAttr(req, "title", "Error " + status);
        setAttr(req, "errorMessage", message);
        setAttr(req, "content", "/WEB-INF/views/errors/error.jsp");
        render(req, resp, "layouts/main");
    }

    /**
     * Forward to a specific JSP (bypasses main layout).
     */
    protected void forward(String action, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, String> pages = getPages();
        String jsp = pages.get(action);

        if (jsp == null) {
            throw new ServletException("Unknown action: " + action);
        }

        setAttr(req, "content", jsp);
        setAttr(req, "title", capitalize(action));

        loadMessages(req);

        String fullPath = "/WEB-INF/views/layouts/main.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(fullPath);

        if (dispatcher == null) {
            throw new ServletException("Unable to find main layout");
        }

        dispatcher.forward(req, resp);
    }

    protected void dd(HttpServletResponse resp, Object... objects) throws IOException {
        DebugUtil.dd(resp, objects);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}