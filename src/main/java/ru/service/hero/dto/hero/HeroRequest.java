package ru.service.hero.dto.hero;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroRequest {

    @Size(min = 1, max = 100, message = "The hero's name must consist of at least one character")
    @NotBlank(message = "The hero's name must consist of at least one character")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "The hero's name can only consist of letters of the Russian or Latin alphabet")
    private String name;

    @Size(min = 1, max = 100, message = "The last name of the hero must consist of at least one character")
    @NotBlank(message = "The last name of the hero must consist of at least one character")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "The hero's surname can consist only of letters of the Russian or Latin alphabet")
    private String lastname;

    @Min(value = 70, message = "Strength should be at least 70")
    @Max(value = 180, message = "Strength should be no more than 180")
    @NotNull(message = "Strength must be at least 70")
    private Integer str;

    @Min(value = 70, message = "Dexterity should be at least 70")
    @Max(value = 180, message = "Dexterity should be no more than 180")
    @NotNull(message = "Dexterity should be at least 70")
    private Integer dex;

    @Min(value = 70, message = "Constitution should be at least 70")
    @Max(value = 180, message = "Constitution should not be more than 180")
    @NotNull(message = "Constitution should be at least 70")
    private Integer con;

    @Min(value = 70, message = "Intelligence should be at least 70")
    @Max(value = 180, message = "Intelligence should be no more than 180")
    @NotNull(message = "Intelligence should be at least 70")
    private Integer intl;

    @Min(value = 70, message = "Wisdom should be at least 70")
    @Max(value = 180, message = "Wisdom should be no more than 180")
    @NotNull(message = "Wisdom should be at least 70")
    private Integer wis;

    @Min(value = 70, message = "Charisma should be at least 70")
    @Max(value = 180, message = "Charisma should be no more than 180")
    @NotNull(message = "Charisma should be at least 70")
    private Integer cha;

    @JsonIgnore
    private Long userId;
}
