package com.mychellmotta.basiccrudapi.dto;

public record ThingRequestDto(String description, String imageUrl) {

    public ThingRequestDto(ThingSheet thingSheet) {
        this(thingSheet.getDescription(), thingSheet.getImageUrl());
    }
}
