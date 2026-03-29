package net.vit.jurassicreborn.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.network.BlueprintPlacePacket;
import net.vit.jurassicreborn.common.network.Network;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SelectBlueprintScreen extends Screen {
    private static final int TEXTURE_PIXELS_PER_BLOCK = 16;

    private final BlockPos placePos;
    private final Direction face;
    private final InteractionHand hand;

    private List<ResourceLocation> allVariantIds = new ArrayList<>();
    private List<ResourceLocation> filteredVariantIds = new ArrayList<>();

    private int page = 0;
    private EditBox searchBox;

    private static final int COLUMNS = 4;
    private static final int ROWS = 3;
    private static final int PER_PAGE = COLUMNS * ROWS;

    private static final int CARD_WIDTH = 78;
    private static final int CARD_HEIGHT = 62;
    private static final int CARD_PADDING = 6;

    private static final int BACKGROUND = 0xC01A2433;
    private static final int NAVBAR = 0xFF0F1D33;
    private static final int PANEL = 0xFF1D314D;
    private static final int SILVER = 0xFFE1E6EF;

    private static final TagKey<PaintingVariant> BLUEPRINT_VARIANTS_TAG = TagKey.create(
            Registries.PAINTING_VARIANT,
            ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "blueprint_variants")
    );

    public SelectBlueprintScreen(BlockPos pos, Direction face, InteractionHand hand) {
        super(Component.literal("Blueprint Selection"));
        this.placePos = pos;
        this.face = face;
        this.hand = hand;
    }

    @Override
    protected void init() {
        Registry<PaintingVariant> registry = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);

        this.allVariantIds = registry.holders()
                .filter(holder -> holder.is(BLUEPRINT_VARIANTS_TAG))
                .map(holder -> registry.getResourceKey(holder.value()).map(key -> key.location()).orElse(null))
                .filter(id -> id != null)
                .sorted(Comparator.comparing(id -> getDisplayName(id).toLowerCase(Locale.ROOT)))
                .toList();

        this.filteredVariantIds = new ArrayList<>(this.allVariantIds);

        int searchWidth = 174;
        int searchX = (this.width - searchWidth) / 2;

        this.searchBox = new EditBox(this.font, searchX, 6, searchWidth, 14, Component.literal("Search blueprints"));
        this.searchBox.setMaxLength(64);
        this.searchBox.setResponder(value -> {
            this.page = 0;
            applySearch(value);
            buildPage();
        });

        this.setInitialFocus(searchBox);
        buildPage();
    }

    private void applySearch(String query) {
        String lowered = query.toLowerCase(Locale.ROOT).trim();

        this.filteredVariantIds = this.allVariantIds.stream()
                .filter(id -> {
                    String label = getDisplayName(id).toLowerCase(Locale.ROOT);
                    return lowered.isEmpty() || label.contains(lowered) || id.getPath().contains(lowered);
                })
                .toList();
    }

    private String getDisplayName(ResourceLocation id) {
        return id.getPath().replace('_', ' ');
    }

    private ResourceLocation getBlueprintTexture(ResourceLocation id) {
        return ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/painting/" + id.getPath() + ".png");
    }

    private PaintingVariant getVariant(ResourceLocation id) {
        Registry<PaintingVariant> registry = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);
        return registry.get(id);
    }

    private void selectBlueprint(ResourceLocation selectedVariant) {
        Network.sendToServer(new BlueprintPlacePacket(placePos, face, hand, selectedVariant));
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    private void buildPage() {
        this.clearWidgets();
        this.addRenderableWidget(searchBox);

        int gridWidth = COLUMNS * CARD_WIDTH;
        int startX = (this.width - gridWidth) / 2;
        int startY = 33;

        int startIndex = page * PER_PAGE;
        int endIndex = Math.min(startIndex + PER_PAGE, filteredVariantIds.size());

        int idx = startIndex;
        for (int row = 0; row < ROWS && idx < endIndex; row++) {
            for (int col = 0; col < COLUMNS && idx < endIndex; col++) {
                ResourceLocation variantId = filteredVariantIds.get(idx);

                int cardX = startX + col * CARD_WIDTH;
                int cardY = startY + row * CARD_HEIGHT;

                this.addRenderableWidget(new BlueprintClickWidget(cardX, cardY, CARD_WIDTH, CARD_HEIGHT, variantId));
                idx++;
            }
        }

        int totalPages = Math.max(1, (filteredVariantIds.size() + PER_PAGE - 1) / PER_PAGE);

        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> {
            this.page = (this.page - 1 + totalPages) % totalPages;
            buildPage();
        }).bounds(startX, startY + ROWS * CARD_HEIGHT + 4, 20, 18).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), btn -> {
            this.page = (this.page + 1) % totalPages;
            buildPage();
        }).bounds(startX + gridWidth - 20, startY + ROWS * CARD_HEIGHT + 4, 20, 18).build());
    }
    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, BACKGROUND);
        guiGraphics.fill(0, 0, this.width, 26, NAVBAR);
        guiGraphics.fill(0, 24, this.width, 26, SILVER);

        int gridWidth = COLUMNS * CARD_WIDTH;
        int startX = (this.width - gridWidth) / 2;
        int startY = 33;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int slot = row * COLUMNS + col;
                int variantIndex = this.page * PER_PAGE + slot;

                int cardX = startX + col * CARD_WIDTH;
                int cardY = startY + row * CARD_HEIGHT;

                guiGraphics.fill(cardX, cardY, cardX + CARD_WIDTH - 4, cardY + CARD_HEIGHT - 4, PANEL);

                if (variantIndex < filteredVariantIds.size()) {
                    ResourceLocation variantId = filteredVariantIds.get(variantIndex);
                    ResourceLocation texture = getBlueprintTexture(variantId);

                    PaintingVariant variant = getVariant(variantId);
                    int texW = variant != null ? variant.width() * TEXTURE_PIXELS_PER_BLOCK : TEXTURE_PIXELS_PER_BLOCK;
                    int texH = variant != null ? variant.height() * TEXTURE_PIXELS_PER_BLOCK : TEXTURE_PIXELS_PER_BLOCK;

                    int maxW = CARD_WIDTH - (CARD_PADDING * 2);
                    int maxH = CARD_HEIGHT - 16;

                    float scale = Math.min((float) maxW / texW, (float) maxH / texH) * 0.95f;

                    int imageW = Math.max(1, Math.round(texW * scale));
                    int imageH = Math.max(1, Math.round(texH * scale));

                    int imageX = cardX + ((CARD_WIDTH - imageW) / 2);
                    int imageY = cardY + 4;

                    guiGraphics.blit(
//                            RenderPipelines.GUI_TEXTURED,
                            texture,
                            imageX,
                            imageY,
                            0,
                            0,
                            imageW,
                            imageH,
                            texW,
                            texH
                    );

                    guiGraphics.drawCenteredString(
                            this.font,
                            getDisplayName(variantId),
                            cardX + (CARD_WIDTH / 2) - 2,
                            cardY + CARD_HEIGHT - 7,
                            0xFFFFFF
                    );
                }
            }
        }

        guiGraphics.drawString(this.font, this.title, 8, 8, SILVER, false);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private class BlueprintClickWidget extends AbstractWidget {
        private final ResourceLocation selectedVariant;

        private BlueprintClickWidget(int x, int y, int width, int height, ResourceLocation variant) {
            super(x, y, width, height, Component.empty());
            this.selectedVariant = variant;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            selectBlueprint(this.selectedVariant);
        }
    }
}
