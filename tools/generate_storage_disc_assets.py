from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "src" / "main" / "resources" / "assets" / "jurassicreborn"
OVERLAY_DIR = ASSETS / "textures" / "item" / "overlay"
PLANT_OVERLAY_DIR = OVERLAY_DIR / "plants"
MODEL_BASE = ASSETS / "models" / "item" / "storage_disc"

DINOSAUR_PREFIX = "item/overlay/"
PLANT_PREFIX = "item/overlay/plants/"

DINOSAUR_START = 1
PLANT_START = 1001


def gather_overlays(directory: Path) -> list[Path]:
    return sorted([p for p in directory.glob("*.png") if p.is_file()])


def ensure_dir(path: Path) -> None:
    path.mkdir(parents=True, exist_ok=True)


def write_json(path: Path, data: dict) -> None:
    ensure_dir(path.parent)
    with path.open("w", encoding="utf-8") as f:
        json.dump(data, f, indent=2)
        f.write("\n")


def register_model(model_ids: dict[str, int], model_paths: dict[int, str], key: str, value: int, model_path: str) -> None:
    model_ids[key] = value
    normalized = key.lower()
    model_ids[normalized] = value
    model_paths[value] = model_path


def generate_models() -> dict[str, int]:
    model_ids: dict[str, int] = {}
    model_paths: dict[int, str] = {}

    dino_files = gather_overlays(OVERLAY_DIR)
    plant_files = gather_overlays(PLANT_OVERLAY_DIR)

    value = DINOSAUR_START
    for png in dino_files:
        name = png.stem
        key = f"{DINOSAUR_PREFIX}{name}"
        model_path = MODEL_BASE / "dinosaurs" / f"{name}.json"
        write_json(
            model_path,
            {
                "parent": "item/generated",
                "textures": {
                    "layer0": "jurassicreborn:item/storage_disc",
                    "layer1": f"jurassicreborn:{DINOSAUR_PREFIX}{name}"
                }
            },
        )
        register_model(model_ids, model_paths, key, value, f"jurassicreborn:item/storage_disc/dinosaurs/{name}")
        value += 1

    value = PLANT_START
    for png in plant_files:
        name = png.stem
        key = f"{PLANT_PREFIX}{name}"
        model_path = MODEL_BASE / "plants" / f"{name}.json"
        write_json(
            model_path,
            {
                "parent": "item/generated",
                "textures": {
                    "layer0": "jurassicreborn:item/storage_disc",
                    "layer1": f"jurassicreborn:{PLANT_PREFIX}{name}"
                }
            },
        )
        register_model(model_ids, model_paths, key, value, f"jurassicreborn:item/storage_disc/plants/{name}")
        value += 1

    write_json(
        ASSETS / "models" / "item" / "storage_disc.json",
        {
            "parent": "item/generated",
            "textures": {"layer0": "jurassicreborn:item/storage_disc"}
        },
    )

    return model_ids, model_paths


def generate_java(model_ids: dict[str, int], model_paths: dict[int, str]) -> None:
    lines: list[str] = []
    lines.append("package net.vit.jurassicreborn.common.items.genetics;")
    lines.append("")
    lines.append("import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;")
    lines.append("import it.unimi.dsi.fastutil.ints.Int2ObjectMap;")
    lines.append("import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;")
    lines.append("import it.unimi.dsi.fastutil.objects.Object2IntMap;")
    lines.append("import net.minecraft.resources.ResourceLocation;")
    lines.append("import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;")
    lines.append("")
    lines.append("import java.util.Locale;")
    lines.append("")
    lines.append("public final class StorageDiscModelData {")
    lines.append("    private static final Object2IntMap<String> MODEL_IDS = new Object2IntArrayMap<>();")
    lines.append("    private static final Int2ObjectMap<ResourceLocation> MODELS = new Int2ObjectArrayMap<>();")
    lines.append("")
    lines.append("    static {")
    for key, value in model_ids.items():
        lines.append(f'        MODEL_IDS.put("{key}", {value});')
    for key, value in model_paths.items():
        lines.append(f'        MODELS.put({key}, new ResourceLocation("{value}"));')
    lines.append("    }")
    lines.append("")
    lines.append("    private StorageDiscModelData() {")
    lines.append("    }")
    lines.append("")
    lines.append("    public static int resolveDinosaur(Dinosaur dinosaur) {")
    lines.append("        if (dinosaur == null || dinosaur == Dinosaur.EMPTY) {")
    lines.append("            return 0;")
    lines.append("        }")
    lines.append("        String formatted = normalize(dinosaur.getFormattedName());")
    lines.append("        if (formatted != null) {")
    lines.append("            int id = MODEL_IDS.getInt(\"item/overlay/\" + formatted);")
    lines.append("            if (id != 0) {")
    lines.append("                return id;")
    lines.append("            }")
    lines.append("        }")
    lines.append("        String name = normalize(dinosaur.getName());")
    lines.append("        if (name != null) {")
    lines.append("            int id = MODEL_IDS.getInt(\"item/overlay/\" + name);")
    lines.append("            if (id != 0) {")
    lines.append("                return id;")
    lines.append("            }")
    lines.append("        }")
    lines.append("        return 0;")
    lines.append("    }")
    lines.append("")
    lines.append("    public static int resolvePlant(ResourceLocation plantId) {")
    lines.append("        if (plantId == null) {")
    lines.append("            return 0;")
    lines.append("        }")
    lines.append("        int id = MODEL_IDS.getInt(\"item/overlay/plants/\" + plantId.getPath());")
    lines.append("        if (id != 0) {")
    lines.append("            return id;")
    lines.append("        }")
    lines.append("        String formatted = normalize(plantId.getPath());")
    lines.append("        if (formatted != null) {")
    lines.append("            return MODEL_IDS.getInt(\"item/overlay/plants/\" + formatted);")
    lines.append("        }")
    lines.append("        return 0;")
    lines.append("    }")
    lines.append("")
    lines.append("    private static String normalize(String value) {")
    lines.append("        if (value == null || value.isEmpty()) {")
    lines.append("            return null;")
    lines.append("        }")
    lines.append("        return value.toLowerCase(Locale.ROOT).replace(' ', '_');")
    lines.append("    }")
    lines.append("")
    lines.append("    public static ResourceLocation getModelLocation(int modelId) {")
    lines.append("        return MODELS.get(modelId);")
    lines.append("    }")
    lines.append("")
    lines.append("    public static Int2ObjectMap<ResourceLocation> getModels() {")
    lines.append("        return MODELS;")
    lines.append("    }")
    lines.append("}")

    java_path = ROOT / "src" / "main" / "java" / "net" / "vit" / "jurassicreborn" / "common" / "items" / "genetics" / "StorageDiscModelData.java"
    ensure_dir(java_path.parent)
    with java_path.open("w", encoding="utf-8") as f:
        f.write("\n".join(lines))
        f.write("\n")


def main() -> None:
    model_ids, model_paths = generate_models()
    generate_java(model_ids, model_paths)


if __name__ == "__main__":
    main()
