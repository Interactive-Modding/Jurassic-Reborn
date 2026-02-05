package net.vit.jurassicreborn.client.model.animation.dto;

import com.google.gson.*;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class AnimatableRenderDefDTO {
    public int version;
    public Map<GrowthStage, GrowthRenderDef> perStage;

    public static class AnimatableDeserializer implements JsonDeserializer<AnimatableRenderDefDTO> {
        @Override
        public AnimatableRenderDefDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject def = json.getAsJsonObject();
            AnimatableRenderDefDTO built = new AnimatableRenderDefDTO();
            built.version = def.get("version") == null ? 0 : def.get("version").getAsInt();
            built.perStage = new EnumMap<>(GrowthStage.class);
            for (GrowthStage g : GrowthStage.VALUES) {
                JsonElement perhaps = def.get(g.name());
                GrowthRenderDef renderDef = perhaps == null ? new GrowthRenderDef() : context.deserialize(perhaps, GrowthRenderDef.class);
                if (renderDef.directory == null || renderDef.directory.isEmpty()) {
                    renderDef.directory = g.name().toLowerCase(Locale.ROOT);
                }
                built.perStage.put(g, renderDef);
            }
            return built;
        }
    }
}