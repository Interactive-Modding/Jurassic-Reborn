package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.world.World;

public class TylosaurusEntity extends SwimmingDinosaurEntity
{
    public TylosaurusEntity(World world)
    {
        super(world);
        this.target(CoelacanthEntity.class, MegapiranhaEntity.class);
    }
}
