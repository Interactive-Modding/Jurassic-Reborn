package mod.reborn.client.render.item;

import net.minecraftforge.fml.common.Mod;
import mod.reborn.RebornMod;
import mod.reborn.client.render.item.models.GuiItemModelWrapper;
import mod.reborn.server.item.ItemHandler;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid= RebornMod.MODID, value=Side.CLIENT)
public class EventHandler {
    
    public static IBakedModel DART_GUN_GUI;
    public static IBakedModel GLOCK_GUI;
    public static IBakedModel REMINGTON_GUI;
    public static IBakedModel SPAS12_GUI;
    public static IBakedModel UTAS_UTS_15;
    
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent event) {
	DART_GUN_GUI = getModel(new ResourceLocation(RebornMod.MODID, "item/dart_gun_gui"), event.getMap());
	GLOCK_GUI = getModel(new ResourceLocation(RebornMod.MODID, "item/dart_gun_gui"), event.getMap());
	REMINGTON_GUI = getModel(new ResourceLocation(RebornMod.MODID, "item/dart_gun_gui"), event.getMap());
	SPAS12_GUI = getModel(new ResourceLocation(RebornMod.MODID, "item/dart_gun_gui"), event.getMap());
	UTAS_UTS_15 = getModel(new ResourceLocation(RebornMod.MODID, "item/dart_gun_gui"), event.getMap());
    }
    
    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event) {
        for(ModelResourceLocation mrl : event.getModelRegistry().getKeys()) {
            if(mrl.getVariant().equals("inventory")) {
                ResourceLocation location = new ResourceLocation(mrl.getResourceDomain(), mrl.getResourcePath());
                if (location.equals(ItemHandler.DART_GUN.getRegistryName())) {
                    event.getModelRegistry().putObject(mrl, new GuiItemModelWrapper(event.getModelRegistry().getObject(mrl), DART_GUN_GUI));
                }
                if (location.equals(ItemHandler.GLOCK.getRegistryName())) {
                    event.getModelRegistry().putObject(mrl, new GuiItemModelWrapper(event.getModelRegistry().getObject(mrl), GLOCK_GUI));
                }
                if (location.equals(ItemHandler.REMINGTON.getRegistryName())) {
                    event.getModelRegistry().putObject(mrl, new GuiItemModelWrapper(event.getModelRegistry().getObject(mrl), REMINGTON_GUI));
                }
                if (location.equals(ItemHandler.SPAS_12.getRegistryName())) {
                    event.getModelRegistry().putObject(mrl, new GuiItemModelWrapper(event.getModelRegistry().getObject(mrl), SPAS12_GUI));
                }
                if (location.equals(ItemHandler.UTS15.getRegistryName())) {
                    event.getModelRegistry().putObject(mrl, new GuiItemModelWrapper(event.getModelRegistry().getObject(mrl), UTAS_UTS_15));
                }
            }
        }
    }
    private static IBakedModel getModel(ResourceLocation resourceLocation, TextureMap map) {
	IModel model;
	try {
	    model = ModelLoaderRegistry.getModel(resourceLocation);
	} catch (Exception e) {
	    e.printStackTrace();
	    model = ModelLoaderRegistry.getMissingModel();
	}
	return model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, map::registerSprite);
    }
}
