package org.kyojin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.kyojin.enums.ReceiverState;
import org.kyojin.enums.Urgency;

@Entity
@Table(name="receivers")
@Getter
@Setter
public class Receiver extends User {

    @Enumerated(EnumType.STRING)
    Urgency urgency;
    @Enumerated(EnumType.STRING)
    ReceiverState state;
    Integer requiredDonors;
}
