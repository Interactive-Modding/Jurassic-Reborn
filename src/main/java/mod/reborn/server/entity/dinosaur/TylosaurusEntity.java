package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import mod.reborn.server.entity.ai.WaterLeapAI;
import mod.reborn.server.entity.animal.GoatEntity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class TylosaurusEntity extends SwimmingDinosaurEntity
{
    public TylosaurusEntity(World world)
    {
        super(world);
        this.target(CoelacanthEntity.class, MegapiranhaEntity.class, EntitySquid.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, EntityMob.class, GoatEntity.class);
//        this.tasks.addTask(0, new WaterLeapAI(this, 12, 1.5F));
    }
}
