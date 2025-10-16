package org.kyojin.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Inject;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.request.AuthRequestDTO;
import org.kyojin.dto.response.AuthResponseDTO;
import org.kyojin.entity.Donor;
import org.kyojin.entity.User;
import org.kyojin.enums.DonorStatus;
import org.kyojin.factory.UserFactory;
import org.kyojin.repository.UserRepository;
import org.kyojin.service.AuthService;
import org.kyojin.service.SessionService;
import org.kyojin.util.PasswordUtil;
import org.kyojin.validation.ValidationResult;
import org.kyojin.validation.Validator;

import java.util.Map;
import java.util.function.Function;

import static org.kyojin.factory.UserFactory.createUser;

@Injectable
@Implementation(AuthService.class)
public class AuthServiceImpl implements AuthService {

    @Inject private UserRepository userRepo;
    @Inject private SessionService sessionService;
    @Inject private UserFactory userFactory;
    @Inject private Validator validator;

    @Override
    @Transactional
    public AuthResponseDTO register(AuthRequestDTO dto) {
        AuthResponseDTO validationResult = validate(dto);
        if (validationResult != null) return validationResult;

        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            return new AuthResponseDTO(null, "Email already in use");
        }

        User user = userRepo.save(createUser(dto));

        if (user instanceof Donor donor && donor.getMedicalProfile() != null) {
            var mp = donor.getMedicalProfile();
            boolean hasRisk = Boolean.TRUE.equals(mp.getHasHiv()) ||
                    Boolean.TRUE.equals(mp.getHasHepatitisB()) ||
                    Boolean.TRUE.equals(mp.getHasHepatitisC()) ||
                    Boolean.TRUE.equals(mp.getHasDiabetes()) ||
                    Boolean.TRUE.equals(mp.getIsPregnant()) ||
                    Boolean.TRUE.equals(mp.getIsBreastfeeding());

            if (hasRisk) {
                donor.setStatus(DonorStatus.INELIGIBLE);
                userRepo.save(donor);
            } else {
                donor.setStatus(DonorStatus.AVAILABLE);
            }
        }

        return new AuthResponseDTO(user, "Registration successful");
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO dto, HttpServletRequest req) {
        Map<String, Function<AuthRequestDTO, Object>> fields = Map.of(
                "email", AuthRequestDTO::getEmail,
                "password", AuthRequestDTO::getPassword
        );

        Map<String, String> rules = Map.of(
                "email", "required|email|max:100",
                "password", "required|min:6|max:50"
        );

        for (var entry : fields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue().apply(dto);
            ValidationResult result = validator.check(value, key).rules(rules.get(key));
            if (!result.isPassed()) return new AuthResponseDTO(null, result.message());
        }

        var userOpt = userRepo.findByEmail(dto.getEmail());
        if (userOpt.isEmpty() || !PasswordUtil.verifyPassword(dto.getPassword(), userOpt.get().getPassword())) {
            return new AuthResponseDTO(null, "Invalid email or password");
        }

        User user = userOpt.get();
        sessionService.createSession(req, user);

        return new AuthResponseDTO(user, "Login successful");
    }

    @Override
    public void logout(HttpServletRequest req) {
        sessionService.destroySession(req);
    }

    private AuthResponseDTO validate(AuthRequestDTO dto) {
        Map<String, Function<AuthRequestDTO, Object>> commonFields = Map.of(
                "username", AuthRequestDTO::getUsername,
                "email", AuthRequestDTO::getEmail,
                "password", AuthRequestDTO::getPassword
        );

        Map<String, String> commonRules = Map.of(
                "username", "required|min:3|max:50",
                "email", "required|email|max:100",
                "password", "required|min:6|max:50"
        );

        for (var entry : commonFields.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue().apply(dto);
            ValidationResult result = validator.check(value, field).rules(commonRules.get(field));
            if (!result.isPassed()) return new AuthResponseDTO(null, result.message());
        }

        String role = dto.getRole();
        if (role == null) return new AuthResponseDTO(null, "Role is required");

        switch (role.toUpperCase()) {
            case "DONOR" -> {
                Map<String, Function<AuthRequestDTO, Object>> donorFields = Map.of(
                        "weight", AuthRequestDTO::getWeight,
                        "isPregnant", AuthRequestDTO::getIsPregnant,
                        "isBreastfeeding", AuthRequestDTO::getIsBreastfeeding,
                        "hasHiv", AuthRequestDTO::getHasHiv,
                        "hasHepatitisB", AuthRequestDTO::getHasHepatitisB,
                        "hasHepatitisC", AuthRequestDTO::getHasHepatitisC,
                        "hasDiabetes", AuthRequestDTO::getHasDiabetes
                );

                Map<String, String> donorRules = Map.of(
                        "weight", "required|number",
                        "isPregnant", "boolean",
                        "isBreastfeeding", "boolean",
                        "hasHiv", "boolean",
                        "hasHepatitisB", "boolean",
                        "hasHepatitisC", "boolean",
                        "hasDiabetes", "boolean"
                );

                for (var entry : donorFields.entrySet()) {
                    Object value = entry.getValue().apply(dto);
                    ValidationResult result = validator.check(value, entry.getKey()).rules(donorRules.get(entry.getKey()));
                    if (!result.isPassed()) return new AuthResponseDTO(null, result.message());
                }
            }

            case "RECEIVER" -> {
                ValidationResult result = validator.check(dto.getUrgency(), "urgency").rules("required");
                if (!result.isPassed()) return new AuthResponseDTO(null, result.message());
            }

            default -> {
                return new AuthResponseDTO(null, "Invalid role");
            }
        }

        return null;
    }
}
