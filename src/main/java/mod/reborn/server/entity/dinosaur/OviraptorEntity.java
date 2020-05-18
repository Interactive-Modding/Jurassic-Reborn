package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class OviraptorEntity extends DinosaurEntity
{
    public OviraptorEntity(World world)
    {
        super(world);
        this.target(CompsognathusEntity.class, LeptictidiumEntity.class, OthnieliaEntity.class);
        doesEatEggs(true);
    }
}
