package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class OviraptorEntity extends DinosaurEntity
{
    public OviraptorEntity(World world)
    {
        super(world);
        this.target(CompsognathusEntity.class, LeptictidiumEntity.class, OthnieliaEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, EntityMob.class, GoatEntity.class);
        doesEatEggs(true);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }
}
