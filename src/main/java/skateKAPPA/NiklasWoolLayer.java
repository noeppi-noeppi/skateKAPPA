package skateKAPPA;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class NiklasWoolLayer extends LayerSheepWool {

    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/sheep/sheep_fur.png");
    private static final ResourceLocation TEXTURE_SUNGLASSES = new ResourceLocation(SkateKAPPA.MODID, "textures/entity/sheep/glasses.png");
    private final RenderSheep sheepRenderer;
    private final ModelSheep1 sheepModel = new ModelSheep1();

    public NiklasWoolLayer(RenderSheep sheepRendererIn) {
        super(sheepRendererIn);
        this.sheepRenderer = sheepRendererIn;
    }

    @Override
    public void doRenderLayer(EntitySheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float age, float headYaw, float headPitch, float scale) {
        if (sheep.hasCustomName() && "derniklaas".equalsIgnoreCase(sheep.getCustomNameTag())) {
            if (!sheep.getSheared() && !sheep.isInvisible()) {
                this.sheepRenderer.bindTexture(TEXTURE);
                float hue = (((Math.abs(sheep.getUniqueID().getLeastSignificantBits()) % 100) + (float) (sheep.ticksExisted % 100) + partialTicks) % 100) / 100f;
                int rgb = Color.HSBtoRGB(hue, 0.7f, 0.7f);
                float r = ((rgb >>> 16) & 0xFF) / 255f;
                float g = ((rgb >>> 8) & 0xFF) / 255f;
                float b = ((rgb) & 0xFF) / 255f;
                GlStateManager.color(r, g, b);
                this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
                this.sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
                this.sheepModel.render(sheep, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
            }
            if (!sheep.isInvisible()) {
                this.sheepRenderer.bindTexture(TEXTURE_SUNGLASSES);
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.01, 1.01, 1.01);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.sheepRenderer.getMainModel().render(sheep, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        } else {
            super.doRenderLayer(sheep, limbSwing, limbSwingAmount, partialTicks, age, headYaw, headPitch, scale);
        }

    }
}
