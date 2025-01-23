package ru.service.hero.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.service.hero.model.Hero;
import ru.service.hero.repo.HeroRepo;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class HeroService {

    private final HeroRepo heroRepo;

    @Autowired
    public HeroService(HeroRepo heroRepo) {
        this.heroRepo = heroRepo;
    }

    @Transactional
    public Hero create(Hero hero) {
        log.info("Creating new hero for: {}", hero.getUserId());
        return heroRepo.save(hero);
    }

    @Transactional
    public Hero save(Hero hero) {
        log.info("Saving hero for: {}", hero.getUserId());
        return heroRepo.save(hero);
    }

    public Hero getByUserId(Long userId) {
        log.info("Searching for a hero by userId: {}", userId);
        return heroRepo.findHeroByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("The hero was not found"));
    }

    public Optional<Hero> getByUserIdOptional(Long userId) {
        log.info("Searching for a hero by userId: {}", userId);
        return heroRepo.findHeroByUserId(userId);
    }
}
