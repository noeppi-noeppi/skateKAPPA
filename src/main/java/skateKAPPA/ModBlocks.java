package skateKAPPA;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import skateKAPPA.base.BlockBase;

public class ModBlocks {

    public static final BlockBase rainbowWool = new BlockBase(Blocks.WOOL, "rainbow_wool").setHardness(0.8f).setSoundType(SoundType.CLOTH);
    
    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                rainbowWool
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                rainbowWool.createItemBlock()
        );
    }

    public static void registerModels() {
        rainbowWool.registerItemModel();
    }
}
