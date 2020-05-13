package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class MawsoniaEntity extends SwimmingDinosaurEntity {

	public MawsoniaEntity(World world) {
		super(world);
        this.target(EntitySquid.class, MegapiranhaEntity.class);
	}

	  protected void applyEntityAttributes()
	    {
	        super.applyEntityAttributes();
	        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
	    }


	    @Override
	    public SoundEvent getSoundForAnimation(Animation animation) {
	        return null;
	    }
}
