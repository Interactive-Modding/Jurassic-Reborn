package net.vit.jurassicreborn.common.items.genetics;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

import java.util.Locale;

public final class StorageDiscModelData {
    private static final Object2IntMap<String> MODEL_IDS = new Object2IntArrayMap<>();
    private static final Int2ObjectMap<ResourceLocation> MODELS = new Int2ObjectArrayMap<>();

    static {
        // MODEL IDS – keyed just by name (no "item/overlay/")
        MODEL_IDS.put("achillobator", 1);
        MODEL_IDS.put("alligator_gar", 2);
        MODEL_IDS.put("allosaurus", 3);
        MODEL_IDS.put("alvarezsaurus", 4);
        MODEL_IDS.put("ankylodocus", 5);
        MODEL_IDS.put("ankylosaurus", 6);
        MODEL_IDS.put("apatosaurus", 7);
        MODEL_IDS.put("arsinoitherium", 8);
        MODEL_IDS.put("asteroceras", 9);
        MODEL_IDS.put("baryonyx", 10);
        MODEL_IDS.put("beelzebufo", 11);
        MODEL_IDS.put("blue", 12);
        MODEL_IDS.put("brachiosaurus", 13);
        MODEL_IDS.put("calymene", 14);
        MODEL_IDS.put("camarasaurus", 15);
        MODEL_IDS.put("cameroceras", 16);
        MODEL_IDS.put("carcharodontosaurus", 17);
        MODEL_IDS.put("carnotaurus", 18);
        MODEL_IDS.put("cearadactylus", 19);
        MODEL_IDS.put("ceratosaurus", 20);
        MODEL_IDS.put("charlie", 21);
        MODEL_IDS.put("chasmosaurus", 22);
        MODEL_IDS.put("chilesaurus", 23);
        MODEL_IDS.put("coelacanth", 24);
        MODEL_IDS.put("coelurus", 25);
        MODEL_IDS.put("compsognathus", 26);
        MODEL_IDS.put("corythosaurus", 27);
        MODEL_IDS.put("crassigyrinus", 28);
        MODEL_IDS.put("deinosuchus", 29);
        MODEL_IDS.put("deinotherium", 30);
        MODEL_IDS.put("delta", 31);
        MODEL_IDS.put("dilophosaurus", 32);
        MODEL_IDS.put("dimetrodon", 33);
        MODEL_IDS.put("dimorphodon", 34);
        MODEL_IDS.put("diplocaulus", 35);
        MODEL_IDS.put("diplodocus", 36);
        MODEL_IDS.put("dodo", 37);
        MODEL_IDS.put("dreadnoughtus", 38);
        MODEL_IDS.put("dunkleosteus", 39);
        MODEL_IDS.put("echo", 40);
        MODEL_IDS.put("edmontosaurus", 41);
        MODEL_IDS.put("elasmotherium", 42);
        MODEL_IDS.put("endoceras", 43);
        MODEL_IDS.put("gallimimus", 44);
        MODEL_IDS.put("giganotosaurus", 45);
        MODEL_IDS.put("guanlong", 46);
        MODEL_IDS.put("herrerasaurus", 48);
        MODEL_IDS.put("hyaenodon", 49);
        MODEL_IDS.put("hypsilophodon", 50);
        MODEL_IDS.put("iguanodon", 51);
        MODEL_IDS.put("indominus", 52);
        MODEL_IDS.put("indoraptor", 53);
        MODEL_IDS.put("kairuku", 54);
        MODEL_IDS.put("lambeosaurus", 55);
        MODEL_IDS.put("leaellynasaura", 56);
        MODEL_IDS.put("leptictidium", 57);
        MODEL_IDS.put("livyatan", 58);
        MODEL_IDS.put("ludodactylus", 59);
        MODEL_IDS.put("maiasaura", 60);
        MODEL_IDS.put("majungasaurus", 61);
        MODEL_IDS.put("mamenchisaurus", 62);
        MODEL_IDS.put("mammoth", 63);
        MODEL_IDS.put("mawsonia", 64);
        MODEL_IDS.put("megalodon", 65);
        MODEL_IDS.put("megapiranha", 66);
        MODEL_IDS.put("megatherium", 67);
        MODEL_IDS.put("metriacanthosaurus", 68);
        MODEL_IDS.put("microceratus", 69);
        MODEL_IDS.put("microraptor", 70);
        MODEL_IDS.put("moganopterus", 71);
        MODEL_IDS.put("mosasaurus", 72);
        MODEL_IDS.put("mussaurus", 73);
        MODEL_IDS.put("nigersaurus", 74);
        MODEL_IDS.put("ornithomimus", 75);
        MODEL_IDS.put("orthoceras", 76);
        MODEL_IDS.put("othnielia", 77);
        MODEL_IDS.put("oviraptor", 78);
        MODEL_IDS.put("pachycephalosaurus", 79);
        MODEL_IDS.put("paraceratherium", 80);
        MODEL_IDS.put("parapuzosia", 81);
        MODEL_IDS.put("parasaurolophus", 82);
        MODEL_IDS.put("patagotitan", 83);
        MODEL_IDS.put("perisphinctes", 84);
        MODEL_IDS.put("postosuchus", 85);
        MODEL_IDS.put("proceratosaurus", 86);
        MODEL_IDS.put("protoceratops", 87);
        MODEL_IDS.put("pteranodon", 88);
        MODEL_IDS.put("quetzalcoatlus", 89);
        MODEL_IDS.put("raphusrex", 90);
        MODEL_IDS.put("rugops", 91);
        MODEL_IDS.put("segisaurus", 92);
        MODEL_IDS.put("sinoceratops", 93);
        MODEL_IDS.put("smilodon", 94);
        MODEL_IDS.put("spinoraptor", 95);
        MODEL_IDS.put("spinosaurus", 96);
        MODEL_IDS.put("stegosaurus", 97);
        MODEL_IDS.put("styracosaurus", 98);
        MODEL_IDS.put("suchomimus", 99);
        MODEL_IDS.put("therizinosaurus", 100);
        MODEL_IDS.put("titanis", 101);
        MODEL_IDS.put("titanites", 102);
        MODEL_IDS.put("triceratops", 103);
        MODEL_IDS.put("troodon", 104);
        MODEL_IDS.put("tropeognathus", 105);
        MODEL_IDS.put("tylosaurus", 106);
        MODEL_IDS.put("tyrannosaurus", 107);
        MODEL_IDS.put("vectipelta", 108);
        MODEL_IDS.put("velociraptor", 109);
        MODEL_IDS.put("zhenyuanopterus", 110);

        // PLANTS
        MODEL_IDS.put("ajuginucula_smithii", 1001);
        MODEL_IDS.put("araucaria", 1002);
        MODEL_IDS.put("aulopora", 1003);
        MODEL_IDS.put("bennettitalean", 1004);
        MODEL_IDS.put("bennettitalean_cycadeoidea", 1005);
        MODEL_IDS.put("bristle_fern", 1006);
        MODEL_IDS.put("calamites", 1007);
        MODEL_IDS.put("cinnamon_fern", 1008);
        MODEL_IDS.put("cladochonus", 1009);
        MODEL_IDS.put("cry_pansy", 1010);
        MODEL_IDS.put("cycad", 1011);
        MODEL_IDS.put("cycad_zamites", 1012);
        MODEL_IDS.put("dicksonia", 1013);
        MODEL_IDS.put("dicroidium_zuberi", 1014);
        MODEL_IDS.put("dictyophyllum", 1015);
        MODEL_IDS.put("enallhelia", 1016);
        MODEL_IDS.put("encephalartos", 1017);
        MODEL_IDS.put("ginkgo", 1018);
        MODEL_IDS.put("graminidites_bambusoides", 1019);
        MODEL_IDS.put("hippurites_radiosus", 1020);
        MODEL_IDS.put("ladinia_simplex", 1021);
        MODEL_IDS.put("largestipule_leather_root", 1022);
        MODEL_IDS.put("liriodendrites", 1023);
        MODEL_IDS.put("lithostrotion", 1024);
        MODEL_IDS.put("magnolia", 1025);
        MODEL_IDS.put("orontium_mackii", 1026);
        MODEL_IDS.put("phoenix", 1027);
        MODEL_IDS.put("psaronius", 1028);
        MODEL_IDS.put("raphaelia", 1029);
        MODEL_IDS.put("rhacophyton", 1030);
        MODEL_IDS.put("rhamnus_salicifolius", 1031);
        MODEL_IDS.put("scaly_tree_fern", 1032);
        MODEL_IDS.put("serenna_veriformans", 1033);
        MODEL_IDS.put("small_chain_fern", 1034);
        MODEL_IDS.put("small_fern", 1035);
        MODEL_IDS.put("small_royal_fern", 1036);
        MODEL_IDS.put("stylophyllopsis", 1037);
        MODEL_IDS.put("tempskya", 1038);
        MODEL_IDS.put("umaltolepis", 1039);
        MODEL_IDS.put("wild_onion", 1040);
        MODEL_IDS.put("wild_potato", 1041);
        MODEL_IDS.put("woolly_stalked_begonia", 1042);
        MODEL_IDS.put("small_cycad", 1043);

        // MODELS: IMPORTANT CHANGE → no "item/" prefix in path.
        // Minecraft will look in: assets/jurassicreborn/models/item/<path>.json

        MODELS.put(1,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/achillobator"));
        MODELS.put(2,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/alligator_gar"));
        MODELS.put(3,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/allosaurus"));
        MODELS.put(4,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/alvarezsaurus"));
        MODELS.put(5,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/ankylodocus"));
        MODELS.put(6,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/ankylosaurus"));
        MODELS.put(7,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/apatosaurus"));
        MODELS.put(8,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/arsinoitherium"));
        MODELS.put(9,   new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/asteroceras"));
        MODELS.put(10,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/baryonyx"));
        MODELS.put(11,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/beelzebufo"));
        MODELS.put(12,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/blue"));
        MODELS.put(13,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/brachiosaurus"));
        MODELS.put(14,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/calymene"));
        MODELS.put(15,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/camarasaurus"));
        MODELS.put(16,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/cameroceras"));
        MODELS.put(17,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/carcharodontosaurus"));
        MODELS.put(18,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/carnotaurus"));
        MODELS.put(19,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/cearadactylus"));
        MODELS.put(20,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/ceratosaurus"));
        MODELS.put(21,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/charlie"));
        MODELS.put(22,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/chasmosaurus"));
        MODELS.put(23,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/chilesaurus"));
        MODELS.put(24,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/coelacanth"));
        MODELS.put(25,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/coelurus"));
        MODELS.put(26,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/compsognathus"));
        MODELS.put(27,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/corythosaurus"));
        MODELS.put(28,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/crassigyrinus"));
        MODELS.put(29,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/deinosuchus"));
        MODELS.put(30,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/deinotherium"));
        MODELS.put(31,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/delta"));
        MODELS.put(32,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/dilophosaurus"));
        MODELS.put(33,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/dimetrodon"));
        MODELS.put(34,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/dimorphodon"));
        MODELS.put(35,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/diplocaulus"));
        MODELS.put(36,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/diplodocus"));
        MODELS.put(37,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/dodo"));
        MODELS.put(38,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/dreadnoughtus"));
        MODELS.put(39,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/dunkleosteus"));
        MODELS.put(40,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/echo"));
        MODELS.put(41,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/edmontosaurus"));
        MODELS.put(42,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/elasmotherium"));
        MODELS.put(43,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/endoceras"));
        MODELS.put(44,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/gallimimus"));
        MODELS.put(45,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/giganotosaurus"));
        MODELS.put(46,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/guanlong"));
        MODELS.put(48,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/herrerasaurus"));
        MODELS.put(49,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/hyaenodon"));
        MODELS.put(50,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/hypsilophodon"));
        MODELS.put(51,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/iguanodon"));
        MODELS.put(52,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/indominus"));
        MODELS.put(53,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/indoraptor"));
        MODELS.put(54,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/kairuku"));
        MODELS.put(55,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/lambeosaurus"));
        MODELS.put(56,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/leaellynasaura"));
        MODELS.put(57,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/leptictidium"));
        MODELS.put(58,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/livyatan"));
        MODELS.put(59,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/ludodactylus"));
        MODELS.put(60,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/maiasaura"));
        MODELS.put(61,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/majungasaurus"));
        MODELS.put(62,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/mamenchisaurus"));
        MODELS.put(63,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/mammoth"));
        MODELS.put(64,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/mawsonia"));
        MODELS.put(65,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/megalodon"));
        MODELS.put(66,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/megapiranha"));
        MODELS.put(67,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/megatherium"));
        MODELS.put(68,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/metriacanthosaurus"));
        MODELS.put(69,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/microceratus"));
        MODELS.put(70,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/microraptor"));
        MODELS.put(71,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/moganopterus"));
        MODELS.put(72,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/mosasaurus"));
        MODELS.put(73,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/mussaurus"));
        MODELS.put(74,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/nigersaurus"));
        MODELS.put(75,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/ornithomimus"));
        MODELS.put(76,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/orthoceras"));
        MODELS.put(77,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/othnielia"));
        MODELS.put(78,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/oviraptor"));
        MODELS.put(79,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/pachycephalosaurus"));
        MODELS.put(80,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/paraceratherium"));
        MODELS.put(81,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/parapuzosia"));
        MODELS.put(82,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/parasaurolophus"));
        MODELS.put(83,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/patagotitan"));
        MODELS.put(84,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/perisphinctes"));
        MODELS.put(85,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/postosuchus"));
        MODELS.put(86,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/proceratosaurus"));
        MODELS.put(87,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/protoceratops"));
        MODELS.put(88,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/pteranodon"));
        MODELS.put(89,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/quetzalcoatlus"));
        MODELS.put(90,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/raphusrex"));
        MODELS.put(91,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/rugops"));
        MODELS.put(92,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/segisaurus"));
        MODELS.put(93,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/sinoceratops"));
        MODELS.put(94,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/smilodon"));
        MODELS.put(95,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/spinoraptor"));
        MODELS.put(96,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/spinosaurus"));
        MODELS.put(97,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/stegosaurus"));
        MODELS.put(98,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/styracosaurus"));
        MODELS.put(99,  new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/suchomimus"));
        MODELS.put(100, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/therizinosaurus"));
        MODELS.put(101, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/titanis"));
        MODELS.put(102, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/titanites"));
        MODELS.put(103, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/triceratops"));
        MODELS.put(104, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/troodon"));
        MODELS.put(105, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/tropeognathus"));
        MODELS.put(106, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/tylosaurus"));
        MODELS.put(107, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/tyrannosaurus"));
        MODELS.put(108, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/vectipelta"));
        MODELS.put(109, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/velociraptor"));
        MODELS.put(110, new ResourceLocation("jurassicreborn", "storage_disc/dinosaurs/zhenyuanopterus"));

        MODELS.put(1001, new ResourceLocation("jurassicreborn", "storage_disc/plants/ajuginucula_smithii"));
        MODELS.put(1002, new ResourceLocation("jurassicreborn", "storage_disc/plants/araucaria"));
        MODELS.put(1003, new ResourceLocation("jurassicreborn", "storage_disc/plants/aulopora"));
        MODELS.put(1004, new ResourceLocation("jurassicreborn", "storage_disc/plants/bennettitalean"));
        MODELS.put(1005, new ResourceLocation("jurassicreborn", "storage_disc/plants/bennettitalean_cycadeoidea"));
        MODELS.put(1006, new ResourceLocation("jurassicreborn", "storage_disc/plants/bristle_fern"));
        MODELS.put(1007, new ResourceLocation("jurassicreborn", "storage_disc/plants/calamites"));
        MODELS.put(1008, new ResourceLocation("jurassicreborn", "storage_disc/plants/cinnamon_fern"));
        MODELS.put(1009, new ResourceLocation("jurassicreborn", "storage_disc/plants/cladochonus"));
        MODELS.put(1010, new ResourceLocation("jurassicreborn", "storage_disc/plants/cry_pansy"));
        MODELS.put(1011, new ResourceLocation("jurassicreborn", "storage_disc/plants/cycad"));
        MODELS.put(1012, new ResourceLocation("jurassicreborn", "storage_disc/plants/cycad_zamites"));
        MODELS.put(1013, new ResourceLocation("jurassicreborn", "storage_disc/plants/dicksonia"));
        MODELS.put(1014, new ResourceLocation("jurassicreborn", "storage_disc/plants/dicroidium_zuberi"));
        MODELS.put(1015, new ResourceLocation("jurassicreborn", "storage_disc/plants/dictyophyllum"));
        MODELS.put(1016, new ResourceLocation("jurassicreborn", "storage_disc/plants/enallhelia"));
        MODELS.put(1017, new ResourceLocation("jurassicreborn", "storage_disc/plants/encephalartos"));
        MODELS.put(1018, new ResourceLocation("jurassicreborn", "storage_disc/plants/ginkgo"));
        MODELS.put(1019, new ResourceLocation("jurassicreborn", "storage_disc/plants/graminidites_bambusoides"));
        MODELS.put(1020, new ResourceLocation("jurassicreborn", "storage_disc/plants/hippurites_radiosus"));
        MODELS.put(1021, new ResourceLocation("jurassicreborn", "storage_disc/plants/ladinia_simplex"));
        MODELS.put(1022, new ResourceLocation("jurassicreborn", "storage_disc/plants/largestipule_leather_root"));
        MODELS.put(1023, new ResourceLocation("jurassicreborn", "storage_disc/plants/liriodendrites"));
        MODELS.put(1024, new ResourceLocation("jurassicreborn", "storage_disc/plants/lithostrotion"));
        MODELS.put(1025, new ResourceLocation("jurassicreborn", "storage_disc/plants/magnolia"));
        MODELS.put(1026, new ResourceLocation("jurassicreborn", "storage_disc/plants/orontium_mackii"));
        MODELS.put(1027, new ResourceLocation("jurassicreborn", "storage_disc/plants/phoenix"));
        MODELS.put(1028, new ResourceLocation("jurassicreborn", "storage_disc/plants/psaronius"));
        MODELS.put(1029, new ResourceLocation("jurassicreborn", "storage_disc/plants/raphaelia"));
        MODELS.put(1030, new ResourceLocation("jurassicreborn", "storage_disc/plants/rhacophyton"));
        MODELS.put(1031, new ResourceLocation("jurassicreborn", "storage_disc/plants/rhamnus_salicifolius"));
        MODELS.put(1032, new ResourceLocation("jurassicreborn", "storage_disc/plants/scaly_tree_fern"));
        MODELS.put(1033, new ResourceLocation("jurassicreborn", "storage_disc/plants/serenna_veriformans"));
        MODELS.put(1034, new ResourceLocation("jurassicreborn", "storage_disc/plants/small_chain_fern"));
        MODELS.put(1035, new ResourceLocation("jurassicreborn", "storage_disc/plants/small_fern"));
        MODELS.put(1036, new ResourceLocation("jurassicreborn", "storage_disc/plants/small_royal_fern"));
        MODELS.put(1037, new ResourceLocation("jurassicreborn", "storage_disc/plants/stylophyllopsis"));
        MODELS.put(1038, new ResourceLocation("jurassicreborn", "storage_disc/plants/tempskya"));
        MODELS.put(1039, new ResourceLocation("jurassicreborn", "storage_disc/plants/umaltolepis"));
        MODELS.put(1040, new ResourceLocation("jurassicreborn", "storage_disc/plants/wild_onion"));
        MODELS.put(1041, new ResourceLocation("jurassicreborn", "storage_disc/plants/wild_potato"));
        MODELS.put(1042, new ResourceLocation("jurassicreborn", "storage_disc/plants/woolly_stalked_begonia"));
        MODELS.put(1043, new ResourceLocation("jurassicreborn", "storage_disc/plants/small_cycad"));
    }

    private StorageDiscModelData() {}

    public static int resolveDinosaur(Dinosaur dinosaur) {
        if (dinosaur == null || dinosaur == Dinosaur.EMPTY) {
            return 0;
        }
        String formatted = normalize(dinosaur.getFormattedName());
        if (formatted != null) {
            int id = MODEL_IDS.getInt(formatted);
            if (id != 0) return id;
        }
        String name = normalize(dinosaur.getName());
        if (name != null) {
            return MODEL_IDS.getInt(name);
        }
        return 0;
    }

    public static int resolvePlant(ResourceLocation plantId) {
        if (plantId == null) return 0;
        String key = normalize(plantId.getPath());
        return key == null ? 0 : MODEL_IDS.getInt(key);
    }

    private static String normalize(String value) {
        if (value == null || value.isEmpty()) return null;
        return value.toLowerCase(Locale.ROOT).replace(' ', '_');
    }

    public static ResourceLocation getModelLocation(int modelId) {
        return MODELS.get(modelId);
    }

    public static Int2ObjectMap<ResourceLocation> getModels() {
        return MODELS;
    }
}