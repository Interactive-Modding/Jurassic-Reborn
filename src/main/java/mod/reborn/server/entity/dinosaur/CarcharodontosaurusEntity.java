package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CarcharodontosaurusEntity extends DinosaurEntity {

    public CarcharodontosaurusEntity(World world) {
        super(world);
        this.target(AchillobatorEntity.class, CamarasaurusEntity.class, AnkylodocusEntity.class, DiplodocusEntity.class, SpinoraptorEntity.class, RaphusrexEntity.class, TitanisEntity.class, SmilodonEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, DeinotheriumEntity.class, ArsinoitheriumEntity.class, SinoceratopsEntity.class, AlligatorGarEntity.class, AllosaurusEntity.class, AlvarezsaurusEntity.class, AnkylosaurusEntity.class, ApatosaurusEntity.class, BaryonyxEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class, CarnotaurusEntity.class, CeratosaurusEntity.class, CearadactylusEntity.class, ChasmosaurusEntity.class, ChilesaurusEntity.class, CoelacanthEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CorythosaurusEntity.class, CrassigyrinusEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, IndominusEntity.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, GiganotosaurusEntity.class, HerrerasaurusEntity.class, MajungasaurusEntity.class, MetriacanthosaurusEntity.class, MammothEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, PostosuchusEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, RugopsEntity.class, QuetzalEntity.class, SegisaurusEntity.class, SpinosaurusEntity.class, StegosaurusEntity.class, StyracosaurusEntity.class, SuchomimusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class, TyrannosaurusEntity.class, TroodonEntity.class, TropeognathusEntity.class, VelociraptorEntity.class, ZhenyuanopterusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class);
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.CARCHARODONTOSAURUS_LIVING;
            case HISSING:
                return SoundHandler.CARCHARODONTOSAURUS_HISS;
            case DYING:
                return SoundHandler.CARCHARODONTOSAURUS_HURT;
            case INJURED:
                return SoundHandler.CARCHARODONTOSAURUS_HURT;
            case ATTACKING:
                return SoundHandler.CARCHARODONTOSAURUS_GROWL;
            case ROARING:
                return SoundHandler.CARCHARODONTOSAURUS_ROAR;
            default:
                return null;
        }
    }
}
