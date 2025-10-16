package org.kyojin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kyojin.dto.request.AuthRequestDTO;
import org.kyojin.dto.response.AuthResponseDTO;
import org.kyojin.entity.Donor;
import org.kyojin.entity.Receiver;
import org.kyojin.repository.UserRepository;
import org.kyojin.service.impl.AuthServiceImpl;
import org.kyojin.validation.Validator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepo;
    private SessionService sessionService;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        sessionService = mock(SessionService.class);
        request = mock(HttpServletRequest.class);

        Validator validator = new Validator();

        authService = new AuthServiceImpl();

        try {
            var fields = authService.getClass().getDeclaredFields();
            for (var f : fields) {
                f.setAccessible(true);
                if (f.getType() == UserRepository.class) f.set(authService, userRepo);
                else if (f.getType() == SessionService.class) f.set(authService, sessionService);
                else if (f.getType() == Validator.class) f.set(authService, validator);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRegisterDonorSuccess() {
        AuthRequestDTO dto = createDonorDTO("test@example.com");

        Donor donor = new Donor();
        donor.setEmail(dto.getEmail());

        when(userRepo.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepo.save(any(Donor.class))).thenAnswer(i -> i.getArguments()[0]);

        AuthResponseDTO response = authService.register(dto);

        assertNotNull(response);
        assertEquals("Registration successful", response.message());
        assertNotNull(response.user());
        assertEquals(dto.getEmail(), response.user().getEmail());
    }

    @Test
    void testRegisterReceiverSuccess() {
        AuthRequestDTO dto = createReceiverDTO("receiver@example.com");

        Receiver receiver = new Receiver();
        receiver.setEmail(dto.getEmail());

        when(userRepo.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepo.save(any(Receiver.class))).thenAnswer(i -> i.getArguments()[0]);

        AuthResponseDTO response = authService.register(dto);

        assertNotNull(response);
        assertEquals("Registration successful", response.message());
        assertNotNull(response.user());
        assertEquals(dto.getEmail(), response.user().getEmail());
    }

    @Test
    void testLoginDonorSuccess() {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("donor@gmail.com");
        dto.setPassword("password123");

        Donor donor = new Donor();
        donor.setEmail(dto.getEmail());
        donor.setPassword(org.kyojin.util.PasswordUtil.hashPassword(dto.getPassword()));

        when(userRepo.findByEmail(dto.getEmail())).thenReturn(Optional.of(donor));

        AuthResponseDTO response = authService.login(dto, request);

        assertNotNull(response);
        assertEquals("Login successful", response.message());
        assertEquals(dto.getEmail(), response.user().getEmail());

        verify(sessionService).createSession(request, donor);
    }

    @Test
    void testLoginReceiverSuccess() {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setEmail("receiver@gmail.com");
        dto.setPassword("password123");

        Receiver receiver = new Receiver();
        receiver.setEmail(dto.getEmail());
        receiver.setPassword(org.kyojin.util.PasswordUtil.hashPassword(dto.getPassword()));

        when(userRepo.findByEmail(dto.getEmail())).thenReturn(Optional.of(receiver));

        AuthResponseDTO response = authService.login(dto, request);

        assertNotNull(response);
        assertEquals("Login successful", response.message());
        assertEquals(dto.getEmail(), response.user().getEmail());

        verify(sessionService).createSession(request, receiver);
    }

    @Test
    void testLogout() {
        authService.logout(request);
        verify(sessionService).destroySession(request);
    }

    private AuthRequestDTO createDonorDTO(String email) {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setUsername("donoruser");
        dto.setEmail(email);
        dto.setPassword("password123");
        dto.setRole("DONOR");
        dto.setPhone("1234567890");
        dto.setCin("CIN12345");
        dto.setBirthDate("1990-01-01");
        dto.setWeight(70.0);
        dto.setIsPregnant(false);
        dto.setIsBreastfeeding(false);
        dto.setHasHiv(false);
        dto.setHasHepatitisB(false);
        dto.setHasHepatitisC(false);
        dto.setHasDiabetes(false);
        return dto;
    }

    private AuthRequestDTO createReceiverDTO(String email) {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setUsername("receiveruser");
        dto.setEmail(email);
        dto.setPassword("password123");
        dto.setRole("RECEIVER");
        dto.setPhone("0987654321");
        dto.setCin("CIN54321");
        dto.setBirthDate("1995-05-05");
        dto.setUrgency(org.kyojin.enums.Urgency.URGENT);
        return dto;
    }
}
