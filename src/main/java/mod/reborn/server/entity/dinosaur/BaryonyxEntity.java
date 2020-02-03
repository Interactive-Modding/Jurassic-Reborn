package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BaryonyxEntity extends DinosaurEntity
{
    public BaryonyxEntity(World world)
    {
        super(world);
        this.target(CompsognathusEntity.class, AnkylosaurusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LudodactylusEntity.class, HypsilophodonEntity.class, GallimimusEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, ParasaurolophusEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, TriceratopsEntity.class, StegosaurusEntity.class, BrachiosaurusEntity.class, ApatosaurusEntity.class, RugopsEntity.class, HerrerasaurusEntity.class, VelociraptorEntity.class, AchillobatorEntity.class, CarnotaurusEntity.class );
    }
}
