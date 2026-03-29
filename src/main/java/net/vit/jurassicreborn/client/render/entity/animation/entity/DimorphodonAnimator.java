package net.vit.jurassicreborn.client.render.entity.animation.entity;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DimorphodonEntity;

@OnlyIn(Dist.CLIENT)
public class DimorphodonAnimator extends AbstractPterosaurAnimator<DimorphodonEntity> {

    @Override
    protected String neck1Cube() {
        return "Neck";
    }

    @Override
    protected String neck2Cube() {
        return "Neck2";
    }

    @Override
    protected String neck3Cube() {
        return null;
    }

    @Override
    protected float groundSpeed() {
        return 0.58F;
    }

    @Override
    protected float groundDegree() {
        return 2.1F;
    }

    @Override
    protected float groundHeight() {
        return 1.8F;
    }

    @Override
    protected float frontOffset() {
        return -1.35F;
    }
}