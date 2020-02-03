package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.world.World;

public class MegapiranhaEntity extends SwimmingDinosaurEntity
{
    public MegapiranhaEntity(World world)
    {
        super(world);
        this.target(CoelacanthEntity.class);
    }
}
