package com.mychellmotta.basiccrudapi.service;

import com.mychellmotta.basiccrudapi.dto.ThingSheetDto;
import com.mychellmotta.basiccrudapi.model.Thing;
import com.mychellmotta.basiccrudapi.repository.ThingRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class ThingService {

    private final ThingRepository repository;
    private final FileService fileService;
    private final EmailService emailService;

    public ThingService(ThingRepository repository, FileService fileService, EmailService emailService) {
        this.repository = repository;
        this.fileService = fileService;
        this.emailService = emailService;
    }

    public List<Thing> findAll() {
        return repository.findAll();
    }

    public Thing findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("can't find a thing with id: " + id));
    }

    public List<Thing> findAllWithDescription(String description) {
        return repository.findAllWithDescription(description);
    }

    @Transactional
    public Thing save(Thing thing) {
        validateUniqueDescription(thing.getDescription(), null);
        return repository.save(thing);
    }

    @Transactional
    public Thing update(UUID id, Thing thing) {
        var existingThing = findById(id);
        validateUniqueDescription(thing.getDescription(), existingThing);
        existingThing.setDescription(thing.getDescription());
        existingThing.setImageUrl(thing.getImageUrl());
        return repository.save(existingThing);
    }

    public List<ThingSheetDto> getListFromExcel(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new IllegalArgumentException("input multipartFile cannot be null");
        }
        return fileService.processExcelData(multipartFile);
    }

    private void validateUniqueDescription(String description, Thing currentThing) {
        var existingThing = repository.findByDescription(description);
        if (existingThing.isPresent() &&
                (currentThing == null || !existingThing.get().equals(currentThing))) {
            throw new IllegalStateException("thing with description: '" + description + "' already exists");
        }
    }

    @Transactional
    public void delete(UUID id) {
        var thing = findById(id);
        repository.delete(thing);
    }

    private void sendEmail(String message) {
        //  testing the email function
        try {
            emailService.sendEmail(message);
        } catch (MessagingException e) {
            throw new RuntimeException("error sending the email: " + e.getMessage(), e);
        }
    }
}
