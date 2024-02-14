package org.rostfactory.school.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="school")
@SQLDelete(sql = "UPDATE school SET deleted = true WHERE id=?")
@Data
public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id")
    private Long citizenId;

    private String value;
    private boolean deleted = false;
}
