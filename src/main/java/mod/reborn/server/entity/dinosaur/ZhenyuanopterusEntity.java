package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ZhenyuanopterusEntity extends FlyingDinosaurEntity
{
    public ZhenyuanopterusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CoelacanthEntity.class, GuanlongEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DodoEntity.class, DiplocaulusEntity.class, MoganopterusEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, EntityPlayer.class);
    }
}
