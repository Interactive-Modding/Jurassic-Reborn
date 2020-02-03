package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.World;

public class CompsognathusEntity extends DinosaurEntity
{
    public CompsognathusEntity(World world)
    {
        super(world);
        this.doesEatEggs(true);
        this.tasks.addTask(0, new CompyHurtByTarget());
    }

    class CompyHurtByTarget extends EntityAIHurtByTarget {


        public CompyHurtByTarget() {
        super(CompsognathusEntity.this, false, new Class[0]);
    }

    public void startExecuting() {
        if (CompsognathusEntity.this.herd.size() >= 3) {
            super.startExecuting();
            if (CompsognathusEntity.this.isChild()) {
                this.alertOthers();
                this.resetTask();
            }
        }}

    protected void setEntityAttackTarget(DinosaurEntity creatureIn, EntityLivingBase entityLivingBaseIn) {
        if (creatureIn instanceof CompsognathusEntity && !creatureIn.isChild()) {
            super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
        }

    }
}}

