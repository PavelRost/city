package org.rostfactory.bank.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="bank")
@SQLDelete(sql = "UPDATE bank SET deleted = true WHERE id=?")
@Data
public class AccountBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id")
    private Long citizenId;

    private long money;
    private boolean deleted = false;
}
