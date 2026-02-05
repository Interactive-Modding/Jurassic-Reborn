//package net.vit.jurassicreborn.client.render.entity.animation;
//
//
//import com.github.alexthe666.citadel.animation.Animation;
//import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
//import net.vit.jurassicreborn.JurassicReborn;
//import net.vit.jurassicreborn.client.model.AnimatableModel;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.DistExecutor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class SafeClientMethods {
//
//    static SafeClientMethods INSTANCE = new SafeClientMethods();
//
//    public long ticks = 0;
//    public SafeClientMethods(){}
//
//    static DistExecutor.SafeCallable<PoseHandler.ModelData> loadModelDataForClient(List<String> posedModelResources, Map<Animation, float[][]> animations){
//        return () -> loadModelDataClient(posedModelResources, animations);
//    }
//
//    public static DistExecutor.SafeCallable<Long> getClientTicks(){
//        return () -> INSTANCE.ticks;
//    }
//
//    public static DistExecutor.SafeCallable<Void> addClientTick(){
//        return () -> {
//
////                System.out.println("AAAAAAAAAAAAA");
//            INSTANCE.ticks += 1;
//
//            return null;
//        };
//    }
//
//    public static PoseHandler.ModelData loadModelDataClient(List<String> posedModelResources, Map<Animation, float[][]> animations) {
//        PosedCuboid[][] posedCuboids = new PosedCuboid[posedModelResources.size()][];
//        AnimatableModel mainModel = JabelarAnimationHandler.loadModel(posedModelResources.get(0));
//        if (mainModel == null) {
//            throw new IllegalArgumentException("Couldn't load the model from " + posedModelResources.get(0));
//        }
//        String[] identifiers = mainModel.getCubeIdentifierArray();
////        System.out.println("Trying to load: ");
////        System.out.println(Arrays.toString(identifiers));
//        int partCount = identifiers.length;
//        for (int i = 0; i < posedModelResources.size(); i++) {
//            String resource = posedModelResources.get(i);
//            AnimatableModel model = JabelarAnimationHandler.loadModel(resource);
//            if (model == null) {
//                throw new IllegalArgumentException("Couldn't load the model from " + resource);
//            }
//            PosedCuboid[] pose = new PosedCuboid[partCount];
//            ArrayList<String> failedToLoadBoxes = new ArrayList<>();
//            for (int partIndex = 0; partIndex < partCount; partIndex++) {
//
//                String identifier = identifiers[partIndex];
//                AdvancedModelBox cube = model.getCubeByIdentifier(identifier);
//                if (cube == null) {
//
//
//
//                    AdvancedModelBox mainCube = mainModel.getCubeByIdentifier(identifier);
//                    AdvancedModelBox cubeAttempt = mainModel.getCube(mainCube.boxName);
////                    if(JurassicReborn.INSTANCE.getEnv()) {//wtf???
//
//
////                    failedToLoadBoxes.add("Could not retrieve cube " + identifier + " (" + mainCube.boxName + ", " + partIndex + ") from the model " + resource);
//                    JurassicReborn.erroredIdentifiers.add(identifier);
////                    failedToLoadBoxes.add("\t->Retrieved cube " + cubeAttempt.boxName + " with identifier ");
////                    }
//                    pose[partIndex] = new PosedCuboid(mainCube);
//                } else {
//                    pose[partIndex] = new PosedCuboid(cube);
//                }
//
//
////                    File output = new File("~/Documents/c++ automation io/"+failedToLoadBoxes.get(0)+".txt");
////                    try {
////                        Files.createFile(Path.of(output.getPath()));
////                        PrintStream out = new PrintStream(output);
////                        failedToLoadBoxes.forEach((f) -> out.println(f));
////                    } catch (IOException e) {
////                        throw new RuntimeException(e);
////                    }
//
//
//
//            }
//            if(failedToLoadBoxes.size() > 0) {
//                JurassicReborn.getLogger().error("Could not retrieve cubes for model! see debug for more info");
//
//                for (String message :
//                        failedToLoadBoxes) {
//                    JurassicReborn.getLogger().error(message);
//
//                }
//            }
//            posedCuboids[i] = pose;
//        }
//        return new PoseHandler.ModelData(posedCuboids, animations);
//    }
//}
