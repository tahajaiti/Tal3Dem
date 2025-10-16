package org.kyojin.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Inject;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.request.ProfileRequestDTO;
import org.kyojin.dto.response.ProfileResponseDTO;
import org.kyojin.entity.User;
import org.kyojin.factory.UserFactory;
import org.kyojin.mapper.ProfileMapper;
import org.kyojin.repository.UserRepository;
import org.kyojin.service.ProfileService;
import org.kyojin.service.SessionService;
import org.kyojin.validation.Validator;

@Injectable
@Implementation(ProfileService.class)
public class ProfileServiceImpl implements ProfileService {

    @Inject
    private UserRepository userRepo;
    @Inject
    private SessionService sessionService;
    @Inject
    private UserFactory userFactory;
    @Inject
    private Validator validator;


    @Override
    @Transactional
    public ProfileResponseDTO updateProfile(ProfileRequestDTO dto, HttpServletRequest req) {
        User sessionUser = (User) sessionService.getUser(req);
        if (sessionUser == null)
            return new ProfileResponseDTO("Not authenticated");

        User updatedUser = ProfileMapper.toEntity(req, dto, sessionService);
        if (updatedUser == null)
            return new ProfileResponseDTO("User not found");

        userRepo.update(updatedUser);

        sessionService.destroySession(req);
        sessionService.createSession(req, updatedUser);

        return new ProfileResponseDTO("Profile updated successfully");
    }

    @Override
    @Transactional
    public  ProfileResponseDTO deleteProfile(HttpServletRequest req) {
        User sessionUser = (User) sessionService.getUser(req);
        if (sessionUser == null)
            return new ProfileResponseDTO("Not authenticated");


        userRepo.delete(sessionUser.getId());
        sessionService.destroySession(req);
        return  new ProfileResponseDTO("Profile deleted successfully");
    }


}
