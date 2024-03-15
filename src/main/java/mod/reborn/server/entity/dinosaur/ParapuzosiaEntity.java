package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import mod.reborn.server.entity.animal.EntityCrab;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.world.World;

public class ParapuzosiaEntity extends SwimmingDinosaurEntity {

    public ParapuzosiaEntity (World world)
    {
        super(world);
        this.target(EntitySquid.class,AsterocerasEntity.class,AmmoniteEntity.class,CoelacanthEntity.class,MawsoniaEntity.class,TitanitesEntity.class,AlligatorGarEntity.class, EntityCrab.class,BeelzebufoEntity.class,MegapiranhaEntity.class);
    }
}
