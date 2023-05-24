package com.mychellmotta.basiccrudapi.controller;

import com.mychellmotta.basiccrudapi.dto.ThingRequestDto;
import com.mychellmotta.basiccrudapi.dto.ThingResponseDto;
import com.mychellmotta.basiccrudapi.model.Thing;
import com.mychellmotta.basiccrudapi.service.ThingService;
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

    private final ThingService service;

    public ThingController(ThingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ThingResponseDto>> getAll() {
        var things = service.getAll()
                .stream()
                .map(ThingResponseDto::new)
                .toList();
        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @GetMapping("/{description}")
    public ResponseEntity<List<ThingResponseDto>> getByDescription(@PathVariable("description") String description) {
        var things = service.getByDescription(description)
                .stream()
                .map(ThingResponseDto::new)
                .toList();
        return new ResponseEntity<>(things, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Thing> save(@RequestBody ThingRequestDto thingRequestDto) {
        var thing = service.save(thingRequestDto);
        return new ResponseEntity<>(thing, HttpStatus.CREATED);
    }

    @PostMapping("/saveFromExcel")
    public ResponseEntity<List<ThingRequestDto>> saveFromExcel(@RequestParam("file") MultipartFile file) {
        var things = service.getListFromExcel(file)
                .stream()
                .map(ThingRequestDto::new)
                .toList();
        things.forEach(service::save);
        return new ResponseEntity<>(things, HttpStatus.CREATED);
    }

//    @PutMapping("/update")
//    public ResponseEntity<Thing> update(@RequestBody AnythingRequestDto anythingRequestDto) {
//         return null;
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
