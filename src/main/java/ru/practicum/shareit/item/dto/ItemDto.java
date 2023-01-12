package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotNull
    @NotBlank(message = "name should not be blank")
    private String name;
    @NotNull
    @NotBlank(message = "description should not be blank")
    private String description;
    @NotNull
    private Boolean available;

    private Long requestId;
}