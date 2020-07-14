package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LudodactylusEntity extends FlyingDinosaurEntity
{
    public LudodactylusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CearadactylusEntity.class, ChilesaurusEntity.class, CoelacanthEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, TropeognathusEntity.class, ZhenyuanopterusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, EntityMob.class);
    }
}
