package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.IHasVariants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ActionFigureBlockEntity extends BlockEntity {


    public boolean suppressUpdates = false;

    private Dinosaur dinosaur = DinosaurHandler.VELOCIRAPTOR;
    private DinosaurEntity entity;
    private EntityType<? extends DinosaurEntity> entityType;
    private int rotation;
    private boolean isFossile;
    private boolean isMale;
    private static final byte NO_VARIANT = (byte) -1;
    private byte variant = NO_VARIANT;
    private boolean isSkeleton;
    private CompoundTag entityTag;

//    private SerializedData serializedData = new InvalidData();

    public ActionFigureBlockEntity(BlockEntityType<? extends ActionFigureBlockEntity> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }

    public ActionFigureBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntities.DISPLAY_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }


    @Override
    public void onLoad() {
        // ensure the display entity exists once the level is available
        checkAndLoadEntity();
    }

    public void setDinosaur(Dinosaur dino, boolean gender, boolean isSkeleton, boolean isFossile){
        this.dinosaur = dino;

        this.isMale = gender;
        this.isSkeleton = isSkeleton;
        this.isFossile = isFossile;

        loadEntityType();

        if(this.level != null && this.entityType != null){
            this.entity = this.entityType.create(this.level);
            this.initializeEntity(entity);
            if(!this.level.isClientSide && !this.suppressUpdates){
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        } else {
            this.entity = null;
        }

        this.setChanged();

    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        // send all persistent data to the client when the chunk is loaded
        return this.saveWithoutMetadata();
    }
    public void setDinosaur(Dinosaur dino, boolean gender, boolean isSkeleton, boolean isFossile, CompoundTag entityTag){
        this.dinosaur = dino;

        this.isMale = gender;
        this.isSkeleton = isSkeleton;
        this.isFossile = isFossile;
        this.entityTag = entityTag.copy();

        loadEntityType();

        if(this.level != null && this.entityType != null){
            this.entity = this.entityType.create(this.level);
            this.initializeEntity(entity, entityTag);

            if(!this.level.isClientSide && !this.suppressUpdates){
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        } else {
            this.entity = null;
        }

        this.setChanged();

    }

    public void loadEntityType(){
        var registry = DinosaurEntity.CLASS_TYPE_LIST.get(this.dinosaur.getDinosaurClass());
        this.entityType = registry != null ? registry.get() : null;
    }

    public void checkAndLoadEntity(){
        if (this.entity != null)
                        return;

        loadEntityType();

        if(this.entity == null && this.entityType != null) {

            this.entity = this.entityType.create(this.level);

            if(this.entity != null){
                if(this.entityTag != null && !this.entityTag.isEmpty()){
                    this.initializeEntity(this.entity, this.entityTag);
                }else{
                    this.initializeEntity(this.entity);
                }
            }
        }
    }


    //these were SUCH a bad way of doing this i have no clue how it worked
//    public void setDinosaur(String dinosaurId, boolean isMale, boolean isSkeleton) {
//        this.isMale = isMale;
//        this.isSkeleton = isSkeleton;
//        try {
//            Dinosaur dinosaur = Dinosaur.getDinosaurByName(dinosaurId);
//            this.entity = dinosaur.getDinosaurClass().getDeclaredConstructor(Level.class).newInstance(this.level);
//            this.initializeEntity(this.entity);
//        } catch (Exception e) {
//        }
//        this.setChanged();
//    }
//    public void setDinosaur(String dinosaurId, boolean isMale, boolean isSkeleton, byte variant, boolean isFossile) {
//        this.isMale = isMale;
//        this.isSkeleton = isSkeleton;
//        this.variant = variant;
//        this.isFossile = isFossile;
//
//        try {
//            Dinosaur dinosaur = Dinosaur.getDinosaurByName(dinosaurId);
//            this.entity = dinosaur.getDinosaurClass().getDeclaredConstructor(Level.class).newInstance(this.level);
//            this.initializeEntity(this.entity);
//        } catch (Exception e) {
//        }
//        this.setChanged();
//    }


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        String dinoName = nbt.getString("Dinosaur");


        var dinosaur = Dinosaur.getDinosaurByName(dinoName);

        boolean isMale = !nbt.contains("IsMale") || nbt.getBoolean("IsMale");
        boolean isSkeleton = nbt.getBoolean("IsSkeleton");


        if (!nbt.contains("IsFossile")) nbt.putBoolean("IsFossile", false);
        this.isFossile = nbt.getBoolean("IsFossile");

        this.dinosaur   = dinosaur;
        this.isMale     = isMale;
        this.isSkeleton = isSkeleton;
        this.isFossile  = nbt.getBoolean("IsFossile");
        this.entityTag  = nbt.getCompound("DinosaurTag").copy(); // add a private CompoundTag entityTag; field
        this.entity     = null;          // spawn later, on the client side only
        loadEntityType();
        this.dinosaur = dinosaur;

        this.rotation = nbt.getInt("Rotation");
        if (nbt.contains("Variant", Tag.TAG_BYTE)) {
            this.variant = nbt.getByte("Variant");
        } else {
            this.variant = NO_VARIANT;
        }


    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        if (this.entity != null) {
            this.syncVariantFromEntity();
            CompoundTag tag = this.entity.serializeNBT();
                        tag.remove("UUID");  // prevent duplicate-UUID clones
                        nbt.put("DinosaurTag", tag);
        }

        nbt.putString("Dinosaur", this.dinosaur.getName());


        nbt.putInt("Rotation", this.rotation);
        nbt.putBoolean("IsMale", this.isMale);
        nbt.putBoolean("IsSkeleton", this.isSkeleton);
        nbt.putByte("Variant", this.variant);
        nbt.putBoolean("IsFossile", this.isFossile);
    }

//    @Override
//    public CompoundTag serializeNBT() {
//        CompoundTag nbt = super.serializeNBT();
//
//        if (this.entity != null) {
//            CompoundTag tag = this.entity.serializeNBT();
//            nbt.put("DinosaurTag", tag);
//        } else if (this.serializedData != null) {
//            this.serializedData.serialize(nbt);
//        }
//
//        nbt.putInt("Rotation", this.rotation);
//        nbt.putBoolean("IsMale", this.isMale);
//        nbt.putBoolean("IsSkeleton", this.isSkeleton);
//        nbt.putByte("Variant", this.variant);
//        nbt.putBoolean("IsFossile", this.isFossile);
//
//        return nbt;
//    }




    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {

        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithoutMetadata);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();

        this.load(data);
        this.checkAndLoadEntity();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public @NotNull AABB getRenderBoundingBox() {
        if (this.isSkeleton) {
            // ensure the display entity exists so the bounding box is correct
            if (this.entity == null) {
                this.checkAndLoadEntity();
            }

            if (this.entity != null) {
                return this.entity.getBoundingBoxForCulling()
                        .move(Vec3.atLowerCornerOf(this.worldPosition))
                        .inflate(3.0);
            }
        }

        return super.getRenderBoundingBox();
    }
    public boolean isMale() {
        return this.isMale;
    }

    public boolean isSkeleton() {
        return this.isSkeleton;
    }

    public boolean isFossile() {
        return this.isFossile;
    }

    public void setVariant(byte variant) {
        this.variant = variant;
        if (this.entity != null) {
            this.applyVariantToEntity(this.entity);
            this.markUpdated();
        } else {
            this.setChanged();
        }
    }

    public byte getVariant() {
        return this.variant;
    }

    public int getRot() {
        return this.rotation;
    }

    protected void setRotationSilently(int rotation) {
        this.rotation = Math.floorMod(rotation, 360);
    }

    public void setRot(int rotation) {
        this.setRotationSilently(rotation);
        this.setChanged();
        if (this.level != null && !this.level.isClientSide && !this.suppressUpdates) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public DinosaurEntity getEntity() {
        if(this.entity == null){
            this.entity = this.createEntity();
        }
        return this.entity;
    }

    private DinosaurEntity createEntity() {
//        DinosaurEntity entity = this.serializedData.create(this.level);
                this.checkAndLoadEntity();               // may have built one already
                if (this.entity == null && this.entityType != null) {               // only build if still missing
                        this.entity = this.entityType.create(this.level);
                        if (this.entity != null) {
                                this.initializeEntity(this.entity);
                            }
                    }
                return this.entity;
    }

    private void initializeEntity(DinosaurEntity entity) {
        entity.setSkeleton(this.isSkeleton);
        entity.setupDisplay(this.isMale);
        this.applyVariantToEntity(entity);
        entity.setIsFossile(this.isFossile);
        entity.setAnimation(EntityAnimation.IDLE.get());
        entity.setNoAi(true);
    }

    private void initializeEntity(DinosaurEntity entity, CompoundTag entityTag) {
        entity.load(entityTag);
        entity.setSkeleton(this.isSkeleton);
        entity.setupDisplay(this.isMale);
        this.applyVariantToEntity(entity);
        entity.setIsFossile(this.isFossile);
        entity.setAnimation(EntityAnimation.IDLE.get());
        entity.setNoAi(true);

    }

    private void applyVariantToEntity(DinosaurEntity entity) {
        if (entity == null) {
            return;
        }

        if (this.isSkeleton) {
            if (this.variant != NO_VARIANT) {
                entity.setSkeletonVariant(this.variant);
            }
            this.variant = entity.getSkeletonVariant();
        } else if (entity instanceof IHasVariants variantHolder) {
            if (this.variant != NO_VARIANT) {
                variantHolder.setVariant(Byte.toUnsignedInt(this.variant));
            }
            this.variant = (byte) variantHolder.getVariant();
        }
    }

    private void syncVariantFromEntity() {
        if (this.entity == null) {
            return;
        }

        if (this.isSkeleton) {
            this.variant = this.entity.getSkeletonVariant();
        } else if (this.entity instanceof IHasVariants variantHolder) {
            this.variant = (byte) variantHolder.getVariant();
        }
    }

    private void markUpdated() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide && !this.suppressUpdates) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
//    private abstract class SerializedData {
//        public SerializedData withInitialData(EntityType<?> type){
//            return this;
//        }
//        abstract void serialize(CompoundTag compound);
//
//        abstract void deserialize(CompoundTag compound);
//
//        abstract DinosaurEntity create(Level world);
//    }
//
//    private class LegacyId extends SerializedData {
//        protected EntityType<?> dinosaurId;
//
//        public LegacyId withInitialData(EntityType<?> type) {
//            this.dinosaurId = type;
//            return this;
//        }
//
//
//        @Override
//        public void serialize(CompoundTag compound) {
//            if(this.dinosaurId == null)
//                this.dinosaurId = DinosaurEntity.CLASS_TYPE_LIST.get(DinosaurHandler.VELOCIRAPTOR.getDinosaurClass()).get();
//            compound.putString("DinosaurId", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.dinosaurId)).toString());
//        }
//
//        @Override
//        public void deserialize(CompoundTag compound) {
//            this.dinosaurId = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(compound.getString("DinosaurId")));
//        }
//
//        @Override
//        public DinosaurEntity create(Level world) {
//            if(this.dinosaurId == null)
//                this.dinosaurId = ModEntities.VELOCIRAPTOR_ENTITY_TYPE.get();
//            Entity dino = dinosaurId.create(world);
//            if(dino instanceof DinosaurEntity d)
//                return d;
//            return null;
//        }
//    }
//
//    private class TagData extends SerializedData {
//        protected CompoundTag data;
//
//        TagData(){}
//
//        public TagData(EntityType<?> type) {
//            CompoundTag data = new CompoundTag();
//            data.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(type)).toString());
//
//            this.data = data;
//
//        }
//
//        public TagData withInitialData(EntityType<?> type) {
//            CompoundTag data = new CompoundTag();
//            data.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(type)).toString());
//
//            this.data = data;
//            return this;
//        }
//
//        @Override
//        public void serialize(CompoundTag compound) {
//            compound.put("DinosaurIdTag", this.data);
//        }
//
//        @Override
//        public void deserialize(CompoundTag compound) {
//            this.data = compound.getCompound("DinosaurIdTag");
//        }
//
//        @Override
//        public DinosaurEntity create(Level world) {
//            Entity entity = EntityType.by(this.data).orElse(EntityType.AXOLOTL/*...*/).create(world);
//            if (entity instanceof DinosaurEntity) {
//                return (DinosaurEntity) entity;
//            }
//            return null;
//        }
//    }
//
//    private class InvalidData extends LegacyId {
//        public SerializedData withInitialData() {
//            return new LegacyId().withInitialData(ModEntities.VELOCIRAPTOR_ENTITY_TYPE.get());
//        }
//
//        @Override
//        public void deserialize(CompoundTag compound) {
//            this.dinosaurId = ModEntities.VELOCIRAPTOR_ENTITY_TYPE.get();
//        }
//    }
//
//    public void setEntity(DinosaurEntity entity) {
//        this.entity = entity;
//    }


    public void tick(Level world, BlockPos pPos, BlockState pState, ActionFigureBlockEntity pBlockEntity) {

    }

    public DinosaurEntity peekEntity(){
        return this.entity;
    }
}
