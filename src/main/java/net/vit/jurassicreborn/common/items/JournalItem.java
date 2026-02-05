package net.vit.jurassicreborn.common.items;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.JournalGui;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JournalItem extends Item {
    private final JournalType type;

    public JournalItem(JournalType type) {
        super(new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS));
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            openScreen();
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen() {
        Minecraft.getInstance().setScreen(new JournalGui(type));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("journal." + type.getIdentifier().getPath() + ".name"));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (group == TabHandler.ITEMS) {
            items.add(new ItemStack(this));
        }
    }

    public enum JournalType {
        CHEF_ALEJANDRO(0, new ResourceLocation(JurassicReborn.MODID, "chef_alejandro")),
        DENNIS_NEDRY   (1, new ResourceLocation(JurassicReborn.MODID, "dennis_nedry")),
        DR_GERRY_HARDING(2, new ResourceLocation(JurassicReborn.MODID, "dr_gerry_harding")),
        DR_HENRY_WU    (3, new ResourceLocation(JurassicReborn.MODID, "dr_henry_wu")),
        DR_LAURA_SORKIN(4, new ResourceLocation(JurassicReborn.MODID, "dr_laura_sorkin")),
        ED_REGIS       (5, new ResourceLocation(JurassicReborn.MODID, "ed_regis")),
        JOHN_HAMMOND   (6, new ResourceLocation(JurassicReborn.MODID, "john_hammond")),
        RAY_ARNOLD     (7, new ResourceLocation(JurassicReborn.MODID, "ray_arnold")),
        ROBERT_MULDOON (8, new ResourceLocation(JurassicReborn.MODID, "robert_muldoon"));

        public static final JournalType[] VALUES = values();

        private final int metadata;
        private final ResourceLocation identifier;
        private final ResourceLocation location;
        @OnlyIn(Dist.CLIENT)
        private Content content;

        JournalType(int metadata, ResourceLocation identifier) {
            this.metadata   = metadata;
            this.identifier = identifier;
            this.location   = new ResourceLocation(
                    identifier.getNamespace(),
                    "journal_entries/" + identifier.getPath() + ".json"
            );
        }

        public int getMetadata() {
            return metadata;
        }

        public ResourceLocation getIdentifier() {
            return identifier;
        }

        @OnlyIn(Dist.CLIENT)
        public Content getContent() {
            if (content == null) {
                try {
                    Minecraft minecraft = Minecraft.getInstance();
                    Resource resource = minecraft.getResourceManager().getResource(location);
                    if (resource != null) {
                        try (InputStream in = resource.getInputStream()) {
                            content = new Gson().fromJson(new InputStreamReader(in), Content.class);
                        }
                    } else {
                        content = getFallbackContent();
                    }
                } catch (IOException e) {
                    content = getFallbackContent();
                }
            }
            return content;
        }

        private Content getFallbackContent() {
            String[][] fallback = { { "Failed to load journal entries" } };
            return new Content("error", fallback);
        }

        public static JournalType get(int meta) {
            if (meta >= 0 && meta < VALUES.length) return VALUES[meta];
            return CHEF_ALEJANDRO;
        }
    }

    public static class Content {
        private final String identifier;
        private final String[][] entries;
        public Content(String identifier, String[][] entries) {
            this.identifier = identifier;
            this.entries    = entries;
        }
        public String getIdentifier() { return identifier; }
        public String[][] getEntries() { return entries; }
    }
}
