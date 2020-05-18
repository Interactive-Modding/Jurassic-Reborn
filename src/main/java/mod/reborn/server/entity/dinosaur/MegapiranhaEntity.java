package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MegapiranhaEntity extends SwimmingDinosaurEntity
{
    public MegapiranhaEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, CoelacanthEntity.class, BeelzebufoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, DodoEntity.class, GuanlongEntity.class, LeptictidiumEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, EntityPlayer.class);
    }
}
