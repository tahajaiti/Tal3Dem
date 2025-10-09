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
    @JoinColumn(name = "donor_id", referencedColumnName = "id", nullable = false)
    private Donor donor;

    @Column(nullable = true)
    private Boolean isPregnant;

    @Column(nullable = true)
    private Boolean isBreastfeeding;

    @Column(nullable = true)
    private Boolean hasHiv;

    @Column(nullable = true)
    private Boolean hasHepatitisB;

    @Column(nullable = true)
    private Boolean hasHepatitisC;

    @Column(nullable = true)
    private Boolean hasDiabetes;
}
