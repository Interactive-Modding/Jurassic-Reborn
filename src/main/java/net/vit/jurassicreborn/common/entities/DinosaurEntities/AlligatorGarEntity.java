package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;



public class AlligatorGarEntity extends SwimmingDinosaurEntity
{
    public AlligatorGarEntity(EntityType<AlligatorGarEntity> type, Level world) {
        super(world, type, DinosaurHandler.ALLIGATOR_GAR);
        this.target(Squid.class,Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        return null;
    }

    @Override
    public Vector3f getDinosaurCultivatorRotation() {
        return new Vector3f(-90F, 0, 0);
    }
}

