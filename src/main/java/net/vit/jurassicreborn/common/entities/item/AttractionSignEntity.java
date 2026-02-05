package net.vit.jurassicreborn.common.entities.item;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.slf4j.Logger;
import org.jetbrains.annotations.Nullable;
import java.util.Locale;
import java.util.function.Predicate;

import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.util.Mth;

public class AttractionSignEntity extends HangingEntity implements IEntityAdditionalSpawnData {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Predicate<Entity> IS_OTHER_SIGN = e -> e instanceof AttractionSignEntity;
    private AttractionSignType type;

    public AttractionSignEntity(EntityType<? extends AttractionSignEntity> entityType, Level world) {
        super(entityType, world);
        this.type = AttractionSignType.AQUARIUM;
        this.noCulling = true; // ensure sign renders even when off screen
    }

    // Proper HangingEntity constructor: sets blockPos and facing
    public AttractionSignEntity(Level world, BlockPos pos, Direction facing, AttractionSignType type) {
        super(ModEntities.ATTRACTION_SIGN.get(), world, pos);
        this.type = type;
        this.setDirection(facing);
        this.noCulling = true; // prevent frustum culling so sign stays visible
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SignType", type.ordinal());
        tag.putByte("Facing", (byte) this.direction.get2DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        int idx = Mth.clamp(tag.getInt("SignType"), 0, AttractionSignType.values().length - 1);
        this.type = AttractionSignType.values()[idx];
        if (tag.contains("Facing")) {
            setDirection(Direction.from2DDataValue(tag.getByte("Facing")));
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeLong(this.getPos().asLong());
        buf.writeByte(this.direction.get2DDataValue());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        int idx = Mth.clamp(buf.readInt(), 0, AttractionSignType.values().length - 1);
        this.type = AttractionSignType.values()[idx];
        BlockPos p = BlockPos.of(buf.readLong());
        this.pos = p; // restore hanging block position before facing
        this.setDirection(Direction.from2DDataValue(buf.readUnsignedByte()));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override public int getWidth()  { return 16; }
    @Override public int getHeight() { return 16; }

    @Override
    public void dropItem(@Nullable Entity broke) {
        if (!level.isClientSide()
                && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)
                && !(broke instanceof Player p && p.getAbilities().instabuild)) {
            ItemStack stack = new ItemStack(ModItems.ATTRACTION_SIGNS.get(this.type).get());
            spawnAtLocation(stack, 0f);
        }
    }

    @Override public void playPlacementSound() {}
    @Override public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.ATTRACTION_SIGNS.get(this.type).get());
    }

    @Override
    public boolean survives() {
        if (!level.noCollision(this)) return false;
        return level.getEntitiesOfClass(
                AttractionSignEntity.class,
                getBoundingBox(),
                e -> e != this
        ).isEmpty();
    }

    @Override protected void recalculateBoundingBox() {
        super.recalculateBoundingBox();
    }

    public ResourceLocation getFaceTexture()   { return type.textureFace; }
    public ResourceLocation getPopoutTexture(){ return type.texturePopout; }


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
        public final ResourceLocation textureFace, texturePopout;

        AttractionSignType() { this(128,128); }
        AttractionSignType(int sx,int sy) {
            this.sizeX = sx; this.sizeY = sy;
            String base = name().toLowerCase(Locale.ROOT);
            this.textureFace   = new ResourceLocation(JurassicReborn.MODID,
                    "textures/attraction_sign/" + base + ".png");
            this.texturePopout = new ResourceLocation(JurassicReborn.MODID,
                    "textures/attraction_sign/" + base + "_popout.png");
        }

    }
}
