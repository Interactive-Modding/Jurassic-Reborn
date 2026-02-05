package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.items.JournalItem;

import java.util.ArrayList;
import java.util.List;

public class JournalGui extends Screen {
    private static final int SIZE_X = 256;
    private static final int SIZE_Y = 192;
    private static final float FONT_SCALE = 1.0F;

    private static final ResourceLocation BACKGROUND = new ResourceLocation(JurassicReborn.MODID, "textures/journal/background.png");
    private static final ResourceLocation WIDGETS = new ResourceLocation(JurassicReborn.MODID, "textures/journal/widgets.png");

    private final JournalItem.JournalType type;
    private final JournalItem.Content content;
    private String[][] pages;
    private int pageCount;
    private int page = 0;

    private PageButton nextButton;
    private PageButton prevButton;

    public JournalGui(JournalItem.JournalType type) {
        super(Component.literal(""));
        this.type = type;
        this.content = type.getContent();
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        this.nextButton = this.addRenderableWidget(new PageButton(x + 235, y + 180, true, btn -> {
            if (page < pageCount - 1) {
                page++;
                updateButtons();
            }
        }));
        this.prevButton = this.addRenderableWidget(new PageButton(x - 3, y + 180, false, btn -> {
            if (page > 0) {
                page--;
                updateButtons();
            }
        }));

        // Build text lines
        List<String> lines = new ArrayList<>();
        for (String[] entry : content.getEntries()) {
            for (String raw : entry) {
                String line = raw;
                List<JournalFormatting> formats = new ArrayList<>();
                for (JournalFormatting fmt : JournalFormatting.values()) {
                    line = fmt.apply(line, formats);
                }
                float scale = FONT_SCALE;
                for (JournalFormatting fmt : formats) {
                    scale *= fmt.getScaleModifier();
                }
                StringBuilder current = new StringBuilder();
                for (String word : line.split("\\s")) {
                    if (this.font.width(current.toString() + word) * scale > 94) {
                        lines.add(JournalFormatting.format(current.toString(), formats));
                        current = new StringBuilder();
                    }
                    current.append(word).append(" ");
                }
                lines.add(JournalFormatting.format(current.toString(), formats));
            }
            lines.add("\n");
        }

        // Paginate
        List<String[]> pageList = new ArrayList<>();
        List<String> currentPage = new ArrayList<>();
        int yOff = 0;
        int lineHeight = (int)(this.font.lineHeight * FONT_SCALE + 2);
        for (String line : lines) {
            boolean newPage = line.endsWith("\n");
            if (!newPage) currentPage.add(line);
            yOff += lineHeight;
            if (yOff > 140 || newPage) {
                pageList.add(currentPage.toArray(new String[0]));
                currentPage.clear();
                yOff = 0;
            }
        }
        pages = pageList.toArray(new String[0][]);
        pageCount = Mth.ceil(pages.length / 2.0F);
        updateButtons();
    }

    private void updateButtons() {
        nextButton.visible = page < pageCount - 1;
        prevButton.visible = page > 0;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pose);
        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(pose, x, y, 0, 0, SIZE_X, SIZE_Y);

        drawPage(pose, page * 2, x + 18, y + 16);
        drawPage(pose, page * 2 + 1, x + 145, y + 16);

        super.render(pose, mouseX, mouseY, partialTicks);
    }

    private void drawPage(PoseStack pose, int index, int ox, int oy) {
        if (index >= 0 && index < pages.length) {
            int yOff = 0;
            int lh = (int)(this.font.lineHeight * FONT_SCALE + 2);
            for (String line : pages[index]) {
                drawScaledString(pose, line, ox, oy + yOff, FONT_SCALE, 0x000000);
                yOff += lh;
            }
        }
    }

    private void drawScaledString(PoseStack pose, String text, float x, float y, float scale, int color) {
        pose.pushPose();
        pose.scale(scale, scale, 1.0F);
        this.font.draw(pose, text, x / scale, y / scale, color);
        pose.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private enum JournalFormatting {
        BOLD("*", ChatFormatting.BOLD, 1.15F),
        ITALIC("_", ChatFormatting.ITALIC, 1.0F);

        private final String id;
        private final ChatFormatting fmt;
        private final float scaleModifier;

        JournalFormatting(String id, ChatFormatting fmt, float scaleModifier) {
            this.id = id;
            this.fmt = fmt;
            this.scaleModifier = scaleModifier;
        }

        public static String format(String text, List<JournalFormatting> fmts) {
            for (JournalFormatting f : fmts) {
                text = f.fmt + text;
            }
            return text.replace("\\\\", "");
        }

        public String apply(String text, List<JournalFormatting> fmts) {
            if (text.startsWith(id)) {
                fmts.add(this);
                return text.substring(id.length());
            }
            return text;
        }

        public float getScaleModifier() {
            return scaleModifier;
        }
    }

    private static class PageButton extends Button {
        private final boolean isForward;

        public PageButton(int x, int y, boolean forward, OnPress onPress) {
            super(x, y, 23, 13, Component.empty(), onPress, DEFAULT_NARRATION);
            this.isForward = forward;
        }

        @Override
        public void renderButton(PoseStack pose, int mx, int my, float pt) {
            if (visible) {
                RenderSystem.setShaderTexture(0, WIDGETS);
                boolean hover = isMouseOver(mx, my);
                int u = hover ? 23 : 0;
                int v = isForward ? 194 : 207;
                blit(pose, this.getX(), this.getY(), u, v, this.width, this.height);
            }
        }
    }
}
