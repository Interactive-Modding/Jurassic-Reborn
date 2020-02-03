package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.entity.LegSolverBiped;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class TyrannosaurusEntity extends DinosaurEntity {
    private int stepCount = 0;

    public LegSolverBiped legSolver;

    public TyrannosaurusEntity(World world) {
        super(world);
        this.target(CompsognathusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, ProtoceratopsEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, StegosaurusEntity.class, BrachiosaurusEntity.class, ApatosaurusEntity.class, RugopsEntity.class, HerrerasaurusEntity.class, SpinosaurusEntity.class, AchillobatorEntity.class, CarnotaurusEntity.class, TherizinosaurusEntity.class, GoatEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, EntityMob.class, DilophosaurusEntity.class, GallimimusEntity.class, TriceratopsEntity.class, ParasaurolophusEntity.class, VelociraptorEntity.class, MussaurusEntity.class);
    }

    @Override
    protected LegSolverBiped createLegSolver() {
        return this.legSolver = new LegSolverBiped(-0.5F, 1.0F, 1.0F);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.TYRANNOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.TYRANNOSAURUS_ROAR;
            case ROARING:
                return SoundHandler.TYRANNOSAURUS_ROAR;
            case DYING:
                return SoundHandler.TYRANNOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.TYRANNOSAURUS_HURT;
            default:
                return null;
        }
    }
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }



    @Override
    public SoundEvent getBreathingSound() {
        return SoundHandler.TYRANNOSAURUS_BREATHING;
    }
   
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.onGround && !this.isInWater()) {
            if (this.moveForward > 0 && (this.posX - this.prevPosX > 0 || this.posZ - this.prevPosZ > 0) && this.stepCount <= 0) {
                this.playSound(SoundHandler.STOMP, (float) this.interpolate(0.1F, 1.0F), this.getSoundPitch());
                this.stepCount = 65;
            }
            this.stepCount -= this.moveForward * 9.5;
        }
    }
}
