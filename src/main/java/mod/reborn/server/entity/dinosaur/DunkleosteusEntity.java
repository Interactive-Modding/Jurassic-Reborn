package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DunkleosteusEntity extends SwimmingDinosaurEntity
{
    public DunkleosteusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, MawsoniaEntity.class, EntitySquid.class, BeelzebufoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, EntityPlayer.class, MegapiranhaEntity.class, EntityVillager.class, EntityAnimal.class, EntityMob.class);
    }
}
