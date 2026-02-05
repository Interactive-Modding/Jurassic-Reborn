package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class DiplocaulusEntity extends SwimmingDinosaurEntity {

    public DiplocaulusEntity(Level world, EntityType<DiplocaulusEntity> type) {
        super(world, type, DinosaurHandler.DIPLOCAULUS);
        this.target(AlvarezsaurusEntity.class, BeelzebufoEntity.class, Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class,    CompsognathusEntity.class, LeptictidiumEntity.class);
    }
}

