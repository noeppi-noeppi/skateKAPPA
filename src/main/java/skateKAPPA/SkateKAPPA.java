package skateKAPPA;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SkateKAPPA.MODID, name = SkateKAPPA.MODNAME, version = SkateKAPPA.VERSION)
public class SkateKAPPA {

    public static final String MODID = "skatekappa";
    public static final String MODNAME = "skateKAPPA";
    public static final String VERSION = "1.0.0";
    
    @Mod.Instance
    public static SkateKAPPA instance;

    @SidedProxy(serverSide = "skateKAPPA.CommonProxy", clientSide = "skateKAPPA.ClientProxy")
    public static CommonProxy proxy;
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventListener());
        proxy.registerTranslationHandler();
    }
    
    public static String replaceStr(String str) {
        return str.replaceAll("(?i)copper", "Kappa")
                .replaceAll("(?i)kupfer", "Kappa")
                .replaceAll("(?i)melon", "Melan")
                .replaceAll("(?i)dandelion", "Dandeldideldon");
    }
}
