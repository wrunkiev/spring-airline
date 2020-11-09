package com.dao;

import com.model.Flight;
import com.model.Passenger;
import com.model.Plane;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class FlightDAO {
    private static final String SELECT_FROM = "SELECT * FROM FLIGHT";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Flight save(Flight flight) throws Exception{
        checkFlight(flight);
        entityManager.persist(flight);
        return flight;
    }

    @Transactional
    public Flight findById(long id){
        return entityManager.find(Flight.class, id);
    }

    @Transactional
    public Flight update(Flight flight) throws Exception{
        checkFlight(flight);
        entityManager.merge(flight);
        return flight;
    }

    @Transactional
    public void delete(long id)throws Exception{
        Flight flight = entityManager.find(Flight.class, id);
        checkFlight(flight);
        entityManager.remove(flight);
    }

    private static void checkFlight(Flight flight)throws Exception{
        if(flight == null){
            throw new Exception("Exception in method FlightDAO.checkFlight. Flight can't be null.");
        }
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<Flight> allFlights(){
        Query query = entityManager.createNativeQuery(SELECT_FROM, Flight.class);
        return query.getResultList();
    }
}