package org.kyojin.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfileRequestDTO {
    private String name;
    private String email;
    private String phone;
    private String cin;
    private Date birthDate;
    private String bloodType;

    // Donor-specific
    private Double weight;


    private Boolean isPregnant;
    private Boolean isBreastfeeding;
    private Boolean hasHiv;
    private Boolean hasHepatitisB;
    private Boolean hasHepatitisC;
    private Boolean hasDiabetes;

    // Receiver-specific
    private String urgency;
}
