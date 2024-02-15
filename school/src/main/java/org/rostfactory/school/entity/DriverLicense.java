package org.rostfactory.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="school")
@SQLDelete(sql = "UPDATE school SET deleted = true WHERE id=?")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
