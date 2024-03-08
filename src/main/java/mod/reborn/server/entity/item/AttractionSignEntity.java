package mod.reborn.server.entity.item;

import com.google.common.base.Predicate;
import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class AttractionSignEntity extends EntityHanging implements IEntityAdditionalSpawnData {
    private static final Predicate<Entity> IS_ATTRACTION_SIGN = entity -> entity instanceof AttractionSignEntity;

    public AttractionSignType type;

    public AttractionSignEntity(World world) {
        super(world);
    }

    public AttractionSignEntity(World world, BlockPos pos, EnumFacing side, AttractionSignType type) {
        super(world, pos);
        this.type = type;
        this.updateFacingWithBoundingBox(side);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("Type", (byte) this.type.ordinal());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.type = AttractionSignType.values()[compound.getByte("Type")];

        super.readEntityFromNBT(compound);
    }

    @Override
    public int getWidthPixels() {
        return this.type.sizeX;
    }

    @Override
    public int getHeightPixels() {
        return this.type.sizeY / 2;
    }

    @Override
    public void onBroken(Entity entity) {
        if (this.world.getGameRules().getBoolean("doTileDrops")) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;

                if (player.capabilities.isCreativeMode) {
                    return;
                }
            }

            this.entityDropItem(new ItemStack(ItemHandler.ATTRACTION_SIGN, 1, this.type.ordinal()), 0.0F);
        }
    }

    @Override
    public void playPlaceSound() {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        BlockPos positionOffset = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
        BlockPos newPosition = this.hangingPosition.add(positionOffset);
        this.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeByte(this.type.ordinal());
        buffer.writeLong(this.hangingPosition.toLong());
        buffer.writeByte(this.facingDirection.getHorizontalIndex());
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        this.type = AttractionSignType.values()[buf.readByte()];
        this.hangingPosition = BlockPos.fromLong(buf.readLong());
        this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(buf.readByte()));
    }

    @Override
    public boolean onValidSurface() {
        if (!this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }

        int width = this.getWidthPixels() / 16;
        int height = this.getHeightPixels() / 16;

        EnumFacing facing = this.facingDirection.rotateYCCW();

        BlockPos pos = this.hangingPosition.offset(this.facingDirection.getOpposite()).offset(facing, -(width / 2) + 1);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BlockPos partPos = pos.offset(facing, x).down(y);
                IBlockState state = this.world.getBlockState(partPos);

                if (!(state.isSideSolid(this.world, partPos, this.facingDirection) && state.getMaterial().isSolid())) {
                    return false;
                }
            }
        }

        return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_ATTRACTION_SIGN).isEmpty();
    }

    @Override
    protected void updateBoundingBox() {
        if (this.facingDirection != null) {
            double x = this.hangingPosition.getX() + 0.5D;
            double y = this.hangingPosition.getY() + 0.5D;
            double z = this.hangingPosition.getZ() + 0.5D;
            double offsetXZ = this.getWidthPixels() % 32 == 0 ? 0.5D : 0.0D;
            double offsetY = this.getHeightPixels() % 32 == 0 ? 0.5D : 0.0D;
            x -= this.facingDirection.getFrontOffsetX() * 0.46875D;
            z -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
            y += offsetY;
            EnumFacing facing = this.facingDirection.rotateYCCW();
            x += offsetXZ * facing.getFrontOffsetX();
            z += offsetXZ * facing.getFrontOffsetZ();
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            double sizeX = this.getWidthPixels();
            double sizeY = this.getHeightPixels();
            double sizeZ = this.getWidthPixels();

            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
                sizeZ = 1.0D;
            } else {
                sizeX = 1.0D;
            }

            sizeX /= 32.0D;
            sizeY /= 16.0D;
            sizeZ /= 32.0D;

            this.setEntityBoundingBox(new AxisAlignedBB(x - sizeX, y, z - sizeZ, x + sizeX, y - sizeY, z + sizeZ));
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(ItemHandler.ATTRACTION_SIGN, 1, this.type.ordinal());
    }

    public enum AttractionSignType {
        AQUARIUM,
        AQUARIUM_CORAL,
        AVIARY,
        AVIARY_PLANTS,
        GALLIMIMUS_VALLEY,
        GALLIMIMUS_VALLEY_PLANTS,
        GENTLE_GIANTS,
        GENTLE_GIANTS_PLANTS,
        RAPTOR_PADDOCK,
        RAPTOR_PADDOCK_PLANTS,
        SAFARI,
        SAFARI_PLANTS,
        TRICERATOPS_TERRITORIUM,
        GARDEN,
        GARDEN_PLANTS,
        TRICERATOPS_TERRITORIUM_PLANTS,
        TYRANNOSAURUS_KINGDOM,
        TYRANNOSAURUS_KINGDOM_PLANTS,
        CENOZOIC_PARK,
        CENOZOIC_PARK_PLANTS,
        SMILODON_COVE,
        SMILODON_COVE_PLANTS,
        CARNIVORE_PADDOCK,
        CARNIVORE_PADDOCK_PLANTS,
        HERBIVORE_PADDOCK,
        HERBIVORE_PADDOCK_PLANTS,
        HYAENODON_LAIR,
        HYAENODON_LAIR_PLANTS,
        HYBRID_PADDOCK,
        HYBRID_PADDOCK_PLANTS,
        HYBRID_HILLS,
        HYBRID_HILLS_PLANTS,
        MAMMOTH_PLAINS,
        MAMMOTH_PLAINS_PLANTS,
        PREHISTORIC_LAKE,
        PREHISTORIC_LAKE_PLANTS,
        MOSA_FEEDING,
        TYLO_FEEDING,
        VISITOR_CENTER,
        VISITOR_CENTER_PLANTS,
        SPINO_SWAMP,
        SPINO_SWAMP_PLANTS,
        SAUROPOD_VALLEY,
        SAUROPOD_VALLEY_PLANTS,
        LOGO,
        LOGO_PLANTS,
        GIFT_SHOP,
        GIFT_SHOP_PLANTS,
        RESTAURANT,
        RESTAURANT_PLANTS;

        public final int sizeX;
        public final int sizeY;
        public final ResourceLocation texture;
        public final ResourceLocation texturePopout;

        AttractionSignType() {
            this(128, 128);
        }

        AttractionSignType(int xSize, int ySize) {
            this.sizeX = xSize;
            this.sizeY = ySize;
            this.texture = new ResourceLocation(RebornMod.MODID, "textures/attraction_sign/" + this.name().toLowerCase(Locale.ENGLISH) + ".png");
            this.texturePopout = new ResourceLocation(RebornMod.MODID, "textures/attraction_sign/" + this.name().toLowerCase(Locale.ENGLISH) + "_popout.png");
        }
    }
}