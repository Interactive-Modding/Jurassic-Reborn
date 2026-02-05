package net.vit.jurassicreborn.common.items.misc;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.vit.jurassicreborn.client.screens.SelectDinosaurScreen;
import net.vit.jurassicreborn.common.entities.item.PaddockSignEntity;

public class PaddockSignItem extends Item {
    public PaddockSignItem(Properties props) {
        super(props);
    }


    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world   = ctx.getLevel();
        Player player = ctx.getPlayer();
        Direction face = ctx.getClickedFace();
        BlockPos clickedPos  = ctx.getClickedPos();

        // Only allow on walls, never place here!
        if (face.getAxis().isHorizontal() && player != null) {
            if (world.isClientSide()) {
                openClientGui(clickedPos, face, ctx.getHand());
            }
            // Always handled, never place here
            return InteractionResult.sidedSuccess(world.isClientSide());
        }

        return InteractionResult.PASS;
    }


    private void openClientGui(BlockPos signPos, Direction face, net.minecraft.world.InteractionHand hand) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> doOpenScreen(signPos, face, hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void doOpenScreen(BlockPos signPos, Direction face, net.minecraft.world.InteractionHand hand) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new SelectDinosaurScreen(signPos, face, hand));
    }
}
