package org.rostfactory.house.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="house")
@Data
@SQLDelete(sql = "UPDATE house SET deleted = true WHERE id=?")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @Column(name = "citizen_id")
    private Long citizenId;
    private boolean deleted = false;
}
