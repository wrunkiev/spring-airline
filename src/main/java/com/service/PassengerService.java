package com.service;

import com.dao.FlightDAO;
import com.dao.PassengerDAO;
import com.model.Flight;
import com.model.Passenger;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PassengerService {
    private PassengerDAO passengerDAO;
    private FlightService flightService;

    @Autowired
    public PassengerService(PassengerDAO passengerDAO, FlightService flightService){
        this.passengerDAO = passengerDAO;
        this.flightService = flightService;
    }

    public Passenger save(Passenger passenger)throws Exception{
        return passengerDAO.save(passenger);
    }

    public Passenger findById(long id)throws NoSuchElementException {
        if(passengerDAO.findById(id) == null){
            throw new NoSuchElementException();
        }
        return passengerDAO.findById(id);
    }

    public Passenger update(Passenger passenger)throws Exception {
        return passengerDAO.update(passenger);
    }

    public void delete(long id)throws Exception{
        passengerDAO.delete(id);
    }

    public List<Passenger> regularPassengers(int year){
        List<Passenger> passengers = new ArrayList<>();
        List<Passenger> allPassengers = passengerDAO.allPassengers();
        List<Flight> flights = new ArrayList<>();

        for (Passenger p : allPassengers){
            int count = 0;
            if(p != null){
                String [] pasFlight = p.getFlights().split("\\|");
                for(String f : pasFlight){
                    if(f != null && !f.equals("")){
                        Flight flight = flightService.findById(Long.parseLong(f));
                        flights.add(flight);
                    }
                }
            }
            for(Flight f : flights){
                if(f != null && f.getDateFlight() != null && Integer.parseInt(f.getDateFlight().toString().substring(0, 4)) == year){
                    count++;
                }
            }
            if(count > 25){
                passengers.add(p);
            }
        }
        return passengers;
    }
}