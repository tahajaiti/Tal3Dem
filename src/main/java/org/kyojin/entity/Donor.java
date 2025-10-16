package org.kyojin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.kyojin.enums.DonorStatus;

import java.util.Date;

@Entity
@Table(name="donors")
@DiscriminatorValue("DONOR")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class Donor extends User {

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DonorStatus status;

    private Date lastDonationDate;

    @OneToOne(mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalProfile medicalProfile;

}
