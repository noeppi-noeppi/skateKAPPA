package skateKAPPA;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static final BanPowder banPowder = new BanPowder("banpowder");

    
    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                banPowder
        );
    }

    public static void registerModels() {
        banPowder.registerItemModel();
    }
}