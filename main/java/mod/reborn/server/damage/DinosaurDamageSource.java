package mod.reborn.server.damage;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class DinosaurDamageSource extends DamageSource {
    protected Entity entity;
    private boolean isThornsDamage = false;

    public DinosaurDamageSource(String damageType, Entity entity) {
        super(damageType);
        this.entity = entity;
    }

    public DinosaurDamageSource setIsThornsDamage() {
        this.isThornsDamage = true;
        return this;
    }

    public boolean getIsThornsDamage() {
        return this.isThornsDamage;
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        ItemStack stack = this.entity instanceof EntityLivingBase ? ((EntityLivingBase) this.entity).getHeldItemMainhand() : null;
        String deathMessage = "death.attack." + this.damageType;
        String itemDeathMessage = deathMessage + ".item";
        return stack != null && stack.hasDisplayName() && I18n.hasKey(itemDeathMessage) ? new TextComponentTranslation(itemDeathMessage, entity.getDisplayName(), this.entity.getDisplayName(), stack.getTextComponent()) : new TextComponentTranslation(deathMessage, entity.getDisplayName(), this.entity.getDisplayName());
    }

    @Override
    public Vec3d getDamageLocation() {
        return new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ);
    }

    @Override
    public boolean isDifficultyScaled() {
        return false;
    }
}