package net.vit.jurassicreborn.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;

import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class AnimatableModel extends FixedTabulaModel {
    public AnimatableModel(TabulaModelContainer model) {
        this(model, null);
    }

    public AnimatableModel(TabulaModelContainer model, ITabulaModelAnimator animator) {
        super(model, animator);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof Animatable animatable) {
            if (animatable.isCarcass()) {
                this.setMovementScale(0.0F);
            } else {
                this.setMovementScale(animatable.isSleeping() ? 0.5F : 1.0F);
            }
        }
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
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

    public Map<String, AdvancedModelBox> getIdentifierCubes() {
        return this.identifierMap;
    }

    @Override
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelBox... boxes) {
        float actualRotationDivisor = rotationDivisor * boxes.length;
        float yawAmount = Mth.clamp(Mth.wrapDegrees(yaw), -45.0F, 45.0F) / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = Mth.wrapDegrees(pitch) / (180.0F / (float) Math.PI) / actualRotationDivisor;
        for (AdvancedModelBox box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }
}