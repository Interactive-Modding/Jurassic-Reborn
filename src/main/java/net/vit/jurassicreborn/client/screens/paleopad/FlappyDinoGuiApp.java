package net.vit.jurassicreborn.client.screens.paleopad;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.PaleoPadScreen;
import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.paleopad.FlappyDinoApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FlappyDinoGuiApp extends GuiApp {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/flappy_dino.png");
    private static final ResourceLocation LOGO = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/flappy_dino.png");
    private static final ResourceLocation PTERANODON = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/flappy_dino_pteranodon.png");
    private static final ResourceLocation[] CHARACTERS = new ResourceLocation[]{
            new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/pteranodon_char.png"),
            new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/dimorphodon_char.png"),
            new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/cearadactylus_char.png"),
            new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/quetzalcoatlus_char.png"),
            new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/quetzalcoatlus_sad_char.png")
    };
    private static final String[] CHAR_NAMES = new String[]{"Pteranodon", "Dimorphodon", "Cearadactylus", "Quetzalcoatlus","Colorful Quetzalcoatlus"};
    private ResourceLocation character;
    private int charIndex;
    private static final ResourceLocation PILLAR_BOTTOM = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/pillar_bottom.png");
    private static final ResourceLocation PILLAR_TOP = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/pillar_top.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(JurassicReborn.MODID, "textures/gui/paleo_pad/apps/background/flappy_dino_background.png");

    private boolean mainScreen;
    private int x;
    private int y;
    private int motionY;
    private int score;

    private final Map<Integer, Integer> pillars = new HashMap<>();

    public FlappyDinoGuiApp(App app) {
        super(app);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, Screen screen, float partialTicks) {
        renderButtons(poseStack, mouseX, mouseY, partialTicks);

        int left = screen.width / 2 - 115;
        int top = 65;

        RenderSystem.setShaderTexture(0, BACKGROUND);
        GuiComponent.blit(poseStack, left, top, 0, 0, 229, 150, 229, 150);

        if (mainScreen) {
            RenderSystem.setShaderTexture(0, LOGO);
            GuiComponent.blit(poseStack, left + 5, top + 5, 0, 0, 128, 64, 128, 64);

            RenderSystem.setShaderTexture(0, PTERANODON);
            GuiComponent.blit(poseStack, left + 145, top + 15, 0, 0, 128, 64, 128, 64);

            RenderSystem.setShaderTexture(0, character);
            GuiComponent.blit(poseStack, left + 10, top + 80, 0, 0, 32, 32, 32, 32);
            ((PaleoPadScreen) screen).drawScaledText(poseStack, "Character", left + 10, top + 75, 0.6F, 0xFFFFFF);
            ((PaleoPadScreen) screen).drawScaledText(poseStack, CHAR_NAMES[charIndex], left + 45, top + 90, 0.7F, 0xFFFFFF);
            ((PaleoPadScreen) screen).drawScaledText(poseStack, "Click to change", left + 10, top + 115, 0.6F, 0xFFFFFF);

            FlappyDinoApp fApp = (FlappyDinoApp) app;
            ((PaleoPadScreen) screen).drawScaledText(poseStack, "High Scores", left + 145, top + 80, 0.6F, 0xFFFFFF);
            int yOff = top + 90;
            int i = 1;
            for (int s : fApp.getScores()) {
                ((PaleoPadScreen) screen).drawScaledText(poseStack, i + ". " + s, left + 145, yOff, 0.6F, 0xFFFFFF);
                yOff += 10;
                if (i++ >= 5) break;
            }

            ((PaleoPadScreen) screen).drawScaledRect(poseStack, left + 90, top + 100, 50, 20, 1.0F, 0x545454);
            ((PaleoPadScreen) screen).drawScaledRect(poseStack, left + 91, top + 101, 48, 18, 1.0F, 0x747474);
            ((PaleoPadScreen) screen).drawScaledText(poseStack, "Play", left + 105, top + 107, 1.0F, 0xFFFFFF);
        } else {
            RenderSystem.setShaderTexture(0, character);
            GuiComponent.blit(poseStack, left + 5, top + (150 - y), 0, 0, 32, 32, 32, 32);

            RenderSystem.setShaderTexture(0, PILLAR_BOTTOM);
            for (Map.Entry<Integer, Integer> entry : pillars.entrySet()) {
                int drawX = entry.getKey() - this.x;
                if (drawX > 0 && drawX < 200) {
                    for (int height = 0; height < entry.getValue(); height++) {
                        GuiComponent.blit(poseStack, left + drawX, top + 130 - (height * 20), 0, 12, 32, 20, 32, 32);
                    }
                    GuiComponent.blit(poseStack, left + drawX, top + 139 - (entry.getValue() * 20), 0, 0, 32, 12, 32, 32);
                }
            }

            RenderSystem.setShaderTexture(0, PILLAR_TOP);
            for (Map.Entry<Integer, Integer> entry : pillars.entrySet()) {
                int drawX = entry.getKey() - this.x;
                if (drawX > 0 && drawX < 200) {
                    int totalHeight = 4 - entry.getValue();
                    for (int height = 0; height < totalHeight; height++) {
                        GuiComponent.blit(poseStack, left + drawX, top + (height * 20), 0, 0, 32, 20, 32, 32);
                    }
                    GuiComponent.blit(poseStack, left + drawX, top + (totalHeight * 20), 0, 20, 32, 12, 32, 32);

                    if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
                        int topHeight = top + ((4 - entry.getValue()) * 20) + 11;
                        int bottomHeight = top + (150 - (entry.getValue() * 20)) - 11;
                        int actualY = top + 150 - y;

                        ((PaleoPadScreen) screen).drawScaledRect(poseStack, left + drawX + 1, topHeight, 30, 1, 1.0F, 0xFFFFFF);
                        ((PaleoPadScreen) screen).drawScaledRect(poseStack, left + drawX + 1, bottomHeight, 30, 1, 1.0F, 0xFFFF00);
                        ((PaleoPadScreen) screen).drawScaledRect(poseStack, left + 6, actualY + 5, 30, 1, 1.0F, 0xFF0000);
                        ((PaleoPadScreen) screen).drawScaledRect(poseStack, left + 6, actualY + 23, 30, 1, 1.0F, 0xFF00FF);
                    }
                }
            }
            ((PaleoPadScreen) screen).drawScaledText(poseStack, String.valueOf(score), left + 5, top + 5, 1.0F, 0xFFFFFF);
        }
    }

    @Override
    public void actionPerformed(Button button) {
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, Screen screen) {
        int left = screen.width / 2 - 115;
        int top = 65;
        int relX = (int) mouseX - left;
        int relY = (int) mouseY - top;

        if (mainScreen) {
            if (relX > 10 && relX < 42 && relY > 80 && relY < 112) {
                charIndex = (charIndex + 1) % CHARACTERS.length;
                character = CHARACTERS[charIndex];
                ((FlappyDinoApp) app).setSelectedCharacter(charIndex);
            } else if (relX > 90 && relX < 140 && relY > 100 && relY < 120) {
                mainScreen = false;
            }
        } else {
            motionY = 6;
        }
    }

    @Override
    public void init() {
        x = 0;
        y = 150;
        motionY = 0;
        score = 0;
        pillars.clear();
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            pillars.put((i * 70) + 70, rand.nextInt(5));
        }
        FlappyDinoApp fApp = (FlappyDinoApp) app;
        charIndex = fApp.getSelectedCharacter();
        if (charIndex < 0 || charIndex >= CHARACTERS.length) {
            charIndex = 0;
        }
        character = CHARACTERS[charIndex];
        mainScreen = true;
    }

    @Override
    public void update() {
        if (!mainScreen && mc.player != null && mc.player.tickCount % 2 == 0) {
            x++;
            score = x / 70;

            if (y > 140) {
                y = 140;
            }

            if (motionY < -5) {
                motionY = -5;
            }

            y += motionY;
            motionY--;

            boolean died = false;
            for (Map.Entry<Integer, Integer> entry : pillars.entrySet()) {
                int renderX = entry.getKey() - x;
                int height = entry.getValue();
                int bottomHeight = (150 - (height * 20)) - 11;
                int topHeight = ((4 - height) * 20) + 11;
                int actualY = 150 - y;

                if (renderX > 0 && renderX < 200) {
                    boolean collideX = renderX < 30;
                    boolean collideY = (actualY + 5) < topHeight || (actualY + 23) > bottomHeight;

                    if (collideX && collideY) {
                        died = true;
                        break;
                    }
                }
            }

            if (y < 20) {
                died = true;
            }

            if (died) {
                ((FlappyDinoApp) app).addScore(score);
                init();
            }
        }
    }

    @Override
    public ResourceLocation getTexture(Screen screen) {
        return TEXTURE;
    }
}
