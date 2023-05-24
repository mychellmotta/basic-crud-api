package com.mychellmotta.basiccrudapi.service;

import com.mychellmotta.basiccrudapi.dto.ThingRequestDto;
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
import java.util.Optional;
import java.util.UUID;

import static com.mychellmotta.basiccrudapi.utils.Utils.convertMultiPartToFile;

@Service
public class ThingService {

    private final ThingRepository repository;

    public ThingService(ThingRepository repository) {
        this.repository = repository;
    }

    public List<Thing> getAll() {
        return repository.findAll();
    }

    public List<Thing> getByDescription(String description) {
        return repository.hasDescription(description);
    }

    @Transactional
    public Thing save(ThingRequestDto thingRequestDto) {
        var thingOptional =
                repository.findByDescription(thingRequestDto.description());

        if (thingOptional.isPresent()) {
            throw new IllegalStateException("description already exists");
        }

        return repository.save(new Thing(thingRequestDto));
    }

    public List<ThingSheetDto> getListFromExcel(MultipartFile multipartfile) {
        File file;
        try {
            file = convertMultiPartToFile(multipartfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Poiji.fromExcel(file, ThingSheetDto.class);
    }

    @Transactional
    public void delete(UUID id) {
        Optional<Thing> optionalThing = repository.findById(id);
        if (optionalThing.isEmpty()) {
            throw new IllegalStateException("can't find a thing with this id");
        }

        repository.deleteById(id);
    }
}
