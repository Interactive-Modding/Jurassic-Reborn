package mod.reborn.server.tabula;

import com.google.gson.Gson;
import mod.reborn.RebornMod;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaCubeGroupContainer;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabulaModelHelper {

    public static TabulaCubeContainer getCubeByName(String name, TabulaModelContainer model) {
        return getAllCubes(model).stream()
                .filter(cube -> cube.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static TabulaCubeContainer getCubeByIdentifier(String identifier, TabulaModelContainer model) {
        return getAllCubes(model).stream()
                .filter(cube -> cube.getIdentifier().equals(identifier))
                .findFirst()
                .orElse(null);
    }

    public static List<TabulaCubeContainer> getAllCubes(TabulaModelContainer model) {
        List<TabulaCubeContainer> cubes = new ArrayList<>();
        try {
            model.getCubeGroups().forEach(cubeGroup -> cubes.addAll(traverse(cubeGroup)));
            model.getCubes().forEach(cube -> cubes.addAll(traverse(cube)));
        } catch (Exception e) {
            RebornMod.getLogger().error("Failed to load all cubes for model {} by {}", model.getName(), model.getAuthor(), e);
        }
        return cubes;
    }

    private static List<TabulaCubeContainer> traverse(TabulaCubeGroupContainer group) {
        List<TabulaCubeContainer> retCubes = new ArrayList<>();
        group.getCubes().forEach(child -> retCubes.addAll(traverse(child)));
        group.getCubeGroups().forEach(child -> retCubes.addAll(traverse(child)));
        return retCubes;
    }

    private static List<TabulaCubeContainer> traverse(TabulaCubeContainer cube) {
        List<TabulaCubeContainer> retCubes = new ArrayList<>();
        retCubes.add(cube);
        cube.getChildren().forEach(child -> retCubes.addAll(traverse(child)));
        return retCubes;
    }

    public static TabulaModelContainer loadTabulaModel(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith(".tbl")) {
            path += ".tbl";
        }
        InputStream stream = TabulaModelHelper.class.getResourceAsStream(path);
        return loadTabulaModel(getModelJsonStream(path, stream));
    }

    public static TabulaModelContainer loadTabulaModel(InputStream stream) {
        return new Gson().fromJson(new InputStreamReader(stream), TabulaModelContainer.class);
    }

    private static InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return zip;
            }
        }
        throw new RuntimeException("No model.json present in " + name);
    }

    public static TabulaModelContainer loadTabulaModel(ResourceLocation location) throws IOException {
        String path = "/assets/" + location.getResourceDomain() + "/" + location.getResourcePath() + ".tbl";
        try (InputStream stream = TabulaModelHelper.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Could not find tabula model at " + location);
            }
            return loadTabulaModel(getModelJsonStream(path, stream));
        }
    }
}
