package skateKAPPA;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import skateKAPPA.trigger.ModTriggers;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventListener {

    private static final UUID SKATE = UUID.fromString("78a049a7-e802-4b26-a8d7-8021052e637f");
    private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.world instanceof WorldServer && event.player instanceof EntityPlayerMP && SKATE.equals(event.player.getGameProfile().getId())) {
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
        if (!event.getWorld().isRemote && event.getTarget() instanceof EntitySheep) {
            ItemStack stack = event.getItemStack();
            if (!stack.isEmpty()) {
                if (stack.getItem() == Items.NAME_TAG && stack.hasDisplayName()) {
                    String text = stack.getDisplayName();
                    if (text.equalsIgnoreCase("Wolli") || text.equalsIgnoreCase("Wolli47")
                            || text.equalsIgnoreCase("Wolli 47")) {
                        if (((EntitySheep) event.getTarget()).getFleeceColor() == EnumDyeColor.RED) {
                            event.getEntityPlayer().sendStatusMessage(new TextComponentString("Aber wer ist eigentlich Wolli47 ?"), false);
                        }
                    }
                } else if (stack.getItem() instanceof ItemShears) {
                    if (event.getTarget().hasCustomName() && "derniklaas".equalsIgnoreCase(event.getTarget().getCustomNameTag())
                            && !((EntitySheep) event.getTarget()).getSheared() && event.getEntityPlayer() instanceof EntityPlayerMP) {
                        ModTriggers.DERNIK_SHEEP.trigger((EntityPlayerMP) event.getEntityPlayer());
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void levelChange(PlayerPickupXpEvent event) {
        if (!event.getEntityPlayer().world.isRemote && event.getEntityPlayer() instanceof EntityPlayerMP) {
            if (event.getEntityPlayer().world.provider.getDimensionType() == DimensionType.NETHER) {
                int xp = event.getOrb().getXpValue();
                float xpBefore = event.getEntityPlayer().experience;
                int levelBefore = event.getEntityPlayer().experienceLevel;
                int totalBefore = event.getEntityPlayer().experienceTotal;
                event.getEntityPlayer().addExperience(xp);
                int levelAfter = event.getEntityPlayer().experienceLevel;
                event.getEntityPlayer().experience = xpBefore;
                event.getEntityPlayer().experienceLevel = levelBefore;
                event.getEntityPlayer().experienceTotal = totalBefore;
                if (levelBefore != 66 && levelAfter == 66) {
                    ModTriggers.NETHER66.trigger((EntityPlayerMP) event.getEntityPlayer());
                }
            }
        }
    }
}
