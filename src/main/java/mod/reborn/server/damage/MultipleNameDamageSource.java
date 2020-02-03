package mod.reborn.server.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class MultipleNameDamageSource extends DamageSource {

    private final int totalAmount;
    
    public MultipleNameDamageSource(String damageName, int totalAmount) {
	super(damageName);
	this.totalAmount = totalAmount;
    }
    
    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
	EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack." + this.damageType + String.valueOf(entityLivingBaseIn.getRNG().nextInt(totalAmount));
        String s1 = s + ".player";
        return entitylivingbase != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()}): new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
    }
}