package org.kyojin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kyojin.enums.BloodType;
import org.kyojin.enums.DonorStatus;
import org.kyojin.enums.Gender;
import org.kyojin.enums.Urgency;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequestDTO {
    private String username;
    private String email;
    private String password;
    private String phone;
    private String cin;
    private String birthDate;
    private BloodType bloodType;
    private Gender gender;

    private String role; // "DONOR" or "RECEIVER"

    // Donor-specific
    private Double weight;

    // Donor MedicalProfile
    private Boolean isPregnant;
    private Boolean isBreastfeeding;
    private Boolean hasHiv;
    private Boolean hasHepatitisB;
    private Boolean hasHepatitisC;
    private Boolean hasDiabetes;
    private DonorStatus status;

    // Receiver-specific
    private Urgency urgency;
}
