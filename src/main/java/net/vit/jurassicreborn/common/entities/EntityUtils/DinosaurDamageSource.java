package net.vit.jurassicreborn.common.entities.EntityUtils;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

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


    public Component getLocalizedDeathMessage(LivingEntity pLivingEntity){
        ItemStack stack = this.entity instanceof LivingEntity ? ((LivingEntity) this.entity).getMainHandItem() : null;
        String deathMessage = "death.attack." + this.msgId;
        String itemDeathMessage = deathMessage + ".item";
        if (stack != null && stack.hasCustomHoverName() && I18n.exists(itemDeathMessage)) {
            return new TranslatableComponent(itemDeathMessage, pLivingEntity.getDisplayName(), this.entity.getDisplayName(), stack.getDisplayName());
        }
        return new TranslatableComponent(deathMessage, pLivingEntity.getDisplayName(), this.entity.getDisplayName());

    }

    @Override
    public Vec3 getSourcePosition() {
        return new Vec3(this.entity.getX(), this.entity.getY(), this.entity.getZ());
    }

    @Override
    public boolean scalesWithDifficulty() {
        return false;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return this.entity;
    }
}