package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SegisaurusEntity extends DinosaurEntity
{
    public SegisaurusEntity(World world)
    {
        super(world);
        this.target(MicroraptorEntity.class, MicroceratusEntity.class, AlligatorGarEntity.class, DodoEntity.class, HypsilophodonEntity.class, CompsognathusEntity.class, AlvarezsaurusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, GoatEntity.class);
        this.doesEatEggs(true);
    }
}
