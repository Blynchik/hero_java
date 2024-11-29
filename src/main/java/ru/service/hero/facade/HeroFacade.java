package ru.service.hero.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.service.hero.dto.hero.HeroRequest;
import ru.service.hero.dto.hero.HeroResponse;
import ru.service.hero.model.Hero;
import ru.service.hero.service.HeroService;
import ru.service.hero.util.validation.HeroValidator;

@Service
@Slf4j
public class HeroFacade {

    private final HeroService heroService;
    private final HeroValidator heroValidator;

    @Autowired
    public HeroFacade(HeroService heroService,
                      HeroValidator heroValidator){
        this.heroService = heroService;
        this.heroValidator = heroValidator;
    }

    public HeroResponse create(HeroRequest heroRequest, BindingResult bindingResult, Long userId) {
        log.info("Starting to create a new hero for the user: {}", userId);
        heroRequest.setUserId(userId);
        heroValidator.validate(heroRequest, bindingResult);
        Hero hero = this.heroService.create(new Hero(heroRequest));
        return new HeroResponse(hero);
    }

    public HeroResponse getByUserId(Long userId) {
        log.info("Starting the search for the user's hero: {}", userId);
        return new HeroResponse(heroService.getByUserId(userId));
    }
}
