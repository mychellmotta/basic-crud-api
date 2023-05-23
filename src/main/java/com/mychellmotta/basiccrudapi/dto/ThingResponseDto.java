package com.mychellmotta.basiccrudapi.dto;

import com.mychellmotta.basiccrudapi.model.Thing;

import java.util.UUID;

public record ThingResponseDto(UUID id, String description, String imageUrl) {

    public ThingResponseDto(Thing thing) {
        this(
                thing.getId(),
                thing.getDescription(),
                thing.getImageUrl()
        );
    }
}
