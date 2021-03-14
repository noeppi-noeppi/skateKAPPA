package skateKAPPA;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;
import java.util.function.Supplier;

public class ClientProxy extends CommonProxy {
    
    private static ModelBase parent = null;
    private static ModelRenderer headModel = null;
    private static ModelRenderer invisibleModel = null;
    
    public void registerTranslationHandler() {
        super.registerTranslationHandler();
        try {
            ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(resourceManager -> updateTranslations());
        } catch (ClassCastException e) {
            //
        }
    }
    
    private void updateTranslations() {
        //noinspection deprecation
        try {
            Locale locale = ObfuscationReflectionHelper.getPrivateValue(I18n.class, null, "field_135054_a");
            Map<String, String> translationsMap = ObfuscationReflectionHelper.getPrivateValue(Locale.class, locale, "field_135032_a");
            translationsMap.replaceAll((k, v) -> SkateKAPPA.replaceStr(v));
        } catch (ReflectionHelper.UnableToAccessFieldException | ClassCastException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
        this.updateServerTranslations();
    }

    @SubscribeEvent
    public void renderEntityPre(RenderLivingEvent.Pre<?> event) {
        if (event.getEntity() instanceof EntityCow && event.getRenderer() instanceof RenderCow && event.getRenderer().getMainModel() instanceof ModelCow) {
            if ((event.getEntity().getUniqueID().getMostSignificantBits() & (1 << 5)) != 0) {
                ModelCow model = (ModelCow) event.getRenderer().getMainModel();
                if (invisibleModel == null || parent != model) {
                    parent = model;
                    invisibleModel = new NothingRenderer(model);
                }        
                headModel = model.head;
                model.head = invisibleModel;
            }
        }
    }
    
    @SubscribeEvent
    public void renderEntityPost(RenderLivingEvent.Post<?> event) {
        if (event.getEntity() instanceof EntityCow && event.getRenderer() instanceof RenderCow && event.getRenderer().getMainModel() instanceof ModelCow) {
            ModelCow model = (ModelCow) event.getRenderer().getMainModel();
            if (model.head instanceof NothingRenderer && headModel != null) {
                model.head = headModel;
            }
        }
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(SkateKAPPA.MODID + ":" + id, "inventory"));
    }

    @Override
    public <T extends Entity> void registerEntityRender(Class<T> entityClass, IRenderFactory<? super T> renderFactory) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
    }

    @Override
    public <T extends TileEntity> void registerTileRender(Class<T> tileClass, Supplier<TileEntitySpecialRenderer<T>> tesr) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, tesr.get());
    }

    @Override
    public void registerColorItem(IItemColor color, Item item) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, item);
    }

    @Override
    public void registerColorBlock(IBlockColor color, Block block) {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, block);
    }
}
