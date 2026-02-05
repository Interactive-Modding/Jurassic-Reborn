package net.vit.jurassicreborn.common.entities.Dinosaurs;

import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList.*;
import net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid;
import net.vit.jurassicreborn.common.util.TimePeriod;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur.*;

public class DinosaurHandler {
    public static boolean inited = false;
    public static String getName(int id) {
        Dinosaur d = getById(id);
        return d != EMPTY
                ? d.getName().toLowerCase(Locale.ROOT)
                : "empty";
    }

    public static List<Integer> getRegisteredIds() {
        return Dinosaur.DINOSAURS.keySet().intStream()
                .filter(id -> DinosaurHandler.getById(id) != Dinosaur.EMPTY)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
    }
    public static final Dinosaur BRACHIOSAURUS = new BrachiosaurusDinosaur();
    public static final Dinosaur DODO = new DodoDinosaur();
    public static final Dinosaur ACHILLOBATOR = new AchillobatorDinosaur();
    public static final Dinosaur ANKYLOSAURUS = new AnkylosaurusDinosaur();
    public static final Dinosaur CARNOTAURUS = new CarnotaurusDinosaur();
    public static final Dinosaur COELACANTH = new CoelacanthDinosaur();
    public static final Dinosaur COMPSOGNATHUS = new CompsognathusDinosaur();
    public static final Dinosaur DUNKLEOSTEUS = new DunkleosteusDinosaur();
    public static final Dinosaur GIGANOTOSAURUS = new GiganotosaurusDinosaur();
    public static final Dinosaur HYPSILOPHODON = new HypsilophodonDinosaur();
    public static final Dinosaur INDOMINUS = new IndominusDinosaur();
    public static final Dinosaur MAJUNGASAURUS = new MajungasaurusDinosaur();
    public static final Dinosaur PTERANODON = new PteranodonDinosaur();
    public static final Dinosaur RUGOPS = new RugopsDinosaur();
    public static final Dinosaur SEGISAURUS = new SegisaurusDinosaur();
    public static final Dinosaur SPINOSAURUS = new SpinosaurusDinosaur();
    public static final Dinosaur LEPTICTIDIUM = new LeptictidiumDinosaur();
    public static final Dinosaur MICROCERATUS = new MicroceratusDinosaur();
    public static final Dinosaur APATOSAURUS = new ApatosaurusDinosaur();
    public static final Dinosaur OTHNIELIA = new OthnieliaDinosaur();
    public static final Dinosaur DIMORPHODON = new DimorphodonDinosaur();
    public static final Dinosaur TYLOSAURUS = new TylosaurusDinosaur();
    public static final Dinosaur LUDODACTYLUS = new LudodactylusDinosaur();
    public static final Dinosaur PROTOCERATOPS = new ProtoceratopsDinosaur();
    public static final Dinosaur TROPEOGNATHUS = new TropeognathusDinosaur();
    public static final Dinosaur LEAELLYNASAURA = new LeaellynasauraDinosaur();
    public static final Dinosaur HERRERASAURUS = new HerrerasaurusDinosaur();
    public static final Dinosaur BLUE = new VelociraptorSquad.VelociraptorBlueDinosaur();
    public static final Dinosaur DELTA = new VelociraptorSquad.VelociraptorDeltaDinosaur();
    public static final Dinosaur CHARLIE = new VelociraptorSquad.VelociraptorCharlieDinosaur();
    public static final Dinosaur ECHO = new VelociraptorSquad.VelociraptorEchoDinosaur();
    public static final Dinosaur THERIZINOSAURUS = new TherizinosaurusDinosaur();
    public static final Dinosaur MEGAPIRANHA = new MegapiranhaDinosaur();
    public static final Dinosaur BARYONYX = new BaryonyxDinosaur();
    public static final Dinosaur CEARADACTYLUS = new CearadactylusDinosaur();
    public static final Dinosaur MAMENCHISAURUS = new MamenchisaurusDinosaur();
    public static final Dinosaur CHASMOSAURUS = new ChasmosaurusDinosaur();
    public static final Dinosaur CORYTHOSAURUS = new CorythosaurusDinosaur();
    public static final Dinosaur EDMONTOSAURUS = new EdmontosaurusDinosaur();
    public static final Dinosaur LAMBEOSAURUS = new LambeosaurusDinosaur();
    public static final Dinosaur METRIACANTHOSAURUS = new MetriacanthosaurusDinosaur();
    public static final Dinosaur MOGANOPTERUS = new MoganopterusDinosaur();
    public static final Dinosaur ORNITHOMIMUS = new OrnithomimusDinosaur();
    public static final Dinosaur ZHENYUANOPTERUS = new ZhenyuanopterusDinosaur();
    public static final Dinosaur TROODON = new TroodonDinosaur();
    public static final Dinosaur PACHYCEPHALOSAURUS = new PachycephalosaurusDinosaur();
    public static final Dinosaur DILOPHOSAURUS = new DilophosaurusDinosaur();
    public static final Dinosaur GALLIMIMUS = new GallimimusDinosaur();
    public static final Dinosaur PARASAUROLOPHUS = new ParasaurolophusDinosaur();
    public static final Dinosaur MICRORAPTOR = new MicroraptorDinosaur();
    public static final Dinosaur MUSSAURUS = new MussaurusDinosaur();
    public static final Dinosaur TRICERATOPS = new TriceratopsDinosaur();
    public static final Dinosaur TYRANNOSAURUS = new TyrannosaurusDinosaur();
    public static final Dinosaur VELOCIRAPTOR = new VelociraptorDinosaur();
    public static final Dinosaur ALLIGATOR_GAR = new AlligatorGarDinosaur();
    public static final Dinosaur STEGOSAURUS = new StegosaurusDinosaur();
    public static final Dinosaur OVIRAPTOR = new OviraptorDinosaur();
    public static final Dinosaur MOSASAURUS = new MosasaurusDinosaur();
    public static final Dinosaur ALVAREZSAURUS = new AlvarezsaurusDinosaur();
    public static final Dinosaur BEELZEBUFO = new BeelzebufoDinosaur();
    public static final Dinosaur CERATOSAURUS = new CeratosaurusDinosaur();
    public static final Dinosaur PROCERATOSAURUS = new ProceratosaurusDinosaur();
    public static final Dinosaur CARCHARODONTOSAURUS = new CarcharodontosaurusDinosaur();
    public static final Dinosaur CHILESAURUS = new ChilesaurusDinosaur();
    public static final Dinosaur CRASSIGYRINUS = new CrassigyrinusDinosaur();
    public static final Dinosaur DIPLOCAULUS = new DiplocaulusDinosaur();
    public static final Dinosaur GUANLONG = new GuanlongDinosaur();
    public static final Dinosaur HYAENODON = new HyaenodonDinosaur();
    public static final Dinosaur PERISPHINCTES = new PerisphinctesDinosaur();
    public static final Dinosaur POSTOSUCHUS = new PostosuchusDinosaur();
    public static final Dinosaur STYRACOSAURUS = new StyracosaurusDinosaur();
    public static final Dinosaur SUCHOMIMUS = new SuchomimusDinosaur();
    public static final Dinosaur ALLOSAURUS = new AllosaurusDinosaur();
    public static final Dinosaur MAMMOTH = new MammothDinosaur();
    public static final Dinosaur QUETZAL = new QuetzalDinosaur();
    public static final Dinosaur COELURUS = new CoelurusDinosaur();
    public static final Dinosaur MAWSONIA = new MawsoniaDinosaur();
    public static final Dinosaur INDORAPTOR = new IndoraptorDinosaur();
    public static final Dinosaur DREADNOUGHTUS = new DreadnoughtusDinosaur();
    public static final Dinosaur SINOCERATOPS = new SinoceratopsDinosaur();
    public static final Dinosaur ARSINOITHERIUM = new ArsinoitheriumDinosaur();
    public static final Dinosaur DEINOTHERIUM = new DeinotheriumDinosaur();
    public static final Dinosaur ELASMOTHERIUM = new ElasmotheriumDinosaur();
    public static final Dinosaur MEGATHERIUM= new MegatheriumDinosaur();
    public static final Dinosaur SMILODON= new SmilodonDinosaur();
    public static final Dinosaur RAPHUSREX= new RaphusrexDinosaur();
    public static final Dinosaur TITANIS= new TitanisDinosaur();
    public static final Dinosaur SPINORAPTOR=new SpinoraptorDinosaur();
    public static final Dinosaur DIPLODOCUS=new DiplodocusDinosaur();
    public static final Dinosaur ANKYLODOCUS=new AnkylodocusDinosaur();
    public static final Dinosaur CAMARASAURUS=new CamarasaurusDinosaur();
    public static final Dinosaur DIMETRODON=new DimetrodonDinosaur();
    public static final Dinosaur ASTEROCERAS=new AsterocerasDinosaur();
    public static final Dinosaur TITANITES=new TitanitesDinosaur();
    public static final Dinosaur PARAPUZOSIA=new ParapuzosiaDinosaur();
    public static final Dinosaur VECTIPELTA=new VectipeltaDinosaur();
    public static final Dinosaur PARACERATHERIUM=new ParaceratheriumDinosaur();
    public static final Dinosaur CAMEROCERAS=new CamerocerasDinosaur();
    public static final Dinosaur ORTHOCERAS=new OrthocerasDinosaur();
    public static final Dinosaur ENDOCERAS=new EndocerasDinosaur();
    public static final Dinosaur CALYMENE=new CalymeneDinosaur();
    public static final Dinosaur LIVYATAN=new LivyatanDinosaur();
    public static final Dinosaur MEGALODON=new MegalodonDinosaur();
    public static final Dinosaur NIGERSAURUS=new NigersaurusDinosaur();
    public static final Dinosaur KAIRUKU=new KairukuDinosaur();
    public static final Dinosaur DEINOSUCHUS=new DeinosuchusDinosaur();
    public static final Dinosaur MAIASAURA = new MaiasauraDinosaur();
    public static final Dinosaur PATAGOTITAN = new PatagotitanDinosaur();




    public static void doDinosInit(){
        if (inited) return;
        inited = true;

        registerDinosaur(0, VELOCIRAPTOR);
        registerDinosaur(2, COELACANTH);
        registerDinosaur(3, MICRORAPTOR);
        registerDinosaur(4, BRACHIOSAURUS);
        registerDinosaur(5, MUSSAURUS);
        registerDinosaur(6, ACHILLOBATOR);
        registerDinosaur(7, ANKYLOSAURUS);
        registerDinosaur(8, DILOPHOSAURUS);
        registerDinosaur(9, COMPSOGNATHUS);
        registerDinosaur(10, GALLIMIMUS);
        registerDinosaur(11, CARNOTAURUS);
        registerDinosaur(12, DUNKLEOSTEUS);
        registerDinosaur(13, GIGANOTOSAURUS);
        registerDinosaur(14, PARASAUROLOPHUS);
        registerDinosaur(15, INDOMINUS);
        registerDinosaur(16, MAJUNGASAURUS);
        registerDinosaur(17, PTERANODON);
        registerDinosaur(18, RUGOPS);
        registerDinosaur(19, SEGISAURUS);
        registerDinosaur(20, TRICERATOPS);
        registerDinosaur(21, TYRANNOSAURUS);
        registerDinosaur(22, ALLIGATOR_GAR);
        registerDinosaur(23, STEGOSAURUS);
        registerDinosaur(24, SPINOSAURUS);
        registerDinosaur(25, HYPSILOPHODON);
        registerDinosaur(26, DODO);
        registerDinosaur(27, LEPTICTIDIUM);
        registerDinosaur(28, MICROCERATUS);
        registerDinosaur(29, APATOSAURUS);
        registerDinosaur(30, OTHNIELIA);
        registerDinosaur(31, DIMORPHODON);
        registerDinosaur(32, TYLOSAURUS);
        registerDinosaur(33, LUDODACTYLUS);
        registerDinosaur(34, PROTOCERATOPS);
        registerDinosaur(35, TROPEOGNATHUS);
        registerDinosaur(36, LEAELLYNASAURA);
        registerDinosaur(37, HERRERASAURUS);
        registerDinosaur(38, BLUE);
        registerDinosaur(39, CHARLIE);
        registerDinosaur(40, DELTA);
        registerDinosaur(41, ECHO);
        registerDinosaur(42, THERIZINOSAURUS);
        registerDinosaur(43, MEGAPIRANHA);
        registerDinosaur(44, BARYONYX);
        registerDinosaur(45, CEARADACTYLUS);
        registerDinosaur(46, MAMENCHISAURUS);
        registerDinosaur(47, CHASMOSAURUS);
        registerDinosaur(48, CORYTHOSAURUS);
        registerDinosaur(49, EDMONTOSAURUS);
        registerDinosaur(50, LAMBEOSAURUS);
        registerDinosaur(51, METRIACANTHOSAURUS);
        registerDinosaur(52, MOGANOPTERUS);
        registerDinosaur(53, ORNITHOMIMUS);
        registerDinosaur(54, ZHENYUANOPTERUS);
        registerDinosaur(55, TROODON);
        registerDinosaur(56, PACHYCEPHALOSAURUS);
        registerDinosaur(57, OVIRAPTOR);
        registerDinosaur(58, MOSASAURUS);
        registerDinosaur(59, ALVAREZSAURUS);
        registerDinosaur(60, BEELZEBUFO);
        registerDinosaur(61, CERATOSAURUS);
        registerDinosaur(62, PROCERATOSAURUS);
        registerDinosaur(63, CARCHARODONTOSAURUS);
        registerDinosaur(64, CHILESAURUS);
        registerDinosaur(65, CRASSIGYRINUS);
        registerDinosaur(66, DIPLOCAULUS);
        registerDinosaur(67, GUANLONG);
        registerDinosaur(68, HYAENODON);
        registerDinosaur(69, PERISPHINCTES);
        registerDinosaur(70, POSTOSUCHUS);
        registerDinosaur(71, STYRACOSAURUS);
        registerDinosaur(72, SUCHOMIMUS);
        registerDinosaur(73, ALLOSAURUS);
        registerDinosaur(74, MAMMOTH);
        registerDinosaur(75, QUETZAL);
        registerDinosaur(76, COELURUS);
        registerDinosaur(77, MAWSONIA);
        registerDinosaur(78, INDORAPTOR);
        registerDinosaur(79, DREADNOUGHTUS);
        registerDinosaur(80, SINOCERATOPS);
        registerDinosaur(81, ARSINOITHERIUM);
        registerDinosaur(82, DEINOTHERIUM);
        registerDinosaur(83, ELASMOTHERIUM);
        registerDinosaur(84, MEGATHERIUM);
        registerDinosaur(85, SMILODON);
        registerDinosaur(86, RAPHUSREX);
        registerDinosaur(87, TITANIS);
        registerDinosaur(88, SPINORAPTOR);
        registerDinosaur(89, DIPLODOCUS);
        registerDinosaur(90, ANKYLODOCUS);
        registerDinosaur(91, CAMARASAURUS);
        registerDinosaur(92, DIMETRODON);
        registerDinosaur(93, ASTEROCERAS);
        registerDinosaur(94, TITANITES);
        registerDinosaur(95, PARAPUZOSIA);
        registerDinosaur(96, VECTIPELTA);
        registerDinosaur(97, PARACERATHERIUM);
        registerDinosaur(98, CAMEROCERAS);
        registerDinosaur(99, ORTHOCERAS);
        registerDinosaur(100, ENDOCERAS);
        registerDinosaur(101, CALYMENE);
        registerDinosaur(102, LIVYATAN);
        registerDinosaur(103, MEGALODON);
        registerDinosaur(104, NIGERSAURUS);
        registerDinosaur(105, KAIRUKU);
        registerDinosaur(106, DEINOSUCHUS);
        registerDinosaur(107, MAIASAURA);
        registerDinosaur(108, PATAGOTITAN);


        Dinosaur.DINOS.forEach((dino) -> {
            if(dino != Dinosaur.EMPTY) {
                dino.init();


                if (!(dino instanceof Hybrid)) {
                    TimePeriod period = dino.getPeriod();
                    List<Dinosaur> periods = DINOSAURS_BY_PERIOD_MAP.computeIfAbsent(period, k -> new LinkedList<>());
                    periods.add(dino);
                }



            }
        });


    }
    public static List<Dinosaur> getDinosaursFromAmber() {

        return DINOS.stream().filter((dino) -> dino != EMPTY).filter(Dinosaur::shouldRegister).filter((dino) -> !dino.isMarineCreature() && !dino.isHybrid).collect(Collectors.toList());
    }
    public static List<Dinosaur> getMarineCreatures() {

        return DINOS.stream().filter((dino) -> dino.isMarineCreature() && !dino.isHybrid()).collect(Collectors.toList());
    }
    public static List<Dinosaur> getMammalCreatures() {
        return DINOS.stream().filter((dino) -> dino.isMammal() && !dino.isHybrid()).collect(Collectors.toList());
    }
    public static Dinosaur getById(int id) {
        Dinosaur d = DINOSAURS.get(id);
        return d != null ? d : EMPTY;
    }
    public static int getId(Dinosaur dino) {
        return Dinosaur.DINOSAUR_IDS.getOrDefault(dino, 0);
    }
    public static int count() {
        int highest = -1;
        for (int id : DINOSAURS.keySet()) {
            if (id > highest) {
                highest = id;
            }
        }
        return highest + 1;
    }

}
