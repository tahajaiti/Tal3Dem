package org.kyojin.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.kyojin.core.annotation.Implementation;
import org.kyojin.core.annotation.Inject;
import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.request.DonationRequestDTO;
import org.kyojin.dto.response.DonationResponseDTO;
import org.kyojin.entity.*;
import org.kyojin.enums.DonorStatus;
import org.kyojin.enums.ReceiverState;
import org.kyojin.repository.DonationRepository;
import org.kyojin.repository.UserRepository;
import org.kyojin.service.DonationService;
import org.kyojin.service.SessionService;

import java.util.Date;
import java.util.Optional;

import static org.kyojin.util.BloodTypeUtil.isCompatible;

@Injectable
@Implementation(DonationService.class)
public class DonationServiceImpl implements DonationService {

    @Inject
    private DonationRepository donationRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private SessionService sessionService;

    @Transactional
    public DonationResponseDTO makeDonation(DonationRequestDTO requestDTO, HttpServletRequest req) {
        User user = (User) sessionService.getUser(req);

        if (!(user instanceof Donor donor)) {
            return new DonationResponseDTO(false, "You must be a donor to donate.");
        }

        if (!isDonorEligible(donor)) {
            return new DonationResponseDTO(false, "You are currently not eligible to donate.");
        }

        Optional<User> receiverOpt = userRepository.find(requestDTO.getReceiverId());
        if (receiverOpt.isEmpty() || !(receiverOpt.get() instanceof Receiver receiver)) {
            return new DonationResponseDTO(false, "Invalid receiver.");
        }

        if (receiver.getRequiredDonors() == null || receiver.getRequiredDonors() <= 0) {
            return new DonationResponseDTO(false, "Receiver does not need any more donations.");
        }

        if (!isCompatible(donor.getBloodType(), receiver.getBloodType())) {
            return new DonationResponseDTO(false, "Blood type not compatible.");
        }

        if (!isMedicalProfileEligible(donor.getMedicalProfile())) {
            return new DonationResponseDTO(false, "Donor not eligible based on medical profile.");
        }

        donor.setLastDonationDate(new Date());
        donor.setStatus(DonorStatus.UNAVAILABLE);

        receiver.setRequiredDonors(receiver.getRequiredDonors() - 1);
        if (receiver.getRequiredDonors() <= 0) {
            receiver.setState(ReceiverState.SATISFIED);
        }

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setReceiver(receiver);
        donation.setDonationDate(new Date());

        donationRepository.save(donation);
        userRepository.update(donor);
        userRepository.update(receiver);

        return new DonationResponseDTO(true, "Donation request sent successfully!");
    }

    private boolean isDonorEligible(Donor donor) {
        return donor.getLastDonationDate() == null
                && donor.getStatus() != DonorStatus.UNAVAILABLE
                && donor.getStatus() != DonorStatus.INELIGIBLE;
    }

    private boolean isMedicalProfileEligible(MedicalProfile profile) {
        if (profile == null) return true;

        return !(Boolean.TRUE.equals(profile.getHasHiv())
                || Boolean.TRUE.equals(profile.getHasHepatitisB())
                || Boolean.TRUE.equals(profile.getHasHepatitisC())
                || Boolean.TRUE.equals(profile.getHasDiabetes())
                || Boolean.TRUE.equals(profile.getIsPregnant())
                || Boolean.TRUE.equals(profile.getIsBreastfeeding()));
    }
}
