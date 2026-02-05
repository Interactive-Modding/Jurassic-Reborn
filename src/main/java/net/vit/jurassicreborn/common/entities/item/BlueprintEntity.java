package net.vit.jurassicreborn.common.entities.item;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.items.ModItems;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * Hanging blueprint entity (Forge 1.18.2).
 * - Extends HangingEntity (painting-like).
 * - Uses IEntityAdditionalSpawnData for extra spawn bytes (type/pos/facing).
 * - Surface check via isFaceSturdy(..., SupportType.FULL).
 */
public class BlueprintEntity extends WallHangingEntity implements IEntityAdditionalSpawnData {

    public Type type = Type.TYRANNOSAURUS; // sensible default

    // Vanilla/Forge required ctor
    public BlueprintEntity(net.minecraft.world.entity.EntityType<? extends BlueprintEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    // Placement ctor that AUTOPICKS a type that fits the surface
    public BlueprintEntity(net.minecraft.world.entity.EntityType<? extends BlueprintEntity> et,
                           Level level, BlockPos pos, Direction side) {
        super(et, level, pos);
        List<Type> fits = Lists.newArrayList();
        for (Type t : Type.values()) {
            this.type = t;
            this.setDirection(side);      // sets facing and recalculates AABB
            if (this.survives()) {
                fits.add(t);
            }
        }
        if (!fits.isEmpty()) {
            this.type = fits.get(this.random.nextInt(fits.size()));
        }
        this.setDirection(side);
    }

    // Placement ctor that USES a PROVIDED type
    public BlueprintEntity(net.minecraft.world.entity.EntityType<? extends BlueprintEntity> et,
                           Level level, BlockPos pos, Direction side, Type givenType) {
        super(et, level, pos);
        this.type = givenType;
        this.setDirection(side);
        this.noCulling = true;
    }

    // ---------- Dimensions (pixels like Painting) ----------

    @Override public int getWidth()  { return 16; }
    @Override public int getHeight() { return 16; }

    // ---------- Placement validity ----------

    // ---------- Breaking & drops ----------

    @Override
    public void dropItem(@Nullable Entity breaker) {
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) return;
        if (breaker instanceof Player player && player.getAbilities().instabuild) return;
        this.spawnAtLocation(new ItemStack(ModItems.BLUEPRINT.get()));
    }

    @Override
    public void playPlacementSound() {
        // intentionally blank (match legacy behavior)
    }

    // ---------- Networking ----------

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buf) {
        buf.writeByte(this.type.ordinal());
        buf.writeBlockPos(this.pos);
        buf.writeByte(this.direction.get2DDataValue());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        this.type = Type.values()[buf.readByte()];
        this.pos = buf.readBlockPos();
        this.setDirection(Direction.from2DDataValue(buf.readByte()));
    }

    // ---------- NBT ----------

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("Type", (byte) this.type.ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Type")) {
            int idx = Mth.clamp(tag.getByte("Type"), 0, Type.values().length - 1);
            this.type = Type.values()[idx];
        }
        super.readAdditionalSaveData(tag);
    }

    // ---------- Pick block (creative middle-click) ----------

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.BLUEPRINT.get());
    }

    // ---------- Utility ----------

    // ---------- Types ----------

    public enum Type {
        TYRANNOSAURUS(1920,1200),
        ANKYLODOCUS(1074,671),
        ANKYLOSAURUS(1074,671),
        APATOSAURUS(1074,671),
        ARSINOITHERIUM(1206,676),
        BARYONYX(1082,676),
        BRACHIOSAURUS(1082,676),
        CAMARASAURUS(1074,671),
        CARCHARODONTOSAURUS(1206,676),
        CARNOTAURUS(1082,676),
        CERATOSAURUS(1074,671),
        CHASMOSAURUS(1074,671),
        COMPSOGNATHUS(1205,676),
        DEINOTHERIUM(1206,676),
        DIMETRODON(1074,671),
        DIPLODOCUS(1074,671),
        DREADNOUGHTUS(1074,671),
        ELASMOTHERIUM(1196,676),
        HERRERASAURUS(1920,1200),
        INDORAPTOR(1074,671),
        LEPTICTIDIUM(1201,676),
        MAMENCHISAURUS(1080,607),
        MEGATHERIUM(1074,671),
        MOSASAURUS(1071,676),
        OVIRAPTOR(1082,676),
        PARASAUROLOPHUS(1395,783),
        QUETZALCOATLUS(1074,671),
        RAPHUSREX(1074,671),
        RUGOPS(1074,671),
        SINOCERATOPS(1074,671),
        SMILODON(1074,671),
        SPINORAPTOR(1074,671),
        SPINOSAURUS(1202,676),
        STYRACOSAURUS(1074,671),
        TITANIS(1074,671),
        PERISPHINCTES(1074,671),
        MICROCERATUS(1074,671),
        PROTOCERATOPS(1074,671),
        TITANITES(1074,671),
        PARAPUZOSIA(1074,671),
        ASTEROCERAS(1074,671),
        VECTIPELTA(1074,671),
        TRICERATOPS(1074,671),
        TROODON(1082,676),
        THERIZINOSAUURS(1082,676),
        STEGOSAURUS(1082,676),
        PATAGOTITAN(1082,676),
        ORTHOCERAS(1082,676),
        NIGERSAURUS(1082,676),
        MEGALODON(1082,676),
        MAJUNGASAURUS(1082,676),
        MAIASAURA(1082,676),
        LIVYATAN(1082,676),
        LAMBEOSAURUS(1082,676),
        KAIRUKU(1082,676),
        GIGANOTOSAURUS(1082,676),
        ENDOCERAS(1082,676),
        EDMONTOSAURUS(1082,676),
        DUNKLEOSTEUS(1082,676),
        DILOPHOSAURUS(1082,676),
        DEINOSUCHUS(1082,676),
        CORYTHOSAURUS(1082,676),
        CAMEROCERAS(1082,676),
        CALYMENE(1082,676),
        PARACERATHERIUM(1074,671);

        public final int sizeX;
        public final int sizeY;
        public final ResourceLocation texture;

        Type(int xSize, int ySize) {
            this.sizeX = 96;
            this.sizeY = 64;
            this.texture = new ResourceLocation(
                    JurassicReborn.MODID,
                    "textures/painting/" + name().toLowerCase(Locale.ENGLISH) + ".png"
            );
        }
    }
}
