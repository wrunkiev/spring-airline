package com.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FLIGHT")
public class Flight {
    @Id
    @Column(name = "FLIGHT_ID")
    @SequenceGenerator(name = "FLIGHT_SEQ", sequenceName = "FLIGHT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLIGHT_SEQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FLIGHT_PLANE_ID")
    private Plane plane;

    @Column(name = "FLIGHT_LIST_PASSENGER_ID")
    private String passenger;

    @Column(name = "FLIGHT_DATE_FLIGHT")
    @Temporal(value = TemporalType.DATE)
    private Date dateFlight;

    @Column(name = "FLIGHT_CITY_FROM")
    private String cityFrom;

    @Column(name = "FLIGHT_CITY_TO")
    private String cityTo;

    public Flight(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public Date getDateFlight() {
        return dateFlight;
    }

    public void setDateFlight(Date dateFlight) {
        this.dateFlight = dateFlight;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", plane=" + plane +
                ", passenger=" + passenger +
                ", dateFlight=" + dateFlight +
                ", cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                '}';
    }
}