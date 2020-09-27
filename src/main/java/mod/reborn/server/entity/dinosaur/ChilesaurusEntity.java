package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.LeapingMeleeEntityAI;
import mod.reborn.server.entity.ai.RaptorLeapEntityAI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class ChilesaurusEntity extends DinosaurEntity {

    private static boolean isKingSet = false;
    private boolean isKing = false;
    private ChilesaurusEntity king = null;

    public ChilesaurusEntity(World world) {
        super(world);
        this.tasks.addTask(1, new RaptorLeapEntityAI(this));
    }


    @Override
    public void onLivingUpdate() {
        double distance2 = 18.0D;
        Entity entityFound2 = null;
        double d4 = -1.0D;
        for (Entity currE : this.world.loadedEntityList) {
            if (currE instanceof ChilesaurusEntity) {
                double d5 = currE.getDistanceSq(this.posX, this.posY, this.posZ);
                if ((d5 < distance2 * distance2) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityFound2 = currE;
                }
            }
        }
        if (entityFound2 != null) {
            if (!isKingSet) {
                king = ((ChilesaurusEntity) entityFound2);
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
}
