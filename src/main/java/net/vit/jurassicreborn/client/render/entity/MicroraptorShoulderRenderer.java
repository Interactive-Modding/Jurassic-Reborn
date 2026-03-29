package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;
import net.vit.jurassicreborn.common.entities.ModEntities;

public class MicroraptorShoulderRenderer {

    @SubscribeEvent
    public void renderShoulderMicroraptor(RenderPlayerEvent.Pre event) {

        Player player = event.getEntity();

        renderShoulder(event, player.getShoulderEntityLeft(), true);
        renderShoulder(event, player.getShoulderEntityRight(), false);
    }

    private static void renderShoulder(RenderPlayerEvent.Pre event, CompoundTag tag, boolean left) {

        if (tag.isEmpty()) return;

        if (!tag.getString("id").contains("microraptor")) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        MicroraptorEntity raptor = new MicroraptorEntity(
                ModEntities.MICRORAPTOR.get(),
                mc.level
        );

        raptor.load(tag);
        raptor.getEntityData().set(MicroraptorEntity.ON_SHOULDER, true);
        Player player = event.getEntity();
        raptor.setPos(player.getX(), player.getY(), player.getZ());
        raptor.xo = raptor.getX();
        raptor.yo = raptor.getY();
        raptor.zo = raptor.getZ();

        float bodyYaw = player.yBodyRot;
        float headYaw = player.getYHeadRot();
        float pitch = player.getXRot();

        raptor.setYBodyRot(bodyYaw);
        raptor.yBodyRotO = bodyYaw;
        raptor.setYRot(bodyYaw);
        raptor.yRotO = bodyYaw;
        raptor.setYHeadRot(headYaw);
        raptor.yHeadRotO = headYaw;
        raptor.setXRot(pitch);
        raptor.xRotO = pitch;
        raptor.tickCount = player.tickCount;

        PoseStack pose = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();

        pose.pushPose();

        float offsetX = left ? 0.35F : -0.35F;

        pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-bodyYaw));
        pose.translate(offsetX, player.isCrouching() ? 1.30F : 1.45F, 0.0F);

        mc.getEntityRenderDispatcher().render(
                raptor,
                0,
                0,
                0,
                bodyYaw,
                event.getPartialTick(),
                pose,
                buffer,
                15728880
        );

        pose.popPose();
    }
}