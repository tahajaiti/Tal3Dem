package org.kyojin.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kyojin.service.impl.SessionServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private SessionService sessionService;
    private HttpServletRequest request;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);

        when(request.getSession(true)).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);

        sessionService = new SessionServiceImpl();
    }

    @Test
    void testCreateSession() {
        Object user = new Object();
        sessionService.createSession(request, user);

        verify(session).setAttribute("user", user);
    }

    @Test
    void testGetUser() {
        Object user = new Object();
        when(session.getAttribute("user")).thenReturn(user);

        Object result = sessionService.getUser(request);
        assertEquals(user, result);
    }

    @Test
    void testIsLoggedIn() {
        when(session.getAttribute("user")).thenReturn(null);
        assertFalse(sessionService.isLoggedIn(request));

        Object user = new Object();
        when(session.getAttribute("user")).thenReturn(user);
        assertTrue(sessionService.isLoggedIn(request));
    }

    @Test
    void testDestroySession() {
        sessionService.destroySession(request);
        verify(session).invalidate();
    }
}
