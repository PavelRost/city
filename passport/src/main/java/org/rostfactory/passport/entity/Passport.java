package org.rostfactory.passport.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="passport")
@SQLDelete(sql = "UPDATE passport SET deleted = true WHERE id=?")
@Data
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id")
    private Long citizenId;

    private String value;
    private boolean deleted = false;
}
