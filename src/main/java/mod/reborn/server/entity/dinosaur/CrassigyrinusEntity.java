package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.world.World;

public class CrassigyrinusEntity extends SwimmingDinosaurEntity {

    public CrassigyrinusEntity(World world) {
        super(world);
        this.target(AlligatorGarEntity.class, BeelzebufoEntity.class, DiplocaulusEntity.class, MegapiranhaEntity.class);
    }
}
