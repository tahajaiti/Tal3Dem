package org.kyojin.factory;

import org.kyojin.core.annotation.Injectable;
import org.kyojin.dto.request.AuthRequestDTO;
import org.kyojin.entity.Donor;
import org.kyojin.entity.MedicalProfile;
import org.kyojin.entity.Receiver;
import org.kyojin.entity.User;
import org.kyojin.enums.ReceiverState;
import org.kyojin.util.PasswordUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Injectable
public class UserFactory {

    private UserFactory() {}

    public static User createUser(AuthRequestDTO dto) {
        if (dto == null || dto.getRole() == null) {
            throw new IllegalArgumentException("DTO or role cannot be null");
        }

        return switch (dto.getRole().toUpperCase()) {
            case "DONOR" -> createDonor(dto);
            case "RECEIVER" -> createReceiver(dto);
            default -> throw new IllegalArgumentException("Invalid role: " + dto.getRole());
        };
    }

    private static Donor createDonor(AuthRequestDTO dto) {
        Donor donor = new Donor();
        setCommonFields(donor, dto);
        donor.setWeight(dto.getWeight());
        donor.setStatus(dto.getStatus());

        MedicalProfile profile = new MedicalProfile();
        profile.setDonor(donor);
        profile.setIsPregnant(dto.getIsPregnant());
        profile.setIsBreastfeeding(dto.getIsBreastfeeding());
        profile.setHasHiv(dto.getHasHiv());
        profile.setHasHepatitisB(dto.getHasHepatitisB());
        profile.setHasHepatitisC(dto.getHasHepatitisC());
        profile.setHasDiabetes(dto.getHasDiabetes());


        donor.setMedicalProfile(profile);
        return donor;
    }

    private static Receiver createReceiver(AuthRequestDTO dto) {
        Receiver receiver = new Receiver();
        setCommonFields(receiver, dto);
        receiver.setState(ReceiverState.PENDING);

        if (dto.getUrgency() != null) {
            receiver.setUrgency(dto.getUrgency());

            switch (dto.getUrgency()) {
                case NORMAL -> receiver.setRequiredDonors(1);
                case URGENT ->  receiver.setRequiredDonors(3);
                case CRITICAL -> receiver.setRequiredDonors(4);
            }

        }

        return receiver;
    }

    private static void setCommonFields(User user, AuthRequestDTO dto) {
        user.setName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setCin(dto.getCin());

        user.setGender(dto.getGender());
        user.setBloodType(dto.getBloodType());

        if (dto.getBirthDate() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dto.getBirthDate());
                user.setBirthDate(date);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid birthDate format: " + dto.getBirthDate(), e);
            }
        }
    }

}
