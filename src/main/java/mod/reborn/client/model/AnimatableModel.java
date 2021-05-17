package mod.reborn.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Set;

@SideOnly(MixinEnvironment.Side.CLIENT)
public class AnimatableModel extends FixedTabulaModel {
    public AnimatableModel(TabulaModelContainer model) {
        this(model, null);

    }

    public AnimatableModel(TabulaModelContainer model, ITabulaModelAnimator animator) {
        super(model, animator);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity) {
        Animatable animatable = (Animatable) entity;

        if (animatable.isCarcass()) {
            this.setMovementScale(0.0F);
        } else {
            this.setMovementScale(animatable.isSleeping() ? 0.5F : 1.0F);
        }

        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
    }

    public String[] getCubeIdentifierArray() {
        String[] cubeIdentifiers = new String[this.identifierMap.size()];
        int index = 0;

        Set<String> identifiers = this.identifierMap.keySet();

        for (String identifier : identifiers) {
            cubeIdentifiers[index] = identifier;
            index++;
        }

        return cubeIdentifiers;
    }

    public String[] getCubeNames() {
        String[] cubeNames = new String[this.cubes.size()];
        int index = 0;

        Set<String> names = this.cubes.keySet();

        for (String identifier : names) {
            cubeNames[index] = identifier;
            index++;
        }

        return cubeNames;
    }

    public Map<String, AdvancedModelRenderer> getIdentifierCubes() {
        return this.identifierMap;
    }

    @Override
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelRenderer... boxes) {
        float actualRotationDivisor = rotationDivisor * boxes.length;
        float yawAmount = MathHelper.clamp(MathHelper.wrapDegrees(yaw), -45.0F, 45.0F) / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = MathHelper.wrapDegrees(pitch) / (180.0F / (float) Math.PI) / actualRotationDivisor;

        for (AdvancedModelRenderer box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }
}
