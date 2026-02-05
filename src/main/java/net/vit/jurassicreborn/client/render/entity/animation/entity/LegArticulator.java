package net.vit.jurassicreborn.client.render.entity.animation.entity;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.LegSolverBiped;
import net.vit.jurassicreborn.common.entities.LegSolverQuadruped;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;

public final class LegArticulator {
    private LegArticulator() {}

    public static void articulateBiped(
            DinosaurEntity entity,
            LegSolverBiped legs,
            AdvancedModelBox body,
            AdvancedModelBox leftThigh, AdvancedModelBox leftCalf,
            AdvancedModelBox rightThigh, AdvancedModelBox rightCalf,
            float rotThigh, float rotCalf,
            float delta
    ) {
        float heightLeft = legs.left.getHeight(delta);
        float heightRight = legs.right.getHeight(delta);
        if (heightLeft > 0 || heightRight > 0) {
            float sc = getScale(entity);
            float avg = avg(heightLeft, heightRight);
            body.rotationPointY += 16 / sc * avg;
            articulateLegPair(sc, heightLeft, heightRight, avg, 0,
                    leftThigh, leftCalf, rightThigh, rightCalf, rotThigh, rotCalf);
        }
    }

    // front legs must be connected to body
    public static void articulateQuadruped(
            DinosaurEntity entity, LegSolverQuadruped legs,
            AdvancedModelBox body, AdvancedModelBox neck,
            AdvancedModelBox backLeftThigh,  AdvancedModelBox backLeftCalf,
            AdvancedModelBox backRightThigh, AdvancedModelBox backRightCalf,
            AdvancedModelBox frontLeftThigh, AdvancedModelBox frontLeftCalf,
            AdvancedModelBox frontRightThigh, AdvancedModelBox frontRightCalf,
            float rotBackThigh,  float rotBackCalf,
            float rotFrontThigh, float rotFrontCalf,
            float delta
    ) {
        float heightBackLeft  = legs.backLeft.getHeight(delta);
        float heightBackRight = legs.backRight.getHeight(delta);
        float heightFrontLeft  = legs.frontLeft.getHeight(delta);
        float heightFrontRight = legs.frontRight.getHeight(delta);

        if (heightBackLeft > 0 || heightBackRight > 0 || heightFrontLeft > 0 || heightFrontRight > 0) {
            float sc = getScale(entity);

            float backAvg  = avg(heightBackLeft,  heightBackRight);
            float frontAvg = avg(heightFrontLeft, heightFrontRight);

            float bodyLength = Math.abs(
                    avg(legs.backLeft.forward, legs.backRight.forward)
                            - avg(legs.frontLeft.forward, legs.frontRight.forward)
            );

            float tilt = (float) (Mth.atan2(bodyLength * sc, backAvg - frontAvg) - Math.PI / 2);

            body.rotationPointY += 16 / sc * backAvg;
            body.rotateAngleX   += tilt;

            // counter-tilt the front assembly to keep shoulder alignment
            frontLeftThigh.rotateAngleX  -= tilt;
            frontRightThigh.rotateAngleX -= tilt;
            neck.rotateAngleX            -= tilt;

            // rear pair
            articulateLegPair(sc, heightBackLeft, heightBackRight, backAvg, 0,
                    backLeftThigh, backLeftCalf, backRightThigh, backRightCalf,
                    rotBackThigh, rotBackCalf);

            // front pair (with offset by -frontAvg)
            articulateLegPair(sc, heightFrontLeft, heightFrontRight, frontAvg, -frontAvg,
                    frontLeftThigh, frontLeftCalf, frontRightThigh, frontRightCalf,
                    rotFrontThigh, rotFrontCalf);
        }
    }

    private static void articulateLegPair(
            float sc,
            float heightLeft, float heightRight,
            float avg, float offsetY,
            AdvancedModelBox leftThigh,  AdvancedModelBox leftCalf,
            AdvancedModelBox rightThigh, AdvancedModelBox rightCalf,
            float rotThigh, float rotCalf
    ) {
        float difLeft  = Math.max(0, heightRight - heightLeft);
        float difRight = Math.max(0, heightLeft  - heightRight);

        leftThigh.rotationPointY  += 16 / sc * (Math.max(heightLeft,  avg) + offsetY);
        rightThigh.rotationPointY += 16 / sc * (Math.max(heightRight, avg) + offsetY);

        leftThigh.rotateAngleX  -= rotThigh * difLeft;
        leftCalf.rotateAngleX   += rotCalf  * difLeft;

        rightThigh.rotateAngleX -= rotThigh * difRight;
        rightCalf.rotateAngleX  += rotCalf  * difRight;
    }

    private static float avg(float a, float b) {
        return (a + b) / 2F;
    }
    private static float getScale(DinosaurEntity entity) {
        float scaleModifier = 1.0F;
        if (entity.getLegacyAttributes() != null) {
            scaleModifier = entity.getLegacyAttributes().getScaleModifier();
        }
        Dinosaur dino = entity.getDinosaur();
        boolean skeleton = entity.isSkeleton();
        return (float) entity.interpolate(dino.getScaleInfant(skeleton), dino.getScaleAdult(skeleton)) * scaleModifier;
    }
}
