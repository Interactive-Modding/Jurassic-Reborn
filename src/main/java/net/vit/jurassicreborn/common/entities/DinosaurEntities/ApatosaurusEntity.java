package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.SpawnGroupData;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.vit.jurassicreborn.common.entities.LegSolverQuadruped;

import java.util.Locale;

public class ApatosaurusEntity extends DinosaurEntity {

    private int stepCount = 0;
    public LegSolverQuadruped legSolver;

    public ApatosaurusEntity(Level level, EntityType<ApatosaurusEntity> type) {
        super(level, type, DinosaurHandler.APATOSAURUS);

        this.addTask(1, new net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal(this));


    }
    @Override
    public SpawnGroupData finalizeSpawn(
            net.minecraft.world.level.ServerLevelAccessor level,
            net.minecraft.world.DifficultyInstance difficulty,
            net.minecraft.world.entity.MobSpawnType reason,
            SpawnGroupData spawnData,
            net.minecraft.nbt.CompoundTag dataTag
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        this.setVariant(this.getRandom().nextInt(3));

        return data;
    }
    @Override
    protected LegSolverQuadruped createLegSolver() {
        return this.legSolver = new LegSolverQuadruped(
                2.5F, 2.0F,
                1.0F, 1.0F,
                1.0F
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (this.onGround() && !this.isInWater()) {
            if (this.zza > 0
                    && (this.getX() - this.xOld > 0 || this.getZ() - this.zOld > 0)
                    && this.stepCount <= 0) {

                this.playSound(
                        SoundHandler.STOMP,
                        (float) this.interpolate(0.1F, 1.0F),
                        this.getVoicePitch()
                );
                this.stepCount = 65;
            }
            this.stepCount -= this.zza * 9.5;
        }
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        return switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK   -> SoundHandler.APATOSAURUS_LIVING;
            case DYING   -> SoundHandler.APATOSAURUS_DEATH;
            case INJURED -> SoundHandler.APATOSAURUS_HURT;
            case CALLING -> SoundHandler.APATOSAURUS_CALL;
            case BEGGING -> SoundHandler.APATOSAURUS_THREAT;
            case WALKING -> SoundHandler.STOMP;
            default      -> null;
        };
    }

    /* -------------------- TEXTURES -------------------- */

    public ResourceLocation getTexture() {
        return switch (this.getVariant()) {
            default -> texture("jw");
            case 1  -> texture("steppe");
            case 2  -> texture("taiga");
        };
    }

    private ResourceLocation texture(String variant) {
        String name = this.dinosaur.getName()
                .toLowerCase(Locale.ENGLISH)
                .replace(" ", "_");

        String base = "textures/entities/" + name + "/" + name;
        String sex  = this.isMale() ? "male" : "female";

        return new ResourceLocation(
                JurassicReborn.MODID,
                base + "_" + sex + "_adult_" + variant + ".png"
        );
    }
}
