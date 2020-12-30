package mod.reborn.server.dinosaur;

import mod.reborn.server.api.Hybrid;
import mod.reborn.server.entity.dinosaur.VelociraptorBlueEntity;
import mod.reborn.server.entity.dinosaur.VelociraptorCharlieEntity;
import mod.reborn.server.entity.dinosaur.VelociraptorDeltaEntity;
import mod.reborn.server.entity.dinosaur.VelociraptorEchoEntity;
import net.minecraft.util.ResourceLocation;

public class VelociraptorSquad {
    private ResourceLocation texture;
    public static class VelociraptorBlueDinosaur extends VelociraptorDinosaur implements Hybrid
    {
        public VelociraptorBlueDinosaur()
        {
            super();

            this.setName("Blue");
            this.setDinosaurClass(VelociraptorBlueEntity.class);
            this.setEggColorMale(0x5A5752, 0x32D3E55);
            this.setEggColorFemale(0x5A5752, 0x32D3E55);
            this.setOverlayCount(0);
            this.enableSkeleton();
            this.setHybrid();
        }

        @Override
        public Class[] getDinosaurs()
        {
            return new Class[] { VelociraptorDinosaur.class, VelociraptorCharlieDinosaur.class};
        }

        @Override
        protected void doSkeletonCheck(){}
    }
    public static class VelociraptorCharlieDinosaur extends VelociraptorDinosaur implements Hybrid
    {
        public VelociraptorCharlieDinosaur()
        {
            super();

            this.setName("Charlie");
            this.setDinosaurClass(VelociraptorCharlieEntity.class);
            this.setEggColorMale(0x525637, 0x2C2F24);
            this.setEggColorFemale(0x525637, 0x2C2F24);
            this.setOverlayCount(0);
            this.enableSkeleton();
            this.setHybrid();
        }

        @Override
        public Class[] getDinosaurs()
        {
            return new Class[] { VelociraptorDinosaur.class };
        }

        @Override
        protected void doSkeletonCheck(){}
    }
    public static class VelociraptorDeltaDinosaur extends VelociraptorDinosaur implements Hybrid
    {
        public VelociraptorDeltaDinosaur()
        {
            super();

            this.setName("Delta");
            this.setDinosaurClass(VelociraptorDeltaEntity.class);
            this.setEggColorMale(0x526353, 0x3D4F40);
            this.setEggColorFemale(0x526353, 0x3D4F40);
            this.setOverlayCount(0);
            this.enableSkeleton();
            this.setHybrid();
        }

        @Override
        public Class[] getDinosaurs()
        {
            return new Class[] { VelociraptorDinosaur.class, VelociraptorBlueDinosaur.class };
        }

        @Override
        protected void doSkeletonCheck(){}
    }
    public static class VelociraptorEchoDinosaur extends VelociraptorDinosaur implements Hybrid
    {
        public VelociraptorEchoDinosaur()
        {
            super();

            this.setName("Echo");
            this.setDinosaurClass(VelociraptorEchoEntity.class);
            this.setEggColorMale(0x665941, 0x363E43);
            this.setEggColorFemale(0x665941, 0x363E43);
            this.setOverlayCount(0);
            this.enableSkeleton();
            this.setHybrid();
        }

        @Override
        public Class[] getDinosaurs()
        {
            return new Class[] { VelociraptorDinosaur.class, VelociraptorDeltaDinosaur.class };
        }

        @Override
        protected void doSkeletonCheck(){}
    }
}

