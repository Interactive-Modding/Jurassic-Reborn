package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.world.World;

public class AsterocerasEntity extends SwimmingDinosaurEntity {

    public AsterocerasEntity (World world)
    {
        super(world);
        this.target(EntitySquid.class);
    }
}
