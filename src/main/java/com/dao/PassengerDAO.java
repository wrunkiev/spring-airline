package com.dao;

import com.model.Passenger;
import com.model.Plane;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PassengerDAO {
    private static final String SELECT_FROM = "SELECT * FROM PASSENGER";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Passenger save(Passenger passenger) throws Exception{
        checkPassenger(passenger);
        entityManager.persist(passenger);
        return passenger;
    }

    @Transactional
    public Passenger findById(long id){
        return entityManager.find(Passenger.class, id);
    }

    @Transactional
    public Passenger update(Passenger passenger) throws Exception{
        checkPassenger(passenger);
        entityManager.merge(passenger);
        return passenger;
    }

    @Transactional
    public void delete(long id)throws Exception{
        Passenger passenger = entityManager.find(Passenger.class, id);
        checkPassenger(passenger);
        entityManager.remove(passenger);
    }

    private static void checkPassenger(Passenger passenger)throws Exception{
        if(passenger == null){
            throw new Exception("Exception in method PassengerDAO.checkPassenger. Passenger can't be null.");
        }
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<Passenger> allPassengers(){
        Query query = entityManager.createNativeQuery(SELECT_FROM, Passenger.class);
        return query.getResultList();
    }
}