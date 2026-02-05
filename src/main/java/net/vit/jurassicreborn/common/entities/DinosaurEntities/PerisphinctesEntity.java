package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;

public class PerisphinctesEntity extends SwimmingDinosaurEntity {

    public PerisphinctesEntity (Level world, EntityType<PerisphinctesEntity> type)
    {
        super(world, type, DinosaurHandler.PERISPHINCTES);
        this.target(Squid.class, Cod.class, Salmon.class, TropicalFish.class,  Axolotl.class, GlowSquid.class);
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

