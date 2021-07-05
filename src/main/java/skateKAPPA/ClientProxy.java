package skateKAPPA;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            event.getToolTip().set(0, SkateKAPPA.replaceExtended(event.getToolTip().get(0)));
            if (event.getToolTip().get(0).toLowerCase(java.util.Locale.ROOT).contains("kexs")) {
                event.getToolTip().add(I18n.format("skatekappa.tooltip_kexs"));
            }
        }
    }

    private static final ResourceLocation MANAGLASS_TEXTURE = new ResourceLocation(SkateKAPPA.MODID, "textures/managlass_notice.png");

    private static final Set<ResourceLocation> MANAGLASS_ITEMS = ImmutableSet.of(
            new ResourceLocation("botania", "managlass"),
            new ResourceLocation("botania", "managlasspane")
    );

    @SubscribeEvent
    public void renderScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            GuiScreen gui = event.getGui();
            if (gui instanceof GuiContainer) {
                GuiContainer container = (GuiContainer) gui;
                for (Slot slot : container.inventorySlots.inventorySlots) {
                    if (slot.getHasStack() && MANAGLASS_ITEMS.contains(slot.getStack().getItem().getRegistryName())) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(0, 0, 1000);
                        GlStateManager.color(1, 1, 1, 1);
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(MANAGLASS_TEXTURE);
                        Gui.drawScaledCustomSizeModalRect(((container.width - container.getXSize()) / 2) + slot.xPos - 32, ((container.height - container.getYSize()) / 2) + slot.yPos - 48, 0, 0, 128, 128, 48, 48, 128, 128);
                        GlStateManager.disableBlend();
                        GlStateManager.popMatrix();
                    }
                }
            }
            try {
                Class<?> jeiGui = Class.forName("mezz.jei.gui.recipes.RecipesGui");
                if (jeiGui.isAssignableFrom(gui.getClass())) {
                    Field recipeLayoutField = jeiGui.getDeclaredField("recipeLayouts");
                    recipeLayoutField.setAccessible(true);
                    List<?> layouts = (List<?>) recipeLayoutField.get(gui);
                    if (!layouts.isEmpty()) {
                        Class<?> jeiLayout = Class.forName("mezz.jei.gui.recipes.RecipeLayout");
                        Class<?> jeiGroup = Class.forName("mezz.jei.gui.ingredients.GuiIngredientGroup");
                        Class<?> jeiIngredient = Class.forName("mezz.jei.gui.ingredients.GuiIngredient");
                        Field xField = jeiLayout.getDeclaredField("posX");
                        Field yField = jeiLayout.getDeclaredField("posY");
                        Field groupField = jeiLayout.getDeclaredField("guiItemStackGroup");
                        Field ingredientsField = jeiGroup.getDeclaredField("guiIngredients");
                        Method currentIngredientMethod = jeiIngredient.getDeclaredMethod("getDisplayedIngredient");
                        Field rectField = jeiIngredient.getDeclaredField("rect");
                        Field xOffset = jeiIngredient.getDeclaredField("xPadding");
                        Field yOffset = jeiIngredient.getDeclaredField("yPadding");
                        xField.setAccessible(true);
                        yField.setAccessible(true);
                        groupField.setAccessible(true);
                        ingredientsField.setAccessible(true);
                        currentIngredientMethod.setAccessible(true);
                        rectField.setAccessible(true);
                        xOffset.setAccessible(true);
                        yOffset.setAccessible(true);
                        for (Object layout : layouts) {
                            int xBase = (Integer) xField.get(layout);
                            int yBase = (Integer) yField.get(layout);
                            Object group = groupField.get(layout);
                            Map<?, ?> ingredients = (Map<?, ?>) ingredientsField.get(group);
                            for (Object ingredient : ingredients.values()) {
                                ItemStack stack = (ItemStack) currentIngredientMethod.invoke(ingredient);
                                if (stack != null && !stack.isEmpty() & MANAGLASS_ITEMS.contains(stack.getItem().getRegistryName())) {
                                    Rectangle rect = (Rectangle) rectField.get(ingredient);
                                    int xPadding = (Integer) xOffset.get(ingredient);
                                    int yPadding = (Integer) yOffset.get(ingredient);
                                    GlStateManager.pushMatrix();
                                    GlStateManager.translate(0, 0, 1000);
                                    GlStateManager.color(1, 1, 1, 1);
                                    GlStateManager.enableBlend();
                                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                                    Minecraft.getMinecraft().getTextureManager().bindTexture(MANAGLASS_TEXTURE);
                                    Gui.drawScaledCustomSizeModalRect(xBase + xPadding + (int) rect.getX() - 32, yBase + yPadding + (int) rect.getY() - 48, 0, 0, 128, 128, 48, 48, 128, 128);
                                    GlStateManager.disableBlend();
                                    GlStateManager.popMatrix();
                                }
                            }
                        }
                    }
                }
            } catch (Exception | NoClassDefFoundError e) {
                //
            }
        }
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(SkateKAPPA.MODID + ":" + id, "inventory"));
    }
}
