package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.LegSolver;
import mod.reborn.server.entity.LegSolverQuadruped;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class MamenchisaurusEntity extends DinosaurEntity {

    private static boolean isKingSet = false;
    private boolean isKing = false;
    private MamenchisaurusEntity king = null;
    private int stepCount = 0;

    public LegSolverQuadruped legSolver;

    public MamenchisaurusEntity(World world) {
        super(world);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected LegSolver createLegSolver() {
        return this.legSolver = new LegSolverQuadruped(2.4F, 0.7F, 0.9F, 0.4F);
    }

    @Override
    public void onLivingUpdate() {
        double distance2 = 18.0D;
        Entity entityFound2 = null;
        double d4 = -1.0D;
        for (Entity currE : this.world.loadedEntityList) {
            if (currE instanceof MamenchisaurusEntity) {
                double d5 = currE.getDistanceSq(this.posX, this.posY, this.posZ);
                if ((d5 < distance2 * distance2) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityFound2 = currE;
                }
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
            moveHelper.setMoveTo(king.posX, king.posY, king.posZ, 1.0D);
        }
        super.onLivingUpdate();
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
