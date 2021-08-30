package mod.reborn.server.entity;

import mod.reborn.server.food.FoodType;

import java.util.ArrayList;
import java.util.List;

public enum DinosaurStatus {
    CARNIVOROUS {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getDiet().canEat(entity, FoodType.MEAT);
            return false;
        }
    },
    PISCIVOROUS {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getDiet().canEat(entity, FoodType.FISH);
            return false;
        }
    },
    HERBIVOROUS {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getDiet().canEat(entity, FoodType.PLANT);
            return false;
        }
    },
    INSECTIVOROUS {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getDiet().canEat(entity, FoodType.INSECT);
            return false;
        }
    },
    DIURNAL {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getSleepTime() == SleepTime.DIURNAL;
            return false;
        }
    },
    NOCTURNAL {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getSleepTime() == SleepTime.NOCTURNAL;
            return false;
        }
    },
    CREPUSCULAR {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getDinosaur().getSleepTime() == SleepTime.CREPUSCULAR;
            return false;
        }
    },
    TAMED {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getOwner() != null;
            return false;
        }
    },
    LOW_HEALTH {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.getHealth() < entity.getMaxHealth() / 4 || entity.isCarcass();
            return false;
        }
    },
    HUNGRY {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.hungry;
        }
    },
    THIRSTY {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.thirsty;
        }
    },
    POISONED {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.poisoned;
        }
    },
    DROWNING {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return !entity.getDinosaur().isMarineCreature() && entity.getAir() < 200;
            return false;
        }
    },
    SLEEPY {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            //return entity.shouldSleep();
            return false;
        }
    },
    FLOCKING {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.flocking;
        }
    },
    SCARED {
        @Override
        public boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
            return info.scared;
        }
    };

    public static List<DinosaurStatus> getActiveStatuses(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info) {
        List<DinosaurStatus> statuses = new ArrayList<>();

        for (DinosaurStatus status : values()) {
            if (status.apply(entity, info)) {
                statuses.add(status);
            }
        }

        return statuses;
    }

    public abstract boolean apply(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo info);
}
