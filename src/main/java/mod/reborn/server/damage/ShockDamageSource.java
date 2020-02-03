package mod.reborn.server.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ShockDamageSource extends DamageSource {
    public ShockDamageSource() {
        super("shock");
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        return new TextComponentTranslation("death.attack." + this.damageType + "." + (entity.getRNG().nextInt(2) + 1), entity.getDisplayName());
    }
}
