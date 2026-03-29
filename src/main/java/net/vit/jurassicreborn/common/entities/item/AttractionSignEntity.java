package net.vit.jurassicreborn.common.entities.item;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.function.Predicate;

public class AttractionSignEntity extends HangingEntity {
    private static final Predicate<Entity> IS_OTHER_SIGN = e -> e instanceof AttractionSignEntity;
    private static final EntityDataAccessor<Integer> SIGN_TYPE =
            SynchedEntityData.defineId(AttractionSignEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> SIGN_FACING =
            SynchedEntityData.defineId(AttractionSignEntity.class, EntityDataSerializers.BYTE);

    private static final int SIGN_WIDTH  = 16;
    private static final int SIGN_HEIGHT = 16;
    private static final Logger LOGGER = LogUtils.getLogger();
    private AttractionSignType type;

    public AttractionSignEntity(EntityType<? extends AttractionSignEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.type = AttractionSignType.AQUARIUM;
    }

    public AttractionSignEntity(Level level, BlockPos pos, Direction facing, AttractionSignType type) {
        super(ModEntities.ATTRACTION_SIGN.get(), level, pos);
        this.noCulling = true;
        this.type = type;
        this.setDirection(facing);
        this.entityData.set(SIGN_TYPE, type.ordinal());
        this.entityData.set(SIGN_FACING, (byte) facing.get2DDataValue());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIGN_TYPE, AttractionSignType.AQUARIUM.ordinal());
        builder.define(SIGN_FACING, (byte) 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIGN_TYPE.equals(key)) {
            int idx = Mth.clamp(this.entityData.get(SIGN_TYPE), 0, AttractionSignType.values().length - 1);
            this.type = AttractionSignType.values()[idx];
        }

        if (SIGN_FACING.equals(key)) {
            Direction newDir = Direction.from2DDataValue(this.entityData.get(SIGN_FACING));
            if (newDir != this.direction) {
                this.setDirection(newDir);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SignType", this.type.ordinal());
        tag.putByte("Facing", (byte) this.direction.get2DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        int idx = Mth.clamp(tag.getInt("SignType"), 0, AttractionSignType.values().length - 1);
        this.type = AttractionSignType.values()[idx];
        this.entityData.set(SIGN_TYPE, idx);
        if (tag.contains("Facing")) {
            Direction facing = Direction.from2DDataValue(tag.getByte("Facing"));
            this.setDirection(facing);
            this.entityData.set(SIGN_FACING, (byte) facing.get2DDataValue());
        }
    }


    @Override
    protected AABB calculateBoundingBox(BlockPos pos, Direction dir) {
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.5D;
        double cz = pos.getZ() + 0.5D;


        double inset = 0.43D;

        cx -= dir.getStepX() * inset;
        cz -= dir.getStepZ() * inset;

        double halfWidth  = SIGN_WIDTH  / 32.0D;
        double halfHeight = SIGN_HEIGHT / 32.0D;
        double halfDepth  = 0.0625D;

        if (dir.getAxis() == Direction.Axis.Z) {
            return new AABB(
                    cx - halfWidth,  cy - halfHeight, cz - halfDepth,
                    cx + halfWidth,  cy + halfHeight, cz + halfDepth
            );
        }
        else {
            return new AABB(
                    cx - halfDepth,  cy - halfHeight, cz - halfWidth,
                    cx + halfDepth,  cy + halfHeight, cz + halfWidth
            );
        }
    }

    @Override
    public boolean survives() {
        if (!level().noCollision(this)) return false;

        return level().getEntitiesOfClass(
                AttractionSignEntity.class,
                getBoundingBox(),
                e -> e != this
        ).isEmpty();
    }

    @Override
    public void playPlacementSound() {}

    @Override
    public void dropItem(@Nullable Entity breaker) {
        if (!level().isClientSide()
                && level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)
                && !(breaker instanceof Player p && p.getAbilities().instabuild)) {

            spawnAtLocation(
                    new ItemStack(ModItems.ATTRACTION_SIGNS.get(this.type).get()),
                    0.0F
            );
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult hit) {
        return new ItemStack(ModItems.ATTRACTION_SIGNS.get(this.type).get());
    }

    /* ------------------------------------------------------------ */
    /*  Renderer hooks                                               */
    /* ------------------------------------------------------------ */

    public int getWidth()  { return this.type.sizeX; }
    public int getHeight() { return this.type.sizeY; }

    public ResourceLocation getFaceTexture() {
        return this.type.textureFace;
    }

    public ResourceLocation getPopoutTexture() {
        return this.type.texturePopout;
    }

    /* ------------------------------------------------------------ */
    /*  Enum                                                         */
    /* ------------------------------------------------------------ */

    public enum AttractionSignType {

        AQUARIUM, AQUARIUM_CORAL, AVIARY, AVIARY_PLANTS,
        GALLIMIMUS_VALLEY, GALLIMIMUS_VALLEY_PLANTS,
        GENTLE_GIANTS, GENTLE_GIANTS_PLANTS,
        RAPTOR_PADDOCK, RAPTOR_PADDOCK_PLANTS,
        SAFARI, SAFARI_PLANTS,
        TRICERATOPS_TERRITORIUM, TRICERATOPS_TERRITORIUM_PLANTS,
        GARDEN, GARDEN_PLANTS,
        TYRANNOSAURUS_KINGDOM, TYRANNOSAURUS_KINGDOM_PLANTS,
        CENOZOIC_PARK, CENOZOIC_PARK_PLANTS,
        SMILODON_COVE, SMILODON_COVE_PLANTS,
        CARNIVORE_PADDOCK, CARNIVORE_PADDOCK_PLANTS,
        HERBIVORE_PADDOCK, HERBIVORE_PADDOCK_PLANTS,
        HYAENODON_LAIR, HYAENODON_LAIR_PLANTS,
        HYBRID_PADDOCK, HYBRID_PADDOCK_PLANTS,
        HYBRID_HILLS, HYBRID_HILLS_PLANTS,
        MAMMOTH_PLAINS, MAMMOTH_PLAINS_PLANTS,
        PREHISTORIC_LAKE, PREHISTORIC_LAKE_PLANTS,
        MOSA_FEEDING, TYLO_FEEDING,
        VISITOR_CENTER, VISITOR_CENTER_PLANTS,
        SPINO_SWAMP, SPINO_SWAMP_PLANTS,
        SAUROPOD_VALLEY, SAUROPOD_VALLEY_PLANTS,
        LOGO, LOGO_PLANTS,
        GIFT_SHOP, GIFT_SHOP_PLANTS,
        RESTAURANT, RESTAURANT_PLANTS,
        LABORATORY, LABORATORY_PLANTS,
        COELACANTH_POND, COELACANTH_POND_CORAL,
        PREHISTORIC_RIVER, PREHISTORIC_RIVER_PLANTS,
        GYROSPHERE_STATION, GYROSPHERE_STATION_PLANTS,
        MONORAIL_STATION, MONORAIL_STATION_PLANTS,
        MUSEUM, MUSEUM_PLANTS,
        SAFARI_TOUR, SAFARI_TOUR_PLANTS,
        INDORAPTOR, INDORAPTOR_VARIANT,
        INDOMINUS_REX, INDOMINUS_REX_VARIANT,
        GARDEN_ALT, GARDEN_ALT_PLANTS;

        public final int sizeX, sizeY;
        public final ResourceLocation textureFace;
        public final ResourceLocation texturePopout;

        AttractionSignType() {
            this(128, 128);
        }

        AttractionSignType(int sx, int sy) {
            this.sizeX = sx;
            this.sizeY = sy;

            String base = name().toLowerCase(Locale.ROOT);

            this.textureFace = ResourceLocation.fromNamespaceAndPath(
                    JurassicReborn.MODID,
                    "textures/attraction_sign/" + base + ".png"
            );
            this.texturePopout = ResourceLocation.fromNamespaceAndPath(
                    JurassicReborn.MODID,
                    "textures/attraction_sign/" + base + "_popout.png"
            );
        }
    }
}
