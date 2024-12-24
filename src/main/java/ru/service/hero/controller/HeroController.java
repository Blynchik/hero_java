package ru.service.hero.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.service.hero.config.security.JwtUser;
import ru.service.hero.dto.hero.HeroRequest;
import ru.service.hero.dto.hero.HeroResponse;
import ru.service.hero.facade.HeroFacade;

@RestController
@Slf4j
@RequestMapping("/api")
public class HeroController {

    private final HeroFacade heroFacade;

    @Autowired
    public HeroController(HeroFacade heroFacade) {
        this.heroFacade = heroFacade;
    }

    @PostMapping("/hero")
    public ResponseEntity<HeroResponse> create(@AuthenticationPrincipal JwtUser jwtUser,
                                               @Valid @RequestBody HeroRequest heroRequest,
                                               BindingResult bindingResult) {
        log.info("Request to POST /api/hero : {} from: {}", heroRequest, jwtUser.getLogin());
        HeroResponse response = this.heroFacade.create(heroRequest, bindingResult, jwtUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hero")
    public ResponseEntity<HeroResponse> getOwn(@AuthenticationPrincipal JwtUser jwtUser) {
        log.info("Request to GET /api/hero/own from: {}", jwtUser.getLogin());
        HeroResponse response = this.heroFacade.getByUserId(jwtUser.getUserId());
        return ResponseEntity.ok(response);
    }
}
