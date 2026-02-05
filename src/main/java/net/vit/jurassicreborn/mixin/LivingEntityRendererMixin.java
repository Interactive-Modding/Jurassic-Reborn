package net.vit.jurassicreborn.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.util.EntityColorTint;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {


    //this is going to be dumb. I'm sorry for my crimes. - gamma
    ArrayList<Optional<?>> renderParams = new ArrayList<>();

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"))
    public void renderParamAccessor(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci){
        if(renderParams.isEmpty()){//check if the param list is empty - has render been called yet?
            renderParams.add(Optional.of(pEntity));//the entity is the first object in the list
            renderParams.add(Optional.of(pEntityYaw));//the entity yaw is the second object in the list
            renderParams.add(Optional.of(pPartialTicks));//the frame/partial tick is the third object in the list
            renderParams.add(Optional.of(pMatrixStack));//the matrix stack is the fourth object in the list
            renderParams.add(Optional.of(pBuffer));//the buffer is the fifth object in the list
            renderParams.add(Optional.of(pPackedLight));//the packedLight is the sixth object in the list
        }else{
            renderParams.set(0, Optional.of(pEntity));//update the param list
            renderParams.set(1, Optional.of(pEntityYaw));
            renderParams.set(2, Optional.of(pPartialTicks));
            renderParams.set(3, Optional.of(pMatrixStack));
            renderParams.set(4, Optional.of(pBuffer));
            renderParams.set(5, Optional.of(pPackedLight));
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", ordinal = 0))
    public void renderMixin(EntityModel<? extends Entity> instance, PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a){

        if(!renderParams.isEmpty() && renderParams.get(0).isPresent()){//run our code here! if it is empty, however, we want to run minecraft's. - gamma
            if(EntityColorTint.isEntityInList(((Optional<LivingEntity>)renderParams.get(0)).get())){//this should NEVER error. if it does, i fucked up. - gamma
//                System.out.println("got entity");
                Color tint = EntityColorTint.getColor();
//                System.out.println(tint);
//                System.out.println("light: %s | overlay: %s | rgba : %s %s %s %s".formatted(light, overlay, r, g, b, a));
                instance.renderToBuffer(pose, consumer, light, overlay, tint.getRed()/255f, tint.getGreen()/255f, tint.getBlue()/255f, tint.getAlpha()/255f);//render with a tint! - gamma
                EntityColorTint.clearColor();
            }

        }
        instance.renderToBuffer(pose, consumer, light, overlay, r, g, b, a);

    }
    @Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
    private void jurassicreborn$hideDinoName(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof DinosaurEntity dinosaur && !dinosaur.hasCustomName()) {
            cir.setReturnValue(false);
        }
        if (entity instanceof SharkEntity sharkEntity && !sharkEntity.hasCustomName()) {
            cir.setReturnValue(false);
        }
        if (entity instanceof GoatEntity goatEntity && !goatEntity.hasCustomName()) {
            cir.setReturnValue(false);
        }
        if (entity instanceof CrabEntity crabEntity && !crabEntity.hasCustomName()) {
            cir.setReturnValue(false);
        }
    }

}
