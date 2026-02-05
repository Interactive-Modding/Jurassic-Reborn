package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class MawsoniaEntity extends SwimmingDinosaurEntity {

	public MawsoniaEntity(Level world, EntityType<MawsoniaEntity> type) {
		super(world, type, DinosaurHandler.MAWSONIA);
        this.target(AlligatorGarEntity.class, BeelzebufoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, Player.class
, MegapiranhaEntity.class, Villager.class, Animal.class, Mob.class);
	}

	public static AttributeSupplier.Builder createMobAttributes() {

	       return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D);
	    }


	@Override
	public SoundEvent getSoundForAnimation(Animation animation) {
		return null;
	}
}

