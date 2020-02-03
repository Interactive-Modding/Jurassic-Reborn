package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class MamenchisaurusEntity extends DinosaurEntity {

    private static boolean isKingSet = false;
    private boolean isKing = false;
    private MamenchisaurusEntity king = null;

    public MamenchisaurusEntity(World world) { super(world); }

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
}
