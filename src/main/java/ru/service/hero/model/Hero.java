package ru.service.hero.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.service.hero.dto.hero.HeroRequest;

import java.util.Date;

@Entity
@Table(name = "hero",
        indexes = {
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_id", columnList = "id")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hero {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(min = 1, max = 100)
    @NotBlank
    private String name;

    @Column(name = "lastname")
    @Size(min = 1, max = 100)
    @NotBlank
    private String lastname;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "user_id", unique = true)
    @NotNull
    private Long userId;

    @Column(name = "str")
    private int str;

    @Column(name = "dex")
    private int dex;

    @Column(name = "con")
    private int con;

    @Column(name = "intl")
    private int intl;

    @Column(name = "wis")
    private int wis;

    @Column(name = "cha")
    private int cha;

    public Hero(String name, String lastname, Long userId, int str, int dex, int con, int intl, int wis, int cha) {
        this.name = name;
        this.lastname = lastname;
        this.createdAt = new Date();
        this.userId = userId;
        this.str = str;
        this.dex = dex;
        this.con = con;
        this.intl = intl;
        this.wis = wis;
        this.cha = cha;
    }

    public Hero(HeroRequest heroRequest){
        this.name = heroRequest.getName();
        this.lastname = heroRequest.getLastname();
        this.createdAt = new Date();
        this.userId = heroRequest.getUserId();
        this.str = heroRequest.getStr();
        this.dex = heroRequest.getDex();
        this.con = heroRequest.getCon();
        this.intl = heroRequest.getIntl();
        this.wis = heroRequest.getWis();
        this.cha = heroRequest.getCha();
    }
}
