package skateKAPPA;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemTooltip(ItemTooltipEvent event) {
        if (!event.getToolTip().isEmpty()) {
            event.getToolTip().set(0, SkateKAPPA.replaceStr(event.getToolTip().get(0)));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void serverChat(ServerChatEvent event) {
        ITextComponent tc = event.getComponent();
        String str = tc.getFormattedText();
        String rep = SkateKAPPA.replaceStr(str);
        if (!rep.equalsIgnoreCase(str)) {
            event.setComponent(new TextComponentString(rep));
        }
    }
}
