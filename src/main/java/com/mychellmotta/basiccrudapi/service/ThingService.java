package com.mychellmotta.basiccrudapi.service;

import com.mychellmotta.basiccrudapi.dto.ThingSheetDto;
import com.mychellmotta.basiccrudapi.model.Thing;
import com.mychellmotta.basiccrudapi.repository.ThingRepository;
import com.poiji.bind.Poiji;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.mychellmotta.basiccrudapi.utils.Utils.convertMultiPartToFile;

@Service
public class ThingService {

    private final ThingRepository repository;

    public ThingService(ThingRepository repository) {
        this.repository = repository;
    }

    public List<Thing> findAll() {
        return repository.findAll();
    }

    public Thing findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("can't find a thing with this id"));
    }

    public List<Thing> findByDescription(String description) {
        return repository.hasDescription(description);
    }

    @Transactional
    public Thing save(Thing thing) {
        var thingOptional =
                repository.findByDescription(thing.getDescription());
        if (thingOptional.isPresent()) {
            throw new IllegalStateException(
                    "description: '" + thingOptional.get().getDescription() + "' already exists");
        }

        return repository.save(thing);
    }

    @Transactional
    public Thing update(UUID id, Thing thing) {
        var existingThing = findById(id);

        existingThing.setDescription(thing.getDescription());
        existingThing.setImageUrl(thing.getImageUrl());

        return repository.save(existingThing);
    }

    public List<ThingSheetDto> getListFromExcel(MultipartFile multipartFile) {
        if (multipartFile == null) {
            throw new IllegalArgumentException("input multipartfile cannot be null");
        }

        File file = null;
        try {
            file = convertMultiPartToFile(multipartFile);

            // TODO: do the logic here and return to controller the list of Things

            return Poiji.fromExcel(file, ThingSheetDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    @Transactional
    public void delete(UUID id) {
        var thing = findById(id);
        repository.delete(thing);
    }
}
