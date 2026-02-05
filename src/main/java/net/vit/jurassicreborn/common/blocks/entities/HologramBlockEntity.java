package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;

public class HologramBlockEntity extends ActionFigureBlockEntity {
    public static final int ROTATION_STEP_DEGREES = 45;

    public static final String TAG_DINO_INDEX = "DinoIndex";
    public static final String TAG_POSE_INDEX = "PoseIndex";
    public static final String TAG_ROTATING = "Rotating";
    public static final String TAG_ROTATION = "Rotation";

    private int dinoIndex = 0;
    private int poseIndex = 0;
    private boolean rotating = true;
    private int cachedRotation = 0;

    public HologramBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOLOGRAM_BLOCK_ENTITY.get(), pos, state);
    }

    // -----------------------------------------------------
    // Rotation helpers
    // -----------------------------------------------------

    public static int snapRotation(int rotation) {
        int snapped = (int) Math.round(rotation / (double) ROTATION_STEP_DEGREES) * ROTATION_STEP_DEGREES;
        return Math.floorMod(snapped, 360);
    }

    private void saveAndSync() {
        setChanged();
        if (!suppressUpdates && level instanceof ServerLevel server) {
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            server.getChunkSource().blockChanged(worldPosition);
        }
    }

    // -----------------------------------------------------
    // Load / Save (critical part for persistence)
    // -----------------------------------------------------

    @Override
    public void load(CompoundTag tag) {
        // Load ActionFigure core data first
        super.load(tag);

        DinosaurHandler.doDinosInit();

        int dinoCount = Math.max(DinosaurHandler.count(), 1);
        int loadedIndex = this.dinoIndex;
        if (tag.contains(TAG_DINO_INDEX, Tag.TAG_INT)) {
            loadedIndex = tag.getInt(TAG_DINO_INDEX);
        } else if (tag.contains("Dinosaur", Tag.TAG_STRING)) {
            Dinosaur saved = Dinosaur.getDinosaurByName(tag.getString("Dinosaur"));
            loadedIndex = DinosaurHandler.getId(saved);
        }

        int poseCount = Math.max(EntityAnimation.values().length, 1);
        int loadedPose = tag.contains(TAG_POSE_INDEX, Tag.TAG_INT)
                ? tag.getInt(TAG_POSE_INDEX)
                : this.poseIndex;
        boolean loadedRotating = !tag.contains(TAG_ROTATING) || tag.getBoolean(TAG_ROTATING);

        int storedRot = tag.contains(TAG_ROTATION, Tag.TAG_INT) ? tag.getInt(TAG_ROTATION) : this.cachedRotation;

        applySettings(Math.floorMod(loadedIndex, Math.max(dinoCount, 1)),
                Math.floorMod(loadedPose, Math.max(poseCount, 1)),
                loadedRotating,
                storedRot,
                false);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        // Save ActionFigure base data first
        super.saveAdditional(tag);

        // Then hologram-specific
        tag.putInt(TAG_DINO_INDEX, this.dinoIndex);
        tag.putInt(TAG_POSE_INDEX, this.poseIndex);
        tag.putBoolean(TAG_ROTATING, this.rotating);
        tag.putInt(TAG_ROTATION, getRot());
    }

    // -----------------------------------------------------
    // Lifecycle
    // -----------------------------------------------------

    @Override
    public void onLoad() {
        super.onLoad();
        DinosaurHandler.doDinosInit();

        Dinosaur dino = DinosaurHandler.getById(dinoIndex);
        if (dino != null && dino != Dinosaur.EMPTY) {
            super.setDinosaur(dino, true, false, false);
        }

        if (!rotating) {
            setRotationSilently(cachedRotation);
        }

        applyPose();
    }

    // -----------------------------------------------------
    // Client & Server ticking
    // -----------------------------------------------------

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        if (rotating) {
            setRotationSilently((getRot() + 2) % 360);
        }
        applyPose();
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        // Nothing needed here; state is updated via packets
    }

    // -----------------------------------------------------
    // State control
    // -----------------------------------------------------

    public void setDinosaurById(int id) {
        DinosaurHandler.doDinosInit();
        int count = Math.max(DinosaurHandler.count(), 1);
        this.dinoIndex = Math.floorMod(id, count);
        Dinosaur dino = DinosaurHandler.getById(dinoIndex);
        setDinosaur(dino, true, false, false);
        applyPose();
        saveAndSync();
    }

    @Override
    public void setDinosaur(Dinosaur dino, boolean gender, boolean isSkeleton, boolean isFossile) {
        super.setDinosaur(dino, gender, isSkeleton, isFossile);
        this.dinoIndex = DinosaurHandler.getId(dino);
    }

    public void setPoseIndex(int poseIndex) {
        int count = EntityAnimation.values().length;
        this.poseIndex = Math.floorMod(poseIndex, count);
        applyPose();
        saveAndSync();
    }

    public void setRotating(boolean rotating) {
        boolean wasRotating = this.rotating;
        this.rotating = rotating;

        if (wasRotating && !rotating) {
            int snapped = snapRotation(getRot());
            setRotationSilently(snapped);
            this.cachedRotation = snapped;
        }

        saveAndSync();
    }

    @Override
    public void setRot(int rotation) {
        int finalRotation = rotating ? Math.floorMod(rotation, 360) : snapRotation(rotation);
        this.cachedRotation = finalRotation;
        setRotationSilently(finalRotation);
        setChanged();
        if (!suppressUpdates && this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    // -----------------------------------------------------
    // Accessors
    // -----------------------------------------------------

    public int getDinoIndex() {
        return dinoIndex;
    }

    public int getPoseIndex() {
        return poseIndex;
    }

    public boolean isRotating() {
        return rotating;
    }

    public EntityAnimation getPoseAnimation() {
        EntityAnimation[] values = EntityAnimation.values();
        return values[Math.floorMod(poseIndex, values.length)];
    }

    // -----------------------------------------------------
    // Helpers
    // -----------------------------------------------------

    private void applyPose() {
        if (this.level != null) {
            var entity = getEntity();
            if (entity != null) {
                entity.setAnimation(getPoseAnimation().get());
            }
        }
    }

    public void applySettings(int dinoIndex, int poseIndex, boolean rotating, int rotation, boolean sync) {
        DinosaurHandler.doDinosInit();

        int dinoCount = Math.max(DinosaurHandler.count(), 1);
        int normalizedDino = Math.floorMod(dinoIndex, dinoCount);

        int poseCount = Math.max(EntityAnimation.values().length, 1);
        int normalizedPose = Math.floorMod(poseIndex, poseCount);

        int normalizedRotation = rotating
                ? Math.floorMod(rotation, 360)
                : snapRotation(rotation);

        boolean previousSuppressed = this.suppressUpdates;
        this.suppressUpdates = true;
        try {
            setDinosaurById(normalizedDino);
            setPoseIndex(normalizedPose);
            setRotating(rotating);
            setRot(normalizedRotation);
        } finally {
            this.suppressUpdates = previousSuppressed;
        }

        if (sync) {
            saveAndSync();
        } else {
            setChanged();
        }
    }

    public void applySettingsFromTag(CompoundTag tag, boolean sync) {
        int dinoIndex = tag.contains(TAG_DINO_INDEX, Tag.TAG_INT) ? tag.getInt(TAG_DINO_INDEX) : this.dinoIndex;
        int poseIndex = tag.contains(TAG_POSE_INDEX, Tag.TAG_INT) ? tag.getInt(TAG_POSE_INDEX) : this.poseIndex;
        boolean rotating = !tag.contains(TAG_ROTATING) || tag.getBoolean(TAG_ROTATING);
        int rotation = tag.contains(TAG_ROTATION, Tag.TAG_INT) ? tag.getInt(TAG_ROTATION) : this.cachedRotation;

        applySettings(dinoIndex, poseIndex, rotating, rotation, sync);
    }
}
