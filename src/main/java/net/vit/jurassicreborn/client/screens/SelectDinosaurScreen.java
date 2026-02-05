package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.network.PaddockSignPlacePacket;

import java.util.List;

public class SelectDinosaurScreen extends Screen {
    private final BlockPos signPos;
    private final Direction face;
    private final InteractionHand hand;

    private List<Integer> dinoIds;
    private int page = 0;

    private static final int COLUMNS = 4;
    private static final int ROWS = 5;
    private static final int PER_PAGE = COLUMNS * ROWS;

    public SelectDinosaurScreen(BlockPos pos, Direction face, InteractionHand hand) {
        super(new TextComponent("Select Dinosaur"));
        this.signPos = pos;
        this.face    = face;
        this.hand    = hand;
    }

    @Override
    protected void init() {
        DinosaurHandler.doDinosInit();
        this.dinoIds = DinosaurHandler.getRegisteredIds();

        this.page = 0;

        buildPage();
    }

    private void buildPage() {
        this.clearWidgets();

        int gridWidth = COLUMNS * 80;
        int gridHeight = ROWS * 20;
        int startX = (this.width - gridWidth) / 2;
        int startY = (this.height - gridHeight) / 2;

        int startIndex = page * PER_PAGE;
        int endIndex = Math.min(startIndex + PER_PAGE, dinoIds.size());

        int idx = startIndex;
        for (int row = 0; row < ROWS && idx < endIndex; row++) {
            for (int col = 0; col < COLUMNS && idx < endIndex; col++) {
                int id = dinoIds.get(idx);
                Dinosaur d = DinosaurHandler.getById(id);
                String name = d == Dinosaur.EMPTY ? "None" : d.getName();
                int x = startX + col * 80;
                int y = startY + row * 20;
                final int selectedId = id;
                this.addRenderableWidget(new Button(x, y, 80, 20, new TextComponent(name), btn -> {
                    Network.sendToServer(new PaddockSignPlacePacket(signPos, face, hand, selectedId));
                    this.minecraft.setScreen(null);
                }));
                idx++;
            }
        }

        int totalPages = (dinoIds.size() + PER_PAGE - 1) / PER_PAGE;
        this.addRenderableWidget(new Button(startX, startY + gridHeight + 5, 20, 20, new TextComponent("<"), btn -> {
            page = (page - 1 + totalPages) % totalPages;
            buildPage();
        }));

        this.addRenderableWidget(new Button(startX + gridWidth - 20, startY + gridHeight + 5, 20, 20, new TextComponent(">"), btn -> {
            page = (page + 1) % totalPages;
            buildPage();
        }));
    }

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float ptt) {
        this.renderBackground(ms);

        int gridHeight = ROWS * 20;
        int startY = (this.height - gridHeight) / 2;

        drawCenteredString(ms, this.font, this.title, width / 2, startY - 20, 0xFFFFFF);

        int totalPages = (dinoIds.size() + PER_PAGE - 1) / PER_PAGE;
        drawCenteredString(ms, this.font, new TextComponent((page + 1) + "/" + totalPages), width / 2, startY + gridHeight + 10, 0xFFFFFF);

        super.render(ms, mouseX, mouseY, ptt);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}