package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DimorphodonEntity extends FlyingDinosaurEntity
{
    public DimorphodonEntity(World world)
    {
        super(world);
        this.target(AlvarezsaurusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, CompsognathusEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, OthnieliaEntity.class, SegisaurusEntity.class);
    }

    @Override
    protected void doTarget(){
        this.target(LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, CompsognathusEntity.class);
    }
}
