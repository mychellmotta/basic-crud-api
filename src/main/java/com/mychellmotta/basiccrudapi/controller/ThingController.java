package com.mychellmotta.basiccrudapi.controller;

import com.mychellmotta.basiccrudapi.model.Thing;
import com.mychellmotta.basiccrudapi.service.EmailService;
import com.mychellmotta.basiccrudapi.service.ThingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/things")
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

    @GetMapping("/findById/{id}")
    public ResponseEntity<Thing> findById(@PathVariable("id") UUID id) {
        var thing = thingService.findById(id);
        return new ResponseEntity<>(thing, HttpStatus.OK);
    }

    @GetMapping("/findAllWithDescription/{description}")
    public ResponseEntity<List<Thing>> findByDescription(@PathVariable("description") String description) {
        var things = thingService.findAllWithDescription(description);
        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Thing> save(@RequestBody @Validated Thing thing) {
        var thingSaved = thingService.save(thing);
        emailService.sendEmail("save", "thing saved!<br><br>" +
                "id: " + thingSaved.getId() + "<br>" +
                "description: " + thingSaved.getDescription() + "<br>" +
                "imageUrl: <img src=\"" + thingSaved.getImageUrl() + "\" width=\"200\" height=\"200\">");
        return new ResponseEntity<>(thingSaved, HttpStatus.CREATED);
    }

    @PostMapping("/saveFromExcel")
    public ResponseEntity<List<Thing>> saveFromExcel(@RequestParam("file") MultipartFile file) {
        var things = thingService.getListFromExcel(file)
                .stream()
                .map(Thing::new)
                .toList();
        things.forEach(thingService::save);
        emailService.sendEmail("saveFromExcel", "file " + file.getOriginalFilename() + " fetched!");
        return new ResponseEntity<>(things, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Thing> update(@PathVariable("id") UUID id, @RequestBody @Validated Thing thing) {
        var updatedThing = thingService.update(id, thing);
        emailService.sendEmail("update",
                "thing with id: " + id + " updated!<br><br>" +
                        "description: " + updatedThing.getDescription() + "<br>" +
                        "imageUrl: <img src=\"" + updatedThing.getImageUrl() + "\" width=\"200\" height=\"200\">");
        return new ResponseEntity<>(updatedThing, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        thingService.delete(id);
        emailService.sendEmail("delete", "thing with id: " + id + " deleted!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
