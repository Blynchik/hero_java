package ru.service.hero.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.service.hero.dto.hero.HeroRequest;

import java.util.List;
import java.util.Random;

;

public class ObjectFactory {

    public static String getRandomString(Random random, int length) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; i++) {
            string.append(Character.toString('A' + random.nextInt(26)));
        }
        return string.toString();
    }

    public static <T> T convertJsonToObject(ObjectMapper objectMapper, String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Convert error " + clazz.getSimpleName(), e);
        }
    }

    public static <T> List<T> convertJsonToList(ObjectMapper objectMapper, String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException("Convert error " + clazz.getSimpleName() + ">", e);
        }
    }

    public static String getChangedSignToken() {
        return getEternalToken().subSequence(0, getEternalToken().length() - 2) + "A";
    }

    public static String getExpiredToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJhQHlhbmRleC5ydSIsInVzZXJJZCI6MSwiaWF0IjoxNzMyMTk3NDAyLCJleHAiOjE3MzIxOTc0MTIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdfQ.Uw76kNVdU-mK_YB59xLMCtKZjScKEzyeolxR610jlyA";
    }

    public static String getEternalToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJhQHlhbmRleC5ydSIsInVzZXJJZCI6MSwiaWF0IjoxNzMyMTk3ODgzLCJleHAiOjIwMzk3ODE4ODMsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdfQ.g9KlcaBmOD1qMH4P0Gq_-Dn_GVcsX14EFzYvtfYXAZI";
    }

    public static HeroRequest getHeroRequest2() {
        return getHeroRequestCustom("Сияющий", "Рыцарь", 120, 110, 100, 100, 90, 80);
    }

    public static HeroRequest getHeroRequest1() {
        return getHeroRequestCustom("Сияющий", "Рыцарь", 100, 100, 100, 100, 100, 100);
    }

    public static HeroRequest getHeroRequestCustom(String name, String lastname,
                                                   Integer str, Integer dex, Integer con, Integer intl, Integer wis, Integer cha) {
        return new HeroRequest(name, lastname, str, dex, con, intl, wis, cha, null);
    }
}
