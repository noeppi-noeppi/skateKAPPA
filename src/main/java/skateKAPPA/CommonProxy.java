package skateKAPPA;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;

public class CommonProxy {
    
    public void registerTranslationHandler() {
        updateServerTranslations();
    }
    
    protected void updateServerTranslations() {
        //noinspection deprecation
        try {
            LanguageMap lmap1 = ObfuscationReflectionHelper.getPrivateValue(I18n.class, null, "field_74839_a");
            LanguageMap lmap2 = ObfuscationReflectionHelper.getPrivateValue(I18n.class, null, "field_150828_b");
            
            modifyLanguageMap(lmap1);
            modifyLanguageMap(lmap2);
        } catch (ReflectionHelper.UnableToAccessFieldException | ClassCastException | NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }
    
    private void modifyLanguageMap(LanguageMap lmap) {
        Map<String, String> translationsMap = ObfuscationReflectionHelper.getPrivateValue(LanguageMap.class, lmap, "field_74816_c");
        translationsMap.replaceAll((k, v) -> SkateKAPPA.replaceStr(v));
    }
}
