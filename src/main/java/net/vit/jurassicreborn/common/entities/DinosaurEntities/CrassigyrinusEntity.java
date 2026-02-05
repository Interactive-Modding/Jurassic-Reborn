package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class CrassigyrinusEntity extends SwimmingDinosaurEntity {

    public CrassigyrinusEntity(Level world, EntityType<CrassigyrinusEntity> type) {
        super(world, type, DinosaurHandler.CRASSIGYRINUS);
        this.target(AlligatorGarEntity.class, BeelzebufoEntity.class, DiplocaulusEntity.class, MegapiranhaEntity.class, Animal.class);
    }
}

