package net.vit.jurassicreborn.client.render.entity;


import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.items.misc.SkeletonPoseHelper;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class DinosaurRenderInfo{
    private static TabulaModel DEFAULT_EGG_MODEL;
    private static ResourceLocation DEFAULT_EGG_TEXTURE;

    static {
        try {
            DEFAULT_EGG_MODEL = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicreborn/models/entities/egg/tyrannosaurus"));
            DEFAULT_EGG_TEXTURE = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/entities/egg/tyrannosaurus.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





        private final Dinosaur dinosaur;
        private final EntityAnimator<?> animator;
        private final AnimatableModel modelAdult;
        private final AnimatableModel modelInfant;
        private final AnimatableModel modelJuvenile;
        private final AnimatableModel modelAdolescent;
        private final List<AnimatableModel> modelSkeletons;
        private TabulaModel eggModel;
        private ResourceLocation eggTexture;
//        private float shadowSize = 0.65F;

        public DinosaurRenderInfo(Dinosaur dinosaur, EntityAnimator<?> animator/*, float shadowSize*/) {
//            super(p_174017_, p_174018_, p_174019_, p_174020_, p_174021_);
            this.dinosaur = dinosaur;
            this.animator = animator;
//            this.shadowSize = shadowSize;

            this.modelAdult = this.loadModel(GrowthStage.ADULT);
            this.modelInfant = this.loadModel(GrowthStage.INFANT);
            this.modelJuvenile = this.loadModel(GrowthStage.JUVENILE);
            this.modelAdolescent = this.loadModel(GrowthStage.ADOLESCENT);
            this.modelSkeletons = this.loadSkeletonModels();

            try {
                String name = dinosaur.getName().toLowerCase(Locale.ENGLISH);
                this.eggModel = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicreborn/models/entities/egg/" + name));
                this.eggTexture = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/entities/egg/" + name + ".png");
            } catch (Exception e) {
                this.eggModel = DEFAULT_EGG_MODEL;
                this.eggTexture = DEFAULT_EGG_TEXTURE;
            }
        }

        public BasicEntityModel<DinosaurEntity> getModel(GrowthStage stage, byte skeletonVariant) {
            switch (stage) {
                case INFANT:
                    return this.modelInfant;
                case JUVENILE:
                    return this.modelJuvenile;
                case ADOLESCENT:
                    return this.modelAdolescent;
                case SKELETON:
                    int idx = Byte.toUnsignedInt(skeletonVariant);
                    if (idx >= this.modelSkeletons.size()) {
                        idx = 0;
                    }
                    return this.modelSkeletons.get(idx);
                default:
                    return this.modelAdult;
            }
        }

        public Model getEggModel() {
            return this.eggModel;
        }

        public ResourceLocation getEggTexture() {
            return this.eggTexture;
        }

        public EntityAnimator<?> getModelAnimator(GrowthStage stage) {
            if (stage == GrowthStage.SKELETON) {
                return null;
            }
            return this.animator;
        }

//        public float getShadowSize() {
//            return this.shadowSize;
//        }

        public AnimatableModel loadModel(GrowthStage stage) {
            if (!this.dinosaur.doesSupportGrowthStage(stage)) {
                return this.getModelAdult();
            }
            return new AnimatableModel(this.dinosaur.getModelContainer(stage), this.getModelAnimator(stage));
        }
    private List<AnimatableModel> loadSkeletonModels() {
        List<AnimatableModel> models = new ArrayList<>();
        List<TabulaModelContainer> containers = SkeletonPoseHelper.getPoseModels(this.dinosaur);
        for (TabulaModelContainer container : containers) {
            models.add(new AnimatableModel(container, null));
        }
        return models;
    }
        public Dinosaur getDinosaur() {
            return this.dinosaur;
        }
        public AnimatableModel getModelAdult() {
            return this.modelAdult;
        }




//    @Override
//    public EntityRenderer<DinosaurEntity> create(EntityRendererProvider.Context ctx) {// in legacy code this was <? extends DinosaurEntity>... this could cause a problem. - gamma
////        Context dCtx = (Context) ctx;
//        return new DinosaurRenderer(ctx, this.getModelAdult(), 0.5f);
//
//    }


}