package ru.service.hero.service;

import ru.service.hero.model.Hero;

public enum AttributeEnum {
    STR {
        @Override
        public Hero increase(Hero hero) {
            hero.setStr(hero.getStr() + 1);
            return hero;
        }

        @Override
        public Hero decrease(Hero hero) {
            hero.setStr(hero.getStr() - 1);
            return hero;
        }
    },
    DEX {
        @Override
        public Hero increase(Hero hero) {
            hero.setDex(hero.getDex() + 1);
            return hero;
        }

        @Override
        public Hero decrease(Hero hero) {
            hero.setDex(hero.getDex() - 1);
            return hero;
        }
    },
    CON {
        @Override
        public Hero increase(Hero hero) {
            hero.setCon(hero.getCon() + 1);
            return hero;
        }

        @Override
        public Hero decrease(Hero hero) {
            hero.setCon(hero.getCon() - 1);
            return hero;
        }
    },
    INT {
        @Override
        public Hero increase(Hero hero) {
            hero.setIntl(hero.getIntl() + 1);
            return hero;
        }

        @Override
        public Hero decrease(Hero hero) {
            hero.setIntl(hero.getIntl() - 1);
            return hero;
        }
    },
    WIS {
        @Override
        public Hero increase(Hero hero) {
            hero.setWis(hero.getWis() + 1);
            return hero;
        }

        @Override
        public Hero decrease(Hero hero) {
            hero.setWis(hero.getWis() - 1);
            return hero;
        }
    },
    CHA {
        @Override
        public Hero increase(Hero hero) {
            hero.setCha(hero.getCha() + 1);
            return hero;
        }

        @Override
        public Hero decrease(Hero hero) {
            hero.setCha(hero.getCha() - 1);
            return hero;
        }
    };

    public abstract Hero increase(Hero hero);

    public abstract Hero decrease(Hero hero);
}
