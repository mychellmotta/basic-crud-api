package com.mychellmotta.basiccrudapi.dto;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;

@Getter
public class ThingSheet {

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String description;

    @ExcelCell(1)
    private String imageUrl;
}
