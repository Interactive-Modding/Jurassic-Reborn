package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;


public class ParapuzosiaEntity extends SwimmingDinosaurEntity {

    public ParapuzosiaEntity(Level world, EntityType<ParapuzosiaEntity> type) {
        super(world, type, DinosaurHandler.PARAPUZOSIA);
        this.target(Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class, AsterocerasEntity.class, OrthocerasEntity.class, PerisphinctesEntity.class, CalymeneEntity.class,CoelacanthEntity.class, MawsoniaEntity.class, EndocerasEntity.class, CamerocerasEntity.class, TitanitesEntity.class, AlligatorGarEntity.class, CrabEntity.class, BeelzebufoEntity.class, MegapiranhaEntity.class);
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

