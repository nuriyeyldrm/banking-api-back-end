package com.banking.api.domain;

import com.banking.api.domain.enumeration.AppUserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Role;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 11)
    @NotNull(message = "ssn must not be null")
    @Column(name = "ssn", nullable = false, unique=true, length = 11)
    private String ssn;

    @NotNull(message = "first name must not be null")
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstname;

    @NotNull(message = "last name must not be null")
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastname;

    @Email
    @NotNull(message = "email must not be null")
    @Size(min = 5, max = 254)
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @NotNull(message = "password must not be null")
    @Size(min = 4, max = 60)
    @Column(name = "password_hash", nullable = false, length = 60)
    private String password;

    @NotNull(message = "address must not be null")
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull(message = "mobile phone number must not be null")
    @Column(name = "mobile_phone_number", nullable = false)
    private String mobilePhoneNumber;

    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_user_role", length = 50, updatable = false)
    private AppUserRole appUserRole;

    public User() {

    }

    public User(Long id, String ssn, String firstname, String lastname, String email, String password, String address,
                String mobilePhoneNumber, String createdBy, Timestamp createdDate, String lastModifiedBy,
                Timestamp lastModifiedDate, AppUserRole appUserRole) {
        this.id = id;
        this.ssn = ssn;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.appUserRole = appUserRole;
    }
}
