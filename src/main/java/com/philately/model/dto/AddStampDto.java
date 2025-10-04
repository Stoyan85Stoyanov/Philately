package com.philately.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddStampDto {

    @NotBlank
    @Size(min = 5, max = 20)
    private String name;

    @NotBlank
    @Size(min = 5, max = 25)
    private String description;

    @NotBlank
    @Size(max = 150)
    private String imageUrl;

    @NotNull
    @Positive
    private int price;

    @NotBlank(message = "You must select a type of paper!")
    private String paper;

}
