package org.kyojin.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.entity.User;
import org.kyojin.service.SessionService;


@Injectable
@Implementation(SessionService.class)
public class SessionServiceImpl implements SessionService {

    private static final String USER_KEY = "user";

    public void createSession(HttpServletRequest req, Object user) {
        HttpSession session = req.getSession(true);
        session.setAttribute(USER_KEY, user);
    }

    public Object getUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? session.getAttribute(USER_KEY) : null;
    }

    public boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute(USER_KEY) != null;
    }

    public void destroySession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
    }

}
