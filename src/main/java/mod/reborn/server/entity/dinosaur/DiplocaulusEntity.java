package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.world.World;

public class DiplocaulusEntity extends SwimmingDinosaurEntity {

    public DiplocaulusEntity(World world) {
        super(world);
        this.target(AlvarezsaurusEntity.class, BeelzebufoEntity.class, EntitySquid.class, CompsognathusEntity.class, LeptictidiumEntity.class);
    }
}
