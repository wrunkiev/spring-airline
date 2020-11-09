package com.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PASSENGER")
public class Passenger {
    @Id
    @Column(name = "PASSENGER_ID")
    @SequenceGenerator(name = "PASSENGER_SEQ", sequenceName = "PASSENGER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PASSENGER_SEQ")
    private Long id;

    @Column(name = "PASSENGER_LAST_NAME")
    private String lastName;

    @Column(name = "PASSENGER_NATIONALITY")
    private String nationality;

    @Column(name = "PASSENGER_DATE_OF_BIRTH")
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "PASSENGER_PASSPORT_CODE")
    private String passportCode;

    @Column(name = "PASSENGER_LIST_FLIGHT_ID")
    private String flights;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportCode() {
        return passportCode;
    }

    public void setPassportCode(String passportCode) {
        this.passportCode = passportCode;
    }

    public String getFlights() {
        return flights;
    }

    public void setFlights(String flights) {
        this.flights = flights;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", passportCode='" + passportCode + '\'' +
                ", flights=" + flights +
                '}';
    }
}