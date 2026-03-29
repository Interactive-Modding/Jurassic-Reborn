package net.vit.jurassicreborn.client.render.entity.animation;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.model.animation.dto.AnimatableRenderDefDTO;
import net.vit.jurassicreborn.client.model.animation.dto.AnimationsDTO;
import net.vit.jurassicreborn.client.model.animation.dto.PoseDTO;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PoseHandler<ENTITY extends LivingEntity & Animatable> {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AnimatableRenderDefDTO.class, new AnimatableRenderDefDTO.AnimatableDeserializer())
            .create();

    private Map<GrowthStage, ModelData> modelData;

    public PoseHandler(Dinosaur dinosaur) {
        this(dinosaur.getName(), dinosaur.getSupportedStages());
    }

    public PoseHandler(String name, List<GrowthStage> supported) {
        name = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        this.modelData = new EnumMap<>(GrowthStage.class);
        URI entityResource;
        try {
            entityResource = new URI("/assets/jurassicreborn/models/entities/" + name + "/");
        } catch (URISyntaxException e) {
            JurassicReborn.getLogger().fatal("Illegal URI /assets/jurassicreborn/models/entities/" + name + "/", e);
            return;
        }
        for (GrowthStage growth : GrowthStage.values()) {
            try {
                GrowthStage actualGrowth = growth;
                if (!supported.contains(actualGrowth)) {
                    actualGrowth = GrowthStage.ADULT;
                }
                if (this.modelData.containsKey(actualGrowth)) {
                    this.modelData.put(growth, this.modelData.get(actualGrowth));
                } else {
                    ModelData loaded = this.loadModelData(entityResource, name, actualGrowth);
                    this.modelData.put(growth, loaded);
                    if (actualGrowth != growth) {
                        this.modelData.put(actualGrowth, loaded);
                    }
                }
            } catch (Exception e) {
                JurassicReborn.getLogger().fatal("Failed to parse growth stage " + growth + " for dinosaur " + name, e);
                this.modelData.put(growth, new ModelData());
            }
        }
    }

    private ModelData loadModelData(URI resourceURI, String name, GrowthStage growth) {
        String growthName = growth.name().toLowerCase(Locale.ROOT);
        URI growthSensitiveDir = resourceURI.resolve(growthName + "/");
        URI definitionFile = growthSensitiveDir.resolve(name + "_" + growthName + ".json");
        InputStream modelStream = TabulaModelHelper.class.getResourceAsStream(definitionFile.toString());
        if (modelStream == null) {
            throw new IllegalArgumentException("No model definition for the dino " + name + " with grow-state " + growth + " exists. Expected at " + definitionFile);
        }
        try {
            Reader reader = new InputStreamReader(modelStream);
            AnimationsDTO rawAnimations = GSON.fromJson(reader, AnimationsDTO.class);
            ModelData data = this.loadModelData(growthSensitiveDir, rawAnimations);
            JurassicReborn.getLogger().debug("Successfully loaded " + name + "(" + growth + ") from " + definitionFile);
            reader.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ModelData loadModelData(URI resourceURI, AnimationsDTO animationsDefinition) {
        if (animationsDefinition == null || animationsDefinition.poses == null
                || animationsDefinition.poses.get(EntityAnimation.IDLE.name()) == null
                || animationsDefinition.poses.get(EntityAnimation.IDLE.name()).length == 0) {
            throw new IllegalArgumentException("Animation files must define at least one pose for the IDLE animation");
        }
        List<String> posedModelResources = new ArrayList<>();
        for (PoseDTO[] poses : animationsDefinition.poses.values()) {
            if (poses == null) {
                continue;
            }
            for (PoseDTO pose : poses) {
                if (pose == null) {
                    continue;
                }
                if (pose.pose == null) {
                    throw new IllegalArgumentException("Every pose must define a pose file");
                }
                String resolvedRes = this.resolve(resourceURI, pose.pose);
                int index = posedModelResources.indexOf(resolvedRes);
                if (index == -1) {
                    pose.index = posedModelResources.size();
                    posedModelResources.add(resolvedRes);
                } else {
                    pose.index = index;
                }
            }
        }

        Map<Animation, float[][]> animations = new HashMap<>();

        for (Map.Entry<String, PoseDTO[]> entry : animationsDefinition.poses.entrySet()) {
            Animation animation = EntityAnimation.valueOf(entry.getKey()).get();
            PoseDTO[] poses = entry.getValue();
            float[][] poseSequence = new float[poses.length][2];
            for (int i = 0; i < poses.length; i++) {
                poseSequence[i][0] = poses[i].index;
                poseSequence[i][1] = poses[i].time;
            }
            animations.put(animation, poseSequence);
        }

        if (FMLEnvironment.dist.isClient()) {
            return this.loadModelDataClient(posedModelResources, animations);
        }

        return new ModelData(animations);
    }

    @OnlyIn(Dist.CLIENT)
    private ModelData loadModelDataClient(
            List<String> posedModelResources,
            Map<Animation, float[][]> animations
    ) {
        PosedCuboid[][] posedCuboids = new PosedCuboid[posedModelResources.size()][];

        AnimatableModel mainModel = (AnimatableModel)
                JabelarAnimationHandler.loadModel(posedModelResources.get(0));

        if (mainModel == null) {
            throw new IllegalArgumentException(
                    "Couldn't load the model from " + posedModelResources.get(0)
            );
        }

        // Use identifierMap directly
        String[] identifiers = mainModel.getCubeIdentifierArray();
        int partCount = identifiers.length;

        for (int i = 0; i < posedModelResources.size(); i++) {
            String resource = posedModelResources.get(i);
            AnimatableModel model = (AnimatableModel)
                    JabelarAnimationHandler.loadModel(resource);

            if (model == null) {
                throw new IllegalArgumentException(
                        "Couldn't load the model from " + resource
                );
            }

            PosedCuboid[] pose = new PosedCuboid[partCount];

            for (int partIndex = 0; partIndex < partCount; partIndex++) {
                String identifier = identifiers[partIndex];

                AdvancedModelBox cube =
                        model.getIdentifierCubes().get(identifier);

                AdvancedModelBox mainCube =
                        mainModel.getIdentifierCubes().get(identifier);

                pose[partIndex] = new PosedCuboid(
                        cube != null ? cube : mainCube
                );
            }

            posedCuboids[i] = pose;
        }

        return new ModelData(posedCuboids, animations);
    }


    private String resolve(URI dinoDirURI, String posePath) {
        URI uri = dinoDirURI.resolve(posePath);
        return uri.toString();
    }

            @OnlyIn(Dist.CLIENT)
            public JabelarAnimationHandler<ENTITY> createAnimationHandler(ENTITY entity,
                                                                          AnimatableModel model,
                                                                          GrowthStage stage,
                                                                          boolean useInertia) {
                ModelData data = this.modelData.get(stage);
                return new JabelarAnimationHandler<>(entity, model, data.poses, data.animations, useInertia);
            }

    public Map<Animation, float[][]> getAnimations(GrowthStage growthStage) {
        return this.modelData.get(growthStage).animations;
    }

    public float getAnimationLength(Animation animation, GrowthStage growthStage) {
        Map<Animation, float[][]> animations = this.getAnimations(growthStage);

        float duration = 0;

        if (animation != null) {
            float[][] poses = animations.get(animation);

            if (poses != null) {
                for (float[] pose : poses) {
                    duration += pose[1];
                }
            }
        }

        return duration;
    }

    public boolean hasAnimation(Animation animation, GrowthStage growthStage) {
        return this.modelData.get(growthStage).animations.get(animation) != null;
    }

    private class ModelData {
                @OnlyIn(Dist.CLIENT)

        PosedCuboid[][] poses;

        Map<Animation, float[][]> animations;

        public ModelData() {
            this(null);
        }

                @OnlyIn(Dist.CLIENT)

        public ModelData(PosedCuboid[][] cuboids, Map<Animation, float[][]> animations) {
            this(animations);

            if (cuboids == null) {
                cuboids = new PosedCuboid[0][];
            }

            this.poses = cuboids;
        }

        public ModelData(Map<Animation, float[][]> animations) {
            if (animations == null) {
                animations = new LinkedHashMap<>();
            }

            this.animations = animations;
        }
    }
}
