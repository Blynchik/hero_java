package ru.service.hero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.service.hero.dto.hero.HeroRequest;

import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.service.hero.util.ApiRequest.createCustomHero;
import static ru.service.hero.util.ApiRequest.getOwnHero;
import static ru.service.hero.util.ObjectFactory.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class HeroControllerTest {

    private final MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Random random;

    @Autowired
    private HeroControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
    }

    @Nested
    @DisplayName(value = "Тесты на аутентификацию")
    class AuthTest {

        private HeroRequest heroRequest;
        private String heroAsString;

        @BeforeEach
        void setUp() throws Exception {
            this.heroRequest = getHeroRequest1();
            this.heroAsString = objectMapper.writeValueAsString(heroRequest);
        }

        @ParameterizedTest
        @ValueSource(strings = {"/api/admin/hero"})
        @Description("Тесты на авторизацию")
        void authorizationTest(String url) throws Exception {
            //given
            // нужно, что бы понимать, что вернувшиеся ошибки связаны только с токенами
            String token = getEternalToken();
            createCustomHero(mockMvc, heroAsString, token);
            //when
            List<String> tokens = List.of(getEternalToken());
            tokens.forEach(t -> {
                MockHttpServletRequestBuilder createRequest = MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + t);
                try {
                    //then
                    mockMvc.perform(createRequest)
                            .andExpect(
                                    status().isForbidden());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"/api/hero", "/api/admin/hero"})
        @Description("Тесты на аутентификацию")
        void authTest(String url) throws Exception {
            //given
            // нужно, что бы понимать, что вернувшиеся ошибки связаны только с токенами
            String token = getEternalToken();
            createCustomHero(mockMvc, heroAsString, token);
            //when
            List<String> tokens = List.of(getExpiredToken(), getChangedSignToken());
            tokens.forEach(t -> {
                MockHttpServletRequestBuilder createRequest = MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + t);
                try {
                    //then
                    mockMvc.perform(createRequest)
                            .andExpect(
                                    status().is4xxClientError())
                            .andExpect(
                                    jsonPath("$.exceptions[0].exception").value("JwtException"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Nested
    @DisplayName(value = "Тесты на создание героя")
    class CreateTest {

        private HeroRequest heroRequest;
        private String heroAsString;

        @BeforeEach
        void setUp() throws Exception {
            this.heroRequest = getHeroRequest1();
            this.heroAsString = objectMapper.writeValueAsString(heroRequest);
        }

        @Test
        @Description(value = "Тест на успешность создания героя")
        void success() throws Exception {
            //given
            String token = getEternalToken();
            //when
            createCustomHero(mockMvc, heroAsString, token)
                    //then
                    .andExpect(
                            status().isCreated())
                    .andExpect(
                            jsonPath("$.name").value(heroRequest.getName()))
                    .andExpect(
                            jsonPath("$.lastname").value(heroRequest.getLastname()))
                    .andExpect(
                            jsonPath("$.userId").value("1"))
                    .andExpect(
                            jsonPath("$.str").value(heroRequest.getStr()))
                    .andExpect(
                            jsonPath("$.dex").value(heroRequest.getDex()))
                    .andExpect(
                            jsonPath("$.con").value(heroRequest.getCon()))
                    .andExpect(
                            jsonPath("$.intl").value(heroRequest.getIntl()))
                    .andExpect(
                            jsonPath("$.wis").value(heroRequest.getWis()))
                    .andExpect(
                            jsonPath("$.cha").value(heroRequest.getCha()));
        }

        @Test
        @Description(value = "Тест на создание героя, если у пользователя уже был герой")
        void doubleCreate() throws Exception {
            //given
            String token = getEternalToken();
            createCustomHero(mockMvc, heroAsString, token);
            //when
            createCustomHero(mockMvc, heroAsString, token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString(String.format("The hero is already linked to the user %s", "1"))));
        }

        @Test
        @Description(value = "Тест на создание героя с длинным именем")
        void longName() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setName(getRandomString(random, 101));
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("The hero's name must consist of at least one character")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "       "})
        @Description(value = "Тест на создание героя с empty и blank именем")
        void emptyName(String name) throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setName(name);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[?(@.exception == 'BindingValidationException' " +
                                    "&& @.field == 'name' " +
                                    "&& @.descr == 'The hero\\'s name must consist of at least one character')]")
                                    .exists());
        }

        @Test
        @Description(value = "Тест на создание героя с null именем")
        void nullName() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setName(null);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("The hero's name must consist of at least one character")));
        }

        @Test
        @Description(value = "Тест на создание героя с невалидными знаками в имени")
        void notValidName() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setName("1!<ZNRЯЧГ");
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("The hero's name can only consist of letters of the Russian or Latin alphabet")));
        }

        @Test
        @Description(value = "Тест на создание героя с длинной фамилией")
        void longLastname() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setLastname(getRandomString(random, 101));
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("The last name of the hero must consist of at least one character")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "       "})
        @Description(value = "Тест на создание героя с empty и blank фамилией")
        void emptyLastname(String name) throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setLastname(name);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[?(@.exception == 'BindingValidationException' " +
                                    "&& @.field == 'lastname' " +
                                    "&& @.descr == 'The last name of the hero must consist of at least one character')]")
                                    .exists());
        }

        @Test
        @Description(value = "Тест на создание героя с null фамилией")
        void nullLastname() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setLastname(null);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("The last name of the hero must consist of at least one character")));
        }

        @Test
        @Description(value = "Тест на создание героя с невалидными знаками в фамилии")
        void notValidLastname() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setLastname("1!<ZNRЯЧГ");
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("The hero's surname can consist only of letters of the Russian or Latin alphabet")));
        }

        @Test
        @Description(value = "Тест на минимальные значения характеристик")
        void minCharacteristic() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setStr(60);
            heroRequest.setDex(60);
            heroRequest.setCon(60);
            heroRequest.setIntl(60);
            heroRequest.setWis(60);
            heroRequest.setCha(60);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[*].descr", containsInAnyOrder(
                                    "Strength should be at least 70",
                                    "Dexterity should be at least 70",
                                    "Constitution should be at least 70",
                                    "Intelligence should be at least 70",
                                    "Wisdom should be at least 70",
                                    "Charisma should be at least 70",
                                    "Too few points were used. Unused points: 240"
                            ))
                    );
        }

        @Test
        @Description(value = "Тест на максимальные значения характеристик")
        void maxCharacteristic() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setStr(190);
            heroRequest.setDex(190);
            heroRequest.setCon(190);
            heroRequest.setIntl(190);
            heroRequest.setWis(190);
            heroRequest.setCha(190);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[*].descr", containsInAnyOrder(
                                    "Strength should be no more than 180",
                                    "Dexterity should be no more than 180",
                                    "Constitution should not be more than 180",
                                    "Intelligence should be no more than 180",
                                    "Wisdom should be no more than 180",
                                    "Charisma should be no more than 180",
                                    "Too many points have been used. Overused points: 540"
                            ))
                    );
        }

        @Test
        @Description(value = "Тест на остаток неиспользованных очков")
        void notUsedPoints() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setStr(90);
            heroRequest.setDex(100);
            heroRequest.setCon(100);
            heroRequest.setIntl(100);
            heroRequest.setWis(100);
            heroRequest.setCha(100);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value("Too few points were used. Unused points: 10")
                    );
        }

        @Test
        @Description(value = "Тест на использование лишних очков")
        void overUsedPoints() throws Exception {
            //given
            String token = getEternalToken();
            heroRequest.setStr(110);
            heroRequest.setDex(100);
            heroRequest.setCon(100);
            heroRequest.setIntl(100);
            heroRequest.setWis(100);
            heroRequest.setCha(100);
            //when
            createCustomHero(mockMvc, objectMapper.writeValueAsString(heroRequest), token)
                    //then
                    .andExpect(
                            status().isBadRequest())
                    .andExpect(
                            jsonPath("$.exceptions[0].exception").value("BindingValidationException"))
                    .andExpect(
                            jsonPath("$.exceptions[0].descr").value(containsString("Too many points have been used. Overused points: 10")));
        }

        @Nested
        @DisplayName(value = "Тесты на получение своего героя")
        class GetOwnTest {

            private HeroRequest heroRequest;
            private String heroAsString;

            @BeforeEach
            void setUp() throws Exception {
                this.heroRequest = getHeroRequest1();
                this.heroAsString = objectMapper.writeValueAsString(heroRequest);
            }

            @Test
            @Description(value = "Тест на успешность получения героя")
            void success() throws Exception {
                //given
                String token = getEternalToken();
                createCustomHero(mockMvc, heroAsString, token);
                //then
                getOwnHero(mockMvc, token)
                        .andExpect(
                                status().isOk())
                        .andExpect(
                                jsonPath("$.name").value(heroRequest.getName()))
                        .andExpect(
                                jsonPath("$.lastname").value(heroRequest.getLastname()))
                        .andExpect(
                                jsonPath("$.userId").value("1"))
                        .andExpect(
                                jsonPath("$.str").value(heroRequest.getStr()))
                        .andExpect(
                                jsonPath("$.dex").value(heroRequest.getDex()))
                        .andExpect(
                                jsonPath("$.con").value(heroRequest.getCon()))
                        .andExpect(
                                jsonPath("$.intl").value(heroRequest.getIntl()))
                        .andExpect(
                                jsonPath("$.wis").value(heroRequest.getWis()))
                        .andExpect(
                                jsonPath("$.cha").value(heroRequest.getCha()));
            }

            @Test
            @Description(value = "Тест на получение героя, если нет героя")
            void noHero() throws Exception {
                //given
                String token = getEternalToken();
                //then
                getOwnHero(mockMvc, token)
                        .andExpect(
                                status().isNotFound())
                        .andExpect(
                                jsonPath("$.exceptions[0].exception").value("EntityNotFoundException"))
                        .andExpect(
                                jsonPath("$.exceptions[0].descr").value(containsString("The hero was not found")));
            }
        }
    }
}
