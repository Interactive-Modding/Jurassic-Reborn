package net.vit.jurassicreborn.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldGuideScreen extends Screen {
    private final int imageWidth = 256;
    private final int imageHeight = 192;
    private int currentIndex = 0;
    private List<Dinosaur> dinosaurs;

    private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation("jurassicreborn", "textures/field_guide/background.png");
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/journal/widgets.png");
    private final Map<Dinosaur, DinosaurEntity> skeletonCache = new HashMap<>();

    public FieldGuideScreen() {
        super(Component.literal("InGen Field Guide"));
        this.dinosaurs = new ArrayList<>(Dinosaur.DINOSAUR_IDS.keySet());
        this.dinosaurs.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }

    @Override
    protected void init() {
        super.init();
        int x = leftPos();
        int y = topPos();

        addRenderableWidget(new ArrowButton(x - 3, y + 180, false, btn -> switchEntry(-1)));
        addRenderableWidget(new ArrowButton(x + 235, y + 180, true, btn -> switchEntry(1)));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
        blit(poseStack, leftPos(), topPos(), 0, 0, imageWidth, imageHeight);
        super.render(poseStack, mouseX, mouseY, partialTicks);

        if (currentIndex >= 0 && currentIndex < dinosaurs.size()) {
            Dinosaur dino = dinosaurs.get(currentIndex);
            int xLeft = leftPos() + 24;
            int y = topPos() + 20;
            int wrapWidth = 90;

            List<FormattedCharSequence> nameLines = font.split(Component.literal((currentIndex + 1) + ". " + dino.getName()), wrapWidth);
            for (int i = 0; i < nameLines.size(); i++) {
                font.draw(poseStack, nameLines.get(i), xLeft, y + i * 10, 0x3E3E3E);
            }
            font.draw(poseStack, "Scientific Name:", xLeft, y + 30, 0x3E3E3E);

            List<FormattedCharSequence> sciLines = font.split(Component.literal(dino.getScientificName()), wrapWidth);
            for (int i = 0; i < sciLines.size(); i++) {
                font.draw(poseStack, sciLines.get(i), xLeft + 5, y + 40 + i * 10, 0x555555);
            }

            int offsetY = y + 15 + sciLines.size() * 10 + 5;

            List<FormattedCharSequence> periodLines = font.split(Component.literal("Period: " + dino.getPeriod()), wrapWidth);
            for (int i = 0; i < periodLines.size(); i++) {
                font.draw(poseStack, periodLines.get(i), xLeft, offsetY + 30 + i * 10, 0x3E3E3E);
            }

            List<FormattedCharSequence> familyLines = font.split(Component.literal("Family: " + dino.getFamily()), wrapWidth);
            for (int i = 0; i < familyLines.size(); i++) {
                font.draw(poseStack, familyLines.get(i), xLeft, offsetY + 50 + i * 10, 0x3E3E3E);
            }

            List<FormattedCharSequence> locationLines = font.split(Component.literal("Location: " + dino.getLocation()), wrapWidth);
            for (int i = 0; i < locationLines.size(); i++) {
                font.draw(poseStack, locationLines.get(i), xLeft, offsetY + 80 + i * 10, 0x3E3E3E);
            }

            String diet = "Unknown";
            if (dino.getDiet() != null && !dino.getDiet().getModules().isEmpty()) {
                FoodType type = dino.getDiet().getModules().get(0).getFoodType();
                if (type != null) {
                    diet = type.name().charAt(0) + type.name().substring(1).toLowerCase();
                }
            }
            font.draw(poseStack, "Diet: " + diet, xLeft, offsetY + 100, 0x3E3E3E);

            int skeletonX = leftPos() + imageWidth - 72;
            int skeletonY = topPos() + imageHeight  - 55;
            int skeletonScale = 15;
            if (dino == DinosaurHandler.CAMARASAURUS ||
                    dino == DinosaurHandler.NIGERSAURUS ||
                    dino == DinosaurHandler.MAMENCHISAURUS ||
                    dino == DinosaurHandler.BRACHIOSAURUS ||
                    dino == DinosaurHandler.ANKYLODOCUS ||
                    dino == DinosaurHandler.DREADNOUGHTUS ||
                    dino == DinosaurHandler.GIGANOTOSAURUS ||
                    dino == DinosaurHandler.SPINOSAURUS ||
                    dino == DinosaurHandler.STEGOSAURUS ||
                    dino == DinosaurHandler.RAPHUSREX ||
                    dino == DinosaurHandler.APATOSAURUS ||
                    dino == DinosaurHandler.TYLOSAURUS ||
                    dino == DinosaurHandler.THERIZINOSAURUS ||
                    dino == DinosaurHandler.LAMBEOSAURUS ||
                    dino == DinosaurHandler.EDMONTOSAURUS ||
                    dino == DinosaurHandler.CORYTHOSAURUS ||
                    dino == DinosaurHandler.INDOMINUS ||
                    dino == DinosaurHandler.DIPLODOCUS) {
                skeletonScale = 5;
            }
            if (dino == DinosaurHandler.MOSASAURUS ) {
                skeletonScale = 3;
            }
            if (dino == DinosaurHandler.ALLIGATOR_GAR ||
                    dino == DinosaurHandler.ALVAREZSAURUS ||
                    dino == DinosaurHandler.ASTEROCERAS ||
                    dino == DinosaurHandler.MEGALODON ||
                    dino == DinosaurHandler.BEELZEBUFO ||
                    dino == DinosaurHandler.DIMORPHODON ||
                    dino == DinosaurHandler.DODO ||
                    dino == DinosaurHandler.LEAELLYNASAURA ||
                    dino == DinosaurHandler.MEGAPIRANHA ||
                    dino == DinosaurHandler.PROTOCERATOPS ||
                    dino == DinosaurHandler.SEGISAURUS ||
                    dino == DinosaurHandler.TROODON ||
                    dino == DinosaurHandler.HYPSILOPHODON) {
                skeletonScale = 30;
            }
            if (dino == DinosaurHandler.DIPLOCAULUS ||
                    dino == DinosaurHandler.ALVAREZSAURUS ||
                    dino == DinosaurHandler.MICROCERATUS ||
                    dino == DinosaurHandler.MICRORAPTOR ||
                    dino == DinosaurHandler.OTHNIELIA ||
                    dino == DinosaurHandler.OVIRAPTOR) {
                skeletonScale = 30;
            }
            if (dino == DinosaurHandler.COMPSOGNATHUS ) {
                skeletonScale = 70;
            }
            if (dino == DinosaurHandler.ALLOSAURUS ||
                    dino == DinosaurHandler.CARNOTAURUS ||
                    dino == DinosaurHandler.CERATOSAURUS ||
                    dino == DinosaurHandler.DEINOTHERIUM ||
                    dino == DinosaurHandler.DUNKLEOSTEUS ||
                    dino == DinosaurHandler.TYRANNOSAURUS ||
                    dino == DinosaurHandler.MAMMOTH ||
                    dino == DinosaurHandler.PARASAUROLOPHUS ||
                    dino == DinosaurHandler.QUETZAL ||
                    dino == DinosaurHandler.SUCHOMIMUS ||
                    dino == DinosaurHandler.PARACERATHERIUM) {
                skeletonScale = 10;
            }
            if (dino == DinosaurHandler.ANKYLOSAURUS ||
                    dino == DinosaurHandler.CARCHARODONTOSAURUS) {
                skeletonScale = 8;
            }
            if (dino == DinosaurHandler.SPINORAPTOR ) {
                skeletonScale = 12;
            }
            renderSkeletonModel(poseStack, skeletonX, skeletonY, skeletonScale, dino);
        }
    }

    private void renderSkeletonModel(PoseStack stack, int x, int y, int scale, Dinosaur dino) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        RegistryObject<? extends EntityType<? extends DinosaurEntity>> reg =
                DinosaurEntity.CLASS_TYPE_LIST.get(dino.getDinosaurClass());
        if (reg == null) {
            return;
        }

        DinosaurEntity entity = skeletonCache.computeIfAbsent(dino, key -> {
            DinosaurEntity e = (DinosaurEntity) reg.get().create(mc.level);
            if (e != null) {
                e.setAge(dino.getMaximumAge());
                e.setSkeleton(true);
                e.setSkeletonVariant((byte) 0);
                e.setIsFossile(true);
                boolean hasMaleSkeleton = !dino.isHybrid;
                e.setIsFossile(hasMaleSkeleton);
                e.setupDisplay(hasMaleSkeleton);
                e.setAnimation(EntityAnimation.IDLE.get());
                e.setNoAi(true);
            }
            return e;
        });
        if (entity == null) {
            return;
        }

        entity.setAnimationTick(0);

        stack.pushPose();
        stack.translate(x, y, 100);
        stack.scale(-scale, scale, scale);
        stack.mulPose(Axis.ZP.rotationDegrees(180f));
        stack.mulPose(Axis.YP.rotationDegrees(-135f));
        stack.mulPose(Axis.XP.rotationDegrees(-10f));
        dispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, stack, mc.renderBuffers().bufferSource(), 15728880));
        dispatcher.setRenderShadow(true);
        mc.renderBuffers().bufferSource().endBatch();
        stack.popPose();
    }

    private void switchEntry(int delta) {
        currentIndex = (currentIndex + delta + dinosaurs.size()) % dinosaurs.size();
    }

    private int leftPos() {
        return (width - imageWidth) / 2;
    }

    private int topPos() {
        return (height - imageHeight) / 2;
    }


    private class ArrowButton extends Button {
        private final boolean isForward;

        public ArrowButton(int x, int y, boolean isForward, OnPress onPress) {
            super(x, y, 23, 13, Component.empty(), onPress, DEFAULT_NARRATION);
            this.isForward = isForward;
        }
        @Override
        public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
                int u = this.isHovered ? 23 : 0;
                int v = this.isForward ? 194 : 207;
                blit(poseStack, this.getX(), this.getY(), u, v, this.width, this.height);
            }
        }
    }
}
