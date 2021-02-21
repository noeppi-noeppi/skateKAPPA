package skateKAPPA;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;

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
        if (event.getEntity() instanceof EntityCow && event.getRenderer().getMainModel() instanceof ModelCow) {
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
        if (event.getEntity() instanceof EntityCow && event.getRenderer().getMainModel() instanceof ModelCow) {
            ModelCow model = (ModelCow) event.getRenderer().getMainModel();
            if (model.head instanceof NothingRenderer) {
                model.head = headModel;
            }
        }
    }
}
