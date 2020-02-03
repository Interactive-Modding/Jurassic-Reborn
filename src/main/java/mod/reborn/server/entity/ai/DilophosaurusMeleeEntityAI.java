package mod.reborn.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import mod.reborn.server.entity.dinosaur.DilophosaurusEntity;

public class DilophosaurusMeleeEntityAI extends DinosaurAttackMeleeEntityAI {
    public DilophosaurusMeleeEntityAI(DilophosaurusEntity entity, double speed) {
        super(entity, speed, false);
        this.setMutexBits(Mutex.ATTACK);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.attacker.getAttackTarget();
        return super.shouldExecute() && target.getHealth() < target.getMaxHealth() * 0.9F && target.isPotionActive(MobEffects.BLINDNESS);
    }
}
