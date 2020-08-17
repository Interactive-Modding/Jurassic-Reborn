package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.world.World;

public class AmmoniteEntity extends SwimmingDinosaurEntity {

    public AmmoniteEntity (World world)
    {
        super(world);
        this.target(EntitySquid.class);
    }
}
