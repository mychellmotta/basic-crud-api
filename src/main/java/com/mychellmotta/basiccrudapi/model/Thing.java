package com.mychellmotta.basiccrudapi.model;

import com.mychellmotta.basiccrudapi.dto.ThingRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Thing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;

    public Thing(ThingRequestDto thingRequestDto) {
        this.description = thingRequestDto.description();
        this.imageUrl = thingRequestDto.imageUrl();
    }
}
