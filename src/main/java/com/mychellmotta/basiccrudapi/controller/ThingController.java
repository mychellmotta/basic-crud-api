package com.mychellmotta.basiccrudapi.controller;

import com.mychellmotta.basiccrudapi.model.Thing;
import com.mychellmotta.basiccrudapi.service.EmailService;
import com.mychellmotta.basiccrudapi.service.ThingService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/thing")
@CrossOrigin("*")
public class ThingController {

    private final ThingService thingService;
    private final EmailService emailService;

    public ThingController(ThingService thingService, EmailService emailService) {
        this.thingService = thingService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<Thing>> findAll() {
        var things = thingService.findAll();
        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @GetMapping("/{description}")
    public ResponseEntity<List<Thing>> findByDescription(@PathVariable("description") String description) {
        var things = thingService.findByDescription(description);
        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Thing> save(@RequestBody Thing thing) {
        return new ResponseEntity<>(thingService.save(thing), HttpStatus.CREATED);
    }

    @PostMapping("/saveFromExcel")
    public ResponseEntity<List<Thing>> saveFromExcel(@RequestParam("file") MultipartFile file) {
        var things = thingService.getListFromExcel(file)
                .stream()
                .map(Thing::new)
                .toList();
        things.forEach(thingService::save);
        return new ResponseEntity<>(things, HttpStatus.CREATED);
    }

    // TODO
    @PutMapping("/update")
    public ResponseEntity<Thing> update(@RequestBody Thing thing) {
         return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        thingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void sendEmail(String message) {
        //  testing the email function
        try {
            emailService.sendEmail(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
