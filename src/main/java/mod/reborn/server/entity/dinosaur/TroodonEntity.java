package mod.reborn.server.entity.dinosaur;

import mod.reborn.server.damage.DinosaurDamageSource;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class TroodonEntity extends DinosaurEntity
{
    public TroodonEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, AchillobatorEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, GallimimusEntity.class, GuanlongEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MussaurusEntity.class, MicroraptorEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, ZhenyuanopterusEntity.class, MoganopterusEntity.class);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
    }

    public boolean attackEntityAsMob(Entity entity){
        float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (entity.attackEntityFrom(new DinosaurDamageSource("mob", this), damage)) {
            if (entity instanceof EntityLivingBase) {
                int i = rand.nextInt(3);
                if(i == 0) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 15, 10));
                } else if(i == 1) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 15, 10));
                }else if(i == 2) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 15, 10));
                }
                return true;
            }
        }
        return super.attackEntityAsMob(entity);
    }
}
