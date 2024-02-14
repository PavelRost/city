package org.rostfactory.police.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="police")
@SQLDelete(sql = "UPDATE police SET deleted = true WHERE id=?")
@Data
public class PoliceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id")
    private Long citizenId;

    @Column(name = "license_id")
    private Long licenseId;
    private boolean deleted = false;
}
