package mod.reborn.server.entity.dinosaur;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class VelociraptorDeltaEntity extends VelociraptorEntity
{
    public VelociraptorDeltaEntity(World world)
    {
        super(world);
        this.target(AlvarezsaurusEntity.class, SpinoraptorEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ArsinoitheriumEntity.class, BeelzebufoEntity.class, CearadactylusEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, GallimimusEntity.class, ProceratosaurusEntity.class, DodoEntity.class, HypsilophodonEntity.class, EntityPlayer.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MetriacanthosaurusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class);
        doesEatEggs(true);
    }
}
