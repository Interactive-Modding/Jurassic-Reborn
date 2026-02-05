package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;


public class EndocerasEntity extends SwimmingDinosaurEntity {

    public EndocerasEntity(Level world, EntityType<EndocerasEntity> type) {
        super(world, type, DinosaurHandler.ENDOCERAS);
        this.target(Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class,   AsterocerasEntity.class, OrthocerasEntity.class, PerisphinctesEntity.class, CalymeneEntity.class, CoelacanthEntity.class, MawsoniaEntity.class, CamerocerasEntity.class, TitanitesEntity.class, AlligatorGarEntity.class, CrabEntity.class, BeelzebufoEntity.class, MegapiranhaEntity.class);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result) {
            this.spawnInk();
        }
        return result;
    }
}

