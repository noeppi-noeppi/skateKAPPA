package skateKAPPA;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
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
        } else if (event.getEntity() instanceof EntitySheep && event.getRenderer() instanceof RenderSheep) {
            RenderSheep sheep = (RenderSheep) event.getRenderer();
            List<LayerRenderer<EntitySheep>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, sheep, "field_177097_h");
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i) instanceof LayerSheepWool && !(layers.get(i) instanceof NiklasWoolLayer)) {
                    layers.set(i, new NiklasWoolLayer(sheep));
                }
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

    @SubscribeEvent
    public void guiChange(GuiOpenEvent event) {
        // Old GUI is still in the Minecraft class
        GuiScreen old = Minecraft.getMinecraft().currentScreen;
        if (old instanceof GuiEditSign) {
            TileEntitySign tile = ObfuscationReflectionHelper.getPrivateValue(GuiEditSign.class, (GuiEditSign) old, "field_146848_f");
            for (int i = 0; i < tile.signText.length; i++) {
                if (tile.signText[i] != null) {
                    tile.signText[i] = new TextComponentString(tile.signText[i].getFormattedText()
                            .replaceAll("(?i)lampen ?blöcke", "Lampenbögen")
                            .replaceAll("(?i)lamp ?blocks", "lamp bows")
                    );
                }
            }
        } else if (old != null) {
            try {
                Class<?> quarkGuiClass = Class.forName("vazkii.quark.client.gui.GuiBetterEditSign");
                if (quarkGuiClass.isAssignableFrom(old.getClass())) {
                    Field field = quarkGuiClass.getDeclaredField("sign");
                    field.setAccessible(true);
                    TileEntitySign tile = (TileEntitySign) field.get(old);
                    for (int i = 0; i < tile.signText.length; i++) {
                        if (tile.signText[i] != null) {
                            tile.signText[i] = new TextComponentString(tile.signText[i].getFormattedText()
                                    .replaceAll("(?i)lampen ?blöcke", "Lampenbögen")
                                    .replaceAll("(?i)lamp ?blocks", "lamp bows")
                            );
                        }
                    }
                }
            } catch (ReflectiveOperationException | ClassCastException | NoClassDefFoundError e) {
                //
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemTooltip(ItemTooltipEvent event) {
        if (!event.getToolTip().isEmpty()) {
            event.getToolTip().set(0, SkateKAPPA.replaceStr(event.getToolTip().get(0)));
        }
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(SkateKAPPA.MODID + ":" + id, "inventory"));
    }
}
