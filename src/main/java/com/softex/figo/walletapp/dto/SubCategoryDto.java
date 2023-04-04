package com.softex.figo.walletapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubCategoryDto {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Icon is mandatory")
    private String icon;
    @NotBlank(message = "Category id is mandatory")
    private String categoryId;
}
