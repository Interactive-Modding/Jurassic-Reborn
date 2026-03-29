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

        // MODELS: use the "item/" prefix so the loader resolves
        // assets/jurassicreborn/models/item/<path>.json

        MODELS.put(1,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/achillobator"));
        MODELS.put(2,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/alligator_gar"));
        MODELS.put(3,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/allosaurus"));
        MODELS.put(4,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/alvarezsaurus"));
        MODELS.put(5,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/ankylodocus"));
        MODELS.put(6,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/ankylosaurus"));
        MODELS.put(7,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/apatosaurus"));
        MODELS.put(8,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/arsinoitherium"));
        MODELS.put(9,   ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/asteroceras"));
        MODELS.put(10,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/baryonyx"));
        MODELS.put(11,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/beelzebufo"));
        MODELS.put(12,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/blue"));
        MODELS.put(13,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/brachiosaurus"));
        MODELS.put(14,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/calymene"));
        MODELS.put(15,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/camarasaurus"));
        MODELS.put(16,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/cameroceras"));
        MODELS.put(17,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/carcharodontosaurus"));
        MODELS.put(18,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/carnotaurus"));
        MODELS.put(19,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/cearadactylus"));
        MODELS.put(20,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/ceratosaurus"));
        MODELS.put(21,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/charlie"));
        MODELS.put(22,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/chasmosaurus"));
        MODELS.put(23,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/chilesaurus"));
        MODELS.put(24,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/coelacanth"));
        MODELS.put(25,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/coelurus"));
        MODELS.put(26,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/compsognathus"));
        MODELS.put(27,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/corythosaurus"));
        MODELS.put(28,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/crassigyrinus"));
        MODELS.put(29,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/deinosuchus"));
        MODELS.put(30,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/deinotherium"));
        MODELS.put(31,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/delta"));
        MODELS.put(32,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/dilophosaurus"));
        MODELS.put(33,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/dimetrodon"));
        MODELS.put(34,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/dimorphodon"));
        MODELS.put(35,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/diplocaulus"));
        MODELS.put(36,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/diplodocus"));
        MODELS.put(37,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/dodo"));
        MODELS.put(38,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/dreadnoughtus"));
        MODELS.put(39,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/dunkleosteus"));
        MODELS.put(40,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/echo"));
        MODELS.put(41,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/edmontosaurus"));
        MODELS.put(42,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/elasmotherium"));
        MODELS.put(43,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/endoceras"));
        MODELS.put(44,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/gallimimus"));
        MODELS.put(45,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/giganotosaurus"));
        MODELS.put(46,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/guanlong"));
        MODELS.put(48,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/herrerasaurus"));
        MODELS.put(49,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/hyaenodon"));
        MODELS.put(50,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/hypsilophodon"));
        MODELS.put(51,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/iguanodon"));
        MODELS.put(52,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/indominus"));
        MODELS.put(53,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/indoraptor"));
        MODELS.put(54,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/kairuku"));
        MODELS.put(55,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/lambeosaurus"));
        MODELS.put(56,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/leaellynasaura"));
        MODELS.put(57,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/leptictidium"));
        MODELS.put(58,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/livyatan"));
        MODELS.put(59,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/ludodactylus"));
        MODELS.put(60,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/maiasaura"));
        MODELS.put(61,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/majungasaurus"));
        MODELS.put(62,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/mamenchisaurus"));
        MODELS.put(63,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/mammoth"));
        MODELS.put(64,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/mawsonia"));
        MODELS.put(65,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/megalodon"));
        MODELS.put(66,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/megapiranha"));
        MODELS.put(67,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/megatherium"));
        MODELS.put(68,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/metriacanthosaurus"));
        MODELS.put(69,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/microceratus"));
        MODELS.put(70,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/microraptor"));
        MODELS.put(71,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/moganopterus"));
        MODELS.put(72,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/mosasaurus"));
        MODELS.put(73,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/mussaurus"));
        MODELS.put(74,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/nigersaurus"));
        MODELS.put(75,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/ornithomimus"));
        MODELS.put(76,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/orthoceras"));
        MODELS.put(77,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/othnielia"));
        MODELS.put(78,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/oviraptor"));
        MODELS.put(79,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/pachycephalosaurus"));
        MODELS.put(80,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/paraceratherium"));
        MODELS.put(81,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/parapuzosia"));
        MODELS.put(82,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/parasaurolophus"));
        MODELS.put(83,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/patagotitan"));
        MODELS.put(84,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/perisphinctes"));
        MODELS.put(85,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/postosuchus"));
        MODELS.put(86,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/proceratosaurus"));
        MODELS.put(87,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/protoceratops"));
        MODELS.put(88,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/pteranodon"));
        MODELS.put(89,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/quetzalcoatlus"));
        MODELS.put(90,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/raphusrex"));
        MODELS.put(91,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/rugops"));
        MODELS.put(92,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/segisaurus"));
        MODELS.put(93,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/sinoceratops"));
        MODELS.put(94,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/smilodon"));
        MODELS.put(95,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/spinoraptor"));
        MODELS.put(96,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/spinosaurus"));
        MODELS.put(97,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/stegosaurus"));
        MODELS.put(98,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/styracosaurus"));
        MODELS.put(99,  ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/suchomimus"));
        MODELS.put(100, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/therizinosaurus"));
        MODELS.put(101, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/titanis"));
        MODELS.put(102, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/titanites"));
        MODELS.put(103, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/triceratops"));
        MODELS.put(104, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/troodon"));
        MODELS.put(105, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/tropeognathus"));
        MODELS.put(106, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/tylosaurus"));
        MODELS.put(107, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/tyrannosaurus"));
        MODELS.put(108, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/vectipelta"));
        MODELS.put(109, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/velociraptor"));
        MODELS.put(110, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/dinosaurs/zhenyuanopterus"));

        MODELS.put(1001, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/ajuginucula_smithii"));
        MODELS.put(1002, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/araucaria"));
        MODELS.put(1003, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/aulopora"));
        MODELS.put(1004, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/bennettitalean"));
        MODELS.put(1005, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/bennettitalean_cycadeoidea"));
        MODELS.put(1006, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/bristle_fern"));
        MODELS.put(1007, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/calamites"));
        MODELS.put(1008, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/cinnamon_fern"));
        MODELS.put(1009, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/cladochonus"));
        MODELS.put(1010, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/cry_pansy"));
        MODELS.put(1011, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/cycad"));
        MODELS.put(1012, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/cycad_zamites"));
        MODELS.put(1013, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/dicksonia"));
        MODELS.put(1014, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/dicroidium_zuberi"));
        MODELS.put(1015, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/dictyophyllum"));
        MODELS.put(1016, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/enallhelia"));
        MODELS.put(1017, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/encephalartos"));
        MODELS.put(1018, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/ginkgo"));
        MODELS.put(1019, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/graminidites_bambusoides"));
        MODELS.put(1020, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/hippurites_radiosus"));
        MODELS.put(1021, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/ladinia_simplex"));
        MODELS.put(1022, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/largestipule_leather_root"));
        MODELS.put(1023, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/liriodendrites"));
        MODELS.put(1024, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/lithostrotion"));
        MODELS.put(1025, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/magnolia"));
        MODELS.put(1026, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/orontium_mackii"));
        MODELS.put(1027, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/phoenix"));
        MODELS.put(1028, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/psaronius"));
        MODELS.put(1029, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/raphaelia"));
        MODELS.put(1030, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/rhacophyton"));
        MODELS.put(1031, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/rhamnus_salicifolius"));
        MODELS.put(1032, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/scaly_tree_fern"));
        MODELS.put(1033, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/serenna_veriformans"));
        MODELS.put(1034, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/small_chain_fern"));
        MODELS.put(1035, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/small_fern"));
        MODELS.put(1036, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/small_royal_fern"));
        MODELS.put(1037, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/stylophyllopsis"));
        MODELS.put(1038, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/tempskya"));
        MODELS.put(1039, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/umaltolepis"));
        MODELS.put(1040, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/wild_onion"));
        MODELS.put(1041, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/wild_potato"));
        MODELS.put(1042, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/woolly_stalked_begonia"));
        MODELS.put(1043, ResourceLocation.fromNamespaceAndPath("jurassicreborn", "item/storage_disc/plants/small_cycad"));
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
