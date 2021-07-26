package skateKAPPA;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skateKAPPA.trigger.ModTriggers;

@Mod(modid = SkateKAPPA.MODID, name = SkateKAPPA.MODNAME, version = SkateKAPPA.VERSION, dependencies = "required-after:forge@[14.23.5.2855,)")
public class SkateKAPPA {

    public static final Gson GSON;
    static {
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.disableHtmlEscaping();
        gsonbuilder.setLenient();
        GSON = gsonbuilder.create();
    }
    
    public static final String MODID = "skatekappa";
    public static final String MODNAME = "skateKAPPA";
    public static final String VERSION = "1.0.13";
    
    @Mod.Instance
    public static SkateKAPPA instance;

    @SidedProxy(serverSide = "skateKAPPA.CommonProxy", clientSide = "skateKAPPA.ClientProxy")
    public static CommonProxy proxy;
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(new EventListener());
        Recipes.register();
        ModTriggers.register();
        proxy.registerTranslationHandler();
    }
    
    public static String replaceExtended(String str) {
        return replaceStr(str)
                .replaceAll("(?i)cookies", "KeXs")
                .replaceAll("(?i)cookie", "KeXs")
                .replaceAll("(?i)kekse", "KeXs")
                .replaceAll("(?i)keks", "KeXs");
    }
    
    public static String replaceStr(String str) {
        return str.replaceAll("(?i)copper", "Kappa")
                .replaceAll("(?i)kupfer", "Kappa")
                .replaceAll("(?i)melon", "Melan")
                .replaceAll("(?i)dandelion", "Dandeldideldon");
    }
    
    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }
    }
}
