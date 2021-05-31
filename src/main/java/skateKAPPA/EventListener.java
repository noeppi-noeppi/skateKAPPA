package skateKAPPA;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
}
