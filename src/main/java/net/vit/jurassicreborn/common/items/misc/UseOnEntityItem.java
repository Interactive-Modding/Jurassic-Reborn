package net.vit.jurassicreborn.common.items.misc;

import com.github.alexthe666.citadel.repack.jaad.Play;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class UseOnEntityItem extends Item {


    private final Function<Interaction, InteractionResult> func;

    public UseOnEntityItem(Item.Properties props, Function<Interaction, InteractionResult> func) {
        super(props);
        this.func = func;
    }

    //We're going to use a Forge event for this


    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        return this.func.apply(new Interaction(pStack, pPlayer, pInteractionTarget, pUsedHand));
    }

    public class Interaction {
        private final ItemStack stack;
        private final Player player;
        private final LivingEntity target;
        private final InteractionHand hand;

        public Interaction(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
            this.stack = stack;
            this.player = player;
            this.target = target;
            this.hand = hand;
        }

        public ItemStack getStack() {
            return stack;
        }

        public Player getPlayer() {
            return player;
        }

        public LivingEntity getTarget() {
            return target;
        }

        public InteractionHand getHand() {
            return hand;
        }
    }
}
