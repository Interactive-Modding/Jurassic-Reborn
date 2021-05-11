package mod.reborn.client.events;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class ModelPlayerRenderEvent extends PlayerEvent {

    private final PlayerModel<AbstractClientPlayerEntity> model;
    private final float partialTicks;

    public ModelPlayerRenderEvent(PlayerEntity player, PlayerModel<AbstractClientPlayerEntity> model, float partialTicks) {
        super(player);
        this.model = model;
        this.partialTicks = partialTicks;
    }

    public PlayerModel<AbstractClientPlayerEntity> getModel() {
        return model;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public static class Render extends ModelPlayerRenderEvent {

        private Render(PlayerEntity playerEntity, PlayerModel<AbstractClientPlayerEntity> playerModel, float partialTicks) {
            super(playerEntity, playerModel, partialTicks);
        }

        public static class Pre extends Render {

            public Pre(PlayerEntity playerEntity, PlayerModel<AbstractClientPlayerEntity> playerModel, float partialTicks)
            {
                super(playerEntity, playerModel, partialTicks);
            }

            @Override
            public boolean isCancelable() {
                return true;
            }
        }

        public static class Post extends Render {

            public Post(PlayerEntity playerEntity, PlayerModel<AbstractClientPlayerEntity> playerModel, float partialTicks)
            {
                super(playerEntity, playerModel, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }


    }
}
