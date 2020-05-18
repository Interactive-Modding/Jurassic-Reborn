package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class SegisaurusEntity extends DinosaurEntity
{
    public SegisaurusEntity(World world)
    {
        super(world);
        this.target(MicroraptorEntity.class, MicroceratusEntity.class, AlligatorGarEntity.class, DodoEntity.class, HypsilophodonEntity.class, CompsognathusEntity.class, AlvarezsaurusEntity.class);
        this.doesEatEggs(true);
    }
}
