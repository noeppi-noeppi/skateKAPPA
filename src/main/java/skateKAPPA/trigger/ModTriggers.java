package skateKAPPA.trigger;

import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers {

    public static final DernikSheepTrigger DERNIK_SHEEP = new DernikSheepTrigger();
    
    public static void register() {
        CriteriaTriggers.register(DERNIK_SHEEP);
    }
}
