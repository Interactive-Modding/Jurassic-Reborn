package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.minecraft.world.World;

public class DimorphodonEntity extends FlyingDinosaurEntity
{
    public DimorphodonEntity(World world)
    {
        super(world);
    }

    @Override
    protected void doTarget(){
        this.target(LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, CompsognathusEntity.class);
    }
}
