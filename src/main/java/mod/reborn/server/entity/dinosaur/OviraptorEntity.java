package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.World;

public class OviraptorEntity extends DinosaurEntity
{
    public OviraptorEntity(World world)
    {
        super(world);
        this.target(CompsognathusEntity.class, LeptictidiumEntity.class, OthnieliaEntity.class);
        doesEatEggs(true);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }
}
