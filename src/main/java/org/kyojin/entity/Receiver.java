package org.kyojin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.kyojin.enums.ReceiverState;
import org.kyojin.enums.Urgency;

@Entity
@Table(name="receivers")
@DiscriminatorValue("RECEIVER")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class Receiver extends User {

    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    private ReceiverState state;

    private Integer requiredDonors;
}
