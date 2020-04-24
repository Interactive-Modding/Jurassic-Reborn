package mod.reborn.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CustomModelPlayer extends ModelPlayer
{
	
    public CustomModelPlayer(float modelSize, boolean slimArms)
    {
        super(modelSize, slimArms);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if(!MinecraftForge.EVENT_BUS.post(new ModelPlayerRenderEvent.Render.Pre((EntityPlayer) entityIn, this, Minecraft.getMinecraft().getRenderPartialTicks())))
        {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            MinecraftForge.EVENT_BUS.post(new ModelPlayerRenderEvent.Render.Post((EntityPlayer) entityIn, this, Minecraft.getMinecraft().getRenderPartialTicks()));
        }
    }

}