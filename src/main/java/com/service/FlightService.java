package com.service;

import com.dao.FlightDAO;
import com.dao.PlaneDAO;
import com.model.Filter;
import com.model.Flight;
import com.model.Plane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FlightService {
    private FlightDAO flightDAO;

    @Autowired
    public FlightService(FlightDAO flightDAO){
        this.flightDAO = flightDAO;
    }

    public Flight save(Flight flight)throws Exception{
        return flightDAO.save(flight);
    }

    public Flight findById(long id)throws NoSuchElementException {
        if(flightDAO.findById(id) == null){
            throw new NoSuchElementException();
        }
        return flightDAO.findById(id);
    }

    public Flight update(Flight flight)throws Exception {
        return flightDAO.update(flight);
    }

    public void delete(long id)throws Exception{
        flightDAO.delete(id);
    }

    @Transactional
    public List<Flight> flightsByDate(Filter filter){
        List<Flight> flights = new ArrayList<>();
        return flights;
    }
}