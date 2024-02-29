package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.container.DNAExtractorContainer;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.genetics.DinoDNA;
import mod.reborn.server.genetics.GeneticsHelper;
import mod.reborn.server.genetics.PlantDNA;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.message.TileEntityFieldsMessage;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import java.util.List;
import java.util.Random;

public class DNAExtractorBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[]{0, 1};
    private static final int[] OUTPUTS = new int[]{2, 3, 4, 5};

    private NonNullList<ItemStack> slots = NonNullList.withSize(6, ItemStack.EMPTY);
    
    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        ItemStack extraction = this.slots.get(0);
        ItemStack storage = this.slots.get(1);
        if (storage.getItem() == ItemHandler.STORAGE_DISC && (extraction.getItem() == ItemHandler.AMBER || extraction.getItem() == ItemHandler.SEA_LAMPREY || extraction.getItem() == ItemHandler.FROZEN_LEECH || extraction.getItem() == ItemHandler.DINOSAUR_MEAT) && (storage.getTagCompound() == null || !storage.getTagCompound().hasKey("Genetics"))) {
            for (int i = 2; i < 6; i++) {
                if (this.slots.get(i).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return Ints.asList(OUTPUTS).contains(slotID);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {

		if ((slotID == 0 && itemstack != null && itemstack.getItem() == ItemHandler.AMBER || itemstack.getItem() == ItemHandler.SEA_LAMPREY || itemstack.getItem() == ItemHandler.FROZEN_LEECH || itemstack.getItem() == ItemHandler.DINOSAUR_MEAT)
				|| slotID == 1 && itemstack != null && itemstack.getItem() == ItemHandler.STORAGE_DISC && (itemstack.getTagCompound() == null || !itemstack.getTagCompound().hasKey("DNAQuality"))) {

			return true;
		}

		return false;
	}

    @Override
    protected void processItem(int process) {
        if (this.canProcess(process)) {
            Random rand = this.world.rand;
            ItemStack input = this.slots.get(0);

            ItemStack disc = ItemStack.EMPTY;

            Item item = input.getItem();

            if (item == ItemHandler.AMBER || item == ItemHandler.SEA_LAMPREY) {
                if (input.getItemDamage() == 0) {
                    List<Dinosaur> possibleDinos = item == ItemHandler.AMBER ? EntityHandler.getDinosaursFromAmber() : EntityHandler.getMarineCreatures();

                    Dinosaur dino = possibleDinos.get(rand.nextInt(possibleDinos.size()));

                    disc = new ItemStack(ItemHandler.STORAGE_DISC);

                    int quality = 50 + (rand.nextInt(50));

                    DinoDNA dna = new DinoDNA(dino, quality, GeneticsHelper.randomGenetics(rand));

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                } else if (input.getItemDamage() == 1) {
                    List<Plant> possiblePlants = PlantHandler.getPrehistoricPlantsAndTrees();
                    Plant plant = possiblePlants.get(rand.nextInt(possiblePlants.size()));

                    int plantId = PlantHandler.getPlantId(plant);

                    disc = new ItemStack(ItemHandler.STORAGE_DISC);

                    int quality = 50 + (rand.nextInt(50));

                    PlantDNA dna = new PlantDNA(plantId, quality);

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                }
            }
            if (item == ItemHandler.AMBER || item == ItemHandler.FROZEN_LEECH) {
                if (input.getItemDamage() == 0) {
                    List<Dinosaur> possibleDinos = item == ItemHandler.AMBER ? EntityHandler.getDinosaursFromAmber() : EntityHandler.getMammalCreatures();

                    Dinosaur dino = possibleDinos.get(rand.nextInt(possibleDinos.size()));

                    disc = new ItemStack(ItemHandler.STORAGE_DISC);

                    int quality = 50 + (rand.nextInt(50));

                    DinoDNA dna = new DinoDNA(dino, quality, GeneticsHelper.randomGenetics(rand));

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                }
            }else if (item == ItemHandler.DINOSAUR_MEAT) {
    			Dinosaur dino = EntityHandler.getDinosaurById(input.getMetadata());
                disc = new ItemStack(ItemHandler.STORAGE_DISC);
                DinoDNA dna = new DinoDNA(dino, 100, GeneticsHelper.randomGenetics(rand));

                NBTTagCompound nbt = new NBTTagCompound();
                dna.writeToNBT(nbt);

                disc.setTagCompound(nbt);
            }

            int empty = this.getOutputSlot(disc);

            this.mergeStack(empty, disc);

            this.decreaseStackSize(0);
            this.decreaseStackSize(1);
            BlockPos pos = this.pos;
            RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 5));
        }
    }

    @Override
    protected int getMainOutput(int process) {
        return 2;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 2000;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return this.getInputs();
    }

    @Override
    protected int[] getOutputs() {
        return OUTPUTS;
    }

    @Override
    protected NonNullList<ItemStack> getSlots() {
        return this.slots;
    }

    @Override
    protected void setSlots(NonNullList<ItemStack> slots) {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    	return new DNAExtractorContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return RebornMod.MODID + ":dna_extractor";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dna_extractor";
    }
    
	@Override
	public void packetDataHandler(ByteBuf fields) {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			this.setInventorySlotContents(0, ByteBufUtils.readItemStack(fields));
		}
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		boolean send = false;
		if (!this.world.isRemote && index == 0 && this.slots.get(0).getItem() != stack.getItem()) {
			send = true;
		}
		super.setInventorySlotContents(index, stack);
		if (send)
			RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5));
	}
	
	@Override
	public NonNullList getSyncFields(NonNullList fields) {
		fields.add(this.slots.get(0));
		return fields;
	}

    @Override
    public boolean isEmpty() {
        return false;
    }
}
