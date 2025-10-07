package org.kyojin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "donor_id", referencedColumnName = "id")
    private Donor donor;

    private Boolean isPregnant;
    private Boolean isBreastfeeding;
    private Boolean hasHiv;
    private Boolean hasHepatitisB;
    private Boolean hasHepatitisC;
    private Boolean hasDiabetes;
}
