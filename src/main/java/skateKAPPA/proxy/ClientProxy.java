package skateKAPPA.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import skateKAPPA.SkateKAPPA;

import java.util.function.Supplier;

public class ClientProxy extends CommonProxy {

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
