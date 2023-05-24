package com.mychellmotta.basiccrudapi.controller;

import com.mychellmotta.basiccrudapi.dto.ThingRequestDto;
import com.mychellmotta.basiccrudapi.dto.ThingResponseDto;
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
@RequestMapping("/thing")
@CrossOrigin("*")
public class ThingController {

    private final ThingService thingService;
    private final EmailService emailService;

    public ThingController(ThingService thingService, EmailService emailService) {
        this.thingService = thingService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<ThingResponseDto>> getAll() {
        var things = thingService.getAll()
                .stream()
                .map(ThingResponseDto::new)
                .toList();

        // testing the email function
        try {
            emailService.sendEmail("message here");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @GetMapping("/{description}")
    public ResponseEntity<List<ThingResponseDto>> getByDescription(@PathVariable("description") String description) {
        var things = thingService.getByDescription(description)
                .stream()
                .map(ThingResponseDto::new)
                .toList();
        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Thing> save(@RequestBody ThingRequestDto thingRequestDto) {
        var thing = thingService.save(thingRequestDto);
        return new ResponseEntity<>(thing, HttpStatus.CREATED);
    }

    @PostMapping("/saveFromExcel")
    public ResponseEntity<List<ThingRequestDto>> saveFromExcel(@RequestParam("file") MultipartFile file) {
        var things = thingService.getListFromExcel(file)
                .stream()
                .map(ThingRequestDto::new)
                .toList();
        things.forEach(thingService::save);
        return new ResponseEntity<>(things, HttpStatus.CREATED);
    }

    // TODO
    @PutMapping("/update")
    public ResponseEntity<Thing> update(@RequestBody ThingRequestDto thingRequestDto) {
         return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        thingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
