package com.controller;

import com.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Plane;
import com.service.PlaneService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class PlaneController {
    private BufferedReader bufferedReader;
    private PlaneService planeService;

    @Autowired
    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/plane/save", produces = "application/json")
    public ResponseEntity<String> save(HttpServletRequest req, HttpServletResponse resp){
        try{
            Plane plane = requestRead(req);
            planeService.save(plane);
            return new ResponseEntity<>("Plane was saved", HttpStatus.OK);
        }catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plane/find", produces = "text/plain")
    public ResponseEntity<String> findById(@RequestParam(value = "id") Long id) {
        try {
             Plane plane = planeService.findById(id);
            return new ResponseEntity<>("Plane was found: " + plane.toString(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/plane/update", produces = "application/json")
    public ResponseEntity<String> update(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Plane plane = requestReadUpdate(req);
            planeService.update(plane);
            return new ResponseEntity<>("Plane was updated", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/plane/delete", produces = "text/plain")
    public ResponseEntity<String> delete(@RequestParam(value = "id") Long id) {
        try {
            planeService.delete(id);
            return new ResponseEntity<>("Plane was deleted", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plane/oldPlanes", produces = "application/json")
    public void findById(HttpServletResponse resp)throws Exception{
        try{
            List<Plane> planes = planeService.oldPlanes();
            for(Plane p : planes){
                resp.getWriter().println(p.toString());
            }
        }catch (NoSuchElementException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(resp.getStatus());
        }
        catch (HibernateException e){
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            resp.getWriter().println(resp.getStatus());
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(resp.getStatus());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plane/regularPlanes", produces = "application/json")
    public void regularPlanes(HttpServletRequest req, HttpServletResponse resp)throws Exception{
        try{
            String yearString = req.getParameter("year");
            List<Plane> planes = planeService.regularPlanes(Integer.parseInt(yearString));
            for(Plane p : planes){
                resp.getWriter().println(p.toString());
            }
        }catch (NoSuchElementException e){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(resp.getStatus());
        }
        catch (HibernateException e){
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            resp.getWriter().println(resp.getStatus());
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(resp.getStatus());
        }
    }

    private Plane requestRead(HttpServletRequest req)throws IllegalArgumentException, IOException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        Plane plane = getPlane(str);
        if(plane == null){
            throw new IllegalArgumentException("Request is empty");
        }
        return plane;
    }

    private Plane requestReadUpdate(HttpServletRequest req)throws IllegalArgumentException, IOException, ParseException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(str);
        String id = jsonNode.get("id").asText();
        String model = jsonNode.get("model").asText();
        String code = jsonNode.get("code").asText();
        String yearProducedString = jsonNode.get("yearProduced").asText();
        Date yearProduced = new SimpleDateFormat("yyyy-MM-dd").parse(yearProducedString);
        String avgFuelConsumptionString = jsonNode.get("avgFuelConsumption").asText();
        Double avgFuelConsumption = Double.parseDouble(avgFuelConsumptionString);

        Plane plane = planeService.findById(Long.parseLong(id));
        plane.setModel(model);
        plane.setCode(code);
        plane.setYearProduced(yearProduced);
        plane.setAvgFuelConsumption(avgFuelConsumption);

        return plane;
    }

    private Plane getPlane(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Plane plane = mapper.readValue(response, Plane.class);
            return plane;
        }catch (Exception e){
            return null;
        }
    }

    private String bodyContent(BufferedReader reader) throws IOException {
        String input;
        StringBuilder requestBody = new StringBuilder();
        while((input = reader.readLine()) != null) {
            requestBody.append(input);
        }
        return requestBody.toString();
    }
}
