package com.banking.api.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    private Long userId;

    @NotNull(message = "first name must not be null")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "last name must not be null")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "middle_initial")
    private String middleInitial;

    @NotNull(message = "email must not be null")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @NotNull(message = "address must not be null")
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @NotNull(message = "ssn must not be null")
    @Column(name = "ssn", nullable = false, unique=true)
    private String ssn;

    @Column(name = "created_date")
    private Timestamp createdDate;

    public Customer(){

    }

    public Customer(Long id, Long userId, String firstName, String lastName, String middleInitial, String email,
                    String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state,
                    String city, String country, String ssn, Timestamp createdDate) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.email = email;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.state = state;
        this.city = city;
        this.country = country;
        this.ssn = ssn;
        this.createdDate = createdDate;
    }
}
