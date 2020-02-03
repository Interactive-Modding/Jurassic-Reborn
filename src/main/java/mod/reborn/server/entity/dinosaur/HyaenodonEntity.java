package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class HyaenodonEntity extends DinosaurEntity {


    public HyaenodonEntity(World world) {
        super(world);
        this.target(GoatEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, MicroraptorEntity.class, MussaurusEntity.class);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
    }

}
