package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;

public class AsterocerasEntity extends SwimmingDinosaurEntity {

    public AsterocerasEntity (Level world, EntityType<AsterocerasEntity> type)
    {
        super(world, type, DinosaurHandler.ASTEROCERAS);
        this.target(Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class);
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

