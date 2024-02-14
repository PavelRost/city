package org.rostfactory.car.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="car")
@NamedQueries(@NamedQuery(name = "Car.findCarsByCitizenId", query = "from Car c where c.citizenId = ?1"))
@SQLDelete(sql = "UPDATE car SET deleted = true WHERE id=?")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id")
    private Long citizenId;

    private String name;
    private String model;
    private boolean deleted = false;
}
