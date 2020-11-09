package com.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PLANE")
public class Plane {
    @Id
    @Column(name = "PLANE_ID")
    @SequenceGenerator(name = "PLANE_SEQ", sequenceName = "PLANE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANE_SEQ")
    private Long id;

    @Column(name = "PLANE_MODEL")
    private String model;

    @Column(name = "PLANE_CODE")
    private String code;

    @Column(name = "PLANE_YEAR_PRODUCED")
    @Temporal(value = TemporalType.DATE)
    private Date yearProduced;

    @Column(name = "PLANE_AVG_FUEL_CONSUMPTION")
    private Double avgFuelConsumption;

    public Plane() {
    }

    public Plane(String model, String code, Date yearProduced, Double avgFuelConsumption) {
        this.model = model;
        this.code = code;
        this.yearProduced = yearProduced;
        this.avgFuelConsumption = avgFuelConsumption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getYearProduced() {
        return yearProduced;
    }

    public void setYearProduced(Date yearProduced) {
        this.yearProduced = yearProduced;
    }

    public Double getAvgFuelConsumption() {
        return avgFuelConsumption;
    }

    public void setAvgFuelConsumption(Double avgFuelConsumption) {
        this.avgFuelConsumption = avgFuelConsumption;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", code='" + code + '\'' +
                ", yearProduced=" + yearProduced +
                ", avgFuelConsumption=" + avgFuelConsumption +
                '}';
    }
}