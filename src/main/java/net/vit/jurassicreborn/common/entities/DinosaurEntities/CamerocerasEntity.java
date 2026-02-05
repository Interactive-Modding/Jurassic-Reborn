package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;


public class CamerocerasEntity extends SwimmingDinosaurEntity {

    public CamerocerasEntity(Level world, EntityType<CamerocerasEntity> type) {
        super(world, type, DinosaurHandler.CAMEROCERAS);
        this.target(Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class, AsterocerasEntity.class, OrthocerasEntity.class, PerisphinctesEntity.class, CoelacanthEntity.class, MawsoniaEntity.class, EndocerasEntity.class, TitanitesEntity.class, AlligatorGarEntity.class, CrabEntity.class, BeelzebufoEntity.class, MegapiranhaEntity.class, CalymeneEntity.class);
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

