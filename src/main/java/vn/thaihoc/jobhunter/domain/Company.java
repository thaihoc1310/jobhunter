package vn.thaihoc.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "username không được để trống")
    private String name;

    private String address;

    private String logo;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

}
