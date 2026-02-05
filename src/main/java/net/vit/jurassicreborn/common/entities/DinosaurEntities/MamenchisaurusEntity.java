package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.vit.jurassicreborn.common.entities.LegSolverQuadruped;

public class MamenchisaurusEntity extends DinosaurEntity {

    private static boolean isKingSet = false;
    private boolean isKing = false;
    private MamenchisaurusEntity king = null;
    private int stepCount = 0;

    public LegSolverQuadruped legSolver;

    public MamenchisaurusEntity(EntityType<MamenchisaurusEntity> type, Level world) {
        super(world, type, DinosaurHandler.MAMENCHISAURUS);
        this.addTask(1, new HurtByTargetGoal(this));

    }

    @Override
    protected LegSolverQuadruped createLegSolver() {
        return this.legSolver = new LegSolverQuadruped(2.4F, 0.7F, 0.9F, 0.4F, 1.0F);
    }

    @Override
    public void aiStep() {
        double distance2 = 18.0D;
        Entity entityFound2 = null;
        double d4 = -1.0D;

//            if (currE instanceof MamenchisaurusEntity) {
//                double d5 = currE.getDistanceSq(this.getX(), this.posY, this.posZ);
//                if ((d5 < distance2 * distance2) && (d4 == -1.0D || d5 < d4)) {
//                    d4 = d5;
//                    entityFound2 = currE;
//                }
//            }
//        }
        for(Entity e : this.level().getEntitiesOfClass(MamenchisaurusEntity.class, this.getBoundingBox().inflate(distance2*distance2))){
            if(e.distanceTo(this) < distance2 * distance2){
                entityFound2 = e;
                break;
            }
        }
        if (entityFound2 != null) {
            if (!isKingSet) {
                king = ((MamenchisaurusEntity) entityFound2);
                king.isKing = true;
                isKingSet = true;
            }
        }
        if(king == null){
            isKingSet = false;
        }
        if(!isKing && isKingSet) {
            moveControl.setWantedPosition(king.getX(), king.getY(), king.getZ(), 1.0D);
        }
        super.aiStep();
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MAMENCHISAURUS_LIVING;
            case CALLING:
                return SoundHandler.MAMENCHISAURUS_CALLING;
            case DYING:
                return SoundHandler.MAMENCHISAURUS_DEATH;
            case BEGGING:
                return SoundHandler.MAMENCHISAURUS_THREAT;
            case INJURED:
                return SoundHandler.MAMENCHISAURUS_HURT;
            case MATING:
                return SoundHandler.MAMENCHISAURUS_MATING;
            case WALKING:
                return SoundHandler.STOMP;
            default:
                return null;
        }
    }
}

