package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;

public class MegalodonEntity extends SwimmingDinosaurEntity {

    public MegalodonEntity(Level world, EntityType<MegalodonEntity> type) {
        super(world, type, DinosaurHandler.MEGALODON);
        this.target(CoelacanthEntity.class, BeelzebufoEntity.class,LivyatanEntity.class,DiplocaulusEntity.class, TylosaurusEntity.class, OrthocerasEntity.class,CamerocerasEntity.class, EndocerasEntity.class,MawsoniaEntity.class, CrassigyrinusEntity.class, AsterocerasEntity.class, ParapuzosiaEntity.class, TitanisEntity.class, AlligatorGarEntity.class, MegapiranhaEntity.class, Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class,  Player.class, Animal.class, Villager.class, Mob.class, Goat.class);
    }
}

