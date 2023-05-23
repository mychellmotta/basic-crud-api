package com.mychellmotta.basiccrudapi.service;

import com.mychellmotta.basiccrudapi.dto.ThingRequestDto;
import com.mychellmotta.basiccrudapi.dto.ThingResponseDto;
import com.mychellmotta.basiccrudapi.dto.ThingSheet;
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

//    @Value("${filePath}")
//    public String FILE_PATH;
    private final ThingRepository repository;

    public ThingService(ThingRepository repository) {
        this.repository = repository;
    }

    public List<ThingResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(ThingResponseDto::new)
                .toList();
    }

    public List<ThingResponseDto> getByDescription(String description) {
        return repository.hasDescription(description)
                .stream()
                .map(ThingResponseDto::new)
                .toList();
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

    public List<ThingRequestDto> getListFromExcel(MultipartFile multipartfile) {
        File file;
        try {
            file = convertMultiPartToFile(multipartfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<ThingSheet> things = Poiji.fromExcel(file, ThingSheet.class);
        return things
                .stream()
                .map(ThingRequestDto::new)
                .toList();
    }

    public void delete(UUID id) {
        Optional<Thing> optionalThing = repository.findById(id);
        if (optionalThing.isEmpty()) {
            throw new RuntimeException("can't find a thing with this id");
        }

        repository.deleteById(id);
    }


}
