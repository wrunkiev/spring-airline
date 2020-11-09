package com.controller;

import com.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Flight;
import com.model.Plane;
import com.service.FlightService;
import com.service.PlaneService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
public class FlightController {
    private BufferedReader bufferedReader;
    private FlightService flightService;
    private PlaneService planeService;

    @Autowired
    public FlightController(FlightService flightService, PlaneService planeService){
        this.flightService = flightService;
        this.planeService = planeService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/flight/save", produces = "application/json")
    public ResponseEntity<String> save(HttpServletRequest req, HttpServletResponse resp){
        try{
            Flight flight = requestRead(req);
            flightService.save(flight);
            return new ResponseEntity<>("Flight was save", HttpStatus.OK);
        }catch (BadRequestException e) {
            return new ResponseEntity<>("Flight wasn't save", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Flight wasn't save", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/flight/find", produces = "application/json")
    public ResponseEntity<String> findById(@RequestParam(value = "id") Long id) {
        try{
            Flight flight = flightService.findById(id);
            return new ResponseEntity<>("Flight was found " + flight.toString(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/flight/update", produces = "application/json")
    public ResponseEntity<String> update(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Flight flight = requestReadUpdate(req);
            flightService.update(flight);
            return new ResponseEntity<>("Flight was updated", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/flight/delete", produces = "text/plain")
    public ResponseEntity<String> delete(@RequestParam(value = "id") Long id) {
        try {
            flightService.delete(id);
            return new ResponseEntity<>("Flight was deleted", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Flight requestReadUpdate(HttpServletRequest req)throws IllegalArgumentException, IOException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        Flight flight = getFlight(str);
        return flight;
    }

    private Flight requestRead(HttpServletRequest req)throws IllegalArgumentException, IOException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        Flight flight = getFlight(str);
        flight.setId(null);
        if(flight == null){
            throw new IllegalArgumentException("Request is empty");
        }
        return flight;
    }

    private Flight getFlight(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);
            String flightId = jsonNode.get("id").asText();
            Long id = Long.parseLong(flightId);
            String planeId = jsonNode.get("plane").asText();
            Plane plane = planeService.findById(Long.parseLong(planeId));
            String passenger = jsonNode.get("passenger").asText();
            String dateFlightString = jsonNode.get("dateFlight").asText();
            Date dateFlight = new SimpleDateFormat("yyyy-MM-dd").parse(dateFlightString);
            String cityFrom = jsonNode.get("cityFrom").asText();
            String cityTo = jsonNode.get("cityTo").asText();

            Flight flight = new Flight();
            flight.setId(id);
            flight.setPlane(plane);
            flight.setPassenger(passenger);
            flight.setDateFlight(dateFlight);
            flight.setCityFrom(cityFrom);
            flight.setCityTo(cityTo);

            return flight;
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