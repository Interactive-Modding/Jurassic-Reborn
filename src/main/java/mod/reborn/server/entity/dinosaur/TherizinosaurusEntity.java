package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class TherizinosaurusEntity extends DinosaurEntity
{
    public TherizinosaurusEntity(World world)
    {
        super(world);
        this.target(TyrannosaurusEntity.class);
    }
}
