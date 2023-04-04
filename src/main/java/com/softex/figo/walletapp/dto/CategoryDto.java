package com.softex.figo.walletapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class CategoryDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3)
    private String name;
    @NotBlank(message = "Icon is mandatory")
    private String icon;
    @NotBlank(message = "Type is mandatory")
    private String type;
}
