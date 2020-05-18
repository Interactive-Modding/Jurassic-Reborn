package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.World;

public class CoelurusEntity extends DinosaurEntity
{
    public CoelurusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CompsognathusEntity.class, DodoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, SegisaurusEntity.class, HypsilophodonEntity.class, OthnieliaEntity.class, MussaurusEntity.class, TroodonEntity.class, DimorphodonEntity.class, ChilesaurusEntity.class, MicroraptorEntity.class, LeptictidiumEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, GuanlongEntity.class, HyaenodonEntity.class, LeaellynasauraEntity.class, ProceratosaurusEntity.class, MicroceratusEntity.class);
    }
}

