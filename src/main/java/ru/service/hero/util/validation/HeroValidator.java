package ru.service.hero.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.service.hero.dto.hero.HeroRequest;
import ru.service.hero.service.HeroService;
import ru.service.hero.util.exception.BindingValidationException;

@Component
@Slf4j
public class HeroValidator implements Validator {

    private final HeroService heroService;

    @Autowired
    public HeroValidator(HeroService heroService) {
        this.heroService = heroService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HeroRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HeroRequest heroDto = (HeroRequest) target;
        log.info("Checking new hero for: {}", heroDto.getUserId());
        validateCreate(heroDto, errors);
        if (errors.hasErrors()) {
            throw new BindingValidationException((BindingResult) errors);
        }
    }

    private void validateCreate(HeroRequest heroDto, Errors errors) {
        int points = heroDto.getStr() + heroDto.getDex() + heroDto.getCon() + heroDto.getIntl() + heroDto.getWis() + heroDto.getCha();

        if (points > 600) {
            errors.rejectValue("", "", String.format("Too many points have been used. Overused points: %d", points - 600));
            log.error("Too many points: {} were used to create a hero: {}", points, heroDto);
        }

        if (points < 600) {
            errors.rejectValue("", "", String.format("Too few points were used. Unused points: %d", 600 - points));
            log.error("Too few points: {} were used to create a hero: {}", points, heroDto);
        }

        if (heroService.getByUserIdOptional(heroDto.getUserId()).isPresent()) {
            errors.rejectValue("", "", String.format("The hero is already linked to the user %s", heroDto.getUserId()));
            log.error(String.format("The hero is already linked to the user %s", heroDto.getUserId()));
        }
    }
}