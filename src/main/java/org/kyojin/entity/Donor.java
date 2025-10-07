package org.kyojin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.kyojin.enums.DonorStatus;

import java.util.Date;

@Entity
@Table(name="donors")
@Getter
@Setter
public class Donor extends User {

    Double weight;
    @Enumerated(EnumType.STRING)
    DonorStatus status;
    Date lastDonationDate;

    @OneToOne(mappedBy = "donor",cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalProfile medicalProfile;
}
