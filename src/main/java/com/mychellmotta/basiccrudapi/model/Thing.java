package com.mychellmotta.basiccrudapi.model;

import com.mychellmotta.basiccrudapi.dto.ThingSheetDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Thing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "description", unique = true)
    private String description;
    @Column(name = "image_url")
    private String imageUrl;

    public Thing(ThingSheetDto thingSheetDto) {
        this.description = thingSheetDto.getDescription();
        this.imageUrl = thingSheetDto.getImageUrl();
    }
}
