package mod.reborn.server.items;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import java.util.function.Function;

public class EntityRightClickItem extends Item {

    private final Function<Interaction, ActionResultType> func;

    public EntityRightClickItem(Function<Interaction, ActionResultType> func) {
        super(new Properties().group(TabHandler.ITEMS));
        this.func = func;
    }

    public EntityRightClickItem(Function<Interaction, ActionResultType> func, ItemGroup tab) {
        super(new Properties().group(tab));
        this.func = func;
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        return func.apply(new Interaction(stack, player, target, hand));
    }

    public static class Interaction {
        private final ItemStack stack;
        private final PlayerEntity player;
        private final LivingEntity target;
        private final Hand hand;

        public Interaction(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
            this.stack = stack;
            this.player = player;
            this.target = target;
            this.hand = hand;
        }

        public ItemStack getStack() {
            return stack;
        }

        public PlayerEntity getPlayer() {
            return player;
        }

        public LivingEntity getTarget() {
            return target;
        }

        public Hand getHand() {
            return hand;
        }
    }
}
