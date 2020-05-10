package mod.reborn.client.event;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class ModelPlayerRenderEvent extends PlayerEvent
{
    private ModelPlayer model;
    private float partialTicks;

    private ModelPlayerRenderEvent(EntityPlayer player, ModelPlayer model, float partialTicks)
    {
        super(player);
        this.model = model;
        this.partialTicks = partialTicks;
    }

    public ModelPlayer getModel()
    {
        return model;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    public static class Render extends ModelPlayerRenderEvent
    {
        private Render(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
        {
            super(player, modelPlayer, partialTicks);
        }

        public static class Pre extends Render
        {
            public Pre(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }
            
            @Override
            public boolean isCancelable()
            {
                return true;
            }
        }

        public static class Post extends Render
        {
            public Post(EntityPlayer player, ModelPlayer modelPlayer, float partialTicks)
            {
                super(player, modelPlayer, partialTicks);
            }

            @Override
            public boolean isCancelable()
            {
                return false;
            }
        }
    }
}