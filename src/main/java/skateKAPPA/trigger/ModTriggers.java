package skateKAPPA.trigger;

import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers {

    public static final DernikSheepTrigger DERNIK_SHEEP = new DernikSheepTrigger();
    public static final LogiSnakeTrigger LOGISNAKE = new LogiSnakeTrigger();
    
    public static void register() {
        CriteriaTriggers.register(DERNIK_SHEEP);
        CriteriaTriggers.register(LOGISNAKE);
    }
}
