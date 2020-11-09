package com.model;

import java.util.Date;

public class Filter {
    private Date dateFlight;
    private String cityFrom;
    private String cityTo;
    private String model;

    public Filter(Date dateFlight, String cityFrom, String cityTo, String model) {
        this.dateFlight = dateFlight;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.model = model;
    }

    public Date getDateFlight() {
        return dateFlight;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public String getModel() {
        return model;
    }
}
