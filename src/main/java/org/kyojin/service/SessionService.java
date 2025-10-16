package org.kyojin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.kyojin.entity.User;

public interface SessionService {
    void createSession(HttpServletRequest req, Object user);

    Object getUser(HttpServletRequest req);

    boolean isLoggedIn(HttpServletRequest req);

    void destroySession(HttpServletRequest req);

}
