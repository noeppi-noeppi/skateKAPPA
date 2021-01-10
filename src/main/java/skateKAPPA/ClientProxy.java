package skateKAPPA;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;

public class ClientProxy extends CommonProxy {
    
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
}
