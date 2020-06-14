package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.World;

public class CompsognathusEntity extends DinosaurEntity
{
    public CompsognathusEntity(World world)
    {
        super(world);
        this.doesEatEggs(true);
        this.target(DodoEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, CrassigyrinusEntity.class, LeptictidiumEntity.class);
        this.tasks.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.tasks.addTask(0, new CompyHurtByTarget());
    }
    @Override
    public void fall(float distance, float damageMultiplier) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            super.fall(distance, damageMultiplier);
        }
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
            if (creatureIn instanceof CompsognathusEntity && !creatureIn.isChild() && creatureIn != null) {
                super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
            }

        }
}}

