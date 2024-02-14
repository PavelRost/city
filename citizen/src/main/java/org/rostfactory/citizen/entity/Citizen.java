package org.rostfactory.citizen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="citizen")
@SQLDelete(sql = "UPDATE citizen SET deleted = true WHERE id=?")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "job")
    private String job;

    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "driver_license_id")
    private Long licenseId;

    @Column(name = "account_bank_id")
    private Long accountBankId;

    @Column(name = "deleted")
    private boolean deleted = false;
}