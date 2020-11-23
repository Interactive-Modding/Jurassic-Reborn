package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class ChilesaurusEntity extends DinosaurEntity {

    public ChilesaurusEntity(World world) {
        super(world);
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));
    }

}
