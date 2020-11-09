package com.controller;

import com.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Passenger;
import com.service.PassengerService;
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
public class PassengerController {
    private BufferedReader bufferedReader;
    private PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService){
        this.passengerService = passengerService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/passenger/save", produces = "application/json")
    public ResponseEntity<String> save(HttpServletRequest req, HttpServletResponse resp){
        try{
            Passenger passenger = requestRead(req);
            passengerService.save(passenger);
            return new ResponseEntity<>("Passenger was saved", HttpStatus.CREATED);
        }catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/passenger/findById", produces = "text/plain")
    public ResponseEntity<String> findById(@RequestParam(value = "id") Long id) {
        try {
            Passenger passenger = passengerService.findById(id);
            return new ResponseEntity<>("Passenger was found " + passenger.toString(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/passenger/update", produces = "application/json")
    public ResponseEntity<String> update(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Passenger passenger = requestReadUpdate(req);
            passengerService.update(passenger);
            return new ResponseEntity<>("Passenger was updated", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/passenger/delete", produces = "text/plain")
    public ResponseEntity<String> delete(@RequestParam(value = "id") Long id) {
        try {
            passengerService.delete(id);
            return new ResponseEntity<>("Passenger was deleted", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/passenger/regularPassengers", produces = "application/json")
    public void regularPassengers(HttpServletRequest req, HttpServletResponse resp)throws Exception{
        try{
            String yearString = req.getParameter("year");
            List<Passenger> passengers = passengerService.regularPassengers(Integer.parseInt(yearString));
            for(Passenger p : passengers){
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

    private Passenger requestReadUpdate(HttpServletRequest req)throws IllegalArgumentException, IOException, ParseException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(str);
        String id = jsonNode.get("id").asText();
        String lastName = jsonNode.get("lastName").asText();
        String nationality = jsonNode.get("nationality").asText();
        String dateOfBirthString = jsonNode.get("dateOfBirth").asText();
        Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthString);
        String passportCode = jsonNode.get("passportCode").asText();
        String flights = jsonNode.get("flights").asText();

        Passenger passenger = passengerService.findById(Long.parseLong(id));
        passenger.setLastName(lastName);
        passenger.setNationality(nationality);
        passenger.setDateOfBirth(dateOfBirth);
        passenger.setPassportCode(passportCode);
        passenger.setFlights(flights);

        return passenger;
    }

    private Passenger requestRead(HttpServletRequest req)throws IllegalArgumentException, IOException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        Passenger passenger = getPassenger(str);
        if(passenger == null){
            throw new IllegalArgumentException("Request is empty");
        }
        return passenger;
    }

    private Passenger getPassenger(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Passenger passenger = mapper.readValue(response, Passenger.class);
            return passenger;
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