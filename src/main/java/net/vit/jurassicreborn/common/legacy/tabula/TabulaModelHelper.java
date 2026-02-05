package net.vit.jurassicreborn.common.legacy.tabula;

import com.github.alexthe666.citadel.client.model.container.TabulaCubeContainer;
import com.github.alexthe666.citadel.client.model.container.TabulaCubeGroupContainer;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabulaModelHelper {



    public static TabulaCubeContainer getCubeByName(String name, TabulaModelContainer model) {
        List<TabulaCubeContainer> allCubes = getAllCubes(model);

        for (TabulaCubeContainer cube : allCubes) {
            if (cube.getName().equals(name)) {
                return cube;
            }
        }

        return null;
    }

    public static TabulaCubeContainer getCubeByIdentifier(String identifier, TabulaModelContainer model) {
        List<TabulaCubeContainer> allCubes = getAllCubes(model);

        for (TabulaCubeContainer cube : allCubes) {
            if (cube.getIdentifier().equals(identifier)) {
                return cube;
            }
        }

        return null;
    }

    public static List<TabulaCubeContainer> getAllCubes(TabulaModelContainer model) {
        List<TabulaCubeContainer> cubes = new ArrayList<>();

        try {
            for (TabulaCubeGroupContainer cubeGroup : model.getCubeGroups()) {
                cubes.addAll(traverse(cubeGroup));
            }

            for (TabulaCubeContainer cube : model.getCubes()) {
                cubes.addAll(traverse(cube));
            }
        } catch (Exception e) {
            //tOdO: logger
//            jurassicreborn.getLogger().error("Failed to load all cubes for model {} by {}", model.getName(), model.getAuthor(), e);
        }

        return cubes;
    }

    private static List<TabulaCubeContainer> traverse(TabulaCubeGroupContainer group) {
        List<TabulaCubeContainer> retCubes = new ArrayList<>();

        for (TabulaCubeContainer child : group.getCubes()) {
            retCubes.addAll(traverse(child));
        }

        for (TabulaCubeGroupContainer child : group.getCubeGroups()) {
            retCubes.addAll(traverse(child));
        }

        return retCubes;
    }

    private static List<TabulaCubeContainer> traverse(TabulaCubeContainer cube) {
        List<TabulaCubeContainer> retCubes = new ArrayList<>();

        retCubes.add(cube);

        for (TabulaCubeContainer child : cube.getChildren()) {
            retCubes.addAll(traverse(child));
        }

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
        if(stream == null){
//            URL test = TabulaModelHelper.class.getResource(path);
//            JurassicReborn.getLogger().error("NULL STREAM FOR PATH " + path);
//            JurassicReborn.getLogger().error("REAL PATH: " + test.getPath());
            return null;
        }
        return TabulaModelHelper.loadTabulaModel(getModelJsonStream(path, stream));
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

        String path = "/assets/" + location.getNamespace() + "/" + location.getPath() + ".tbl";
        try (InputStream stream = TabulaModelHelper.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Could not find tabula model at " + location);
            }
            return TabulaModelHelper.loadTabulaModel(getModelJsonStream(path, stream));
        }
    }
}