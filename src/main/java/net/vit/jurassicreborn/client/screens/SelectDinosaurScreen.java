package net.vit.jurassicreborn.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.network.PaddockSignPlacePacket;

import java.util.ArrayList;
import java.util.List;

public class SelectDinosaurScreen extends Screen {
    private final BlockPos signPos;
    private final Direction face;
    private final InteractionHand hand;

    private List<Integer> allDinoIds;
    private List<Integer> filteredDinoIds;
    private int page = 0;
    private EditBox searchBox;

    private static final int COLUMNS = 4;
    private static final int ROWS = 3;
    private static final int PER_PAGE = COLUMNS * ROWS;
    private static final int CARD_WIDTH = 78;
    private static final int CARD_HEIGHT = 62;
    private static final int IMAGE_WIDTH = 58;
    private static final int IMAGE_HEIGHT = 48;

    private static final int BACKGROUND = 0xC03A3A3A;
    private static final int NAVBAR = 0xFF2A2A2A;
    private static final int PANEL = 0xFF474747;
    private static final int ORANGE = 0xFFFF8C2A;
    private static final int ORANGE_DARK = 0xFFCC6B15;

    public SelectDinosaurScreen(BlockPos pos, Direction face, InteractionHand hand) {
        super(Component.literal("Paddock Sign Selection"));
        this.signPos = pos;
        this.face = face;
        this.hand = hand;
    }

    @Override
    protected void init() {
        DinosaurHandler.doDinosInit();

        this.allDinoIds = new ArrayList<>(DinosaurHandler.getRegisteredIds());
        this.allDinoIds.sort((a, b) -> {
            Dinosaur d1 = DinosaurHandler.getById(a);
            Dinosaur d2 = DinosaurHandler.getById(b);

            String n1 = d1 == Dinosaur.EMPTY ? "none" : d1.getName();
            String n2 = d2 == Dinosaur.EMPTY ? "none" : d2.getName();

            return n1.compareToIgnoreCase(n2);
        });

        this.filteredDinoIds = new ArrayList<>(this.allDinoIds);
        this.page = 0;

        int searchWidth = 174;
        int searchX = (this.width - searchWidth) / 2;
        this.searchBox = new EditBox(this.font, searchX, 6, searchWidth, 14, Component.literal("Search dinosaurs"));
        this.searchBox.setBordered(true);
        this.searchBox.setTextColor(0xFFF4F4F4);
        this.searchBox.setTextColorUneditable(0xFFBBBBBB);
        this.searchBox.setMaxLength(64);
        this.searchBox.setResponder(value -> {
            this.page = 0;
            this.applySearch(value);
            this.buildPage();
        });

        this.setInitialFocus(this.searchBox);
        this.buildPage();
    }

    private void applySearch(String query) {
        String lowered = query.toLowerCase().trim();
        this.filteredDinoIds = this.allDinoIds.stream().filter(id -> {
            Dinosaur dino = DinosaurHandler.getById(id);
            if (dino == Dinosaur.EMPTY) {
                return "none".contains(lowered);
            }
            return dino.getName().toLowerCase().contains(lowered);
        }).toList();
    }
    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    private ResourceLocation getSignTexture(int dinosaurId) {
        Dinosaur dino = DinosaurHandler.getById(dinosaurId);
        String textureName = dino == Dinosaur.EMPTY ? "none" : dino.getName().replace(' ', '_').toLowerCase();
        return ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/paddock/" + textureName + ".png");
    }

    private void selectDinosaur(int selectedId) {
        Network.sendToServer(new PaddockSignPlacePacket(signPos, face, hand, selectedId));
        this.minecraft.setScreen(null);
    }

    private void buildPage() {
        this.clearWidgets();
        this.addRenderableWidget(this.searchBox);

        int gridWidth = COLUMNS * CARD_WIDTH;
        int gridHeight = ROWS * CARD_HEIGHT;
        int startX = (this.width - gridWidth) / 2;
        int startY = 33;

        int startIndex = page * PER_PAGE;
        int endIndex = Math.min(startIndex + PER_PAGE, filteredDinoIds.size());

        int idx = startIndex;
        for (int row = 0; row < ROWS && idx < endIndex; row++) {
            for (int col = 0; col < COLUMNS && idx < endIndex; col++) {
                int id = filteredDinoIds.get(idx);
                int cardX = startX + col * CARD_WIDTH;
                int cardY = startY + row * CARD_HEIGHT;
                int imageX = cardX + ((CARD_WIDTH - IMAGE_WIDTH) / 2);
                int imageY = cardY + ((CARD_HEIGHT - IMAGE_HEIGHT) / 2);

                this.addRenderableWidget(new SignClickWidget(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT, id));
                idx++;
            }
        }

        int totalPages = Math.max(1, (filteredDinoIds.size() + PER_PAGE - 1) / PER_PAGE);
        int pagerY = startY + gridHeight + 4;
        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> {
            page = (page - 1 + totalPages) % totalPages;
            buildPage();
        }).bounds(startX, pagerY, 20, 18).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), btn -> {
            page = (page + 1) % totalPages;
            buildPage();
        }).bounds(startX + gridWidth - 20, pagerY, 20, 18).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float ptt) {
        guiGraphics.fill(0, 0, this.width, this.height, BACKGROUND);
        guiGraphics.fill(0, 0, this.width, 26, NAVBAR);
        guiGraphics.fill(0, 24, this.width, 26, ORANGE);

        int gridWidth = COLUMNS * CARD_WIDTH;
        int gridHeight = ROWS * CARD_HEIGHT;
        int startX = (this.width - gridWidth) / 2;
        int startY = 33;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int slot = row * COLUMNS + col;
                int dinoIndex = page * PER_PAGE + slot;
                int cardX = startX + col * CARD_WIDTH;
                int cardY = startY + row * CARD_HEIGHT;

                guiGraphics.fill(cardX, cardY, cardX + CARD_WIDTH - 4, cardY + CARD_HEIGHT - 4, PANEL);
                guiGraphics.fill(cardX, cardY, cardX + CARD_WIDTH - 4, cardY + 1, ORANGE_DARK);

                if (dinoIndex < filteredDinoIds.size()) {
                    ResourceLocation texture = getSignTexture(filteredDinoIds.get(dinoIndex));
                    int imageX = cardX + ((CARD_WIDTH - IMAGE_WIDTH) / 2);
                    int imageY = cardY + ((CARD_HEIGHT - IMAGE_HEIGHT) / 2);
                    guiGraphics.blit(texture, imageX, imageY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
                }
            }
        }

        guiGraphics.drawString(this.font, this.title, 8, 8, ORANGE, false);

        int totalPages = Math.max(1, (filteredDinoIds.size() + PER_PAGE - 1) / PER_PAGE);
        guiGraphics.drawCenteredString(this.font, Component.literal((page + 1) + "/" + totalPages), width / 2, startY + gridHeight + 9, 0xFFF8F8F8);

        if (filteredDinoIds.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, Component.literal("No paddock signs match your search."), width / 2, startY + gridHeight / 2, ORANGE);
        }

        super.render(guiGraphics, mouseX, mouseY, ptt);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            return this.searchBox.charTyped(codePoint, modifiers);
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox != null && this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    private class SignClickWidget extends AbstractWidget {
        private final int selectedId;

        private SignClickWidget(int x, int y, int width, int height, int selectedId) {
            super(x, y, width, height, Component.empty());
            this.selectedId = selectedId;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            // Intentionally no widget visuals; only the sign PNG is shown.
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            selectDinosaur(this.selectedId);
        }
    }
}