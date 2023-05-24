package com.mychellmotta.basiccrudapi.dto;

public record ThingRequestDto(String description, String imageUrl) {

    public ThingRequestDto(ThingSheetDto thingSheetDto) {
        this(thingSheetDto.getDescription(), thingSheetDto.getImageUrl());
    }
}
