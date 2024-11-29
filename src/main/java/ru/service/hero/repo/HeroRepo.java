package ru.service.hero.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.service.hero.model.Hero;

import java.util.Optional;

@Repository
public interface HeroRepo extends JpaRepository<Hero, Long> {

    Optional<Hero> findHeroByUserId(Long userId);
}
