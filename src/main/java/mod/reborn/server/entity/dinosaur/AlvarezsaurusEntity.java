package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class AlvarezsaurusEntity extends DinosaurEntity {

    public AlvarezsaurusEntity(World world) {
        super(world);
        this.target(DodoEntity.class, CompsognathusEntity.class, HypsilophodonEntity.class, LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, DimorphodonEntity.class, LeaellynasauraEntity.class, ProtoceratopsEntity.class, OviraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, TroodonEntity.class, SegisaurusEntity.class);
    }
}
