package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import mod.reborn.RebornMod;
import mod.reborn.server.api.Hybrid;
import mod.reborn.server.container.DNACombinatorHybridizerContainer;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.genetics.DinoDNA;
import mod.reborn.server.genetics.GeneticsHelper;
import mod.reborn.server.genetics.PlantDNA;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class DNACombinatorHybridizerBlockEntity extends MachineBaseBlockEntity {
    private static final int[] HYBRIDIZER_INPUTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    private static final int[] COMBINATOR_INPUTS = new int[] { 8, 9 };
    private static final int[] HYBRIDIZER_OUTPUTS = new int[] { 10 };
    private static final int[] COMBINATOR_OUTPUTS = new int[] { 11 };

    private NonNullList<ItemStack> slots = NonNullList.withSize(12, ItemStack.EMPTY);

    private boolean hybridizerMode;

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    private Dinosaur getHybrid() {
        return this.getHybrid(this.slots.get(0), this.slots.get(1), this.slots.get(2), this.slots.get(3), this.slots.get(4), this.slots.get(5), this.slots.get(6), this.slots.get(7));
    }

    private Dinosaur getHybrid(ItemStack... discs) {
        Dinosaur hybrid = null;

        Dinosaur[] dinosaurs = new Dinosaur[discs.length];

        for (int i = 0; i < dinosaurs.length; i++) {
            dinosaurs[i] = this.getDino(discs[i]);
        }

        for (Dinosaur dino : EntityHandler.getRegisteredDinosaurs()) {
            if (dino instanceof Hybrid) {
                Hybrid dinoHybrid = (Hybrid) dino;

                int count = 0;
                boolean extra = false;

                List<Class<? extends Dinosaur>> usedGenes = new ArrayList<>();

                for (Dinosaur discDinosaur : dinosaurs) {
                    Class match = null;

                    for (Class clazz : dinoHybrid.getDinosaurs()) {
                        if (clazz.isInstance(discDinosaur) && !usedGenes.contains(clazz)) {
                            match = clazz;
                        }
                    }

                    if (match != null && match.isInstance(discDinosaur)) {
                        usedGenes.add(match);
                        count++;
                    } else if (discDinosaur != null) {
                        extra = true;
                    }
                }

                if (!extra && count == dinoHybrid.getDinosaurs().length) {
                    hybrid = dino;

                    break;
                }
            }
        }
        return hybrid;
    }

    private Dinosaur getDino(ItemStack disc) {
        if (!disc.isEmpty() && disc.hasTagCompound()) {
            DinoDNA data = DinoDNA.readFromNBT(disc.getTagCompound());

            return data.getDNAQuality() == 100 ? data.getDinosaur() : null;
        } else {
            return null;
        }
    }

    @Override
    protected boolean canProcess(int process) {
        if (this.hybridizerMode) {
            return this.slots.get(10).isEmpty() && this.getHybrid() != null;
        } else {
        	final ItemStack left = this.slots.get(8);
        	final ItemStack right = this.slots.get(9);
            if (!left.isEmpty() && left.getItem() == ItemHandler.STORAGE_DISC && !right.isEmpty() && right.getItem() == ItemHandler.STORAGE_DISC) {
            	final String leftID = left.getTagCompound().getString("StorageId");
            	final String rightID = right.getTagCompound().getString("StorageId");
                if (left.getTagCompound() != null && right.getTagCompound() != null && this.slots.get(11).isEmpty() && left.getItemDamage() == right.getItemDamage() && leftID.equals(rightID)) {
                    if(leftID.equals("DinoDNA")) {
                    	DinoDNA dna1 = DinoDNA.readFromNBT(left.getTagCompound());
                        DinoDNA dna2 = DinoDNA.readFromNBT(right.getTagCompound());
                        if(dna1.getDinosaur() == dna2.getDinosaur())
                        	return true;
                        
                    }else if(leftID.equals("PlantDNA")) {
                    	PlantDNA dna1 = PlantDNA.readFromNBT(left.getTagCompound());
                        PlantDNA dna2 = PlantDNA.readFromNBT(right.getTagCompound());
                        if(dna1.getPlant() == dna2.getPlant())
                        	return true;
                    }
                	return false;
                }
            }

            return false;
        }
    }

    @Override
    protected void processItem(int process) {
        if (this.canProcess(process)) {
            if (this.hybridizerMode) {
                Dinosaur hybrid = this.getHybrid();

                NBTTagCompound nbt = new NBTTagCompound();
                DinoDNA dna = new DinoDNA(getHybrid(), 100, GeneticsHelper.randomGenetics(world.rand));
                try {
                    dna = new DinoDNA(hybrid, 100, this.slots.get(0).getTagCompound().getString("Genetics"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dna.writeToNBT(nbt);

                ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
                output.setTagCompound(nbt);

                this.mergeStack(this.getOutputSlot(output), output);
            } else {
                ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);

                String storageId = this.slots.get(8).getTagCompound().getString("StorageId");

                if (storageId.equals("DinoDNA")) {
                    DinoDNA dna1 = DinoDNA.readFromNBT(this.slots.get(8).getTagCompound());
                    DinoDNA dna2 = DinoDNA.readFromNBT(this.slots.get(9).getTagCompound());
					int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

					if (newQuality > 100) {
						newQuality = 100;
					}

					DinoDNA newDNA = new DinoDNA(dna1.getDinosaur(), newQuality, dna1.getGenetics());

					NBTTagCompound outputTag = new NBTTagCompound();
					newDNA.writeToNBT(outputTag);
					output.setTagCompound(outputTag);
					
                } else if (storageId.equals("PlantDNA")) {
                    PlantDNA dna1 = PlantDNA.readFromNBT(this.slots.get(8).getTagCompound());
                    PlantDNA dna2 = PlantDNA.readFromNBT(this.slots.get(9).getTagCompound());
					int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

					if (newQuality > 100) {
						newQuality = 100;
					}

					PlantDNA newDNA = new PlantDNA(dna1.getPlant(), newQuality);

					NBTTagCompound outputTag = new NBTTagCompound();
					newDNA.writeToNBT(outputTag);
					output.setTagCompound(outputTag);
                }

                this.mergeStack(11, output);

                this.decreaseStackSize(8);
                this.decreaseStackSize(9);
            }
        }
    }

    @Override
    protected int getMainOutput(int process) {
        return this.hybridizerMode ? 10 : 11;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 1000;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return this.hybridizerMode ? HYBRIDIZER_INPUTS : COMBINATOR_INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return this.getInputs();
    }

    @Override
    protected int[] getOutputs() {
        return this.hybridizerMode ? HYBRIDIZER_OUTPUTS : COMBINATOR_OUTPUTS;
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
        return new DNACombinatorHybridizerContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
    	return RebornMod.MODID + ":dna_comb_hybrid";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dna_combinator_hybridizer";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.hybridizerMode = nbt.getBoolean("HybridizerMode");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setBoolean("HybridizerMode", this.hybridizerMode);

        return nbt;
    }

    @Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return Ints.asList(this.hybridizerMode ? HYBRIDIZER_OUTPUTS : COMBINATOR_OUTPUTS).contains(slotID);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(this.hybridizerMode ? HYBRIDIZER_INPUTS : COMBINATOR_INPUTS).contains(slotID)) {
			if (itemstack != null && itemstack.getItem() == ItemHandler.STORAGE_DISC && this.getStackInSlot(slotID).getCount() == 0 && (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("DNAQuality"))) {
				return true;
			}
		}

		return false;
	}

    public boolean getMode() {
        return this.hybridizerMode;
    }

    public void setMode(boolean mode) {
        this.hybridizerMode = mode;
        this.processTime[0] = 0;
        this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.hybridizerMode ? "container.dna_hybridizer" : "container.dna_combinator");
    }

	@Override
	public boolean isEmpty() {
		return false;
	}

}
