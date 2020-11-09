package com.dao;

import com.model.Plane;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlaneDAO {
    private static final String SELECT_FROM = "SELECT * FROM PLANE";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Plane save(Plane plane) throws Exception{
        checkPlane(plane);
        entityManager.persist(plane);
        return plane;
    }

    @Transactional
    public Plane findById(long id){
        return entityManager.find(Plane.class, id);
    }

    @Transactional
    public Plane update(Plane plane) throws Exception{
        checkPlane(plane);
        entityManager.merge(plane);
        return plane;
    }

    @Transactional
    public void delete(long id)throws Exception{
        Plane plane = entityManager.find(Plane.class, id);
        checkPlane(plane);
        entityManager.getTransaction().begin();
        entityManager.remove(plane);
        entityManager.getTransaction().commit();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<Plane> allPlanes(){
        Query query = entityManager.createNativeQuery(SELECT_FROM, Plane.class);
        return query.getResultList();
    }

    private static void checkPlane(Plane plane)throws Exception{
        if(plane == null){
            throw new Exception("Exception in method PlaneDAO.checkPlane. Plane can't be null.");
        }
    }
}