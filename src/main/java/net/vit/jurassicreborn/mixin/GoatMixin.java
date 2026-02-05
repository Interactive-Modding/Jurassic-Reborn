package net.vit.jurassicreborn.mixin;

import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class GoatMixin extends LivingEntity{



    protected GoatMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method="dropCustomDeathLoot", at = @At("HEAD"))
    public void dropMixin(DamageSource pSource, int pLooting, boolean pRecentlyHit, CallbackInfo ci){
        if(this.getType() == EntityType.GOAT){
            this.spawnAtLocation(Items.LEATHER, this.random.nextInt(2) + 1);
            if (this.random.nextBoolean()) {
                this.spawnAtLocation(new ItemStack(this.random.nextBoolean() ? Blocks.WHITE_WOOL : Blocks.BROWN_WOOL, 1), 0.0F);
            }
            this.spawnAtLocation(this.isOnFire() ? ModItems.GOAT_COOKED.get() : ModItems.GOAT_RAW.get(), this.random.nextInt(2) + 1);
        }
    }
}
