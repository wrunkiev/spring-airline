package com.service;

import com.dao.FlightDAO;
import com.dao.PlaneDAO;
import com.model.Flight;
import com.model.Plane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaneService {
    private PlaneDAO planeDAO;
    private FlightDAO flightDAO;

    @Autowired
    public PlaneService(PlaneDAO planeDAO, FlightDAO flightDAO){
        this.planeDAO = planeDAO;
        this.flightDAO = flightDAO;
    }

    public Plane save(Plane plane)throws Exception{
        return planeDAO.save(plane);
    }

    public Plane findById(long id)throws NoSuchElementException {
        if(planeDAO.findById(id) == null){
            throw new NoSuchElementException();
        }
        return planeDAO.findById(id);
    }

    public Plane update(Plane plane)throws Exception {
        return planeDAO.update(plane);
    }

    public void delete(long id)throws Exception{
        planeDAO.delete(id);
    }

    public List<Plane> oldPlanes(){
        List<Plane> planes = new ArrayList<>();
        for (Plane p : planeDAO.allPlanes()){
            Date dateCur = new Date();
            Date datePlane = new Date(p.getYearProduced().getTime());
            if(dateCur.getYear() - datePlane.getYear() > 20){
                planes.add(p);
            }
        }
        return planes;
    }

    public List<Plane> regularPlanes(int year){
        int count;
        List<Plane> planes = new ArrayList<>();
        for(Plane p : planeDAO.allPlanes()){
            count = 0;
            for(Flight f : flightDAO.allFlights()){
                if(f != null && f.getDateFlight() != null && Integer.parseInt(f.getDateFlight().toString().substring(0, 4)) == year && f.getPlane().getId() == p.getId()){
                    count++;
                }
            }
            if(count > 300){
                planes.add(p);
            }
        }
        return planes;
    }
}