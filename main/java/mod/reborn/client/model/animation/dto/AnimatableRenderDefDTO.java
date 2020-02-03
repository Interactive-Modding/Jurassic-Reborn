package mod.reborn.client.model.animation.dto;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mod.reborn.server.entity.GrowthStage;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * Deserialize with the {@link AnimatableDeserializer} from {@link Gson}
 *
 * @author WorldSEnder
 */
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
