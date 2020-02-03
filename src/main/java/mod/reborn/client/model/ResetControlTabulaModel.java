package mod.reborn.client.model;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.entity.Entity;

public class ResetControlTabulaModel<T extends Entity> extends TabulaModel {
    private final ITabulaModelAnimator<T> animator;
    private boolean resetAllowed;

    public ResetControlTabulaModel(TabulaModelContainer model) {
        this(model, null);
    }

    public ResetControlTabulaModel(TabulaModelContainer model, ITabulaModelAnimator<T> animator) {
        super(model, animator);
        this.animator = animator;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity) {
        if (this.resetAllowed) {
            super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
        } else {
            if (this.animator != null) {
                this.animator.setRotationAngles(this, (T) entity, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks);
            }
        }
    }

    public void setResetEachFrame(boolean reset) {
        this.resetAllowed = reset;
    }
    
    public ITabulaModelAnimator<T> getAnimator() {
	return animator;
    }
}
