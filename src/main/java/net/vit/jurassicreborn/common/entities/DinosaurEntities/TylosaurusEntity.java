package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;import net.vit.jurassicreborn.common.entities.ai.WaterLeapAI;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class TylosaurusEntity extends SwimmingDinosaurEntity
{
    public TylosaurusEntity(Level world, EntityType<TylosaurusEntity> type)
    {
        super(world, type, DinosaurHandler.TYLOSAURUS);
        this.target(CoelacanthEntity.class,DiplocaulusEntity.class,BeelzebufoEntity.class,OrthocerasEntity.class,CamerocerasEntity.class, EndocerasEntity.class,MawsoniaEntity.class, CrassigyrinusEntity.class, AsterocerasEntity.class, ParapuzosiaEntity.class, TitanisEntity.class, AlligatorGarEntity.class, MegapiranhaEntity.class, Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class,    Player.class, Animal.class, Villager.class, Mob.class, Goat.class);
        this.addTask(0, new WaterLeapAI/*WaterYeetAI*/(this, 12, 1.2F));
    }
}

