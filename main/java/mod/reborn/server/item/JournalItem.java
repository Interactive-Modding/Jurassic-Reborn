package mod.reborn.server.item;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.client.util.ITooltipFlag;
import mod.reborn.RebornMod;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class JournalItem extends Item {
    public JournalItem() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
        	
            RebornMod.PROXY.openJournal(JournalType.get(stack.getMetadata()));
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        JournalType type = JournalType.get(stack.getMetadata());
        tooltip.add(I18n.translateToLocal("journal." + type.getIdentifier().getResourcePath() + ".name"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab))
        for (JournalType type : JournalType.values()) {
            items.add(new ItemStack(this, 1, type.getMetadata()));
        }
    }

    public enum JournalType {
        CHEF_ALEJANDRO(0, new ResourceLocation(RebornMod.MODID, "chef_alejandro")),
        DENNIS_NEDRY(1, new ResourceLocation(RebornMod.MODID, "dennis_nedry")),
        DR_GERRY_HARDING(2, new ResourceLocation(RebornMod.MODID, "dr_gerry_harding")),
        DR_HENRY_WU(3, new ResourceLocation(RebornMod.MODID, "dr_henry_wu")),
        DR_LAURA_SORKIN(4, new ResourceLocation(RebornMod.MODID, "dr_laura_sorkin")),
        ED_REGIS(5, new ResourceLocation(RebornMod.MODID, "ed_regis")),
        JOHN_HAMMOND(6, new ResourceLocation(RebornMod.MODID, "john_hammond")),
        RAY_ARNOLD(7, new ResourceLocation(RebornMod.MODID, "ray_arnold")),
        ROBERT_MULDOON(8, new ResourceLocation(RebornMod.MODID, "robert_muldoon"));

        public static final JournalType[] VALUES = new JournalType[values().length];

        private final int metadata;
        private final ResourceLocation identifier;
        private final ResourceLocation location;

        @SideOnly(Side.CLIENT)
        private Content content;

        static {
            for (JournalType type : values()) {
                VALUES[type.getMetadata()] = type;
            }
        }

        JournalType(int metadata, ResourceLocation identifier) {
            this.metadata = metadata;
            this.identifier = identifier;
            this.location = new ResourceLocation(identifier.getResourceDomain(), "journal_entries/" + identifier.getResourcePath() + ".json");
        }

        public int getMetadata() {
            return this.metadata;
        }

        @SideOnly(Side.CLIENT)
        public Content getContent() {
            if (this.content == null) {
                try (InputStream input = Minecraft.getMinecraft().getResourceManager().getResource(this.location).getInputStream()) {
                    this.content = new Gson().fromJson(new InputStreamReader(input), Content.class);
                } catch (IOException e) {
                    String[][] entries = new String[][] { new String[] { "Failed to load journal entries" } };
                    return new Content("rebornmod:error", entries);
                }
            }
            return this.content;
        }

        public ResourceLocation getIdentifier() {
            return this.identifier;
        }

        public ResourceLocation getLocation() {
            return this.location;
        }

        public static JournalType get(int metadata) {
            if (metadata >= 0 && metadata < VALUES.length) {
                return VALUES[metadata];
            }
            return CHEF_ALEJANDRO;
        }
    }

    public static class Content {
        private String identifier;
        private String[][] entries;

        public Content(String identifier, String[][] entries) {
            this.identifier = identifier;
            this.entries = entries;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String[][] getEntries() {
            return this.entries;
        }
    }
}
