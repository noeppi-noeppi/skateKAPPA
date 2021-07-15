package skateKAPPA;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import skateKAPPA.trigger.ModTriggers;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventListener {
    
    private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    
    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.world instanceof WorldServer && event.player instanceof EntityPlayerMP) {
            WorldServer world = (WorldServer) event.player.world;
            executor.schedule(() -> {
                try {
                    if (GithubQuery.checkMerged("sebinside", "LogiSnake", 1)
                            || GithubQuery.checkMerged("sebinside", "LogiSnake", 2)
                            || GithubQuery.checkMerged("sebinside", "LogiSnake", 3)) {
                        // Pull request got merged, grant the advancement on the main thread now.
                        MinecraftServer server = world.getMinecraftServer();
                        if (server != null) {
                            server.addScheduledTask(() -> ModTriggers.LOGISNAKE.trigger((EntityPlayerMP) event.player));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1, TimeUnit.MILLISECONDS);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void serverChat(ServerChatEvent event) {
        ITextComponent tc = event.getComponent();
        String str = tc.getFormattedText();
        String rep = SkateKAPPA.replaceExtended(str);
        if (!rep.equalsIgnoreCase(str)) {
            event.setComponent(new TextComponentString(rep));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void entityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.getWorld().isRemote && event.getTarget() instanceof EntitySheep
                && ((EntitySheep) event.getTarget()).getFleeceColor() == EnumDyeColor.RED) {
            ItemStack stack = event.getItemStack();
            if (!stack.isEmpty() && stack.getItem() == Items.NAME_TAG && stack.hasDisplayName()) {
                String text = stack.getDisplayName();
                if (text.equalsIgnoreCase("derniklaas") && event.getEntityPlayer() instanceof EntityPlayerMP) {
                    ModTriggers.DERNIK_SHEEP.trigger((EntityPlayerMP) event.getEntityPlayer());
                } else if (text.equalsIgnoreCase("Wolli") || text.equalsIgnoreCase("Wolli47")
                        || text.equalsIgnoreCase("Wolli 47")) {
                    event.getEntityPlayer().sendStatusMessage(new TextComponentString("Aber wer ist eigentlich Wolli47 ?"), false);
                }
            }
        }
    }
}
