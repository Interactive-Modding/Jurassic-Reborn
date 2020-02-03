package mod.reborn.client.gui;

import mod.reborn.RebornMod;
import mod.reborn.server.item.JournalItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class JournalGui extends GuiScreen {
    private static final int SIZE_X = 256;
    private static final int SIZE_Y = 192;

    private static final float FONT_SCALE = 1.0F;

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/journal/background.png");
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/journal/widgets.png");

    private final JournalItem.JournalType type;
    private final JournalItem.Content content;
    private String[][] pages;
    private int pageCount;

    private PageButton nextPage;
    private PageButton previousPage;
    private int page;

    public JournalGui(JournalItem.JournalType type) {
        this.type = type;
        this.content = type.getContent();
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        this.buttonList.add(this.nextPage = new PageButton(0, x + 235, y + 180, true));
        this.buttonList.add(this.previousPage = new PageButton(1, x - 3, y + 180, false));

        List<String> lines = new ArrayList<>();

        for (String[] entry : this.content.getEntries()) {
            for (String line : entry) {
                List<JournalFormatting> format = new ArrayList<>();
                for (JournalFormatting formatting : JournalFormatting.values()) {
                    line = formatting.apply(line, format);
                }
                float scale = FONT_SCALE;
                for (JournalFormatting formatting : format) {
                    scale *= formatting.getScaleModifier();
                }
                StringBuilder currentLine = new StringBuilder();
                for (String word : line.split("\\s")) {
                    if (this.fontRenderer.getStringWidth(currentLine.toString() + word) * scale > 94) {
                        lines.add(JournalFormatting.format(currentLine.toString(), format));
                        currentLine = new StringBuilder();
                    }
                    currentLine.append(word).append(" ");
                }
                lines.add(JournalFormatting.format(currentLine.toString(), format));
            }
            lines.add("\n");
        }

        List<String[]> pages = new ArrayList<>();
        List<String> currentPage = new ArrayList<>();

        int lineOffsetY = 0;
        int lineHeight = (int) (this.fontRenderer.FONT_HEIGHT * FONT_SCALE + 2);

        for (String line : lines) {
            boolean nextPage = line.endsWith("\n");
            if (!nextPage) {
                currentPage.add(line);
            }
            lineOffsetY += lineHeight;
            if (lineOffsetY > 140 || nextPage) {
                pages.add(currentPage.toArray(new String[currentPage.size()]));
                currentPage.clear();
                lineOffsetY = 0;
            }
        }

        this.pages = pages.toArray(new String[pages.size()][]);
        this.pageCount = MathHelper.ceil(this.pages.length / 2.0);

        this.updateButtons();
    }

    private void updateButtons() {
        this.nextPage.visible = this.page < this.pageCount - 1;
        this.previousPage.visible = this.page > 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int x = (this.width - SIZE_X) / 2;
        int y = (this.height - SIZE_Y) / 2;

        this.zLevel = -1000;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect(x, y, 0, 0, 256, 256);

        this.drawPage(this.page * 2, x + 18, y + 16);
        this.drawPage(this.page * 2 + 1, x + 145, y + 16);
    }

    private void drawPage(int pageIndex, int originX, int originY) {
        if (pageIndex >= 0 && pageIndex < this.pages.length) {
            int lineOffsetY = 0;
            int lineHeight = (int) (this.fontRenderer.FONT_HEIGHT * FONT_SCALE + 2);
            String[] page = this.pages[pageIndex];
            for (String line : page) {
                this.drawScaledString(line, originX, originY + lineOffsetY, FONT_SCALE, 0);
                lineOffsetY += lineHeight;
            }
        }
    }

    private void drawScaledString(String text, float x, float y, float scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.fontRenderer.drawString(text, x / scale, y / scale, color, false);
        GlStateManager.popMatrix();
    }

    private void drawCenteredScaledString(String text, float x, float y, float scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.fontRenderer.drawString(text, (x - this.fontRenderer.getStringWidth(text) / 2) / scale, y / scale, color, false);
        GlStateManager.popMatrix();
    }

    private void drawFullTexturedRect(int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, this.zLevel).tex(0.0F, 1.0F).endVertex();
        buffer.pos(x + width, y + height, this.zLevel).tex(1.0F, 1.0F).endVertex();
        buffer.pos(x + width, y, this.zLevel).tex(1.0F, 0.0F).endVertex();
        buffer.pos(x, y, this.zLevel).tex(0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                if (this.page < this.pageCount) {
                    this.page++;
                }
            } else if (button.id == 1) {
                if (this.page > 0) {
                    this.page--;
                }
            }

            this.updateButtons();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private enum JournalFormatting {
        BOLD("*", TextFormatting.BOLD, 1.15F),
        ITALICS("_", TextFormatting.ITALIC);

        private final String identifier;
        private final TextFormatting textFormat;
        private final float scaleModifier;

        JournalFormatting(String identifier, TextFormatting textFormat) {
            this(identifier, textFormat, 1.0F);
        }

        JournalFormatting(String identifier, TextFormatting textFormat, float scaleModifier) {
            this.identifier = identifier;
            this.textFormat = textFormat;
            this.scaleModifier = scaleModifier;
        }

        public static String format(String text, List<JournalFormatting> format) {
            for (JournalFormatting formatting : format) {
                text = formatting.format(text);
            }
            text = text.replaceAll("\\\\", "");
            return text;
        }

        public String format(String text) {
            return this.textFormat.toString() + text;
        }

        public String apply(String text, List<JournalFormatting> format) {
            if (text.startsWith(this.identifier)) {
                format.add(this);
                return text.replaceFirst(Pattern.quote(this.identifier), "");
            }
            return text;
        }

        public float getScaleModifier() {
            return this.scaleModifier;
        }
    }

    @SideOnly(Side.CLIENT)
    static class PageButton extends GuiButton {
        private final boolean isForward;

        PageButton(int id, int x, int y, boolean isForward) {
            super(id, x, y, 23, 13, "");
            this.isForward = isForward;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean selected = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(WIDGETS_TEXTURE);
                int u = 0;
                int v = 194;
                if (selected) {
                    u += 23;
                }
                if (!this.isForward) {
                    v += 13;
                }
                this.drawTexturedModalRect(this.x, this.y, u, v, 23, 13);
            }
        }
    }
}
