package skateKAPPA.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import java.util.function.Supplier;

public class CommonProxy {

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
