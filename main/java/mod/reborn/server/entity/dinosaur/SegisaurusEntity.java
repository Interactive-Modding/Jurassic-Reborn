package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class SegisaurusEntity extends DinosaurEntity
{
    public SegisaurusEntity(World world)
    {
        super(world);
        this.target(LeptictidiumEntity.class, HypsilophodonEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, CompsognathusEntity.class);
        this.doesEatEggs(true);
    }
}
