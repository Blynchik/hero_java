package ru.service.hero.dto.hero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.service.hero.model.Hero;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroResponse {
    private Long id;
    private String name;
    private String lastname;
    private Date createdAt;
    private Long userId;
    private Integer str;
    private Integer dex;
    private Integer con;
    private Integer intl;
    private Integer wis;
    private Integer cha;

    public HeroResponse(Hero hero) {
        this.id = hero.getId();
        this.name = hero.getName();
        this.lastname = hero.getLastname();
        this.createdAt = hero.getCreatedAt();
        this.userId = hero.getUserId();
        this.str = hero.getStr();
        this.dex = hero.getDex();
        this.con = hero.getCon();
        this.intl = hero.getIntl();
        this.wis = hero.getWis();
        this.cha = hero.getCha();
    }
}
