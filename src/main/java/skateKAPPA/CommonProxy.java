package skateKAPPA;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;
import java.util.function.Supplier;

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
    
    public void registerItemRenderer(Item item, int meta, String id) {
        //
    }

    public <T extends Entity> void registerEntityRender(Class<T> entityClass, IRenderFactory<? super T> renderFactory) {
        //
    }

    public <T extends TileEntity> void registerTileRender(Class<T> tileClass, Supplier<TileEntitySpecialRenderer<T>> tesr) {
        //
    }

    public void registerColorItem(IItemColor color, Item item) {

    }

    public void registerColorItem(IItemColor color, Block block) {
        registerColorItem(color, Item.getItemFromBlock(block));
    }


    public void registerColorBlock(IBlockColor color, Block block) {

    }
}
