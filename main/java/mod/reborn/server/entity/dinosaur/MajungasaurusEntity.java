package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class MajungasaurusEntity extends DinosaurEntity
{
    public MajungasaurusEntity(World world)
    {
        super(world);
        this.target(AnkylosaurusEntity.class, ApatosaurusEntity.class, ChasmosaurusEntity.class, CorythosaurusEntity.class, DodoEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, HypsilophodonEntity.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, ProtoceratopsEntity.class, StegosaurusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class);
    }
}
