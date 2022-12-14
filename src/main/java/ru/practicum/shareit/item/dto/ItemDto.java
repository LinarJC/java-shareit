package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название вещи не может быть пустым")
    @NotNull
    private String name;
    @NotNull
    @NotBlank(message = "Описание вещи не может быть пустым")
    private String description;
    @NotNull
    private Boolean available;
}