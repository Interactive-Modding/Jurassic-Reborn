package mod.reborn.server.entity.ai.metabolism;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.food.FoodHelper;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EatFoodItemEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    protected EntityItem item;

    public EatFoodItemEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dinosaur.isDead && !this.dinosaur.isCarcass() && GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.world)) {
            if (this.dinosaur.getMetabolism().isHungry()) {
                double posX = this.dinosaur.posX;
                double posY = this.dinosaur.posY;
                double posZ = this.dinosaur.posZ;

                double closestDist = Integer.MAX_VALUE;
                EntityItem closest = null;

                boolean found = false;

                World world = this.dinosaur.world;

                List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(posX - 16, posY - 16, posZ - 16, posX + 16, posY + 16, posZ + 16));

                for (EntityItem entity : items) {
                    ItemStack stack = entity.getItem();

                    Item item = stack.getItem();

                    if (FoodHelper.isEdible(this.dinosaur, this.dinosaur.getDinosaur().getDiet(), item)) {
                        double deltaX = Math.abs(posX - entity.posX);
                        double deltaY = Math.abs(posY - entity.posY);
                        double deltaZ = Math.abs(posZ - entity.posZ);

                        double distance = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);

                        if (distance < closestDist) {
                            closestDist = distance;
                            closest = entity;

                            found = true;
                        }
                    }
                }

                if (found) {
                    this.dinosaur.getNavigator().tryMoveToEntityLiving(closest, 1.0);
                    this.item = closest;

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dinosaur != null && !this.dinosaur.getNavigator().noPath() && this.item != null && !this.item.isDead;
    }
}