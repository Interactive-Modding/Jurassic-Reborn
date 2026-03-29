//package net.vit.jurassicreborn.mixin;
//
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.vit.jurassicreborn.common.items.ModItems;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(LivingEntity.class)
//public abstract class GoatMixin extends LivingEntity {
//
//    protected GoatMixin(EntityType<? extends LivingEntity> type, Level level) {
//        super(type, level);
//    }
//
//    @Inject(method = "dropCustomDeathLoot", at = @At("HEAD"))
//    private void dropMixin(DamageSource source, int looting, boolean recentlyHit, CallbackInfo ci) {
//        if (this.getType() == EntityType.GOAT) {
//
//            this.spawnAtLocation(Items.LEATHER, this.random.nextInt(2) + 1);
//
//            if (this.random.nextBoolean()) {
//                this.spawnAtLocation(
//                        new ItemStack(
//                                this.random.nextBoolean()
//                                        ? Blocks.WHITE_WOOL.asItem()
//                                        : Blocks.BROWN_WOOL.asItem(),
//                                1
//                        ),
//                        0.0F
//                );
//            }
//
//            this.spawnAtLocation(
//                    this.isOnFire()
//                            ? ModItems.GOAT_COOKED.get()
//                            : ModItems.GOAT_RAW.get(),
//                    this.random.nextInt(2) + 1
//            );
//        }
//    }
//}