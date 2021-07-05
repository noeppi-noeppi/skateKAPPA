package skateKAPPA;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import skateKAPPA.trigger.ModTriggers;

public class EventListener {
    
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
